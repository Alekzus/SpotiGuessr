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

enum class PlaylistsApiStatus { LOADING, ERROR, DONE, NONE }

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface PlaylistsApiService {
    @GET("me/playlists")
    fun getPlaylistsAsync(@Header("Authorization") accessToken: String): Deferred<PlaylistPaging>

    @GET("search")
    fun getPlaylistsSearchAsync(
        @Header("Authorization") accessToken: String,
        @Query("q") query: String,
        @Query("type") type: String
    ): Deferred<PlaylistPaging>

    @GET("playlists/{playlist_id}/tracks")
    fun getPlaylistTracksAsync(
        @Header("Authorization") accessToken: String,
        @Path("playlist_id") playlistId: String
    ): Deferred<PlaylistTrackPaging>
}

object PlaylistsApi {
    val retrofitService: PlaylistsApiService by lazy {
        retrofit.create(PlaylistsApiService::class.java)
    }
}