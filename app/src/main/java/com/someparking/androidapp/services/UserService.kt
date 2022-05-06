package com.someparking.androidapp.services

import com.someparking.androidapp.domain.RepositoryResult
import com.someparking.androidapp.domain.UserCheckResult
import com.someparking.androidapp.domain.UserData
import com.someparking.androidapp.domain.mappers.toUserData
import com.someparking.androidapp.repositories.ProfileRepository
import com.someparking.androidapp.storage.preferences.Preferences
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserService @Inject constructor(
        private val preferences: Preferences,
        private val profileRepository: ProfileRepository
) {
    private var _user: UserData? = null

    val user: UserData
        @Synchronized
        get() {
            if (_user == null) {
                _user = getUserData()
            }
            return _user ?: getUserData()
        }

    @Synchronized
    private fun getUserData(): UserData = UserData(
            preferences.getUserIdSync(),
            preferences.getUserNameSync(),
            preferences.getUserSurnameSync(),
            preferences.getUserMidnameSync(),
    )

    fun updatePersonalData(surname: String, name: String, midname: String) {
        with(preferences) {
            setUserNameSync(name)
            setUserSurnameSync(surname)
            setUserMidameSync(midname)
        }
        _user = user.copy(surname = surname, name = name, midname = midname)
    }

    private fun updateUserData(profile: UserData?) {
        profile?.also {
            _user = it
            with(preferences) {
                setUserNameSync(it.name)
                setUserSurnameSync(it.surname)
                setUserMidameSync(it.midname)
            }
        }
    }

    fun checkRegistration(): Single<UserCheckResult> {
        return if (preferences.getUserTokenSync().isBlank() || preferences.getUserIdSync() == -1L) {
            Single.just(UserCheckResult.NotRegistered)
        } else {
            val userId = preferences.getUserIdSync()
            profileRepository.getProfile(userId)
                    .map { result ->
                        when (result) {
                            is RepositoryResult.Success -> {
                                val user = result.data.toUserData()
                                updateUserData(user)
                                UserCheckResult.Registered(user)
                            }
                            is RepositoryResult.Error -> throw result.exception
                        }
                    }
        }
    }
}