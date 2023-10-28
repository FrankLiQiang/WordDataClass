package com.frank.word

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat

/**
 * @author komine
 * 监听蓝牙音频设备的连接状态
 */
class BluetoothStateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {

            //蓝牙已连接,如果不需要判断是否为音频设备,下面代码块的判断可以不要
            BluetoothDevice.ACTION_ACL_CONNECTED -> {
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                        ?: return
                //如果在Android32 SDK还需要声明 android.permission.BLUETOOTH_CONNECT权限
                if (ActivityCompat.checkSelfPermission(
                        context!!,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                if (isHeadPhone(device.bluetoothClass)) {
                    Log.d("BluetoothStateReceiver", "蓝牙音频设备已连接")
                }
            }

            //蓝牙状态发生改变,断开,正在扫描,正在连接
            BluetoothAdapter.ACTION_STATE_CHANGED -> {
                val blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)
                if (blueState == BluetoothAdapter.STATE_OFF) {
                    Log.d("BluetoothStateReceiver", "蓝牙设备已断开")
                }
            }
        }
    }

    companion object {
        private const val PROFILE_HEADSET = 0

        private const val PROFILE_A2DP = 1

        private const val PROFILE_OPP = 2

        private const val PROFILE_HID = 3

        private const val PROFILE_PANU = 4

        private const val PROFILE_NAP = 5

        private const val PROFILE_A2DP_SINK = 6
    }


    private fun isHeadPhone(bluetoothClass: BluetoothClass?): Boolean {
        if (bluetoothClass == null) {
            return false
        }
        return if (doesClassMatch(bluetoothClass, PROFILE_HEADSET)) true else doesClassMatch(
            bluetoothClass,
            PROFILE_A2DP
        )
    }

    //系统源码拷贝
    //可以参考 http://androidxref.com/9.0.0_r3/xref/frameworks/base/core/java/android/bluetooth/BluetoothClass.java
    private fun doesClassMatch(bluetoothClass: BluetoothClass, profile: Int): Boolean {
        return if (profile == PROFILE_A2DP) {
            if (bluetoothClass.hasService(BluetoothClass.Service.RENDER)) {
                return true
            }
            when (bluetoothClass.deviceClass) {
                BluetoothClass.Device.AUDIO_VIDEO_HIFI_AUDIO, BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES, BluetoothClass.Device.AUDIO_VIDEO_LOUDSPEAKER, BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO -> true
                else -> false
            }
        } else if (profile == PROFILE_A2DP_SINK) {
            if (bluetoothClass.hasService(BluetoothClass.Service.CAPTURE)) {
                return true
            }
            when (bluetoothClass.deviceClass) {
                BluetoothClass.Device.AUDIO_VIDEO_HIFI_AUDIO, BluetoothClass.Device.AUDIO_VIDEO_SET_TOP_BOX, BluetoothClass.Device.AUDIO_VIDEO_VCR -> true
                else -> false
            }
        } else if (profile == PROFILE_HEADSET) {
            // The render service class is required by the spec for HFP, so is a
            // pretty good signal
            if (bluetoothClass.hasService(BluetoothClass.Service.RENDER)) {
                return true
            }
            when (bluetoothClass.deviceClass) {
                BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE, BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET, BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO -> true
                else -> false
            }
        } else if (profile == PROFILE_OPP) {
            if (bluetoothClass.hasService(BluetoothClass.Service.OBJECT_TRANSFER)) {
                return true
            }
            when (bluetoothClass.deviceClass) {
                BluetoothClass.Device.COMPUTER_UNCATEGORIZED, BluetoothClass.Device.COMPUTER_DESKTOP, BluetoothClass.Device.COMPUTER_SERVER, BluetoothClass.Device.COMPUTER_LAPTOP, BluetoothClass.Device.COMPUTER_HANDHELD_PC_PDA, BluetoothClass.Device.COMPUTER_PALM_SIZE_PC_PDA, BluetoothClass.Device.COMPUTER_WEARABLE, BluetoothClass.Device.PHONE_UNCATEGORIZED, BluetoothClass.Device.PHONE_CELLULAR, BluetoothClass.Device.PHONE_CORDLESS, BluetoothClass.Device.PHONE_SMART, BluetoothClass.Device.PHONE_MODEM_OR_GATEWAY, BluetoothClass.Device.PHONE_ISDN -> true
                else -> false
            }
        } else if (profile == PROFILE_HID) {
            bluetoothClass.deviceClass and BluetoothClass.Device.Major.PERIPHERAL == BluetoothClass.Device.Major.PERIPHERAL
        } else if (profile == PROFILE_PANU || profile == PROFILE_NAP) {
            // No good way to distinguish between the two, based on class bits.
            if (bluetoothClass.hasService(BluetoothClass.Service.NETWORKING)) {
                true
            } else bluetoothClass.deviceClass and BluetoothClass.Device.Major.NETWORKING == BluetoothClass.Device.Major.NETWORKING
        } else {
            false
        }
    }
}