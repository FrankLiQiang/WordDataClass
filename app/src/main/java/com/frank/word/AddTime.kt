package com.frank.word

fun addTimeInit() {
    isToAddTime = true
    playOrder.clear()
    for (item: Int in 0 until wordList.size) {
        playOrder.add(item)
    }
    sortType = 0
    musicStep = 0.0f
    iShowForeign = true
    iShowPronunciation = true
    iShowMeaning = true

    iShowForeign = true
    iShowPronunciation = true
    iShowMeaning = true

    sortWords()
    titleString = "$fileName(/${wordList.size - 1})"
    iStart = 0
}

fun addMiddleTime() {
    wordList[wordIndex].middlePlayTime = mMediaPlayer.currentPosition
    if (wordIndex == wordList.size - 2) {
        saveFile("")
    }
    showNext()
}

fun addTime() {
    if (wordIndex < wordList.size) {
        iStart = mMediaPlayer.currentPosition
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
        if (playOrder[wordIndex] > 0) {
            if (iStart > wordList[playOrder[wordIndex] - 1].startPlayTime + 1000) {
                iStart -= 100
                wordList[playOrder[wordIndex]].startPlayTime = iStart
            }
        } else {
            if (iStart > 100) {
                iStart -= 100
                wordList[playOrder[wordIndex]].startPlayTime = iStart
            }
        }
    } else {
        if (playOrder[wordIndex] < wordList.size - 1) {
            if (iStart < wordList[playOrder[wordIndex] + 1].startPlayTime - 1000) {
                iStart += 100
                wordList[playOrder[wordIndex]].startPlayTime = iStart
            }
        } else {
            if (iStart < mMediaPlayer.duration - 200) {
                iStart += 100
                wordList[playOrder[wordIndex]].startPlayTime = iStart
            }
        }
    }
}
