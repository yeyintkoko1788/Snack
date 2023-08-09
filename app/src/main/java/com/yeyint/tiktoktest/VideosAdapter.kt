package com.yeyint.tiktoktest

import android.animation.Animator
import android.content.Context
import android.content.res.Resources
import android.media.MediaCodec
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView.SurfaceTextureListener
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.FileDataSource
import androidx.media3.datasource.cache.CacheDataSink
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.recyclerview.widget.RecyclerView
import com.yeyint.tiktoktest.databinding.ItemVideoContainerBinding
import com.yeyint.tiktoktest.utils.DoubleClick
import com.yeyint.tiktoktest.utils.DoubleClickListener
import com.yeyint.tiktoktest.utils.VideoCache


@UnstableApi
class VideosAdapter(
    private val mContext: Context,
    private val mVideoItems: List<VideoItem>,
    private val listener: SnackInterface
) : RecyclerView.Adapter<VideosAdapter.VideoViewHolder>() {

    interface SnackInterface {
        fun onDoubleTap(position: Int)
        fun onFavouriteTap(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(
            ItemVideoContainerBinding.inflate(LayoutInflater.from(mContext), parent, false),
            context = mContext,
            listener
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

    @UnstableApi
    class VideoViewHolder(
        private val binding: ItemVideoContainerBinding,
        private val context: Context,
        private val delegate: SnackInterface
    ) : RecyclerView.ViewHolder(binding.root) {
        private var player: ExoPlayer? = null

        private var playWhenReady = true
        private var currentItem = 0
        private var playbackPosition = 0L
        private var mData: VideoItem? = null

        private var mediaLifecycleObserver: MediaLifecycleObserver? = null


        fun setVideoData(videoItem: VideoItem) {
            mData = videoItem
            binding.txtTitle.text = videoItem.videoTitle
            binding.txtDesc.text = videoItem.videoDesc
//            videoItem.videoURL?.let { initializePlayer() }
            if (videoItem.isLiked) {
                binding.ivHeart.setImageDrawable(
                    ContextCompat.getDrawable(
                        context, R.drawable.ic_heart
                    )
                )
            } else {
                binding.ivHeart.setImageDrawable(
                    ContextCompat.getDrawable(
                        context, R.drawable.ic_heart_white
                    )
                )
            }
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

            binding.root.setOnClickListener(DoubleClick(object : DoubleClickListener {

                override fun onDoubleClickEvent(view: View?) {
                    binding.heartAni.playAnimation()
                    binding.heartAni.visibility = View.VISIBLE
                    delegate.onDoubleTap(bindingAdapterPosition)
                    binding.ivHeart.setImageDrawable(
                        ContextCompat.getDrawable(context, R.drawable.ic_heart)
                    )
                }

                override fun onSingleClickEvent(view: View?) {
                    if (player?.isPlaying == true) {
                        binding.ivPlay.visibility = View.VISIBLE
                        player?.pause()
                    } else {
                        binding.ivPlay.visibility = View.GONE
                        player?.play()
                    }
                }
            }))

            binding.ivHeart.setOnClickListener {
                delegate.onFavouriteTap(bindingAdapterPosition)
                binding.ivHeart.setImageResource(getImageResourceId(mData!!.isLiked))
            }

            initializePlayer()
        }

        fun getItemPosition(): Int {
            return bindingAdapterPosition
        }

        fun initializePlayer() {
            Log.d("TAG", "initializePlayer ${getItemPosition()}")
            val trackSelector = DefaultTrackSelector(context).apply {
                setParameters(buildUponParameters().setMaxVideoSizeSd())
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

            val mediaItem =
                MediaItem.Builder().setUri(mData?.videoURL).setMimeType(MimeTypes.VIDEO_MP4).build()
//             val mediaSource =
//                 HlsMediaSource.Factory(cacheDataSourceFactory).createMediaSource(mediaItem)
            val mediaSource: MediaSource =
                DefaultMediaSourceFactory(context).setDataSourceFactory(cacheDataSourceFactory)
                    .createMediaSource(mediaItem)


            player = ExoPlayer.Builder(binding.root.context).setTrackSelector(trackSelector).build()
                .also { exoPlayer ->
                    binding.videoView.player = exoPlayer
                    binding.videoView.useController = false

                    // exoPlayer.setVideoTextureView(binding.videoView)

                    exoPlayer.seekTo(0)
                    exoPlayer.setMediaSource(mediaSource)
                    exoPlayer.repeatMode = Player.REPEAT_MODE_ALL
                    exoPlayer.videoScalingMode =
                        MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
                    exoPlayer.playWhenReady = false
                    exoPlayer.seekTo(currentItem, playbackPosition)

                    exoPlayer.addListener(object : Player.Listener {
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
                            layout.height =
                                ((videoHeight.toFloat() / videoWidth.toFloat()) * screenWidth.toFloat()).toInt()
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
            //  mediaLifecycleObserver = MediaLifecycleObserver(player, context as LifecycleOwner)

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
