package software.girls.vzor.custom_view.code_keyboard_view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import software.girls.vzor.R

class EnterCodeView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private val codeView: CodeView
    private val digitKeyboardView: DigitKeyboardView

    init {
        inflate(context, R.layout.custom_enter_code, this)

        codeView = findViewById(R.id.code)
        digitKeyboardView = findViewById(R.id.digitKeyboard)

        initEnterCodeView()
    }

    private fun initEnterCodeView() {
        digitKeyboardView.setOnDigitClickListener { codeView.enterDigit(it) }
        digitKeyboardView.setOnEraseBtnClickListener { codeView.eraseDigit() }
    }
}