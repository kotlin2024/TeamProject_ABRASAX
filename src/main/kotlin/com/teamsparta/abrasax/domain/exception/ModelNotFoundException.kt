package com.teamsparta.abrasax.domain.exception

data class ModelNotFoundException(val modelName: String, val id: Long) :
    RuntimeException("$modelName Not Found with id $id")