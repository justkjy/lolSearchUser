package kr.co.lol

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.co.lol.databinding.ActivityNavigationMainBinding
import kr.co.lol.ui.champion.ChampionFragment
import kr.co.lol.ui.home.HomeFragment
import kr.co.lol.ui.home.viewModel.ChampionInitViewModel
import kr.co.lol.ui.user.UserFragment

class StartActivity : AppCompatActivity() {

    //private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityNavigationMainBinding

    private lateinit var sharedViewModel: SharedViewModel

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)

        val navView: BottomNavigationView = binding.navView
        val navClickEvent = binding.navView

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
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_user
//            )
//        )
//        //setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)

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