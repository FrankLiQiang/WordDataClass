package com.frank.word

import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.frank.word.ui.moveToCurrentWord
import kotlin.math.abs

const val SHOW_ALL = 0
const val SHOW_FOREIGN = 1
const val SHOW_PRONUNCIATION = 2
const val SHOW_NATIVE = 3
const val SHOW_NONE = 4

const val SHOW_RANGE_NORMAL = 0
const val SHOW_FAVORITE = 1
const val SHOW_DEL = 2
const val SHOW_CHOSEN = 3
const val SHOW_RANGE_ALL = 4
const val SHOW_RANGE_CLASS = 5

var showWordType = SHOW_ALL
var iShowRange = SHOW_RANGE_ALL
var removed_num = 0
var normal_num = 0
var favorite_num = 0
var chosen_num = 0
var iEnd = 0
var isNextLesson = false
var all_num = 0  //by mutableStateOf(0)
var CurrentClassStr by mutableStateOf("")

fun showWord() {
    if (showCurrentWord()) {
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
    val offset = if (isAdjust) 0 else 1
    removed_num = 0
    normal_num = 0
    favorite_num = 0
    for (i in 0 until wordList.size - offset) {
        if (wordList[playOrder[i]].rememberDepth == SHOW_RANGE_NORMAL) {
            normal_num++
        } else if (wordList[playOrder[i]].rememberDepth == SHOW_FAVORITE) {
            favorite_num++
        } else if (wordList[playOrder[i]].rememberDepth == SHOW_DEL) {
            removed_num++
        }
    }
    chosen_num = normal_num + favorite_num
    all_num = chosen_num + removed_num
    showWord()
}

fun showCurrentWord(): Boolean {
    if (wordIndex >= wordList.size || wordIndex >= playOrder.size) {
        return false
    }
    if (isShowList && wordIndex >= 0) {
        moveToCurrentWord(wordIndex)
    }

    var str = ""
    if (showWordType == SHOW_ALL) {
        str = if (wordList[playOrder[wordIndex]].foreign
            == wordList[playOrder[wordIndex]].pronunciation
        ) {
            """
     ${wordList[playOrder[wordIndex]].foreign}
     ${wordList[playOrder[wordIndex]].native}
     """.trimIndent()
        } else {
            """
     ${wordList[playOrder[wordIndex]].foreign}
     ${wordList[playOrder[wordIndex]].pronunciation}
     ${wordList[playOrder[wordIndex]].native}
     """.trimIndent()
        }
    } else if (showWordType == SHOW_FOREIGN) {
        str = wordList[playOrder[wordIndex]].foreign
    } else if (showWordType == SHOW_PRONUNCIATION) {
        str = wordList[playOrder[wordIndex]].pronunciation
    } else if (showWordType == SHOW_NATIVE) {
        str = wordList[playOrder[wordIndex]].native
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
        titleString ="$fileName(${wordIndex + 1}/${wordList.size})"
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
    when (iShowRange) {
        SHOW_RANGE_CLASS -> {
//            if (isNumeric(wordList[playOrder[index]].wordClass)) {
//                val wClass = wordList[playOrder[index]].wordClass.toInt()
//                if (CurrentWordClass == 16) {
//                    if (wClass in 23..25) {
//                        return true
//                    }
//                } else if (CurrentWordClass == 17) {
//                    if (wClass in 43..45) {
//                        return true
//                    }
//                } else {
//                    if (wClass % 20 == CurrentWordClass) {
//                        return true
//                    }
//                }
//            } else {
            if (wordList[playOrder[index]].wordClass.contains(CurrentClassStr)) {
                return true
            }
//        }
        }

        SHOW_RANGE_ALL -> {
            return !(isAdjust || wordList[playOrder[index]].rememberDepth == 3)
        }

        SHOW_CHOSEN -> {
            if (wordList[playOrder[index]].rememberDepth == SHOW_RANGE_NORMAL
                || wordList[playOrder[index]].rememberDepth == SHOW_FAVORITE
            ) {
                return true
            }
        }

        else -> {
            if (wordList[playOrder[index]].rememberDepth == iShowRange) {
                return true
            }
        }
    }
    return false
}