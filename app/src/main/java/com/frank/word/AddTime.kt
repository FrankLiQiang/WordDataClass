package com.frank.word

import com.frank.word.ui.isToAddTime
import java.text.DecimalFormat

fun addTimeInit() {
    isToAddTime = true
    playOrder.clear()
    sortType = 0
    musicStep = 0.0f
    showWordType = SHOW_ALL
    sortWords()
    titleString = "$fileName(/${wordList.size - 1})"
    iStart = 0
}

fun addTime() {
    if (wordIndex < wordList.size) {
        val decimalFormat = DecimalFormat("0000000")
        iStart = mediaPlayer.currentPosition
        showCurrentWord()
        wordList[wordIndex].startPlayTime = iStart
        if (wordList[wordIndex].foreign == "3") {
            wordList[wordIndex].rememberDepth = 3
        }
        titleString = "$fileName(${wordIndex + 1}/${wordList.size})"
        musicStep = (wordIndex + 1).toFloat() / wordList.size.toFloat()
        wordIndex++
    } else {
        isLRC_Time_OK = true
        saveFile("")
    }
}

fun changeTime(isToLeft: Boolean) {
    if (isToLeft) {
        if (iStart > 100) {
            iStart -= 100
            wordList[playOrder[wordIndex]].startPlayTime = iStart
        }
    } else {
        if (iStart < mediaPlayer.duration - 200) {
            val decimalFormat = DecimalFormat("0000000")
            iStart += 100
            wordList[playOrder[wordIndex]].startPlayTime = iStart
        }
    }
}
