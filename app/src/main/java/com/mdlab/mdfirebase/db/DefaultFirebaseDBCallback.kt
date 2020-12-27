package com.mdlab.mdfirebase.db

import com.google.firebase.database.DataSnapshot

class DefaultFirebaseDBCallback<T>(
    var onAddedCallback: OnAddedCallback<T> = OnAddedCallback {  },
    var onAddedListCallback: OnAddedListCallback<T> = OnAddedListCallback {  },
    var onErrorCallbac: OnErrorCallback<T> = OnErrorCallback {  },
    var onChangedCallback: OnChangedCallback<T> = OnChangedCallback {  },
    var onRemovedCallbac: OnRemovedCallback<T> = OnRemovedCallback {  },
    var onDataChangedCallback: OnDataChangedCallback<T> = OnDataChangedCallback {  }
): IFirebaseDatabaseRepositoryCallback<T> {
    override fun onAdded(result: List<T>) = onAddedListCallback.onAdded(result)
    override fun onAdded(result: T) = onAddedCallback.onAdded(result)
    override fun onError(e: Exception) = onErrorCallbac.onError(e)
    override fun onChanged(result: T) = onChangedCallback.onChanged(result)
    override fun onRemoved(result: T) = onRemovedCallbac.onRemoved(result)
    override fun onDataChange(result: DataSnapshot) = onDataChangedCallback.onDataChange(result)

    fun interface OnAddedListCallback<T> {    fun onAdded(result: List<T>)   }
    fun interface OnAddedCallback<T> {    fun onAdded(result: T)  }
    fun interface OnErrorCallback<T> {    fun onError(e: Exception)  }
    fun interface OnChangedCallback<T> {    fun onChanged(result: T)  }
    fun interface OnRemovedCallback<T> {    fun onRemoved(result: T)  }
    fun interface OnDataChangedCallback<T> {    fun onDataChange(result: DataSnapshot)  }
}