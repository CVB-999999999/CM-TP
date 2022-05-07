package com.example.bd.models

data class studentList(
    //val title: String ?= null,
    //val address: String ?= null,
    //val rent: Float ?= null,
    //val shared: Boolean = false,
    //val smoke: Boolean = false,
    //val accessible: Boolean = false,
    //val sex: Int ?= null,
    //val rooms: Int ?= null

val codAnuncio: String ?= null,
val dataAtualizacao: Long ?= null,
val dataCriacao: Long ?= null,
val descricao: String ?= null,
val email: String ?= null,
val idUtilizador: String ?= null,
val morada : String ?= null,
val preco: String ?= null,
val rAcessivel: Boolean = false,
val rAnimais: Boolean = false,
val rFumadores: Boolean = false,
val rPreco: Boolean = false,
val reservado: String ?= null,
val telemovel: String ?= null,
val titulo: String ?= null,
val visiblidade: String ?= null
){

}