package com.mdlab.mdfirebase.db

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
/**
 * Implement this function, if you want to use query on the data.
 */
fun interface IMapDatabaseReference {
    fun invoke(dbRef: DatabaseReference): Query
}