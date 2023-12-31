package com.yeyint.tiktoktest

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SnackViewModel : ViewModel() {

    var snackList: MutableLiveData<ArrayList<VideoItem>> = MutableLiveData()
    private var videoItems: ArrayList<VideoItem> = ArrayList()

    init {
        val item = VideoItem()
        item.id = 1
        item.videoURL =
            "https://saya-education.s3.amazonaws.com/staging/reel_videos/4/1.5-M-1.mp4"
        item.videoTitle = "Women In Tech"
        item.videoDesc = "International Women's Day 2019"
        videoItems.add(item)

        val item2 = VideoItem()
        item.id = 2
        item2.videoURL =
            "https://saya-education.s3.amazonaws.com/staging/reel_videos/3/Language-ai-render~1.mp4"
        item2.videoTitle = "Sasha Solomon"
        item2.videoDesc = "How Sasha Solomon Became a Software Developer at Twitter"
        videoItems.add(item2)

        val item3 = VideoItem()
        item.id = 3
        item3.videoURL =
            "https://saya-education.s3.amazonaws.com/staging/reel_videos/2/No-courage-render_1~1.mp4"
        item3.videoTitle = "Happy Hour Wednesday"
        item3.videoDesc = " Depth-First Search Algorithm"
        videoItems.add(item3)

        val item4 = VideoItem()
        item.id = 4
        item4.videoURL =
            "https://saya-education.s3.amazonaws.com/staging/reel_videos/1/No-time-render~1.mp4"
        item4.videoTitle = "Happy Hour Wednesday"
        item4.videoDesc = " Depth-First Search Algorithm"
        videoItems.add(item4)

        val item5 = VideoItem()
        item.id = 5
        item5.videoURL =
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4"
        item5.videoTitle = "Happy Hour Wednesday"
        item5.videoDesc = " Depth-First Search Algorithm"
        videoItems.add(item5)

        val item6 = VideoItem()
        item.id = 6
        item6.videoURL =
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4"
        item6.videoTitle = "Happy Hour Wednesday"
        item6.videoDesc = " Depth-First Search Algorithm"
        videoItems.add(item6)

        val item7 = VideoItem()
        item.id = 7
        item7.videoURL =
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4"
        item7.videoTitle = "Happy Hour Wednesday"
        item7.videoDesc = " Depth-First Search Algorithm"
        videoItems.add(item7)

        val item8 = VideoItem()
        item.id = 8
        item8.videoURL =
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"
        item8.videoTitle = "Happy Hour Wednesday"
        item8.videoDesc = " Depth-First Search Algorithm"
        videoItems.add(item8)

    }

    fun getSnackList() {
        snackList.value = videoItems
    }

//    fun toggleLike(position : Int){
//        val item9 = VideoItem()
//        item9.videoURL =
//            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4"
//        item9.videoTitle = "Happy Hour Wednesday"
//        item9.videoDesc = " Depth-First Search Algorithm"
//        videoItems.add(item9)
//        videoItems[position].videoTitle = "Changed"
//        videoItems[position].isLiked = !videoItems[position].isLiked
//        snackList.value = videoItems
//    }

    fun toggleLike(position: Int, isLike: Boolean) {
        val updatedVideoItems = ArrayList(videoItems)
        val currentItem = updatedVideoItems[position]
        val updatedItem = currentItem.copy(isLiked = !isLike)
        updatedVideoItems[position] = updatedItem
        snackList.value = updatedVideoItems
        videoItems[position] = updatedItem
        Log.d("TAG", "on toggle viewmodel $isLike")
    }

    fun togglePlay(position : Int, isPlay :Boolean){
        val updatedVideoItems = ArrayList(videoItems)
        val currentItem = updatedVideoItems[position]
        val updatedItem = currentItem.copy(isPlay = !isPlay)
        updatedVideoItems[position] = updatedItem
        snackList.value = updatedVideoItems
        videoItems[position] = updatedItem
        Log.d("TAG", "on toggle viewmodel $isPlay")
    }
}