package es.jfp.gallerymodel.fragments.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import es.jfp.gallerymodel.databinding.FragmentForgottenPasswordBinding
import es.jfp.gallerymodel.utils.AuthManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ForgottenPasswordFragment: Fragment(), OnClickListener {

    interface ForgottenPasswordFragmentButtons {
        fun onClickForgottenPasswordButton()
    }

    private var _binding: FragmentForgottenPasswordBinding? = null
    private val binding get() = _binding!!

    private val authManager = AuthManager.getInstance()
    private var mListener: ForgottenPasswordFragmentButtons? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as? ForgottenPasswordFragmentButtons
        if (mListener==null) {
            throw NullPointerException("$context must implement ForgottenPasswordFragmentButtons!")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgottenPasswordBinding.inflate(inflater, container, false)

        binding.sendButton.setOnClickListener(this)

        return binding.root
    }


    private fun resetPassword(email: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            authManager?.resetPassword(email)
            withContext(Dispatchers.Main) {
                Snackbar.make(binding.root, "The mail was sent", Snackbar.LENGTH_SHORT).show()
            }
        }
    }


    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onClick(v: View) {
        when(v) {
            binding.sendButton -> {
                val email = binding.emailToSubmit.text.toString()
                resetPassword(email)
            }
        }
    }

}