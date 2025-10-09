package net.lsafer.compose.simpleform

import androidx.compose.runtime.*

sealed class ValueFormField<T>(
    /**
     * The default value of the field for any entity.
     */
    val defaultValue: T,
) : AbstractFormField<T>() {
    override var latestValue by mutableStateOf(defaultValue)
        private set

    override val errors by derivedStateOf {
        listOfNotNull(error)
    }

    protected abstract fun setValue0(newValue: T)

    override fun clear() {
        error = null
        setValue0(defaultValue)
    }

    override fun reset() {
        error = null
        setValue0(latestValue)
    }

    override fun update(newValue: T) {
        error = null
        latestValue = newValue
        setValue0(newValue)
    }
}

class ValueSingleFormField<T>(
    defaultValue: T,
    private val validator: Validator<T> = { },
) : ValueFormField<T>(defaultValue), SingleFormField<T> {
    override var value by mutableStateOf(defaultValue)
    override val isClear by derivedStateOf { value == defaultValue }
    override val isDirty by derivedStateOf { value != latestValue }

    override val isValid by derivedStateOf {
        validator.isValid(value)
    }

    override fun get() = value

    override fun setValue0(newValue: T) {
        value = newValue
    }

    override fun validate() {
        error = validator.validate(value)
    }
}

class ValueMapFormField<K, V>(
    defaultValue: Map<K, V>,
    private val validator: Validator<Map<K, V>> = { },
) : ValueFormField<Map<K, V>>(defaultValue), MapFormField<K, V> {
    override val value = mutableStateMapOf<K, V>().also { it.putAll(defaultValue) }
    override val isClear by derivedStateOf { value.toMap() == defaultValue }
    override val isDirty by derivedStateOf { value.toMap() != latestValue }

    override val isValid by derivedStateOf {
        validator.isValid(value.toMap())
    }

    override fun get() = value.toMap()

    override fun setValue0(newValue: Map<K, V>) {
        value.clear()
        value.putAll(newValue)
    }

    override fun validate() {
        error = validator.validate(value.toMap())
    }
}

class ValueListFormField<E>(
    defaultValue: List<E>,
    private val validator: Validator<List<E>> = { },
) : ValueFormField<List<E>>(defaultValue), ListFormField<E> {
    override val value = mutableStateListOf<E>().also { it.addAll(defaultValue) }
    override val isClear by derivedStateOf { value.toList() == defaultValue }
    override val isDirty by derivedStateOf { value.toList() != latestValue }

    override val isValid by derivedStateOf {
        validator.isValid(value.toList())
    }

    override fun get() = value.toList()

    override fun setValue0(newValue: List<E>) {
        value.clear()
        value.addAll(newValue)
    }

    override fun validate() {
        error = validator.validate(value.toList())
    }

    fun update(newValue: Iterable<E>) =
        update(newValue.toList())
}

class ValueSetFormField<E>(
    defaultValue: Set<E>,
    private val validator: Validator<Set<E>> = { },
) : ValueFormField<Set<E>>(defaultValue), SetFormField<E> {
    override val value = mutableStateSetOf<E>().also { it.addAll(defaultValue) }
    override val isClear by derivedStateOf { value.toSet() == defaultValue }
    override val isDirty by derivedStateOf { value.toSet() != latestValue }

    override val isValid by derivedStateOf {
        validator.isValid(value.toSet())
    }

    override fun get() = value.toSet()

    override fun setValue0(newValue: Set<E>) {
        value.clear()
        value.addAll(newValue)
    }

    override fun validate() {
        error = validator.validate(value.toSet())
    }

    fun update(newValue: Iterable<E>) =
        update(newValue.toSet())
}
