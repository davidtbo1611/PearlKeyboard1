package com.example.pearlkeyboard

import android.inputmethodservice.InputMethodService
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*

class PearlInputMethodService : InputMethodService() {
    private var symbols = false
    private var emojiMode = false
    private var shifted = false
    private var dark = false
    private lateinit var root: LinearLayout
    private val blue = Color.rgb(0,122,255)
    private val lightBg = Color.rgb(214,217,225)
    private val darkBg = Color.rgb(42,44,52)
    private val lightKey = Color.WHITE
    private val darkKey = Color.rgb(74,77,87)
    private val lightText = Color.rgb(35,36,42)
    private val darkText = Color.WHITE

    override fun onCreate() { super.onCreate(); dark = getSharedPreferences("pearl", MODE_PRIVATE).getBoolean("dark", false) }
    override fun onCreateInputView(): View { buildKeyboard(); return root }
    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) { super.onStartInputView(info, restarting); buildKeyboard() }

    private fun buildKeyboard() {
        val bg = if (dark) darkBg else lightBg
        root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(4),dp(7),dp(4),dp(7)); setBackgroundColor(bg) }
        val text = if (dark) darkText else lightText
        val panel = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL; setPadding(dp(3),0,dp(3),dp(5)) }
        addTextButton(panel, if (emojiMode) "ABC" else "☺", 0.9f, bg, text) { emojiMode = !emojiMode; buildKeyboard() }
        if (emojiMode) {
            val emojis = listOf("😀","😂","🥹","😍","🥰","😘","😎","😭","😡","👍","🙏","❤️","🔥","✨","🫶","💀","🐸","🦎","🦋","🌸","🌙","⭐","🍄","🐢")
            val scroll = HorizontalScrollView(this).apply { isHorizontalScrollBarEnabled = false }
            val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
            emojis.forEach { e -> addTextButton(row,e,1f,bg,text,48) { currentInputConnection?.commitText(e,1) } }
            scroll.addView(row); panel.addView(scroll, LinearLayout.LayoutParams(0,dp(45),1f))
            root.addView(panel); addBottomRow(text); return
        }
        addTextButton(panel,"◌",0.8f,bg,text) { symbols = !symbols; buildKeyboard() }
        addTextButton(panel,"GIF",0.8f,bg,text) { }
        addTextButton(panel,"☺",0.8f,bg,text) { emojiMode = true; buildKeyboard() }
        addTextButton(panel,"⚙",0.8f,bg,text) { launchSettings() }
        addTextButton(panel,"⋯",0.8f,bg,text) { (getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager).showInputMethodPicker() }
        root.addView(panel)
        val rows = if (symbols) listOf(listOf("1","2","3","4","5","6","7","8","9","0"), listOf("@","#","$","%","&","-","+","(",")","/"), listOf("=","*","\"", "'", ":",";","!","?","⌫")) else listOf(listOf("q","w","e","r","t","y","u","i","o","p"), listOf("a","s","d","f","g","h","j","k","l"), listOf("⇧","z","x","c","v","b","n","m","⌫"))
        rows.forEachIndexed { index, row ->
            val line = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER; setPadding(0,dp(2),0,dp(2)) }
            if (!symbols && index == 1) line.setPadding(dp(13),dp(2),dp(13),dp(2))
            row.forEach { key ->
                val special = key in listOf("⇧","⌫")
                addTextButton(line, if (shifted && key.length == 1) key.uppercase() else key, if (special) 1.35f else 1f, if (special) (if(dark) Color.rgb(95,99,111) else Color.rgb(173,179,191)) else (if(dark) darkKey else lightKey), text, 47) {
                    when(key) {
                        "⌫" -> currentInputConnection?.deleteSurroundingText(1,0)
                        "⇧" -> { shifted = !shifted; buildKeyboard() }
                        else -> { val out = if(shifted) key.uppercase() else key; currentInputConnection?.commitText(out,1); if(shifted) { shifted = false; buildKeyboard() } }
                    }
                }
            }
            root.addView(line)
        }
        addBottomRow(text)
    }

    private fun addBottomRow(text: Int) {
        val line = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER; setPadding(0,dp(3),0,0) }
        addTextButton(line,"?123",1.35f,if(dark) Color.rgb(95,99,111) else Color.rgb(173,179,191),text,44) { symbols = !symbols; buildKeyboard() }
        addTextButton(line,",",0.8f,if(dark) darkKey else lightKey,text,44) { currentInputConnection?.commitText(",",1) }
        addTextButton(line,"English (US)",4.5f,if(dark) darkKey else lightKey,text,44) { currentInputConnection?.commitText(" ",1) }
        addTextButton(line,".",0.8f,if(dark) darkKey else lightKey,text,44) { currentInputConnection?.commitText(".",1) }
        addTextButton(line,"↵",1.35f,blue,Color.WHITE,44) { currentInputConnection?.performEditorAction(currentInputEditorInfo?.imeOptions?.and(EditorInfo.IME_MASK_ACTION) ?: EditorInfo.IME_ACTION_DONE) }
        root.addView(line)
    }

    private fun addTextButton(parent: LinearLayout, label: String, weight: Float, color: Int, textColor: Int, height: Int = 43, action: () -> Unit) {
        val button = TextView(this).apply {
            text = label; gravity = Gravity.CENTER; textSize = if(label.length > 3) 13f else 20f; setTextColor(textColor)
            typeface = Typeface.create("sans-serif", Typeface.NORMAL)
            background = GradientDrawable().apply { setColor(color); cornerRadius = dp(6).toFloat() }
            elevation = dp(1).toFloat(); setOnClickListener { action() }
            layoutParams = LinearLayout.LayoutParams(0,dp(height),weight).apply { setMargins(dp(2),dp(1),dp(2),dp(1)) }
        }
        parent.addView(button)
    }
    private fun launchSettings() { val i = packageManager.getLaunchIntentForPackage(packageName); i?.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK); if(i != null) startActivity(i) }
    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()
}
