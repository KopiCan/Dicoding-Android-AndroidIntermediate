package com.dicoding.andrintermediate2.ui.utilitys

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.dicoding.andrintermediate2.R

class EmailCustomEdittext: AppCompatEditText {
    constructor(context: Context) : super(context) { init() }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { init() }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init() }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable) {
                if (!isValidEmail(s)) {
                    error = context.getString(R.string.text_mandatory_format)
                }
            }
        })
    }

    private fun isValidEmail(email: CharSequence) : Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}