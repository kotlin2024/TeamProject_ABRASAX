package com.teamsparta.abrasax.domain.exception

open class ModelInvariantException(message: String) : RuntimeException(message) {
    class InvalidTitleException(message: String) : ModelInvariantException(message)
    class InvalidContentException(message: String) : ModelInvariantException(message)
    class InvalidTagSizeException(message: String) : ModelInvariantException(message)
    class InvalidTagLengthException(message: String) : ModelInvariantException(message)
    class InvalidDuplicateTagException(message: String) : ModelInvariantException(message)
}