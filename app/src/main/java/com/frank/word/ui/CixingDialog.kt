package com.frank.word.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.frank.word.CurrentClassStr
import com.frank.word.isShowCixingDialog
import com.frank.word.isToDraw
import com.frank.word.myFontSize
import com.frank.word.wClass

@Composable
private fun ShowCixingDialog(
    onDismiss: () -> Unit,
    onNegativeClick: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {

        Card(
            shape = RoundedCornerShape(12.dp)
        ) {
            Column {
                LazyColumn(
                    modifier = Modifier
                        .height(500.dp)
                ) {

                    if (isToDraw < -1) return@LazyColumn

                    items(wClass) { menuItem ->
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                                .padding(start = 50.dp)
                                .clickable {
                                    CurrentClassStr = menuItem
                                    isShowCixingDialog = false
                                    isToDraw = 1 - isToDraw
                                },
                            text = menuItem,
                            fontSize = 23.sp,
                            color = Color.White,
                            lineHeight = (myFontSize + 2).sp,
                            maxLines = 10,
                            style = MaterialTheme.typography.labelMedium,
                        )
                        Divider(color = Color.LightGray)
                    }
                }
                Divider(color = Color.LightGray)
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onNegativeClick) {
                        Text(text = "Close")
                    }
                }
            }
        }
    }
}

@Composable
fun SetCixingDialog() {

    if (isShowCixingDialog) {
        ShowCixingDialog(
            onDismiss = {
                isShowCixingDialog = !isShowCixingDialog
            },
            onNegativeClick = {
                isShowCixingDialog = !isShowCixingDialog
            },
        )
    }
}
