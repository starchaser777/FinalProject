package kr.ac.kopo.finalproject

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileInputStream
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    lateinit var datePicker: DatePicker
    lateinit var viewDatePick: TextView
    lateinit var editTitle: EditText
    lateinit var editContent: EditText
    lateinit var textCount: TextView

    lateinit var btnSave: Button
    lateinit var btnUpdate: Button
    lateinit var btnDelete: Button
    var fileName: String? = null
    var originalContent: String? = null
    var originalTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setTitle("심플기록 일기장")

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        val btnBackToTitle = findViewById<ImageButton>(R.id.btnBackToTitle)
        datePicker = findViewById(R.id.datePicker)
        viewDatePick = findViewById(R.id.viewDatePick)
        editTitle = findViewById(R.id.editTitle)
        editContent = findViewById(R.id.editContent)
        textCount = findViewById(R.id.textCount)
        btnSave = findViewById(R.id.btnSave)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)

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

        btnBackToTitle.setOnClickListener {
            val intent = Intent(this, TitleActivity::class.java)
            startActivity(intent)
            finish()
        }

        editContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val input: String = editContent.text.toString()
                textCount.text = "${input.length} / 500"
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

    // 일기 작성(저장)
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

    // 일기 삭제
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

    // 일기 수정
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
                editTitle.setText(originalTitle)
                editContent.setText(originalContent)
                dialog.dismiss()
            }
        builder.create().show()
    }

    // 버튼 visibility 설정
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
}
