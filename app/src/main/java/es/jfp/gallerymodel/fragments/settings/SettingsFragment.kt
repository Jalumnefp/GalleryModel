package es.jfp.gallerymodel.fragments.settings

import android.content.Context
import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.Preference.OnPreferenceChangeListener
import androidx.preference.PreferenceFragmentCompat
import es.jfp.gallerymodel.R

class SettingsFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val prefs = requireActivity().getSharedPreferences("ChatPreferences", Context.MODE_PRIVATE)

        val chatUsername = findPreference<EditTextPreference>("CHAT_USERNAME")
        val storedUsername = prefs.getString("CHAT_USERNAME", null)
        chatUsername?.text = storedUsername
        chatUsername?.summary = "The username or alias of the chat"

        val chatColor = findPreference<ListPreference>("CHAT_COLOR")
        chatColor?.summary = "The background color of your messages"

    }

}