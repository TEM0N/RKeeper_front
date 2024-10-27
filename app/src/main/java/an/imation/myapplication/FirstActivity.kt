package an.imation.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


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


    }

    fun authorized(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}