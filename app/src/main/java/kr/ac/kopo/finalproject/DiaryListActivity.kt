package kr.ac.kopo.finalproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DiaryListActivity : AppCompatActivity() {

    private lateinit var diaryRepository: DiaryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_list)

        diaryRepository = DiaryRepository(this)
        val btnBackToTitle = findViewById<ImageButton>(R.id.btnBackToTitle)
        val diaryListView = findViewById<ListView>(R.id.diaryListView)
        val noDiariesTextView = findViewById<TextView>(R.id.noDiariesTextView)

        // 일기 데이터를 가져와 날짜 순으로 정렬
        val diaries = diaryRepository.getAllDiaries().sortedBy { it.date }

        if (diaries.isEmpty()) {
            diaryListView.visibility = View.GONE
            noDiariesTextView.visibility = View.VISIBLE
        } else {
            val diaryTitles = diaries.map { "\n${it.date}\n제목: ${it.title}\n내용: ${it.content}\n" }

            // 정렬된 데이터를 ListView에 설정
            val adapter = ArrayAdapter(this, R.layout.list_item, R.id.listItemTextView, diaryTitles)
            diaryListView.adapter = adapter

            diaryListView.setOnItemClickListener { _, _, position, _ ->
                val selectedDiary = diaries[position]
                val dateParts = selectedDiary.date.split("년 ", "월 ", "일")
                val year = dateParts[0].toInt()
                val month = dateParts[1].toInt()
                val day = dateParts[2].toInt()

                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("year", year)
                    putExtra("month", month)
                    putExtra("day", day)
                }
                startActivity(intent)
            }
        }

        btnBackToTitle.setOnClickListener {
            val intent = Intent(this, TitleActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
