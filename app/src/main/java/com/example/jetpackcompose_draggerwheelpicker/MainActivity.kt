package com.example.jetpackcompose_draggerwheelpicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.jetpackcompose_draggerwheelpicker.ui.theme.JetpackCompose_DraggerWheelPickerTheme
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackCompose_DraggerWheelPickerTheme {
                Surface(color = MaterialTheme.colors.background) {

                    val lists = arrayListOf<String>().apply {
                        repeat(100) {
                            add("$it")
                        }
                    }

                    WheelPicker(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        pickerMaxHeight = 250.dp,
                        lists = lists
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSnapperApi::class)
@Composable
fun WheelPicker(
    modifier: Modifier,
    pickerMaxHeight: Dp,
    lists: ArrayList<String>,
    selectedBackgroundColor: Color = Color.Black.copy(alpha = 0.1f)
) {

    val lazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = Int.MAX_VALUE / 2 - 25,
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(pickerMaxHeight),
            state = lazyListState,
            flingBehavior = rememberSnapperFlingBehavior(lazyListState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(
                count = Int.MAX_VALUE,
                key = { it }
            ) {
                val index = it % lists.size

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 14.dp),
                    text = lists[index],
                    style = MaterialTheme.typography.h5,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(pickerMaxHeight * 0.215f)
                .background(selectedBackgroundColor)
        )
    }
}
