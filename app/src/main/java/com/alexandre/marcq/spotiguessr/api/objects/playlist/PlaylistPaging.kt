package com.alexandre.marcq.spotiguessr.api.objects.playlist

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * A page of playlists.
 *
 * This class is used to represent a page of playlists retrieved from Spotify's API.
 *
 * @property playlists the list of playlists.
 */
@Parcelize
class PlaylistPaging(
    @Json(name = "items") val playlists: List<Playlist>
) : Parcelable {}