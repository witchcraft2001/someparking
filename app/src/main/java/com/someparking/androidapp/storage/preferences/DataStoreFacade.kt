package com.someparking.androidapp.storage.preferences

import android.annotation.SuppressLint
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.rxjava2.RxDataStore
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import timber.log.Timber

class DataStoreFacade(
    private val dataStore: RxDataStore<Preferences>
) {

    fun <T> getPreference(key: Key<T>, defaultValue: T): Flowable<T> {
        return dataStore.data().map { preferences: Preferences ->
            preferences[key.preferenceKey] ?: defaultValue
        }
    }

    fun <T> getPreferenceSync(key: Key<T>, defaultValue: T): T =
        getPreference(key, defaultValue).blockingFirst()

    fun <T> setPreference(key: Key<T>, value: T?): Completable {
        return setPreferences(key to value)
    }

    fun <T> setPreferenceSync(key: Key<T>, value: T?) {
        setPreferences(key to value).blockingGet()
    }

    fun <T> setPreferences(vararg pairs: PreferencePair<T>): Completable {
        return dataStore.updateDataAsync { preferences ->
            val mutablePreferences = preferences.toMutablePreferences()
            pairs.forEach { (key, value) ->
                if (value != null) {
                    mutablePreferences[key.preferenceKey] = value
                } else {
                    mutablePreferences.remove(key.preferenceKey)
                }
            }
            Single.just(mutablePreferences)
        }.ignoreElement()
    }

    fun <T> setPreferencesSync(vararg pairs: PreferencePair<T>) {
        setPreferences(*pairs).blockingGet()
    }

    @SuppressLint("CheckResult")
    fun clearOnLogout() {
        try {
            dataStore.updateDataAsync { preferences ->
                val mutablePreferences = preferences.toMutablePreferences()
                mutablePreferences.clear()
                Single.just(mutablePreferences)
            }.blockingGet()
        } catch (error: Exception) {
            Timber.e(error)
        }
    }

    class Key<T>(val preferenceKey: Preferences.Key<T>) {

        infix fun to(value: T?): PreferencePair<T> = PreferencePair(this, value)
    }

    data class PreferencePair<T>(val key: Key<T>, val value: T?)
}
