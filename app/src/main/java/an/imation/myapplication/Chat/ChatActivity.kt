package an.imation.myapplication.Chat

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import an.imation.myapplication.R
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChatActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button
    private lateinit var messageAdapter: MessageAdapter
    private val messages = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        recyclerView = findViewById(R.id.recyclerView)
        messageInput = findViewById(R.id.messageInput)
        sendButton = findViewById(R.id.sendButton)

        messageAdapter = MessageAdapter(messages)
        recyclerView.adapter = messageAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        sendButton.setOnClickListener {
            sendMessage()
        }
    }

    private fun sendMessage() {
        val content = messageInput.text.toString()
        if (content.isNotEmpty()) {
            val message = Message("user1", "user2", content, System.currentTimeMillis())
            messages.add(message)
            messageAdapter.notifyItemInserted(messages.size - 1)
            messageInput.text.clear()
            recyclerView.scrollToPosition(messages.size - 1)
        }
    }
}