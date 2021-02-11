package com.alexandre.marcq.spotiguessr.api.objects.artist

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * An artist.
 *
 * This class is used to represent an artist retrieved from Spotify's API.
 *
 * @property name name of the artist.
 */
@Parcelize
class Artist(
    val name: String
) : Parcelable
