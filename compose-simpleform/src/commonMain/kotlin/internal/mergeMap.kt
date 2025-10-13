package net.lsafer.compose.simpleform.internal

internal fun <K, V, Ko, Vo> mergeMap(
    origin: Map<K, V>,
    update: Map<Ko, Vo>,
    decodeKey: (Ko) -> K,
    decodeValue: (Vo) -> V,
    merge: (K, V, Vo) -> V,
): Map<K, V> {
    val result = origin.toMutableMap()
    val toRemoveList = origin.keys.toMutableSet()

    // main result manipulation
    for ((updateKo, updateVo) in update) {
        val updateK = decodeKey(updateKo)

        // key found in new value; remove from to-be-removed list
        toRemoveList -= updateK

        if (updateK in origin) {
            // compute current item with new item
            @Suppress("UNCHECKED_CAST")
            val originV = origin[updateK] as V
            val computedV = merge(updateK, originV, updateVo)

            // add to result; ignore if merged in-place
            if (originV != computedV)
                result[updateK] = computedV
        } else {
            // new item; no need to merge, just add
            val computedV = decodeValue(updateVo)
            result[updateK] = computedV
        }
    }

    // result finalization
    result -= toRemoveList

    return result
}
