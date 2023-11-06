package com.frank.word.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.frank.word.CurrentClassStr
import com.frank.word.R
import com.frank.word.iShowDel
import com.frank.word.iShowFavorite
import com.frank.word.iShowForeign
import com.frank.word.iShowMeaning
import com.frank.word.iShowNormal
import com.frank.word.iShowPronunciation
import com.frank.word.isFirstTime
import com.frank.word.isForeignOnly
import com.frank.word.isShowCixingDialog
import com.frank.word.isShowEditText
import com.frank.word.isShowList
import com.frank.word.isShowSettingDialog
import com.frank.word.isToSaveInfo
import com.frank.word.loopNumber
import com.frank.word.mediaPlayer
import com.frank.word.pauseTime
import com.frank.word.playVolume
import com.frank.word.sortType


@Composable
fun ReadInfo() {
    val sp = LocalContext.current.getSharedPreferences("MY_WORD_RECITE_APP", Context.MODE_PRIVATE)
    loopNumber = sp.getInt("loopNumber", 1)
    pauseTime = sp.getLong("pauseTime", 0)
    isShowEditText = sp.getBoolean("isShowEditText", false)
    CurrentClassStr = sp.getString("CurrentClassStr", "全部")!!
    sortType = sp.getInt("sortType", 0)
    iShowDel = sp.getBoolean("iShowDel", true)
    iShowNormal = sp.getBoolean("iShowNormal", true)
    iShowFavorite = sp.getBoolean("iShowFavorite", true)
    iShowForeign = sp.getBoolean("iShowForeign", true)
    iShowPronunciation = sp.getBoolean("iShowPronunciation", true)
    iShowMeaning = sp.getBoolean("iShowMeaning", true)
    isForeignOnly = sp.getBoolean("isForeignOnly", false)
    isShowList = sp.getBoolean("isShowList", false)
    playVolume = if (sp.getBoolean("isMute", false)) 0.0f else 1f
}

@Composable
fun SaveInfo() {
    val sp = LocalContext.current.getSharedPreferences("MY_WORD_RECITE_APP", Context.MODE_PRIVATE)
    val editor: SharedPreferences.Editor = sp.edit()
    editor.putInt("loopNumber", loopNumber)
    editor.putLong("pauseTime", pauseTime)
    editor.putBoolean("isShowEditText", isShowEditText)
    editor.putString("CurrentClassStr", CurrentClassStr)
    editor.putInt("sortType", sortType)
    editor.putBoolean("iShowDel", iShowDel)

    editor.putBoolean("iShowNormal", iShowNormal)
    editor.putBoolean("iShowFavorite", iShowFavorite)
    editor.putBoolean("iShowForeign", iShowForeign)
    editor.putBoolean("iShowPronunciation", iShowPronunciation)
    editor.putBoolean("iShowMeaning", iShowMeaning)
    editor.putBoolean("isForeignOnly", isForeignOnly)
    editor.putBoolean("isShowList", isShowList)
    editor.putBoolean("isMute", playVolume == 0f)

    //apply()是异步写入数据
    editor.apply()
    //commit()是同步写入数据
    //editor.commit()
}

