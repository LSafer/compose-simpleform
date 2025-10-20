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

open class FormFormError(
    val field: FormField<*>,
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

val FormError.asFormOrNull get() = this as? FormFormError
val FormError.formFieldOrNull get() = asFormOrNull?.field

val FormError.isRoot: Boolean
    get() = when (this) {
        is FormMapError -> false
        is FormListError -> false
        is FormSetError -> false
        is FormFormError -> false
        else -> true
    }

fun FormError.resolve(): FormError {
    return when (this) {
        is FormMapError -> error.resolve()
        is FormListError -> error.resolve()
        is FormSetError -> error.resolve()
        is FormFormError -> error.resolve()
        else -> this
    }
}
