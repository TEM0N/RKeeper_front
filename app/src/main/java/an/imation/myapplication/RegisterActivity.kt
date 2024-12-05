package an.imation.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import timber.log.Timber
import java.io.IOException

data class ResponseData(val message: String, val token: String?)

class RegisterActivity(
    private val client: OkHttpClient = OkHttpClient() // Теперь можно передать фейковый клиент
) : AppCompatActivity() {
    var authToken: String? = null
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var repeatPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        email = findViewById(R.id.mail)
        password = findViewById(R.id.passsword)
        repeatPassword = findViewById(R.id.repeat_password)

        val signInButton: Button = findViewById(R.id.register_button)
        signInButton.setOnClickListener {
            if (password.text.toString() != repeatPassword.text.toString()) {
                Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
            } else {
                sendPostRequest()
            }
        }
    }

    fun sendPostRequest() {
        Timber.i("Отправка POST-запроса на регистрацию")
        val formBody = FormBody.Builder()
            .add("grant_type", "password")
            .add("username", email.text.toString())
            .add("password", password.text.toString())
            .add("client_id", "your_client_id")
            .add("client_secret", "your_client_secret")
            .build()

        val request = Request.Builder()
            .url("http://10.0.2.2:8080/auth/register")
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Timber.e(e, "Ошибка при отправке запроса")
                sendLogsToServer("Ошибка при отправке запроса: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@RegisterActivity, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    Timber.i("Успешный ответ: $responseData")

                    responseData?.let {
                        val gson = Gson()
                        val data = gson.fromJson(it, ResponseData::class.java)
                        authToken = data.token

                        Timber.i("Извлеченный токен: $authToken")
                        runOnUiThread {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Ответ: ${data.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } ?: run {
                        Timber.w("Ответ тела пустой")
                    }
                } else {
                    Timber.w("Ошибка ответа: ${response.message}")
                    sendLogsToServer("Ошибка ответа: ${response.message}")
                }
            }
        })
    }

    private fun sendLogsToServer(log: String) {
        val url = "http://10.0.2.2:8080/auth/register" // Замените на ваш URL сервера
        val client = OkHttpClient()

        val json = "{\"log\": \"$log\"}"
        val requestBody = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json)

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Timber.e(e, "Ошибка при отправке логов на сервер")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Timber.i("Логи успешно отправлены на сервер: ${response.body?.string()}")
                } else {
                    Timber.w("Ошибка при отправке логов: ${response.message}")
                }
            }
        })
    }

    fun login(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}