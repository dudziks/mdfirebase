package com.mdlab.mdfirebase.db

import com.google.firebase.auth.FirebaseUser
/**
 * Implement this, if you need other root structure than default  /users/$userId/some_dir
 */
fun interface IRootNodePath {
    fun get(fbUser: FirebaseUser): String
}