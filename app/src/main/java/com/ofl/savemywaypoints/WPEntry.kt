package com.ofl.savemywaypoints

class WPEntry(var date: String, var lon: Double, var lat: Double) {

    fun getWPAsWaypointEntry(): String {

        var cnt = 1

        val result = StringBuilder()

        result.append("<wpt ")
        result.append("lat=\"").append(lat.toString()).append("\" ")
        result.append("lon=\"").append(lon.toString()).append("\">\n ")
        result.append("<time>").append(date).append("</time>\n")
        result.append("</wpt>\n")
        return result.toString()
    }
}
