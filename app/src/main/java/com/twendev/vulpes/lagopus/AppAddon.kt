package com.twendev.vulpes.lagopus

import android.content.Context
import android.content.pm.ApplicationInfo
import android.widget.Toast

class AppAddon {
    companion object {
        fun ToastIsDebug(text: String, context: Context) {
            if (BuildConfig.BUILD_TYPE == "debug") {
                Toast.makeText(context, text, Toast.LENGTH_LONG).show()
            }
        }
    }
}