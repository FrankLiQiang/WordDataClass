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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frank.word.ChooseLessonMenu
import com.frank.word.R
import com.frank.word.Word
import com.frank.word.addMiddleTime
import com.frank.word.addTime
import com.frank.word.currentSentence1
import com.frank.word.currentSentence2
import com.frank.word.currentShowWord
import com.frank.word.currentWordClass
import com.frank.word.isDEL
import com.frank.word.isEditFile
import com.frank.word.isFAVORITE
import com.frank.word.isMiddleTime
import com.frank.word.isPlay
import com.frank.word.isShowEditText
import com.frank.word.isShowList
import com.frank.word.isToAddTime
import com.frank.word.isToDraw
import com.frank.word.musicStep
import com.frank.word.myFontSize
import com.frank.word.showNext
import com.frank.word.showPrev
import com.frank.word.showWord
import com.frank.word.wordClassColor
import com.frank.word.wordIndex
import com.frank.word.wordList
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
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
                    onValueChangeFinished = {},
                    valueRange = 15f..maxProgress,
                    modifier = Modifier
                        .height(20.dp)
                )
            }
            Row(modifier = Modifier.height(padding.dp)) {}
        }
    }

    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    ChooseLessonMenu()
    Column(Modifier.background(Color.Black)) {
        if (isToDraw < -1) return
        Row(
            Modifier
                .fillMaxWidth()
                .height(30.dp)      //小米 Redmi Note11 Pro
//                .height(50.dp)        //Google Pixel 6A
        ) {}
        SetLessonRangDialog()
        SetPauseTimeDialog()
        if (!isShowList) {
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
            if (currentSentence1.isNotEmpty()) {
                Text(
                    text = AnnotatedString(currentSentence1),
                    fontSize = myFontSize.sp,
                    color = if (isPlay) Color.White else Color.Cyan,
                    textAlign = TextAlign.Start,
                    lineHeight = (myFontSize * 1.2f).sp,
                    modifier = Modifier
                        .fillMaxWidth()
                    //.padding(5.dp)
                )
            }
            if (currentSentence2.isNotEmpty()) {
                Text(
                    text = AnnotatedString(currentSentence2),
                    fontSize = myFontSize.sp,
                    lineHeight = (myFontSize * 1.2f).sp,
                    color = if (isPlay) Color.White else Color.Cyan,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
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
                Row(
                    Modifier
                        .fillMaxWidth()
                        .weight(1.0f)
                        .pointerInteropFilter {
                            when (it.action) {
                                MotionEvent.ACTION_DOWN -> {
                                    StartX = it.x
                                }

                                MotionEvent.ACTION_UP -> {
                                    if (it.x > StartX) {
                                        showNext()
                                    } else {
                                        showPrev()
                                    }
                                }
                            }
                            true
                        }
                        .padding(5.dp)
                ) {}
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
        } else {
            LinearProgressIndicator(
                progress = musicStep,
                Modifier
                    .fillMaxWidth()
            )
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .weight(1f)
                .background(Color.White)
            ) {

                if (isToDraw < -1) return@LazyColumn

                moveToCurrentWord = { row ->
                    coroutineScope.launch {
                        if (lastClickItem != null && wordList[row] != lastClickItem) {
                            lastClickItem!!.isItemChosen = false
                        }
                        wordList[row].isItemChosen = true
                        lastClickItem = wordList[row]
                        //scrollState.animateScrollToItem(row)
                        scrollState.scrollToItem(row)
                        isToDraw = 1 - isToDraw
                    }
                }

                itemsIndexed(wordList.subList(0, wordList.size - 1)) { index, menuItem ->

                    NavigationDrawerItem(
                        modifier = Modifier.height(60.dp),
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = Color.White,
                            unselectedContainerColor = Color.White,
                        ),
                        shape = MaterialTheme.shapes.small,
                        selected = true,
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.baseline_brightness_1_24),
                                contentDescription = stringResource(id = R.string.app_name),
                                tint = if (menuItem.isItemChosen) Color.Red else Color.DarkGray,
                            )
                        },
                        label = {
                            Row {
                                Text(
                                    text = decimalFormat.format(index + 1),
                                    fontSize = 14.sp,
                                    color = Color.Blue,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.labelMedium,
                                )
                                Text(
                                    text = menuItem.foreign,
                                    fontSize = myFontSize.sp,
                                    color = Color.Black,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.labelMedium,
                                )
                                Text(
                                    text = menuItem.wordClass + menuItem.tone,
                                    fontSize = 20.sp,
                                    color = Color.Blue,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.labelMedium,
                                )
                            }
                        },
                        onClick = {
                            if (lastClickItem != null && menuItem != lastClickItem) {
                                lastClickItem!!.isItemChosen = false
                            }
                            menuItem.isItemChosen = !menuItem.isItemChosen
                            if (menuItem.isItemChosen) {
                                lastClickItem = menuItem
                                wordIndex = index
                                showWord()
                            }
                            isItem = true
                            isToDraw = 1 - isToDraw
                        },
                    )
                    if (menuItem.isItemChosen) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.LightGray)
                        )
                        {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color.DarkGray,
                                                Color.LightGray,
                                            ),
                                            startY = 0f,
                                            endY = 30f,
                                            tileMode = TileMode.Clamp
                                        )
                                    )
                                    .padding(5.dp)
                                    .padding(start = 50.dp)
                                    .clickable {
                                        menuItem.isItemChosen = false
                                        isItem = false
                                        isToDraw = 1 - isToDraw
                                    },
                                text = menuItem.pronunciation + "\n" + menuItem.native,
                                fontSize = myFontSize.sp,
                                color = Color.Black,
                                lineHeight = (myFontSize + 2).sp,
                                maxLines = 10,
                                style = MaterialTheme.typography.labelMedium,
                            )
                        }
                    }
                    Divider(color = Color.LightGray)
                }
            }
            showSlider()
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
