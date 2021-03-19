package com.kotlinmovieappmvvm

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.CompoundButton
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
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

    var isNightModeOn: Boolean = false
    lateinit var sharedPrefsEdit: SharedPreferences.Editor

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var switch: SwitchMaterial
    private lateinit var searchMenu: Menu
    private lateinit var searchToolbar: Toolbar
    private lateinit var item_search: MenuItem

    companion object{
        var app_context: Context? = null
            set(value) {
                field = value?.applicationContext
            }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        app_context = applicationContext

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        setSearchToolbar()

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
        val menuItem: MenuItem = drawer_nav_view.menu.findItem(R.id.nav_switch)

        switch = menuItem.actionView.findViewById(R.id.drawer_switch) as SwitchMaterial

        setToggleButton()

        setThemeFromSharedManager()

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

    /**
     * This method is responsible for the clicks on the menu of the action bar
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    circleReveal(R.id.search_toolbar, 1, true, true)
                else
                    searchToolbar.visibility = View.VISIBLE

                item_search.expandActionView()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    /**
     * this method is responsible for the setting up of search view toolbar
     * with the ripple animations
     */
    private fun setSearchToolbar() {
        searchToolbar = findViewById(R.id.search_toolbar)
        if (searchToolbar != null) {
            searchToolbar.inflateMenu(R.menu.menu_search)
            searchMenu = searchToolbar.menu

            searchToolbar.setNavigationOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    circleReveal(R.id.search_toolbar, 1, true, false)
                else
                    searchToolbar.visibility = View.GONE
            }

            item_search = searchMenu.findItem(R.id.search_filter)
            item_search.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                    return true
                }

                override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        circleReveal(R.id.search_toolbar, 1, true, false)
                    else
                        searchToolbar.visibility = View.GONE

                    return true
                }

            })

            initSearchView()

        }
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
        val appSettingsPrefs: SharedPreferences = getSharedPreferences("AppSetting", MODE_PRIVATE)
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

    /***************** search toolbar methods ******************/

    /**
     *  This method is responsible for the setting up of Edit text in
     */
    private fun initSearchView() {
        val searchView: SearchView =
            searchMenu.findItem(R.id.search_filter).actionView as SearchView

        // Enable / Disable submit button in the Keyboard
        searchView.isSubmitButtonEnabled = false;

        //set Hint and Text color
        var txtSearch: EditText = searchView.findViewById(R.id.search_src_text) as EditText
        txtSearch.hint = "Search..."

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { callSearch(it) }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //newText?.let { callSearch(it) }
                return true
            }

            public fun callSearch(query: String) {
                //Do searching
                Log.i("TAG", "" + query)
            }

        })


    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun circleReveal(
        viewID: Int,
        posFromRight: Int,
        containsOverflow: Boolean,
        isShow: Boolean
    ) {
        val myView: View = findViewById(viewID)
        var width: Int = myView.getWidth()
        if (posFromRight > 0) width -= posFromRight * resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) - resources.getDimensionPixelSize(
            R.dimen.abc_action_button_min_width_material
        ) / 2
        if (containsOverflow) width -= resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material)
        val cx = width
        val cy: Int = myView.getHeight() / 2
        val anim: Animator
        anim =
            if (isShow) ViewAnimationUtils.createCircularReveal(
                myView,
                cx,
                cy,
                0f,
                width.toFloat()
            ) else ViewAnimationUtils.createCircularReveal(
                myView, cx, cy,
                width.toFloat(), 0f
            )
        anim.duration = 220.toLong()

        // make the view invisible when the animation is done
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                if (!isShow) {
                    super.onAnimationEnd(animation)
                    myView.setVisibility(View.INVISIBLE)
                }
            }
        })

        // make the view visible and start the animation
        if (isShow) myView.setVisibility(View.VISIBLE)

        // start the animation
        anim.start()
    }

}