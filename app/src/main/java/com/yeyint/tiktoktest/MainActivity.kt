package com.yeyint.tiktoktest

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback


@UnstableApi
class MainActivity : AppCompatActivity(), VideosAdapter.SnackInterface {

    val videoItems: MutableList<VideoItem> = ArrayList()
    private var adapter: VideosAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val videosViewPager = findViewById<ViewPager2>(R.id.viewPagerVideos)

        adapter = VideosAdapter(this, this)

        val item = VideoItem()
        item.videoURL =
            "https://saya-education.s3.amazonaws.com/staging/reel_videos/4/1.5-M-1.mp4"
        item.videoTitle = "Women In Tech"
        item.videoDesc = "International Women's Day 2019"
        videoItems.add(item)

        val item2 = VideoItem()
        item2.videoURL = "https://saya-education.s3.amazonaws.com/staging/reel_videos/3/Language-ai-render~1.mp4"
        item2.videoTitle = "Sasha Solomon"
        item2.videoDesc = "How Sasha Solomon Became a Software Developer at Twitter"
        videoItems.add(item2)

        val item3 = VideoItem()
        item3.videoURL =
            "https://saya-education.s3.amazonaws.com/staging/reel_videos/2/No-courage-render_1~1.mp4"
        item3.videoTitle = "Happy Hour Wednesday"
        item3.videoDesc = " Depth-First Search Algorithm"
        videoItems.add(item3)

        val item4 = VideoItem()
        item4.videoURL =
            "https://saya-education.s3.amazonaws.com/staging/reel_videos/1/No-time-render~1.mp4"
        item4.videoTitle = "Happy Hour Wednesday"
        item4.videoDesc = " Depth-First Search Algorithm"
        videoItems.add(item4)

        val item5 = VideoItem()
        item5.videoURL =
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4"
        item5.videoTitle = "Happy Hour Wednesday"
        item5.videoDesc = " Depth-First Search Algorithm"
        videoItems.add(item5)

        val item6 = VideoItem()
        item6.videoURL =
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4"
        item6.videoTitle = "Happy Hour Wednesday"
        item6.videoDesc = " Depth-First Search Algorithm"
        videoItems.add(item6)

        val item7 = VideoItem()
        item7.videoURL =
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4"
        item7.videoTitle = "Happy Hour Wednesday"
        item7.videoDesc = " Depth-First Search Algorithm"
        videoItems.add(item7)

        val item8 = VideoItem()
        item8.videoURL =
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"
        item8.videoTitle = "Happy Hour Wednesday"
        item8.videoDesc = " Depth-First Search Algorithm"
        videoItems.add(item8)

        val item9 = VideoItem()
        item9.videoURL =
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4"
        item9.videoTitle = "Happy Hour Wednesday"
        item9.videoDesc = " Depth-First Search Algorithm"
        videoItems.add(item9)
        adapter?.appendNewData(videoItems)

        videosViewPager.adapter = adapter

/*        val onPageChangeCallback = object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == videosViewPager.adapter?.itemCount?.minus(3)) {
                // Reached the end of the list
                // Your code here to handle the end of list reach
                Log.d("TAG","On list end reach")
                }
                Log.d("TAG","On page selected")

            }
        }
        videosViewPager.registerOnPageChangeCallback(onPageChangeCallback)*/

        videosViewPager.registerOnPageChangeCallback(object : OnPageChangeCallback(){
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                Log.d("TAG","On page scroll state $state ")
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                Log.d("TAG","On page scroll $position , $positionOffset , $positionOffsetPixels ")
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.d("TAG","On page selected $position")
            }

        })
    }

    override fun onDoubleTap(position: Int) {
        videoItems[position].isLiked = true
//        Handler(Looper.getMainLooper()).postDelayed({
//            adapter?.notifyItemChanged(position)
//        }, 1000)
    }

    override fun onFavouriteTap(position: Int) {
        videoItems[position].isLiked = !videoItems[position].isLiked
    }
}