package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.uploadproof

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.closedcircuit.closedcircuitapplication.common.presentation.util.InputField
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class UploadProofUiState(
    val isLoading: Boolean = false,
    val titleField: InputField = InputField(),
    val descriptionField: InputField = InputField(),
    val isLoadingUploads: Boolean = false,
    val canEditUpload: Boolean = false,
    val uploadItems: SnapshotStateList<UploadItem> = SnapshotStateList(),
    val existingUploadedItems: ImmutableList<UploadedItem> = persistentListOf()
) {
    val canSubmitUpload: Boolean
        get() = descriptionField.value.isNotEmpty() && uploadItems.isNotEmpty()
}

data class UploadItem(
    val bytes: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as UploadItem

        return bytes.contentEquals(other.bytes)
    }

    override fun hashCode(): Int {
        return bytes.contentHashCode()
    }
}

data class UploadedItem(
    val url: String
)

sealed interface UploadProofResult {
    object UploadSuccess : UploadProofResult

    data class UploadError(val message: String) : UploadProofResult
}