package net.lsafer.compose.simpleform.internal

import net.lsafer.compose.simpleform.AbstractFormField
import net.lsafer.compose.simpleform.Form
import net.lsafer.compose.simpleform.FormField
import net.lsafer.compose.simpleform.SerialFormField

internal fun <T> FormField<T>.resolve(): AbstractFormField<T> {
    return when (this) {
        is AbstractFormField<T> -> this
        is SerialFormField<T, *, *> -> this.original.resolve()
    }
}

internal fun AbstractFormField<*>.ensureSafeToBindTo(form: Form) {
    check(!this.formIsInitialized || this.form == form) {
        "Cannot bind form to an already bound field of another form."
    }
}
