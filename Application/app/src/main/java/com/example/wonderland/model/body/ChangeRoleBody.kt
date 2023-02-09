package com.example.wonderland.model.body

import com.google.gson.annotations.SerializedName

class ChangeRoleBody(
    @SerializedName("email") val email: String,
    @SerializedName("newRole") val newRole: Int
)