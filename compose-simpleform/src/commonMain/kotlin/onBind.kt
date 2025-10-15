package net.lsafer.compose.simpleform

internal fun FormField<*>.onBind(form: Form) {
    when (this) {
        is AbstractFormField<*> -> {
            require(!this.formIsInitialized || this.form == form) {
                "Cannot bind form to an already bound field of another form."
            }
            this.form = form
        }

        is SerialFormField<*, *, *> -> this.original.onBind(form)
    }
}
