package com.alexandre.marcq.spotiguessr.api.objects.track

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
class PlaylistTrackPaging(
    @Json(name = "items") val playlistTracks : List<PlaylistTrack>
) : Parcelable {}