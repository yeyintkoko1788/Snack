package com.yeyint.tiktoktest

import android.animation.Animator
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
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
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.extractor.DefaultExtractorsFactory
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.yeyint.tiktoktest.databinding.ItemVideoContainerBinding
import com.yeyint.tiktoktest.utils.DiffUtils
import com.yeyint.tiktoktest.utils.DoubleClick
import com.yeyint.tiktoktest.utils.DoubleClickListener
import com.yeyint.tiktoktest.utils.VideoCache
import com.yeyint.tiktoktest.viewholder.VideoViewHolder


@UnstableApi
class VideosAdapter(
    private val mContext: Context, private val listener: SnackInterface
) : RecyclerView.Adapter<VideoViewHolder>() {

    private var mData: ArrayList<VideoItem>? = null
    val payLoad = "like_pay"

    init {
        mData = ArrayList()
    }

    fun getItemAt(position: Int): VideoItem {
        return mData!![position]
    }

    fun update(newDataList: List<VideoItem>, clear: Boolean) {
        val diffResult = DiffUtil.calculateDiff(DiffUtils(this.mData!!, newDataList), true)
        if (clear) this.mData!!.clear()
        this.mData!!.addAll(newDataList)

        diffResult.dispatchUpdatesTo(object : ListUpdateCallback {
            override fun onInserted(position: Int, count: Int) {
                notifyItemChanged(position,payLoad)
            }

            override fun onRemoved(position: Int, count: Int) {}
            override fun onMoved(fromPosition: Int, toPosition: Int) {}
            override fun onChanged(position: Int, count: Int, payload: Any?) {
               // this@VideosAdapter.notifyItemChanged(position)
            }
        })
    }

    fun appendNewData(newData: List<VideoItem>, clear: Boolean = true) {
        if (mData!!.isEmpty()) {
            mData!!.addAll(newData)
        } else update(newData, clear)
    }

    interface SnackInterface {
        fun onDoubleTap(position: Int, isLike: Boolean)
        fun onFavouriteTap(position: Int, isLike: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(
            ItemVideoContainerBinding.inflate(LayoutInflater.from(mContext), parent, false),
            context = mContext,
            listener
        )
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        Log.d("TAG","on bind view holder")
    }

//    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
//        Log.d("TAG", "onBind $position")
//        holder.setVideoData(mData!![position])
//        Log.d("TAG", "onView BindViewHolder ${mData!![position]}")
//        //holder.initializePlayer()
//    }

    override fun onBindViewHolder(
        holder: VideoViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        if(payloads.isNotEmpty())
            holder.toggleFavouriteOrPlay(mData!![position])
        else
            holder.setVideoData(mData!![position])
    }



    override fun onViewAttachedToWindow(holder: VideoViewHolder) {
        super.onViewAttachedToWindow(holder)
        Log.d("TAG", "onView AttachedToWindow ${holder.getItemPosition()}")
        //holder.play()
    }

    override fun onViewDetachedFromWindow(holder: VideoViewHolder) {
        super.onViewDetachedFromWindow(holder)
        Log.d("TAG", "onView DetachedFromWindow ${holder.getItemPosition()}")
        //holder.releasePlayer()
        holder.pause()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        Log.d("TAG", "onView AttachedToRecyclerView")
    }

    override fun onViewRecycled(holder: VideoViewHolder) {
        super.onViewRecycled(holder)
        Log.d("TAG", "onView Recycled ${holder.getItemPosition()}")
        holder.releasePlayer()
    }

    override fun getItemCount(): Int {
        return mData!!.size

    }
}
