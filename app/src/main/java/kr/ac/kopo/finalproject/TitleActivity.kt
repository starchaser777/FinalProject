package kr.ac.kopo.finalproject

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class TitleActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

        setContentView(R.layout.activity_title)

        val btnGoToDiary = findViewById<Button>(R.id.btnGoToWriteDiary)
        btnGoToDiary.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val btnGoToDiaryList = findViewById<Button>(R.id.btnGoToDiaryList)
        btnGoToDiaryList.setOnClickListener {
            val intent = Intent(this, DiaryListActivity::class.java)
            startActivity(intent)
        }

        val btnResetPassword = findViewById<Button>(R.id.btnResetPassword)
        btnResetPassword.setOnClickListener {
            showResetPasswordDialog()
        }

        val btnExitApp = findViewById<Button>(R.id.btnExitApp)
        btnExitApp.setOnClickListener {
            showExitConfirmationDialog()
        }
    }

    private fun showResetPasswordDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_reset_password, null)
        val edtNewPassword = dialogView.findViewById<EditText>(R.id.edtNewPassword)
        val edtConfirmPassword = dialogView.findViewById<EditText>(R.id.edtConfirmPassword)

        AlertDialog.Builder(this)
            .setTitle("비밀번호 재설정")
            .setView(dialogView)
            .setPositiveButton("확인") { dialog, _ ->
                val newPassword = edtNewPassword.text.toString()
                val confirmPassword = edtConfirmPassword.text.toString()

                if (newPassword == confirmPassword && newPassword.length == 3) {
                    sharedPreferences.edit().putString("app_password", newPassword).apply()
                    Toast.makeText(this, "비밀번호가 변경되었습니다", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "비밀번호가 일치하지 않거나 길이가 맞지 않습니다", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("앱 종료")
            .setMessage("앱을 종료하시겠습니까?")
            .setPositiveButton("예") { _, _ ->
                finishAffinity()
            }
            .setNegativeButton("아니오") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}
