package com.alexandre.marcq.spotiguessr.api.objects.playlist

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
class PlaylistTrackNumberPaging(
    @Json(name = "items") val playlistTrackNumbers: List<PlaylistTrackNumber>
) : Parcelable {}