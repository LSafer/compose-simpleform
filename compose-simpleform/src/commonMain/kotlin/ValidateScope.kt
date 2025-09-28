package net.lsafer.compose.simpleform

class ValidateScope<T>(val value: T, var error: String? = null) {
    fun rule(condition: Boolean, message: () -> String) {
        if (!condition) error = message()
    }
}
