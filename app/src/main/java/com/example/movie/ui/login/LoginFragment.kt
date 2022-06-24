package com.example.movie.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.movie.R
import com.example.movie.data.Storage
import com.example.movie.databinding.LoginFragmentBinding
import com.example.movie.util.launchWhenStarted
import com.example.movie.util.viewBinding
import com.example.movie.view.MainActivity
import com.example.movie.viewmodel.LoginActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.login_fragment) {

    @Inject
    lateinit var storage: Storage

    private val binding by viewBinding(LoginFragmentBinding::bind)

    private val loginActivityViewModel by viewModels<LoginActivityViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            loginActivityViewModel.getSessionSuccessFlow().onEach {
                if (it == true) {
                    startMain()
                }
            }.launchWhenStarted(lifecycleScope)
            loginActivityViewModel.getErrorRetrofitRequest().onEach {
                it?.let {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.login_Incorrect_login_or_password),
                        Toast.LENGTH_SHORT).show()
                }
            }.launchWhenStarted(lifecycleScope)
            btnSignIn.setOnClickListener { signInFunction() }
        }
    }

    private fun startMain() {
        val intent = Intent(this@LoginFragment.context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun startForgot() {
        val action =
            LoginFragmentDirections.actionLoginFragmentToForgotFragment()
        binding.tvForgotPassword.findNavController().navigate(action)
    }

    private fun startRegister() {
        val action =
            LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        binding.tvSignUpClick.findNavController().navigate(action)
    }

    private fun signInFunction() {
        if (!validate()) {
            return
        } else {
            with(binding) {
                loginActivityViewModel.authenticationSession(
                    etEmail.text.toString(),
                    etPassword.text.toString()
                )
            }
        }
    }

    private fun validate(): Boolean {
        var valid = true
        with(binding) {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (email.isEmpty()) {
                etEmail.error = getString(R.string.login_valid_username)
                valid = false
            } else {
                etEmail.error = null
            }

            if (password.isEmpty() || password.length <= 5 || password.length > 12) {
                etPassword.error = getString(R.string.login_valid_password)
                valid = false
            } else {
                etPassword.error = null
            }

            return valid
        }
    }
}
