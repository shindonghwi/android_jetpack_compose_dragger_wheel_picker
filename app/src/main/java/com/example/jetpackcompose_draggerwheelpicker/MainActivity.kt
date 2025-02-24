package com.example.jetpackcompose_draggerwheelpicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.jetpackcompose_draggerwheelpicker.ui.theme.JetpackCompose_DraggerWheelPickerTheme
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.LazyListSnapperLayoutInfo
import dev.chrisbanes.snapper.rememberLazyListSnapperLayoutInfo
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import kotlin.math.abs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackCompose_DraggerWheelPickerTheme {
                Surface(color = MaterialTheme.colors.background) {

                    val lists = arrayListOf<Int>().apply {
                        repeat(100) { add(it) }
                    }

                    WheelPicker(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Yellow.copy(alpha = 0.2f))
                            .padding(all = 20.dp),
                        pickerMaxHeight = 250.dp,
                        defaultTextStyle = MaterialTheme.typography.body1,
                        centerTextStyle = MaterialTheme.typography.h6,
                        lists = lists,
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
    pickerMaxHeight: Dp = 250.dp,
    initIndex: Int? = null,
    lists: ArrayList<Int>,
    defaultTextStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.body1,
    centerTextStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.h6,
    defaultTextColor: Color = Color.Black.copy(alpha = 0.4f),
    centerTextColor: Color = Color.Black.copy(alpha = 1.0f),
    selectedBackgroundColor: Color = Color.Black.copy(alpha = 0.3f)
) {
    val lazyListState = rememberLazyListState(initialFirstVisibleItemIndex = (Int.MAX_VALUE / 2.0).toInt() - 25)
    val layoutInfo: LazyListSnapperLayoutInfo = rememberLazyListSnapperLayoutInfo(lazyListState) // wheel picker 의 layout 정보
    var currentIndex: Int? // 현재 선택된 아이템의 인덱스

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

                currentIndex = layoutInfo.currentItem?.index?.minus(Int.MAX_VALUE / 2)?.plus(23)?.rem(lists.size)

                if (currentIndex != null && currentIndex!! < 0) {
                    currentIndex = currentIndex!! + lists.size
                }

                val curTextIsCenter = currentIndex == lists[index]
                val curTextIsCenterDiffer1 = currentIndex == abs(lists[index] - 1) || currentIndex == abs(lists[index] + 1)
                val curTextIsCenterDiffer2 = currentIndex == abs(lists[index] - 2) || currentIndex == abs(lists[index] + 2)

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(pickerMaxHeight * 0.215f)
                        .padding(vertical = 14.dp)
                        .align(Alignment.Center)
                        .scale(
                            scaleX = if (curTextIsCenter) 1.2f else if (curTextIsCenterDiffer1) 1.0f else 0.8f,
                            scaleY = if (curTextIsCenter) 1.2f else if (curTextIsCenterDiffer1) 1.0f else 0.8f
                        ),
                    text = if (lists[index] == 0) "표시안함" else "${lists[index]}번 아이템",
                    style = if (curTextIsCenter) centerTextStyle else defaultTextStyle,
                    color = if (curTextIsCenter) centerTextColor else defaultTextColor,
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

        LaunchedEffect(Unit) {
            initIndex?.let {
                lazyListState.scrollToItem(
                    index = (Int.MAX_VALUE / 2.0).toInt() - 25 + it,
                    scrollOffset = lazyListState.layoutInfo.visibleItemsInfo[0].size / 5
                )
            } ?: kotlin.run {
                lazyListState.scrollBy(
                    value = lazyListState.layoutInfo.visibleItemsInfo[0].size.toFloat() / 5
                )
            }
        }
    }
}