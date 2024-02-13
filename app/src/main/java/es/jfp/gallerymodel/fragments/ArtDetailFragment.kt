package es.jfp.gallerymodel.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import es.jfp.gallerymodel.activitys.MainActivity
import es.jfp.gallerymodel.databinding.FragmentArtDetailBinding


class ArtDetailFragment : Fragment() {

    private var _binding: FragmentArtDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var paramTitle: String
    private lateinit var paramAuthor: String
    private var paramImage: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramTitle = it.getString(ARG_TITLE).toString()
            paramAuthor = it.getString(ARG_AUTHOR).toString()
            paramImage = it.getInt(ARG_IMAGE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentArtDetailBinding.inflate(inflater, container, false)

        binding.artTitleTextview.text = paramTitle
        binding.artAuthorTextview.text = "By $paramAuthor"
        binding.artImageview.setImageResource(paramImage!!)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? MainActivity)?.supportActionBar?.title = this::class.java.simpleName
    }

    companion object {
        const val ARG_TITLE: String = "param_title"
        const val ARG_AUTHOR: String = "param_author"
        const val ARG_IMAGE: String = "param_image"
    }
}