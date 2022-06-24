package com.example.movie.ui.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.example.movie.R
import com.example.movie.adapter.TabResultAdapter
import com.example.movie.databinding.PeopleDetailsFragmentBinding
import com.example.movie.model.search.ResultMultiSearch
import com.example.movie.util.glidePoster
import com.example.movie.util.launchWhenStarted
import com.example.movie.util.viewBinding
import com.example.movie.viewmodel.PeopleFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class PeopleDetailsFragment : Fragment(R.layout.people_details_fragment) {

    @Inject
    lateinit var glideRequestManager: RequestManager

    private val binding by viewBinding(PeopleDetailsFragmentBinding::bind)

    private var resultPeople: ResultMultiSearch? = null
    private var peopleName: String? = null

    private val fragmentViewModel by viewModels<PeopleFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pbDetailsPeople.show()
        peopleName = arguments?.let { PeopleDetailsFragmentArgs.fromBundle(it).peopleName }
        peopleName?.let { fragmentViewModel.multiSearch(it) }
        fragmentViewModel.getSearchFlow().onEach {
                resultPeople = it?.results?.get(0)
                fillView()
            }.launchWhenStarted(lifecycleScope)
        fragmentViewModel.getDetailPeopleFlow().onEach {
            binding.tvBiographyPeople.text = it?.biography
        }.launchWhenStarted(lifecycleScope)
    }

    private fun fillView() {
        with(binding) {
            pbDetailsPeople.hide()
            val imagePath = getString(R.string.adapter_image_path) +
                    resultPeople?.profile_path

            ivPosterPeople.glidePoster(imagePath, glideRequestManager)

            resultPeople?.known_for?.let {
                val filmAdapter = TabResultAdapter(
                    it,
                    glideRequestManager
                ) { film, view, text ->
                    startDetails(film, view, text)
                }

                rvPeopleKnownFor.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

                rvPeopleKnownFor.itemAnimator = DefaultItemAnimator()
                rvPeopleKnownFor.adapter = filmAdapter
                filmAdapter.notifyDataSetChanged()
            }
            tvNamePeople.text = resultPeople?.name.toString()
            resultPeople?.id?.let { fragmentViewModel.updateDetailsPeople(it) }
        }
    }

    private fun startDetails(media: ResultMultiSearch, view: View, text: TextView) {
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(
            R.id.fcvDetails
        ) as NavHostFragment
        val navController = navHostFragment.navController
        val extras = FragmentNavigatorExtras(view to "ivMovieToDetail")
        if (media.media_type == "movie") {
            media.id?.let {
                val action =
                    PeopleDetailsFragmentDirections.actionPeopleDetailsFragmentToMovieDetailsFragment(
                        it
                    )
                navController.navigate(action, extras)
            }
        } else if (media.media_type == "tv") {
            media.id?.let {
                val action =
                    PeopleDetailsFragmentDirections.actionPeopleDetailsFragmentToSerialsDetailsFragment(
                        it
                    )
                navController.navigate(action, extras)
            }
        }
    }
}
