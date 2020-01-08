package com.ofl.savemywaypoints

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

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
         */

        val textView = findViewById<TextView>(R.id.textView)

        // get the database
        mDB = WPDataDbHelper(this, null, 1)

        val cursor = mDB.getAllName()
        cursor!!.moveToFirst()
        textView.append((cursor.getString(cursor.getColumnIndex(DBContract.WPData.COLUMN_NAME_DATE))))
        while (cursor.moveToNext()) {
            textView.append((cursor.getString(cursor.getColumnIndex(DBContract.WPData.COLUMN_NAME_DATE))))
            textView.append("\n")
        }
        cursor.close()

    }
}
