package com.example.cataracta.ui.imageSelection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is image selection fragment"
    }
    val text: LiveData<String> = _text
}