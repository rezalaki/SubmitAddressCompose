package com.rezalaki.registeraddresscompose.utils



object ValidationRegexes {

    const val NAME = "^[A-Za-z\\s]{3,10}\$"
    const val LAST_NAME = "^[A-Za-z\\s]{3,20}\$"
    const val MOBILE = "^09\\d{9}\$"
    const val PHONE = "^0\\d{10}\$"
    const val ADDRESS = "^[A-Za-z0-9\\s]{6,60}\$"
    const val LAT = "^35\\.\\d{1,2}\$"
    const val LNG = "^51\\.\\d{1,2}\$"

}