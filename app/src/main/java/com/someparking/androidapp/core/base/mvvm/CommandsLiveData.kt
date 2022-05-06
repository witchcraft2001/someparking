package com.someparking.androidapp.core.base.mvvm

import androidx.lifecycle.LiveData
import java.util.*

open class CommandsLiveData<T> : LiveData<LinkedList<T>>()
