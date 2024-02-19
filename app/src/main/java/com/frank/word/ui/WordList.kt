package com.frank.word.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frank.word.R
import com.frank.word.Word
import com.frank.word.iShowForeign
import com.frank.word.iShowMeaning
import com.frank.word.iShowPronunciation
import com.frank.word.isRightIndex
import com.frank.word.isToDraw
import com.frank.word.myFontSize
import com.frank.word.showWord
import com.frank.word.wordIndex
import com.frank.word.wordList
import com.frank.word.wordShowIndex
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordList(modifier: Modifier) {

    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    LazyColumn(
        state = scrollState, modifier = modifier
        //                    .background(Color.White)
    ) {

        if (isToDraw < -1) return@LazyColumn

        fun getShowItem(isTitle: Boolean, word: Word): String {
            if (isTitle) {
                if (iShowForeign) {
                    return word.foreign
                } else if (iShowPronunciation) {
                    val c = word.pronunciation.toCharArray()[0]
                    if (c in 'a'..'z' || c in 'A'..'Z') {
                        return word.foreign
                    }
                    return word.pronunciation
                } else if (iShowMeaning) {
                    return word.native
                }
            } else {
                if (iShowForeign) {
                    return word.pronunciation + "\n" + word.native
                } else if (iShowPronunciation) {
                    val c = word.pronunciation.toCharArray()[0]
                    if (c in 'a'..'z' || c in 'A'..'Z') {
                        return word.pronunciation + "\n" + word.native
                    }
                    return word.foreign + "\n" + word.native
                } else if (iShowMeaning) {
                    return word.foreign + "\n" + word.pronunciation
                }
            }

            return word.foreign
        }

        moveToCurrentWord = { row ->
            coroutineScope.launch {
                if (lastClickItem != null && wordList[row] != lastClickItem) {
                    lastClickItem!!.isItemChosen = false
                }
                wordList[row].isItemChosen = true
                lastClickItem = wordList[row]
                //scrollState.animateScrollToItem(row)
                try {
                    scrollState.scrollToItem(if (row > 2) row - 2 else 0)
                } catch (_: Exception) {
                }
                isToDraw = 1 - isToDraw
            }
        }

        itemsIndexed(wordList.subList(0, wordList.size - 1)) { index, menuItem ->

            if (isRightIndex(index)) {
                NavigationDrawerItem(
                    modifier = Modifier.height(60.dp),
//                            colors = NavigationDrawerItemDefaults.colors(
//                                selectedContainerColor = Color.White,
//                                unselectedContainerColor = Color.White,
//                            ),
                    shape = MaterialTheme.shapes.small,
                    selected = true,
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.baseline_brightness_1_24),
                            contentDescription = stringResource(id = R.string.app_name),
                            tint = if (menuItem.isItemChosen) Color.Red else (if (isSystemInDarkTheme()) Color.White else Color.Black)
                        )
                    },
                    label = {
                        Row {
                            Text(
                                text = decimalFormat.format(index + 1),
                                fontSize = 14.sp,
//                                        color = Color.Blue,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.labelMedium,
                            )
                            Text(
                                text = getShowItem(true, menuItem),
                                fontSize = myFontSize.sp,
//                                        color = Color.Black,
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
                            wordShowIndex = 0
                            for (i in 0..wordList.size) {
                                if (isRightIndex(i)) {
                                    wordShowIndex++
                                }
                                if (i >= index) {
                                    break
                                }
                            }
                            showWord()
                        }
                        isItem = true
                        isToDraw = 1 - isToDraw
                    },
                )
                if (menuItem.isItemChosen) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
//                                    .background(Color.LightGray)
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.DarkGray,
                                            Color.LightGray,
                                        ), startY = 0f, endY = 30f, tileMode = TileMode.Clamp
                                    )
                                )
                                .padding(5.dp)
                                .padding(start = 50.dp)
                                .clickable {
                                    menuItem.isItemChosen = false
                                    isItem = false
                                    isToDraw = 1 - isToDraw
                                },
                            text = getShowItem(false, menuItem),
                            fontSize = myFontSize.sp,
//                                    color = Color.Black,
                            lineHeight = (myFontSize + 2).sp,
                            maxLines = 10,
//                                    style = MaterialTheme.typography.labelMedium,
                        )
                        Text(
                            text = "　" + menuItem.wordClass + menuItem.tone,
                            fontSize = 20.sp,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                Divider(color = Color.LightGray)
            }
        }
    }
}
