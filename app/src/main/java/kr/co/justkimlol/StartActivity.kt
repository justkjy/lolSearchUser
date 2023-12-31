package kr.co.justkimlol

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.co.justkimlol.mainfragment.champion.ChampionFragment
import kr.co.justkimlol.mainfragment.home.HomeFragment
import kr.co.justkimlol.mainfragment.user.UserFragment
import kr.co.justkimlol.databinding.ActivityNavigationMainBinding
import kr.co.justkimlol.viewModel.SharedViewModel


class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNavigationMainBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        binding.navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    changeFragment(HomeFragment())
                    true
                }
                R.id.navigation_user -> {
                    changeFragment(UserFragment())
                    true
                }
                R.id.navigation_champion -> {
                    changeFragment(ChampionFragment())
                    true
                }
                else -> {
                    true
                }
            }
        }


        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val mainDestinations = setOf(
            R.id.navigation_user, R.id.navigation_champion
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            navView.visibility = when (destination.id) {
                in mainDestinations -> {
                    View.VISIBLE }
                else -> View.GONE
            }
        }
    }

    private fun changeFragment(fragment: Fragment) {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.nav_host_fragment_activity_main, fragment)
            .commit()
    }
}