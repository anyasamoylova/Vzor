package software.girls.vzor

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.core.content.ContextCompat

fun Context.checkCameraPermission(): Boolean {
    return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
}

fun Context.checkOverlayPermission(): Boolean {
    return Settings.canDrawOverlays(this)
}

