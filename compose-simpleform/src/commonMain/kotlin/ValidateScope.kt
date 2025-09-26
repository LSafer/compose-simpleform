package net.lsafer.compose.simpleform

class ValidateScope(var error: String? = null) {
    fun rule(condition: Boolean, message: () -> String) {
        if (!condition) error = message()
    }
}
