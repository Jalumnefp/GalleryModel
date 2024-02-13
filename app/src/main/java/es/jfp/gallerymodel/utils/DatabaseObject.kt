package es.jfp.gallerymodel.utils

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import es.jfp.gallerymodel.classes.DBArtwork
import es.jfp.gallerymodel.services.ArtworkDao

@Database(
    entities =[DBArtwork::class],
    version = 1
)
abstract class DatabaseObject: RoomDatabase() {
    abstract fun artworkDao(): ArtworkDao
    companion object {
        private var instance: DatabaseObject? = null
        fun getInstance(context: Context): DatabaseObject {
            synchronized(this) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DatabaseObject::class.java,
                        "ARTWORKS_DB"
                    ).build()
                }
            }
            return instance!!
        }
    }
}