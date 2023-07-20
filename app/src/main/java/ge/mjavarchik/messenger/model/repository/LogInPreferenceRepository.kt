package ge.mjavarchik.messenger.model.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class LogInPreferenceRepository(private val context: Context) {

    private val preferences: SharedPreferences = context
        .getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    private val editor = preferences.edit()

    fun getLoggedInUsername(): String? {
        return preferences.getString(KEY_LOGGED_IN_USERNAME, null)
    }

    fun setLoggedInUsername(username: String) {
        editor.putString(KEY_LOGGED_IN_USERNAME, username)
        editor.commit()
    }

    fun clearLoggedInUsername() {
        editor.remove(KEY_LOGGED_IN_USERNAME)
        editor.commit()
    }

    companion object {
        const val PREFERENCE_NAME = "PREFERENCES"
        const val KEY_LOGGED_IN_USERNAME = "username"
    }
}