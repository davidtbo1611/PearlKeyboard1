package com.example.pearlkeyboard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.content.Context
import android.widget.*
import android.graphics.Color

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dark = getSharedPreferences("pearl", MODE_PRIVATE).getBoolean("dark", false)
        val bg = if (dark) Color.rgb(25,26,31) else Color.rgb(245,245,250)
        val fg = if (dark) Color.WHITE else Color.rgb(35,36,42)
        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(24,32,24,24); setBackgroundColor(bg) }
        val title = TextView(this).apply { text = "Pearl Keyboard"; textSize = 28f; setTextColor(fg); typeface = android.graphics.Typeface.create("sans-serif", android.graphics.Typeface.BOLD) }
        val desc = TextView(this).apply { text = "An iPhone-inspired keyboard for Android. Rounded keys, emoji shortcuts, and a clean typeface."; textSize = 16f; setTextColor(fg); setPadding(0,14,0,24) }
        val enable = Button(this).apply { text = "1. Enable Pearl Keyboard"; setOnClickListener { startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)) } }
        val choose = Button(this).apply { text = "2. Choose Pearl Keyboard"; setOnClickListener { (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showInputMethodPicker() } }
        val theme = Switch(this).apply { text = "Dark keyboard theme"; setTextColor(fg); isChecked = dark; setOnCheckedChangeListener { _, checked -> getSharedPreferences("pearl", MODE_PRIVATE).edit().putBoolean("dark", checked).apply(); recreate() } }
        val note = TextView(this).apply { text = "Privacy: this starter keyboard does not request internet access and does not collect or transmit what you type. Emoji appearance in other apps depends on your Android system font."; textSize = 13f; setTextColor(fg); setPadding(0,24,0,0) }
        root.addView(title); root.addView(desc); root.addView(enable); root.addView(choose); root.addView(theme); root.addView(note)
        setContentView(root)
    }
}
