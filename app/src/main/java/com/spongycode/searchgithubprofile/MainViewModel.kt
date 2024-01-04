package com.spongycode.searchgithubprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spongycode.searchgithubprofile.data.model.Profile
import com.spongycode.searchgithubprofile.domain.repository.MyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MyRepository
) : ViewModel() {

    private val _queryState = MutableLiveData<QueryState>(QueryState.Idle)
    val queryState: LiveData<QueryState> = _queryState

    private val _profileResultDatabase = MutableLiveData<Map<String, Profile>>(mutableMapOf())
    val profileResultDatabase: LiveData<Map<String, Profile>> = _profileResultDatabase

    fun makeProfileQuery(text: String, search: Boolean = false) {
        val username = text.trim()
        viewModelScope.launch {
            if (search) _queryState.value = QueryState.Checking
            try {
                val profile = repository.makeProfileQuery(text)
                if (profile == null) {
                    if (search) _queryState.value = QueryState.Error
                } else {
                    if (profile.login.isNotBlank()) {
                        _profileResultDatabase.value =
                            _profileResultDatabase.value!!.toMutableMap().apply {
                                put(username, profile)
                            }.toMap()
                        if (search) {
                            _queryState.value = QueryState.Found
                        }
                    } else {
                        if (search) _queryState.value = QueryState.NotFound
                    }
                }
            } catch (e: Exception) {
                if (search) _queryState.value = QueryState.Error
            }
        }
    }

    fun populateFollowersList(endpoint: String) {
        val username = endpoint.split(".").firstOrNull().toString()
        viewModelScope.launch {
            try {
                val result = repository.populateFollowersList(endpoint)
                if (result != null) {
                    val updatedProfile = _profileResultDatabase.value?.get(username)?.copy(
                        followers_list = result
                    )
                    _profileResultDatabase.value =
                        _profileResultDatabase.value!!.toMutableMap().apply {
                            put(username, updatedProfile!!)
                        }.toMap()
                }
            } catch (_: Exception) {
            }
        }
    }

    sealed class QueryState {
        data object Idle : QueryState()
        data object Checking : QueryState()
        data object Found : QueryState()
        data object NotFound : QueryState()
        data object Error : QueryState()
    }
}