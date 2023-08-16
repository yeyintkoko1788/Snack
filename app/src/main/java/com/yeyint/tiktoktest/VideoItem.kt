package com.yeyint.tiktoktest

data class VideoItem(var videoURL: String? = null,
                var videoTitle: String? = null,
                var videoDesc: String? = null,
                var isLiked : Boolean = false,
                var isPlay : Boolean = false)