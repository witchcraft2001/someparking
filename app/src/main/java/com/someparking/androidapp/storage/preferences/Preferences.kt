package com.someparking.androidapp.storage.preferences

import com.someparking.androidapp.storage.preferences.PreferenceConstants.KEY_FOOD_DELIVERY_LINK
import com.someparking.androidapp.storage.preferences.PreferenceConstants.KEY_PARKING_PHONE
import com.someparking.androidapp.storage.preferences.PreferenceConstants.KEY_ROLE_ID
import com.someparking.androidapp.storage.preferences.PreferenceConstants.KEY_SECURITY_PHONE
import com.someparking.androidapp.storage.preferences.PreferenceConstants.KEY_SERVER_TIME_DELTA
import com.someparking.androidapp.storage.preferences.PreferenceConstants.KEY_USER_EMAIL
import com.someparking.androidapp.storage.preferences.PreferenceConstants.KEY_USER_ID
import com.someparking.androidapp.storage.preferences.PreferenceConstants.KEY_USER_MIDNAME
import com.someparking.androidapp.storage.preferences.PreferenceConstants.KEY_USER_NAME
import com.someparking.androidapp.storage.preferences.PreferenceConstants.KEY_USER_PHONE
import com.someparking.androidapp.storage.preferences.PreferenceConstants.KEY_USER_SURNAME
import com.someparking.androidapp.storage.preferences.PreferenceConstants.KEY_USER_TOKEN
import javax.inject.Inject

class Preferences @Inject constructor(
    private val dataStoreFacade: DataStoreFacade
) {
    fun getUserTokenSync(): String =
        dataStoreFacade.getPreferenceSync(KEY_USER_TOKEN, "")

    fun setUserTokenSync(token: String) =
        dataStoreFacade.setPreferenceSync(KEY_USER_TOKEN, token)

    fun getUserIdSync(): Long =
        dataStoreFacade.getPreferenceSync(KEY_USER_ID, -1)

    fun setUserIdSync(userId: Long) =
        dataStoreFacade.setPreferenceSync(KEY_USER_ID, userId)

    fun getRoleIdSync(): Long =
        dataStoreFacade.getPreferenceSync(KEY_ROLE_ID, -1)

    fun setRoleIdSync(roleId: Long) =
        dataStoreFacade.setPreferenceSync(KEY_ROLE_ID, roleId)

    fun getUserNameSync(): String =
        dataStoreFacade.getPreferenceSync(KEY_USER_NAME, "")

    fun setUserNameSync(username: String) =
        dataStoreFacade.setPreferenceSync(KEY_USER_NAME, username)

    fun getUserSurnameSync(): String =
        dataStoreFacade.getPreferenceSync(KEY_USER_SURNAME, "")

    fun setUserSurnameSync(username: String) =
        dataStoreFacade.setPreferenceSync(KEY_USER_SURNAME, username)

    fun getUserMidnameSync(): String =
        dataStoreFacade.getPreferenceSync(KEY_USER_MIDNAME, "")

    fun setUserMidameSync(username: String) =
        dataStoreFacade.setPreferenceSync(KEY_USER_MIDNAME, username)

    fun getUserEmailSync(): String =
        dataStoreFacade.getPreferenceSync(KEY_USER_EMAIL, "")

    fun setUserEmailSync(email: String) =
        dataStoreFacade.setPreferenceSync(KEY_USER_EMAIL, email)

    fun getUserPhoneSync(): String =
        dataStoreFacade.getPreferenceSync(KEY_USER_PHONE, "")

    fun setUserPhoneSync(phone: String) =
        dataStoreFacade.setPreferenceSync(KEY_USER_PHONE, phone)

    fun getSecurityPhoneSync(): String =
        dataStoreFacade.getPreferenceSync(KEY_SECURITY_PHONE, "")

    fun setSecurityPhoneSync(phone: String) =
        dataStoreFacade.setPreferenceSync(KEY_SECURITY_PHONE, phone)

    fun getParkingPhoneSync(): String =
        dataStoreFacade.getPreferenceSync(KEY_PARKING_PHONE, "")

    fun setParkingPhoneSync(phone: String) =
        dataStoreFacade.setPreferenceSync(KEY_PARKING_PHONE, phone)

    fun getFoodDeliveryLinkSync(): String =
        dataStoreFacade.getPreferenceSync(KEY_FOOD_DELIVERY_LINK, "")

    fun setFoodDeliveryLinkSync(link: String) =
        dataStoreFacade.setPreferenceSync(KEY_FOOD_DELIVERY_LINK, link)

    /**
     * Возвращает разницу "локальное время - серверное"
     */
    fun getServerTimeDeltaSync(): Long =
        dataStoreFacade.getPreferenceSync(KEY_SERVER_TIME_DELTA, 0L)

    fun setServerTimeDeltaSync(delta: Long) =
        dataStoreFacade.setPreferenceSync(KEY_SERVER_TIME_DELTA, delta)

    fun clearOnLogout() {
        dataStoreFacade.clearOnLogout()
    }
}