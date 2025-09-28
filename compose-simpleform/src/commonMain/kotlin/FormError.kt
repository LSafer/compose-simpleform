package net.lsafer.compose.simpleform

open class FormError(val message: String)

open class FormMapError(
    val key: Any?,
    val value: Any?,
    val error: FormError,
) : FormError(error.message)

open class FormListError(
    val index: Int,
    val element: Any?,
    val error: FormError,
) : FormError(error.message)

open class FormSetError(
    val element: Any?,
    val error: FormError,
) : FormError(error.message)
