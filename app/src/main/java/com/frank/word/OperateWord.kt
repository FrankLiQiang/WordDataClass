package com.frank.word

import com.frank.word.ui.isDEL
import com.frank.word.ui.isFAVORITE
import com.frank.word.ui.isNORMAL

fun addFavoriteWord() {
    if (wordList[playOrder[wordIndex]].rememberDepth == SHOW_FAVORITE) {
        return
    }
    if (wordList[playOrder[wordIndex]].rememberDepth == SHOW_DEL) {
        removed_num--
    }
    if (wordList[playOrder[wordIndex]].rememberDepth == SHOW_RANGE_NORMAL) {
        normal_num--
    }
    favorite_num++

    chosen_num = normal_num + favorite_num
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
    if (wordList[playOrder[wordIndex]].rememberDepth == SHOW_FAVORITE) {
        favorite_num--
    }
    if (wordList[playOrder[wordIndex]].rememberDepth == SHOW_DEL) {
        removed_num--
    }
    normal_num++
    chosen_num = normal_num + favorite_num
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
    if (wordList[playOrder[wordIndex]].rememberDepth == SHOW_FAVORITE) {
        favorite_num--
    }
    if (wordList[playOrder[wordIndex]].rememberDepth == SHOW_RANGE_NORMAL) {
        normal_num--
    }
    removed_num++

    chosen_num = normal_num + favorite_num
    wordList[playOrder[wordIndex]].rememberDepth = SHOW_DEL
    saveFile("")
    if (iShowRange == SHOW_RANGE_ALL) {
        isFAVORITE = false
        isDEL = true
        isNORMAL = false
    } else {
        showNext()
    }
}
