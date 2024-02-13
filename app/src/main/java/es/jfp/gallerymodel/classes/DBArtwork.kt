package es.jfp.gallerymodel.classes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity("artworks")
data class DBArtwork(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo("title")
    var title: String,

    @ColumnInfo("author")
    var author: String,

    @ColumnInfo("image")
    var image: String

)
