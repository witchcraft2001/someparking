package com.someparking.androidapp.storage.preferences

fun stringPreferencesKey(name: String) = DataStoreFacade.Key(
    androidx.datastore.preferences.core.stringPreferencesKey(name)
)

fun intPreferencesKey(name: String) = DataStoreFacade.Key(
    androidx.datastore.preferences.core.intPreferencesKey(name)
)

fun longPreferencesKey(name: String) = DataStoreFacade.Key(
    androidx.datastore.preferences.core.longPreferencesKey(name)
)

fun floatPreferencesKey(name: String) = DataStoreFacade.Key(
    androidx.datastore.preferences.core.floatPreferencesKey(name)
)

fun doublePreferencesKey(name: String) = DataStoreFacade.Key(
    androidx.datastore.preferences.core.doublePreferencesKey(name)
)

fun booleanPreferencesKey(name: String) = DataStoreFacade.Key(
    androidx.datastore.preferences.core.booleanPreferencesKey(name)
)

fun stringSetPreferencesKey(name: String) = DataStoreFacade.Key(
    androidx.datastore.preferences.core.stringSetPreferencesKey(name)
)
