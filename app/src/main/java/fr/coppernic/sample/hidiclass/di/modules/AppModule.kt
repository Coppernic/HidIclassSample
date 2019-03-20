package fr.coppernic.sample.hidiclass.di.modules

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import fr.coppernic.sample.hidiclass.settings.Settings
import fr.coppernic.sample.hidiclass.settings.SettingsImpl
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    @Named("SETTINGS")
    fun providesSettings(sharedPreferences: SharedPreferences): Settings {
        return SettingsImpl(sharedPreferences)
    }
}