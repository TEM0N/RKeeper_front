package an.imation.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Locale

class FilterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        val timeLabel = findViewById<TextView>(R.id.time_label)
        val timeFilterLayout = findViewById<LinearLayout>(R.id.time_filter_layout)


        timeLabel.setOnClickListener {
            if (timeFilterLayout.visibility == View.GONE) {
                timeFilterLayout.visibility = View.VISIBLE
                //timeFilterArrow.setImageResource(R.drawable.arrow_up)
            } else {
                timeFilterLayout.visibility = View.GONE
                //timeFilterArrow.setImageResource(R.drawable.arrow_down)
            }
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
}