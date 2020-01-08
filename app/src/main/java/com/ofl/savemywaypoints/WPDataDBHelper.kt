package com.ofl.savemywaypoints

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class WPDataDbHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase){
        db.execSQL(SQL_CREATE_ENTRIES);
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "WPData.db"

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${DBContract.WPData.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${DBContract.WPData.COLUMN_NAME_DATE} DATE," +
                    "${DBContract.WPData.COLUMN_NAME_TIME} TIME," +
                    "${DBContract.WPData.COLUMN_NAME_LONG} FLOAT," +
                    "${DBContract.WPData.COLUMN_NAME_LAT} FLOAT)"

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${DBContract.WPData.TABLE_NAME}"
    }

}