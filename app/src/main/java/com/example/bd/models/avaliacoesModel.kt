package com.example.bd.models

data class avaliacoesModel(
    var codAnuncio: String ?= null,
    var codAvaliacao: String ?= null,
    var comentario: String ?= null,
    val dataPublicacao: Long ?= null,
    val espaco: Double ?= null,
    val geral: Double ?= null,
    var idUtilizador: String ?= null,
    val proprietario: Double ?= null,
    var visiblidade: String ?= null
){

}

