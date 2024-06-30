// DiaryRepository.kt
package kr.ac.kopo.finalproject

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DiaryRepository(context: Context) {
    private val dbHelper: DBHelper = DBHelper(context)

    fun getDiary(date: String): Diary? {
        val db = dbHelper.readableDatabase
        val cursor = db.query("Diary", arrayOf("title", "content"), "date = ?", arrayOf(date), null, null, null)
        var diary: Diary? = null
        if (cursor.moveToFirst()) {
            val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
            val content = cursor.getString(cursor.getColumnIndexOrThrow("content"))
            diary = Diary(date, title, content)
        }
        cursor.close()
        return diary
    }

    fun saveDiary(date: String, title: String, content: String): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("date", date)
            put("title", title)
            put("content", content)
        }
        val newRowId = db.insert("Diary", null, values)
        return newRowId != -1L
    }

    fun updateDiary(date: String, title: String, content: String): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("title", title)
            put("content", content)
        }
        val count = db.update("Diary", values, "date = ?", arrayOf(date))
        return count > 0
    }

    fun deleteDiary(date: String): Boolean {
        val db = dbHelper.writableDatabase
        val count = db.delete("Diary", "date = ?", arrayOf(date))
        return count > 0
    }

    fun getAllDiaryDates(): List<String> {
        val db = dbHelper.readableDatabase
        val cursor = db.query("Diary", arrayOf("date"), null, null, null, null, null)
        val dates = mutableListOf<String>()
        with(cursor) {
            while (moveToNext()) {
                dates.add(getString(getColumnIndexOrThrow("date")))
            }
        }
        cursor.close()
        return dates
    }

    fun getAllDiaries(): List<Diary> {
        val db = dbHelper.readableDatabase
        val cursor = db.query("Diary", arrayOf("date", "title", "content"), null, null, null, null, null)
        val diaries = mutableListOf<Diary>()
        with(cursor) {
            while (moveToNext()) {
                val date = getString(getColumnIndexOrThrow("date"))
                val title = getString(getColumnIndexOrThrow("title"))
                val content = getString(getColumnIndexOrThrow("content"))
                diaries.add(Diary(date, title, content))
            }
        }
        cursor.close()
        return diaries
    }
}

private class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DELETE_TABLE)
        onCreate(db)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Diary.db"
        private const val CREATE_TABLE =
            "CREATE TABLE Diary (date TEXT PRIMARY KEY, title TEXT, content TEXT)"
        private const val DELETE_TABLE = "DROP TABLE IF EXISTS Diary"
    }
}
