package com.frank.word

val wClass = arrayOf(
    "名",
    "代",
    "数",
    "動1",
    "動2",
    "動3",
    "イ形",
    "ナ形",
    "連体",
    "副",
    "接续词",
    "叹",
    "助动词",
    "助词",
    "专",
    "短",
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
