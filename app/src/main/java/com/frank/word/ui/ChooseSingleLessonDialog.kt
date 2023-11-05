package com.frank.word.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.frank.word.fileBeginIndex
import com.frank.word.fileEndIndex
import com.frank.word.fileIndex
import com.frank.word.files
import com.frank.word.isChooseSingleLessonDialog
import com.frank.word.isToDraw
import com.frank.word.mp3Uri
import com.frank.word.myFontSize
import com.frank.word.pathAndName
import com.frank.word.readTextFile
import com.frank.word.sortFiles

@Composable
private fun ShowLessonRangeDialog(
    onDismiss: () -> Unit,
    onNegativeClick: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {

        Card(
            //elevation = 8.dp,
            shape = RoundedCornerShape(12.dp)
        ) {
            Column {
                LazyColumn(
                    modifier = Modifier
//                        .background(Color.White)
                        .height(500.dp)
                ) {

                    if (isToDraw < -1) return@LazyColumn

                    itemsIndexed(files) { index, menuItem ->

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                                .padding(start = 50.dp)
                                .clickable {
                                    if (fileIndex != index) {
                                        fileIndex = index
                                        if (fileIndex < fileBeginIndex) {
                                            fileBeginIndex = fileIndex
                                        }
                                        if (fileIndex > fileEndIndex) {
                                            fileEndIndex = fileIndex
                                        }
                                        sortFiles()
                                        pathAndName = files[fileIndex].uri.path!!
                                        mp3Uri = files[fileIndex].uri
                                        readTextFile(0)
                                    }
                                    isChooseSingleLessonDialog = false
                                    isToDraw = 1 - isToDraw
                                },
                            text = menuItem.name!!.substring(0, menuItem.name!!.length - 4),
                            fontSize = 23.sp,
//                            color = Color.Black,
                            lineHeight = (myFontSize + 2).sp,
                            maxLines = 10,
                            style = MaterialTheme.typography.labelMedium,
                        )
                        Divider(color = Color.LightGray)
                    }
                }
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
fun SetChooseSingleLessonDialog() {

    if (isChooseSingleLessonDialog) {
        ShowLessonRangeDialog(
            onDismiss = {
                isChooseSingleLessonDialog = !isChooseSingleLessonDialog
            },
            onNegativeClick = {
                isChooseSingleLessonDialog = !isChooseSingleLessonDialog
            },
        )
    }
}
