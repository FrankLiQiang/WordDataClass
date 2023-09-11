package com.frank.word.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frank.word.ChooseLessonMenu
import com.frank.word.R
import com.frank.word.addTime
import com.frank.word.currentSentence1
import com.frank.word.currentSentence2
import com.frank.word.currentShowWord
import com.frank.word.currentWordClass
import com.frank.word.hideFunction
import com.frank.word.inputText
import com.frank.word.isDEL
import com.frank.word.isFAVORITE
import com.frank.word.isPlay
import com.frank.word.isShowEditText
import com.frank.word.isToAddTime
import com.frank.word.musicStep
import com.frank.word.myFontSize
import com.frank.word.wordClassColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    progress: Float,
    doSlider: (Float) -> Unit,
    pause: (Int) -> Unit,
    maxProgress: Float
) {
    val focusRequester = remember { FocusRequester() }
    ChooseLessonMenu()
    Column(Modifier.background(Color.Black)) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(30.dp)      //小米 Redmi Note11 Pro
                //.height(50.dp)        //Google Pixel 6A
        ) {}
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
                modifier = Modifier.width(23.dp)
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
        SetLessonRangDialog()
        if (currentSentence1.isNotEmpty()) {
            Text(
                text = AnnotatedString(currentSentence1),
                fontSize = myFontSize.sp,
                color = if (isPlay) Color.White else Color.Cyan,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
        }
        if (currentSentence2.isNotEmpty()) {
            Text(
                text = AnnotatedString(currentSentence2),
                fontSize = myFontSize.sp,
                color = if (isPlay) Color.White else Color.Cyan,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
        }
        if (isShowEditText && !isToAddTime
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .weight(1.0f)
                    .padding(5.dp)
            ) {
                //TextField
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { hideFunction(it) },
                    label = { Text("Enter text") },
                    textStyle = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = myFontSize.sp
                    ),
                    modifier = Modifier
                        .background(Color.Black)
                        .focusRequester(focusRequester)
                        .focusable()
                        .fillMaxWidth()
                        .padding(start = 5.dp, end = 5.dp)
                )
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
            }
        } else if (!isToAddTime) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .weight(1.0f)
                    .padding(5.dp)
            ) {}
        }
        if (isToAddTime) {
            Icon(
                painterResource(R.drawable.outline_add_circle_outline_24),
                "",
                Modifier
                    .fillMaxSize()
                    .padding(50.dp)
                    .clickable {
                        addTime()
                    },
                tint = Color.LightGray
            )
        } else {
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
                    onValueChangeFinished = {},
                    valueRange = 0f..maxProgress,
                    modifier = Modifier
                        .height(20.dp)
                )
            }
            BottomBar()
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
    Home(50.0f, {}, {}, 100.0f)
}
