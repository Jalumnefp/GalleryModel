package es.jfp.gallerymodel.fragments.main

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import es.jfp.gallerymodel.R
import es.jfp.gallerymodel.activitys.MainActivity
import es.jfp.gallerymodel.databinding.FragmentMultimediaBinding

class MultimediaFragment: Fragment(), OnClickListener {

    private var _binding: FragmentMultimediaBinding? = null
    private val binding get() = _binding!!

    private var audio: MediaPlayer? = null
    private var paused = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMultimediaBinding.inflate(inflater, container, false)
        audio = MediaPlayer.create(requireContext(), R.raw.classic_music)
        binding.playBtn.setOnClickListener(this)
        binding.stopBtn.setOnClickListener(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.supportActionBar?.title = this::class.java.simpleName
        setUpVideoView()
    }

    override fun onDetach() {
        super.onDetach()
        audio?.stop()
        audio = null
    }

    override fun onClick(view: View) {
        when(view) {
            binding.playBtn -> onClickPlayButton()
            binding.stopBtn -> onClickStopButton()
        }
    }

    private fun onClickStopButton() {
        paused = false
        binding.playBtn.setImageResource(R.drawable.ic_action_play)
        audio?.stop()
        audio?.prepare()
    }

    private fun setUpVideoView() {
        prepareVideo(R.raw.art_video)
        binding.videoView.start()
    }

    private fun onClickPlayButton() {
        paused = if (!paused) {
            binding.playBtn.setImageResource(R.drawable.ic_action_pause)
            audio?.start()
            true
        } else {
            binding.playBtn.setImageResource(R.drawable.ic_action_play)
            audio?.pause()
            false
        }
    }

    private fun prepareVideo(video_id: Int) {
        binding.videoView.setVideoURI(Uri.parse("android.resource://es.jfp.gallerymodel/$video_id"))
        val mediaController = MediaController(requireContext())
        mediaController.setAnchorView(binding.videoView)
        mediaController.setMediaPlayer(binding.videoView)
        binding.videoView.setMediaController(mediaController)
    }


}