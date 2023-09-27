package com.frank.word

import android.view.MenuItem
import android.widget.Toast

fun readOnly(item: MenuItem) {
    item.isChecked = isShowEditText
    isShowEditText = !isShowEditText
}

fun playOrder(item: MenuItem, index: Int) {
    sortType = index
    item.isChecked = true
    sortFiles()
    sortWords()
}

fun elseItemSelect(item: MenuItem): Boolean {
    for (i in 0..5) {
        if (item.itemId == numberArray[i]) {
            loopNumber = i
            item.isChecked = true
            item.isCheckable = true
            loopIndex = 0
            if (loopNumber != 1 && wordIndex < playOrder.size && wordList.size > playOrder[wordIndex] + 1) {
                iEnd = if (isForeignOnly && wordList[playOrder[wordIndex]].middlePlayTime > 0) {
                    wordList[playOrder[wordIndex]].middlePlayTime
                } else {
                    wordList[playOrder[wordIndex] + 1].startPlayTime
                }
            }
            return false
        }
    }
    for (i in 6..21) {
        if (item.itemId == numberArray[i]) {
            item.isChecked = true
            item.isCheckable = true
            CurrentWordClass = i - 6
            if (iShowRange == SHOW_RANGE_CLASS) {
                showClassNext()
            } else {
                wordList[playOrder[wordIndex]].wordClass = wClass[CurrentWordClass]
                saveFile("")
                showCurrentWord()
            }
            return false
        }
    }
    return true
}

fun doHome() {
    if (isFirstTime) {
        inputText = ""
        isShowDict = true
        isToDraw = 1 - isToDraw
    } else {
        if (isEditFile && isShowEditText) {
            saveFile("")
        }
        if (!isPlay) {
            mediaPlayer.start()
        }
        readTextFile(0)
    }
}

fun doOneKey() {
    loopNumber = 1
    sortType = 1
    iShowRange = SHOW_FAVORITE
}

fun showRangeAll(item: MenuItem) {
    iShowRange = SHOW_RANGE_ALL
    sortFiles()
    sortWords()
    showTitle()
    item.isChecked = true
    item.isCheckable = true

}

fun showRangeChosen(item: MenuItem): Boolean {
    if (chosen_num == 0 && !isPlayFolder) {
        Toast.makeText(mainActivity, "没有相应单词", Toast.LENGTH_LONG).show()
        return false
    }
    iShowRange = SHOW_CHOSEN
    item.isChecked = true
    item.isCheckable = true
    sortFiles()
    sortWords()
    if (wordList[playOrder[wordIndex]].rememberDepth == SHOW_DEL) {
        readTextFile(0)
    } else {
        showTitle()
    }
    return true
}

fun showRangeDel(item: MenuItem): Boolean {
    if (removed_num == 0 && !isPlayFolder) {
        Toast.makeText(mainActivity, "没有相应单词", Toast.LENGTH_LONG).show()
        return false
    }
    iShowRange = SHOW_DEL
    item.isChecked = true
    item.isCheckable = true
    sortFiles()
    sortWords()
    if (wordList[playOrder[wordIndex]].rememberDepth == SHOW_DEL) {
        readTextFile(0)
    } else {
        showTitle()
    }
    return true
}

fun showRangeNormal(item: MenuItem): Boolean {
    if (normal_num == 0 && !isPlayFolder) {
        Toast.makeText(mainActivity, "没有相应单词", Toast.LENGTH_LONG).show()
        return false
    }
    iShowRange = SHOW_RANGE_NORMAL
    item.isChecked = true
    sortFiles()
    sortWords()
    if (wordList[playOrder[wordIndex]].rememberDepth == SHOW_RANGE_NORMAL) {
        showTitle()
    } else {
        readTextFile(0)
    }
    return true
}

fun showRangeFavorite(item: MenuItem): Boolean {
    if (favorite_num == 0 && !isPlayFolder) {
        Toast.makeText(mainActivity, "没有相应单词", Toast.LENGTH_LONG).show()
        return false
    }
    iShowRange = SHOW_FAVORITE
    item.isChecked = true
    sortFiles()
    sortWords()
    if (wordList[playOrder[wordIndex]].rememberDepth == SHOW_FAVORITE) {
        showTitle()
    } else {
        readTextFile(0)
    }
    return true
}

fun showRangeClass(item: MenuItem) {
    iShowRange = SHOW_RANGE_CLASS
    item.isChecked = true
}

fun showHelp() {
    isShowEditText = true

    inputText = """文字末尾打入字符时的隐藏功能

换行：
  有文字时检查正确性
     正确时清空, 错误时显示正确单词
  无文字时：
     下一个单词

<: 上一个单词
空格或点击单词：清空

调整时刻模式,顺序,全部播放，才可以的:
  +：插入新单词
  -：删除当前单词(删文字 留读音)(慎用！！)
  !；设置当前单词为<非单词>属性(文字读音全删)

#：更新当前单词
&：查找并移动到指定单词位置
*：在所有书中，查找单词
@：调整单词播放时刻 切换
?：显示本帮助信息。"""

}

fun showWordType(item: MenuItem, type: Int) {
    item.isChecked = true
    item.isCheckable = true
    showWordType = type
    showWord()
}

fun setMute(item: MenuItem, volume: Float) {
    if (!isFirstTime) {
        playVolume = volume
        item.isChecked = true
        item.isCheckable = true
        mediaPlayer.setVolume(volume, volume)
    }
}

fun editWordFile() {
    if (lrcFile != null) {
        isLRC_Time_OK = false
        val str = readRawTxtFile(lrcFile!!)
        editWords(str)
    }
}