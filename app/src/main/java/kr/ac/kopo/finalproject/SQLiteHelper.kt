package kr.ac.kopo.finalproject

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_DIARY ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_DATE TEXT,"
                + "$COLUMN_TITLE TEXT,"
                + "$COLUMN_CONTENT TEXT)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DIARY")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "DiaryDatabase.db"
        const val TABLE_DIARY = "diary"
        const val COLUMN_ID = "_id"
        const val COLUMN_DATE = "date"
        const val COLUMN_TITLE = "title"
        const val COLUMN_CONTENT = "content"
    }
}
