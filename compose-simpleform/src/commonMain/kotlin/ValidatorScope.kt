package net.lsafer.compose.simpleform

import arrow.core.raise.Raise
import arrow.core.raise.RaiseDSL
import arrow.core.raise.recover
import kotlin.jvm.JvmName

@RaiseDSL
class ValidatorScope<out T> internal constructor(
    val value: T,
    raise: Raise<FormError>
) : Raise<FormError> by raise

typealias Validator<T> = context(ValidatorScope<T>) () -> Unit

context(ctx: ValidatorScope<T>) val <T> value: T get() = ctx.value

fun <T> Validator<T>.validate(value: T): FormError? =
    recover({ invoke(ValidatorScope(value, this)); null }, { it })

fun <T> Validator<T>.isValid(value: T): Boolean =
    recover({ invoke(ValidatorScope(value, this)); true }, { false })

context(ctx: ValidatorScope<T>)
fun <T> catch(catch: (FormError) -> FormError, validator: Validator<T>) =
    recover({ validator(ValidatorScope(value, this)) }) { ctx.raise(catch(it)) }

context(ctx: ValidatorScope<*>)
fun <T> use(value: T, validator: ValidatorScope<T>.() -> Unit) =
    recover({ validator(ValidatorScope(value, this)) }) { ctx.raise(it) }

context(ctx: ValidatorScope<*>)
fun <T> use(value: T, catch: (FormError) -> FormError, validator: ValidatorScope<T>.() -> Unit) =
    recover({ validator(ValidatorScope(value, this)) }) { ctx.raise(catch(it)) }

@JvmName("each_Map")
context(ctx: ValidatorScope<Map<K, V>?>)
fun <K, V> each(validator: ValidatorScope<V>.(K) -> Unit) {
    ctx.value?.forEach { (key, value) ->
        recover({ validator(ValidatorScope(value, this), key) }) {
            ctx.raise(FormMapError(key, value, it))
        }
    }
}

@JvmName("each_List")
context(ctx: ValidatorScope<List<E>?>)
fun <E> each(validator: ValidatorScope<E>.(Int) -> Unit) {
    ctx.value?.forEachIndexed { index, element ->
        recover({ validator(ValidatorScope(element, this), index) }) {
            ctx.raise(FormListError(index, element, it))
        }
    }
}

context(ctx: ValidatorScope<Set<E>?>)
@JvmName("each_Set")
fun <E> each(validator: ValidatorScope<E>.() -> Unit) {
    ctx.value?.forEach { element ->
        recover({ validator(ValidatorScope(element, this)) }) {
            ctx.raise(FormSetError(element, it))
        }
    }
}

context(ctx: ValidatorScope<*>)
operator fun <T> FormField<T>.invoke(validator: ValidatorScope<T>.() -> Unit) {
    val field = this
    recover({ validator(ValidatorScope(field.value, this)) }) {
        ctx.raise(FormFormError(field, it))
    }
}

context(ctx: ValidatorScope<Map<K, V>?>)
operator fun <K, V> K.invoke(validator: ValidatorScope<V?>.() -> Unit) {
    val key = this
    val value = value?.get(key)
    recover({ validator(ValidatorScope(value, this)) }) {
        ctx.raise(FormMapError(key, value, it))
    }
}

context(ctx: ValidatorScope<List<E>?>)
operator fun <E> Int.invoke(validator: ValidatorScope<E?>.() -> Unit) {
    val index = this
    val element = value?.getOrNull(index)
    recover({ validator(ValidatorScope(element, this)) }) {
        ctx.raise(FormListError(index, element, it))
    }
}

context(ctx: ValidatorScope<T?>)
fun <T> ifPresent(block: ValidatorScope<T & Any>.() -> Unit) {
    if (value != null) {
        @Suppress("UNCHECKED_CAST")
        block(ctx as ValidatorScope<T & Any>)
    }
}
