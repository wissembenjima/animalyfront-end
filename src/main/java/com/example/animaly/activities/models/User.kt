package com.example.animaly.activities.models

import com.google.gson.annotations.SerializedName

data class User (



    @SerializedName("id") val id: String?="",
    @SerializedName("firstName") val firstName: String?="",
    @SerializedName("lastName") val lastName: String?="",
    @SerializedName("cin") val cin: String?="",
    @SerializedName("email") val email: String?="",
    @SerializedName("address") val adress: String?="",
    @SerializedName("lat") val lat: String?="",
    @SerializedName("lng") val lng: String?="",
    @SerializedName("password") val password: String?="",
    @SerializedName("phoneNumber") val phone: String?="",
    @SerializedName("profilePicture") val idPhoto: String?="",
    @SerializedName("role") val role: String?=""



) {
    override fun toString(): String {
        return "User(id=$id, firstName=$firstName, lastName=$lastName, cin=$cin, email=$email, adress=$adress, lat=$lat, lng=$lng, password=$password, phone=$phone, idPhoto=$idPhoto, role=$role)"
    }
}