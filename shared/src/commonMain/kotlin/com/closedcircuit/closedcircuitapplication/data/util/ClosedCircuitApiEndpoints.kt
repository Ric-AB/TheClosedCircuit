package com.closedcircuit.closedcircuitapplication.data.util

object ClosedCircuitApiEndpoints {
    //no auth
    const val LOGIN = "user/login/"
    const val REGISTER = "user/register/"
    const val REGISTER_SPONSOR = "user/register/sponsor/"
    const val USER_DETAILS = "user/manage-user/{id}/"
    const val UPDATE_PROFILE = "user/manage-user/{id}/"
    const val CHANGE_PASSWORD = "user/change-password/{id}/"
    const val GENERATE_OTP = "user/generate-otp/"
    const val VERIFY_OTP = "user/verify-otp/"
    const val RESET_PASSWORD = "user/reset-password/"
    const val VERIFY_TRANSACTION_STATUS = "v1/transactions/webhook"
    const val PLAN_DETAILS_NO_AUTH = "v1/plan_details/{id}"
    const val PLAN_DETAILS_BY_FUND_REQUEST_ID_NO_AUTH = "v1/fund_request/{id}/"

    //beneficiary
    const val DASHBOARD = "user/beneficiary/get_details/"
    const val DELETE_PLAN = "user/plans/{id}/"
    const val UPDATE_PLAN = "user/plans/{id}/"
    const val PLANS = "user/plans/{id}"
    const val GET_MY_PLANS = "user/plans/get-plans/"
    const val DELETE_STEP = "user/steps/{id}/"
    const val UPDATE_BUDGET = "user/budget/{id}/"
    const val STEPS = "user/steps/"
    const val BUDGET = "user/budget/"
    const val DELETE_BUDGET = "user/budget/{id}/"
    const val PLAN = "user/plans/"
    const val GET_STEP = "user/steps/{id}/"
    const val FUND_REQUEST = "v1/fund_request/"
    const val GET_LOAN_OFFERS_PLANS = "v1/loan_offer/plans/"
    const val GET_LOAN_OFFERS_PLAN = "v1/loan_offer/plans/{id}"
    const val GET_LOAN_OFFER = "v1/loan_offer/{id}"
    const val ACCEPT_DELCINE_OFFER = "v1/loan_offer/{id}/"
    const val RECENT_DONATIONS = "v1/donations/"
    const val KYC = "v1/kyc/"
    const val NOTIFICATIONS = "v1/notifications/"
    const val BULK_DELETE_NOTIFICATION = "v1/notifications/bulk-delete/"

    //sponsor
    const val MAKE_OFFER = "v1/loan_offer/"
    const val GET_OFFERS = "v1/loan_offer/sponsor/plans/"
    const val GET_OFFER = "v1/loan_offer/sponsor/{id}/"
    const val CANCEL_OFFER = "v1/loan_offer/sponsor/{id}/"
    const val GET_DASHBOARD_PLANS = "v1/sponsor/dashboard/"
    const val GET_PLAN_DETAILS = "v1/sponsor/dashboard/plans/{id}/"
    const val GET_STEP_PROOFS = "v1/sponsor/dashboard/steps/{id}/"
    const val UPDATE_VIEWED_PLAN = "user/plans/{id}/"
    const val APPROVE_BUDGET = "user/budget/{id}/approve/"
    const val APPROVE_STEP = "user/steps/{id}/approve/"
    const val CHARGE = "v1/transactions/charge"

    //messaging
    const val GET_CONVERSATION_PARTNERS = "user/manage-user/"
    const val SEND_FCM_TOKEN = "user/manage-user/{id}/"

    //payment
    const val TOKENIZE_CARD = "v1/transactions/tokenize-charge"

}