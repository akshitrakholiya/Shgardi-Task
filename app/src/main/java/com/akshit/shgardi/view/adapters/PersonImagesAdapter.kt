package com.akshit.shgardi.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.akshit.shgardi.R
import com.akshit.shgardi.databinding.ItemPersonImagesGridBinding
import com.akshit.shgardi.models.ProfilesItem

class PersonImagesAdapter(
    private val personImages: List<ProfilesItem?>
) : RecyclerView.Adapter<PersonImagesAdapter.PersonImagesViewHolder>() {

    inner class PersonImagesViewHolder(private val binding: ItemPersonImagesGridBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(personImage: ProfilesItem) {
            binding.executePendingBindings()
            binding.personImage = personImage
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonImagesViewHolder {
        val binding = DataBindingUtil.inflate<ItemPersonImagesGridBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_person_images_grid,
            parent,
            false)
        return PersonImagesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PersonImagesViewHolder, position: Int) {
        personImages[position]?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int {
        return personImages.size
    }

}