package com.ofl.savemywaypoints

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import java.text.DecimalFormat

class ListOfWPActivity : AppCompatActivity() {

    private lateinit var mDB: WPDataDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_wp)

        /*
        val message = intent.getStringExtra(EXTRA_MESSAGE)

        // Capture the layout's TextView and set the string as its text
        val textView = findViewById<TextView>(R.id.textView).apply {
            text = message
        }
        */


        val textView = findViewById<TextView>(R.id.textView)
        textView.append("\n")

        // get the database
        mDB = WPDataDbHelper(this, null, 1)

        var cursor = mDB.getAllName()

        var long: String = ""
        var lat: String = ""
        val dec = DecimalFormat("#,###.00")

        if (cursor!!.count > 0) {
            cursor!!.moveToFirst()
            textView.append((cursor.getString(cursor.getColumnIndex(DBContract.WPData.COLUMN_NAME_DATE))))
            while (cursor.moveToNext()) {

                // get coordinates
                long = dec.format(cursor.getDouble(cursor.getColumnIndex(DBContract.WPData.COLUMN_NAME_LONG)))
                lat = dec.format(cursor.getDouble(cursor.getColumnIndex(DBContract.WPData.COLUMN_NAME_LAT)))

                textView.append((cursor.getString(cursor.getColumnIndex(DBContract.WPData.COLUMN_NAME_DATE))))
                textView.append(" / ")
                textView.append(long)
                textView.append(" / ")
                textView.append(lat)
                textView.append("\n")
            }
        } else {
            textView.append("Sorry, no data!")
            textView.append("\n")
        }

        cursor.close()

    }
}
