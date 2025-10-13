package net.lsafer.compose.simpleform

import kotlin.jvm.JvmName

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

/** WARNING: This is still experimental */
fun <T, O> field(
    codec: FormCodec<T?, O, SingleFormField<T?>>,
    validator: Validator<T?> = { },
) = field(validator).transform(codec)

/** WARNING: This is still experimental */
fun <T, O> field(
    codec: FormCodec<T, O, SingleFormField<T>>,
    defaultValue: T,
    validator: Validator<T> = { },
) = field(defaultValue, validator).transform(codec)

/** WARNING: This is still experimental */
fun <E, O> fieldList(
    codec: FormCodec<List<E>, O, ListFormField<E>>,
    defaultValue: List<E> = emptyList(),
    validator: Validator<List<E>> = { },
) = fieldList(defaultValue, validator).transform(codec)

/** WARNING: This is still experimental */
fun <E, O> fieldSet(
    codec: FormCodec<Set<E>, O, SetFormField<E>>,
    defaultValue: Set<E> = emptySet(),
    validator: Validator<Set<E>> = { },
) = fieldSet(defaultValue, validator).transform(codec)

/** WARNING: This is still experimental */
fun <K, V, O> fieldMap(
    codec: FormCodec<Map<K, V>, O, MapFormField<K, V>>,
    defaultValue: Map<K, V> = emptyMap(),
    validator: Validator<Map<K, V>> = { },
) = fieldMap(defaultValue, validator).transform(codec)

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

//

/** WARNING: This is still experimental | PLEASE READ DOCS OF [FormFormField] */
fun <T : Form, O> fieldForm(
    codec: FormCodec<T, O, SingleFormField<T>>,
    initialValue: T,
    validator: Validator<T> = { },
) = fieldForm(initialValue, validator).transform(codec)

/** WARNING: This is still experimental | PLEASE READ DOCS OF [FormFormField] */
fun <E : Form, O> fieldFormList(
    codec: FormCodec<List<E>, O, ListFormField<E>>,
    initialValue: List<E> = emptyList(),
    validator: Validator<List<E>> = { },
) = fieldFormList(initialValue, validator).transform(codec)

/** WARNING: This is still experimental | PLEASE READ DOCS OF [FormFormField] */
fun <E : Form, O> fieldFormSet(
    codec: FormCodec<Set<E>, O, SetFormField<E>>,
    initialValue: Set<E> = emptySet(),
    validator: Validator<Set<E>> = { },
) = fieldFormSet(initialValue, validator).transform(codec)

/** WARNING: This is still experimental | PLEASE READ DOCS OF [FormFormField] */
fun <K, V : Form, O> fieldFormMap(
    codec: FormCodec<Map<K, V>, O, MapFormField<K, V>>,
    initialValue: Map<K, V> = emptyMap(),
    validator: Validator<Map<K, V>> = { },
) = fieldFormMap(initialValue, validator).transform(codec)

//

/** WARNING: This is still experimental */
@JvmName("transformSingle")
fun <T, O, F : SingleFormField<T>> (() -> F).transform(codec: FormCodec<T, O, F>) =
    { SerialSingleFormField(this(), codec) }

/** WARNING: This is still experimental */
@JvmName("transformMap")
fun <K, V, O, F : MapFormField<K, V>> (() -> F).transform(codec: FormCodec<Map<K, V>, O, F>) =
    { SerialMapFormField(this(), codec) }

/** WARNING: This is still experimental */
@JvmName("transformList")
fun <E, O, F : ListFormField<E>> (() -> F).transform(codec: FormCodec<List<E>, O, F>) =
    { SerialListFormField(this(), codec) }

/** WARNING: This is still experimental */
@JvmName("transformSet")
fun <E, O, F : SetFormField<E>> (() -> F).transform(codec: FormCodec<Set<E>, O, F>) =
    { SerialSetFormField(this(), codec) }
