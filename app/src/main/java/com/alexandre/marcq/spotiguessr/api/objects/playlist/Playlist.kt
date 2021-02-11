package com.alexandre.marcq.spotiguessr.api.objects.playlist

import android.os.Parcelable
import com.alexandre.marcq.spotiguessr.api.objects.image.Image
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * A playlist.
 *
 * This class is used to represent a playlist retrieved from Spotify's API.
 *
 * @property id the identifier of the playlist.
 * @property name the name of the playlist.
 * @property playlistTrackNumber the number of tracks in the playlist.
 * @property images the thumbnails of the playlist.
 */
@Parcelize
class Playlist(
    val id: String,
    val name: String,
    @Json(name = "tracks") val playlistTrackNumber: PlaylistTrackNumber,
    val images: List<Image>
) : Parcelable {}
