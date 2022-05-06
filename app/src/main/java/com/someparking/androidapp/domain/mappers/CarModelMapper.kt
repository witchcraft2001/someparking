package com.someparking.androidapp.domain.mappers

import com.someparking.androidapp.domain.CarData
import com.someparking.androidapp.domain.models.CarModel

fun CarData.toCarModel() : CarModel =
    CarModel(id, number, brand, model, comment, status)

fun CarModel.toCarData() : CarData =
        CarData(id, number, brand, model, comment, status)