@Composable
private fun ShowSettingDialog(
    onDismiss: () -> Unit,
    onNegativeClick: () -> Unit,
) {
    SetCixingDialog()
    ReadInfo()
    Dialog(onDismissRequest = onDismiss) {

        Card(
            //elevation = 8.dp,
            shape = RoundedCornerShape(12.dp)
        ) {

            Column(modifier = Modifier.padding(8.dp)) {

                Text(
                    text = LocalContext.current.getString(R.string.app_name),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.White)
                ) {}
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = LocalContext.current.getString(R.string.loop_count) + " (" + loopNumber.toString() + ")",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Slider(
                    value = loopNumber.toFloat(),
                    onValueChange = {
                        loopNumber = it.toInt()
                    },
                    valueRange = 0f..5f,
                    onValueChangeFinished = {}
                )
                Row(Modifier.padding(start = 5.dp)) {
                    Column(
                        Modifier
                            .weight(2.0f)
                    ) {
                        Text(
                            text = "间隔时间 ($pauseTime) 毫秒",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                        Slider(
                            value = pauseTime.toFloat() / 10,
                            onValueChange = {
                                pauseTime = it.toLong() * 10
                            },
                            valueRange = 0f..500f,
                            onValueChangeFinished = {}
                        )
                    }
                    Column(
                        modifier = Modifier
                            .weight(1.0f)
                    ) {
                        Checkbox(
                            checked = isShowEditText,
                            onCheckedChange = {
                                isShowEditText = !isShowEditText
                            },
                            modifier = Modifier.align(CenterHorizontally)
                        )
                        Text(
                            text = "显示输入框",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.align(CenterHorizontally)
                        )
                    }
                }
                Row(Modifier.padding(start = 5.dp)) {
                    Text(
                        text = "词性",
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable { isShowCixingDialog = true })
                    Text(
                        text = CurrentClassStr,
                        modifier = Modifier.clickable { isShowCixingDialog = true },
                    )
                }
                Row(
                    Modifier
                        .selectableGroup()
                        .padding(start = 5.dp)
                ) {
                    Column(
                        Modifier
                            .weight(1.0f)
                    ) {
                        RadioButton(
                            selected = sortType == 0,
                            onClick = { sortType = 0 },
                            modifier = Modifier.align(CenterHorizontally)
                        )
                        Text(
                            text = "顺序播放",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.align(CenterHorizontally)
                        )
                    }
                    Column(
                        Modifier
                            .weight(1.0f)
                    ) {
                        RadioButton(
                            selected = sortType == 1,
                            onClick = { sortType = 1 },
                            modifier = Modifier.align(CenterHorizontally)
                        )
                        Text(
                            text = "课内乱序",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.align(CenterHorizontally)
                        )
                    }
                    Column(
                        Modifier
                            .weight(1.0f)
                    ) {
                        RadioButton(
                            selected = sortType == 2,
                            onClick = { sortType = 2 },
                            modifier = Modifier.align(CenterHorizontally)
                        )
                        Text(
                            text = "全部乱序",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.align(CenterHorizontally)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
                Row(
                    Modifier
                        .padding(start = 5.dp)
                ) {
                    Column(
                        Modifier
                            .weight(1.0f)
                    ) {
                        Checkbox(
                            checked = iShowDel,
                            onCheckedChange = {
                                iShowDel = !iShowDel
                            },
                            modifier = Modifier.align(CenterHorizontally)
                        )
                        Text(
                            text = "已记住",
                            modifier = Modifier.align(CenterHorizontally)
                        )
                    }
                    Column(Modifier.weight(1.0f)) {
                        Checkbox(
                            checked = iShowNormal,
                            onCheckedChange = {
                                iShowNormal = !iShowNormal
                            },
                            modifier = Modifier.align(CenterHorizontally)
                        )
                        Text(
                            text = "生词",
                            modifier = Modifier.align(CenterHorizontally)
                        )
                    }
                    Column(
                        Modifier
                            .weight(1.0f)
                    ) {
                        Checkbox(
                            checked = iShowFavorite,
                            onCheckedChange = {
                                iShowFavorite = !iShowFavorite
                            },
                            modifier = Modifier.align(CenterHorizontally)
                        )
                        Text(
                            text = "重点",
                            modifier = Modifier.align(CenterHorizontally)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
                Row(
                    Modifier
                        .padding(start = 5.dp)
                ) {
                    Column(
                        Modifier
                            .weight(1.0f)
                    ) {
                        Checkbox(
                            checked = iShowForeign,
                            onCheckedChange = {
                                iShowForeign = !iShowForeign
                            },
                            modifier = Modifier.align(CenterHorizontally)
                        )
                        Text(
                            text = "日文",
                            modifier = Modifier.align(CenterHorizontally)
                        )
                    }
                    Column(Modifier.weight(1.0f)) {
                        Checkbox(
                            checked = iShowPronunciation,
                            onCheckedChange = {
                                iShowPronunciation = !iShowPronunciation
                            },
                            modifier = Modifier.align(CenterHorizontally)
                        )
                        Text(
                            text = "假名",
                            modifier = Modifier.align(CenterHorizontally)
                        )
                    }
                    Column(
                        Modifier
                            .weight(1.0f)
                    ) {
                        Checkbox(
                            checked = iShowMeaning,
                            onCheckedChange = {
                                iShowMeaning = !iShowMeaning
                            },
                            modifier = Modifier.align(CenterHorizontally)
                        )
                        Text(
                            text = "中文意思",
                            modifier = Modifier.align(CenterHorizontally)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
                Row(
                    Modifier
                        .padding(start = 5.dp)
                ) {
                    Column(
                        Modifier
                            .weight(1.0f)
                    ) {
                        Checkbox(
                            //TODO
                            checked = isForeignOnly,    //isMiddleTime,
                            onCheckedChange = {
//                                isMiddleTime = !isMiddleTime
                                isForeignOnly = !isForeignOnly
                            },
                            modifier = Modifier.align(CenterHorizontally)
                        )
                        Text(
                            text = "只播日文",
                            modifier = Modifier.align(CenterHorizontally)
                        )
                    }
                    Column(Modifier.weight(1.0f)) {
                        Checkbox(
                            checked = isShowList,
                            onCheckedChange = {
                                isShowList = !isShowList
                            },
                            modifier = Modifier.align(CenterHorizontally)
                        )
                        Text(
                            text = "列表模式",
                            modifier = Modifier.align(CenterHorizontally)
                        )
                    }
                    Column(
                        Modifier
                            .weight(1.0f)
                    ) {
                        Checkbox(
                            checked = playVolume == 0f,
                            onCheckedChange = {
                                playVolume = if (it) 0f else 1f
                                if (!isFirstTime) {
                                    mediaPlayer.setVolume(playVolume, playVolume)
                                }
                            },
                            modifier = Modifier.align(CenterHorizontally)
                        )
                        Text(
                            text = "静音播放",
                            modifier = Modifier.align(CenterHorizontally)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.White)
                ) {}
                Spacer(modifier = Modifier.height(8.dp))
                // Buttons
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    TextButton(onClick = onNegativeClick) {
                        Text(text = LocalContext.current.getString(R.string.btnOK))
                    }
                }
            }
        }
    }
}

@Composable
fun SetSettingDialog() {
    if (isShowSettingDialog) {
        ShowSettingDialog(
            onDismiss = {
                isShowSettingDialog = !isShowSettingDialog
                isToSaveInfo = true
            },
            onNegativeClick = {
                isShowSettingDialog = !isShowSettingDialog
                isToSaveInfo = true
            },
        )
    }
}
