package software.girls.vzor.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import software.girls.vzor.R
import software.girls.vzor.screen.home.HomeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            openHomeFragment()
        }
    }

    private fun openHomeFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, HomeFragment.newInstance())
            .commitAllowingStateLoss()
    }
}