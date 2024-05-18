package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.kyc

import androidx.compose.runtime.Stable
import com.closedcircuit.closedcircuitapplication.common.domain.model.AccountType
import com.closedcircuit.closedcircuitapplication.common.domain.model.KycStatus
import com.closedcircuit.closedcircuitapplication.common.domain.model.KycDocumentType
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.user.User
import com.closedcircuit.closedcircuitapplication.common.presentation.util.InputField

@Stable
data class KycUiState(
    val selectedDocumentType: KycDocumentType?,
    val documentNumber: InputField,
    val accountType: AccountType,
    val kycStatus: KycStatus,
    val phoneStatus: KycStatus,
    val isLoading: Boolean
) {
    val canSubmit: Boolean
        get() = documentNumber.value.isNotEmpty()

    companion object {
        fun init(user: User?): KycUiState? {
            if (user == null) return null

            return KycUiState(
                selectedDocumentType = null,
                documentNumber = InputField(name = "documentNumber"),
                accountType = user.accountType,
                kycStatus = user.kycStatus,
                phoneStatus = user.phoneNumberStatus,
                isLoading = false
            )
        }
    }
}

sealed interface KycResult {
    object Success : KycResult
    data class Failure(val message: String) : KycResult
}
