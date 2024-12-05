package an.imation.myapplication

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class CodeInputDialog : DialogFragment() {

    private lateinit var timerTextView: TextView
    private lateinit var codeEditText: EditText
    private var timer: CountDownTimer? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater

        val view = inflater.inflate(R.layout.dialog_code_input, null)

        timerTextView = view.findViewById(R.id.timerTextView)
        codeEditText = view.findViewById(R.id.codeEditText)

        builder.setView(view)
            .setTitle("Введите код")
            .setPositiveButton("Подтвердить") { _, _ ->
                val code = codeEditText.text.toString()
                // Здесь добавлю  логику проверки кода
            }
            .setNegativeButton("Отменить") { dialog, _ -> dialog.dismiss() }

        startTimer()

        return builder.create()
    }

    private fun startTimer() {
        timer = object : CountDownTimer(300000, 1000) { // 5 минут
            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished / 1000).toInt()
                val minutes = seconds / 60
                val remainingSeconds = seconds % 60
                timerTextView.text = String.format("Осталось: %02d:%02d", minutes, remainingSeconds)
            }

            override fun onFinish() {
                timerTextView.text = "Время вышло!"
                codeEditText.isEnabled = false // Отключаем поле ввода
            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel() // Остановка таймера
    }
}
