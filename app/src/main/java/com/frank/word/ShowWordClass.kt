package com.frank.word

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
}
