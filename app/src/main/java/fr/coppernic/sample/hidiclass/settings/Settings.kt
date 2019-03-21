package fr.coppernic.sample.hidiclass.settings

import android.content.SharedPreferences
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

const val KEY_BEEP_ENABLE = "pref_key_beep_enable"
const val KEY_PROTOCOL = "pref_protocol_key"
const val KEY_HF_ENABLE = "pref_LFProx_key"

interface Settings {

    fun isBeepEnabled(): Boolean
    fun getProtocolList(): MutableSet<String>
    fun isHfEnabled(): Boolean
}

@Singleton
class SettingsImpl @Inject constructor(private val sharedPreferences: SharedPreferences) : Settings {
    override fun isHfEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_HF_ENABLE, false)
    }

    override fun getProtocolList(): MutableSet<String> {
        return sharedPreferences.getStringSet(KEY_PROTOCOL, HashSet<String>())
    }


    override fun isBeepEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_BEEP_ENABLE, false)
    }
}
