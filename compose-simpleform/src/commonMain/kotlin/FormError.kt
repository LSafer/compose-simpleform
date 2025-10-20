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

val FormError.asOfMapOrNull get() = this as? FormMapError
val FormError.keyOfMapOrNull get() = asOfMapOrNull?.key
val FormError.valueOfMapOrNull get() = asOfMapOrNull?.value

val FormError.asOfListOrNull get() = this as? FormListError
val FormError.indexOfListOrNull get() = asOfListOrNull?.index
val FormError.elementOfListOrNull get() = asOfListOrNull?.element

val FormError.asOfSetOrNull get() = this as? FormSetError
val FormError.elementOfSetOrNull get() = asOfSetOrNull?.element

val FormError.asOfFormOrNull get() = this as? FormFormError
val FormError.fieldOfFormOrNull get() = asOfFormOrNull?.field

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
