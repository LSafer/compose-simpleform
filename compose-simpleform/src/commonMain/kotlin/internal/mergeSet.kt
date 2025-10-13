package net.lsafer.compose.simpleform.internal

internal fun <E, Eo> mergeSet(
    origin: Set<E>,
    update: Set<Eo>,
    decodeItem: (Eo) -> E,
    match: (E, Eo) -> Boolean,
    merge: (E, Eo) -> E,
): Set<E> {
    val result = origin.toMutableSet()
    val toAddList = update.toMutableSet()
    val toAddList2 = mutableSetOf<E>()

    // main result manipulation
    val resultIter = result.iterator()
    mainLoop@ while (resultIter.hasNext()) {
        val originE = resultIter.next()
        val toAddIter = toAddList.iterator()

        while (toAddIter.hasNext()) {
            val updateEo = toAddIter.next()

            if (match(originE, updateEo)) {
                // match found; remove new value from to-be-added list
                toAddIter.remove()

                // compute current item with new item
                val computedE = merge(originE, updateEo)

                // replace current value in result; ignore if merged in-place
                if (originE != computedE) {
                    resultIter.remove()
                    toAddList2 += computedE
                }

                continue@mainLoop
            }
        }

        // element does not exist in new list; exclude from result
        resultIter.remove()
    }

    // result finalization
    result += toAddList2

    for (newEo in toAddList) {
        // new item; no need to merge, just add
        val computedE = decodeItem(newEo)
        result += computedE
    }

    return result
}
