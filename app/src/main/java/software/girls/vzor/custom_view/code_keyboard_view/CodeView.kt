package software.girls.vzor.custom_view.code_keyboard_view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import org.w3c.dom.Text
import software.girls.vzor.R

class CodeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    private var curIndex = 0
    private val digits: MutableList<TextView>

    init {
        inflate(context, R.layout.custom_code, this)
        orientation = HORIZONTAL
        digits = getDigitViews()
    }

    private fun getDigitViews() =
        mutableListOf<TextView> (
            findViewById<ConstraintLayout>(R.id.index0).findViewById(R.id.digit),
            findViewById<ConstraintLayout>(R.id.index1).findViewById(R.id.digit),
            findViewById<ConstraintLayout>(R.id.index2).findViewById(R.id.digit),
            findViewById<ConstraintLayout>(R.id.index3).findViewById(R.id.digit),
            )

    fun enterDigit(digit: String) {
        if (curIndex < DIGITS_NUMBER) {
            with (digits[curIndex]) {
                text = digit
                visibility = View.VISIBLE
            }
            curIndex++
        } else {
            //TODO make some animation
        }
    }

    fun eraseDigit() {
        if (curIndex != 0) {
            curIndex--
            digits[curIndex].visibility = View.INVISIBLE
        }
    }

    companion object {
        private const val DIGITS_NUMBER = 4
    }
}