package com.example.bd

import android.app.Activity
import android.app.Dialog
import android.app.Instrumentation
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Patterns
import android.view.Menu
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.bd.databinding.ActivityEditarPerfilBinding
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.zip.Inflater

class EditarPerfil : AppCompatActivity() {

    //view Binding
    private lateinit var binding: ActivityEditarPerfilBinding

    //firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth

    //image uri
    private var imagegeUri:Uri ?= null

    //Progress Dialog
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditarPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //progress
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle(R.string.wait)
        progressDialog.setCanceledOnTouchOutside(false)

        //Iniciar o firebase
        firebaseAuth = FirebaseAuth.getInstance()
        carregarUtilizador()

        //quando clica na imagem de voltar para tras
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        //quando clica na foto para escolher galeria ou foto
        binding.profileIv.setOnClickListener {
            showimageMenu()
        }

        //quando clica no btn update
        binding.updateBtn.setOnClickListener {
            validarDados()
        }
    }

    private var nome = ""
    private var email = ""
    private var telemovel = ""
    private fun validarDados() {
        nome = binding.nomeEt.text.toString().trim()
        email = binding.emailEt.text.toString().trim()
        telemovel = binding.telemovelEt.text.toString().trim()

        //validar
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //email invalido
            binding.emailEt.error = "Email " + R.string.fomatInvalid
        }else if (TextUtils.isEmpty(telemovel)){
            //sem pass
            binding.telemovelEt.error = R.string.insertPhoneNumber.toString()
        }else if (telemovel.length<6){
            //sem comprimento suficiente
            binding.telemovelEt.error = R.string.insertPhoneNumber.toString()
        }else if (TextUtils.isEmpty(nome)){
            //sem nome
            binding.nomeEt.error = R.string.insertName.toString()
        }else{
            //dados validados validar imagem
            if (imagegeUri == null){
                //update sem imagem
                atualizarPerfil("")

            }else{
                //atualiar com imagem
                atualizarImagem()
            }

        }

    }

    private fun atualizarImagem() {
        progressDialog.setMessage(R.string.save.toString())
        progressDialog.show()

        // Pasta + uid do utilizador | imagem
        val filePath = "imagensPerfil/" + firebaseAuth.uid

        //referencia de armazenamento
        val reference = FirebaseStorage.getInstance().getReference(filePath)
        reference.putFile(imagegeUri!!)
            .addOnSuccessListener { taskSnapshot ->
                //Sucesso obtem a url da imagem
                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);

                val uploadedImageUrl = "${uriTask.result}"
                atualizarPerfil(uploadedImageUrl)

            }
            .addOnFailureListener{
                progressDialog.dismiss()

                //Envia um toast de erro
                Toast.makeText(this, R.string.erroImageSave, Toast.LENGTH_SHORT).show()
            }

    }

    private fun atualizarPerfil(imagemUrl: String) {
        progressDialog.setMessage(R.string.atualizar.toString())

        //envia informação para a BD
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["nome"] = "$nome"
        hashMap["email"] = "$email"
        hashMap["telemovel"] = "$telemovel"

        if (imagemUrl != null){
            hashMap["fotoPerfil"] = imagemUrl
        }

        //update
        val reference = FirebaseDatabase.getInstance().getReference("Utilizadores")
        reference.child(firebaseAuth.uid!!)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()

                //Envia um toast de erro
                Toast.makeText(this, R.string.perfilAtualizado, Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                progressDialog.dismiss()

                //Envia um toast de erro
                Toast.makeText(this, R.string.erroPerfilAtualizado, Toast.LENGTH_SHORT).show()
            }

    }

    private fun carregarUtilizador() {
        //carregar info da BD
        val ref = FirebaseDatabase.getInstance().getReference("Utilizadores")
        ref.child(firebaseAuth.uid!!)
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot){
                    //obtem os dados de facto
                    val email = "${snapshot.child("email").value}"
                    val nome = "${snapshot.child("nome").value}"
                    val fotoPerfil = "${snapshot.child("fotoPerfil").value}"
                    val telemovel = "${snapshot.child("telemovel").value}"

                    //Coloca a data nos campos
                    binding.emailEt.setText(email)
                    binding.nomeEt.setText(nome)
                    binding.telemovelEt.setText(telemovel)

                    //Caso da Foto
                    try {
                        Glide.with(this@EditarPerfil)
                            .load(fotoPerfil)
                            .placeholder(R.drawable.user)
                            .into(binding.profileIv)
                    }catch (e: Exception){

                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    private fun showimageMenu(){
        //mostrar opções

        //Popup
        val popupMenu = PopupMenu(this, binding.profileIv)
        popupMenu.menu.add(Menu.NONE, 0, 0, R.string.camera)
        popupMenu.menu.add(Menu.NONE, 1, 1, R.string.galeria)
        popupMenu.show()

        //ao clicar
        popupMenu.setOnMenuItemClickListener { item ->
            //obtem o id do clicado
            val id = item.itemId
            if (id == 0){
                //Camera
                pickImageCamera()
            }else{
                //Galeria
                pickImageGaleria()
            }

            true
        }
    }

    private fun pickImageGaleria() {
        //escolhe da galeria
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galeriaActivity.launch(intent)
    }

    private fun pickImageCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Temp_Title")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Description")

        imagegeUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imagegeUri)
        cameraActivity.launch(intent)

    }

    //utilizar a camera para substituir a imagem
    private val cameraActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult>{result ->
            //obter o uri da imagem
            if (result.resultCode == Activity.RESULT_OK){
                val data = result.data
                //imagegeUri = data!!.data nao é necessário já o possuimos no caso da camera

                //coloca a imagem
                binding.profileIv.setImageURI(imagegeUri)
            }else{
                //Cancela
                Toast.makeText(this, R.string.erroG, Toast.LENGTH_SHORT).show()
            }
        }
    )

    //Utilizar imagens da galeria
    private val galeriaActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult>{result ->
            //obter o uri da imagem
            if (result.resultCode == Activity.RESULT_OK){
                val data = result.data
                imagegeUri = data!!.data

                //coloca a imagem
                binding.profileIv.setImageURI(imagegeUri)
            }else{
                //Cancela
                Toast.makeText(this, R.string.erroG, Toast.LENGTH_SHORT).show()
            }
        }
    )
}

