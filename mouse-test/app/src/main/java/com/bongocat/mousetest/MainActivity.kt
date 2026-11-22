package com.bongocat.mousetest

import android.app.Activity
import android.os.Bundle
import android.view.InputDevice
import android.view.MotionEvent
import android.view.Gravity
import android.graphics.Typeface
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Button

class MainActivity : Activity() {
    private lateinit var summary: TextView
    private lateinit var details: TextView
    private var moves = 0
    private var clicks = 0
    private var scrolls = 0
    private var lastEvent = "Aguardando mouse..."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(24, 28, 24, 24)
        }
        root.addView(TextView(this).apply {
            text = "BONGO CAT — MOUSE TEST"
            textSize = 22f
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
        })
        root.addView(TextView(this).apply {
            text = "Com esta tela aberta, mova o mouse, clique e use a rodinha."
            textSize = 16f
            gravity = Gravity.CENTER
            setPadding(0, 18, 0, 18)
        })
        summary = TextView(this).apply {
            textSize = 18f
            typeface = Typeface.MONOSPACE
            setPadding(12, 18, 12, 18)
        }
        root.addView(summary)
        details = TextView(this).apply {
            textSize = 14f
            typeface = Typeface.MONOSPACE
            setPadding(12, 12, 12, 12)
        }
        root.addView(details)
        root.addView(Button(this).apply {
            text = "ZERAR CONTADORES"
            setOnClickListener {
                moves = 0
                clicks = 0
                scrolls = 0
                lastEvent = "Contadores zerados"
                refresh()
            }
        })
        root.addView(TextView(this).apply {
            text = "Este teste observa o mouse somente enquanto esta tela está em primeiro plano. Não ativa captura global nem altera os controles de outros aplicativos."
            textSize = 13f
            setPadding(4, 22, 4, 0)
        })
        setContentView(root)
        refresh()
        window.decorView.isFocusableInTouchMode = true
        window.decorView.requestFocus()
    }

    override fun onGenericMotionEvent(event: MotionEvent): Boolean {
        val mouse = (event.source and InputDevice.SOURCE_MOUSE) == InputDevice.SOURCE_MOUSE ||
            (event.source and InputDevice.SOURCE_MOUSE_RELATIVE) == InputDevice.SOURCE_MOUSE_RELATIVE
        if (!mouse) return super.onGenericMotionEvent(event)

        when (event.actionMasked) {
            MotionEvent.ACTION_HOVER_MOVE, MotionEvent.ACTION_MOVE -> {
                moves++
                lastEvent = "MOVIMENTO x=${event.x.toInt()} y=${event.y.toInt()}"
            }
            MotionEvent.ACTION_BUTTON_PRESS -> {
                clicks++
                lastEvent = "BOTAO PRESSIONADO: ${event.buttonState}"
            }
            MotionEvent.ACTION_BUTTON_RELEASE -> {
                lastEvent = "BOTAO SOLTO: ${event.buttonState}"
            }
            MotionEvent.ACTION_SCROLL -> {
                scrolls++
                val v = event.getAxisValue(MotionEvent.AXIS_VSCROLL)
                val h = event.getAxisValue(MotionEvent.AXIS_HSCROLL)
                lastEvent = "RODÍZIO vertical=$v horizontal=$h"
            }
            else -> lastEvent = "AÇÃO ${event.actionMasked} / fonte ${event.source}"
        }
        refresh()
        return true
    }

    private fun refresh() {
        summary.text = "MOVIMENTOS: $moves\nCLICKS:     $clicks\nROLAGENS:   $scrolls"
        details.text = "ÚLTIMO EVENTO:\n$lastEvent"
    }
}
