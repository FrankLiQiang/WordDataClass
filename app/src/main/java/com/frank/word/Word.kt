package com.frank.word

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

var wordNum = 0
var wordList: ArrayList<Word> = arrayListOf()
var playOrder: ArrayList<Int> = arrayListOf()

var currentShowWord by mutableStateOf("")
var currentWordClass by mutableStateOf("")
var currentSentence1 by mutableStateOf("")
var currentSentence2 by mutableStateOf("")
var inputText by mutableStateOf("")
var isShowEditText by mutableStateOf(false)
var isShowDict by mutableStateOf(false)

var isNORMAL by mutableStateOf(false)
var isFAVORITE by mutableStateOf(false)
var isDEL by mutableStateOf(false)
var isToAddTime by mutableStateOf(false)
var isMiddleTime by mutableStateOf(false)
var isShowList by mutableStateOf(false)
var isForeignOnly by mutableStateOf(false)
var isAdjust by mutableStateOf(false)
var isShowChooseLessonDialog by mutableStateOf(false)
var isChooseSingleLessonDialog by mutableStateOf(false)
var isShowCixingDialog by mutableStateOf(false)
var isShowSettingDialog by mutableStateOf(true)
var isToSaveInfo by mutableStateOf(true)
var isShowPauseTimeDialog by mutableStateOf(false)
var wordClassColor by mutableStateOf(0)
var isToDraw by mutableStateOf(0)
var pauseTime by mutableStateOf(0L)

data class Word(
    var wordClass: String = "0",
    var tone: String = "",
    var foreign: String = "",
    var pronunciation: String = "",
    var native: String = "",
    var sentence1: String = "",
    var sentence2: String = "",
    var sentence3: String = "",
    var startPlayTime: Int = 0,
    var middlePlayTime: Int = 0,
//                var lessonIndex: Int,
    var rememberDepth: Int = 0,
//                var wordIndex: Int,
    var isItemChosen: Boolean = false,
)
