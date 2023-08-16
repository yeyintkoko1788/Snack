package com.yeyint.tiktoktest.compose.ui

import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PlantDetailDescription() {
    Surface {
        Text("Hello Compose")
    }
}

@Preview
@Composable
fun ShowPlantDetail(){
    PlantDetailDescription()
}