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

val FormError.asMapOrNull get() = this as? FormMapError
val FormError.mapKeyOrNull get() = asMapOrNull?.key
val FormError.mapValueOrNull get() = asMapOrNull?.value

val FormError.asListOrNull get() = this as? FormListError
val FormError.listIndexOrNull get() = asListOrNull?.index
val FormError.listElementOrNull get() = asListOrNull?.element

val FormError.asSetOrNull get() = this as? FormSetError
val FormError.setElementOrNull get() = asSetOrNull?.element

fun FormError.resolve(): FormError {
    return when (this) {
        is FormMapError -> error.resolve()
        is FormListError -> error.resolve()
        is FormSetError -> error.resolve()
        else -> this
    }
}
