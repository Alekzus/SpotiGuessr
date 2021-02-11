package com.alexandre.marcq.spotiguessr.api

import com.alexandre.marcq.spotiguessr.utils.Constants.BASE_URL
import com.alexandre.marcq.spotiguessr.api.objects.playlist.PlaylistPaging
import com.alexandre.marcq.spotiguessr.api.objects.track.PlaylistTrackPaging
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

// Used to indicate the status of the query
// Helps adapting the UI
enum class PlaylistsApiStatus { LOADING, ERROR, DONE, NONE }

// Moshi is a JSON library for Kotlin/Java
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// Retrofit is a HTTP request library
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()


interface PlaylistsApiService {
    /**
     * This function gets the playlists of a user.
     *
     * @param accessToken the token of the user.
     * @return the [page of playlists][PlaylistPaging].
     */
    @GET("me/playlists")
    fun getPlaylistsAsync(@Header("Authorization") accessToken: String): Deferred<PlaylistPaging>

    /**
     * This function gets playlists from a query.
     *
     * @param accessToken the token of the user.
     * @param query the query of the search
     * @param type the type of query (i.e. playlist, artist, ...)
     * @return the [page of playlists][PlaylistPaging]
     */
    @GET("search")
    fun getPlaylistsSearchAsync(
        @Header("Authorization") accessToken: String,
        @Query("q") query: String,
        @Query("type") type: String
    ): Deferred<PlaylistPaging>

    /**
     * This function gets the tracks of a given playlist.
     *
     * @param accessToken the token of the user.
     * @param playlistId the identifier of the playlist.
     * @return the [page of tracks][PlaylistTrackPaging]
     */
    @GET("playlists/{playlist_id}/tracks")
    fun getPlaylistTracksAsync(
        @Header("Authorization") accessToken: String,
        @Path("playlist_id") playlistId: String
    ): Deferred<PlaylistTrackPaging>
}

// Object used to make the different requests.
object PlaylistsApi {
    val retrofitService: PlaylistsApiService by lazy {
        retrofit.create(PlaylistsApiService::class.java)
    }
}