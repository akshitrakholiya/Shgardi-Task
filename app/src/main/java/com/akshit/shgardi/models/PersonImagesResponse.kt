package com.akshit.shgardi.models

import com.google.gson.annotations.SerializedName

data class PersonImagesResponse(

	@field:SerializedName("profiles")
	val profiles: List<ProfilesItem?>? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class ProfilesItem(

	@field:SerializedName("aspect_ratio")
	val aspectRatio: Any? = null,

	@field:SerializedName("file_path")
	val filePath: String? = null,

	@field:SerializedName("width")
	val width: Int? = null,

	@field:SerializedName("height")
	val height: Int? = null
)
