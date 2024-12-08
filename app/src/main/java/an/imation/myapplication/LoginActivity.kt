package an.imation.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.*
import java.io.IOException
import java.util.Locale

class LoginActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    private lateinit var emailInput: EditText  // Изменено на EditText
    private lateinit var passwordInput: EditText  // Изменено на EditText
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailInput = findViewById(R.id.email)  // Инициализация EditText для email
        passwordInput = findViewById(R.id.password)  // Инициализация EditText для пароля
        searchView = findViewById(R.id.search_view)

        val logInButton: Button = findViewById(R.id.login_button)

        logInButton.setOnClickListener {
            val username = emailInput.text.toString()  // Получаем текст из EditText
            val password = passwordInput.text.toString()  // Получаем текст из EditText
            sendPostRequest(username, password)
        }
        // Загрузить язык из SharedPreferences
        val prefs: SharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        val language = prefs.getString("My_Lang", "en") ?: "en"
        setLocale(language)

        val changeLanguageButton: FloatingActionButton = findViewById(R.id.change_language_button)
        changeLanguageButton.setOnClickListener {
            val newLanguage = if (language == "en") "ru" else "en"
            setLocale(newLanguage)

            // Сохранить выбранный язык
            with(prefs.edit()) {
                putString("My_Lang", newLanguage)
                apply()
            }
            restartActivity()
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                search(newText)
                return true
            }
        })
    }

    private fun search(query: String?) {
        // Приводим к нижнему регистру и убираем пробелы
        val inputQuery = query?.toLowerCase(Locale.ROOT)?.trim() ?: ""

        // Получаем текст из всех элементов
        val emailText = emailInput.text.toString()
        val passwordText = passwordInput.text.toString()
        val signText = getString(R.string.sign)
        val registerText = getString(R.string.dont_have_ac)
        val loginButtonText = findViewById<Button>(R.id.login_button).text.toString()

        // Обновляем EditText с выделением
        highlightText(emailInput, emailText, inputQuery)
        highlightText(passwordInput, passwordText, inputQuery)

        // Обновляем TextView с выделением
        highlightText(findViewById(R.id.sign), signText, inputQuery)
        highlightText(findViewById(R.id.onRegisterTV), registerText, inputQuery)

        // Обновляем кнопку с выделением
        highlightText(findViewById(R.id.login_button), loginButtonText, inputQuery)


    }

    private fun highlightText(view: TextView, text: String, query: String) {
        if (query.isEmpty()) {
            view.text = text // Сбрасываем текст если запрос пуст
            return
        }

        val spannable = SpannableString(text)
        val lowerText = text.toLowerCase(Locale.ROOT)

        var indexOfMatch = lowerText.indexOf(query)

        while (indexOfMatch != -1) {
            spannable.setSpan(
                ForegroundColorSpan(Color.RED), // Замените Color.YELLOW на Color.RED или другой яркий цвет
                indexOfMatch,
                indexOfMatch + query.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            // Находим следующее совпадение
            indexOfMatch = lowerText.indexOf(query, indexOfMatch + query.length)
        }

        view.text = spannable
    }

    private fun setLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun restartActivity() {
        val intent = intent
        finish()
        startActivity(intent)
    }

    fun authorized(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
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