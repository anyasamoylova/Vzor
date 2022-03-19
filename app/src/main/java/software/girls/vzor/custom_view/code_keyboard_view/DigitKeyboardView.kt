package software.girls.vzor.custom_view.code_keyboard_view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import software.girls.vzor.R

class DigitKeyboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private val digits: List<TextView>
    private val btnErase: FrameLayout

    init {
        inflate(context, R.layout.custom_digit_keyboard, this)

        digits = getDigitList()
        btnErase = findViewById(R.id.btnErase)
    }

    private fun getDigitList() =
        listOf<TextView>(
            findViewById(R.id.digit_0),
            findViewById(R.id.digit_1),
            findViewById(R.id.digit_2),
            findViewById(R.id.digit_3),
            findViewById(R.id.digit_4),
            findViewById(R.id.digit_5),
            findViewById(R.id.digit_6),
            findViewById(R.id.digit_7),
            findViewById(R.id.digit_8),
            findViewById(R.id.digit_9)
        )


    fun setOnDigitClickListener(onDigitClick: (String) -> Unit) {
        digits.forEach { digit -> digit.setOnClickListener { onDigitClick.invoke(digit.text.toString()) } }
    }

    fun setOnEraseBtnClickListener(onEraseClick: () -> Unit) {
        btnErase.setOnClickListener { onEraseClick.invoke() }
    }
}