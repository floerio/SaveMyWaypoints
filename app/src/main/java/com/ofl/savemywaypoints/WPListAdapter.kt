package com.ofl.savemywaypoints

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.text.DecimalFormat

class WPListAdapter(private val context: Context, private val dataSource: ArrayList<WPEntry>): BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val dec = DecimalFormat("#,###.00")

        // Get view for row item
        val rowView = inflater.inflate(R.layout.wp_entry, parent, false)

        val wpEntry = getItem(position) as WPEntry

        val theDate = rowView.findViewById(R.id.wp_date) as TextView
        theDate.text = wpEntry.date

        val theLong = rowView.findViewById(R.id.wp_long) as TextView
        theLong.text = " Long: <" + dec.format(wpEntry.lon) + ">"

        val theLat = rowView.findViewById(R.id.wp_lat) as TextView
        theLat.text = " Lat: <" + dec.format(wpEntry.lat) + ">"

        val myButton = rowView.findViewById(R.id.wp_button_delete) as Button
        myButton.setOnClickListener{
            Toast.makeText(context, "Pressed " + wpEntry.date , Toast.LENGTH_LONG).show()
        }
        return rowView
    }
}