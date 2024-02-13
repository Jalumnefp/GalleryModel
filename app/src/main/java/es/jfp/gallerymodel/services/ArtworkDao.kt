package es.jfp.gallerymodel.services

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.jfp.gallerymodel.classes.DBArtwork

@Dao
interface ArtworkDao {

    @Query("SELECT * FROM artworks")
    suspend fun getAllArtworks(): List<DBArtwork>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArtwork(artwork: DBArtwork): Long

}