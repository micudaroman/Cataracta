package com.example.cataracta

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.cataracta.databinding.FragmentMediaPlayerBinding
import com.example.cataracta.ui.record.RecordFragment

class MediaPlayerFragment: Fragment() {
    private lateinit var videoUri: Uri
    private var path: String? = null
    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView
    private lateinit var mediaItem: MediaItem
    private lateinit var binding: FragmentMediaPlayerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {;
        binding = FragmentMediaPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        path = requireArguments().getString("path")
        var args = Bundle()
        args.putString("path", path)
        binding.analysisButton.setOnClickListener{
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.my_fragment_container, UploadVideoFragment::class.java, args)
                .addToBackStack(MediaPlayerFragment::class.java.name)
                .commit()

        }
        setupPlayer()
        binding.btnOpenPopup.setOnClickListener{
            showPopup()
        }
    }

    private fun showPopup() {
        val popupView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_preview_instructions, null)
        val popupWindow = PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        popupWindow.isFocusable = true
        popupWindow.showAsDropDown(binding.tvPopupHelper, 0, 0)

        popupView.findViewById<View>(R.id.btnInfoReviewOk).setOnClickListener {
            popupWindow.dismiss()
        }
    }
    private fun setupPlayer() {
//        path = requireArguments().getString("path")
        Log.d(ContentValues.TAG, path.toString())
        videoUri = Uri.parse(path)
        mediaItem = MediaItem.fromUri(videoUri)
        player = ExoPlayer.Builder(requireContext()).build()
        playerView = requireView().findViewById(R.id.player_view)
        playerView.player = player
        player.setPlaybackSpeed(0.25F)
        player.volume = 0F
        player.setMediaItem(mediaItem)
        player.prepare()
    }


    override fun onResume() {
        super.onResume()
        // Set the MediaPlayer to play the video
        Log.d(TAG, "mediaPlayer.onResume()")
    }

    companion object {
        private const val TAG = "Cataracta/MediaPlayer"
    }

    override fun onDestroy() {
        player.release()
        super.onDestroy()

    }
}