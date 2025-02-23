package com.rezalaki.registeraddresscompose.data.model.submitAddress

import com.google.errorprone.annotations.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Keep
@Serializable
data class SubmitAddressResponse(
    @SerialName("id") var id: String? = null,
    @SerialName("address_id") var addressId: String? = null,
    @SerialName("region") var region: Region? = Region(),
    @SerialName("address") var address: String? = null,
    @SerialName("last_name") var lastName: String? = null,
    @SerialName("first_name") var firstName: String? = null,
    @SerialName("gender") var gender: String? = null,
    @SerialName("lat") var lat: Double? = null,
    @SerialName("lng") var lng: Double? = null,
    @SerialName("coordinate_mobile") var coordinateMobile: String? = null,
    @SerialName("coordinate_phone_number") var coordinatePhoneNumber: String? = null
)

@Serializable
data class CityObject(
    @SerialName("city_id") var cityId: Int? = null,
    @SerialName("city_name") var cityName: String? = null
)

@Serializable
data class StateObject(
    @SerialName("state_id") var stateId: Int? = null,
    @SerialName("state_name") var stateName: String? = null
)

@Serializable
data class Region(
    @SerialName("id") var id: Int? = null,
    @SerialName("name") var name: String? = null,
    @SerialName("city_object") var cityObject: CityObject? = CityObject(),
    @SerialName("state_object") var stateObject: StateObject? = StateObject()
)