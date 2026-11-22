package com.bongocat.mousetest

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.graphics.Typeface
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(28, 34, 28, 24)
        }
        root.addView(TextView(this).apply {
            text = "🐱  BONGO CAT"
            textSize = 30f
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
        })
        root.addView(TextView(this).apply {
            text = "TESTE DO MOUSE FORA DO APP"
            textSize = 17f
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
            setPadding(0, 8, 0, 24)
        })
        root.addView(TextView(this).apply {
            text = "Este teste coloca um gatinho em uma bolinha flutuante. Depois, você pode sair desta tela e abrir outro aplicativo para observar se o gatinho reage ao mouse."
            textSize = 16f
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 22)
        })
        root.addView(Button(this).apply {
            text = "1 — PERMITIR A BOLINHA FLUTUANTE"
            setOnClickListener {
                if (!Settings.canDrawOverlays(this@MainActivity)) {
                    startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
                }
            }
        })
        root.addView(Button(this).apply {
            text = "2 — ATIVAR TESTE DE MOUSE"
            setOnClickListener {
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("Atenção: teste experimental")
                    .setMessage("Para observar o mouse fora do app, o Android exige ativar o modo de exploração por toque e solicitar os eventos do mouse. O próprio Android informa que os eventos das fontes solicitadas não são enviados ao app que está em primeiro plano. Portanto, enquanto o teste estiver ativo, o mouse pode deixar de controlar o outro app e o toque na tela pode se comportar de forma diferente. Teste somente em uma tela/app sem importância, nunca durante uma partida. Desative o serviço nas configurações de Acessibilidade para restaurar o comportamento normal.")
                    .setNegativeButton("Cancelar", null)
                    .setPositiveButton("Entendi — abrir ajustes") { _, _ ->
                        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                    }
                    .show()
            }
        })
        root.addView(TextView(this).apply {
            text = "ORDEM: conceda a permissão da bolinha primeiro; depois ative “Bongo Cat — teste do mouse” em Acessibilidade. Volte ao app e deixe o serviço ligado. O gatinho aparecerá por cima dos outros apps."
            textSize = 14f
            setPadding(2, 22, 2, 0)
        })
        root.addView(TextView(this).apply {
            text = "Para parar: desative o serviço Bongo Cat nas configurações de Acessibilidade. Para remover a bolinha, desative também a permissão de sobreposição."
            textSize = 13f
            setPadding(2, 18, 2, 0)
        })
        setContentView(root)
    }
}