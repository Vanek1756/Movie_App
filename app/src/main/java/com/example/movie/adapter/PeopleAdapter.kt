package com.example.movie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.movie.R
import com.example.movie.databinding.TabResultListItemBinding
import com.example.movie.model.result.ResultPeople
import com.example.movie.util.getInfo
import com.example.movie.util.glideTint

class PeopleAdapter(
    private val resultPeople: ArrayList<ResultPeople>,
    private val glideRequestManager: RequestManager,
    private val listener: (result: ResultPeople, view: ImageView) -> Unit
) : RecyclerView.Adapter<PeopleAdapter.ResultViewHolder>() {

    private lateinit var binding: TabResultListItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        binding = TabResultListItemBinding.inflate(LayoutInflater.from(parent.context))
        return ResultViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {

        val knownFor = resultPeople[position].known_for?.getInfo()
        holder.knownForTextView.text = knownFor
        holder.nameTextView.text = resultPeople[position].name

        val imagePath = holder.context.getString(R.string.adapter_image_path) +
                resultPeople[position].profile_path
        holder.profileImageView.glideTint(imagePath, glideRequestManager)

        holder.progressBar.visibility = View.GONE
        holder.tvProgress.visibility = View.GONE
    }

    override fun getItemCount() = resultPeople.size

    inner class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var nameTextView = binding.tvTitle
        var knownForTextView = binding.tvReleaseDate
        var profileImageView = binding.ivMovie
        var progressBar = binding.progressBar
        var tvProgress = binding.tvProgress
        var context: Context = itemView.context

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener(resultPeople[position], profileImageView)
                }
            }
        }
    }
}
