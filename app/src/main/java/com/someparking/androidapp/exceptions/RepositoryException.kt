package com.someparking.androidapp.exceptions

import java.lang.Exception

class RepositoryException(message: String, errorCode: Int? = null) : Exception(message)
