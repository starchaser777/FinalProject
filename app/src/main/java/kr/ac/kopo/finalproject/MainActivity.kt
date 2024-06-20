package kr.ac.kopo.finalproject

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileInputStream
import java.util.Calendar


class MainActivity : AppCompatActivity() {

    lateinit var datePicker: DatePicker; //  datePicker - 날짜를 선택하는 달력
    lateinit var viewDatePick: TextView; //  viewDatePick - 선택한 날짜를 보여주는 textView
    lateinit var editTitle: EditText; // editContent - 선택한 날짜의 일기를 쓰거나 기존에 저장된 일기가 있다면 보여주고 수정하는 영역
    lateinit var editContent: EditText; // editContent - 선택한 날짜의 일기를 쓰거나 기존에 저장된 일기가 있다면 보여주고 수정하는 영역
    lateinit var textCount: TextView; // textCount - 제한 글자수와 현재 입력된 글자수를 보여주는 영역

    lateinit var btnSave: Button; //  btnSave - 선택한 날짜의 일기 저장 버튼
    lateinit var btnUpdate: Button; //  btnSave - 선택한 날짜의 일기 수정(덮어쓰기) 버튼
    lateinit var btnDelete: Button; //  btnSave - 선택한 날짜의 일기 삭제 버튼
    var fileName: String? = null //  fileName - 선택된 날짜의 파일 이름
    var originalContent: String? = null
    var originalTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("심플기록 일기장")

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        datePicker = findViewById<DatePicker>(R.id.datePicker)
        viewDatePick = findViewById<TextView>(R.id.viewDatePick)
        editTitle = findViewById<EditText>(R.id.editTitle)
        editContent = findViewById<EditText>(R.id.editContent)
        textCount = findViewById<TextView>(R.id.textCount)
        btnSave = findViewById<Button>(R.id.btnSave)
        btnUpdate = findViewById<Button>(R.id.btnUpdate)
        btnDelete = findViewById<Button>(R.id.btnDelete)

        val c = Calendar.getInstance()
        val cYear = c[Calendar.YEAR]
        val cMonth = c[Calendar.MONTH]
        val cDay = c[Calendar.DAY_OF_MONTH]

        checkedDay(cYear, cMonth, cDay)

        datePicker.init(
            datePicker.year, datePicker.month, datePicker.dayOfMonth
        ) { view, year, monthOfYear, dayOfMonth ->
            checkedDay(year, monthOfYear, dayOfMonth)
        }

        btnSave.setOnClickListener {
            saveDiary(fileName!!)
        }

        btnUpdate.setOnClickListener {
            showConfirmationDialog("일기를 수정하시겠습니까?") {
                updateDiary(fileName!!)
            }
        }

        btnDelete.setOnClickListener {
            showConfirmationDialog("일기를 삭제하시겠습니까?") {
                deleteDiary(fileName!!)
            }
        }

        editContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val input: String = editContent.getText().toString()
                textCount.setText(input.length.toString() + " / 500")
            }

            override fun afterTextChanged(s: Editable) {}
        })

    }

    // 일기 파일 읽기
    private fun checkedDay(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        viewDatePick.text = "$year - ${monthOfYear + 1} - $dayOfMonth"

        fileName = "$year${monthOfYear + 1}$dayOfMonth.txt"

        var fis: FileInputStream? = null
        try {
            fis = openFileInput(fileName)
            val fileData = ByteArray(fis.available())
            fis.read(fileData)
            fis.close()
            val str = String(fileData, charset("UTF-8"))

            val splitData = str.split("\n", limit = 2)
            if (splitData.size == 2) {
                editTitle.setText(splitData[0])
                editContent.setText(splitData[1])
                originalTitle = splitData[0]
                originalContent = splitData[1]
            } else {
                editTitle.setText("")
                editContent.setText(str)
                originalTitle = ""
                originalContent = str
            }

            buttonVisibility(true)
        } catch (e: Exception) {
            editTitle.setText("")
            editContent.setText("")
            originalTitle = ""
            originalContent = ""
            buttonVisibility(false)
            e.printStackTrace()
        }
    }

    private fun saveDiary(readDay: String) {
        try {
            val title: String = editTitle.text.toString().trim()
            val content: String = editContent.text.toString().trim()
            if (title.isBlank() || content.isBlank()) {
                Toast.makeText(applicationContext, "제목 혹은 내용이 없어 일기를 저장할 수 없습니다.", Toast.LENGTH_SHORT).show()
                return
            }

            val file = File(filesDir, readDay)
            file.writeText("$title\n$content", Charsets.UTF_8)
            Toast.makeText(applicationContext, "일기가 저장되었습니다.", Toast.LENGTH_SHORT).show()
            buttonVisibility(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(applicationContext, "일기 저장에 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteDiary(readDay: String) {
        try {
            val file = File(filesDir, readDay)
            if (file.exists()) {
                file.delete()
                editTitle.setText("")
                editContent.setText("")
                originalTitle = ""
                originalContent = ""
                Toast.makeText(applicationContext, "일기가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                buttonVisibility(false)
            } else {
                Toast.makeText(applicationContext, "삭제할 일기가 없습니다.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(applicationContext, "일기 삭제에 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateDiary(readDay: String) {
        try {
            val title: String = editTitle.text.toString().trim()
            val content: String = editContent.text.toString().trim()
            if (title.isBlank() || content.isBlank()) {
                Toast.makeText(applicationContext, "제목 혹은 내용이 없어 일기를 수정할 수 없습니다.", Toast.LENGTH_SHORT).show()
                return
            }

            val file = File(filesDir, readDay)
            if (file.exists()) {
                file.writeText("$title\n$content", Charsets.UTF_8)
                Toast.makeText(applicationContext, "일기가 수정되었습니다.", Toast.LENGTH_SHORT).show()
                originalTitle = title
                originalContent = content
            } else {
                Toast.makeText(applicationContext, "수정할 일기가 없습니다.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(applicationContext, "일기 수정에 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showConfirmationDialog(message: String, onConfirm: () -> Unit) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setPositiveButton("예") { _, _ ->
                onConfirm()
            }
            .setNegativeButton("아니오") { dialog, _ ->
                // 원래 내용을 복원
                editTitle.setText(originalTitle)
                editContent.setText(originalContent)
                dialog.dismiss()
            }
        builder.create().show()
    }

    private fun buttonVisibility(isDiaryExist: Boolean) {
        if (isDiaryExist) {
            btnSave.visibility = View.GONE
            btnUpdate.visibility = View.VISIBLE
            btnDelete.visibility = View.VISIBLE
        } else {
            btnSave.visibility = View.VISIBLE
            btnUpdate.visibility = View.GONE
            btnDelete.visibility = View.GONE
        }
    }

// 추가할 기능 - 텍스트 입력시 밑쪽 작성 버튼도 함께 보이도록 설정하기, 제목 부분 추가, 작성한 일기 목록화, 그간 쓴 일기 목록 탭호스트, 함수 필요없는 기능 다듬기
// 마무리 - values 파일에 스타일 지정해서 적용, 디자인
}