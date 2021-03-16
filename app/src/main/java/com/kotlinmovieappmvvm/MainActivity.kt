package com.kotlinmovieappmvvm

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.switchmaterial.SwitchMaterial


class MainActivity : AppCompatActivity() {

    var isNightModeOn:Boolean = false
    lateinit var sharedPrefsEdit: SharedPreferences.Editor

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var switch: SwitchMaterial;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // step:1 find and assign navController
        val navController = findNavController(R.id.nav_host_fragment)

        // step : 2 initialize drawer
        drawerLayout = findViewById(R.id.drawer_layout)

        // step : 3 find Id of drawer
        val drawer_nav_view: NavigationView = findViewById(R.id.drawer_nav_view)

        // step :4
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery
            ), drawerLayout
        )

        //step:5 setupActionBarWithNavController
        setupActionBarWithNavController(navController, appBarConfiguration)

        // step:6 set up controller to both drawer navigation to not to write all fragment transactions code
        drawer_nav_view.setupWithNavController(navController)
        val menuItem :MenuItem= drawer_nav_view.menu.findItem(R.id.nav_switch)

        switch = menuItem.actionView.findViewById(R.id.drawer_switch) as SwitchMaterial

        setToggleButton()

        setThemeFromSharedManager()

    }


    /**
     * This method setup the toggle button from the drawer and sets the click listener to it
     */
    private fun setToggleButton() {
        switch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPrefsEdit.putBoolean("NightMode", true)
                sharedPrefsEdit.apply()
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPrefsEdit.putBoolean("NightMode", false)
                sharedPrefsEdit.apply()
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
        })
    }

    /**
     *  Will check the status of night mode enabled or not from shared preferences
     */
    private fun setThemeFromSharedManager() {
        val appSettingsPrefs:SharedPreferences = getSharedPreferences("AppSetting", MODE_PRIVATE)
        sharedPrefsEdit = appSettingsPrefs.edit()
        isNightModeOn = appSettingsPrefs.getBoolean("NightMode", false)

        if (isNightModeOn) {
            switch.isChecked = true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            switch.isChecked = false
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    /**
     * The NavigationUI can handle the Up button behavior by itself so that you don’t have to manage it explicitly.
     * It also automatically hides the Up button when we are on the home screen of the app.
     */
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    /**
     *  This method will inflate the menu present in action bar
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
}