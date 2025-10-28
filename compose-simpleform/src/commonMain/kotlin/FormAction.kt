package net.lsafer.compose.simpleform

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class FormAction(
    private val coroutineScope: CoroutineScope,
    private val condition: () -> Boolean = { true },
    private val block: suspend CoroutineScope.() -> Unit,
) {
    var loadingCount by mutableStateOf(0)
        private set
    val isLoading get() = loadingCount > 0
    val isEnabled get() = condition()

    operator fun invoke() {
        coroutineScope.launch {
            if (!condition())
                return@launch

            try {
                loadingCount++
                coroutineScope {
                    block()
                }
            } finally {
                loadingCount--
            }
        }
    }
}

context(vm: ViewModel)
fun FormAction(condition: () -> Boolean = { true }, block: suspend CoroutineScope.() -> Unit) =
    FormAction(vm.viewModelScope, condition, block)

@Composable
fun rememberFormAction(
    condition: () -> Boolean = { true },
    block: suspend CoroutineScope.() -> Unit
): FormAction {
    val coroutineScope = rememberCoroutineScope()
    return remember {
        FormAction(coroutineScope, condition, block)
    }
}
