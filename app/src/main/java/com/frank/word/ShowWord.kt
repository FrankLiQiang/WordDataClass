package com.frank.word

import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.frank.word.ui.moveToCurrentWord
import kotlin.math.abs

const val SHOW_RANGE_NORMAL = 0
const val SHOW_FAVORITE = 1
const val SHOW_DEL = 2

var iShowDel by mutableStateOf(true)
var iShowNormal by mutableStateOf(true)
var iShowFavorite by mutableStateOf(true)

var iShowForeign by mutableStateOf(true)
var iShowPronunciation by mutableStateOf(true)
var iShowMeaning by mutableStateOf(true)

var iEnd = 0
var isNextLesson = false
var CurrentClassStr by mutableStateOf("全部")

fun showWord() {
    if (showCurrentWord()) {
        inputText = ""
        showSeekTo()
    }
}

fun showSeekTo() {
    if (wordIndex < playOrder.size && playOrder[wordIndex] < wordList.size) {
        iStart = wordList[playOrder[wordIndex]].startPlayTime
        if (playOrder[wordIndex] + 1 < wordList.size) {
            iEnd = if (isForeignOnly && wordList[playOrder[wordIndex]].middlePlayTime > 0) {
                wordList[playOrder[wordIndex]].middlePlayTime
            } else {
                wordList[playOrder[wordIndex] + 1].startPlayTime
            }
        }
        if (!isFirstTime && abs(iStart - mediaPlayer.currentPosition) > 1000) {
            mediaPlayer.seekTo(iStart)
        }
    }
}

fun showFirstWord() {
    if (sortType == 1 || sortType == 2) {
        sortWords()
    }
    showWord()
}

fun showCurrentWord(): Boolean {
    if (wordIndex >= wordList.size || wordIndex >= playOrder.size) {
        return false
    }
    if (isShowList && wordIndex >= 0 && !isFirstTime) {
        moveToCurrentWord(wordIndex)
    }

    var str = ""
    if (iShowForeign) {
        str += wordList[playOrder[wordIndex]].foreign
    }
    if (iShowPronunciation) {
        if (iShowForeign) {
            if (wordList[playOrder[wordIndex]].foreign != wordList[playOrder[wordIndex]].pronunciation) {
                str += "\n" + wordList[playOrder[wordIndex]].pronunciation
            }
        } else {
            str += wordList[playOrder[wordIndex]].pronunciation
        }
    }
    if (iShowMeaning) {
        if (iShowPronunciation || iShowForeign) {
            str += "\n"
        }
        str += wordList[playOrder[wordIndex]].native
    }
    currentShowWord = str
    currentSentence1 = wordList[playOrder[wordIndex]].sentence1
    currentSentence2 = wordList[playOrder[wordIndex]].sentence2

    isFAVORITE = wordList[playOrder[wordIndex]].rememberDepth == SHOW_FAVORITE
    isDEL = wordList[playOrder[wordIndex]].rememberDepth == SHOW_DEL
    isNORMAL = !isFAVORITE && !isFAVORITE

    showTitle()
    showWordClass()
    return true
}

fun showTitle() {
    if (isAdjust) {
        titleString = "$fileName(${wordIndex + 1}/${wordList.size})"
        musicStep = (wordIndex + 1).toFloat() / wordList.size.toFloat()
    } else {
        titleString = "$fileName($wordShowIndex/$wordNum)"
        musicStep = wordShowIndex.toFloat() / wordNum.toFloat()
    }
}

var beginIndex = -1
fun showPrev() {
    if (!isPlayFolder) {
        if (beginIndex == -1) {
            beginIndex = wordIndex
        } else {
            if (beginIndex == wordIndex) {
                Toast.makeText(mainActivity, "没有相应单词", Toast.LENGTH_LONG).show()
                return
            }
        }
    }
    val offset = if (isAdjust) 1 else 2
    if (wordIndex > 0) {
        wordIndex--
        if (isRightIndex(wordIndex)) {
            wordShowIndex--
            showWord()
            beginIndex = -1
        } else {
            showPrev()
        }
    } else {
        if (isPlayFolder) {
            showPrevLesson()
        } else {
            wordIndex = wordList.size - offset + 1
            sortWords()
            showPrev()
        }
    }
}

fun showNext() {
    if (!isPlayFolder) {
        if (beginIndex == -1) {
            beginIndex = wordIndex
        } else {
            if (beginIndex == wordIndex) {
                Toast.makeText(mainActivity, "没有相应单词", Toast.LENGTH_LONG).show()
                return
            }
        }
    }
    val offset = if (isAdjust) 1 else 2
    if (wordIndex < wordList.size - offset) {
        wordIndex++
        if (isRightIndex(wordIndex)) {
            wordShowIndex++
            showWord()
            beginIndex = -1
        } else {
            showNext()
        }
    } else {
        if (isPlayFolder) {
            showNextLesson()
        } else {
            wordIndex = -1
            sortWords()
            showNext()
        }
    }
}

fun isRightIndex(index: Int): Boolean {
    if (!isAdjust && wordList[playOrder[index]].rememberDepth == 3) {
        return false
    }
    if (CurrentClassStr != "全部") {
        if (!wordList[playOrder[index]].wordClass.contains(CurrentClassStr)) {
            return false
        }
    }
    if (iShowNormal && wordList[playOrder[index]].rememberDepth == SHOW_RANGE_NORMAL) {
        return true
    }
    if (iShowFavorite && wordList[playOrder[index]].rememberDepth == SHOW_FAVORITE) {
        return true
    }
    if (iShowDel && wordList[playOrder[index]].rememberDepth == SHOW_DEL) {
        return true
    }
    return false
}