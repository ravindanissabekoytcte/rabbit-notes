package com.Kolqamo5.Vanmow.domain.model

enum class PatchKind {
    CARROT,
    CHARM,
    FOX
}

data class Patch(
    val row: Int,
    val column: Int,
    val kind: PatchKind,
    val revealed: Boolean = false
)
