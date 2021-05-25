package com.example.today_kotlin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Layout
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ActionBarContainer
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.api.Distribution
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firestore.v1.FirestoreGrpc
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import com.example.today_kotlin.NavHeaderActivity as NavHeaderActivity

class Main2Activity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    lateinit var txtView: TextView
    //lateinit var binding:FragmentListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_save, R.id.nav_community
            ), drawerLayout
        )
        var db = Firebase.firestore
        val user = Firebase.auth.currentUser




        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val headerView: View = navigationView.getHeaderView(0)
        val headerIcon: ImageView = headerView.findViewById(R.id.imageView)
        val headerUsername: Button = headerView.findViewById(R.id.name_btn)
        val headerEmail: TextView = headerView.findViewById(R.id.textView)
        //val fragmentView: View =
        //val appBarMainTmp: View = findViewById(R.id.includeAppBarMain)
        //val contentMainTmp: View = appBarMainTmp.findViewById(R.id.includeContentMain)
        //val navHostFragTmp: View = contentMainTmp.findViewById(R.id.nav_host_fragment)
        //val viewTmp: View = inflater.inflate(R.layout.)

        //val tmp: View = onCreateView()
        /*val navHomeTmp: Fragment = navHostFragTmp.findViewById(R.id.nav_home);
        val textToday: TextView = navHomeTmp.findViewById(R.id.text_home); */



        headerUsername.setOnClickListener {
            startActivity(Intent(this, settingActivity::class.java))
        }
        /*
        val docRef = db.collection("users").document(user.uid)
        docRef.get().addOnSuccessListener { document ->
            if(document != null) {
                if(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE) == document.data?.get("date") as String) {
                    textToday.setText(document.data?.get("todayWords").toString())
                } else {
                    val newListGiveMe = db.collection("words").document("mGHEB2dhXFQkPF8KJww2")
                    newListGiveMe.get().addOnSuccessListener { document1 ->
                        val wordList: ArrayList<String> = document1.data?.get("words") as ArrayList<String>
                        val todayWords: String = wordList.get(Random().nextInt(wordList.size))
                        val listWords: ArrayList<String> = document.data?.get("listWords") as ArrayList<String>
                        listWords.add(todayWords)
                        val firestoreData = hashMapOf(
                            "date" to LocalDateTime.now().format(DateTimeFormatter.ISO_DATE),
                            "todayWords" to todayWords,
                            "listWords" to listWords
                        )
                        docRef.set(firestoreData).addOnSuccessListener {
                            textToday.setText(todayWords)
                        }

                    }
                }
            }
        } */

        storage= FirebaseStorage.getInstance()

        val httpsReference = storage.getReferenceFromUrl(user.photoUrl.toString())
        Glide.with(this).load(httpsReference).into(headerIcon)

        headerUsername.setText(user.displayName)
        headerEmail.setText(user.email)

    }
    /*
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container,false)
        return binding.root
    }
    fun test() {
        // binding 이 프로퍼티로 선언되어 있기 때문에 프래그먼트 전체에서 호출 가능
        // binding.위젯id.속성 = "값"
        binding.textView.text = "hello fragment"
    }
 */

    override fun onStart(){
        super.onStart()
        auth = Firebase.auth
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main2, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}