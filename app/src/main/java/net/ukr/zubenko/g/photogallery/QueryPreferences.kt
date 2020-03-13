package net.ukr.zubenko.g.photogallery

import android.content.Context
import android.preference.PreferenceManager

object QueryPreferences {
    private val PREF_SEARCH_QUERY = "searchQuery"
    fun getStoredQuery(context: Context): String {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getString(PREF_SEARCH_QUERY, "") ?: ""
    }

    fun setStoredQuery(context: Context, query: String) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(PREF_SEARCH_QUERY, query)
            .apply()
    }
}