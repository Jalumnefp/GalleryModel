package es.jfp.gallerymodel.adapters

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import es.jfp.gallerymodel.R
import es.jfp.gallerymodel.classes.Message
import es.jfp.gallerymodel.databinding.RecyclerChatItemBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatAdapter(
    private val messages: MutableList<Message>
): RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private lateinit var parentContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        parentContext = parent.context
        val binding = RecyclerChatItemBinding.inflate(LayoutInflater.from(parentContext), parent, false)

        return ChatViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val item = messages[position]
        holder.getMessageItem().setCardBackgroundColor(Color.parseColor(item.color))
        holder.bindItem(item)
    }

    class ChatViewHolder(val binding: RecyclerChatItemBinding): RecyclerView.ViewHolder(binding.root) {

        private val ownerTextView = binding.messageOwner
        private val contentTextView = binding.messageContent
        private val dateTextView = binding.messageDate

        @RequiresApi(Build.VERSION_CODES.O)
        fun bindItem(msg: Message) {

            ownerTextView.text = msg.owner
            contentTextView.text = msg.content
            dateTextView.text = getHour(msg.date)

        }

        fun getMessageItem(): CardView = binding.cardBocadillo

        @RequiresApi(Build.VERSION_CODES.O)
        private fun getHour(date: LocalDateTime): String {
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            return date.format(formatter)
        }

    }
}