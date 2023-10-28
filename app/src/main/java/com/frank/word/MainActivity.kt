package com.frank.word

//import android.bluetooth.BluetoothAdapter
//import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.documentfile.provider.DocumentFile
import com.frank.word.ui.Home
import com.frank.word.ui.SetBlackSystemBars
import com.frank.word.ui.ShowTextFieldFun
import com.frank.word.ui.maxLessonNum
import com.frank.word.ui.theme.WordTheme
import kotlin.system.exitProcess

var myFontSize by mutableStateOf(30.0f)
var musicStep by mutableStateOf(0.0f)
lateinit var lrcPath: String
lateinit var folderName: String
lateinit var pathAndName: String

lateinit var mainActivity: MainActivity
lateinit var mediaPlayer: MediaPlayer

var isPlay by mutableStateOf(true)
var isFirstTime by mutableStateOf(true)
var isEditFile by mutableStateOf(false)
var fileName = ""
var titleString = ""
var playVolume = 1.0f
lateinit var openMP3: () -> Unit
lateinit var openFolder: () -> Unit
lateinit var pause: () -> Unit
var rangeItem: MenuItem? = null
var chooseItem: MenuItem? = null

class MainActivity : ComponentActivity(), MediaButtonReceiver.IKeyDownListener {

    //    private val mBluetoothStateReceiver: BluetoothStateReceiver = BluetoothStateReceiver()
    private val dirRequest =
        registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
            uri?.let {
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                val f = DocumentFile.fromTreeUri(this, uri)
                files.clear()
                f?.listFiles()?.forEach {
                    if (it.isFile && it.uri.path.toString().endsWith(".mp3")) {
                        files.add(it)
                    }
                }
                fileBeginIndex = 0
                fileEndIndex = files.size - 1
                maxLessonNum = files.size
                sortFiles()
                isPlayFolder = true
                rangeItem?.isVisible = true
                chooseItem?.isVisible = true
                pathAndName = files[0].uri.path ?: ""
                mp3Uri = files[0].uri
                readTextFile(0)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        mainActivity = this
        actionBar?.show()
        actionBar?.setDisplayHomeAsUpEnabled(true)
        lrcPath = getExternalFilesDir(null).toString()

        setContent {
            val appName = stringResource(id = R.string.app_name)
            title = appName + "_".repeat(26 - appName.length)
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { result ->
                pathAndName = result?.path!!
                mp3Uri = result
                readTextFile(0)
            }
            openMP3 = {
                rangeItem?.isVisible = false
                chooseItem?.isVisible = false
                isPlayFolder = false
                launcher.launch("audio/*")
            }
            openFolder = { dirRequest.launch(Uri.EMPTY) }
            pause = {
                isPlay = !mediaPlayer.isPlaying
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                } else {
                    mediaPlayer.start()
                }
            }

            WordTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    SetBlackSystemBars()
                    if (isFirstTime) {
                        if (isShowDict) {
                            Column {
                                Row(Modifier.height(55.dp)) {}
                                //Row(Modifier.height(40.dp)) {}
                                ShowTextFieldFun(
                                    Modifier
                                        .weight(1.0f)
                                        .fillMaxWidth()
                                    //.padding(15.dp)
                                )
                                Slider(
                                    value = myFontSize,
                                    onValueChange = { myFontSize = it },
                                    onValueChangeFinished = {},
                                    valueRange = 20f..50f,
                                    modifier = Modifier
                                        .height(50.dp)
                                        .padding(20.dp)
                                )
                                Row(Modifier.height(30.dp)) {}
                            }
                        }
                    } else {
                        Home(
                            myFontSize,
                            { myFontSize = it },
                            { pause() },
                            50.0f
                        )
                    }
                }
            }
        }
        //registerBluetoothReceiver()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        rangeItem = menu!!.findItem(R.id.folder_range)
        chooseItem = menu.findItem(R.id.choose_lesson)
        menuInit(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> doHome()
            R.id.read_only -> readOnly(item)
            R.id.in_order -> playOrder(item, 0)
            R.id.play_in_random -> playOrder(item, 1)
            R.id.play_all_random -> playOrder(item, 2)
            R.id.show_word_all -> showWordType(item, SHOW_ALL)
            R.id.show_word_foreign -> showWordType(item, SHOW_FOREIGN)
            R.id.show_word_pronunciation -> showWordType(item, SHOW_PRONUNCIATION)
            R.id.show_word_native -> showWordType(item, SHOW_NATIVE)
            R.id.show_none -> showWordType(item, SHOW_NONE)
            R.id.mode_normal -> setMute(item, 1.0f)
            R.id.mode_mute -> setMute(item, 0.0f)
            R.id.show_range_all -> showRangeAll(item)
            R.id.show_range_chosen -> {
                return if (showRangeChosen(item)) true else super.onOptionsItemSelected(item)
            }

            R.id.show_range_del -> {
                return if (showRangeDel(item)) true else super.onOptionsItemSelected(item)
            }

            R.id.show_range_normal -> {
                return if (showRangeNormal(item)) true else super.onOptionsItemSelected(item)
            }

            R.id.show_range_favorite -> {
                return if (showRangeFavorite(item)) true else super.onOptionsItemSelected(item)
            }

            R.id.class_noun1,
            R.id.class_verb,
            R.id.class_verb_10,
            R.id.class_verb_20,
            R.id.class_verb_30,
            R.id.class_vi0,
            R.id.class_vt0,
            R.id.class_adj_10,
            R.id.class_adj_20,
            R.id.class_fuci0,
            R.id.class_lianti,
            R.id.class_lianyu,
            R.id.class_jietou,
            R.id.class_jiewei,
            R.id.class_spec,
            R.id.class_item,
            -> showRangeClass(item)

            R.id.help -> showHelp()
            R.id.open -> openMP3()
            R.id.folder -> openFolder()
            R.id.one_key -> doOneKey()
            R.id.folder_range -> {
                isShowChooseLessonDialog = true
            }

            R.id.choose_lesson -> {
                isShowPopupMenu = true
            }

            R.id.middle_play -> {
                isToAddTime = !isToAddTime
                isMiddleTime = !isMiddleTime
                isAdjust = isMiddleTime
                item.isChecked = isMiddleTime
                item.isCheckable = true
            }

            R.id.show_mode -> {
                isShowList = !isShowList
                item.isChecked = isShowList
                item.isCheckable = true
            }

            R.id.play_middle -> {
                isForeignOnly = !isForeignOnly
                item.isChecked = isForeignOnly
                item.isCheckable = true
            }

            R.id.play_pause_time -> {
                isShowPauseTimeDialog = true
            }

            R.id.edit_word -> {
                editWordFile()
            }

            else -> {
                return if (elseItemSelect(item)) true else super.onOptionsItemSelected(item)
            }
        }
        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (!isFirstTime) {
            MediaPlayer().stop()
            MediaPlayer().release()
        }
        exitProcess(0)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        var bRet = true
        if (KeyEvent.KEYCODE_HEADSETHOOK == keyCode) {
            mediaPlayer.pause()
            bRet = true
        } else if (KeyEvent.KEYCODE_MEDIA_NEXT == keyCode) {
            showNext()
        } else if (KeyEvent.KEYCODE_MEDIA_PREVIOUS == keyCode) {
            showPrev()
        } else {
            bRet = super.onKeyDown(keyCode, event)
        }
        return bRet
    }

    //https://www.cnblogs.com/komine/p/16187278.html
    override fun onKeyDown(keyAction: Int) {
        when (keyAction) {
            MediaButtonReceiver.KeyActions.PLAY_ACTION -> mediaPlayer.start()
            MediaButtonReceiver.KeyActions.PAUSE_ACTION -> mediaPlayer.pause()
            MediaButtonReceiver.KeyActions.PREV_ACTION -> showPrev()
            MediaButtonReceiver.KeyActions.NEXT_ACTION -> showNext()
        }
    }

//    private fun registerBluetoothReceiver() {
//        val intentFilter = IntentFilter()
//        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
//        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
//        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
//        intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_OFF")
//        intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_ON")
//
//        registerReceiver(mBluetoothStateReceiver, intentFilter)
//    }
}

