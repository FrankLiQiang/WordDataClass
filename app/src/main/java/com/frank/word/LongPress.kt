package com.frank.word

import androidx.annotation.DrawableRes
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.frank.word.ui.TabItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LongPressButton(
    callBack: () -> Unit,
    @DrawableRes iconId: Int,
    title: String,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var isLong: Boolean

    TabItem(
        iconId, title,
        Color.White,
        callBack,
        modifier
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDrag = { _, _ ->},
                    onDragStart = {
                        isLong = true
                        coroutineScope.launch {
                            while (isLong) {
                                callBack()
                                delay(100)
                            }
                        }
                    },
                    onDragEnd = {
                        isLong = false
                    },
                    onDragCancel = {
                        isLong = false
                    }
                )
            }
    )
}
