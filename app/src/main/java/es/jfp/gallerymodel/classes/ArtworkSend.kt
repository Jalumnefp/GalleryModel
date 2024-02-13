package es.jfp.gallerymodel.classes

import android.net.Uri
import com.google.gson.annotations.SerializedName

data class ArtworkSend(
    @SerializedName("title")
    var title: String,
    @SerializedName("author")
    var author: String,
    @SerializedName("image")
    var image: Uri
)
