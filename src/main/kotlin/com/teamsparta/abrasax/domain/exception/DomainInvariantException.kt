package com.teamsparta.abrasax.domain.exception

open class DomainInvariantException(message: String) : RuntimeException(message) {
    class InvalidTitleException(message: String) : DomainInvariantException(message)
    class InvalidContentException(message: String) : DomainInvariantException(message)
    class InvalidTagSizeException(message: String) : DomainInvariantException(message)
    class InvalidTagLengthException(message: String) : DomainInvariantException(message)
    class InvalidDuplicateTagException(message: String) : DomainInvariantException(message)
}