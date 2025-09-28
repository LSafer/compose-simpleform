package net.lsafer.compose.simpleform

import arrow.core.raise.Raise
import arrow.core.raise.recover
import kotlin.jvm.JvmName

class ValidatorScope<T> internal constructor(
    val value: T,
    raise: Raise<FormError>
) : Raise<FormError> by raise

typealias Validator<T> = ValidatorScope<T>.() -> Unit

fun <T> Validator<T>.validate(value: T): FormError? =
    recover({ invoke(ValidatorScope(value, this)); null }, { it })

fun <T> Validator<T>.isValid(value: T): Boolean =
    recover({ invoke(ValidatorScope(value, this)); true }, { false })

@JvmName("each_Map")
fun <K, V> ValidatorScope<Map<K, V>>.each(
    validator: ValidatorScope<V>.(K) -> Unit,
) {
    value.forEach { (key, value) ->
        recover({ validator(ValidatorScope(value, this), key) }) {
            raise(FormMapError(key, value, it))
        }
    }
}

@JvmName("each_List")
fun <E> ValidatorScope<List<E>>.each(
    validator: ValidatorScope<E>.(Int) -> Unit
) {
    value.forEachIndexed { index, element ->
        recover({ validator(ValidatorScope(element, this), index) }) {
            raise(FormListError(index, element, it))
        }
    }
}

@JvmName("each_Set")
fun <E> ValidatorScope<Set<E>>.each(
    validator: ValidatorScope<E>.() -> Unit
) {
    value.forEach { element ->
        recover({ validator(ValidatorScope(element, this)) }) {
            raise(FormSetError(element, it))
        }
    }
}
