package com.alexandre.marcq.spotiguessr.api.objects.playlist

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
class PlaylistPaging(
    @Json(name = "items") val playlists: List<Playlist>
) : Parcelable {}