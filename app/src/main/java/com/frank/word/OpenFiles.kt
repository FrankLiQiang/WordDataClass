package com.frank.word

import androidx.documentfile.provider.DocumentFile

var files: ArrayList<DocumentFile> = arrayListOf()

var fileBeginIndex = 0
var fileEndIndex = 0
var fileIndex = 0
var isPlayFolder: Boolean = false

fun sortFiles() {
    if (files.isEmpty()) return

    files.sortBy { it.name }
    if (sortType == 2) {
        shuffleFiles()
    }
}

private fun shuffleFiles() {
    val length = fileEndIndex + 1
    if (fileBeginIndex == 0) {
        for (i in 0 until length) {
            val iRandNum = (Math.random() * length).toInt()
            val temp = files[iRandNum]
            files[iRandNum] = files[i]
            files[i] = temp
        }
    } else {
        for (i in length - 1 downTo fileBeginIndex) {
            var iRandNum = (Math.random() * (length - fileBeginIndex)).toInt()
            iRandNum += fileBeginIndex
            val temp = files[iRandNum]
            files[iRandNum] = files[i]
            files[i] = temp
        }
    }
}

fun showPrevLesson() {
    if (fileIndex > fileBeginIndex) {
        fileIndex--
    } else {
        fileIndex = fileEndIndex
    }
    isNextLesson = false
    playMp3(files[fileIndex].uri, -1)
    isNextLesson = true
}

fun showNextLesson() {
    if (fileIndex < fileEndIndex) {
        fileIndex++
    } else {
        fileIndex = fileBeginIndex
    }
    isNextLesson = true
    playMp3(files[fileIndex].uri, 1)
}
