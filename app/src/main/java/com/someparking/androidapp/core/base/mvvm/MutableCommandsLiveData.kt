package com.someparking.androidapp.core.base.mvvm

import java.util.LinkedList

class MutableCommandsLiveData<T> : CommandsLiveData<T>() {

    fun onNext(value: T) {
        var commands = getValue()
        if (commands == null) {
            commands = LinkedList()
        }
        commands.add(value)
        setValue(commands)
    }
}
