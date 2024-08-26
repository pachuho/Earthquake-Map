package com.pachuho.earthquakemap

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.gun0912.tedpermission.coroutine.TedPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun requestAppPermissions(permissions: List<String>, callback: (Boolean) -> Unit) {
    CoroutineScope(Dispatchers.Main).launch {
        val isGranted = withContext(Dispatchers.IO) {
            TedPermission
                .create()
                .setPermissions(*permissions.toTypedArray())
                .check()
                .isGranted
        }
        callback(isGranted)
    }
}


fun AppCompatActivity.hasPermissions(permissions: List<String>): Boolean {
    return permissions.all { permission ->
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }
}

fun Fragment.hasPermissions(permissions: List<String>): Boolean {
    return permissions.all { permission ->
        ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
}