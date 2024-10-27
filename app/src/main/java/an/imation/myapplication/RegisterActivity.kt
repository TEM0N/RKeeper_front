package an.imation.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException

class RegisterActivity : AppCompatActivity() {
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val signInButton: Button = findViewById(R.id.register_button)
        signInButton.setOnClickListener {
            sendPostRequest()
        }
    }

    private fun sendPostRequest() {
        val json = "{\"username\":\"your_username\",\"password\":\"your_password\"}" // Замените на ваше JSON тело
        val requestBody = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json)

        val request = Request.Builder()
            .url("http://0.0.0.0:8080")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@RegisterActivity, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    // Обработайте ответ
                    runOnUiThread {
                        Toast.makeText(this@RegisterActivity, "Ответ: $responseData", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@RegisterActivity, "Ошибка: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}