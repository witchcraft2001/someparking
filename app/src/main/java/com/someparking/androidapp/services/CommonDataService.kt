package com.someparking.androidapp.services

import com.someparking.androidapp.domain.CommonData
import com.someparking.androidapp.domain.RepositoryResult
import com.someparking.androidapp.repositories.CommonRepository
import com.someparking.androidapp.storage.preferences.Preferences
import io.reactivex.Completable
import javax.inject.Inject

class CommonDataService @Inject constructor(
    private val userService: UserService,
    private val preferences: Preferences,
    private val commonRepository: CommonRepository
) {
    private var _parkingPhone: String? = null
    private var _securityPhone: String? = null
    private var _deliveryFoodLink: String? = null

    val parkingPhone: String
        get() {
            if (_parkingPhone.isNullOrBlank()) {
                _parkingPhone = preferences.getParkingPhoneSync()
            }
            return _parkingPhone ?: preferences.getParkingPhoneSync()
        }

    val securityPhone: String
        get() {
            if (_securityPhone.isNullOrBlank()) {
                _securityPhone = preferences.getSecurityPhoneSync()
            }
            return _securityPhone ?: preferences.getSecurityPhoneSync()
        }

    val deliveryFoodLink: String
        get() {
            if (_deliveryFoodLink.isNullOrBlank()) {
                _deliveryFoodLink = preferences.getFoodDeliveryLinkSync()
            }
            return _deliveryFoodLink ?: preferences.getFoodDeliveryLinkSync()
        }

    fun update(): Completable {
        val userId = userService.user.userId
        return commonRepository.getCommonData(userId)
            .flatMapCompletable { result ->
                when (result) {
                    is RepositoryResult.Success -> {
                        updateCommonData(result.data)
                        Completable.complete()
                    }
                    is RepositoryResult.Error -> Completable.error(result.exception)
                }
            }
    }

    private fun updateCommonData(data: CommonData) {
        preferences.setSecurityPhoneSync(data.securityPhone)
        _securityPhone = data.securityPhone
        preferences.setParkingPhoneSync(data.parkingPhone)
        _parkingPhone = data.parkingPhone
        preferences.setFoodDeliveryLinkSync(data.foodDeliveryLink)
        _deliveryFoodLink = data.foodDeliveryLink
    }
}