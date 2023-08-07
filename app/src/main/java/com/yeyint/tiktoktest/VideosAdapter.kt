package com.yeyint.tiktoktest

import android.content.Context
import android.content.res.Resources
import android.graphics.SurfaceTexture
import android.media.MediaCodec
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView.SurfaceTextureListener
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.recyclerview.widget.RecyclerView
import com.yeyint.tiktoktest.databinding.ItemVideoContainerBinding


@UnstableApi class VideosAdapter(private val mContext : Context, private val mVideoItems: List<VideoItem>) :
    RecyclerView.Adapter<VideosAdapter.VideoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(
            ItemVideoContainerBinding.inflate(LayoutInflater.from(mContext), parent, false)
        )
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        Log.d("TAG", "onBind $position")
        holder.setVideoData(mVideoItems[position])
        //holder.initializePlayer()
    }

    override fun onViewAttachedToWindow(holder: VideoViewHolder) {
        super.onViewAttachedToWindow(holder)
        Log.d("TAG", "onViewAttachedToWindow ${holder.getItemPosition()}")
        holder.play()
    }

    override fun onViewDetachedFromWindow(holder: VideoViewHolder) {
        super.onViewDetachedFromWindow(holder)
        Log.d("TAG", "onViewDetachedFromWindow ${holder.getItemPosition()}")
        //holder.releasePlayer()
        holder.pause()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        Log.d("TAG", "onAttachedToRecyclerView")
    }

    override fun onViewRecycled(holder: VideoViewHolder) {
        super.onViewRecycled(holder)
        Log.d("TAG", "onViewRecycled ${holder.getItemPosition()}")
        holder.releasePlayer()
    }

    override fun getItemCount(): Int {
        return mVideoItems.size
    }

    @UnstableApi class VideoViewHolder(private val binding : ItemVideoContainerBinding) : RecyclerView.ViewHolder(binding.root) {
        private var player: ExoPlayer? = null

        private var playWhenReady = true
        private var currentItem = 0
        private var playbackPosition = 0L
        private var mData : VideoItem? = null

        private var mediaLifecycleObserver: MediaLifecycleObserver? = null


        fun setVideoData(videoItem: VideoItem) {
            mData = videoItem
            binding.txtTitle.text = videoItem.videoTitle
            binding.txtDesc.text = videoItem.videoDesc
//            videoItem.videoURL?.let { initializePlayer() }
            initializePlayer()
//            binding.videoView.setVideoPath(videoItem.videoURL)
//            binding.videoView.setOnPreparedListener { mp ->
//                binding.progressBar.visibility = View.GONE
//                mp.start()
//                val videoRatio = mp.videoWidth / mp.videoHeight.toFloat()
//                val screenRatio = binding.videoView.width / binding.videoView.height.toFloat()
//                val scale = videoRatio / screenRatio
//                if (scale >= 1f) {
//                    binding.videoView.scaleX = scale
//                } else {
//                    binding.videoView.scaleY = 1f / scale
//                }
//            }
//            binding.videoView.setOnCompletionListener { mp -> mp.start() }

        }

        fun getItemPosition() : Int{
            return bindingAdapterPosition
        }
         fun initializePlayer() {
             if (player == null){
                 Log.d("TAG","initializePlayer ${getItemPosition()}")
                 val trackSelector = DefaultTrackSelector(binding.root.context).apply {
                     setParameters(buildUponParameters().setMaxVideoSizeSd())
                 }

                 player = ExoPlayer.Builder(binding.root.context)
                     .setTrackSelector(trackSelector)
                     .build()
                     .also { exoPlayer ->
                         //binding.videoView.player = exoPlayer
                         exoPlayer.setVideoTextureView(binding.videoView)
                         val mediaItem = MediaItem.Builder()
                             .setUri(mData?.videoURL)
                             .setMimeType(MimeTypes.VIDEO_MP4)
                             .build()
                         exoPlayer.seekTo(0)
                         exoPlayer.setMediaItem(mediaItem)
                         exoPlayer.repeatMode = Player.REPEAT_MODE_ALL
                         exoPlayer.videoScalingMode = MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
                         exoPlayer.playWhenReady = false
                         exoPlayer.seekTo(currentItem, playbackPosition)
                         exoPlayer.addListener(object : Player.Listener{
                             override fun onVideoSizeChanged(videoSize: VideoSize) {
                                 super.onVideoSizeChanged(videoSize)
//                                 val videoRatio = videoSize.width / videoSize.height.toFloat()
//                                val screenRatio = binding.videoView.width / binding.videoView.height.toFloat()
//                                val scale = videoRatio / screenRatio
//                                if (scale >= 1f) {
//                                    binding.videoView.scaleX = scale
//                                } else {
//                                    binding.videoView.scaleY = 1f / scale
//                                }
//                }
                                 val videoWidth = videoSize.width
                                 val videoHeight = videoSize.height
                                 val screenWidth = Resources.getSystem().displayMetrics.widthPixels
                                 val layout = binding.videoView.layoutParams
                                 layout.width = screenWidth
                                 layout.height = ((videoHeight.toFloat() / videoWidth.toFloat()) * screenWidth.toFloat()).toInt()
                                 binding.videoView.layoutParams = layout
                             }
                             override fun onPlaybackStateChanged(playbackState: Int) {
                                 val stateString: String = when (playbackState) {
                                     ExoPlayer.STATE_IDLE -> "ExoPlayer.STATE_IDLE      -"
                                     ExoPlayer.STATE_BUFFERING -> {
                                         binding.progressBar.visibility = View.VISIBLE
                                         "ExoPlayer.STATE_BUFFERING -"
                                     }
                                     ExoPlayer.STATE_READY -> {
                                         binding.progressBar.visibility = View.GONE
                                         "ExoPlayer.STATE_READY     -"
                                     }
                                     ExoPlayer.STATE_ENDED -> "ExoPlayer.STATE_ENDED     -"
                                     else -> "UNKNOWN_STATE             -"
                                 }
                                 Log.d("TAG", "changed state to $stateString")
                             }

                             override fun onPlayerError(error: PlaybackException) {
                                 super.onPlayerError(error)
                                 if (error.errorCode == PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW) {
                                     player?.seekToDefaultPosition()
                                     player?.prepare()
                                 }
                                 Log.e("TAG", error.errorCodeName)
                             }
                         })
                         exoPlayer.prepare()
                         //exoPlayer.play()
                     }
                 mediaLifecycleObserver = MediaLifecycleObserver(player,binding.root.context as LifecycleOwner)
             }
             binding.root.setOnClickListener{
                 if( player?.isPlaying == true){
                     binding.ivPlay.visibility = View.VISIBLE
                     player?.pause()
                 }else{
                     binding.ivPlay.visibility = View.GONE
                     player?.play()
                 }
             }
        }

        fun play(){
            binding.ivPlay.visibility = View.GONE
            player?.seekTo(0)
           player?.play()
        }

        fun pause(){
           player?.pause()
        }

        fun releasePlayer() {
            Log.d("TAG","releasePlayer ${getItemPosition()}")
            player?.let { exoPlayer ->
                playbackPosition = exoPlayer.currentPosition
                currentItem = exoPlayer.currentMediaItemIndex
                playWhenReady = exoPlayer.playWhenReady
                exoPlayer.release()
                exoPlayer.setVideoSurfaceView(null)
            }
            player?.release()
            player = null
        }
    }
}
