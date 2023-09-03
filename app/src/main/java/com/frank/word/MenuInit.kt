package com.frank.word

import android.view.Menu
import android.view.MenuItem
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.frank.word.ui.isShowPopupMenu

val numberArray = IntArray(22)
val ClassColor = IntArray(16)
val menuWordClass = arrayOfNulls<MenuItem>(16)

fun menuInit(menu: Menu?) {
    menuWordClass[0] = menu!!.findItem(R.id.class_noun)
    menuWordClass[1] = menu.findItem(R.id.class_daici)
    menuWordClass[2] = menu.findItem(R.id.class_shuci)
    menuWordClass[3] = menu.findItem(R.id.class_verb_1)
    menuWordClass[4] = menu.findItem(R.id.class_verb_2)
    menuWordClass[5] = menu.findItem(R.id.class_verb_3)
    menuWordClass[6] = menu.findItem(R.id.class_adj_1)
    menuWordClass[7] = menu.findItem(R.id.class_adj_2)
    menuWordClass[8] = menu.findItem(R.id.class_liantici)
    menuWordClass[9] = menu.findItem(R.id.class_fuci)
    menuWordClass[10] = menu.findItem(R.id.class_jiexuci)
    menuWordClass[11] = menu.findItem(R.id.class_tanci)
    menuWordClass[12] = menu.findItem(R.id.class_zhudongci)
    menuWordClass[13] = menu.findItem(R.id.class_zhuci)
    menuWordClass[14] = menu.findItem(R.id.class_zhuanyou)
    menuWordClass[15] = menu.findItem(R.id.class_duanyu)

    numberArray[0] = R.id.single_menu_00
    numberArray[1] = R.id.single_menu_01
    numberArray[2] = R.id.single_menu_02
    numberArray[3] = R.id.single_menu_03
    numberArray[4] = R.id.single_menu_04
    numberArray[5] = R.id.single_menu_05

    numberArray[6] = R.id.class_noun
    numberArray[7] = R.id.class_daici
    numberArray[8] = R.id.class_shuci
    numberArray[9] = R.id.class_verb_1
    numberArray[10] = R.id.class_verb_2
    numberArray[11] = R.id.class_verb_3
    numberArray[12] = R.id.class_adj_1
    numberArray[13] = R.id.class_adj_2
    numberArray[14] = R.id.class_liantici
    numberArray[15] = R.id.class_fuci
    numberArray[16] = R.id.class_jiexuci
    numberArray[17] = R.id.class_tanci
    numberArray[18] = R.id.class_zhudongci
    numberArray[19] = R.id.class_zhuci
    numberArray[20] = R.id.class_zhuanyou
    numberArray[21] = R.id.class_duanyu

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
                    playMp3(files[fileIndex].uri, 0)
                }
                isShowPopupMenu = false
            })
        }
    }
}

