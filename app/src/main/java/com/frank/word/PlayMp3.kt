package com.frank.word

import android.content.Intent
import android.net.Uri
import java.util.Timer
import java.util.TimerTask

var thisTimer: Timer = Timer()
var thisTask: TimerTask? = null
var mp3Uri: Uri? = null

fun playMp3() {

    if (isFirstTime || isFirstTimeForPlay) {
        val intent = Intent(mainActivity, MyService::class.java)
        mainActivity.startService(intent)
    } else {
        mMediaPlayer.reset()
        mMediaPlayer.setDataSource(mainActivity, mp3Uri!!)
        mMediaPlayer.prepareAsync()
        mMediaPlayer.start()
    }
}

fun doTask() {
    if (!mMediaPlayer.isPlaying) return

    if (!isLRC_Time_OK || !isLRC_Format_OK) {
        iEnd = mMediaPlayer.duration
        if (isLRC_Time_OK || !isLRC_Format_OK) {
            return
        }
    }
    if (isFirstTimeForPlay) {
        mMediaPlayer.seekTo(iStart)
        isFirstTime = false
        isFirstTimeForPlay = false
    }
    val currentPosition = mMediaPlayer.currentPosition
    if (loopNumber == 0) {
        if (currentPosition >= iEnd) {
            mMediaPlayer.seekTo(iStart)
        }
        return
    } else if (loopNumber == 1) {
        if (playOrder[wordIndex] + 1 < wordList.size) {
            if (currentPosition > iEnd) {
                if (isForeignOnly) {
                    Thread() {
                        mMediaPlayer.pause()
                        Thread.sleep(pauseTime)
                        mMediaPlayer.start()
                        showNext()
                    }.start()
                } else {
                    showNext()
                }
            }
        } else {
            if (isAdjust) {
                if (isToAddTime && isMiddleTime) {
                    showNext()
                }
            } else {
                showNext()
            }
        }
    } else {
        if (currentPosition < iEnd) {
            return
        }
        if (loopIndex < loopNumber - 1) {
            loopIndex++
            mMediaPlayer.seekTo(iStart)
            return
        }
        loopIndex = 0
        showNext()
    }
}

