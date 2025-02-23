package com.rezalaki.registeraddresscompose.data.model.addressList

import com.google.errorprone.annotations.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Keep
@Serializable
data class AddressItem(
    @SerialName("id") val id: String,
    @SerialName("address") val address:String,
    @SerialName("first_name") val first_name:String,
    @SerialName("last_name") val last_name:String,
    @SerialName("coordinate_mobile") val coordinate_mobile:String
) {
    fun getFullName() = "$first_name $last_name"
}
