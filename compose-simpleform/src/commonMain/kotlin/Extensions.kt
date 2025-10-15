package net.lsafer.compose.simpleform

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

// group(<fields>)

/**
 * Create a new form group bound to [this] form.
 */
fun Form.group() = FieldGroup(this)

/**
 * Create a new form group bound to [this] form
 * and bind the given [fields] to both [this] form
 * and the newly created form group.
 *
 * > function follows all-or-nothing fail policy.
 *
 * @throws IllegalStateException if already bound to a different form.
 */
@OptIn(DelicateSimpleFormApi::class)
fun Form.group(vararg fields: FormField<*>): FieldGroup {
    val group = group()
    group.bind(fields.asList())
    return group
}

// val <field-name> by <field-spec>

/**
 * Bind the field passed as [this] to the form passed as [thisRef]
 * and return a readonly delegate to the bound field.
 *
 * @throws IllegalStateException if already bound to a different form.
 */
@OptIn(DelicateSimpleFormApi::class)
operator fun <T, F : FormField<T>> F.provideDelegate(
    thisRef: Form,
    property: KProperty<*>,
): ReadOnlyProperty<Any?, F> {
    thisRef.bind(this)
    return ReadOnlyProperty { _, _ -> this }
}

/**
 * Create a [FormSingleFormField] that holds the form passed as [this]
 * and bind the newly created field to the form passed as [thisRef] and
 * return a readonly delegate to the form passed as [this].
 */
@OptIn(DelicateSimpleFormApi::class)
operator fun <T : Form> T.provideDelegate(
    thisRef: Form,
    property: KProperty<*>,
): ReadOnlyProperty<Any?, T> {
    val field = FormSingleFormField(this)
    thisRef.bind(field)
    return ReadOnlyProperty { _, _ -> field.value }
}

// <groups> + <field-spec>

/**
 * Bind given [field] to both the form of this group and this group.
 *
 * @return the given field.
 * @throws IllegalStateException if already bound to a different form.
 */
@OptIn(DelicateSimpleFormApi::class)
operator fun <T, F : FormField<T>> FieldGroup.plus(field: F): F {
    bind(field)
    return field
}

/**
 * Bind given [field] to all the field groups in [this].
 *
 * @return the given field.
 * @throws IllegalArgumentException if the groups are from different forms or this list is empty.
 * @throws IllegalStateException if already bound to a different form.
 */
@OptIn(DelicateSimpleFormApi::class)
operator fun <T, F : FormField<T>> List<FieldGroup>.plus(field: F): F {
    require(isNotEmpty()) { "Group list is empty" }
    val form = first().form
    require(all { it.form == form }) { "Group list from different forms" }
    forEach { it.bind(field) }
    return field
}

operator fun FieldGroup.plus(other: FieldGroup) = listOf(this, other)
