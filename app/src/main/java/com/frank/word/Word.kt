package com.frank.word

var wordList: ArrayList<Word> = arrayListOf()
var playOrder: ArrayList<Int> = arrayListOf()

data class Word(//var playTime: String = "0",
                var wordClass: String = "",
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
