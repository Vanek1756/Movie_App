package com.example.movie.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.movie.R
import com.example.movie.databinding.TabResultListItemBinding
import com.example.movie.model.search.ResultMultiSearch
import com.example.movie.util.getProgress
import com.example.movie.util.glidePosterDetails

class TabResultAdapter(
    private val resultFilms: List<ResultMultiSearch>,
    private val glideRequestManager: RequestManager,
    private val listener: (result: ResultMultiSearch, view: ImageView, text: TextView) -> Unit
) : RecyclerView.Adapter<TabResultAdapter.ResultViewHolder>() {

    private lateinit var binding: TabResultListItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        binding = TabResultListItemBinding.inflate(LayoutInflater.from(parent.context))
        return ResultViewHolder(binding.root)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {

        holder.titleTextView.text = resultFilms[position].title
        holder.releaseDateTextView.text = resultFilms[position].release_date

        val imagePath = holder.context.getString(R.string.adapter_image_path) +
                resultFilms[position].poster_path
        holder.movieImageView.glidePosterDetails(imagePath, glideRequestManager)

        holder.progressBar.max = 100
        val progress = resultFilms[position].vote_average.toString().getProgress()
        holder.progressBar.progress = progress.toInt()
        holder.tvProgress.text = progress + holder.context.getString(R.string.percent)
    }

    override fun getItemCount() = resultFilms.size

    inner class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var titleTextView = binding.tvTitle
        var releaseDateTextView = binding.tvReleaseDate
        var movieImageView = binding.ivMovie
        var progressBar = binding.progressBar
        var tvProgress = binding.tvProgress
        var context: Context = itemView.context

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener(resultFilms[position], movieImageView, titleTextView)
                }
            }
        }
    }
}
