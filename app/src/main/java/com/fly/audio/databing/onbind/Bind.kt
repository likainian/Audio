package com.fly.audio.databing.onbind

import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View

@Suppress("unused")
object Bind {

    fun onClick(block: (v: View?) -> Unit) =
        OnClickBinding(block)

    fun onLongClick(block: (v: View) -> Boolean) =
        OnLongClickBinding(block)

    fun onKeyListener(block: (v: View, keyCode: Int, event: KeyEvent) -> Boolean) =
        OnKeyBinding(block)

    fun onFocusChanged(block: (v: View, focus: Boolean) -> Unit) =
        OnFocusChangedBinding(block)

    fun onTouch(block: (v: View, event: MotionEvent) -> Boolean) =
        OnTouchBinding(block)

}
