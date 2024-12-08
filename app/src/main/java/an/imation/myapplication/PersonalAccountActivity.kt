package an.imation.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Locale

class PersonalAccountActivity : AppCompatActivity() {

    private lateinit var profileImage: ImageView
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_account)

        val changeEmail: TextView = findViewById(R.id.change_mail)

        changeEmail.setOnClickListener {
            showEmailChangeDialog()
        }

        profileImage = findViewById(R.id.profile_image)
        val changePhoto: TextView = findViewById(R.id.change_photo)

        // Запрос разрешения
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }

        changePhoto.setOnClickListener {
            openGallery()
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
    private fun showEmailChangeDialog() {
        val editText = EditText(this)
        editText.hint = "Enter new email"

        val dialog = AlertDialog.Builder(this)
            .setTitle("Change Email")
            .setView(editText)
            .setPositiveButton("OK") { _, _ ->
                val newEmail = editText.text.toString()
                showToast("New email: $newEmail")
                // Здесь добавьте логику для обработки новой почты
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            val imageUri: Uri? = data?.data
            profileImage.setImageURI(imageUri) // Устанавливаем выбранное изображение в ImageView
        }
    }
}