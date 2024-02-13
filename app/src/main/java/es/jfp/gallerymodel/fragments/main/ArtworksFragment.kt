package es.jfp.gallerymodel.fragments.main

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import es.jfp.gallerymodel.R
import es.jfp.gallerymodel.activitys.MainActivity
import es.jfp.gallerymodel.adapters.ArtworkAdapter
import es.jfp.gallerymodel.classes.APIArtwork
import es.jfp.gallerymodel.classes.ArtworkSend
import es.jfp.gallerymodel.classes.DBArtwork
import es.jfp.gallerymodel.databinding.FragmentArtworksViewBinding
import es.jfp.gallerymodel.dialogs.ArtDialogFragment
import es.jfp.gallerymodel.fragments.ArtDetailFragment
import es.jfp.gallerymodel.fragments.ArtDetailFragment.Companion.ARG_AUTHOR
import es.jfp.gallerymodel.fragments.ArtDetailFragment.Companion.ARG_IMAGE
import es.jfp.gallerymodel.fragments.ArtDetailFragment.Companion.ARG_TITLE
import es.jfp.gallerymodel.services.ArtworksService
import es.jfp.gallerymodel.utils.DatabaseObject
import es.jfp.gallerymodel.utils.RetrofitObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ArtworksFragment : Fragment(), ArtDialogFragment.ArtDialogFragmentButtons {

    private var _binding: FragmentArtworksViewBinding? = null
    private val binding get() = _binding!!

    private val retrofit = RetrofitObject.getInstance()
    private val artworks = mutableListOf<APIArtwork>()
    private var artAdapter: ArtworkAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentArtworksViewBinding.inflate(inflater, container, false)

        binding.floatingButtonAdd.setOnClickListener {
            ArtDialogFragment(this@ArtworksFragment).show(requireActivity().supportFragmentManager, "ADD_ART_DIALOG")
        }

        setupRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? MainActivity)?.supportActionBar?.title = this::class.java.simpleName
    }

    private fun setupRecyclerView() {

        artAdapter = ArtworkAdapter(requireContext(), artworks) { art: APIArtwork ->
            val bundle: Bundle = bundleOf(
                ARG_TITLE to art.title,
                ARG_AUTHOR to art.author,
                ARG_IMAGE to art.image
            )
            if (resources.configuration.orientation == ORIENTATION_PORTRAIT) {
               fragmentChanger(R.id.main_fragment_container, bundle)
            } else {
                fragmentChanger(R.id.main_fragment_container_2, bundle)
            }
        }

        if (PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean("OFFLINE_MODE", false)) {
            getLocalDatabaseData()
        } else {
            getAllArtworks()
        }


        binding.artworkRecyclerContainer.adapter = artAdapter
        binding.artworkRecyclerContainer.setHasFixedSize(true)
        binding.artworkRecyclerContainer.layoutManager = GridLayoutManager(requireContext(), 3)
    }

    private fun getAllArtworks() {
        lifecycleScope.launch(Dispatchers.IO) {
            val call = retrofit.create(ArtworksService::class.java).getAllArtworks()
            val response = call.body()
           response?.forEach {
               DatabaseObject.getInstance(requireContext()).artworkDao().insertArtwork(
                   DBArtwork(
                       id = it.id!!.toInt(),
                       title = it.title,
                       author = it.author,
                       image = it.image
                   )
               )
           }
            withContext(Dispatchers.Main) {
                response.let {
                    artworks.clear()
                    artworks.addAll(response!!)
                    artAdapter?.notifyDataSetChanged()
                }
            }
        }
    }

    private fun fragmentChanger(container: Int, args: Bundle) {
        activity?.supportFragmentManager?.commit {
            replace<ArtDetailFragment>(container, args = args)
            addToBackStack(null)
            setReorderingAllowed(false)
        }
    }

    override fun onClickCreateButton(title: String, author: String, selectedImage: Uri) {
        lifecycleScope.launch(Dispatchers.IO) {
            retrofit.create(ArtworksService::class.java).addArtwork(
                ArtworkSend(title, author, selectedImage)
            )
            withContext(Dispatchers.Main) {
                getAllArtworks()
            }
        }
    }

    private fun getLocalDatabaseData() {
        lifecycleScope.launch(Dispatchers.IO) {
            val databaseData = DatabaseObject.getInstance(requireContext()).artworkDao().getAllArtworks().map {
                APIArtwork(it.id.toString(), it.title, it.author, it.image)
            }
            withContext(Dispatchers.Main) {
                databaseData.let {
                    artworks.clear()
                    artworks.addAll(databaseData)
                    artAdapter?.notifyDataSetChanged()
                }
            }
        }
    }


}