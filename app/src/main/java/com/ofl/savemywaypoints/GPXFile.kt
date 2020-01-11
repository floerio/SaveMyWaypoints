package com.ofl.savemywaypoints

import android.content.Context
import android.os.Environment
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream


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

        val appDir = context.filesDir

        val fp = File(appDir,"/external_files")

        fp.mkdirs()

        val f = File(fp.toString(), FILE_NAME)

        try {
            f.createNewFile()

            val fos = FileOutputStream(f)
            addWPsToFile(fos)
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
            TODO("Signalisieren dass das Erzeugen der Datei fehlgeschlagen ist")
        }

        return f

    }

    private fun addWPsToFile(stream: FileOutputStream) {

        stream.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\n".toByteArray())
        stream.write("<gpx version=\"1.1\" creator=\"Ersteller der Datei\">\n".toByteArray())
        for (wp in mWPList) {
            stream.write(wp.getWPAsWaypointEntry().toByteArray())
        }
        stream.write("</gpx>\n".toByteArray())

    }
}