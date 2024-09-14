package com.closedcircuit.closedcircuitapplication.common.presentation.feature.profile.home

import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl
import com.closedcircuit.closedcircuitapplication.common.domain.plan.PlanRepository
import com.closedcircuit.closedcircuitapplication.common.domain.user.UserRepository
import com.closedcircuit.closedcircuitapplication.common.domain.util.getFirebaseDataObj
import com.closedcircuit.closedcircuitapplication.common.presentation.util.BaseScreenModel
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import dev.gitlive.firebase.storage.StorageReference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileTabViewModel(
    planRepository: PlanRepository,
    private val userRepository: UserRepository,
    private val imageStorageReference: StorageReference,
) : BaseScreenModel<ProfileUIState, ProfileTabResult>() {

    private val userFlow = userRepository.userFlow
    private val plansFlow = planRepository.getPlansAsFlow()
    private val loadingFlow = MutableStateFlow(false)

    val state: StateFlow<ProfileUIState?> =
        combine(userFlow.filterNotNull(), loadingFlow, plansFlow) { user, loading, _ ->
            ProfileUIState(
                imageUploadLoading = loading,
                avatar = user.avatar.value,
                firstName = user.firstName.value,
                fullName = user.fullName.value,
                email = user.email.value,
                phoneNumber = user.phoneNumber.phoneWithCode,
                country = user.country.name,
                isEmailVerified = user.isVerified,
                phoneNumberStatus = user.phoneNumberStatus,
                kycStatus = user.kycStatus,
            )
        }.stateIn(
            screenModelScope,
            SharingStarted.WhileSubscribed(),
            productInitialState()
        )

    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            is ProfileUiEvent.ProfileImageChange -> updateProfileAvatar(event.bytes)
        }
    }

    private fun updateProfileAvatar(bytes: ByteArray) {
        loadingFlow.value = true
        screenModelScope.launch {
            deleteCurrentProfileAvatar()
            val fileName = "${ID.generateRandomUUID().value}.jpg"
            val storage = imageStorageReference.child(fileName)
            storage.putData(getFirebaseDataObj(bytes))

            val imageUrl = storage.getDownloadUrl()
            val currentUser = userRepository.getCurrentUser()
            val updatedUser = currentUser.copy(avatar = ImageUrl(imageUrl))
            userRepository.updateUser(updatedUser).onSuccess {
                loadingFlow.value = false
            }.onError { _, message ->
                _resultChannel.send(ProfileTabResult.ProfileUpdateError(message))
            }
        }
    }

    private suspend fun deleteCurrentProfileAvatar() {
        val currentUserAvatar = userRepository.getCurrentUser().avatar.value

        // todo needs rework. need to store file name as well.
        val avatarStorageRef = imageStorageReference.listAll()
            .items
            .find { it.getDownloadUrl() == currentUserAvatar }

        avatarStorageRef?.delete()
    }

    private fun productInitialState(): ProfileUIState? {
        val user = userFlow.value ?: return null
        return ProfileUIState(
            imageUploadLoading = false,
            avatar = user.avatar.value,
            firstName = user.firstName.value,
            fullName = user.fullName.value,
            email = user.email.value,
            phoneNumber = user.phoneNumber.phoneWithCode,
            country = user.country.name,
            isEmailVerified = user.isVerified,
            phoneNumberStatus = user.phoneNumberStatus,
            kycStatus = user.kycStatus,
        )
    }
}