package com.closedcircuit.closedcircuitapplication.common.presentation.feature.profile.home

sealed interface ProfileUiEvent {
    data class ProfileImageChange(val bytes: ByteArray) : ProfileUiEvent {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as ProfileImageChange

            return bytes.contentEquals(other.bytes)
        }

        override fun hashCode(): Int {
            return bytes.contentHashCode()
        }
    }
}