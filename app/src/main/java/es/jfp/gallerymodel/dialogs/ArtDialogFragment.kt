package es.jfp.gallerymodel.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import es.jfp.gallerymodel.R
import es.jfp.gallerymodel.databinding.FragmentArtDialogBinding
import es.jfp.gallerymodel.fragments.main.ArtworksFragment


class ArtDialogFragment(
    val fragment: ArtworksFragment
) : DialogFragment() {

    interface ArtDialogFragmentButtons {
        fun onClickCreateButton(title: String, author: String, selectedImage: Uri)
    }

    private val binding: FragmentArtDialogBinding by lazy { FragmentArtDialogBinding.inflate(requireActivity().layoutInflater) }
    private var mListener: ArtDialogFragmentButtons? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = this.fragment as? ArtDialogFragmentButtons
        if (mListener==null) {
            throw NullPointerException("$fragment must implement ArtDialogFragmentButtons!")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val author = binding.toaddAuthorArt.text.toString()
            val title = binding.toaddTitleArt.text.toString()
            lateinit var image: Uri
            val btnImg = binding.addArtBtn

            val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {uri ->
                if (uri != null) {
                    image = uri
                }
            }

            btnImg.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            builder
                .setView(binding.root)
                .setPositiveButton(R.string.create) {dialog, id ->
                    mListener?.onClickCreateButton(title, author, image)
                }
                .setNegativeButton(R.string.cancel) {dialog, id ->}

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }


}