package es.jfp.gallerymodel.services

import es.jfp.gallerymodel.classes.APIArtwork
import es.jfp.gallerymodel.classes.ArtworkSend
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ArtworksService {

    @GET("artworks")
    suspend fun getAllArtworks(): Response<List<APIArtwork>>

    @POST("artworks/")
    suspend fun addArtwork(@Body artwork: ArtworkSend): Response<APIArtwork>

    @PUT("artworks/{id}")
    suspend fun updateArtwork(@Path("id") id: Int, @Body artwork: APIArtwork): Response<APIArtwork>

    @DELETE("artworks/{id}")
    suspend fun deleteArtwork(@Path("id") id: Int): Response<Unit>

}