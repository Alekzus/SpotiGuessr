package com.alexandre.marcq.spotiguessr.api.objects.playlist

import android.os.Parcelable
import com.alexandre.marcq.spotiguessr.api.objects.image.Image
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
class Playlist(
    val id: String,
    val name: String,
    val uri: String,
    @Json(name = "tracks") val playlistTrackNumber: PlaylistTrackNumber,
    val images: List<Image>
) : Parcelable {}
