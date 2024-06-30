package kr.ac.kopo.finalproject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SetPasswordActivity : AppCompatActivity() {

    private lateinit var edtNewPassword: EditText
    private lateinit var edtConfirmPassword: EditText
    private lateinit var btnSetPassword: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_password)

        sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

        edtNewPassword = findViewById(R.id.edtNewPassword)
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword)
        btnSetPassword = findViewById(R.id.btnSetPassword)

        btnSetPassword.setOnClickListener {
            val newPassword = edtNewPassword.text.toString()
            val confirmPassword = edtConfirmPassword.text.toString()

            if (newPassword == confirmPassword && newPassword.length == 3) {
                sharedPreferences.edit().putString("app_password", newPassword).apply()
                Toast.makeText(this, "비밀번호가 설정되었습니다", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LockActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "비밀번호가 일치하지 않거나 길이가 맞지 않습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }
}