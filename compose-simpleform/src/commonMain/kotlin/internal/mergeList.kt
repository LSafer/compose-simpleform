package net.lsafer.compose.simpleform.internal

internal fun <E, Eo> mergeList(
    origin: List<E>,
    update: List<Eo>,
    decodeItem: (Eo) -> E,
    match: (E, Eo) -> Boolean,
    merge: (E, Eo) -> E,
): List<E> {
    val result = origin.toMutableList()
    val toAddList = update.toMutableList()

    // main result manipulation
    val resultIter = result.listIterator()
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
                if (originE != computedE)
                    resultIter.set(computedE)

                continue@mainLoop
            }
        }

        // element does not exist in new list; exclude from result
        resultIter.remove()
    }

    // result finalization
    for (newEo in toAddList) {
        // new item; no need to merge, just add
        val computedE = decodeItem(newEo)
        resultIter.add(computedE)
    }

    return result
}
