package es.jfp.gallerymodel.fragments.auth

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import es.jfp.gallerymodel.activitys.LoginActivity
import es.jfp.gallerymodel.databinding.FragmentLoginBinding
import es.jfp.gallerymodel.utils.AuthManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginFragment : Fragment(), OnClickListener {

    interface LoginFragmentButtons {
        fun onClickLoginButton()
        fun onClickSignupButton()
        fun onClickRememberButton()
    }

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private var miListener: LoginFragmentButtons? = null
    private val authManager = AuthManager.getInstance()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        miListener = context as? LoginFragmentButtons
        if (miListener==null) {
            throw java.lang.NullPointerException("$context must implement LoginFragmentButtons!")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.loginButton.setOnClickListener(this)
        binding.singupTextview.setOnClickListener(this)
        binding.rememberButton?.setOnClickListener(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? LoginActivity)?.supportActionBar?.title = this::class.java.simpleName
    }

    override fun onDetach() {
        super.onDetach()
        miListener = null
    }

    override fun onClick(v: View) {
        when(v) {
            binding.loginButton -> {
                val username = binding.usernameEditText.text.toString()
                val password = binding.passwordEditText.text.toString()
                if (!username.isNullOrBlank() && !password.isNullOrBlank()) {
                    login(username, password)
                } else {
                    Toast.makeText(requireContext(), "Error: Credenciales nulas!", Toast.LENGTH_SHORT).show()
                }
            }
            binding.singupTextview -> miListener?.onClickSignupButton()
            binding.rememberButton -> miListener?.onClickRememberButton()
        }
    }

    private fun login(email: String, password: String) {

        lifecycleScope.launch(Dispatchers.IO) {
            val user = authManager?.login(email, password)
            withContext(Dispatchers.Main) {
                if (user == null) {
                    Snackbar.make(binding.root, "Inicio de sesión incorrecto", Snackbar.LENGTH_SHORT).show()
                } else {
                    miListener?.onClickLoginButton()
                }
            }

        }

    }

}