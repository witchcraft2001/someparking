package com.someparking.androidapp.core.extensions

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import java.lang.IllegalArgumentException

/**
 * Расширения для извлечения аргументов, переданных фрагменту. Покрывают 3 варианта
 * использования аргументов в проекте:
 * 1. аргумент может отсутствовать, но требуется не нулевое дефолтное значение
 * 2. аргумент может отсутствовать, допустимо если он null
 * 3. аргумент с заданным ключом точно должен быть
 */

inline fun <reified T> Fragment.argument(key: String, defaultValue: T): Lazy<T> =
    lazy { arguments?.get(key) as? T ?: defaultValue }

inline fun <reified T> Fragment.optionalArgument(key: String): Lazy<T?> =
    lazy { arguments?.get(key) as? T }

/**
 * Throws an [IllegalStateException] if no arguments were supplied to the Fragment.
 * Throws an [IllegalArgumentException] if the [key] does not exist.
 * Throws an [ClassCastException] if a returned value cannot be cast to [T].
 * Otherwise returns not null value.
 */
inline fun <reified T> Fragment.requireArgument(key: String): Lazy<T> =
    lazy {
        val rawArgument = requireArguments().get(key) ?: throw IllegalArgumentException(
            "The Fragment $this does not have an argument with the key \"$key\""
        )
        val argument = rawArgument as? T ?: throw ClassCastException(
            "Can't cast an argument with the key \"$key\" to ${T::class.java}"
        )
        argument
    }

/**
 * Отправка какого-либо результата [Fragment] любому [Fragment] в рамках [Activity] или самой [Activity],
 * который подписался на [resultKey].
 * Метод полезен для тех случаев, когда присутствует большая вложенность фрагментов A->B->C->D->E->F
 * и вы хотите во фрагменте A получить какое-либо событие из F.
 * Метод работает в паре с методом [observeFragmentResult].
 * Если необходимо отправить результат только одним уровнем ирерахии выше (т.е. только прямому parent),
 * то необходимо использовать [sendFragmentResultToParent] и [observeFragmentResultFromChild].
 * @param resultKey Идентификатор результата
 * @param pairs Набор значений для результата, которые будут упакованы в [Bundle]
 */
fun Fragment.sendFragmentResult(resultKey: String, vararg pairs: Pair<String, Any?>) {
    requireActivity().supportFragmentManager.setFragmentResult(
        resultKey,
        bundleOf(*pairs)
    )
}

/**
 * Подписка на какой-либо результат, отправляемый через [sendFragmentResult].
 * Метод полезен для тех случаев, когда присутствует большая вложенность фрагментов A->B->C->D->E->F
 * и вы хотите во фрагменте A получить какое-либо событие из F.
 * В случае, если кто-то уже подписался на [resultKey], то предыдущая подписка будет отменена.
 * Если необходимо получить результат только от прямого child в иерархии фрагментов,
 * то необходимо использовать [sendFragmentResultToParent] и [observeFragmentResultFromChild].
 * @param resultKey Идентификатор результата
 * @param listener Callback, вызываемый при получении результата с идентификатором [resultKey]
 */
fun Fragment.observeFragmentResult(
    resultKey: String,
    listener: (Bundle) -> Unit
) {
    val fragmentResultListener = FragmentResultListener { _, result ->
        listener.invoke(result)
    }
    requireActivity().supportFragmentManager.setFragmentResultListener(
        resultKey,
        this,
        fragmentResultListener
    )
}

/**
 * Отправка какого-либо результата [Fragment] первому [Fragment]/[Activity] по иерархии выше, т.е. прямому parent.
 * Метод работает в паре с методом [observeFragmentResultFromChild].
 * Если необходимо отправлять результат через несколько parent,
 * то нужно использовать [sendFragmentResult] и [observeFragmentResult].
 * @param resultKey Идентификатор результата
 * @param pairs Набор значений для результата, которые будут упакованы в [Bundle]
 */
fun Fragment.sendFragmentResultToParent(resultKey: String, vararg pairs: Pair<String, Any?>) {
    parentFragmentManager.setFragmentResult(
        resultKey,
        bundleOf(*pairs)
    )
}

/**
 * Подписка на какой-либо результат, отправляемый через [sendFragmentResultToParent].
 * Результат будет получен только от прямого child-фрагмента.
 * В случае, если кто-то уже подписался на [resultKey], то предыдущая подписка будет отменена.
 * Если необходимо получить результат от любого child-фрагмента в иерархии,
 * то нужно использовать [sendFragmentResult] и [observeFragmentResult].
 * @param resultKey Идентификатор результата
 * @param listener Callback, вызываемый при получении результата с идентификатором [resultKey]
 */
fun Fragment.observeFragmentResultFromChild(
    resultKey: String,
    listener: (Bundle) -> Unit
) {
    val fragmentResultListener = FragmentResultListener { _, result ->
        listener.invoke(result)
    }
    childFragmentManager.setFragmentResultListener(
        resultKey,
        this,
        fragmentResultListener
    )
}