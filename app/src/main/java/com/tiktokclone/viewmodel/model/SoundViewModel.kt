package com.tiktokclone.viewmodel.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiktokclone.data.datasource.model.sound.DataXXXX
import com.tiktokclone.data.repository.SoundRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SoundViewModel  @Inject constructor( var soundRepository: SoundRepository) :ViewModel() {

    val repos = MutableLiveData<List<DataXXXX>>()

    fun getTracks() {
        viewModelScope.launch {
            var  response =soundRepository.getTracks().tracks.data
            repos.value=  response

        }
    }
}