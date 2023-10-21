package com.frank.word

import android.content.Context
import android.content.Intent
import android.media.session.MediaSession
import android.view.KeyEvent

class MediaButtonReceiver(mContent: Context, mKeyDownListener: IKeyDownListener) {

    //创建一个MediaSession的实例,参数2是一个字符串,可以随便填
    private val mMediaSession = MediaSession(mContent, javaClass.name)

    internal annotation class KeyActions {
        companion object {
            //好像还支持手柄按键...
            //所有keyCode参考:https://www.apiref.com/android-zh/android/view/KeyEvent.html
            var PLAY_ACTION: Int = 126
            var PAUSE_ACTION: Int = 127
            var PREV_ACTION: Int = 88
            var NEXT_ACTION: Int = 87
        }
    }

    init {
        mMediaSession.setCallback(object : MediaSession.Callback() {
            override fun onMediaButtonEvent(mediaButtonIntent: Intent): Boolean {
                val keyEvent: KeyEvent =
                    mediaButtonIntent.getParcelableExtra(Intent.EXTRA_KEY_EVENT)
                        ?: return false

                mKeyDownListener.onKeyDown(keyEvent.keyCode)

                //返回值的作用跟事件分发的原理是一样的,返回true代表事件被消费,其他应用也就收不到了
                return true
            }
        })
        mMediaSession.setFlags(MediaSession.FLAG_HANDLES_MEDIA_BUTTONS or MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS)
        mMediaSession.isActive = true
    }

    interface IKeyDownListener {
        fun onKeyDown(@KeyActions keyAction: Int)
    }
}