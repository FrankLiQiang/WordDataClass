package com.frank.word

import android.view.Menu
import android.view.MenuItem
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

val numberArray = IntArray(22)
var rangeItem: MenuItem? = null
var chooseItem: MenuItem? = null
var chooseItem0: MenuItem? = null
var chooseItem1: MenuItem? = null

fun menuInit(menu: Menu?) {
    numberArray[0] = R.id.single_menu_00
    numberArray[1] = R.id.single_menu_01
    numberArray[2] = R.id.single_menu_02
    numberArray[3] = R.id.single_menu_03
    numberArray[4] = R.id.single_menu_04
    numberArray[5] = R.id.single_menu_05
}

@Preview
@Composable
fun ChooseLessonMenu() {
    DropdownMenu(expanded = isShowPopupMenu, onDismissRequest = {
    }) {
        DropdownMenuItem(text = {
            Text("Close")
        }, onClick = {
            isShowPopupMenu = false
        })
        for ((index, value) in files.withIndex()) {
            println("the element at $index is $value")
            DropdownMenuItem(text = {
                Text(
                    value.name ?: "",
                    color = if (index in (fileBeginIndex..fileEndIndex)) Color.Red else Color.White
                )
            }, onClick = {
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
                isShowPopupMenu = false
            })
        }
    }
}

