package org.kvpbldsck.notes.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DbManager(context: Context) {

    var dbName = "Notes"
    var dbTable = "Notes"

    private val columnId = "ID"
    private val columnTitle = "Title"
    private val columnDescription = "Description"

    var dbVersion = 1

    //CREATE TABLE IF NOT EXISTS MyNotes (ID INTEGER PRIMARY KEY,title TEXT, Description TEXT);"
    val sqlCreateTable =
        "CREATE TABLE IF NOT EXISTS $dbTable($columnId INTEGER PRIMARY KEY, $columnTitle TEXT, $columnDescription TEXT);"

    private var sqlDb: SQLiteDatabase? = null

    init {
        sqlDb = DatabaseHelperNotes(context).writableDatabase
    }

    inner class DatabaseHelperNotes(context: Context) :
        SQLiteOpenHelper(context, dbName, null, dbVersion) {

        private var context: Context? = context

        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(sqlCreateTable)
            Toast
                .makeText(this.context, "database created...", Toast.LENGTH_SHORT)
                .show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("DROP TABLE IF EXISTS $dbTable")
        }
    }

    fun insert(values: ContentValues): Long {
        return sqlDb!!.insert(dbTable, "", values)
    }

    fun query(
        projection: Array<String>,
        selection: String,
        selectionArgs: Array<String>,
        sorOrder: String
    ): Cursor {
        val qb = SQLiteQueryBuilder()
        qb.tables = dbTable
        return qb.query(sqlDb, projection, selection, selectionArgs, null, null, sorOrder)
    }

    fun delete(selection: String, selectionArgs: Array<String>): Int {
        return sqlDb!!.delete(dbTable, selection, selectionArgs)
    }

    fun update(values: ContentValues, selection: String, selectionArgs: Array<String>): Int {
        return sqlDb!!.update(dbTable, values, selection, selectionArgs)
    }
}