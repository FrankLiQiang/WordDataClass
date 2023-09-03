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
import com.frank.word.fileBeginIndex
import com.frank.word.fileEndIndex

var maxLessonNum = 0

@Composable
private fun CustomDialogWithResultExample(
    onDismiss: () -> Unit,
    onNegativeClick: () -> Unit,
    onPositiveClick: (Int, Int) -> Unit
) {
    var lessonFrom by remember { mutableStateOf(fileBeginIndex + 1) }
    var lessonTo by remember { mutableStateOf(fileEndIndex + 1) }

    Dialog(onDismissRequest = onDismiss) {

        Card(
            //elevation = 8.dp,
            shape = RoundedCornerShape(12.dp)
        ) {

            Column(modifier = Modifier.padding(8.dp)) {

                Text(
                    text = "Select Lesson Range",
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

                        Text(text = "Lesson From $lessonFrom")
                        Slider(
                            value = lessonFrom.toFloat(),
                            onValueChange = {
                                if (it.toInt() > lessonFrom) {
                                    lessonTo = it.toInt()
                                }
                                lessonFrom = it.toInt()
                            },
                            valueRange = 1f..maxLessonNum.toFloat(),
                            onValueChangeFinished = {}
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = "Lesson To $lessonTo")
                        Slider(
                            value = lessonTo.toFloat(),
                            onValueChange = {
                                if (lessonFrom > it.toInt()) {
                                    lessonFrom = it.toInt()
                                }
                                lessonTo = it.toInt()
                            },
                            valueRange = 1f..maxLessonNum.toFloat(),
                            onValueChangeFinished = {}
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Spacer(modifier = Modifier.height(8.dp))
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
                        onPositiveClick(lessonFrom, lessonTo)
                    }) {
                        Text(text = "OK")
                    }
                }
            }
        }
    }
}

@Composable
fun SetLessonRangDialog() {

    if (isShowDialog) {
        CustomDialogWithResultExample(
            onDismiss = {
                isShowDialog = !isShowDialog
            },
            onNegativeClick = {
                isShowDialog = !isShowDialog
            },
            onPositiveClick = { from, to ->
                isShowDialog = !isShowDialog

                fileBeginIndex = from - 1
                fileEndIndex = to - 1
            }
        )
    }
}
