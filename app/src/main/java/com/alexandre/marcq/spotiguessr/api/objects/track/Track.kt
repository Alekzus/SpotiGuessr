package com.alexandre.marcq.spotiguessr.api.objects.track

import android.os.Parcelable
import com.alexandre.marcq.spotiguessr.api.objects.artist.Artist
import kotlinx.android.parcel.Parcelize

@Parcelize
class Track(
    val artists: List<Artist>,
    val id: String,
    val name: String
) : Parcelable
