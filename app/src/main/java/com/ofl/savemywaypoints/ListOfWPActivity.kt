package com.ofl.savemywaypoints

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.graphics.ColorSpace.Model
import android.net.wifi.WifiManager
import android.widget.ArrayAdapter
import android.widget.ListView


class ListOfWPActivity : AppCompatActivity() {

    private lateinit var mDB: WPDataDbHelper
    private lateinit var mListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_list_of_wp)

        mListView = findViewById<ListView>(R.id.wp_list_view)

        val list = arrayListOf<WPEntry>()
/*
        val wp1 =  WPEntry("Montag", 10.0, 5.0)
        val wp2 =  WPEntry("Dienstag", 20.0, 4.0)
        val wp3 =  WPEntry("Mittwoch", 30.0, 3.0)
        val wp4 =  WPEntry("Donnerstag", 40.0, 2.0)
        val wp5 =  WPEntry("Freitag", 50.0,1.0)

        list.add(wp1)
        list.add(wp2)
        list.add(wp3)
        list.add(wp4)
        list.add(wp5)

        // val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
*/

        // get the database and all WP data elements
        mDB = WPDataDbHelper(this, null, 1)
        val cursor = mDB.getAllWP()

        with(cursor) {
            while (moveToNext()) {

                val wp = WPEntry(
                    getString(getColumnIndex(DBContract.WPData.COLUMN_NAME_DATE)),
                    getDouble(getColumnIndex(DBContract.WPData.COLUMN_NAME_LONG)),
                    getDouble(getColumnIndex(DBContract.WPData.COLUMN_NAME_LAT)))

                list.add(wp)
            }
        }

        cursor.close()

        val adapter = WPListAdapter(this, list )

        mListView.adapter = adapter


    }
}
