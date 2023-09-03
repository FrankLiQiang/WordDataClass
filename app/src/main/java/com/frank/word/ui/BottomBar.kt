package com.frank.word.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frank.word.LongPressButton
import com.frank.word.R
import com.frank.word.addFavoriteWord
import com.frank.word.changeTime
import com.frank.word.delWord
import com.frank.word.setWordNormal
import com.frank.word.showNext
import com.frank.word.showPrev
import com.frank.word.ui.theme.WordTheme

@Composable
fun BottomBar() {
    Row(
        Modifier
            .height(130.dp)
            .background(Color.Black)
    ) {
        LongPressButton(
            ::showPrev,
            R.drawable.twotone_skip_previous_24, "Previous",
            Modifier.weight(1f)
        )
        if (isAdjust) {
            TabItem(
                R.drawable.outline_chevron_left_24, "Left",
                Color.White,
                {changeTime(true)},
                Modifier
                    .weight(1f)
            )
            TabItem(
                R.drawable.outline_chevron_right_24, "Right",
                Color.White,
                {changeTime(false)},
                Modifier
                    .weight(1f)
            )
        } else {
            TabItem(
                R.drawable.twotone_delete_forever_24, "Remove",
                Color.White,
                ::delWord,
                Modifier
                    .weight(1f)
            )
            TabItem(
                R.drawable.twotone_more_horiz_24, "All",
                Color.White,
                ::setWordNormal,
                Modifier
                    .weight(1f)
            )
            TabItem(
                R.drawable.twotone_favorite_border_24, "Favorite",
                Color.White,
                ::addFavoriteWord,
                Modifier
                    .weight(1f)
            )
        }
        LongPressButton(
            ::showNext,
            R.drawable.twotone_skip_next_24, "Next",
            Modifier.weight(1f)
        )
    }
}

@Composable
fun TabItem(@DrawableRes iconId: Int, title: String, tint: Color, event:()->Unit, modifier: Modifier = Modifier) {
    val count = remember { mutableStateOf(0) }
    Column(
        modifier
            .padding(vertical = 8.dp)
            .background(if (count.value == 1) Color.Cyan else Color.Transparent )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { count.value = 1}, // 按下
//                    onDoubleTap = { count.value = 2 }, // 双击
//                    onLongPress = { count.value = 3 }, // 长按
                    onTap = { count.value = 4
                        event()} // 单击
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painterResource(iconId), title,
            Modifier
                .size(50.dp)
                .fillMaxWidth(),
            tint = tint
        )
        Text(title, fontSize = 11.sp, color = tint)
    }
}

@Preview(showBackground = true)
@Composable
fun WeBottomBarPreview() {
    WordTheme {
        BottomBar()
    }
}
