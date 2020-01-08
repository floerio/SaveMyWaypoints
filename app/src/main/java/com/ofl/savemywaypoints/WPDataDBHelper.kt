package com.ofl.savemywaypoints

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.widget.Toast

class WPDataDbHelper(context: Context, factory: SQLiteDatabase.CursorFactory?, version: Int): SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase){
        db.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DROP_TABLE)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun addWP(date: String, long: Double, lat: Double) {
        val values = ContentValues()

        values.put(DBContract.WPData.COLUMN_NAME_DATE, date);
        values.put(DBContract.WPData.COLUMN_NAME_LONG, long);
        values.put(DBContract.WPData.COLUMN_NAME_LAT, lat);

        val thisDB = this.writableDatabase

        thisDB.insert(DBContract.WPData.TABLE_NAME, null, values)
        thisDB.close()
    }


    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "WPData.db"

        private const val SQL_CREATE_TABLE =
            "CREATE TABLE ${DBContract.WPData.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${DBContract.WPData.COLUMN_NAME_DATE} TEXT," +
                    "${DBContract.WPData.COLUMN_NAME_LONG} REAL," +
                    "${DBContract.WPData.COLUMN_NAME_LAT} REAL)"

        private const val SQL_DROP_TABLE = "DROP TABLE IF EXISTS ${DBContract.WPData.TABLE_NAME}"
    }

}