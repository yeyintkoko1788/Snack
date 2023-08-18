package com.yeyint.tiktoktest.viewholder

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.FileDataSource
import androidx.media3.datasource.cache.CacheDataSink
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.extractor.DefaultExtractorsFactory
import androidx.recyclerview.widget.RecyclerView
import com.yeyint.tiktoktest.MediaLifecycleObserver
import com.yeyint.tiktoktest.R
import com.yeyint.tiktoktest.VideoItem
import com.yeyint.tiktoktest.VideosAdapter
import com.yeyint.tiktoktest.compose.ui.SnackItem
import com.yeyint.tiktoktest.databinding.ItemVideoContainerBinding
import com.yeyint.tiktoktest.utils.DoubleClick
import com.yeyint.tiktoktest.utils.DoubleClickListener
import com.yeyint.tiktoktest.utils.VideoCache

@SuppressLint("UnsafeOptInUsageError")
class VideoViewHolder(
    private val binding: ItemVideoContainerBinding,
    private val context: Context,
    private val delegate: VideosAdapter.SnackInterface
) : RecyclerView.ViewHolder(binding.root) {
    private var player: ExoPlayer? = null

    private var playWhenReady = true
    private var currentItem = 0
    private var playbackPosition = 0L
    private var mData: VideoItem? = null


    private var mediaLifecycleObserver: MediaLifecycleObserver? = null

    fun toggleFavouriteOrPlay(videoItem: VideoItem){
        if (mData!!.isLiked != videoItem.isLiked){
            binding.composeContainer.setContent {
                val resource = if (videoItem.isLiked) {
                    R.drawable.ic_heart
                } else {
                    R.drawable.ic_heart_white
                }
                SnackItem(title = videoItem.videoTitle!!, description = videoItem.videoDesc!!, resource, onTapHeart = {
                    delegate.onFavouriteTap(bindingAdapterPosition, mData!!.isLiked)
                }, singleClick = {
                    if (player?.isPlaying == true) {
                        binding.ivPlay.visibility = View.VISIBLE
                        player?.pause()
                    } else {
                        binding.ivPlay.visibility = View.GONE
                        player?.play()
                    }
                }, doubleClick = {
                    binding.heartAni.playAnimation()
                    binding.heartAni.visibility = View.VISIBLE
                    if (!mData!!.isLiked)
                        delegate.onDoubleTap(bindingAdapterPosition, mData!!.isLiked)
                })
            }
        }else if (mData!!.isPlay != videoItem.isPlay){
            if(videoItem.isPlay){
                player?.seekTo(0)
                player?.play()
            }

        }
        this.mData = videoItem
    }


    fun setVideoData(videoItem: VideoItem) {
        mData = videoItem
        binding.composeContainer.setContent {
            val resource = if (videoItem.isLiked) {
                R.drawable.ic_heart
            } else {
                R.drawable.ic_heart_white
            }
            SnackItem(title = videoItem.videoTitle!!, description = videoItem.videoDesc!!, resource, onTapHeart = {
                delegate.onFavouriteTap(bindingAdapterPosition, mData!!.isLiked)
            }, singleClick = {
                if (player?.isPlaying == true) {
                    binding.ivPlay.visibility = View.VISIBLE
                    player?.pause()
                } else {
                    binding.ivPlay.visibility = View.GONE
                    player?.play()
                }
            }, doubleClick = {
                binding.heartAni.playAnimation()
                binding.heartAni.visibility = View.VISIBLE
                if (!mData!!.isLiked)
                    delegate.onDoubleTap(bindingAdapterPosition, mData!!.isLiked)
            })
        }
//            videoItem.videoURL?.let { initializePlayer() }

        binding.heartAni.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {

            }

            override fun onAnimationEnd(p0: Animator) {
                binding.heartAni.visibility = View.GONE
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationRepeat(p0: Animator) {
            }

        })

        initializePlayer()
    }

    fun getItemPosition(): Int {
        return bindingAdapterPosition
    }

    fun initializePlayer() {
        Log.d("TAG", "on view initializePlayer ${getItemPosition()}")

        val trackSelector = DefaultTrackSelector(context).apply {
            setParameters(
                buildUponParameters().setAllowAudioMixedChannelCountAdaptiveness(
                    true
                )
            )
        }

        // Produces DataSource instances through which media data is loaded.
        val downloadCache = VideoCache.getInstance(context)
        val cacheSink = CacheDataSink.Factory().setCache(downloadCache!!)
        val upstreamFactory =
            DefaultDataSource.Factory(context, DefaultHttpDataSource.Factory())
        val downStreamFactory = FileDataSource.Factory()
        val cacheDataSourceFactory = CacheDataSource.Factory().setCache(downloadCache)
            .setCacheWriteDataSinkFactory(cacheSink)
            .setCacheReadDataSourceFactory(downStreamFactory)
            .setUpstreamDataSourceFactory(upstreamFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)

        val mediaItem = MediaItem.Builder().setUri(mData?.videoURL).build()
//             val mediaSource =
//                 HlsMediaSource.Factory(cacheDataSourceFactory).createMediaSource(mediaItem)

        val mediaSource: MediaSource = ProgressiveMediaSource.Factory(
            cacheDataSourceFactory, DefaultExtractorsFactory()
        ).createMediaSource(mediaItem)


        player = ExoPlayer.Builder(context).setTrackSelector(trackSelector).build()
            .also { exoPlayer ->
                binding.videoView.player = exoPlayer
                binding.videoView.useController = false

                // exoPlayer.setVideoTextureView(binding.videoView)
                exoPlayer.seekTo(0)
                exoPlayer.setMediaSource(mediaSource)
                exoPlayer.volume = 1f
                exoPlayer.repeatMode = Player.REPEAT_MODE_ALL
                exoPlayer.playWhenReady = false
                exoPlayer.setAudioAttributes(
                    AudioAttributes.Builder().setUsage(C.USAGE_MEDIA)
                        .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
                        .setSpatializationBehavior(C.SPATIALIZATION_BEHAVIOR_AUTO).build(), false
                )

                exoPlayer.addListener(object : Player.Listener {

                    override fun onPlayWhenReadyChanged(
                        playWhenReady: Boolean, reason: Int
                    ) {
                        super.onPlayWhenReadyChanged(playWhenReady, reason)
                        if (playWhenReady) {
                            binding.ivPlay.visibility = View.GONE
                            Log.d("TAG", "on view play when ready $playWhenReady")

                        } else {
                            binding.ivPlay.visibility = View.VISIBLE
                            Log.d("TAG", "on view play when ready else$playWhenReady")

                        }
                    }

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
//                            val videoWidth = videoSize.width
//                            val videoHeight = videoSize.height
//                            val screenWidth = Resources.getSystem().displayMetrics.widthPixels
//                            val layout = binding.videoView.layoutParams
//                            layout.width = screenWidth
//                            layout.height =
//                                ((videoHeight.toFloat() / videoWidth.toFloat()) * screenWidth.toFloat()).toInt()
//                            binding.videoView.layoutParams = layout
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

        if (mData?.isPlay == true) {
            play()
        }
        mediaLifecycleObserver = MediaLifecycleObserver(player, context as LifecycleOwner)
    }

    private fun getImageResourceId(isFavourite: Boolean): Int {
        return if (isFavourite) R.drawable.ic_heart
        else R.drawable.ic_heart_white
    }

    fun play() {
        binding.ivPlay.visibility = View.GONE
        player?.seekTo(0)
        player?.play()
    }

    fun pause() {
        player?.pause()
    }

    fun releasePlayer() {
        Log.d("TAG", "releasePlayer ${getItemPosition()}")
        mData = null
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