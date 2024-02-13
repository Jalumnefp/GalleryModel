package es.jfp.gallerymodel.activitys

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.preference.EditTextPreference
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import es.jfp.gallerymodel.R
import es.jfp.gallerymodel.databinding.ActivityMainBinding
import es.jfp.gallerymodel.dialogs.LogoffDialogFragment
import es.jfp.gallerymodel.fragments.*
import es.jfp.gallerymodel.fragments.ArtDetailFragment
import es.jfp.gallerymodel.fragments.ArtDetailFragment.Companion.ARG_AUTHOR
import es.jfp.gallerymodel.fragments.ArtDetailFragment.Companion.ARG_IMAGE
import es.jfp.gallerymodel.fragments.ArtDetailFragment.Companion.ARG_TITLE
import es.jfp.gallerymodel.fragments.main.ArtworksFragment
import es.jfp.gallerymodel.fragments.PresentationFragment
import es.jfp.gallerymodel.fragments.OldSettingsFragment
import es.jfp.gallerymodel.fragments.main.ChatFragment
import es.jfp.gallerymodel.fragments.main.MultimediaFragment
import es.jfp.gallerymodel.utils.AuthManager

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, NavigationBarView.OnItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    private var isPortrait: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("ChatPreferences", Context.MODE_PRIVATE)
        if (prefs != null) {
            with (prefs.edit()) {
                putString("CHAT_USERNAME", AuthManager.getInstance()?.getCurrentUser()?.email)
                apply()
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.mainToolbar)

        binding.mainToolbar.inflateMenu(R.menu.main_toolbar_menu)
        binding.mainFragmentContainer2?.visibility = View.INVISIBLE
        setupNavigationDrawer()

        val headerView: View = binding.navigationView.getHeaderView(0)
        setNavHeaderUserdata(headerView)

        Log.d("asdfqwer", supportFragmentManager.findFragmentById(R.id.main_fragment_container).toString())

        isPortrait = resources.configuration.orientation == ORIENTATION_PORTRAIT

        binding.bottomNavMain?.setOnItemSelectedListener(this)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.action_settings -> {
            fragmentChanger(R.id.main_fragment_container, OldSettingsFragment())
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall") //This is to avoid the error of not calling the superclass
    override fun onBackPressed() {
        if (binding.mainDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupNavigationDrawer() {
        val toggle = ActionBarDrawerToggle(
            this,
            binding.mainDrawerLayout,
            binding.mainToolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)
        binding.mainDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
        return when(item.itemId) {
            R.id.nav_home ->{
                fragmentChanger(R.id.main_fragment_container, PresentationFragment())
                binding.mainFragmentContainer2?.visibility = View.INVISIBLE
                true
            }
            R.id.nav_gallery ->{
                if (!isPortrait) {
                    val args: Bundle = bundleOf(
                        ARG_TITLE to "?", ARG_AUTHOR to "?", ARG_IMAGE to "0"
                    )
                    fragmentChanger(R.id.main_fragment_container_2, ArtDetailFragment(), args)
                }
                fragmentChanger(R.id.main_fragment_container, ArtworksFragment())
                binding.mainFragmentContainer2?.visibility = View.VISIBLE
                true
            }
            R.id.nav_settings ->{
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                //fragmentChanger(R.id.main_fragment_container, OldSettingsFragment())
                //binding.mainFragmentContainer2?.visibility = View.INVISIBLE
                true
            }
            R.id.nav_logoff ->{
                LogoffDialogFragment().show(supportFragmentManager, "LOGOFF")
                true
            }
            R.id.ic_nav_chat ->{
                fragmentChanger(R.id.main_fragment_container, ChatFragment())
                true
            }
            R.id.ic_nav_media ->{
                fragmentChanger(R.id.main_fragment_container, MultimediaFragment())
                true
            }
            R.id.ic_nav_retrofit ->{
                fragmentChanger(R.id.main_fragment_container, ArtworksFragment())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun fragmentChanger(container: Int, fragment: Fragment, args: Bundle? = null) {
        supportFragmentManager.commit {
            if (args==null)
                replace(container, fragment)
            else
                replace<ArtDetailFragment>(container, args = args)
            addToBackStack(null)
            setReorderingAllowed(false)
        }
    }

    private fun setNavHeaderUserdata(view: View) {
        val user: TextView = view.findViewById(R.id.user_logged_nav)
        user.text = AuthManager.getInstance()?.getCurrentUser()?.email
    }

}