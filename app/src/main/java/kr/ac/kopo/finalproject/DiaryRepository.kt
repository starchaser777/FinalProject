package kr.ac.kopo.finalproject

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import java.text.SimpleDateFormat
import java.util.Locale

class DiaryRepository(context: Context) {
    private val dbHelper = SQLiteHelper(context)

    fun saveDiary(date: String, title: String, content: String): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(SQLiteHelper.COLUMN_DATE, date)
            put(SQLiteHelper.COLUMN_TITLE, title)
            put(SQLiteHelper.COLUMN_CONTENT, content)
        }
        val result = db.insert(SQLiteHelper.TABLE_DIARY, null, values)
        db.close()
        return result != -1L
    }

    fun updateDiary(date: String, title: String, content: String): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(SQLiteHelper.COLUMN_TITLE, title)
            put(SQLiteHelper.COLUMN_CONTENT, content)
        }
        val result = db.update(SQLiteHelper.TABLE_DIARY, values, "${SQLiteHelper.COLUMN_DATE}=?", arrayOf(date))
        db.close()
        return result > 0
    }

    fun deleteDiary(date: String): Boolean {
        val db = dbHelper.writableDatabase
        val result = db.delete(SQLiteHelper.TABLE_DIARY, "${SQLiteHelper.COLUMN_DATE}=?", arrayOf(date))
        db.close()
        return result > 0
    }

    fun getDiary(date: String): Diary? {
        val db = dbHelper.readableDatabase
        var diary: Diary? = null
        val cursor: Cursor = db.query(SQLiteHelper.TABLE_DIARY, null, "${SQLiteHelper.COLUMN_DATE}=?", arrayOf(date), null, null, null)
        if (cursor.moveToFirst()) {
            val title = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_CONTENT))
            diary = Diary(date, title, content)
        }
        cursor.close()
        db.close()
        return diary
    }

    fun getAllDiaries(): List<Diary> {
        val db = dbHelper.readableDatabase
        val diaryList = mutableListOf<Diary>()
        val cursor: Cursor = db.query(SQLiteHelper.TABLE_DIARY, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val date = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_DATE))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_TITLE))
                val content = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_CONTENT))
                diaryList.add(Diary(date, title, content))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return diaryList
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
}


