package com.yeyint.tiktoktest.compose.ui

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.yeyint.tiktoktest.R


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SnackItem(
    title: String,
    description: String,
    resource: Int,
    onTapHeart: () -> Unit,
    singleClick: () -> Unit,
    doubleClick: () -> Unit
) {
    var isPlaying by remember { mutableStateOf(false) }
    var appLaunchedFirstTime by remember {
        mutableStateOf(true)
    }
    var isVisible by remember { mutableStateOf(false) }
    var previousProgress = remember {
        1
    }
    val handler = Handler(Looper.getMainLooper())
    val interactionSource = remember { MutableInteractionSource() }
    var counterClicks by remember {
        mutableIntStateOf(0)
    }
    var isBusy by remember {
        mutableStateOf(false)
    }

    val composition by rememberLottieComposition(
        LottieCompositionSpec.Url("https://lottie.host/34243fdc-007c-441c-b6cc-0b1385c1a793/ZvYH0UBJXP.json")
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isPlaying
    )

    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .combinedClickable(
                onDoubleClick = {
                    appLaunchedFirstTime = false
                    isPlaying = true
                    isVisible = true
                    doubleClick.invoke()
                },
                onClick = {

                    singleClick.invoke()
                }
            )
//            .clickable(
//                interactionSource = interactionSource,
//                indication = null,
//                onClick = {
//                    appLaunchedFirstTime = false
//                    if (!isBusy) {
//                        isBusy = true
//                        counterClicks++
//                        handler.postDelayed({
//                            if (counterClicks >= 2) {
//                                //isVisible = true
//                                //appLaunchedFirstTime = false
//                                isPlaying = true
//                                doubleClick.invoke()
//                            }
//                            if (counterClicks == 1) {
//                                singleClick.invoke()
//                            }
//                            counterClicks = 0
//                        }, 200L)
//                        isBusy = false
//                    }
//                }
//
//            )

            .fillMaxSize(1f)
            .padding(5.dp, 0.dp, 5.dp, 30.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            ComposeCenterImageView(resource, onTapHeart)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            LaunchedEffect(key1 = progress) {
                Log.d("ANI", "progress $progress")
                if (appLaunchedFirstTime) {
                    // start of animation

                    if (progress == 0f) {
                        isPlaying = true
                        isVisible = true
                    }

                    // end of animation
                    if (progress == 1f) {
                        isPlaying = false
                        isVisible = false
                    }
                    Log.d("ANI", "app launch first if ${appLaunchedFirstTime}")
                } else {
                    if (progress == 0f) {
                        isPlaying = true
                        isVisible = true
                    }

                    // end of animation
                    if (progress == 1f) {
                        isPlaying = false
                        isVisible = false
                    }
                    Log.d("ANI", "app launch first else ${appLaunchedFirstTime}")

                }

                /*                if (progress == 0f) {
                                    isPlaying = true
                                }

                                // end of animation
                                if (progress == 1f) {
                                    isPlaying = false
                                }*/
            }

            LottieAnimation(
                composition = composition,
                modifier = Modifier
                    .size(400.dp),
                progress = {
                    if (appLaunchedFirstTime) {
                        // start of animation
                        if (progress == 0f) {
                            isPlaying = true
                            isVisible = true
                        }

                        // end of animation
                        if (progress == 1f) {
                            isPlaying = false
                            isVisible = false
                        }
                        Log.d("ANI", "app launch animation if ${appLaunchedFirstTime}")

                    } else {
                        if (progress == 0f) {
                            isPlaying = true
                            isVisible = true
                        }

                        // end of animation
                        if (progress == 1f) {
                            isPlaying = false
                            isVisible = false
                        }
                        Log.d("ANI", "app launch animation else ${appLaunchedFirstTime}")

                    }

                    progress
                }
            )


            //Log.d("Animation", "Progress = $progress")

            //Log.d("Animation", "Progress = $progress, previousProgress = $previousProgress")

            /*if (progress == 1.0f){
                if (previousProgress == 2){
                    isPlaying = false
                    isVisible = false
                    previousProgress = 1
                }
                previousProgress++
                //previousProgress = progress
            }else{
                //previousProgress = progress
            }
*/
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /*Button(
                onClick = {
                    isPlaying = true
                    appLaunchedFirstTime = false
                }
            ) {
                Text(text = "Start Animation")
            }*/
            TextWithShadow(text = title, textSize = 25.sp, modifier = Modifier)
            TextWithShadow(text = description, textSize = 15.sp, modifier = Modifier)
        }
    }
}


@Composable
fun LottieFavouriteTap() {

}
//
//@Preview(showBackground = true)
//@Composable
//fun LottieFavouriteTapPreview() {
//    LottieFavouriteTap()
//}


@Composable
fun ComposeCenterImageView(resource: Int, onTapHeart: () -> Unit) {
    val imageModifier = Modifier
        .size(30.dp)
        .layoutId("center_image")
        .clickable(
            interactionSource = remember {
                MutableInteractionSource()
            },
            indication = null
        ) {
            onTapHeart.invoke()
        }

    Image(
        painter = painterResource(id = resource),
        contentDescription = null, // Provide a proper content description
        modifier = imageModifier,
    )
}

@Composable
fun TextWithShadow(
    text: String,
    textSize: TextUnit,
    modifier: Modifier
) {
    Text(
        text = text,
        color = Color.White,
        fontSize = textSize,
        modifier = modifier
            .offset(
                x = 2.dp,
                y = 2.dp
            )
            .fillMaxWidth()
            .alpha(0.75f)
            .padding(5.dp, 5.dp, 5.dp, 0.dp)
    )
}

@Preview
@Composable
fun SnackItemPreview() {
    SnackItem(title = "This is Title",
        description = "This is the description text for the video",
        R.drawable.ic_heart_white,
        {},
        {},
        {}
    )
}