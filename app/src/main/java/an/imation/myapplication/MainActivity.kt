package an.imation.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun register(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
    fun Log_in(view: View) {
        val intent = Intent(this, FilterActivity::class.java)
        startActivity(intent)
    }
}