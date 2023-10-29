package com.frank.word

import android.widget.Toast
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.text.DecimalFormat

fun saveFile(msg: String) {
    var stringBuffer: StringBuilder? = null
    val decimalFormat = DecimalFormat("00")
    val timeFormat = DecimalFormat("0000000")
    if (isLRC_Time_OK) {
        if (msg.isEmpty()) {
            stringBuffer = StringBuilder()
            for (i in wordList.indices) {
                try {
                    stringBuffer.append(wordList[i].rememberDepth)
                    iStart = wordList[i].startPlayTime
                    stringBuffer.append(timeFormat.format(iStart.toLong()))
                    if (isNumeric(wordList[i].wordClass)) {
                        stringBuffer.append(decimalFormat.format(wordList[i].wordClass.toInt()))
                        wordList[i].wordClass = wClass[wordList[i].wordClass.toInt() % 20]
                    } else {
                        stringBuffer.append("00")
                    }
                    stringBuffer.append(wordList[i].foreign)
                    stringBuffer.append(" ")
                    stringBuffer.append(wordList[i].pronunciation)
                    stringBuffer.append(" ")
                    stringBuffer.append(wordList[i].native)
                    stringBuffer.append(" ")
                    stringBuffer.append(wordList[i].sentence1.replace("\n","%"))
                    stringBuffer.append(" ")
                    stringBuffer.append(wordList[i].sentence2.replace("\n","%"))
                    stringBuffer.append(" ")
                    stringBuffer.append(wordList[i].sentence3.replace("\n","%"))
                    iStart = wordList[i].middlePlayTime
                    stringBuffer.append(" ")
                    stringBuffer.append(timeFormat.format(iStart.toLong()))
                    if (!isNumeric(wordList[i].wordClass)) {
                        stringBuffer.append(" ")
                        stringBuffer.append(wordList[i].wordClass)
                    }
                    if (wordList[i].tone.isNotEmpty()) {
                        stringBuffer.append(" ")
                        stringBuffer.append(wordList[i].tone)
                    }
                    stringBuffer.append("\n")
                } catch (ee: Exception) {
                    ee.printStackTrace()
                }
            }
        } else {
            val str: String = inputText
            val strArray = str.split("\n".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            if (strArray.size == wordList.size) {
                stringBuffer = StringBuilder()
                for (i in wordList.indices) {
                    stringBuffer.append(wordList[i].rememberDepth)
                    if (isNumeric(wordList[i].wordClass)) {
                        stringBuffer.append(decimalFormat.format(wordList[i].wordClass))
                    } else {
                        stringBuffer.append("00")
                    }
                    iStart = wordList[i].startPlayTime
                    stringBuffer.append(timeFormat.format(iStart.toLong()))
                    stringBuffer.append(strArray[i])
                    stringBuffer.append("\n")
                }
            }
        }
    }

    val pathName = "$lrcPath/$folderName/$fileName.txt"
    var out: FileOutputStream? = null
    var writer: BufferedWriter? = null
    try {
        val fileImage = File(pathName)
        val parentFile = fileImage.parentFile
        if (parentFile == null) {
            Toast.makeText(mainActivity, "保存失败", Toast.LENGTH_LONG).show()
            return
        }
        if (!parentFile.exists()) {
            val dirFile = parentFile.mkdirs()
            if (!dirFile) {
                Toast.makeText(mainActivity, "创建目录失败！", Toast.LENGTH_LONG).show()
                return
            }
        }
        if (!fileImage.exists()) {
            fileImage.createNewFile()
        }
        out = FileOutputStream(fileImage)
        writer = BufferedWriter(OutputStreamWriter(out))
        if (stringBuffer == null) {
            val str: String = inputText
            writer.write(str)
        } else {
            writer.write(stringBuffer.toString())
        }
        Toast.makeText(mainActivity, msg + "保存成功", Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        if (writer != null) {
            try {
                writer.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        if (out != null) {
            try {
                out.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
