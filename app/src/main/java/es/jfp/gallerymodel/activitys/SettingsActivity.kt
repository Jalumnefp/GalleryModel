package es.jfp.gallerymodel.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.jfp.gallerymodel.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private val binding: ActivitySettingsBinding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}