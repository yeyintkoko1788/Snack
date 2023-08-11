package com.yeyint.tiktoktest.utils

import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import java.io.File

@UnstableApi object VideoCache {
    private var sDownloadCache: SimpleCache? = null
    private const val cacheSize : Long = 200 * 1024 * 1024
    private const val DOWNLOAD_CONTENT_DIRECTORY = "downloads"
    fun getInstance(context: Context): SimpleCache? {
        if (sDownloadCache == null) {

            val downloadContentDirectory =
                File(context.getExternalFilesDir(null),DOWNLOAD_CONTENT_DIRECTORY)
            sDownloadCache = SimpleCache(downloadContentDirectory, LeastRecentlyUsedCacheEvictor(
                cacheSize), StandaloneDatabaseProvider(context))
        }
        return sDownloadCache
    }
}