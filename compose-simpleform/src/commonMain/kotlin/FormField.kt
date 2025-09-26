package net.lsafer.compose.simpleform

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester

sealed class FormField<T> {
    /**
     * The form this field belongs/bound to.
     */
    lateinit var form: Form
        internal set

    /**
     * The current value of the field. Mutated by the user.
     */
    abstract val value: T

    /**
     * The actual value the entity is currently having.
     *
     * > Can be changed via [update]
     */
    abstract val latestValue: T

    /**
     * The current validation or api error of the field.
     *
     * > Can be changed directly.
     */
    var error by mutableStateOf<String?>(null)

    /**
     * A list of the errors produced by this field and all its subfields (if any).
     */
    abstract val errors: List<String>

    /**
     * True, when the user changed the current value from the latest one.
     *
     * > UI logic should ignore this when [Form.isDraft] is true.
     */
    abstract val isDirty: Boolean

    /**
     * True, if [value] is valid according to field validation logic.
     *
     * > This is decoupled from [validate] and will respond to every value change.
     * > Field UI should use [error] instead.
     */
    abstract val isValid: Boolean

    /**
     * The focus requester set to the UI component.
     */
    val focus = FocusRequester()

    val index by derivedStateOf { form.fields.indexOf(this) }
    val previous by derivedStateOf { form.fields.getOrNull(index - 1) }
    val next by derivedStateOf { form.fields.getOrNull(index + 1) }

    /**
     * Change field value to default one.
     */
    abstract fun clear()

    /**
     * Change field value to latest one.
     */
    abstract fun reset()

    /**
     * Update latest field value to [newValue].
     */
    abstract fun update(newValue: T)

    /**
     * Run validation. This should be invoked when field loses focus.
     *
     * > This is to populate [error] with validation error and NOT for [isValid].
     */
    abstract fun validate()
}
