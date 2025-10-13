package net.lsafer.compose.simpleform

import net.lsafer.compose.simpleform.internal.mergeList
import net.lsafer.compose.simpleform.internal.mergeMap
import net.lsafer.compose.simpleform.internal.mergeSet

/** WARNING: This is still experimental */
interface FormCodec<T, O, in F : FormField<T>> {
    companion object

    fun get(field: F): O
    fun update(field: F, newValue: O)
}

/** WARNING: This is still experimental */
class SingleFormCodec<T, O>(
    private val encode: (T) -> O,
    private val merge: (T, O) -> T,
) : FormCodec<T, O, SingleFormField<T>> {
    override fun get(field: SingleFormField<T>): O {
        return this.encode(field.get())
    }

    override fun update(field: SingleFormField<T>, newValue: O) {
        val currentValue = field.get()
        val result = this.merge(currentValue, newValue)

        if (currentValue != result)
            field.update(result)
    }
}

/** WARNING: This is still experimental */
class MapFormCodec<K, V, Ko, Vo>(
    private val encodeItem: (K, V) -> Pair<Ko, Vo>,
    private val decodeKey: (Ko) -> K,
    private val decodeValue: (Vo) -> V,
    private val merge: (K, V, Vo) -> V,
) : FormCodec<Map<K, V>, Map<Ko, Vo>, MapFormField<K, V>> {
    override fun get(field: MapFormField<K, V>): Map<Ko, Vo> {
        return field.value.entries.associate { (k, v) ->
            this.encodeItem(k, v)
        }
    }

    override fun update(field: MapFormField<K, V>, newValue: Map<Ko, Vo>) {
        val currentValue = field.value.toMap()
        val result = mergeMap(
            origin = currentValue,
            update = newValue,
            decodeKey = this.decodeKey,
            decodeValue = this.decodeValue,
            merge = this.merge,
        )

        field.update(result)
    }
}

/** WARNING: This is still experimental */
class ListFormCodec<E, Eo>(
    private val encodeItem: (E) -> Eo,
    private val decodeItem: (Eo) -> E,
    private val match: (E, Eo) -> Boolean,
    private val merge: (E, Eo) -> E,
) : FormCodec<List<E>, List<Eo>, ListFormField<E>> {
    override fun get(field: ListFormField<E>): List<Eo> {
        return field.value.map {
            this.encodeItem(it)
        }
    }

    override fun update(field: ListFormField<E>, newValue: List<Eo>) {
        val currentValue = field.value.toList()
        val result = mergeList(
            origin = currentValue,
            update = newValue,
            decodeItem = this.decodeItem,
            match = this.match,
            merge = this.merge,
        )

        field.update(result)
    }
}

/** WARNING: This is still experimental */
class SetFormCodec<E, Eo>(
    private val encodeItem: (E) -> Eo,
    private val decodeItem: (Eo) -> E,
    private val match: (E, Eo) -> Boolean,
    private val merge: (E, Eo) -> E,
) : FormCodec<Set<E>, Set<Eo>, SetFormField<E>> {
    override fun get(field: SetFormField<E>): Set<Eo> {
        return field.value.mapTo(mutableSetOf()) {
            this.encodeItem(it)
        }
    }

    override fun update(field: SetFormField<E>, newValue: Set<Eo>) {
        val currentValue = field.value.toSet()
        val result = mergeSet(
            origin = currentValue,
            update = newValue,
            decodeItem = this.decodeItem,
            match = this.match,
            merge = this.merge,
        )

        field.update(result)
    }
}

// =============== Builtin Codecs =============== //

@Suppress("FunctionName")
fun FormCodec.Companion.SingleString(
    optional: Boolean = false,
): SingleFormCodec<String, String?> {
    return SingleFormCodec(
        encode = { if (it.isBlank()) if (optional) null else "" else it.trim() },
        merge = { _, new -> new.orEmpty() },
    )
}

@Suppress("FunctionName")
fun <T : Any> FormCodec.Companion.SingleString(
    default: T?,
    parse: (String) -> T?,
    stringify: (T) -> String,
): SingleFormCodec<String, T?> {
    return SingleFormCodec(
        encode = { if (it.isBlank()) default else parse(it.trim()) },
        merge = { _, new -> if (new == null) "" else stringify(new) },
    )
}
