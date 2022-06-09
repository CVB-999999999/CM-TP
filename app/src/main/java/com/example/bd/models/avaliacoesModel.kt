package com.example.bd.models

data class avaliacoesModel(
    var codAnuncio: String ?= null,
    var codAvaliacao: String ?= null,
    var comentario: String ?= null,
    val dataPublicacao: Long ?= null,
    val espaco: Long ?= null,
    val geral: Long ?= null,
    var idUtilizador: String ?= null,
    val proprietario: Long ?= null,
    var visiblidade: String ?= null
){

}

