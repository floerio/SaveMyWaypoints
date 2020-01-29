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

        // get the database and all WP data elements
        mDB = WPDataDbHelper(this, null, 1)

        val list = mDB.getAllWPasList()

        val adapter = WPListAdapter(this, list )

        mListView.adapter = adapter


    }
}
