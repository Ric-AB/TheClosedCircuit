package com.closedcircuit.closedcircuitapplication.common.data.util

object ClosedCircuitApiEndpoints {
    //no auth
    const val LOGIN = "user/login/"
    const val REGISTER = "user/register/"
    const val REGISTER_SPONSOR = "user/register/sponsor/"
    const val USER = "user/manage-user/{id}/"
    const val CHANGE_PASSWORD = "user/change-password/{id}/"
    const val GENERATE_OTP = "user/generate-otp/"
    const val VERIFY_OTP = "user/verify-otp/"
    const val RESET_PASSWORD = "user/reset-password/"
    const val VERIFY_TRANSACTION_STATUS = "v1/transactions/webhook"
    const val PLAN_DETAILS_NO_AUTH = "v1/plan_details/{id}"
    const val PLAN_BY_FUND_REQUEST_ID = "v1/fund_request/{id}/"
    const val SAVE_FCM_TOKEN = "v1/registration_tokens/"
    const val DELETE_ACCOUNT = "user/{id}/request-account-deletion/"


    //beneficiary
    const val DASHBOARD = "user/beneficiary/get_details/"


    // fund request
    const val FUND_REQUEST = "v1/fund_request/"

    // loans
    const val GET_LOAN_PREVIEWS = "v1/loan_offer/plans/"
    const val GET_PLAN_LOAN_OFFERS = "v1/loan_offer/plans/{id}/"
    const val GET_LOAN_OFFER = "v1/loan_offer/{id}/"
    const val ACCEPT_DELCINE_OFFER = "v1/loan_offer/{id}/"

    // donations
    const val RECENT_DONATIONS = "v1/donations/"

    // kyc
    const val KYC = "v1/kyc/"

    // notifications
    const val NOTIFICATIONS = "v1/notifications/"
    const val BULK_DELETE_NOTIFICATION = "v1/notifications/bulk-delete/"

    // plans
    const val CREATE_PLAN = "user/plans/"
    const val PLANS = "user/plans/get-plans/"
    const val PLAN = "user/plans/{id}/"

    // steps
    const val STEPS = "user/steps/"
    const val STEP = "user/steps/{id}/"

    // budget
    const val BUDGETS = "user/budget/"
    const val BUDGET = "user/budget/{id}/"

    //sponsor
    const val MAKE_OFFER = "v1/loan_offer/"
    const val GET_SPONSOR_OFFERS = "v1/loan_offer/sponsor/plans/"
    const val GET_SPONSOR_OFFER_DETAILS = "v1/loan_offer/sponsor/{id}/"
    const val CANCEL_LOAN_OFFER = "v1/loan_offer/sponsor/{id}/"
    const val GET_FUNDED_PLANS_PREVIEW = "v1/sponsor/dashboard/"
    const val GET_FUNDED_PLAN_DETAILS = "v1/sponsor/dashboard/plans/{id}/"
    const val GET_STEP_PROOFS = "v1/sponsor/dashboard/steps/{id}/"
    const val UPDATE_VIEWED_PLAN = "user/plans/{id}/"
    const val APPROVE_BUDGET = "user/budget/{id}/approve/"
    const val APPROVE_STEP = "user/steps/{id}/approve/"
    const val CHARGE = "v1/transactions/charge"

    //chat
    const val GET_CONVERSATION_PARTNERS = "user/manage-user/"
    const val GET_CONVERSATIONS = "v1/chat/conversations/"
    const val GET_CONVERSATION_MESSAGES = "v1/chat/users/{id}/conversations"

    //payment
    const val TOKENIZE_CARD = "v1/transactions/tokenize-charge"

}