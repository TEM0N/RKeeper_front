package an.imation.myapplication

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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
    }
}