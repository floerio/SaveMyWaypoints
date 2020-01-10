package com.ofl.savemywaypoints

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import java.io.*

class GPXFile(val context: Context){

    private val mWPList = arrayListOf<WPEntry>()
    private val FILE_NAME = "SaveMyWaypoints.gpx"

    // add one wp to the internal list
    fun addPoints(pDate: String, pLong: Double, pLat: Double ) {
        val wp = com.ofl.savemywaypoints.WPEntry(pDate, pLong, pLat)

        mWPList.add(wp)
    }

    // Add entire list of wps to internal list
    fun addPoints(pList: ArrayList<WPEntry>){
        mWPList.addAll(pList)
    }

    //
    // Create a file with the local WP data
    fun createFile(): File{

        var fos: FileOutputStream

        try {
            fos =  context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
            fos.write("HHHHH".toByteArray())
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val f = File(FILE_NAME)

        return f

    }
}