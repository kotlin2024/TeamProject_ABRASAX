package com.teamsparta.abrasax.domain.exception

class DeleteNotAllowedException(model: String, id: Long) : RuntimeException("$model with id $id cannot be deleted")