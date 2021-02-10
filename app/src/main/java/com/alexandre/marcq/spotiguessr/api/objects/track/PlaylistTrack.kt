package com.alexandre.marcq.spotiguessr.api.objects.track

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class PlaylistTrack(
    val track: Track
) : Parcelable {}
