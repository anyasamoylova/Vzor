package software.girls.vzor.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import software.girls.vzor.R
import software.girls.vzor.databinding.FragmentRootBinding
import software.girls.vzor.screen.home.HomeFragment
import software.girls.vzor.utils.showToast

class RootFragment private constructor() : Fragment() {

    private var _binding: FragmentRootBinding? = null
    private val binding: FragmentRootBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRootBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showChildFragment(HomeFragment.newInstance())

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.actionHome -> {
                    showToast("Action Home")
                    true
                }
                R.id.actionResearch -> {
                    showToast("Action Research")
                    true
                }
                R.id.actionHistory -> {
                    showToast("Action History")
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showChildFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commitAllowingStateLoss()
    }

    companion object {
        fun newInstance() = RootFragment()
    }
}