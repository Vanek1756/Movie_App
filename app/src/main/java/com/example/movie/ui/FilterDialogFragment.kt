package com.example.movie.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.movie.R
import com.example.movie.databinding.DialogFilterViewBinding
import com.example.movie.model.result.ResultGenre
import com.example.movie.util.*
import com.example.movie.viewmodel.FilmFragmentViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class FilterDialogFragment : DialogFragment(R.layout.dialog_filter_view),
    AdapterView.OnItemSelectedListener {

    private val viewModel by activityViewModels<FilmFragmentViewModel>()

    private val binding by viewBinding(DialogFilterViewBinding::bind)

    private lateinit var genreList: ArrayList<ResultGenre>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.getGenreFilmFlow().value == null) {
            viewModel.getGenreFilm()
        }
        with(binding) {
            val genreIdActive = viewModel.getGenre()
            viewModel.getGenreFilmFlow().onEach { resultList ->
                if (resultList != null) {
                    genreList = resultList as ArrayList<ResultGenre>
                    genreList.forEach {
                        val layoutInflater = LayoutInflater.from(context)
                        val newChip = layoutInflater.inflate(
                            R.layout.chip_layout_filter, cgGenre,
                            false
                        ) as Chip
                        newChip.text = it.name
                        if (it.id.toString() == genreIdActive) {
                            newChip.isChecked = true
                        }
                        cgGenre.addView(newChip)
                    }
                }
            }.launchWhenStarted(lifecycleScope)

            val layoutInflate = LayoutInflater.from(context)
            val nChip = layoutInflate.inflate(
                R.layout.chip_layout_filter, cgGenre,
                false
            ) as Chip
            nChip.text = KEY_GENRE_ALL
            if (KEY_GENRE_ALL == genreIdActive) {
                nChip.isChecked = true
            }
            cgGenre.addView(nChip)

            SORT.forEach {
                val layoutInflater = LayoutInflater.from(context)
                val newChip = layoutInflater.inflate(
                    R.layout.chip_layout_filter, cgSort,
                    false
                ) as Chip
                newChip.text = it
                if (it == viewModel.getSort()) {
                    newChip.isChecked = true
                }
                cgSort.addView(newChip)
            }
            btnSave.setOnClickListener { saveFilterInfo() }
        }
    }

    private fun saveFilterInfo() {
        with(binding) {
            val genreId = cgGenre.checkedChipId
            val sortId = cgSort.checkedChipId
            if (genreId >= 0) {
                val chipGenre: Chip = cgGenre.findViewById(genreId)

                if (chipGenre.text.toString() == KEY_GENRE_ALL) {
                    viewModel.setGenreFilm(KEY_GENRE_ALL)
                } else {
                    genreList.forEach {
                        if (it.name == chipGenre.text.toString()) {
                            viewModel.setGenreFilm(it.id.toString())
                        }
                    }
                }
            }

            if (sortId >= 0) {
                val chipSort: Chip = cgSort.findViewById(sortId)
                viewModel.setSortFilm(chipSort.text.toString())
            }
            viewModel.updateUi()
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        viewModel.setGenreFilm(GENRES[position])
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}
