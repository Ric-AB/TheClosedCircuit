package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.uploadproof

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.domain.budget.Budget
import com.closedcircuit.closedcircuitapplication.common.domain.budget.BudgetRepository
import com.closedcircuit.closedcircuitapplication.common.domain.model.File
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl
import com.closedcircuit.closedcircuitapplication.common.domain.util.getFirebaseDataObj
import com.closedcircuit.closedcircuitapplication.common.presentation.util.InputField
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import dev.gitlive.firebase.storage.StorageReference
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UploadProofViewModel(
    private val budgetID: ID,
    private val budgetRepository: BudgetRepository,
    private val imageStorageReference: StorageReference,
    private val ioDispatcher: CoroutineDispatcher
) : ScreenModel {

    val state = mutableStateOf(UploadProofUiState())

    private val _resultChannel = Channel<UploadProofResult>()
    val resultChannel = _resultChannel.receiveAsFlow()

    init {
        fetchBudget()
    }

    fun onEvent(event: UploadProofUiEvent) {
        when (event) {
            is UploadProofUiEvent.ImageAdded -> addImage(event.bytes)
            is UploadProofUiEvent.ImageRemoved -> removeImage(event.index)
            is UploadProofUiEvent.ProofDescriptionChange -> updateProofDescription(event.description)
            UploadProofUiEvent.Submit -> uploadProof()
        }
    }

    private fun removeImage(index: Int) {
        state.value.uploadItems.removeAt(index)
    }

    private fun updateProofDescription(description: String) {
        state.value.descriptionField.onValueChange(description)
    }

    private fun addImage(bytes: ByteArray) {
        val uploadItem = UploadItem(bytes)
        state.value.uploadItems.add(uploadItem)
    }

    private fun fetchBudget() {
        screenModelScope.launch {
            val budget = budgetRepository.getBudgetById(budgetID)
            state.value = state.value.copy(
                titleField = InputField(inputValue = mutableStateOf(budget.name)),
            )

            // fetch from api to get uploads
            state.value = state.value.copy(isLoadingUploads = true)
            budgetRepository.fetchBudgetById(budgetID).onSuccess {
                state.value = state.value.copy(
                    canEditUpload = !it.hasUploadedProof,
                    existingUploadedItems = it.uploadedItem().toImmutableList(),
                    isLoadingUploads = false
                )
            }
        }
    }

    private fun uploadProof() {
        screenModelScope.launch {
            state.value = state.value.copy(isLoading = true)
            val filesAndNamesPair = state.value.uploadItems.map {
                async(ioDispatcher) {
                    val fileName = "${ID.generateRandomUUID().value}.jpg"
                    val storage = imageStorageReference.child(fileName)
                    storage.putData(getFirebaseDataObj(it.bytes))

                    val downloadUrl = storage.getDownloadUrl()
                    val title = state.value.titleField.value
                    val description = state.value.descriptionField.value
                    val file = File(
                        url = ImageUrl(downloadUrl),
                        title = title,
                        description = description
                    )
                    Pair(fileName, file)
                }
            }.awaitAll()

            val files = filesAndNamesPair.map { (_, file) -> file }
            budgetRepository.uploadProof(budgetID, files).onSuccess {
                state.value = state.value.copy(isLoading = false)
                _resultChannel.send(UploadProofResult.UploadSuccess)
            }.onError { _, message ->
                val fileNames = filesAndNamesPair.map { (fileName, _) -> fileName }
                deleteUploadedFiles(fileNames)

                state.value = state.value.copy(isLoading = false)
                _resultChannel.send(UploadProofResult.UploadError(message))
            }
        }
    }

    private suspend fun deleteUploadedFiles(fileNames: List<String>) {
        withContext(ioDispatcher) {
            fileNames.map { fileName ->
                launch { imageStorageReference.child(fileName).delete() }
            }.joinAll()
        }
    }

    private fun Budget.uploadedItem() = this.proofs.map { UploadedItem(it.url.value) }
}