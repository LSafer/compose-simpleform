package net.lsafer.compose.simpleform

sealed class SerialFormField<T, O, F : FormField<T>> : FormField<T> {
    abstract val original: F
    abstract val codec: FormCodec<T, O, F>

    fun collect(): O = codec.get(original)
    fun accept(newValue: O) = codec.update(original, newValue)
}

/** WARNING: This is still experimental */
class SerialSingleFormField<T, O, F : SingleFormField<T>>(
    override val original: F,
    override val codec: FormCodec<T, O, F>,
) : SerialFormField<T, O, F>(), SingleFormField<T> by original

/** WARNING: This is still experimental */
class SerialMapFormField<K, V, O, F : MapFormField<K, V>>(
    override val original: F,
    override val codec: FormCodec<Map<K, V>, O, F>,
) : SerialFormField<Map<K, V>, O, F>(), MapFormField<K, V> by original

/** WARNING: This is still experimental */
class SerialListFormField<E, O, F : ListFormField<E>>(
    override val original: F,
    override val codec: FormCodec<List<E>, O, F>,
) : SerialFormField<List<E>, O, F>(), ListFormField<E> by original

/** WARNING: This is still experimental */
class SerialSetFormField<E, O, F : SetFormField<E>>(
    override val original: F,
    override val codec: FormCodec<Set<E>, O, F>,
) : SerialFormField<Set<E>, O, F>(), SetFormField<E> by original
