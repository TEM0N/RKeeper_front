package an.imation.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException

class LoginActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    private lateinit var emailInput: EditText  // Изменено на EditText
    private lateinit var passwordInput: EditText  // Изменено на EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailInput = findViewById(R.id.email)  // Инициализация EditText для email
        passwordInput = findViewById(R.id.password)  // Инициализация EditText для пароля

        val logInButton: Button = findViewById(R.id.login_button)

        logInButton.setOnClickListener {
            val username = emailInput.text.toString()  // Получаем текст из EditText
            val password = passwordInput.text.toString()  // Получаем текст из EditText
            sendPostRequest(username, password)
        }
    }

    private fun sendPostRequest(username: String, password: String) {
        // Данные для отправки
        val formBody = FormBody.Builder()
            .add("grant_type", "password")  // Замените на ваше значение
            .add("username", username)  // Используем username из EditText
            .add("password", password)  // Используем password из EditText
            .add("client_id", "")  // Замените на ваш client_id
            .add("client_secret", "")  // Замените на ваш client_secret
            .build()

        val request = Request.Builder()
            .url("http://10.0.2.2:8080/auth/login")
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@LoginActivity, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "Ответ: $responseData", Toast.LENGTH_SHORT).show()
                    }
                    val dialog = CodeInputDialog()
                    dialog.show(supportFragmentManager, "CodeInputDialog")
                } else {
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "Ошибка: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    fun register(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}