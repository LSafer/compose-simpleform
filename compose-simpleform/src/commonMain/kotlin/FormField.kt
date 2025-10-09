package net.lsafer.compose.simpleform

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.snapshots.SnapshotStateSet
import androidx.compose.ui.focus.FocusRequester

sealed interface FormField<T> {
    /**
     * The form this field belongs/bound to.
     */
    val form: Form

    /**
     * The current value of the field. Mutated by the user.
     */
    val value: T

    /**
     * The actual value the entity is currently having.
     *
     * > Can be changed via [update]
     */
    val latestValue: T

    /**
     * The current validation or api error of the field.
     *
     * > Can be changed directly.
     */
    var error: FormError?

    /**
     * A list of the errors produced by this field and all its subfields (if any).
     */
    val errors: List<FormError>

    /**
     * True, if [value] is set to the default value.
     */
    val isClear: Boolean

    /**
     * True, when the user changed the current value from the latest one.
     *
     * > UI logic should ignore this when [Form.isDraft] is true.
     */
    val isDirty: Boolean

    /**
     * True, if [value] is valid according to field validation logic.
     *
     * > This is decoupled from [validate] and will respond to every value change.
     * > Field UI should use [error] instead.
     */
    val isValid: Boolean

    /**
     * The focus requester set to the UI component.
     */
    val focus: FocusRequester

    val index: Int
    val previous: FormField<*>?
    val next: FormField<*>?

    /**
     * Return non-mutable-state instance of [value].
     */
    fun get(): T

    /**
     * Change field value to default one.
     */
    fun clear()

    /**
     * Change field value to latest one.
     */
    fun reset()

    /**
     * Update latest field value to [newValue].
     */
    fun update(newValue: T)

    /**
     * Run validation. This should be invoked when field loses focus.
     *
     * > This is to populate [error] with validation error and NOT for [isValid].
     */
    fun validate()
}

sealed interface SingleFormField<T> : FormField<T> {
    override var value: T
}

sealed interface MapFormField<K, V> : FormField<Map<K, V>> {
    override val value: SnapshotStateMap<K, V>
}

sealed interface ListFormField<E> : FormField<List<E>> {
    override val value: SnapshotStateList<E>
}

sealed interface SetFormField<E> : FormField<Set<E>> {
    override val value: SnapshotStateSet<E>
}

sealed class AbstractFormField<T> : FormField<T> {
    override lateinit var form: Form
        internal set

    override var error by mutableStateOf<FormError?>(null)
    override val focus = FocusRequester()

    override val index by derivedStateOf { form.fields.indexOf(this) }
    override val previous by derivedStateOf { form.fields.getOrNull(index - 1) }
    override val next by derivedStateOf { form.fields.getOrNull(index + 1) }
}
