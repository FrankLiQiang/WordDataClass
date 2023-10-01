package com.frank.word

import android.widget.Toast
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

var lrcFile: File? = null
var isLRC_Time_OK: Boolean = false
var isLRC_Format_OK: Boolean = false
var iStart: Int = 0
var wordIndex: Int = 0
var loopIndex: Int = 0
var loopNumber: Int = 1
var sortType: Int = 0

fun readTextFile(index: Int) {
    var start = pathAndName.lastIndexOf("/")
    var end = pathAndName.lastIndexOf(".")
    if (start != -1 && end != -1) {
        fileName = pathAndName.substring(start + 1, end)
        val tmp = pathAndName.substring(0, start)
        end = start
        start = tmp.lastIndexOf("/")
        folderName = pathAndName.substring(start + 1, end)
    }
    val pathName = "$lrcPath/$folderName/$fileName.txt"
    try {
        isPlay = true
        isLRC_Format_OK = false
        isLRC_Time_OK = false
        inputText = ""
        iStart = 0
        wordIndex = index
        loopIndex = 0
//        isAdjust = false
//        isToAddTime = false
        isEditFile = false
        wordList.clear()
        playOrder.clear()
        lrcFile = File(pathName)
        if (lrcFile!!.exists()) {
            isLRC_Time_OK = true
            readTxtFileIntoStringArrList(lrcFile!!, false)
            sortWords()
            if (isLRC_Time_OK) {
                if (isLRC_Format_OK) {
                    showWordsNormal()
                } else {
                    val str: String = readRawTxtFile(lrcFile!!)
                    editWords(str)
                }
            } else {
                readTxtFileIntoStringArrList(lrcFile!!, true)
                if (isLRC_Format_OK) {
                    loopNumber = 0
                    addTimeInit()
                } else {
                    val str: String = readRawTxtFile(lrcFile!!)
                    editWords(str)
                }
            }
        } else {
            editWords("")
        }
    } catch (_: java.lang.Exception) {
    }
}

fun readTxtFileIntoStringArrList(file: File?, isMakeLRC: Boolean) {
    var read: InputStreamReader? = null
    var bufferedReader: BufferedReader? = null
    try {
        read = InputStreamReader(FileInputStream(file))
        bufferedReader = BufferedReader(read)
        var lineTxt: String
        var strArray: Array<String>
        isLRC_Time_OK = true
        isLRC_Format_OK = true
        while (bufferedReader.readLine()
                .also { lineTxt = it ?: "" } != null && !lineTxt.isEmpty()
        ) {
            if (lineTxt.length < 10 || !isNumeric(lineTxt.substring(0, 10))) {
                isLRC_Time_OK = false
            }
            val word = Word()
            if (isMakeLRC) {
                strArray =
                    lineTxt.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            } else {
                if (lineTxt.length < 10 || !isNumeric(lineTxt.substring(0, 10))) {
                    isLRC_Time_OK = false
                    bufferedReader.close()
                    read.close()
                    return
                }
                val flag = lineTxt.substring(0, 1)
                val wCls = lineTxt.substring(8, 10)

                word.rememberDepth = flag.toInt()
                word.startPlayTime = lineTxt.substring(1, 8).toInt()
                word.wordClass = wCls
                strArray = lineTxt.substring(10).split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
            }
            if (strArray.size == 1) {
                if (strArray[0] != "完了" && strArray[0] != "结束" && strArray[0] != "3") {
                    isLRC_Format_OK = false
                }
                word.foreign = strArray[0]
                word.pronunciation = strArray[0]
                word.native = strArray[0]
                wordList.add(word)
                continue
            }
            if (strArray.size == 2) {
                word.foreign = strArray[0]
                word.pronunciation = strArray[0]
                word.native = strArray[1]
                wordList.add(word)
                continue
            }
            if (strArray.size < 3 || lineTxt.trim { it <= ' ' }.isEmpty()) {
                isLRC_Format_OK = false
                //error = "2"
                continue
            }
            word.foreign = strArray[0]
            word.pronunciation = strArray[1]
            word.native = strArray[2]
            if (strArray.size >= 5) {
                word.sentence1 = strArray[3]
                word.sentence2 = strArray[4]
            }
            if (strArray.size >= 7) {
                word.middlePlayTime = strArray[6].toInt()
            }
            if (strArray.size >= 8) {
                word.wordClass = strArray[7]
            }
            if (strArray.size >= 9) {
                word.tone = strArray[8]
            }
            wordList.add(word)
        }
        bufferedReader.close()
        read.close()
        return
    } catch (e: Exception) {
        isLRC_Time_OK = false
        isLRC_Format_OK = false
        //error = e.toString()
        try {
            bufferedReader!!.close()
            read!!.close()
        } catch (_: Exception) {
        }
        e.printStackTrace()
    }
}

fun sortWords() {
    if (playOrder.isEmpty()) {
        for (item: Int in 0 until wordList.size) {
            playOrder.add(item)
        }
    }
    if (sortType == 1 || sortType == 2) {
        shuffleWords()
    }
}

fun shuffleWords() {
    val length = playOrder.size
    for (i in 0 until length - 1) {
        val iRandNum = (Math.random() * (length - 1)).toInt()
        val temp = playOrder[iRandNum]
        playOrder[iRandNum] = playOrder[i]
        playOrder[i] = temp
    }
}

fun showWordsNormal() {
    showFirstWord()
    menu_word_class?.isVisible = true
}

fun readRawTxtFile(file: File): String {
    val read: InputStreamReader
    val bufferedReader: BufferedReader
    val stringBuffer = StringBuilder()
    try {
        read = InputStreamReader(FileInputStream(file))
        bufferedReader = BufferedReader(read)
        var lineTxt: String
        while (bufferedReader.readLine().also { lineTxt = it ?: "" } != null) {
            if (isLRC_Time_OK) {
                stringBuffer.append(lineTxt.substring(10))
            } else {
                stringBuffer.append(lineTxt)
            }
            stringBuffer.append("\n")
        }
        bufferedReader.close()
        read.close()
        return stringBuffer.toString()
    } catch (e: java.lang.Exception) {
        e.toString()
    }
    return ""
}

fun editWords(str: String) {
    if (str.isEmpty()) {
        Toast.makeText(mainActivity, "单词文件不存在！", Toast.LENGTH_LONG).show()
    } else {
        Toast.makeText(mainActivity, "单词文件格式错误！", Toast.LENGTH_LONG).show()
    }
    inputText = str
    isShowEditText = true
    isEditFile = true
    iStart = 0
}

fun isNumeric(str: String?): Boolean {
    return try {
        str?.toDouble()
        true
    } catch (e: NumberFormatException) {
        false
    }
}
