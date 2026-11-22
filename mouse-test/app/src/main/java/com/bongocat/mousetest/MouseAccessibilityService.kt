package com.bongocat.mousetest

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.graphics.PixelFormat
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.Gravity
import android.view.InputDevice
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.provider.Settings

class MouseAccessibilityService : AccessibilityService() {
    private var windowManager: WindowManager? = null
    private var bubble: TextView? = null
    private var params: WindowManager.LayoutParams? = null
    private var moves = 0
    private var clicks = 0
    private var wheels = 0
    private var lastX = 0f
    private var lastY = 0f

    override fun onServiceConnected() {
        super.onServiceConnected()
        val info = serviceInfo
        if (Build.VERSION.SDK_INT >= 34) {
            // Android only delivers requested generic motion sources while touch exploration is enabled.
            // Requested mouse events are not forwarded to the foreground app by the OS.
            info.flags = info.flags or AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE
            info.setMotionEventSources(InputDevice.SOURCE_MOUSE or InputDevice.SOURCE_MOUSE_RELATIVE)
            serviceInfo = info
        }
        showBubble()
    }

    private fun showBubble() {
        if (!Settings.canDrawOverlays(this) || bubble != null) return
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val cat = TextView(this).apply {
            text = "🐱\nAGUARDA"
            textSize = 13f
            gravity = Gravity.CENTER
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(0xFFFFFFFF.toInt())
            background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(0xDD202A44.toInt())
                setStroke(4, 0xFFFFC857.toInt())
            }
            elevation = 12f
        }
        val windowType = if (Build.VERSION.SDK_INT >= 26)
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        else WindowManager.LayoutParams.TYPE_PHONE
        val lp = WindowManager.LayoutParams(
            104, 104, windowType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 36
            y = 180
        }
        var startX = 0
        var startY = 0
        var downX = 0f
        var downY = 0f
        cat.setOnTouchListener { _, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    startX = lp.x
                    startY = lp.y
                    downX = event.rawX
                    downY = event.rawY
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    lp.x = startX + (event.rawX - downX).toInt()
                    lp.y = startY + (event.rawY - downY).toInt()
                    try { windowManager?.updateViewLayout(cat, lp) } catch (_: Exception) {}
                    true
                }
                else -> true
            }
        }
        try {
            windowManager?.addView(cat, lp)
            bubble = cat
            params = lp
        } catch (_: Exception) {
            bubble = null
        }
    }

    override fun onMotionEvent(event: MotionEvent) {
        when (event.actionMasked) {
            MotionEvent.ACTION_HOVER_MOVE, MotionEvent.ACTION_MOVE -> {
                moves++
                lastX = event.x
                lastY = event.y
                updateBubble("🐱\nMOVE $moves")
            }
            MotionEvent.ACTION_BUTTON_PRESS -> {
                clicks++
                updateBubble("🐱\nCLICK $clicks")
            }
            MotionEvent.ACTION_BUTTON_RELEASE -> updateBubble("🐱\nSOLTO")
            MotionEvent.ACTION_SCROLL -> {
                wheels++
                updateBubble("🐱\nRODÍZIO $wheels")
            }
        }
    }

    private fun updateBubble(label: String) {
        bubble?.post { bubble?.text = label }
    }

    override fun onAccessibilityEvent(event: android.view.accessibility.AccessibilityEvent?) {}
    override fun onInterrupt() {}

    override fun onDestroy() {
        try {
            bubble?.let { windowManager?.removeView(it) }
        } catch (_: Exception) {}
        bubble = null
        super.onDestroy()
    }
}