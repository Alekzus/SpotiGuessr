package com.alexandre.marcq.spotiguessr.api.objects.image

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Image(
    val url: String,
    val height: Int?,
    val width: Int?
) : Parcelable {}
