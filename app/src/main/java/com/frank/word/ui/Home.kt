package com.frank.word.ui

import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frank.word.R
import com.frank.word.Word
import com.frank.word.addMiddleTime
import com.frank.word.addTime
import com.frank.word.currentSentence1
import com.frank.word.currentSentence2
import com.frank.word.currentSentence3
import com.frank.word.currentShowWord
import com.frank.word.currentWordClass
import com.frank.word.iShowForeign
import com.frank.word.iShowForeign0
import com.frank.word.iShowMeaning
import com.frank.word.iShowMeaning0
import com.frank.word.iShowPronunciation
import com.frank.word.iShowPronunciation0
import com.frank.word.isDEL
import com.frank.word.isEditFile
import com.frank.word.isFAVORITE
import com.frank.word.isMiddleTime
import com.frank.word.isPlay
import com.frank.word.isShowEditText
import com.frank.word.isShowList
import com.frank.word.isToAddTime
import com.frank.word.isToDraw
import com.frank.word.isToSaveInfo
import com.frank.word.musicStep
import com.frank.word.myFontSize
import com.frank.word.showCurrentWord
import com.frank.word.showNext
import com.frank.word.showPrev
import com.frank.word.wordClassColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.text.DecimalFormat

private var StartX = 0f
var lastClickItem: Word? = null
var isItem by mutableStateOf(true)
val decimalFormat = DecimalFormat("000")
lateinit var moveToCurrentWord: (Int) -> Unit

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Home(
    progress: Float,
    doSlider: (Float) -> Unit,
    pause: (Int) -> Unit,
    maxProgress: Float
) {

    @Composable
    fun showSlider(padding: Int = 20) {
        Column() {
            Row(modifier = Modifier.padding(10.dp)) {
                Text(
                    "Font Size",
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier
                        .size(90.dp, 20.dp)
                        .padding(start = 5.dp, end = 0.dp)
                )
                Slider(
                    value = progress,
                    onValueChange = doSlider,
                    onValueChangeFinished = { isToSaveInfo = true },
                    valueRange = 15f..maxProgress,
                    modifier = Modifier
                        .height(20.dp)
                )
            }
            Row(modifier = Modifier.height(padding.dp)) {}
        }
    }

    Column(Modifier.background(Color.Black)) {
        if (isToDraw < -1) return
        SetChooseSingleLessonDialog()
        SetLessonRangDialog()
        if (isShowList) {
            LinearProgressIndicator(
                progress = musicStep,
                Modifier
                    .fillMaxWidth()
            )
            WordList(Modifier
                .weight(1f))
            showSlider()
        } else {
            if (!isEditFile) {
                LinearProgressIndicator(
                    progress = musicStep,
                    Modifier
                        .fillMaxWidth()
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    Text(
                        text = currentWordClass,
                        style = TextStyle(
                            fontSize = 17.sp,
                            color = Color.White,
                            textAlign = TextAlign.Start
                        ),
                        modifier = Modifier
                            .width(23.dp)
                            .padding(3.dp)
                            .background(if (wordClassColor == 1) Color.Cyan else Color.Black)
                    )
                    ClickableText(
                        text = AnnotatedString(currentShowWord),
                        style = TextStyle(
                            fontSize = myFontSize.sp,
                            color = if (isPlay) Color.White else Color.Cyan,
                            textAlign = TextAlign.Start
                        ),
                        onClick = pause,
                        modifier = Modifier
                            .fillMaxWidth()
                            .pointerInteropFilter {
                                when (it.action) {
                                    MotionEvent.ACTION_DOWN -> {
                                        iShowForeign0 = iShowForeign
                                        iShowPronunciation0 = iShowPronunciation
                                        iShowMeaning0 = iShowMeaning
                                        iShowForeign = true
                                        iShowPronunciation = true
                                        iShowMeaning = true
                                    }
                                    MotionEvent.ACTION_UP -> {
                                        iShowForeign = iShowForeign0
                                        iShowPronunciation = iShowPronunciation0
                                        iShowMeaning = iShowMeaning0
                                    }
                                }
                                showCurrentWord()
                                true
                            }
                            .background(
                                if (isFAVORITE)
                                    colorResource(R.color.purple_700)
                                else if (isDEL)
                                    colorResource(R.color.gray)
                                else
                                    colorResource(R.color.transparent)
                            )
                    )
                }
            }

            val m = Modifier
                .fillMaxWidth()
                .pointerInteropFilter {
                    when (it.action) {
                        MotionEvent.ACTION_DOWN -> {
                            StartX = it.x
                        }

                        MotionEvent.ACTION_UP -> {
                            if (kotlin.math.abs(it.x - StartX) < 10) {
                                pause(0)
                            } else {
                                if (it.x < StartX) {
                                    showNext()
                                } else {
                                    showPrev()
                                }
                            }
                        }
                    }
                    true
                }
                .padding(5.dp)
            if (currentSentence1.isNotEmpty()) {
                Text(
                    text = AnnotatedString(currentSentence1),
                    fontSize = myFontSize.sp,
                    color = if (isPlay) Color.White else Color.Cyan,
                    textAlign = TextAlign.Start,
                    lineHeight = (myFontSize * 1.2f).sp,
                    modifier = m
                )
            }
            if (currentSentence2.isNotEmpty()) {
                Text(
                    text = AnnotatedString(currentSentence2),
                    fontSize = myFontSize.sp,
                    lineHeight = (myFontSize * 1.2f).sp,
                    color = if (isPlay) Color.White else Color.Cyan,
                    textAlign = TextAlign.Start,
                    modifier = m
                )
            }
            if (currentSentence3.isNotEmpty()) {
                Text(
                    text = AnnotatedString(currentSentence3),
                    fontSize = myFontSize.sp,
                    lineHeight = (myFontSize * 1.2f).sp,
                    color = if (isPlay) Color.White else Color.Cyan,
                    textAlign = TextAlign.Start,
                    modifier = m
                )
            }
            if (isShowEditText && !isToAddTime) {
                ShowTextFieldFun(
                    Modifier
                        .fillMaxWidth()
                        .weight(1.0f)
                        .padding(5.dp)
                )
            } else if (!isToAddTime) {
                Row(m.weight(1.0f)) {}
            }
            if (isToAddTime) {
                Icon(
                    painterResource(R.drawable.outline_add_circle_outline_24),
                    "",
                    Modifier
                        .weight(3.0f)
                        .fillMaxWidth()
                        .padding(50.dp)
                        .clickable {
                            if (isMiddleTime) {
                                addMiddleTime()
                            } else {
                                addTime()
                            }
                        },
                    tint = Color.LightGray
                )
                if (isMiddleTime) {
                    BottomBar()
                }
            } else {
                showSlider(0)
                BottomBar()
            }
        }
    }
}


@Composable
fun SetBlackSystemBars() {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Black,
            darkIcons = useDarkIcons,
            isNavigationBarContrastEnforced = false
        )
        systemUiController.setStatusBarColor(
            color = Color.Black,
            darkIcons = false
        )
        systemUiController.setNavigationBarColor(
            color = Color.Black,
            darkIcons = false
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    Home(50.0f, {}, {}, 50.0f)
}
