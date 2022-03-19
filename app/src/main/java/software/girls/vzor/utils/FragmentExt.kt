package software.girls.vzor.utils

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showToast(text: String) = Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show()