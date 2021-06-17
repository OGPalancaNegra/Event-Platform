package Activities

import Fragments.HomeFragment
import Fragments.ProfileFragment
import Fragments.SearchFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.eventplatform.R
import kotlinx.android.synthetic.main.fragment_eo_navigation.*

class egNavigation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_eo_navigation)

        val homeFragment = HomeFragment()
        val searchFragment = SearchFragment()
        val profileFragment = ProfileFragment()

        setCurrentFragment(homeFragment)

        //listen to user click/input on bottom navigation menu with setOnNavigation

        //navigate to either home , search or profile activty based on which user item user clicked

        bottom_nav_view.setOnNavigationItemSelectedListener {
            when (it.itemId){

                R.id.home_nav -> {
                    //set homeFragment
                    setCurrentFragment(homeFragment)
                }

                R.id.search_nav -> {
                    //set homeFragment
                    setCurrentFragment(searchFragment)
                }

                R.id.profile_nav -> {
                    //set homeFragment
                    setCurrentFragment(profileFragment)
                }


            }

            true
        }

    }


    //what value changes? fragment so thats parameter
    fun setCurrentFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout, fragment).commit()
        }
    }


}