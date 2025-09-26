package net.lsafer.compose.simpleform

import androidx.compose.runtime.*

abstract class ValueFormField<T>(
    /**
     * The default value of the field for any entity.
     */
    val defaultValue: T,
    private val onValidate: ValidateScope.(T) -> Unit = { },
) : FormField<T>() {
    override var latestValue by mutableStateOf(defaultValue)
        private set

    override val errors by derivedStateOf {
        listOfNotNull(error)
    }

    private val _isValidValidationScope = ValidateScope() // this is to prevent gc
    override val isValid by derivedStateOf {
        onValidate(_isValidValidationScope, value)
        val isValid = _isValidValidationScope.error == null
        _isValidValidationScope.error = null
        isValid
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

    override fun validate() {
        // No need to cache scope object here, this is only invoked on focus loss.
        val scope = ValidateScope()
        scope.apply { onValidate(value) }
        error = scope.error
    }
}

class SingleFormField<T>(
    defaultValue: T,
    onValidate: ValidateScope.(T) -> Unit = { },
) : ValueFormField<T>(defaultValue, onValidate) {
    override var value by mutableStateOf(defaultValue)
    override val isDirty by derivedStateOf { value != latestValue }

    override fun setValue0(newValue: T) {
        value = newValue
    }
}

class MapFormField<K, V>(
    defaultValue: Map<K, V>,
    onValidate: ValidateScope.(Map<K, V>) -> Unit = { },
) : ValueFormField<Map<K, V>>(defaultValue, onValidate) {
    override val value = mutableStateMapOf<K, V>().also { it.putAll(defaultValue) }
    override val isDirty by derivedStateOf { value.toMap() != latestValue }

    override fun setValue0(newValue: Map<K, V>) {
        value.clear()
        value.putAll(defaultValue)
    }
}

class ListFormField<E>(
    defaultValue: List<E>,
    onValidate: ValidateScope.(List<E>) -> Unit = { },
) : ValueFormField<List<E>>(defaultValue, onValidate) {
    override val value = mutableStateListOf<E>().also { it.addAll(defaultValue) }
    override val isDirty by derivedStateOf { value.toList() != latestValue }

    override fun setValue0(newValue: List<E>) {
        value.clear()
        value.addAll(defaultValue)
    }
}

class SetFormField<E>(
    defaultValue: Set<E>,
    onValidate: ValidateScope.(Set<E>) -> Unit = { },
) : ValueFormField<Set<E>>(defaultValue, onValidate) {
    override val value = mutableStateSetOf<E>().also { it.addAll(defaultValue) }
    override val isDirty by derivedStateOf { value.toSet() != latestValue }

    override fun setValue0(newValue: Set<E>) {
        value.clear()
        value.addAll(defaultValue)
    }
}
