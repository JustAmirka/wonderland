package com.example.wonderland.model.body

import com.google.gson.annotations.SerializedName

class ChangePasswordBody (
    @SerializedName("oldPassword") val oldPassword: String,
    @SerializedName("newPassword") val newPassword: String,
)