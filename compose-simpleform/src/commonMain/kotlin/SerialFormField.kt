package net.lsafer.compose.simpleform

private const val GET_DEPRECATION_MSG = "This is a field with codec, use collect() instead."
private const val UPDATE_DEPRECATION_MSG = "This is a field with codec, use accept(newValue) instead."

sealed class SerialFormField<T, O, F : FormField<T>> : FormField<T> {
    abstract val original: F
    abstract val codec: FormCodec<T, O, F>

    fun collect(): O = codec.collect(original)
    fun accept(newValue: O) = codec.accept(original, newValue)

    @Deprecated(GET_DEPRECATION_MSG)
    abstract override fun get(): T
    @Deprecated(UPDATE_DEPRECATION_MSG)
    abstract override fun update(newValue: T)
}

/** WARNING: This is still experimental */
class SerialSingleFormField<T, O, F : SingleFormField<T>>(
    override val original: F,
    override val codec: FormCodec<T, O, F>,
) : SerialFormField<T, O, F>(), SingleFormField<T> by original {
    @Deprecated(GET_DEPRECATION_MSG)
    override fun get(): T = original.get()
    @Deprecated(UPDATE_DEPRECATION_MSG)
    override fun update(newValue: T) = original.update(newValue)
}

/** WARNING: This is still experimental */
class SerialMapFormField<K, V, O, F : MapFormField<K, V>>(
    override val original: F,
    override val codec: FormCodec<Map<K, V>, O, F>,
) : SerialFormField<Map<K, V>, O, F>(), MapFormField<K, V> by original {
    @Deprecated(GET_DEPRECATION_MSG)
    override fun get(): Map<K, V> = original.get()
    @Deprecated(UPDATE_DEPRECATION_MSG)
    override fun update(newValue: Map<K, V>) = original.update(newValue)
}

/** WARNING: This is still experimental */
class SerialListFormField<E, O, F : ListFormField<E>>(
    override val original: F,
    override val codec: FormCodec<List<E>, O, F>,
) : SerialFormField<List<E>, O, F>(), ListFormField<E> by original {
    @Deprecated(GET_DEPRECATION_MSG)
    override fun get(): List<E> = original.get()
    @Deprecated(UPDATE_DEPRECATION_MSG)
    override fun update(newValue: List<E>) = original.update(newValue)
}

/** WARNING: This is still experimental */
class SerialSetFormField<E, O, F : SetFormField<E>>(
    override val original: F,
    override val codec: FormCodec<Set<E>, O, F>,
) : SerialFormField<Set<E>, O, F>(), SetFormField<E> by original {
    @Deprecated(GET_DEPRECATION_MSG)
    override fun get(): Set<E> = original.get()
    @Deprecated(UPDATE_DEPRECATION_MSG)
    override fun update(newValue: Set<E>) = original.update(newValue)
}
