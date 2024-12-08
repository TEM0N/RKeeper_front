package an.imation.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Locale


class FirstActivity : AppCompatActivity() {

    private lateinit var recipeAdapter: RecipeAdapter
    private val recipeList = ArrayList<Recipe>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_first)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Создание списка рецептов
        recipeList.add(Recipe("Title 1", "Ingredients 1", R.drawable.logo1))
        recipeList.add(Recipe("Title 2", "Ingredients 2", R.drawable.logo2))
        recipeList.add(Recipe("Title 3", "Ingredients 3", R.drawable.logo3))

        // Настройка адаптера
        recipeAdapter = RecipeAdapter(recipeList)
        recyclerView.adapter = recipeAdapter

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