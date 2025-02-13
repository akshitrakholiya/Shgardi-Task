package com.akshit.shgardi.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.akshit.shgardi.R
import com.akshit.shgardi.databinding.ItemPopularPersonGridBinding
import com.akshit.shgardi.models.ResultsItem

class PopularPersonAdapter(
    private val popularPersonList: MutableList<ResultsItem?> = mutableListOf(),
    private val itemClickListener: (ResultsItem?) -> Unit
): RecyclerView.Adapter<PopularPersonAdapter.PopularPersonViewHolder>() {


    inner class PopularPersonViewHolder(private val binding: ItemPopularPersonGridBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(personShortInfo: ResultsItem) {
            binding.executePendingBindings()
            binding.personShortInfo = personShortInfo

            when (personShortInfo.gender) {
                1 -> {
                    binding.ivBackDrop.setImageResource(R.drawable.ic_backdrop_f)
                }

                2 -> {
                    binding.ivBackDrop.setImageResource(R.drawable.ic_backdrop_m)
                }
                else -> {
                    binding.ivBackDrop.setImageResource(R.drawable.ic_sample)
                }
            }

            binding.root.setOnClickListener {
                itemClickListener(personShortInfo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularPersonViewHolder {
        val binding = DataBindingUtil.inflate<ItemPopularPersonGridBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_popular_person_grid,
            parent,
            false
        )

        return PopularPersonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularPersonViewHolder, position: Int) {
        popularPersonList[position]?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int {
        return popularPersonList.size
    }

    fun addPopularPersonList(newPopularPersonList: List<ResultsItem?>) {
        val startPosition = popularPersonList.size
        popularPersonList.addAll(newPopularPersonList)
        notifyItemRangeInserted(startPosition, newPopularPersonList.size)
    }

    fun clearPopularPersonList() {
        popularPersonList.clear()
        notifyDataSetChanged()
    }

}