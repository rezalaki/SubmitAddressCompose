package com.rezalaki.registeraddresscompose.data.model.submitAddress


import com.google.android.gms.maps.model.LatLng
import com.google.errorprone.annotations.Keep
import com.rezalaki.registeraddresscompose.utils.Constants
import com.rezalaki.registeraddresscompose.utils.ValidationRegexes
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.ln

@Keep
@Serializable
data class SubmitAddressRequestBody(
    @SerialName("region") var region: Int = 1,
    @SerialName("address") var address: String? = null,
    @SerialName("lat") var lat: Double = Constants.DEFAULT_LAT,
    @SerialName("lng") var lng: Double = Constants.DEFAULT_LNG,
    @SerialName("coordinate_mobile") var coordinate_mobile: String? = null,
    @SerialName("coordinate_phone_number") var coordinate_phone_number: String? = null,
    @SerialName("first_name") var first_name: String? = null,
    @SerialName("last_name") var last_name: String? = null,
    @SerialName("gender") var gender: String? = "FEMAIL"
) {

    fun getLatLng() = LatLng(lat, lng)

    fun validate(): List<String> {
        val errors = mutableListOf<String>()
        if (Regex(ValidationRegexes.NAME).matches(first_name.orEmpty()).not())
            errors.add("نام به درستی وارد نشده")

        if (Regex(ValidationRegexes.LAST_NAME).matches(last_name.orEmpty()).not())
            errors.add("نام خانوادگی به درستی وارد نشده")

        if (Regex(ValidationRegexes.MOBILE).matches(coordinate_mobile.orEmpty()).not())
            errors.add("تلفن همراه به درستی وارد نشده")

        if (Regex(ValidationRegexes.PHONE).matches(coordinate_phone_number.orEmpty()).not())
            errors.add("تلفن ثابت به درستی وارد نشده")

        if (Regex(ValidationRegexes.ADDRESS).matches(address.orEmpty()).not())
            errors.add("آدرس به درستی وارد نشده")

        return errors
    }
}
