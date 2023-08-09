package com.yeyint.tiktoktest

import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import java.io.File


@UnstableApi
object VideoCache {
    private var sDownloadCache: SimpleCache? = null
    fun getInstance(context: Context): SimpleCache? {
        if (sDownloadCache == null) {
            val DOWNLOAD_CONTENT_DIRECTORY = "downloads"
            val downloadContentDirectory =
                File(context.getExternalFilesDir(null), DOWNLOAD_CONTENT_DIRECTORY)
            sDownloadCache = SimpleCache(
                downloadContentDirectory,
                LeastRecentlyUsedCacheEvictor(50000000),
                StandaloneDatabaseProvider(context)
            )
        }
        return sDownloadCache
    }
}