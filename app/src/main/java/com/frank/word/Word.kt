package com.frank.word

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

var wordList: ArrayList<Word> = arrayListOf()
var playOrder: ArrayList<Int> = arrayListOf()

var currentShowWord by mutableStateOf("")
var currentWordClass by mutableStateOf("")
var currentSentence1 by mutableStateOf("")
var currentSentence2 by mutableStateOf("")
var inputText by mutableStateOf("")
var isShowEditText by mutableStateOf(false)

var isNORMAL by mutableStateOf(false)
var isFAVORITE by mutableStateOf(false)
var isDEL by mutableStateOf(false)
var isToAddTime by mutableStateOf(false)
var isAdjust by mutableStateOf(false)
var isShowDialog by mutableStateOf(false)
var isShowPopupMenu by mutableStateOf(false)
var wordClassColor by mutableStateOf(0)

data class Word(//var playTime: String = "0",
                var wordClass: String = "0",
                var foreign: String = "",
                var pronunciation: String = "",
                var native: String = "",
                var sentence1: String = "",
                var sentence2: String = "",
                var sentence3: String = "",
                var startPlayTime: Int = 0,
//                var lessonIndex: Int,
                var rememberDepth: Int = 0,
//                var wordIndex: Int,
                var isWord: Boolean = true,
                var isOver: Boolean = false,
)
