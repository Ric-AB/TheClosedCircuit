package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.uploadproof

sealed interface UploadProofUiEvent {
    data class ImageAdded(val bytes: ByteArray) : UploadProofUiEvent {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as ImageAdded

            return bytes.contentEquals(other.bytes)
        }

        override fun hashCode(): Int {
            return bytes.contentHashCode()
        }
    }

    data class ImageRemoved(val index: Int) : UploadProofUiEvent

    data class ProofDescriptionChange(val description: String) : UploadProofUiEvent
}