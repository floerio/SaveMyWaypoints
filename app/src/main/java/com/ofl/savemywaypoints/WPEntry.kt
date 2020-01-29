package com.ofl.savemywaypoints

/*
 Class for DB Table Objects
 Also creates a GPX Data for one data object
 */
class WPEntry(var _id: Int, var date: String, var lon: Double, var lat: Double) {

    fun getWPAsWaypointEntry(): String {

        val result = StringBuilder()

        result.append("<wpt ")
        result.append("lat=\"").append(lat.toString()).append("\" ")
        result.append("lon=\"").append(lon.toString()).append("\">\n ")
        result.append("<time>").append(date).append("</time>\n")
        result.append("</wpt>\n")
        return result.toString()
    }
}
