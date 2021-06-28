package com.example.booksworld.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.booksworld.*
import com.example.booksworld.fragments.AboutAppFragment
import com.example.booksworld.fragments.FavouriteFragment
import com.example.booksworld.fragments.FragmentDashboard
import com.example.booksworld.fragments.ProfileFragment
import com.google.android.material.navigation.NavigationView

lateinit var drawerLayout: DrawerLayout
lateinit var coordinatorLayout: CoordinatorLayout

@SuppressLint("StaticFieldLeak")
lateinit var toolBar: androidx.appcompat.widget.Toolbar

@SuppressLint("StaticFieldLeak")
lateinit var frameLayout: FrameLayout
lateinit var navigationView: NavigationView

var previousMenuItem : MenuItem? = null


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolBar = findViewById(R.id.toolBar)
        frameLayout = findViewById(R.id.frame)
        navigationView = findViewById(R.id.navigationBar)

        setToolBar()

        // Another Method for Setting Menu on Tools Bar, primary method written in onCreateOptionsMenu Function
        /*toolBar.post {
            toolBar.inflateMenu(R.menu.toolbar_menu)
        }*/

        //for opening the application from dashboard screen
        openDashboard()


        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        // for toggling the drawer
        drawerLayout.addDrawerListener(actionBarDrawerToggle)

        // For syncing the Hamburger button with the drawer window
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {

            if(previousMenuItem != null) {
                previousMenuItem?.isChecked = false
            }
            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it

            when (it.itemId) {
                R.id.dashboard -> {
                    openDashboard()
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, ProfileFragment())
                        // .addToBackStack("Profile")
                        // this is because we don't want to move to last visited page.
                        .commit()

                    supportActionBar?.title = "Profile"
                    drawerLayout.closeDrawers()
                }
                R.id.aboutApp -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, AboutAppFragment())
                        .commit()

                    supportActionBar?.title = "About App"
                    drawerLayout.closeDrawers()
                }
                R.id.favourites -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, FavouriteFragment())
                        .commit()

                    supportActionBar?.title = "Favourites"
                    drawerLayout.closeDrawers()
                }

            }

            return@setNavigationItemSelectedListener true
        }

    }

    private fun openDashboard() {

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, FragmentDashboard())
            .commit()

        supportActionBar?.title = "Dashboard"
        navigationView.setCheckedItem(R.id.dashboard)
        drawerLayout.closeDrawers()
    }

    private fun setToolBar() {
        setSupportActionBar(toolBar)
        supportActionBar?.title = "Tool Bar"

        // For setting the Home button
        supportActionBar?.setHomeButtonEnabled(true)

        //For displaying the Home button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }

    // This is actually the Click Listener for the hamburger Button.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)

    }

    // Settings menu on Tool Bar
    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }*/

    override fun onBackPressed() {

        // when the visited page is Dashboard then show the default behaviour else move to DashBoard

        when (supportFragmentManager.findFragmentById(R.id.frame)) {
            !is FragmentDashboard -> openDashboard()

            else -> super.onBackPressed()
        }
    }
}