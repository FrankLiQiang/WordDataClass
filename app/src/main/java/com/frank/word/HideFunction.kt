package com.frank.word

import android.widget.Toast
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

fun hideFunction(str: String) {
    //inputText = str
    if (str.endsWith("\n")) {
        if (str == "\n") {
            inputText = ""
            showNext()
            return
        }
        testWord(str)
    } else if (str.endsWith("＜") || str.endsWith("<")) {
        showPrev()
        inputText = ""
    } else if (str.endsWith(" ")) {
        inputText = ""
    } else if (str.endsWith("?")) {
        showHelp()
    } else if (str.endsWith("+")) {
        addWord(str)
    } else if (str.endsWith("-")) {
        removeWord()
    } else if (str.endsWith("!")) {
        removeWord2()
    } else if (str.endsWith("#")) {
        editWord(str)
    } else if (str.endsWith("&")) {
        searchWord(str)
    } else if (str.endsWith("*")) {
        searchWordAll(str)
    } else if (str.endsWith("@")) {
        adjustWord()
    } else {
        inputText = str
    }
}

fun testWord(str: String) {
    if (str != wordList[playOrder[wordIndex]].foreign
        && str != wordList[playOrder[wordIndex]].pronunciation
        && !wordList[playOrder[wordIndex]].native.contains(str)
    ) {
        inputText =
            """
            ${wordList[playOrder[wordIndex]].foreign}
            ${wordList[playOrder[wordIndex]].pronunciation}
            ${wordList[playOrder[wordIndex]].native}
            """.trimIndent()
        return
    }
}

