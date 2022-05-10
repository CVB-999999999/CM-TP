package com.example.bd

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide
import com.example.bd.databinding.ActivityDefenicoesBinding
import com.example.bd.databinding.ActivityEditarPerfilBinding
import com.example.bd.models.studentList
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Defenicoes : AppCompatActivity() {
    private lateinit var binding: ActivityDefenicoesBinding

    private lateinit var bottomNavigationView: BottomNavigationView

    //Firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDefenicoesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //btn logout
        binding.sairLL.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }

        //Bottom menu
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        //seleciona o item do menu
        bottomNavigationView.setSelectedItemId(R.id.dashboard)

        //ao clicar em um item
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.dashboard -> {
                    startActivity(Intent(this, Defenicoes::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.home -> {
                    startActivity(Intent(this, StudentList::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.favourite -> {
                    startActivity(Intent(this, StdFavoritosList::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.qrCode -> {
                    startActivity(Intent(this, StudentList::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.search -> {
                    startActivity(Intent(this, StudentList::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }

        //Ativa o modo imersivo
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        binding.criarAnuncio.setOnClickListener {
            startActivity(Intent(this, CriarQuarto::class.java))
        }

        binding.editContaLL.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
        }

        //Dark mode
        val appSettingPrefs: SharedPreferences = getSharedPreferences("AppSettingPrefs", 0)
        val sharedPrefEdit : SharedPreferences.Editor = appSettingPrefs.edit()
        val isNightModeOn: Boolean = appSettingPrefs.getBoolean("NightMode", false)

        if (isNightModeOn){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            binding.dark.setChecked(true)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            binding.dark.setChecked(false)
        }

        binding.dark.setOnClickListener{
            if (isNightModeOn){
                //Dark ativado tem de desligar
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.dark.setChecked(false)
                sharedPrefEdit.putBoolean("NightMode", false)
                sharedPrefEdit.apply()
            }else{
                //ativa darkmode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.dark.setChecked(true)
                sharedPrefEdit.putBoolean("NightMode", true)
                sharedPrefEdit.apply()
            }
        }
    }

    private fun checkUser() {
        //check if user is logged or not
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null){

            //carregar info da BD
            val ref = FirebaseDatabase.getInstance().getReference("Utilizadores")
            ref.child(firebaseAuth.uid!!)
                .addValueEventListener(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot){
                        //obtem os dados de facto
                        val email = "${snapshot.child("email").value}"
                        val nome = "${snapshot.child("nome").value}"
                        val fotoPerfil = "${snapshot.child("fotoPerfil").value}"

                        //Coloca a data nos campos
                        binding.emailUtilizadorTV.setText(email)
                        binding.nomeUtilizadorTV.setText(nome)

                        //Caso da Foto
                        try {
                            Glide.with(this@Defenicoes)
                                .load(fotoPerfil)
                                .placeholder(R.drawable.user)
                                .into(binding.profileIv)
                        }catch (e: Exception){

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })

        }else{
            //user logout
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }
}