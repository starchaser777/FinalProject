package kr.ac.kopo.finalproject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LockActivity : AppCompatActivity() {

    private lateinit var edtPassword: EditText
    private lateinit var btnUnlock: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock)

        sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

        val savedPassword = sharedPreferences.getString("app_password", null)
        if (savedPassword == null) {
            // 비밀번호가 설정되지 않은 경우 SetPasswordActivity로 이동
            startActivity(Intent(this, SetPasswordActivity::class.java))
            finish()
            return
        }

        edtPassword = findViewById(R.id.edtPassword)
        btnUnlock = findViewById(R.id.btnUnlock)

        btnUnlock.setOnClickListener {
            val inputPassword = edtPassword.text.toString()
            if (inputPassword == savedPassword) {
                sharedPreferences.edit().putBoolean("is_logged_in", true).apply()
                val intent = Intent(this, TitleActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "비밀번호가 틀렸습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }
}