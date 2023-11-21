package com.frank.word

//import android.bluetooth.BluetoothAdapter
//import android.bluetooth.BluetoothDevice
import android.Manifest
import android.app.AlertDialog
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.view.KeyEvent
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.documentfile.provider.DocumentFile
import com.frank.word.ui.Home
import com.frank.word.ui.SaveInfo
import com.frank.word.ui.SetBlackSystemBars
import com.frank.word.ui.SetSettingDialog
import com.frank.word.ui.ShowTextFieldFun
import com.frank.word.ui.maxLessonNum
import com.frank.word.ui.theme.WordTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlin.system.exitProcess

var myFontSize by mutableStateOf(30.0f)
var musicStep by mutableStateOf(0.0f)
lateinit var lrcPath: String
lateinit var folderName: String
lateinit var pathAndName: String

lateinit var mainActivity: MainActivity

var isPlay by mutableStateOf(true)
var isShowPermission by mutableStateOf(true)
var isFirstTime by mutableStateOf(true)
var isFirstTimeForPlay by mutableStateOf(true)
var isEditFile by mutableStateOf(false)
var playVolume by mutableStateOf(1.0f)
var fileName = ""
var titleString by mutableStateOf("")
lateinit var openMP3: () -> Unit
lateinit var openFolder: () -> Unit
lateinit var pause: () -> Unit

class MainActivity : ComponentActivity(), MediaButtonReceiver.IKeyDownListener {

    lateinit var myBinder: MyService.mBinder
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            myBinder = service as MyService.mBinder
//            myBinder.startDownload()
//            myBinder.getProgress()
        }

        override fun onServiceDisconnected(name: ComponentName) {
        }
    }

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
                pathAndName = files[0].uri.path ?: ""
                mp3Uri = files[0].uri
                readTextFile(0)
            }
        }

    @OptIn(ExperimentalPermissionsApi::class)
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
            titleString = appName
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { result ->
                pathAndName = result?.path!!
                mp3Uri = result
                readTextFile(0)
            }
            openMP3 = {
                if (!iShowDel && !iShowNormal && !iShowFavorite) {
                    Toast.makeText(mainActivity, "请选择单词范围。", Toast.LENGTH_LONG).show()
                    isShowSettingDialog = true
                } else {
                    isPlayFolder = false
                    launcher.launch("audio/*")
                }
            }
            openFolder = {
                if (!iShowDel && !iShowNormal && !iShowFavorite) {
                    Toast.makeText(mainActivity, "请选择单词范围。", Toast.LENGTH_LONG).show()
                    isShowSettingDialog = true
                } else {
                    dirRequest.launch(Uri.EMPTY)
                }
            }
            pause = {
                isPlay = !mMediaPlayer.isPlaying
                if (mMediaPlayer.isPlaying) {
                    mMediaPlayer.pause()
                } else {
                    mMediaPlayer.start()
                }
            }

            WordTheme {
                TransparentSystemBars()         // 沉浸式 状态栏 条件之二
                if (isToSaveInfo) {
                    SaveInfo()
                    isToSaveInfo = false
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black // MaterialTheme.colorScheme.background,
                ) {
                    SetBlackSystemBars()
                    val cameraPermissionState = rememberPermissionState(
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                    if (cameraPermissionState.hasPermission) {
                        SetSettingDialog()
                        Column(modifier = Modifier.fillMaxSize()) {
                            Row(Modifier.height(40.dp)) {}
                            Row(Modifier.height(45.dp)) {
                                Icon(
                                    painterResource(R.drawable.outline_folder_open_24),
                                    " ",
                                    Modifier
                                        .size(45.dp)
                                        .clickable {
                                            if (isOpenSingleFile) {
                                                openMP3()
                                            } else if (!isFirstTime) {
                                                isShowChooseLessonDialog = true
//                                            isChooseSingleLessonDialog = true
//                                            openFolder()
                                            }
                                        }
                                        .padding(4.dp),
                                    tint = Color.White
                                )
                                Text(
                                    titleString,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = if (isPlay) Color.White else Color.Cyan,
                                    modifier = Modifier
                                        .weight(1.0f)
                                        .align(alignment = Alignment.CenterVertically)
                                        .clickable {
                                            if (!isFirstTime) {
                                                if (isEditFile && isShowEditText) {
                                                    saveFile("")
                                                } else if (isShowList) {
                                                    if (mMediaPlayer.isPlaying) {
                                                        mMediaPlayer.pause()
                                                    } else {
                                                        mMediaPlayer.start()
                                                    }
                                                    isPlay = mMediaPlayer.isPlaying
                                                }
                                            }
                                        }
                                        .padding(start = 5.dp, end = 0.dp)
                                )
                                Icon(
                                    painterResource(R.drawable.outline_settings_24),
                                    " ",
                                    Modifier
                                        .size(45.dp)
                                        .padding(5.dp)
                                        .clickable { isShowSettingDialog = true },
                                    tint = Color.White
                                )
                            }
                            if (isFirstTime) {
                                if (isShowDict) {
                                    Column {
//                                    Row(Modifier.height(55.dp)) {}
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
                                            onValueChangeFinished = { isToSaveInfo = true },
                                            valueRange = 20f..50f,
                                            modifier = Modifier
                                                .height(50.dp)
                                                .padding(20.dp)
                                        )
//                                Row(Modifier.height(30.dp)) {}
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
                    } else {
                        if (isShowPermission) {
                            isShowPermission = false
                            AlertDialog.Builder(this)
                                .setTitle("通知权限")
                                .setMessage("本APP必须授予通知权限，否则无法实现持久播放。")
                                .setOnCancelListener{
                                    cameraPermissionState.launchPermissionRequest()
                                }
                                .setPositiveButton("OK") { dialog, _ ->
                                    dialog.cancel()
                                }
                                .show()
                        }
                    }
                }
            }
        }
        //registerBluetoothReceiver()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (!isFirstTime) {
            MediaPlayer().stop()
            MediaPlayer().release()
            val intent = Intent(this, MyService::class.java)
            stopService(intent) // 停止Service
        }
        exitProcess(0)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        var bRet = true
        if (KeyEvent.KEYCODE_HEADSETHOOK == keyCode) {
            mMediaPlayer.pause()
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
            MediaButtonReceiver.KeyActions.PLAY_ACTION -> mMediaPlayer.start()
            MediaButtonReceiver.KeyActions.PAUSE_ACTION -> mMediaPlayer.pause()
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

@Composable
fun TransparentSystemBars() {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons,
            isNavigationBarContrastEnforced = false,
        )
    }
}
