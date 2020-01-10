package com.ofl.savemywaypoints

import android.content.*
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import androidx.core.content.FileProvider
import java.lang.Integer.parseInt

class FileContentProvider : FileProvider() {

    var mData: Array<String>? = null
    private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    override fun onCreate(): Boolean {
        initializeUriMatching()

/*
        val context = context
        mData = context!!.resources.getStringArray(R.array.sentence)
*/

        return true
    }
    private fun initializeUriMatching() {
        sUriMatcher.addURI(Contract.AUTHORITY, Contract.CONTENT_PATH + "/#", 1)
        sUriMatcher.addURI(Contract.AUTHORITY, Contract.CONTENT_PATH, 0)
    }
/*
    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor {
        var id = -1


        when (sUriMatcher.match(uri)) {
            0 -> {
                id = Contract.ALL_ITEMS
                if (selection != null) {
                    id = Integer.parseInt(selectionArgs!![0])
                }
            }
            1 -> id = parseInt(uri.getLastPathSegment())
            UriMatcher.NO_MATCH -> {
                id = -1
            }
            else -> {
                id = -1
            }
        }

        return populateCursor(id)
    }
*/

    /*
    private fun populateCursor(id: Int): Cursor {
        val cursor = MatrixCursor(arrayOf(Contract.CONTENT_PATH))
        if (id == Contract.ALL_ITEMS) {
            for (i in 0 until mData!!.size) {
                val word = mData!![i]
                cursor.addRow(arrayOf<Any>(word))
            }
        } else if (id >= 0) {
            val word = mData!![id]
            cursor.addRow(arrayOf<Any>(word))
        }
        return cursor
    }
    */

    override fun getType(uri: Uri): String? {
        return null
    }
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }


}


