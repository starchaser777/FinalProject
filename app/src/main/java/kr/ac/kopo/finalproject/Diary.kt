package kr.ac.kopo.finalproject

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

data class Diary(val date: String, val title: String, val content: String) {
    fun getFormattedDate(): String {
        val originalFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val targetFormat = SimpleDateFormat("yyyy년 M월 d일", Locale.getDefault())
        val originalDate: Date = originalFormat.parse(date)
        return targetFormat.format(originalDate)
    }

    fun getComparableDate(): Long {
        val format = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val date: Date = format.parse(this.date)
        return date.time
    }
}
