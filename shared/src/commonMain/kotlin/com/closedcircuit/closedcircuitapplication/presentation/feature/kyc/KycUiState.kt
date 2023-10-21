package com.closedcircuit.closedcircuitapplication.presentation.feature.kyc

import com.closedcircuit.closedcircuitapplication.domain.model.VerificationStatus
import com.closedcircuit.closedcircuitapplication.domain.user.KycVerificationType
import com.closedcircuit.closedcircuitapplication.domain.user.User
import com.closedcircuit.closedcircuitapplication.util.InputField

data class KycUiState(
    val user: User,
    val selectedVerificationType: KycVerificationType? = null,
    val verificationNumber: InputField = InputField(name = "documentNumber"),
    val hasAttemptedKyc: Boolean,
    val isNigerianAccount: Boolean,
    val isLoading: Boolean
) {
    val canSubmit: Boolean
        get() = verificationNumber.value.isNotEmpty()

    companion object {
        fun init(user: User?): KycUiState? {
            if (user == null) return null

            val hasAttemptedKyc = user.kycStatus != VerificationStatus.NOT_STARTED
            return KycUiState(
                user = user,
                selectedVerificationType = null,
                verificationNumber = InputField(name = "documentNumber"),
                isNigerianAccount = user.phoneNumber.value.startsWith("+234"),
                hasAttemptedKyc = hasAttemptedKyc,
                isLoading = false
            )
        }
    }
}

sealed interface KycResult {
    object Success : KycResult
    data class Failure(val message: String) : KycResult
}
