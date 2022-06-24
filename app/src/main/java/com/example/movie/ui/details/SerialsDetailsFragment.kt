package com.example.movie.ui.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.RequestManager
import com.example.movie.R
import com.example.movie.databinding.SerialsDetailsFragmentBinding
import com.example.movie.model.result.ResultSerials
import com.example.movie.util.*
import com.example.movie.viewmodel.DetailsFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SerialsDetailsFragment : Fragment(R.layout.serials_details_fragment) {

    @Inject
    lateinit var glideRequestManager: RequestManager

    private val binding by viewBinding(SerialsDetailsFragmentBinding::bind)
    private var resultSerials: ResultSerials? = null
    private var serialId: Int? = null

    private val fragmentViewModel by viewModels<DetailsFragmentViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pbDetailsSerial.show()
        serialId = arguments?.let { SerialsDetailsFragmentArgs.fromBundle(it).serialId }
        serialId?.let { fragmentViewModel.getDetailsSerials(it) }
        fragmentViewModel.getSuccessRetrofitRequest().onEach { success ->
            success?.let {
                fragmentViewModel.getDetailSerialsFlow().onEach {
                    resultSerials = it
                    fillView()
                }.launchWhenStarted(lifecycleScope)
            }
        }.launchWhenStarted(lifecycleScope)
    }

    @SuppressLint("SetTextI18n")
    private fun fillView() {
        with(binding) {
            pbDetailsSerial.hide()
            val imagePath = getString(R.string.adapter_image_path_w500) +
                    resultSerials?.poster_path

            ivPosterFilm.glidePoster(imagePath, glideRequestManager)

            val imageTintPath = getString(R.string.adapter_image_path) +
                    resultSerials?.backdrop_path
            ivTintFilm.glideTint(imageTintPath, glideRequestManager)

            progressBar.max = 100
            val progress = resultSerials?.vote_average.toString().getProgress()
            progressBar.progress = progress.toInt()
            tvProgress.text = progress + getString(R.string.percent)

            tvOverviewFilm.text = resultSerials?.overview
            tvTitleFilm.text = resultSerials?.name
            tvReleaseDateFilm.text =
                "(" + resultSerials?.first_air_date?.substringBefore('-') + ")"
        }
    }
}
