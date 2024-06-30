package kr.ac.kopo.finalproject

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class DiaryListActivity : AppCompatActivity() {

    private lateinit var diaryRepository: DiaryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_list)

        diaryRepository = DiaryRepository(this)
        val btnBackToTitle = findViewById<ImageButton>(R.id.btnBackToTitle)
        val diaryListView = findViewById<ListView>(R.id.diaryListView)

        // 일기 데이터를 가져와 날짜 순으로 정렬
        val diaries = diaryRepository.getAllDiaries().sortedBy { it.date }
        val diaryTitles = diaries.map { "\n${it.date}\n제목: ${it.title}\n내용: ${it.content}\n" }

        // 정렬된 데이터를 ListView에 설정
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, diaryTitles)
        diaryListView.adapter = adapter

        btnBackToTitle.setOnClickListener {
            val intent = Intent(this, TitleActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
