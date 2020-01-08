package com.ofl.savemywaypoints

import android.provider.BaseColumns

object DBContract {
    object WPData : BaseColumns {
        const val TABLE_NAME = "wpData"
        const val COLUMN_NAME_DATE = "date"
        const val COLUMN_NAME_TIME = "time"
        const val COLUMN_NAME_LONG = "longitude"
        const val COLUMN_NAME_LAT = "latitude"
    }
}