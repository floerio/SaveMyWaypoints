package com.ofl.savemywaypoints

import java.io.File

class GPXFile(){

    private val mWPList = arrayListOf<WPEntry>()

    // add one wp to the internal list
    fun addPoints(pDate: String, pLong: Double, pLat: Double ) {
        val wp = com.ofl.savemywaypoints.WPEntry(pDate, pLong, pLat)

        mWPList.add(wp)
    }

    // Add entire list of wps to internal list
    fun addPoints(pList: ArrayList<WPEntry>){
        mWPList.addAll(pList)
    }

    fun createFile(pFileName: String): File {

        val gpxFile = File.createTempFile(pFileName,"")

        gpxFile.writeText("Hello Wordl")
        gpxFile.writeText("Hello zweite Welt")

        return gpxFile

    }
}