package com.example.movie.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.bumptech.glide.RequestManager
import com.example.movie.R
import com.example.movie.databinding.ProfileFragmentBinding
import com.example.movie.model.User
import com.example.movie.util.glideProfile
import com.example.movie.util.launchWhenStarted
import com.example.movie.util.viewBinding
import com.example.movie.view.LoginActivity
import com.example.movie.viewmodel.FilmFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.profile_fragment) {

    private val binding by viewBinding(ProfileFragmentBinding::bind)

    private val fragmentViewModel by activityViewModels<FilmFragmentViewModel>()

    @Inject
    lateinit var user: User

    @Inject
    lateinit var glideRequestManager: RequestManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentViewModel.getUserFlow().onEach {
            updateUserInfo(it)
        }.launchWhenStarted(lifecycleScope)
        fragmentViewModel.updateUser()
        with(binding) {
            btnSingOut.setOnClickListener { singOut() }
            tvProfileFavorites.setOnClickListener { startFavorite() }
            tvProfileRecommendations.setOnClickListener { startRecommendation() }
            tvProfileWatchList.setOnClickListener { startWatchList() }
            tvProfileRating.setOnClickListener { startRatings() }
        }
    }

    private fun startRatings() {
        val action =
            ProfileFragmentDirections.actionProfileFragmentToRatingsFragment()
        binding.tvProfileRating.findNavController().navigate(action)
    }

    private fun updateUserInfo(it: User?) {
        if (it?.name != "") {
            binding.tvProfileName.text = it?.name
        } else {
            binding.tvProfileName.text = it.userName
        }
        it?.imageProfile
        if (it?.imageProfile != null) {
            val imagePath = context?.getString(R.string.adapter_image_path) + it.imageProfile
            binding.ivProfile.glideProfile(imagePath, glideRequestManager)
        }
    }

    private fun startRecommendation() {
        val action =
            ProfileFragmentDirections.actionProfileFragmentToRecommendationsFragment()
        binding.tvProfileRecommendations.findNavController().navigate(action)
    }

    private fun startFavorite() {
        val action =
            ProfileFragmentDirections.actionProfileFragmentToFavoriteFragment()
        binding.tvProfileFavorites.findNavController().navigate(action)
    }

    private fun startWatchList() {
        val action =
            ProfileFragmentDirections.actionProfileFragmentToWatchListFragment()
        binding.tvProfileFavorites.findNavController().navigate(action)
    }

    private fun singOut() {
        fragmentViewModel.singOutUser()
        val intent = Intent(this.context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}
