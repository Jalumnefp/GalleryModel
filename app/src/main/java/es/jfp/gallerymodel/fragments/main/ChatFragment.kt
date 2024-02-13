package es.jfp.gallerymodel.fragments.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import es.jfp.gallerymodel.R
import es.jfp.gallerymodel.activitys.MainActivity
import es.jfp.gallerymodel.adapters.ChatAdapter
import es.jfp.gallerymodel.classes.Message
import es.jfp.gallerymodel.databinding.FragmentChatBinding
import es.jfp.gallerymodel.services.RetrofitListenerService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.util.*

class ChatFragment: Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val firestoneDatabase = FirebaseFirestore.getInstance()

    private var chatAdapter: ChatAdapter? = null

    private val messages = mutableListOf<Message>()
    private val messagesCollection = firestoneDatabase.collection("/messages")

    private val chatUpdateListener: RetrofitListenerService by lazy { RetrofitListenerService() }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)

        binding.sendButton.setOnClickListener { sendMessage() }

        setUpRecyclerView()

        startRetrofitListener()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.supportActionBar?.title = this::class.java.simpleName
    }

    private fun setUpRecyclerView() {
        chatAdapter = ChatAdapter(messages)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        layoutManager.reverseLayout = false
        layoutManager.stackFromEnd = true
        binding.recyclerChatContainer.adapter = chatAdapter
        binding.recyclerChatContainer.setHasFixedSize(true)
        binding.recyclerChatContainer.layoutManager = layoutManager
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createFirebaseMessage() {
        messagesCollection.add(mapOf(
            "owner" to getChatUsernamePreference(),
            "content" to binding.sendMessageEdittext.text.toString(),
            "date" to LocalDateTime.now(),
            "color" to PreferenceManager.getDefaultSharedPreferences(requireContext()).getString("CHAT_COLOR", null)
        ))
            .addOnFailureListener { exception ->
                Log.w("FAIL_CREATE_MESSAGE", exception)
            }
        createNotification("New message from ${getChatUsernamePreference()}", binding.sendMessageEdittext.text.toString())
    }

    private fun scrollPositionToEnd() {
        binding.recyclerChatContainer.post {
            val position = chatAdapter!!.itemCount - 1
            if (position >= 0) {
                binding.recyclerChatContainer.scrollToPosition(position)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startRetrofitListener() {
        lifecycleScope.launch(Dispatchers.IO) {
            chatUpdateListener.startOnUpdateListener(messagesCollection).collect { newmessages ->
                messages.clear()
                messages.addAll(newmessages)
                withContext(Dispatchers.Main) {
                    chatAdapter?.notifyDataSetChanged()
                    scrollPositionToEnd()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendMessage() {
        if (binding.sendMessageEdittext.text != null) {
            createFirebaseMessage()
            binding.sendMessageEdittext.text = null
        }
    }
    private fun getChatUsernamePreference(): String = PreferenceManager.getDefaultSharedPreferences(requireContext()).getString("CHAT_USERNAME", null).toString()


    private fun createNotification(title: String, body: String){

        val CHANNEL_ID = "CHAT NOTIFICATION ID"
        val CHANNEL_NAME = "Chat notification channel"

        val intent = Intent(requireContext(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            requireContext(), 0, intent, FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val manager = requireActivity().getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }

        manager.notify(1, notificationBuilder.build())
        if (PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean("GENERAL_VIBRATION", false)) {
            vibrateDevice()
        }
    }


    private fun vibrateDevice() {
        val vibrator = if (Build.VERSION.SDK_INT>=31) {
            val vibratorManager = requireContext().getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        val mVibrationPattern = longArrayOf(0, 400, 200, 400)
        if (Build.VERSION.SDK_INT>=26) {
            vibrator.vibrate(VibrationEffect.createWaveform(mVibrationPattern, -1))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(mVibrationPattern, -1)
        }
    }

}