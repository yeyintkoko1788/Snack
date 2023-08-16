package com.yeyint.tiktoktest

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2


@UnstableApi
class MainActivity : AppCompatActivity(), VideosAdapter.SnackInterface {

    private var adapter: VideosAdapter? = null
    private val viewModel : SnackViewModel by viewModels()
    private var previousPosition : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val videosViewPager = findViewById<ViewPager2>(R.id.viewPagerVideos)

        adapter = VideosAdapter(this, this)
        videosViewPager.adapter = adapter
        viewModel.getSnackList()
        viewModel.snackList.observe(this) {
            if (previousPosition != -1){
                val data = adapter?.getItemAt(position = previousPosition)
                if (data != null) {
                    Log.d("TAG","on toggle main ${data.isLiked } : ${data.videoTitle.toString()} " )
                }
            }
            adapter?.appendNewData(it)
          //  adapter?.notifyItemChanged(previousPosition)
        }

        val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == videosViewPager.adapter?.itemCount?.minus(3)) {
                // Reached the end of the list
                // Your code here to handle the end of list reach
                Log.d("TAG","On list end reach")
                }
//                if (previousPosition != -1){
//                    viewModel.togglePlay(previousPosition)
//                }
                if (previousPosition != -1 && previousPosition != position){
                    adapter?.getItemAt(previousPosition)?.isPlay?.let { viewModel.togglePlay(previousPosition, it) }
                }
                adapter?.getItemAt(position)?.isPlay?.let { viewModel.togglePlay(position, it) }
                previousPosition = position

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }
        }
        videosViewPager.registerOnPageChangeCallback(onPageChangeCallback)
    }

    override fun onDoubleTap(position: Int, isLike: Boolean) {
        previousPosition = position
        viewModel.toggleLike(position, isLike)
//        Handler(Looper.getMainLooper()).postDelayed({
//            adapter?.notifyItemChanged(position)
//        }, 1000)
    }

    override fun onFavouriteTap(position: Int,isLike: Boolean) {
        viewModel.toggleLike(position,isLike)
    }

//    fun ViewPager2.reduceDragSensitivity() {
//        val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
//        recyclerViewField.isAccessible = true
//        val recyclerView = recyclerViewField.get(this) as RecyclerView
//
//        val touchSlopField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
//        touchSlopField.isAccessible = true
//        val touchSlop = touchSlopField.get(recyclerView) as Int
//        touchSlopField.set(recyclerView, touchSlop*8)       // "8" was obtained experimentally
//    }
}