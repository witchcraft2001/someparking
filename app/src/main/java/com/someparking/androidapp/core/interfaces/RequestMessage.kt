package com.someparking.androidapp.core.interfaces

import com.someparking.androidapp.domain.Message

interface RequestMessage {
    fun showMessage(message: Message)
}