package com.aks4125.cachelibrary.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.NavHostFragment
import com.aks4125.cachelibrary.R
import com.aks4125.cachelibrary.ui.loadmore.LoadMoreActivity
import com.aks4125.cachelibrary.util.Utils
import com.aks4125.cachex.DownloadUtils
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_nav_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

/* Navigation view controller*/
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var mProvider: DownloadUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_main)
        setSupportActionBar(toolbar)
        mProvider = DownloadUtils.getInstance() // get download utils instance
        setupUI()
    }

    private fun setupUI() {
        fab.setOnClickListener {
            AlertDialog.Builder(this@MainActivity)
                    .setIcon(R.mipmap.ic_launcher_round)
                    .setTitle("Project Details")
                    .setMessage(Utils(this).projectInfo())
                    .setPositiveButton("Ok", null)
                    .show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.title = getString(R.string.app_name)
        nav_view.setNavigationItemSelectedListener(this)
        nav_view.menu.getItem(0).isChecked = true
        navigateToHome()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                if (!item.isChecked)
                    navigateToHome()
            }
            R.id.nav_load_more -> {
                startActivity(Intent(this, LoadMoreActivity::class.java))
            }
            R.id.nav_clear -> {
                mProvider?.resetCache()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun navigateToHome() {
        val host = NavHostFragment.create(R.navigation.main_navigator)
        supportFragmentManager.beginTransaction().replace(R.id.main_container, host).setPrimaryNavigationFragment(host).commit()

    }
}
