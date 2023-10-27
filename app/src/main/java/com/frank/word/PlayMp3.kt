package com.frank.word

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import java.util.Timer
import java.util.TimerTask

var thisTimer: Timer = Timer()
var thisTask: TimerTask? = null
var mp3Uri: Uri? = null

fun playMp3() {

    if (isFirstTime) {
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )

            if (thisTask == null) {
                thisTask = object : TimerTask() {
                    override fun run() {
                        try {
                            if (titleString.isNotEmpty()) mainActivity.title = titleString
                            titleString = ""
                            doTask()
                        } catch (e: Exception) {
                            e.toString()
                        }
                    }
                }
                thisTimer.scheduleAtFixedRate(thisTask, 100, 100)
            }
        }
    } else {
        mediaPlayer.reset()
    }
    isFirstTime = true
    mediaPlayer.setDataSource(mainActivity, mp3Uri!!)
    mediaPlayer.prepare()
    mediaPlayer.start()
    //MediaButtonReceiver(mainActivity,mainActivity)
}

fun doTask() {
    if (!mediaPlayer.isPlaying) return

    if (!isLRC_Time_OK || !isLRC_Format_OK) {
        iEnd = mediaPlayer.duration
        if (isLRC_Time_OK || !isLRC_Format_OK) {
            return
        }
    }
    if (isFirstTime) {
        mediaPlayer.seekTo(iStart)
        isFirstTime = false
    }
    val currentPosition = mediaPlayer.currentPosition
    if (loopNumber == 0) {
        if (currentPosition >= iEnd) {
            mediaPlayer.seekTo(iStart)
        }
        return
    } else if (loopNumber == 1) {
        if (playOrder[wordIndex] + 1 < wordList.size) {
            if (currentPosition > iEnd) {
                if (isForeignOnly) {
                    Thread() {
                        mediaPlayer.pause()
                        Thread.sleep(pauseTime)
                        mediaPlayer.start()
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
            mediaPlayer.seekTo(iStart)
            return
        }
        loopIndex = 0
        showNext()
    }
}