fun addWord(str: String) {
    if (!isAdjust || sortType != 0 || iShowRange != SHOW_RANGE_ALL || wordIndex == wordList.size - 1) {
        Toast.makeText(mainActivity, "要：调时，顺序，全部播放，才可以。", Toast.LENGTH_LONG).show()
        return
    }
    var time = wordList[wordIndex].startPlayTime + wordList[wordIndex + 1].startPlayTime
    time /= 2
    val word = Word()
    word.rememberDepth = 0
    word.wordClass = "0"
    word.startPlayTime = time

    val strArray: Array<String> =
        str.substring(0, str.length - 1).split("\n".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
    when (strArray.size) {
        3 -> {
            word.foreign = strArray[0]
            word.pronunciation = strArray[1]
            word.native = strArray[2]
            wordList.add(wordIndex + 1, word)
            inputText = ""
            saveFile("")
            showWord()
        }

        2 -> {
            word.foreign = strArray[0]
            word.pronunciation = strArray[0]
            word.native = strArray[1]
            wordList.add(wordIndex + 1, word)
            inputText = ""
            saveFile("")
            showWord()
        }

        else -> {
            Toast.makeText(mainActivity, "格式错误。", Toast.LENGTH_LONG).show()
        }
    }
}

fun removeWord() {
    if (!isAdjust || sortType != 0 || iShowRange != SHOW_RANGE_ALL || wordIndex == wordList.size - 1) {
        Toast.makeText(
            mainActivity,
            "要：调整时刻模式，顺序，全部播放，才可以。",
            Toast.LENGTH_LONG
        ).show()
        return
    }
    wordList.removeAt(wordIndex)
    inputText = ""
    saveFile("")
    showNext()
}

fun removeWord2() {
    if (wordList[playOrder[wordIndex]].rememberDepth == 3) {
        inputText = ""
        return
    }
    if (!isAdjust || sortType != 0 || iShowRange != SHOW_RANGE_ALL || wordIndex == wordList.size - 1) {
        Toast.makeText(
            mainActivity,
            "要：调整时刻模式，顺序，全部播放，才可以。",
            Toast.LENGTH_LONG
        ).show()
        inputText = ""
        return
    }
    if (wordList[playOrder[wordIndex]].rememberDepth == SHOW_FAVORITE) {
        favorite_num--
    }
    if (wordList[playOrder[wordIndex]].rememberDepth == SHOW_DEL) {
        removed_num--
    }
    if (wordList[playOrder[wordIndex]].rememberDepth == SHOW_RANGE_NORMAL) {
        normal_num--
    }
    chosen_num = normal_num + favorite_num
    wordList[playOrder[wordIndex]].rememberDepth = 3
    saveFile("")
    inputText = ""
}

fun editWord(s: String) {
    val strArray: Array<String> =
        s.substring(0, s.length - 1).split("\n".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
    when (strArray.size) {
        3 -> {
            if (strArray[0].contains(" ") || strArray[1].contains(" ") || strArray[2].contains(" ")) {
                Toast.makeText(mainActivity, "格式错误。", Toast.LENGTH_LONG).show()
                return
            }
            wordList[playOrder[wordIndex]].foreign = strArray[0]
            wordList[playOrder[wordIndex]].pronunciation = strArray[1]
            wordList[playOrder[wordIndex]].native = strArray[2]
            inputText = ""
            saveFile("")
            showWord()
        }

        2 -> {
            if (strArray[0].contains(" ") || strArray[1].contains(" ")) {
                Toast.makeText(mainActivity, "格式错误。", Toast.LENGTH_LONG).show()
                return
            }
            wordList[playOrder[wordIndex]].foreign = strArray[0]
            wordList[playOrder[wordIndex]].pronunciation = strArray[0]
            wordList[playOrder[wordIndex]].native = strArray[1]
            inputText = ""
            saveFile("")
            showWord()
        }

        else -> {
            Toast.makeText(mainActivity, "格式错误。", Toast.LENGTH_LONG).show()
        }
    }
}

fun searchWord(s: String) {
    if (iShowRange != SHOW_RANGE_ALL) {
        inputText = ""
        Toast.makeText(mainActivity, "请选择显示所有单词模式。", Toast.LENGTH_LONG).show()
        return
    }
    if (sortType != 0) {
        inputText = ""
        Toast.makeText(mainActivity, "请选择顺序显示模式。", Toast.LENGTH_LONG).show()
        return
    }
    val str: String = s.substring(0, s.length - 1)
    var foundIndex = -1
    for (i in wordList.indices) {
        if (wordList[i].foreign.contains(str)) {
            foundIndex = i
            break
        }
        if (wordList[i].pronunciation.contains(str)) {
            foundIndex = i
            break
        }
        if (wordList[i].native.contains(str)) {
            foundIndex = i
            break
        }
    }
    if (foundIndex == -1) {
        inputText = "Not Found!"
        //TODO 得到焦点
    } else {
        wordIndex = foundIndex
        inputText = ""
        allIndex = foundIndex
        showWord()
    }
}

fun searchWordAll(s: String) {
    val str = s.substring(0, s.length - 1)
    val found: String = searchWord0(str)
    inputText = found
    //TODO 获得焦点
}

fun searchWord0(searchStr: String): String {
    val ret = StringBuilder()
    val lrcFiles: File? = mainActivity.getExternalFilesDir(null)
    val folder = lrcFiles?.list()
    if (folder != null) {
        for (i in folder.indices) {
            val pathName = lrcPath + "/" + folder[i]
            val lrcFile = File(pathName)
            if (!pathName.contains("双语") && lrcFile.isDirectory) {
                val fileNames = lrcFile.list()
                var fileName: String
                var tmp: String?
                if (fileNames != null) {
                    for (j in fileNames.indices) {
                        fileName = pathName + "/" + fileNames[j]
                        tmp = searchWordFromFile(fileName, folder[i], fileNames[j], searchStr)
                        ret.append(tmp)
                    }
                }
            }
        }
    }
    val rt = ret.toString()
    return if (rt.isEmpty()) {
        ""
    } else {
        rt.substring(2)
    }
}

fun searchWordFromFile(
    findFileName: String, folder: String,
    fileName: String, searchStr: String
): String {
    val lrcFile = File(findFileName)
    val tmp: String = searchWordFromTxtFile(lrcFile, searchStr)
    return if (tmp.isEmpty()) {
        ""
    } else {
        """

  $folder / ${fileName.substring(0, fileName.length - 4)}$tmp"""
    }
}

fun searchWordFromTxtFile(file: File?, str: String?): String {
    val ret = java.lang.StringBuilder()
    var tmp:String
    var read: InputStreamReader? = null
    var bufferedReader: BufferedReader? = null
    try {
        read = InputStreamReader(FileInputStream(file))
        bufferedReader = BufferedReader(read)
        var lineTxt: String
        var num = 0
        while (bufferedReader.readLine().also { lineTxt = it } != null) {
            num++
            if (lineTxt.contains(str!!)) {
                tmp = "  【" + num + "】" + lineTxt.substring(10)
                tmp = tmp.replace(" ", "\n")
                ret.append(tmp)
            }
        }
        bufferedReader.close()
        read.close()
        return ret.toString()
    } catch (e: Exception) {
        try {
            bufferedReader!!.close()
            read!!.close()
        } catch (e1: Exception) {
            //TODO
        }
        e.printStackTrace()
    }
    return ret.toString()
}

fun adjustWord() {
    if (iShowRange != SHOW_RANGE_ALL) {
        inputText = ""
        Toast.makeText(mainActivity, "请选择显示所有单词模式。", Toast.LENGTH_LONG).show()
        return
    }
    isAdjust = !isAdjust
    if (isAdjust) {
        loopNumber = 1
    }
    showTitle()
    inputText = ""
}
