package com.yeyint.tiktoktest.compose.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlantDetailDescription() {
    Surface {
        Text(

            fontSize = 16.sp,
            style = MaterialTheme.typography.h5,
            text = "Hello Compose"
        )
    }
}

@Preview
@Composable
fun ShowPlantDetail(){
    MaterialTheme{
        PlantDetailDescription()
    }
}