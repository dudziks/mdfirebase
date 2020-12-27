package com.mdlab.mdfirebase.db

import com.google.firebase.auth.FirebaseUser

class DefaultRootNodePath: IRootNodePath {    override fun get(fbUser: FirebaseUser): String = "users/${fbUser.uid}" }