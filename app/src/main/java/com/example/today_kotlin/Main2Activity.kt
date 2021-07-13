package com.example.today_kotlin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.*
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class Main2Activity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        val drawer: Button = findViewById(R.id.btn_dr)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_save, R.id.nav_community
            ), drawerLayout
        )
        val user = Firebase.auth.currentUser

        drawer.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START);
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val headerView: View = navigationView.getHeaderView(0)
        val headerIcon: ImageButton = headerView.findViewById(R.id.imageButton)
        val headerUsername: TextView = headerView.findViewById(R.id.name_tv)
        val headerEmail: TextView = headerView.findViewById(R.id.textView)

        

        headerUsername.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }
        headerIcon.setOnClickListener{
            startActivity(Intent(this, SettingActivity::class.java))
        }
        headerEmail.setOnClickListener{
            startActivity(Intent(this, SettingActivity::class.java))
        }


        storage= FirebaseStorage.getInstance()
        val httpsReference = storage.getReferenceFromUrl(user?.photoUrl.toString())
        Glide.with(headerView).load(httpsReference).apply(
            RequestOptions.bitmapTransform(
            RoundedCorners(14)
        )).into(headerIcon)

        headerUsername.text = user?.displayName
        headerEmail.text = user?.email

    }


    override fun onStart(){
        super.onStart()
        if (Firebase.auth.currentUser == null){
            startActivity(Intent(this,LoginActivity::class.java))
        }
        auth = Firebase.auth
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main2, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}