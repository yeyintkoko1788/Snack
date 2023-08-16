package com.yeyint.tiktoktest.compose

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.yeyint.tiktoktest.databinding.ActivityComposeMainBinding

class ComposeMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityComposeMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}