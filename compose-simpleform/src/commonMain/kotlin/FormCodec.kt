package net.lsafer.compose.simpleform

import net.lsafer.compose.simpleform.internal.mergeList
import net.lsafer.compose.simpleform.internal.mergeMap
import net.lsafer.compose.simpleform.internal.mergeSet
import kotlin.jvm.JvmName

/** WARNING: This is still experimental */
interface FormCodec<T, O, in F : FormField<T>> {
    fun get(field: F): O
    fun update(field: F, newValue: O)
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
    crossinline get: (F) -> O,
    crossinline update: (F, newValue: O) -> Unit,
): FormCodec<T, O, F> {
    return object : FormCodec<T, O, F> {
        override fun get(field: F): O = get(field)
        override fun update(field: F, newValue: O) = update(field, newValue)
    }
}

/** WARNING: This is still experimental */
inline fun <T, O> SingleFormCodec(
    crossinline get: (SingleFormField<T>) -> O,
    crossinline update: (SingleFormField<T>, newValue: O) -> Unit,
): SingleFormCodec<T, O> = FormCodec(get, update)

/** WARNING: This is still experimental */
inline fun <K, V, O> MapFormCodec(
    crossinline get: (MapFormField<K, V>) -> O,
    crossinline update: (MapFormField<K, V>, newValue: O) -> Unit,
): MapFormCodec<K, V, O> = FormCodec(get, update)

/** WARNING: This is still experimental */
inline fun <E, O> ListFormCodec(
    crossinline get: (ListFormField<E>) -> O,
    crossinline update: (ListFormField<E>, newValue: O) -> Unit,
): ListFormCodec<E, O> = FormCodec(get, update)

/** WARNING: This is still experimental */
inline fun <E, O> SetFormCodec(
    crossinline get: (SetFormField<E>) -> O,
    crossinline update: (SetFormField<E>, newValue: O) -> Unit,
): SetFormCodec<E, O> = FormCodec(get, update)

// =============== Builtin Merging Codecs =============== //

/** WARNING: This is still experimental */
@JvmName("SingleFormCodec_merging")
inline fun <T, O> SingleFormCodec(
    crossinline encode: (T) -> O,
    crossinline merge: (T, O) -> T,
): SingleFormCodec<T, O> {
    return SingleFormCodec(
        get = { field -> encode(field.get()) },
        update = { field, newValue ->
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
        get = { field -> encode(field.get()) },
        update = { field, newValue ->
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
        get = { field -> encode(field.get()) },
        update = { field, newValue ->
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
        get = { field -> encode(field.get()) },
        update = { field, newValue ->
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
