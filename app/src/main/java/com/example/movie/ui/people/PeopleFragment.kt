package com.example.movie.ui.people

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import com.example.movie.R
import com.example.movie.adapter.PeopleAdapter
import com.example.movie.databinding.PeopleFragmentBinding
import com.example.movie.model.result.ResultPeople
import com.example.movie.util.launchWhenStarted
import com.example.movie.util.toastError
import com.example.movie.util.viewBinding
import com.example.movie.view.DetailsActivity
import com.example.movie.viewmodel.PeopleFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class PeopleFragment : Fragment(R.layout.people_fragment) {

    @Inject
    lateinit var glideRequestManager: RequestManager

    private val binding by viewBinding(PeopleFragmentBinding::bind)

    private lateinit var resultArrayList: ArrayList<ResultPeople>
    private val fragmentViewModel by viewModels<PeopleFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            pbPopularPeople.show()
            fragmentViewModel.updatePeopleFlow()
            fragmentViewModel.getSuccessRetrofitRequest().onEach {
                it?.let {
                    fragmentViewModel.getPeopleFlow().onEach { results ->
                        resultArrayList = results as ArrayList<ResultPeople>
                        popularPeopleSwipeRefresh.isRefreshing = false
                        pbPopularPeople.hide()
                        tvNotConnect.visibility = View.GONE
                        btnRetryConnect.visibility = View.GONE
                        rvPopularPeople.visibility = View.VISIBLE
                        fillRecyclerView()
                    }.launchWhenStarted(lifecycleScope)
                }
            }.launchWhenStarted(lifecycleScope)
            fragmentViewModel.getErrorRetrofitRequest().onEach {
            it?.let {
                    it.toastError(requireContext())
                    popularPeopleSwipeRefresh.isRefreshing = false
                    pbPopularPeople.hide()
                    tvNotConnect.visibility = View.VISIBLE
                    btnRetryConnect.visibility = View.VISIBLE
                    rvPopularPeople.visibility = View.GONE
                }
            }.launchWhenStarted(lifecycleScope)

            btnRetryConnect.setOnClickListener {
                fragmentViewModel.updatePeopleFlow()
                tvNotConnect.visibility = View.GONE
                btnRetryConnect.visibility = View.GONE
                pbPopularPeople.show()
            }

            popularPeopleSwipeRefresh.setColorSchemeResources(R.color.colorPrimary)
            popularPeopleSwipeRefresh.setOnRefreshListener { fragmentViewModel.updatePeopleFlow() }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fillRecyclerView() {

        val peoAdapter = PeopleAdapter(
            resultArrayList,
            glideRequestManager
        ) { film, view ->
            startDetails(film, view)
        }

        with(binding) {
            if (resources.configuration.orientation ==
                Configuration.ORIENTATION_PORTRAIT
            ) {
                rvPopularPeople.layoutManager = GridLayoutManager(context, 2)
            } else {
                rvPopularPeople.layoutManager = GridLayoutManager(context, 4)
            }

            rvPopularPeople.itemAnimator = DefaultItemAnimator()
            rvPopularPeople.adapter = peoAdapter
            peoAdapter.notifyDataSetChanged()
            popularPeopleSwipeRefresh.isRefreshing = false
        }
    }

    private fun startDetails(film: ResultPeople, view: ImageView) {
        val intent = Intent(context, DetailsActivity::class.java)

        val ivPair = androidx.core.util.Pair.create<View, String>(view, "ivPeopleToDetail")

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this.requireActivity(),
            ivPair
        )
        intent.putExtra(DetailsActivity.PEOPLE_DATA_EXTRA, film.name)
        startActivity(intent, options.toBundle())
    }
}
