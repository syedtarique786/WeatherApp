package com.android.weatherapp.utils

import android.annotation.SuppressLint
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.adapters.TextViewBindingAdapter
import com.android.weatherapp.model.viewmodel.CustomOnEditorActionListener
import android.text.TextWatcher
import android.os.Build
import android.graphics.drawable.Drawable
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.android.weatherapp.R
import java.lang.NullPointerException


/**
 * Created by    :   Syed
 * Created on    :   09,February,2020.
 * Modified By   :
 * Modified On   :   09,February,2020.
 * Copyright (c) 2020 @Syed. All rights reserved.
 */

@BindingAdapter("onEditorEnterAction")
fun EditText.onEditorEnterAction(f: Function1<String, Unit>?) {

    if (f == null) setOnEditorActionListener(null)
    else setOnEditorActionListener { v, actionId, event ->

        val imeAction = when (actionId) {
            EditorInfo.IME_ACTION_DONE,
            EditorInfo.IME_ACTION_SEARCH,
            EditorInfo.IME_ACTION_SEND,
            EditorInfo.IME_ACTION_GO -> true
            else -> false
        }

        val keydownEvent = event?.keyCode == KeyEvent.KEYCODE_SEARCH
                && event.action == KeyEvent.ACTION_DOWN

        if (imeAction or keydownEvent)
            true.also { f(v.editableText.toString()) }
        else false
    }
}


@BindingAdapter("android:onTextChanged")
fun setListener(view: TextView, onTextChanged: TextViewBindingAdapter.OnTextChanged) {
    //setListener(view, null, onTextChanged, null)
}


@BindingAdapter("app:customOnEditorActionListener")
fun setCustomOnEditorActionListener(view: TextView, listener: CustomOnEditorActionListener?) {
    if (listener == null) {
        view.setOnEditorActionListener(null)
    } else {
        view.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                listener.onEditorAction(v?.text.toString())
                return false
            }
        })
    }
}

@BindingAdapter("textChangedListener")
fun bindTextWatcher(editText: EditText, textWatcher: TextWatcher) {
    editText.addTextChangedListener(textWatcher)
}


@SuppressLint("ResourceAsColor")
@BindingAdapter("backgroundColor")
fun bindBackgroundColor(layout: RelativeLayout, color: Int) {
    try {
        layout.setBackgroundColor(color)
    } catch (ex : NullPointerException) {
        ex.printStackTrace()
        layout.setBackgroundColor(R.color.feature_box_indigo)
    }
}