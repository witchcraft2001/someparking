package com.someparking.androidapp.ui

import android.content.Context
import android.graphics.PixelFormat
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.someparking.androidapp.R

class ProgressLayout(context: Context) {
    private var processLayout: View? = null
    private var wm: WindowManager? = null
    private var params: WindowManager.LayoutParams? = null
    private var visible = false

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        processLayout = inflater.inflate(R.layout.layout_progress, null)
        wm = (context as AppCompatActivity).windowManager
        params = WindowManager.LayoutParams().apply {
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.MATCH_PARENT
            format = PixelFormat.TRANSLUCENT
        }
    }

    @Synchronized
    fun showProcessLayout() {
        if (visible) {
            return
        }
        wm?.addView(processLayout, params)
        visible = true
    }

    @Synchronized
    fun hideProcessLayout() {
        if (!visible) {
            return
        }
        wm?.removeViewImmediate(processLayout)
        visible = false
    }

    fun isVisible(): Boolean {
        return visible
    }
}