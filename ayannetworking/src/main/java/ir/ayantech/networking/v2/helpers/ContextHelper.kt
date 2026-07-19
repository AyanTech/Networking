package ir.ayantech.networking.v2.helpers

import android.content.Context
import android.os.Build

fun Context.generateUserAgent(sign: String): String {

    val versionName = packageManager?.getPackageInfo(packageName, 0)?.versionName

    return buildString {
        append("BuildVersion:(${Build.VERSION.RELEASE})")
        append("Brand:(${Build.BRAND})")
        append("Model:(${Build.MODEL})")
        append("Device:(${Build.DEVICE})")
        append("AppVersion:($versionName)")
        append("Sign:(${sign})")
    }
}