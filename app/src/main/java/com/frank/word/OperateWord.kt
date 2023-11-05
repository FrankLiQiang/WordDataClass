package com.frank.word

fun addFavoriteWord() {
    if (wordList[playOrder[wordIndex]].rememberDepth == SHOW_FAVORITE) {
        return
    }
    wordList[playOrder[wordIndex]].rememberDepth = SHOW_FAVORITE
    isFAVORITE = true
    isDEL = false
    isNORMAL = false
    saveFile("")
}

fun setWordNormal() {
    if (wordList[playOrder[wordIndex]].rememberDepth == SHOW_RANGE_NORMAL) {
        return
    }
    wordList[playOrder[wordIndex]].rememberDepth = SHOW_RANGE_NORMAL

    isFAVORITE = false
    isDEL = false
    isNORMAL = true
    saveFile("")
}

fun delWord() {
    if (wordList[playOrder[wordIndex]].rememberDepth == SHOW_DEL) {
        return
    }
    wordList[playOrder[wordIndex]].rememberDepth = SHOW_DEL
    saveFile("")
    if (iShowDel) {
        isFAVORITE = false
        isDEL = true
        isNORMAL = false
    } else {
        showNext()
    }
}
