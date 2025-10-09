package net.lsafer.compose.simpleform

typealias FormFieldSpec<T> = () -> FormField<T>

typealias SingleFormFieldSpec<T> = () -> SingleFormField<T>
typealias MapFormFieldSpec<K, V> = () -> MapFormField<K, V>
typealias ListFormFieldSpec<E> = () -> ListFormField<E>
typealias SetFormFieldSpec<E> = () -> SetFormField<E>

typealias ValueSingleFormFieldSpec<T> = () -> ValueSingleFormField<T>
typealias ValueMapFormFieldSpec<K, V> = () -> ValueMapFormField<K, V>
typealias ValueListFormFieldSpec<E> = () -> ValueListFormField<E>
typealias ValueSetFormFieldSpec<E> = () -> ValueSetFormField<E>

/** WARNING: This is still experimental | PLEASE READ DOCS OF [FormFormField] */
typealias FormSingleFormFieldSpec<T> = () -> FormSingleFormField<T>
/** WARNING: This is still experimental | PLEASE READ DOCS OF [FormFormField] */
typealias FormMapFormFieldSpec<K, V> = () -> FormMapFormField<K, V>
/** WARNING: This is still experimental | PLEASE READ DOCS OF [FormFormField] */
typealias FormListFormFieldSpec<E> = () -> FormListFormField<E>
/** WARNING: This is still experimental | PLEASE READ DOCS OF [FormFormField] */
typealias FormSetFormFieldSpec<E> = () -> FormSetFormField<E>

//

fun <T> field(
    validator: Validator<T?> = { },
): ValueSingleFormFieldSpec<T?> = {
    ValueSingleFormField(defaultValue = null, validator)
}

fun <T> field(
    defaultValue: T,
    validator: Validator<T> = { },
): ValueSingleFormFieldSpec<T> = {
    ValueSingleFormField(defaultValue, validator)
}

fun <E> fieldList(
    defaultValue: List<E> = emptyList(),
    validator: Validator<List<E>> = { },
): ValueListFormFieldSpec<E> = {
    ValueListFormField(defaultValue, validator)
}

fun <E> fieldSet(
    defaultValue: Set<E> = emptySet(),
    validator: Validator<Set<E>> = { },
): ValueSetFormFieldSpec<E> = {
    ValueSetFormField(defaultValue, validator)
}

fun <K, V> fieldMap(
    defaultValue: Map<K, V> = emptyMap(),
    validator: Validator<Map<K, V>> = { },
): ValueMapFormFieldSpec<K, V> = {
    ValueMapFormField(defaultValue, validator)
}

//

/** WARNING: This is still experimental | PLEASE READ DOCS OF [FormFormField] */
fun <T : Form> fieldForm(
    initialValue: T,
    validator: Validator<T> = { },
): FormSingleFormFieldSpec<T> = {
    FormSingleFormField(initialValue, validator)
}

/** WARNING: This is still experimental | PLEASE READ DOCS OF [FormFormField] */
fun <E : Form> fieldFormList(
    initialValue: List<E> = emptyList(),
    validator: Validator<List<E>> = { },
): FormListFormFieldSpec<E> = {
    FormListFormField(initialValue, validator)
}

/** WARNING: This is still experimental | PLEASE READ DOCS OF [FormFormField] */
fun <E : Form> fieldFormSet(
    initialValue: Set<E> = emptySet(),
    validator: Validator<Set<E>> = { },
): FormSetFormFieldSpec<E> = {
    FormSetFormField(initialValue, validator)
}

/** WARNING: This is still experimental | PLEASE READ DOCS OF [FormFormField] */
fun <K, V : Form> fieldFormMap(
    initialValue: Map<K, V> = emptyMap(),
    validator: Validator<Map<K, V>> = { },
): FormMapFormFieldSpec<K, V> = {
    FormMapFormField(initialValue, validator)
}
