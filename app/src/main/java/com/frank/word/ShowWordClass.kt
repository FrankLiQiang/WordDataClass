package com.frank.word

import android.widget.Toast

val wClass = arrayOf(
    "名词",
    "代词",
    "数词",
    "动1",
    "动2",
    "动3",
    "形1",
    "形2",
    "连体词",
    "副词",
    "接续词",
    "叹词",
    "助动词",
    "助词",
    "专有名词",
    "动词",
    ""
)

fun showWordClass() {
    if (!isNumeric(wordList[playOrder[wordIndex]].wordClass)) {
        currentWordClass = wordList[playOrder[wordIndex]].wordClass
        currentWordClass += wordList[playOrder[wordIndex]].tone
        return
    }
    val classId0 = wordList[playOrder[wordIndex]].wordClass.toInt()
    wordClassColor = classId0
    val classId = classId0 % 20
    currentWordClass = when (classId0) {
        in 43..45 -> {
            "自${wClass[classId]}"
        }

        in 23..25 -> {
            "他${wClass[classId]}"
        }

        else -> {
            wClass[classId]
        }
    }
    menuWordClass[classId]!!.isChecked = true
}

fun showClassPrev() {
    val offset = if (isAdjust) 1 else 2
    if (wordIndex > 0) {
        var isFound = false
        for (i in wordIndex - 1 downTo 0) {
            if (isNumeric(wordList[playOrder[i]].wordClass)) {
                val wClass = wordList[playOrder[i]].wordClass.toInt()
                if (CurrentWordClass == 16) {
                    if (wClass in 23..25) {
                        wordIndex = i
                        isFound = true
                        break
                    }
                } else if (CurrentWordClass == 17) {
                    if (wClass in 43..45) {
                        wordIndex = i
                        isFound = true
                        break
                    }
                } else {
                    if (wClass % 20 == CurrentWordClass) {
                        wordIndex = i
                        isFound = true
                        break
                    }
                }
            } else {
                if (wordList[playOrder[i]].wordClass.contains(CurrentClassStr)) {
                    wordIndex = i
                    isFound = true
                    break
                }
            }
        }
        if (!isFound) {
            if (isPlayFolder) {
                showPrevLesson()
            } else {
                for (i in wordList.size - offset downTo wordIndex + 1) {
                    if (isNumeric(wordList[playOrder[i]].wordClass)) {
                        val wClass = wordList[playOrder[i]].wordClass.toInt()
                        if (CurrentWordClass == 16) {
                            if (wClass in 23..25) {
                                wordIndex = i
                                break
                            }
                        } else if (CurrentWordClass == 17) {
                            if (wClass in 43..45) {
                                wordIndex = i
                                break
                            }
                        } else {
                            if (wClass % 20 == CurrentWordClass) {
                                wordIndex = i
                                break
                            }
                        }
                    } else {
                        if (wordList[playOrder[i]].wordClass.contains(CurrentClassStr)) {
                            wordIndex = i
                            break
                        }
                    }
                }
            }
        }
        showWord()
    } else {
        if (isPlayFolder) {
            showPrevLesson()
        } else {
            wordIndex = wordList.size - offset + 1
            showClassPrev()
        }
    }
}

fun showClassNext() {
    val offset = if (isAdjust) 1 else 2
    if (wordIndex < wordList.size - offset) {
        var isFound = false
        for (i in wordIndex + 1..wordList.size - offset) {
            if (isNumeric(wordList[playOrder[i]].wordClass)) {
                val wClass = wordList[playOrder[i]].wordClass.toInt()
                if (CurrentWordClass == 16) {
                    if (wClass in 23..25) {
                        wordIndex = i
                        isFound = true
                        break
                    }
                } else if (CurrentWordClass == 17) {
                    if (wClass in 43..45) {
                        wordIndex = i
                        isFound = true
                        break
                    }
                } else {
                    if (wClass % 20 == CurrentWordClass) {
                        wordIndex = i
                        isFound = true
                        break
                    }
                }
            } else {
                if (wordList[playOrder[i]].wordClass.contains(CurrentClassStr)) {
                    wordIndex = i
                    isFound = true
                    break
                }
            }
        }
        if (!isFound) {
            if (isPlayFolder) {
                showNextLesson()
                return
            } else {
                for (i in 0 until wordIndex) {
                    if (isNumeric(wordList[playOrder[i]].wordClass)) {
                        val wClass = wordList[playOrder[i]].wordClass.toInt()
                        if (CurrentWordClass == 16) {
                            if (wClass in 23..25) {
                                wordIndex = i
                                break
                            }
                        } else if (CurrentWordClass == 17) {
                            if (wClass in 43..45) {
                                wordIndex = i
                                break
                            }
                        } else {
                            if (wClass % 20 == CurrentWordClass) {
                                wordIndex = i
                                break
                            }
                        }
                    } else {
                        if (wordList[playOrder[i]].wordClass.contains(CurrentClassStr)) {
                            wordIndex = i
                            break
                        }
                    }
                }
            }
        }
        showWord()
    } else {
        if (isPlayFolder) {
            showNextLesson()
        } else {
            wordIndex = -1
            sortWords()
            showClassNext()
        }
    }
}

fun showClassVt(): Boolean {
    CurrentWordClass = 16
    if (iShowRange == SHOW_RANGE_CLASS) {
        showClassNext()
    } else {
        val cls = wordList[playOrder[wordIndex]].wordClass.toInt() % 20
        if (cls < 3 || cls > 5) {
            Toast.makeText(mainActivity, "请选择动词", Toast.LENGTH_LONG).show()
            return false
        }
        wordList[playOrder[wordIndex]].wordClass = "" + (20 + cls)
        saveFile("")
        showCurrentWord()
    }
    return true
}

fun showClassVi(): Boolean {
    CurrentWordClass = 17
    if (iShowRange == SHOW_RANGE_CLASS) {
        showClassNext()
    } else {
        val cls = wordList[playOrder[wordIndex]].wordClass.toInt() % 20
        if (cls < 3 || cls > 5) {
            Toast.makeText(mainActivity, "请选择动词", Toast.LENGTH_LONG).show()
            return false
        }
        wordList[playOrder[wordIndex]].wordClass = "" + (40 + cls)
        saveFile("")
        showCurrentWord()
    }
    return true
}
