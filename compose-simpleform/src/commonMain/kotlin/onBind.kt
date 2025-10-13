package net.lsafer.compose.simpleform

internal fun FormField<*>.onBind(form: Form) {
    when (this) {
        is AbstractFormField<*> -> this.form = form
        is SerialFormField<*, *, *> -> this.original.onBind(form)
    }
}
