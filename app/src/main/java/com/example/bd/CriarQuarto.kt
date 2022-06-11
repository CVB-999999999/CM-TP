package com.example.bd

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.bd.databinding.ActivityCriarQuartoBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.HashMap

class CriarQuarto : AppCompatActivity() {

    //view Binding
    private lateinit var binding: ActivityCriarQuartoBinding

    //firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth


    //Progress Dialog
    private lateinit var progressDialog: ProgressDialog

    //image uri
    private var imagegeUri:Uri ?= null

    private var imagem = 0

    private val codAnuncio = UUID.randomUUID().toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCriarQuartoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //progress
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle(R.string.wait)
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

        //quando clica no btn de escolher imagens
        binding.addImgBtn.setOnClickListener {
            //escolhe da galeria
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            galeriaActivity.launch(intent)
        }

        //quando clica no btn de guardar
        binding.publicar.setOnClickListener {
            validarDados(0) // 0 - Publicar || 1 - Previsualizar
        }

        //Ativa o modo imersivo (Para mais Info Ide pesquisar na documentação do androi studio)
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
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
    private var preco = ""
    private var partilhado = false

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
        preco = binding.precoET.text.toString().trim()
        partilhado = binding.partilhadoCheck.isChecked

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
            binding.emailEt.error = "Email " + R.string.fomatInvalid
        }else if (TextUtils.isEmpty(telemovel)){
            //sem telemovel
            binding.telemovelEt.error = R.string.insertPhoneNumber.toString()
        }else if (telemovel.length<6){
            //sem comprimento suficiente
            binding.telemovelEt.error = R.string.insertPhoneNumber.toString()
        }else if (TextUtils.isEmpty(titulo)){
            //sem titulo
            binding.tituloEt.error = R.string.insertTitle.toString()
        }else if (TextUtils.isEmpty(descricao)){
            //sem descricao
            binding.descricaoEt.error = R.string.insertDescription.toString()
        }else if (TextUtils.isEmpty(morada)){
            //sem morada
            binding.moradaEt.error = R.string.insertAddress.toString()
        }else if(binding.masculino.isChecked==false && binding.feminino.isChecked==false && binding.indiferente.isChecked==false){
            binding.indiferente.error = R.string.selectOp.toString()
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
        progressDialog.setMessage(R.string.save.toString())

        val timeStamp = System.currentTimeMillis()
        val uId = firebaseAuth.uid

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
        hashMap["partilhado"]=partilhado
        hashMap["morada"]=morada
        hashMap["visiblidade"]="1" // visiblidade do anuncio 0-oculto || 1-visivel
        hashMap["dataCriacao"]=timeStamp
        hashMap["dataAtualizacao"]=timeStamp
        hashMap["preco"]=preco

        //guardar td
        val ref = FirebaseDatabase.getInstance().getReference("Anuncios")
        ref.child(codAnuncio!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                //caso de sucesso
                progressDialog.dismiss()
                Toast.makeText(this, R.string.anuncioRegistado, Toast.LENGTH_SHORT).show()

                //volta para a pagina inicial, neste momento vai para o perfil novamente
                startActivity(Intent(this, Profile::class.java))
                finish()
            }
            .addOnFailureListener {
                //caso de fail
                progressDialog.dismiss()
                Toast.makeText(this, R.string.registerFail, Toast.LENGTH_SHORT).show()
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

    //Utilizar imagens da galeria
    private val galeriaActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult>{result ->
            //obter o uri da imagem
            if (result.resultCode == Activity.RESULT_OK){
                var codImagem = UUID.randomUUID().toString()

                val data = result.data
                imagegeUri = data!!.data

                //coloca a imagem
                //binding.profileIv.setImageURI(imagegeUri)

                progressDialog.setMessage(R.string.save.toString())
                progressDialog.show()

                // Pasta + codImagem | imagem
                val filePath = "imagensAnuncios/" + codImagem

                //referencia de armazenamento
                val reference = FirebaseStorage.getInstance().getReference(filePath)
                reference.putFile(imagegeUri!!)
                    .addOnSuccessListener { taskSnapshot ->
                        progressDialog.dismiss()
                        //Sucesso obtem a url da imagem
                        val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                        while (!uriTask.isSuccessful);

                        val uploadedImageUrl = "${uriTask.result}"
                        //atualizarPerfil(uploadedImageUrl)
                        imagem=1

                        gravaImagemRealTime(codImagem, uploadedImageUrl)
                    }
                    .addOnFailureListener{
                        progressDialog.dismiss()

                        //Envia um toast de erro
                        Toast.makeText(this, R.string.erroImageSave, Toast.LENGTH_SHORT).show()
                    }

            }else{
                //Cancela
                Toast.makeText(this, R.string.erroG, Toast.LENGTH_SHORT).show()
            }
        }
    )

    private fun gravaImagemRealTime(codImagem: String, imageUrl: String) {
        //envia informação para a BD
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["codImagem"] = codImagem
        hashMap["codAnuncio"] = codAnuncio
        hashMap["imagemURL"] = imageUrl


        //update
        val reference = FirebaseDatabase.getInstance().getReference("ImagensAnuncios")
        reference.child(codImagem!!)
            .updateChildren(hashMap)
            .addOnSuccessListener {

                //Envia um toast
                Toast.makeText(this, R.string.imageSave, Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                progressDialog.dismiss()

                //Envia um toast de erro
                Toast.makeText(this, R.string.erroImageSave, Toast.LENGTH_SHORT).show()
            }
    }


}