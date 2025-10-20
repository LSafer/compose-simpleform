package net.lsafer.compose.simpleform

import androidx.compose.runtime.*
import net.lsafer.compose.simpleform.internal.ensureSafeToBindTo
import net.lsafer.compose.simpleform.internal.resolve

abstract class Form(isDraft: Boolean = false) {
    private val _fields = mutableStateListOf<AbstractFormField<*>>()
    val fields: List<FormField<*>> get() = _fields

    /**
     * Bind given [field] to this form.
     *
     * @throws IllegalStateException if already bound to a different form.
     */
    @DelicateSimpleFormApi
    fun bind(field: FormField<*>) {
        val base = field.resolve()
        if (base !in _fields) {
            base.ensureSafeToBindTo(this)
            base.form = this
            _fields.add(base)
        }
    }

    /**
     * Bind given [fields] to this form.
     *
     * > function follows all-or-nothing fail policy.
     *
     * @throws IllegalStateException if already bound to a different form.
     */
    @DelicateSimpleFormApi
    fun bind(fields: List<FormField<*>>) {
        val baseList = fields
            .map { it.resolve() }
            .filter { it !in _fields }
        for (base in baseList) {
            base.ensureSafeToBindTo(this)
        }
        for (base in baseList) {
            base.form = this
            _fields.add(base)
        }
    }

    /**
     * True, indicating that the form is for creating an entity.
     *
     * Field UI Logic should ignore `field.isDirty` when this is true.
     */
    var isDraft by mutableStateOf(isDraft)

    /**
     * A list of all error messages of fields.
     */
    val errors by derivedStateOf {
        _fields.flatMap { f ->
            f.errors.map { FormFormError(f, it) }
        }
    }

    /**
     * True, if all the fields in this form are set to their default values.
     */
    val isClear by derivedStateOf {
        _fields.fold(true) { r, f ->
            f.isClear && r
        }
    }

    /**
     * True, indicating that at least one field has changed from latest value.
     *
     * > UI logic should ignore this when [isDraft] is true.
     */
    val isDirty by derivedStateOf {
        _fields.fold(false) { r, f ->
            f.isDirty || r
        }
    }

    /**
     * True, indicating that all fields validation passed.
     */
    val isValid by derivedStateOf {
        _fields.fold(true) { r, f ->
            f.isValid && r
        }
    }

    /**
     * True, when the form is ready to be submitted.
     */
    val isSubmittable get() = isValid && (isDraft || isDirty)

    /**
     * Change all fields to their latest values.
     */
    fun reset() {
        _fields.forEach { it.reset() }
    }

    /**
     * Change all fields to their default values.
     */
    fun clear() {
        _fields.forEach { it.clear() }
    }

    /**
     * Run validation for all the fields.
     */
    fun validate() {
        _fields.forEach { it.validate() }
    }
}
