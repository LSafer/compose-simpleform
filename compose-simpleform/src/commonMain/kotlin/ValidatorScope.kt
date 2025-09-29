package net.lsafer.compose.simpleform

import arrow.core.raise.Raise
import arrow.core.raise.RaiseDSL
import arrow.core.raise.recover
import kotlin.jvm.JvmName

@RaiseDSL
class ValidatorScope<T> internal constructor(
    val value: T,
    raise: Raise<FormError>
) : Raise<FormError> by raise

typealias Validator<T> = context(ValidatorScope<T>) () -> Unit

context(ctx: ValidatorScope<T>) val <T> value: T get() = ctx.value

fun <T> Validator<T>.validate(value: T): FormError? =
    recover({ invoke(ValidatorScope(value, this)); null }, { it })

fun <T> Validator<T>.isValid(value: T): Boolean =
    recover({ invoke(ValidatorScope(value, this)); true }, { false })

@JvmName("each_Map")
context(ctx: ValidatorScope<Map<K, V>>)
fun <K, V> each(validator: ValidatorScope<V>.(K) -> Unit) {
    ctx.value.forEach { (key, value) ->
        recover({ validator(ValidatorScope(value, this), key) }) {
            ctx.raise(FormMapError(key, value, it))
        }
    }
}

@JvmName("each_List")
context(ctx: ValidatorScope<List<E>>)
fun <E> each(validator: ValidatorScope<E>.(Int) -> Unit) {
    ctx.value.forEachIndexed { index, element ->
        recover({ validator(ValidatorScope(element, this), index) }) {
            ctx.raise(FormListError(index, element, it))
        }
    }
}

context(ctx: ValidatorScope<Set<E>>)
@JvmName("each_Set")
fun <E> each(validator: ValidatorScope<E>.() -> Unit) {
    ctx.value.forEach { element ->
        recover({ validator(ValidatorScope(element, this)) }) {
            ctx.raise(FormSetError(element, it))
        }
    }
}
