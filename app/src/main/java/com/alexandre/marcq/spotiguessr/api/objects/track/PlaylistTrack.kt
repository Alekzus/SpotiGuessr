package com.alexandre.marcq.spotiguessr.api.objects.track

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * A track in a playlist.
 *
 * The class is used to represent a track inside of a playlist.
 *
 * @property track the track in the playlist.
 */
@Parcelize
class PlaylistTrack(
    val track: Track
) : Parcelable {}
