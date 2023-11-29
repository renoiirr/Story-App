package com.dicoding.picodiploma.StoryApp.data.pref

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Suppress("DEPRECATED_ANNOTATION")
@Parcelize
data class UserModel(
    val email: String?,
    val password: String?
): Parcelable