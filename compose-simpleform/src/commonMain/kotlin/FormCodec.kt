package net.lsafer.compose.simpleform

import net.lsafer.compose.simpleform.internal.mergeList
import net.lsafer.compose.simpleform.internal.mergeMap
import net.lsafer.compose.simpleform.internal.mergeSet
import kotlin.jvm.JvmName

/** WARNING: This is still experimental */
interface FormCodec<T, O, in F : FormField<T>> {
    fun collect(field: F): O
    fun accept(field: F, newValue: O)
}

/** WARNING: This is still experimental */
typealias SingleFormCodec<T, O> = FormCodec<T, O, SingleFormField<T>>
/** WARNING: This is still experimental */
typealias MapFormCodec<K, V, O> = FormCodec<Map<K, V>, O, MapFormField<K, V>>
/** WARNING: This is still experimental */
typealias ListFormCodec<E, O> = FormCodec<List<E>, O, ListFormField<E>>
/** WARNING: This is still experimental */
typealias SetFormCodec<E, O> = FormCodec<Set<E>, O, SetFormField<E>>

// =============== Builtin Lambda Codecs =============== //

/** WARNING: This is still experimental */
inline fun <T, O, F : FormField<T>> FormCodec(
    crossinline collect: (F) -> O,
    crossinline accept: (F, newValue: O) -> Unit,
): FormCodec<T, O, F> {
    return object : FormCodec<T, O, F> {
        override fun collect(field: F): O = collect(field)
        override fun accept(field: F, newValue: O) = accept(field, newValue)
    }
}

/** WARNING: This is still experimental */
inline fun <T, O> SingleFormCodec(
    crossinline collect: (SingleFormField<T>) -> O,
    crossinline accept: (SingleFormField<T>, newValue: O) -> Unit,
): SingleFormCodec<T, O> = FormCodec(collect, accept)

/** WARNING: This is still experimental */
inline fun <K, V, O> MapFormCodec(
    crossinline collect: (MapFormField<K, V>) -> O,
    crossinline accept: (MapFormField<K, V>, newValue: O) -> Unit,
): MapFormCodec<K, V, O> = FormCodec(collect, accept)

/** WARNING: This is still experimental */
inline fun <E, O> ListFormCodec(
    crossinline collect: (ListFormField<E>) -> O,
    crossinline accept: (ListFormField<E>, newValue: O) -> Unit,
): ListFormCodec<E, O> = FormCodec(collect, accept)

/** WARNING: This is still experimental */
inline fun <E, O> SetFormCodec(
    crossinline collect: (SetFormField<E>) -> O,
    crossinline accept: (SetFormField<E>, newValue: O) -> Unit,
): SetFormCodec<E, O> = FormCodec(collect, accept)

// =============== Builtin Merging Codecs =============== //

/** WARNING: This is still experimental */
@JvmName("SingleFormCodec_merging")
inline fun <T, O> SingleFormCodec(
    crossinline encode: (T) -> O,
    crossinline merge: (T, O) -> T,
): SingleFormCodec<T, O> {
    return SingleFormCodec(
        collect = { field -> encode(field.get()) },
        accept = { field, newValue ->
            val currentValue = field.get()
            val result = merge(currentValue, newValue)

            if (currentValue != result)
                field.update(result)
        },
    )
}

/** WARNING: This is still experimental */
@JvmName("MapFormCodec_merging")
fun <K, V, Ko, Vo> MapFormCodec(
    encode: (Map<K, V>) -> Map<Ko, Vo>,
    decodeKey: (Ko) -> K,
    decodeValue: (Vo) -> V,
    merge: (V, Vo) -> V,
): MapFormCodec<K, V, Map<Ko, Vo>> {
    return MapFormCodec(
        collect = { field -> encode(field.get()) },
        accept = { field, newValue ->
            val currentValue = field.value.toMap()
            val result = mergeMap(
                origin = currentValue,
                update = newValue,
                decodeKey = decodeKey,
                decodeValue = decodeValue,
                merge = merge,
            )

            field.update(result)
        },
    )
}

/** WARNING: This is still experimental */
@JvmName("ListFormCodec_merging")
fun <E, Eo> ListFormCodec(
    encode: (List<E>) -> List<Eo>,
    decodeItem: (Eo) -> E,
    match: (E, Eo) -> Boolean,
    merge: (E, Eo) -> E,
): ListFormCodec<E, List<Eo>> {
    return ListFormCodec(
        collect = { field -> encode(field.get()) },
        accept = { field, newValue ->
            val currentValue = field.value.toList()
            val result = mergeList(
                origin = currentValue,
                update = newValue,
                decodeItem = decodeItem,
                match = match,
                merge = merge,
            )

            field.update(result)
        },
    )
}

/** WARNING: This is still experimental */
@JvmName("SetFormCodec_merging")
fun <E, Eo> SetFormCodec(
    encode: (Set<E>) -> Set<Eo>,
    decodeItem: (Eo) -> E,
    match: (E, Eo) -> Boolean,
    merge: (E, Eo) -> E,
): SetFormCodec<E, Set<Eo>> {
    return SetFormCodec(
        collect = { field -> encode(field.get()) },
        accept = { field, newValue ->
            val currentValue = field.value.toSet()
            val result = mergeSet(
                origin = currentValue,
                update = newValue,
                decodeItem = decodeItem,
                match = match,
                merge = merge,
            )

            field.update(result)
        },
    )
}
