package com.frank.word

import android.view.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

val numberArray = IntArray(22)
val ClassColor = IntArray(16)

fun menuInit(menu: Menu?) {

    numberArray[0] = R.id.single_menu_00
    numberArray[1] = R.id.single_menu_01
    numberArray[2] = R.id.single_menu_02
    numberArray[3] = R.id.single_menu_03
    numberArray[4] = R.id.single_menu_04
    numberArray[5] = R.id.single_menu_05

    ClassColor[0] = R.color.mingci
    ClassColor[1] = R.color.daici
    ClassColor[2] = R.color.shuci
    ClassColor[3] = R.color.dong1
    ClassColor[4] = R.color.dong2
    ClassColor[5] = R.color.dong3
    ClassColor[6] = R.color.adj1
    ClassColor[7] = R.color.adj2
    ClassColor[8] = R.color.liantici
    ClassColor[9] = R.color.fuci
    ClassColor[10] = R.color.jiexuci
    ClassColor[11] = R.color.tanci
    ClassColor[12] = R.color.zhudongci
    ClassColor[13] = R.color.zhuci
    ClassColor[14] = R.color.zhuanyou
    ClassColor[15] = R.color.duanyu
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

