package net.lsafer.compose.simpleform

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import net.lsafer.compose.simpleform.internal.resolve

class FieldGroup(val form: Form) {
    private val _fields = mutableStateListOf<AbstractFormField<*>>()
    val fields: List<FormField<*>> get() = _fields

    /**
     * Bind given [field] to both the form of this group and this group.
     *
     * @throws IllegalStateException if already bound to a different form.
     */
    @DelicateSimpleFormApi
    fun bind(field: FormField<*>) {
        val base = field.resolve()
        if (base !in _fields) {
            form.bind(base)
            _fields.add(base)
        }
    }

    /**
     * Bind given [fields] to both the form of this group and this group.
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
        form.bind(baseList)
        _fields.addAll(baseList)
    }

    val errors by derivedStateOf {
        _fields.flatMap { it.errors }
    }
    val isClear by derivedStateOf {
        _fields.fold(true) { result, field ->
            field.isClear && result
        }
    }
    val isDirty by derivedStateOf {
        _fields.fold(false) { result, field ->
            field.isDirty || result
        }
    }
    val isValid by derivedStateOf {
        _fields.fold(true) { result, field ->
            field.isValid && result
        }
    }

    val isSubmittable get() = isValid && (form.isDraft || isDirty)

    fun reset() = _fields.forEach { it.reset() }
    fun clear() = _fields.forEach { it.clear() }
    fun validate() = _fields.forEach { it.validate() }
}
