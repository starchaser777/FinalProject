package kr.ac.kopo.finalproject

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class DiaryListActivity : AppCompatActivity() {

    private lateinit var diaryRepository: DiaryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_list)

        diaryRepository = DiaryRepository(this)

        val diaryListView = findViewById<ListView>(R.id.diaryListView)
        val diaries = diaryRepository.getAllDiaries()
        val diaryTitles = diaries.map { "\n${it.date}\n제목: ${it.title}\n내용: ${it.content}\n" }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, diaryTitles)
        diaryListView.adapter = adapter

        diaryListView.setOnItemClickListener { parent, view, position, id ->
            val selectedDiary = diaries[position]
            // 여기서 일기 상세보기 화면으로 이동할 수 있도록 구현할 수 있습니다.
            // 예: Intent를 사용하여 상세보기 액티비티로 이동
        }
    }
}
