package com.alexandre.marcq.spotiguessr.api.objects.playlist

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * The number of tracks of a playlist.
 *
 * This class is used to represent the number of tracks in a playlist.
 * Spotify's API returns this number as an object, so a class is needed.
 *
 * @property total the number of tracks.
 */
@Parcelize
class PlaylistTrackNumber(
    val total: Int
) : Parcelable {}
