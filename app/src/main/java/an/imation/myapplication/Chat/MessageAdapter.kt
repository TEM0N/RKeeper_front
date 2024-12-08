package an.imation.myapplication.Chat

import an.imation.myapplication.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessageAdapter(private val messages: List<Message>) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageContent: TextView = view.findViewById(R.id.messageContent)
        val messageTime: TextView = view.findViewById(R.id.messageTime)
        val userAvatar: ImageView = view.findViewById(R.id.userAvatar) // Добавляем ImageView для аватара
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.messageContent.text = message.content

        // Форматирование времени
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        holder.messageTime.text = dateFormat.format(Date(message.timestamp))

        // Установка аватара пользователя
        holder.userAvatar.setImageResource(R.drawable.baseline_person_24) // Замените на ваш ресурс изображения
    }

    override fun getItemCount() = messages.size
}