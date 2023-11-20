package com.frank.word

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import java.util.TimerTask

private const val ACTION_PLAY: String = "com.example.action.PLAY"

class MyService : Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    private val myBinder = mBinder()

    class mBinder : Binder() {
        fun a() {
            Log.d("data", "service")
        }
    }

    private var mMediaPlayer: MediaPlayer? = MediaPlayer()  //null

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
        Log.d("data", "onCreate")
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "my_service",
            "前台Service通知",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        manager.createNotificationChannel(channel)
        //}
        val intent = Intent(this, MainActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(this, "my_service")
            .setContentTitle("这是主题")
            .setContentText("这是内容")
            .setSmallIcon(R.drawable.outline_add_circle_outline_24)
            .build()
        manager.notify(1,notification);
//        startForeground(1, notification)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.action) {
            ACTION_PLAY -> {
//                mediaPlayer = MediaPlayer()
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
                                    doTask()
                                } catch (e: Exception) {
                                    e.toString()
                                }
                            }
                        }
                        thisTimer.scheduleAtFixedRate(thisTask, 100, 100)
                    }
                }
                mMediaPlayer?.apply {
                    setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
                    setOnPreparedListener(this@MyService)
                    isLooping = true
                    prepareAsync() // prepare async to not block main thread
                }
            }
        }
        return START_STICKY_COMPATIBILITY
    }

    fun initMediaPlayer() {
        // ...initialize the MediaPlayer here...
        mediaPlayer?.setOnErrorListener(this@MyService)
    }

    override fun onBind(intent: Intent?): IBinder {
        return myBinder
    }

    /** Called when MediaPlayer is ready */
    override fun onPrepared(mediaPlayer: MediaPlayer) {
        mediaPlayer.start()
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}
