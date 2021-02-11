package com.alexandre.marcq.spotiguessr.api.objects.track

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * A page of tracks.
 *
 * This class is used to represent a page of tracks belonging to a playlist.
 *
 * @property playlistTracks the list of tracks.
 */
@Parcelize
class PlaylistTrackPaging(
    @Json(name = "items") val playlistTracks : List<PlaylistTrack>
) : Parcelable {}