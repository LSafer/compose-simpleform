package net.lsafer.compose.simpleform

typealias FormFieldSpec<T> = () -> FormField<T>

typealias SingleFormFieldSpec<T> = () -> SingleFormField<T>
typealias MapFormFieldSpec<K, V> = () -> MapFormField<K, V>
typealias ListFormFieldSpec<E> = () -> ListFormField<E>
typealias SetFormFieldSpec<E> = () -> SetFormField<E>

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
    onValidate: ValidateScope<T?>.() -> Unit = { },
): SingleFormFieldSpec<T?> = {
    SingleFormField(defaultValue = null, onValidate)
}

fun <T> field(
    defaultValue: T,
    onValidate: ValidateScope<T>.() -> Unit = { },
): SingleFormFieldSpec<T> = {
    SingleFormField(defaultValue, onValidate)
}

fun <E> fieldList(
    defaultValue: List<E> = emptyList(),
    onValidate: ValidateScope<List<E>>.() -> Unit = { },
): ListFormFieldSpec<E> = {
    ListFormField(defaultValue, onValidate)
}

fun <E> fieldSet(
    defaultValue: Set<E> = emptySet(),
    onValidate: ValidateScope<Set<E>>.() -> Unit = { },
): SetFormFieldSpec<E> = {
    SetFormField(defaultValue, onValidate)
}

fun <K, V> fieldMap(
    defaultValue: Map<K, V> = emptyMap(),
    onValidate: ValidateScope<Map<K, V>>.() -> Unit = { },
): MapFormFieldSpec<K, V> = {
    MapFormField(defaultValue, onValidate)
}

//

/** WARNING: This is still experimental | PLEASE READ DOCS OF [FormFormField] */
fun <T : Form> fieldForm(
    initialValue: T,
    onValidate: ValidateScope<T>.() -> Unit = { },
): FormSingleFormFieldSpec<T> = {
    FormSingleFormField(initialValue, onValidate)
}

/** WARNING: This is still experimental | PLEASE READ DOCS OF [FormFormField] */
fun <E : Form> fieldFormList(
    initialValue: List<E> = emptyList(),
    onValidate: ValidateScope<List<E>>.() -> Unit = { },
): FormListFormFieldSpec<E> = {
    FormListFormField(initialValue, onValidate)
}

/** WARNING: This is still experimental | PLEASE READ DOCS OF [FormFormField] */
fun <E : Form> fieldFormSet(
    initialValue: Set<E> = emptySet(),
    onValidate: ValidateScope<Set<E>>.() -> Unit = { },
): FormSetFormFieldSpec<E> = {
    FormSetFormField(initialValue, onValidate)
}

/** WARNING: This is still experimental | PLEASE READ DOCS OF [FormFormField] */
fun <K, V : Form> fieldFormMap(
    initialValue: Map<K, V> = emptyMap(),
    onValidate: ValidateScope<Map<K, V>>.() -> Unit = { },
): FormMapFormFieldSpec<K, V> = {
    FormMapFormField(initialValue, onValidate)
}
