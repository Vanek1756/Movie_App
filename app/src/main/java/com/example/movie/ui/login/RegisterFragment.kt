package com.example.movie.ui.login

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.movie.R
import com.example.movie.databinding.RegisterFragmentBinding
import com.example.movie.util.launchWhenStarted
import com.example.movie.util.viewBinding
import com.example.movie.viewmodel.LoginActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.register_fragment) {

    private val binding by viewBinding(RegisterFragmentBinding::bind)

    private val loginActivityViewModel: LoginActivityViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            ibBack.setOnClickListener { returnLogin() }
            btnSignUp.setOnClickListener { signUpFunction() }
            tvPrivacyPolice.setOnClickListener { showPrivacyPolicy() }
            loginActivityViewModel.getIsUserCreateFlow().onEach {
//                if (it) {
//                    val action =
//                        RegisterFragmentDirections.actionRegisterFragmentToMainActivity()
//                    val navController = Navigation.findNavController(root)
//                    navController.navigate(action)
//                }
            }.launchWhenStarted(lifecycleScope)
        }
    }

    private fun returnLogin() {
        val action =
            RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
        binding.ibBack.findNavController().navigate(action)
    }

    private fun showPrivacyPolicy() {
        val alertDialogBuilder = context?.let { AlertDialog.Builder(it, R.style.DialogTheme) }
        alertDialogBuilder?.setTitle(getString(R.string.login_privacy_policy))
        alertDialogBuilder
            ?.setMessage(getString(R.string.login_privacy_policy_terms))
            ?.setCancelable(false)
            ?.setPositiveButton(
                getString(R.string.login_agree_policy)
            ) { dialog, _ ->
                dialog.cancel()
            }
        val alertDialog = alertDialogBuilder?.create()
        alertDialog?.show()
    }

    private fun signUpFunction() {
        with(binding) {
            if (!validate()) {
                return
            } else {
//                loginActivityViewModel.createNewUser(
//                    etFullName.text.toString(),
//                    etEmail.text.toString(),
//                    etPassword.text.toString()
//                )
            }
        }
    }

    private fun validate(): Boolean {
        var valid = true
        with(binding) {
            val fullName = etFullName.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (fullName.isEmpty()) {
                etFullName.error = getString(R.string.login_enter_your_name)
                valid = false
            } else {
                etFullName.error = null
            }

            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.error = getString(R.string.login_valid_email)
                valid = false
            } else {
                etEmail.error = null
            }

            if (password.isEmpty() || password.length < 6 || password.length > 12) {
                etPassword.error = getString(R.string.login_valid_password)
                valid = false
            } else {
                etPassword.error = null
            }

            return valid
        }
    }
}
