package com.someparking.androidapp.fragments.message

import com.someparking.androidapp.core.base.mvvm.ViewCommand

sealed class MessageViewCommand : ViewCommand
object ShowNetworkErrorCommand : MessageViewCommand()
data class ShowTextErrorCommand(val text: String) : MessageViewCommand()
object FinishMessageCommand : MessageViewCommand()
