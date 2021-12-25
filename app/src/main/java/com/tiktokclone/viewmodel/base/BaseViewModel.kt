package com.tiktokclone.viewmodel.base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    protected val tag: String = this.javaClass.simpleName

    protected val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    protected open fun onError(throwable: Throwable) {
        Log.e(tag, "An error occurred: ${throwable.message}", throwable)
        _toastMessage.value = throwable.message
    }

    protected open fun onComplete(message: String) {
        Log.i(tag, message)
        _toastMessage.value = message
    }
}
