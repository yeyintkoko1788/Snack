package com.yeyint.tiktoktest.compose.ui

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.yeyint.tiktoktest.R


@Composable
fun SnackItem(title : String, description : String, resource: Int, onTapHeart : () -> Unit, singleClick : () -> Unit, doubleClick : () -> Unit){
    val handler = Handler(Looper.getMainLooper())
    val interactionSource = remember { MutableInteractionSource() }
    var counterClicks = 0
    var isBusy = false
    Surface(color = Color.Transparent,
    modifier = Modifier
        .clickable(
            onClick = {
                if (!isBusy) {
                    isBusy = true
                    counterClicks++
                    handler.postDelayed({
                        if (counterClicks >= 2) {
                            doubleClick.invoke()
                        }
                        if (counterClicks == 1) {
                            singleClick.invoke()
                        }
                        counterClicks = 0
                    }, 200L)
                    isBusy = false
                }
            },
            interactionSource = interactionSource,
            indication = null
        )
        .fillMaxSize(1f)
        .padding(5.dp, 0.dp, 5.dp, 30.dp)){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            contentAlignment = Alignment.CenterEnd
        ){
            ComposeCenterImageView(resource, onTapHeart)
        }
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(modifier = Modifier) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    TextWithShadow(text = title, 25.sp, modifier = Modifier)
                }
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    TextWithShadow(text = description, textSize = 15.sp, modifier = Modifier)
                }
            }
        }
    }
}

@Composable
fun ComposeCenterImageView(resource : Int, onTapHeart: () -> Unit) {
    val imageModifier = Modifier
        .size(30.dp)
        .layoutId("center_image")
        .clickable(
            interactionSource = remember {
                MutableInteractionSource ()
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
    textSize : TextUnit,
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
fun SnackItemPreview(){
    SnackItem(title = "This is Title", description = "This is the description text for the video", R.drawable.ic_heart_white,
        {}, {}, {}
    )
}