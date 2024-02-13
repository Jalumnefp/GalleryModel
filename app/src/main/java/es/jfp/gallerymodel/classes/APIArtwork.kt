package es.jfp.gallerymodel.classes

import com.google.gson.annotations.SerializedName

data class APIArtwork(
    @SerializedName("id")
    var id: String?,
    @SerializedName("title")
    var title: String,
    @SerializedName("author")
    var author: String,
    @SerializedName("image")
    var image: String

)