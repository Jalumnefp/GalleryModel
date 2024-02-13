package es.jfp.gallerymodel.fragments

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import es.jfp.gallerymodel.R
import es.jfp.gallerymodel.activitys.MainActivity
import es.jfp.gallerymodel.databinding.FragmentOldSettingsBinding
import java.util.*


class OldSettingsFragment : Fragment(), OnItemSelectedListener {

    private var _binding: FragmentOldSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOldSettingsBinding.inflate(inflater, container, false)

        val adapter = createAdapter()
        binding.languageSpinner.adapter = adapter
        binding.languageSpinner.onItemSelectedListener = this

        currentLanguage?.let { updateDefaultLang(it) }

        if ((AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) or isDarkModeActivated()) {
            binding.switchDarkMode.isChecked = true
        }

        binding.switchDarkMode.setOnClickListener { changeDarkMode() }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? MainActivity)?.supportActionBar?.title = this::class.java.simpleName
    }

    private fun createAdapter(): SpinnerAdapter {
        val myAdapter = ArrayAdapter.createFromResource(
            activity!!, R.array.languages,
            android.R.layout.simple_spinner_item
        )
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return myAdapter
    }

    override fun onItemSelected(adapter: AdapterView<*>?, itemSelected: View?, position: Int, id: Long) {
        val rawLanguage: String = adapter?.getItemAtPosition(position).toString()
        val language: String = rawLanguage.substring(0, 5)
        if (currentLanguage != language) {
            changeLanguage(language)
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun changeDarkMode() {
        if (binding.switchDarkMode.isChecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun changeLanguage(languageToLoad: String) {
        // https://stackoverflow.com/questions/15970271/clicking-a-button-to-switch-the-language
        val locale = Locale(languageToLoad.substring(0, 2))
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, context!!.resources.displayMetrics)
        currentLanguage = languageToLoad
        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    private fun updateDefaultLang(currentLang: String) {
        resources.getStringArray(R.array.languages).forEachIndexed { position, language ->
            val lang = language.substring(0, 5)
            if (lang == currentLang) {
                binding.languageSpinner.setSelection(position)
            }
        }
    }

    private fun isDarkModeActivated(): Boolean {
        return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }

    companion object {
        var currentLanguage: String? = Locale.getDefault().toString().substring(0, 5)
    }


}