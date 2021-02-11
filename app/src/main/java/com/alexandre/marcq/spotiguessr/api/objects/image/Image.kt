package com.alexandre.marcq.spotiguessr.api.objects.image

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * An image.
 *
 * This class is used to represent an image retrieved from Spotify's API.
 *
 * @property url the URL of the image.
 * @property height the height of the image.
 * @property width the width of the image.
 */
@Parcelize
class Image(
    val url: String,
    val height: Int?,
    val width: Int?
) : Parcelable {}
