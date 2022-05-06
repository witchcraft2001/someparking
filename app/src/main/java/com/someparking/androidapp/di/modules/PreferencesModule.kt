package com.someparking.androidapp.di.modules

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder
import androidx.datastore.rxjava2.RxDataStore
import com.someparking.androidapp.storage.preferences.DataStoreFacade
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PreferencesModule {
    @Singleton
    @Provides
    fun provideRxDataStore(context: Context): RxDataStore<Preferences> {
        return RxPreferenceDataStoreBuilder(context, "cleanable_data_store").build()
    }

    @Singleton
    @Provides
    fun provideDataStoreFacade(dataStore: RxDataStore<Preferences>): DataStoreFacade {
        return DataStoreFacade(dataStore)
    }
}