package com.akshit.shgardi.utilities

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.akshit.shgardi.R
import com.akshit.shgardi.infra.network.WebApiInterface
import com.bumptech.glide.Glide

object BindingUtils {
    @JvmStatic
    @BindingAdapter("load_image_with_prefix")
    fun loadImageWithPrefix(imageView: ImageView, url: String?) {
        Glide.with(imageView.context)
            .load(WebApiInterface.PERSON_IMG_PREFIX+url) // image url
            .placeholder(R.drawable.placeholder_dark) // any placeholder to load at start
            .error(R.drawable.placeholder_dark) // any image in case of error
            .override(512, 512) // resizing
            .centerCrop()
            .into(imageView)
    }
}