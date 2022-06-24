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
import com.example.movie.util.glideTint

class SearchAdapter(
    private val resultMultiSearch: ArrayList<ResultMultiSearch>,
    private val glideRequestManager: RequestManager,
    private val listener: (result: ResultMultiSearch, view: ImageView, text: TextView) -> Unit
) : RecyclerView.Adapter<SearchAdapter.ResultViewHolder>() {

    private lateinit var binding: TabResultListItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        binding = TabResultListItemBinding.inflate(LayoutInflater.from(parent.context))
        return ResultViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        when (resultMultiSearch[position].media_type) {
            "movie" -> {
                holder.titleTextView.text = resultMultiSearch[position].title
                val imagePath = holder.context.getString(R.string.adapter_image_path) +
                        resultMultiSearch[position].poster_path
                holder.movieImageView.glideTint(imagePath, glideRequestManager)
                progress(holder, position)
            }
            "tv" -> {
                holder.titleTextView.text = resultMultiSearch[position].name
                val imagePath = holder.context.getString(R.string.adapter_image_path) +
                        resultMultiSearch[position].poster_path
                holder.movieImageView.glideTint(imagePath, glideRequestManager)
                progress(holder, position)
            }
            "person" -> {
                holder.titleTextView.text = resultMultiSearch[position].name
                val imagePath = holder.context.getString(R.string.adapter_image_path) +
                        resultMultiSearch[position].profile_path
                holder.movieImageView.glideTint(imagePath, glideRequestManager)
                holder.progressBar.visibility = View.GONE
                holder.tvProgress.visibility = View.GONE
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun progress(holder: ResultViewHolder, position: Int) {
        holder.progressBar.max = 100
        val progress = resultMultiSearch[position].vote_average.toString().getProgress()
        holder.progressBar.progress = progress.toInt()
        holder.tvProgress.text = progress + holder.context.getString(R.string.percent)
    }

    override fun getItemCount() = resultMultiSearch.size

    inner class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var titleTextView = binding.tvTitle
        var movieImageView = binding.ivMovie
        var progressBar = binding.progressBar
        var tvProgress = binding.tvProgress
        var context: Context = itemView.context

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener(resultMultiSearch[position], movieImageView, titleTextView)
                }
            }
        }
    }
}
