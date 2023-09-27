package com.frank.word.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.frank.word.isShowPauseTimeDialog
import com.frank.word.pauseTime

var maxPauseTime = 0

@Composable
private fun ShowPauseTimeDialog(
    onDismiss: () -> Unit,
    onNegativeClick: () -> Unit,
    onPositiveClick: (Long) -> Unit
) {
    var thePauseTime by remember { mutableStateOf(pauseTime) }

    Dialog(onDismissRequest = onDismiss) {

        Card(
            //elevation = 8.dp,
            shape = RoundedCornerShape(12.dp)
        ) {

            Column(modifier = Modifier.padding(8.dp)) {

                Text(
                    text = "Pause Time",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Color Selection
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {


                    Column {

                        Text(text = "Pause Time $thePauseTime 毫秒")
                        Slider(
                            value = thePauseTime.toFloat() / 10,
                            onValueChange = {
                                thePauseTime = it.toLong() * 10
                            },
                            valueRange = 0f..500f,
                            onValueChangeFinished = {}
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }

                // Buttons
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    TextButton(onClick = onNegativeClick) {
                        Text(text = "CANCEL")
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    TextButton(onClick = {
                        onPositiveClick(thePauseTime)
                    }) {
                        Text(text = "OK")
                    }
                }
            }
        }
    }
}

@Composable
fun SetPauseTimeDialog() {

    if (isShowPauseTimeDialog) {
        ShowPauseTimeDialog(
            onDismiss = {
                isShowPauseTimeDialog = !isShowPauseTimeDialog
            },
            onNegativeClick = {
                isShowPauseTimeDialog = !isShowPauseTimeDialog
            },
            onPositiveClick = { pTime ->
                isShowPauseTimeDialog = !isShowPauseTimeDialog
                pauseTime = pTime
            }
        )
    }
}
