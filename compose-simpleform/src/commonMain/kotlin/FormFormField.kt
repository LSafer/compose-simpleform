package net.lsafer.compose.simpleform

import androidx.compose.runtime.*

/**
 * WARNING: This is still experimental
 *
 * #### Additional notes for [value] and [latestValue]
 *
 * These fields are just form containers, actual current values or
 * latest values are stored accordingly in the fields of the forms.
 *
 * Forms obtained from these fields may get abandoned by the field.
 * Always allow snapshot to detect changes and recompose accordingly
 * when values get abandoned or replaced.
 *
 * #### Additional notes for [isDirty] and [errors]
 *
 * These fields will combine the state of this field as well as the
 * state of the fields inside the forms in [value].
 *
 * #### Additional notes for [update] and [value]
 *
 * Once forms are passed to these functions then they are
 * owned by the field and should never be passed to
 * other fields or used as independant form objects.
 *
 * #### Additional notes for [Form.isDraft] of sub-forms.
 *
 * `isDraft` of sub-forms is user responsibility.
 *
 * It is expected from the user to set [Form.isDraft] to `true`
 * before adding values directly to [value] and to set it to `false`
 * before updating latest values using [update].
 */
abstract class FormFormField<T> : FormField<T>()

/** WARNING: This is still experimental | PLEASE READ DOCS OF [FormFormField] */
class FormSingleFormField<T : Form>(
    initialValue: T,
    private val onValidate: ValidateScope<T>.() -> Unit = { },
) : FormFormField<T>() {
    override var value by mutableStateOf(initialValue)
    override var latestValue by mutableStateOf(initialValue)
        private set

    override val isDirty by derivedStateOf { value.isDirty }
    override val errors by derivedStateOf { listOfNotNull(error) + value.errors }

    override val isValid by derivedStateOf {
        val scope = ValidateScope(value)
        onValidate(scope)
        val isValid = scope.error == null
        isValid && value.isValid
    }

    override fun clear() {
        error = null
        value.clear()
    }

    override fun reset() {
        error = null
        value.reset()
    }

    override fun update(newValue: T) {
        error = null
        latestValue = newValue
    }

    override fun validate() {
        val scope = ValidateScope(value)
        onValidate(scope)
        error = scope.error
        value.validate()
    }
}

/** WARNING: This is still experimental | PLEASE READ DOCS OF [FormFormField] */
class FormMapFormField<K, V : Form>(
    initialValue: Map<K, V>,
    private val onValidate: ValidateScope<Map<K, V>>.() -> Unit = { },
) : FormFormField<Map<K, V>>() {
    override val value = mutableStateMapOf<K, V>().also { it.putAll(initialValue) }
    override var latestValue by mutableStateOf(initialValue)
        private set

    override val isDirty by derivedStateOf {
        value.toMap() != latestValue || value.values.any { it.isDirty }
    }
    override val errors by derivedStateOf {
        listOfNotNull(error) + value.values.flatMap { it.errors }
    }

    override val isValid by derivedStateOf {
        val scope = ValidateScope(value.toMap())
        onValidate(scope)
        val isValid = scope.error == null
        isValid && value.values.all { it.isValid }
    }

    override fun clear() {
        error = null
        value.clear()
    }

    override fun reset() {
        error = null
        value.clear()
        value.putAll(latestValue)
        value.values.forEach { it.reset() }
    }

    override fun update(newValue: Map<K, V>) {
        error = null
        latestValue = newValue
        value.clear()
        value.putAll(newValue)
    }

    override fun validate() {
        // No need to cache scope object here, this is only invoked on focus loss.
        val scope = ValidateScope(value.toMap())
        onValidate(scope)
        error = scope.error
        value.values.forEach { it.validate() }
    }
}

/** WARNING: This is still experimental | PLEASE READ DOCS OF [FormFormField] */
class FormListFormField<E : Form>(
    initialValue: List<E>,
    private val onValidate: ValidateScope<List<E>>.() -> Unit = { },
) : FormFormField<List<E>>() {
    override val value = mutableStateListOf<E>().also { it.addAll(initialValue) }
    override var latestValue by mutableStateOf(initialValue)
        private set

    override val isDirty by derivedStateOf {
        value.toList() != latestValue || value.any { it.isDirty }
    }
    override val errors by derivedStateOf {
        listOfNotNull(error) + value.flatMap { it.errors }
    }

    override val isValid by derivedStateOf {
        val scope = ValidateScope(value.toList())
        onValidate(scope)
        val isValid = scope.error == null
        isValid && value.all { it.isValid }
    }

    override fun clear() {
        error = null
        value.clear()
    }

    override fun reset() {
        error = null
        value.clear()
        value.addAll(latestValue)
        value.forEach { it.reset() }
    }

    override fun update(newValue: List<E>) {
        error = null
        latestValue = newValue
        value.clear()
        value.addAll(newValue)
    }

    override fun validate() {
        // No need to cache scope object here, this is only invoked on focus loss.
        val scope = ValidateScope(value.toList())
        onValidate(scope)
        error = scope.error
        value.forEach { it.validate() }
    }
}

/** WARNING: This is still experimental | PLEASE READ DOCS OF [FormFormField] */
class FormSetFormField<E : Form>(
    initialValue: Set<E>,
    private val onValidate: ValidateScope<Set<E>>.() -> Unit = { },
) : FormFormField<Set<E>>() {
    override val value = mutableStateSetOf<E>().also { it.addAll(initialValue) }
    override var latestValue by mutableStateOf(initialValue)
        private set

    override val isDirty by derivedStateOf {
        value.toSet() != latestValue || value.any { it.isDirty }
    }
    override val errors by derivedStateOf {
        listOfNotNull(error) + value.flatMap { it.errors }
    }

    override val isValid by derivedStateOf {
        val scope = ValidateScope(value.toSet())
        onValidate(scope)
        val isValid = scope.error == null
        isValid && value.all { it.isValid }
    }

    override fun clear() {
        error = null
        value.clear()
    }

    override fun reset() {
        error = null
        value.clear()
        value.addAll(latestValue)
        value.forEach { it.reset() }
    }

    override fun update(newValue: Set<E>) {
        error = null
        latestValue = newValue
        value.clear()
        value.addAll(newValue)
    }

    override fun validate() {
        // No need to cache scope object here, this is only invoked on focus loss.
        val scope = ValidateScope(value.toSet())
        onValidate(scope)
        error = scope.error
        value.forEach { it.validate() }
    }
}
