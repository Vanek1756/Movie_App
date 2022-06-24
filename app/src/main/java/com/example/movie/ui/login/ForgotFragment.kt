package com.example.movie.ui.login

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.movie.R
import com.example.movie.databinding.ForgotFragmentBinding
import com.example.movie.model.User
import com.example.movie.util.launchWhenStarted
import com.example.movie.util.viewBinding
import com.example.movie.viewmodel.LoginActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ForgotFragment : Fragment(R.layout.forgot_fragment) {

    private val binding by viewBinding(ForgotFragmentBinding::bind)

    private var user: User? = null

    private val loginActivityViewModel: LoginActivityViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnChangePassword.setOnClickListener { changePassword() }
            loginActivityViewModel.getUserForgotFlow().onEach {
                if (it != null) {
                    user = it
                    // changePassword(it)
                    tilPassword.visibility = View.VISIBLE
//                    val action =
//                        LoginFragmentDirections.actionLoginFragmentToMainActivity()
//                    val navController = Navigation.findNavController(root)
//                    navController.navigate(action)
                } else {
                    etEmail.error = getString(R.string.login_user_is_not_found)
                }
            }.launchWhenStarted(lifecycleScope)
        }
    }

    private fun changePassword() {
        if (user != null) {
            if (!validatePassword()) {
                return
            } else {
                with(binding) {
//                    if (user!!.password != etPassword.text.toString()) {
//
//                        user!!.password = etPassword.text.toString()
//
//                        loginActivityViewModel.updateUserPassword(user!!)
//
//                        Snackbar.make(
//                            root,
//                            getString(R.string.login_forgot_password_successfully),
//                            Snackbar.LENGTH_LONG
//                        ).show()
//
//                        val action =
//                            ForgotFragmentDirections.actionForgotFragmentToLoginFragment()
//                        val navController = Navigation.findNavController(root)
//                        navController.navigate(action)
//                    } else {
//                        Snackbar.make(
//                            root,
//                            getString(R.string.login_forgot_password_unsuccessfully),
//                            Snackbar.LENGTH_LONG
//                        ).show()
//                    }
                }
            }
        } else {
            if (!validateEmail()) {
                return
            } else {
//                with(binding) {
//                    loginActivityViewModel.searchUserEmail(
//                        etEmail.text.toString()
//                    )
//                }
            }
        }
    }

    private fun validateEmail(): Boolean {
        var valid = true
        with(binding) {
            val email = etEmail.text.toString()
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.error = getString(R.string.login_valid_email)
                valid = false
            } else {
                etEmail.error = null
            }
            return valid
        }
    }

    private fun validatePassword(): Boolean {
        var valid = true
        with(binding) {
            val password = etPassword.text.toString()
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
