package com.someparking.androidapp.fragments.message

import com.someparking.androidapp.core.base.mvvm.ViewState
import com.someparking.androidapp.domain.FeedbackCompanyData

data class MessageViewState(
    val isProgressVisible: Boolean = false,
    val isFormVisible: Boolean = false,
    val isMessageSentVisible: Boolean = false,
    val companies: List<FeedbackCompanyData>? = null,
    val companyId: Long? = null,
    val isNameIncorrect: Boolean = false,
    val isPhoneIncorrect: Boolean = false,
    val isTextIncorrect: Boolean = false,
    val isWorkplaceIncorrect: Boolean = false,
    val name: String? = null,
    val phone: String? = null,
    val text: String? = null,
    val workplace: String? = null
): ViewState
