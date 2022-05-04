package com.example.bd

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.bd.databinding.ActivityCriarQuartoBinding
import com.example.bd.databinding.ActivityEditarPerfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.HashMap

class CriarQuarto : AppCompatActivity() {

    //view Binding
    private lateinit var binding: ActivityCriarQuartoBinding

    //firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth


    //Progress Dialog
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCriarQuartoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //progress
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Por favor aguarde")
        progressDialog.setCanceledOnTouchOutside(false)

        //Iniciar o firebase
        firebaseAuth = FirebaseAuth.getInstance()
        carregarUtilizador()

        //quando clica na imagem de voltar para tras
        //binding.backBtn.setOnClickListener {
        //    onBackPressed()
        //}

        //quando clica na foto para escolher galeria ou foto
        //binding.profileIv.setOnClickListener {
        //    showimageMenu()
        //}

        //quando clica no btn de guardar
        binding.publicar.setOnClickListener {
            validarDados(0) // 0 - Publicar || 1 - Previsualizar
        }
    }

    private var titulo = ""
    private var descricao = ""
    private var email = ""
    private var telemovel = ""
    private var rFumadores = false
    private var rAnimais = false
    private var rAcessivel = false
    private var rPreco = false
    private var reservado = ""
    private var morada = ""

    //valida dos dados antes de publicar
    private fun validarDados(pub: Int) {
        titulo = binding.tituloEt.text.toString().trim()
        descricao = binding.descricaoEt.text.toString().trim()
        email = binding.emailEt.text.toString().trim()
        telemovel = binding.telemovelEt.text.toString().trim()
        rFumadores = binding.check1.isChecked
        rAnimais = binding.check2.isChecked
        rAcessivel = binding.check3.isChecked
        rPreco = binding.check4.isChecked
        morada = binding.moradaEt.text.toString().trim()

        //Define se o quarto é especifico para algum tipo de sexo
        //0 - Masculino
        //1 - Feminino
        //2 - Indiferente
        if (binding.masculino.isChecked){
            reservado = "0"
        }else if (binding.feminino.isChecked){
            reservado = "1"
        }else{
            reservado = "2"
        }

        //validar
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //email invalido
            binding.emailEt.error = "Email no formato inválido"
        }else if (TextUtils.isEmpty(telemovel)){
            //sem telemovel
            binding.telemovelEt.error = "Por favor insira um contacto"
        }else if (telemovel.length<6){
            //sem comprimento suficiente
            binding.telemovelEt.error = "Por favor insira um numero válido"
        }else if (TextUtils.isEmpty(titulo)){
            //sem titulo
            binding.tituloEt.error = "Por favor insira um titulo"
        }else if (TextUtils.isEmpty(descricao)){
            //sem descricao
            binding.descricaoEt.error = "Por favor insira uma descricao"
        }else if (TextUtils.isEmpty(morada)){
            //sem morada
            binding.moradaEt.error = "Por favor insira uma morada"
        }else if(binding.masculino.isChecked==false && binding.feminino.isChecked==false && binding.indiferente.isChecked==false){
            binding.indiferente.error = "Por favor selecione uma das opcções"
        }else{
            if (pub==0){  //submter os dados para a bd
                guardaInfo()
            }else{  //pre visulaizar o anuncio
                //para já nao faz nada
            }
        }
    }

    private fun guardaInfo() {
        //guarda os dados no real time database
        progressDialog.setMessage("A guardar a informação...")

        val timeStamp = System.currentTimeMillis()
        val uId = firebaseAuth.uid
        val codAnuncio = UUID.randomUUID().toString()

        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["codAnuncio"]=codAnuncio
        hashMap["idUtilizador"]=uId
        hashMap["titulo"]=titulo
        hashMap["descricao"]=descricao
        hashMap["email"]=email
        hashMap["telemovel"]=telemovel
        hashMap["rFumadores"]=rFumadores
        hashMap["rAnimais"]=rAnimais
        hashMap["rAcessivel"]=rAcessivel
        hashMap["rPreco"]=rPreco
        hashMap["reservado"]=reservado
        hashMap["morada"]=morada
        hashMap["dataCriacao"]=timeStamp
        hashMap["dataAtualizacao"]=timeStamp

        //guardar td
        val ref = FirebaseDatabase.getInstance().getReference("Anuncios")
        ref.child(codAnuncio!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                //caso de sucesso
                progressDialog.dismiss()
                Toast.makeText(this, "Anuncio $codAnuncio registado com sucesso", Toast.LENGTH_SHORT).show()

                //volta para a pagina inicial, neste momento vai para o perfil novamente
                startActivity(Intent(this, Profile::class.java))
                finish()
            }
            .addOnFailureListener { e->
                //caso de fail
                progressDialog.dismiss()
                Toast.makeText(this, "O registou falhou no ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    //carrega os dados da base de dados do utilizador
    private fun carregarUtilizador() {
        //carregar info da BD
        val ref = FirebaseDatabase.getInstance().getReference("Utilizadores")
        ref.child(firebaseAuth.uid!!)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot){
                    //obtem os dados de facto
                    val email = "${snapshot.child("email").value}"
                    val telemovel = "${snapshot.child("telemovel").value}"

                    //Coloca a data nos campos
                    binding.emailEt.setText(email)
                    binding.telemovelEt.setText(telemovel)

                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}