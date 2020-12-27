package com.mdlab.mdfirebase.model

import com.google.firebase.database.Exclude

/**
 * Convert to map for storing as the JSON
 */
interface IDTO {
    @Exclude
    fun toMap(): Map<String, Any?>
}
