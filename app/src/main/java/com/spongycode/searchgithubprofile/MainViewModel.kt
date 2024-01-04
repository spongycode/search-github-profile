package com.spongycode.searchgithubprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spongycode.searchgithubprofile.data.model.Profile
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {


    private val _profileResultState = MutableLiveData<ValidationState>(ValidationState.Idle)
    val profileResultState: LiveData<ValidationState> = _profileResultState

    private val _profileResultDatabase = MutableLiveData<Map<String, Profile>>(mutableMapOf())
    val profileResultDatabase: LiveData<Map<String, Profile>> = _profileResultDatabase

    fun makeProfileQuery(username: String) {
        viewModelScope.launch {
            try {
                _profileResultState.value = ValidationState.Checking
                val client = createHttpClient()
                val response: HttpResponse = client.get {
                    url("https://api.github.com/users/${username}")
                }
                val resBody: Profile = response.body<Profile>()
                if (resBody.login.isNotBlank()) {
                    _profileResultDatabase.value =
                        _profileResultDatabase.value!!.toMutableMap().apply {
                            put(username, resBody)
                        }.toMap()
                    _profileResultState.value = ValidationState.Valid
                }
                client.close()
            } catch (e: Exception) {
                _profileResultState.value = ValidationState.Invalid
            }
        }
    }

    fun populateFollowersList(endpoint: String) {
        val username = endpoint.split(".").firstOrNull().toString()
        val freshEndpoint = endpoint.replace(".", "/")
        viewModelScope.launch {
            try {
                _profileResultState.value = ValidationState.Checking
                val client = createHttpClient()
                val response: HttpResponse = client.get {
                    url("https://api.github.com/users/${freshEndpoint}")
                }
                val resBody: List<Profile> = response.body<List<Profile>>()
                if (resBody.isNotEmpty()) {
                    val updatedProfile = _profileResultDatabase.value?.get(username)?.copy(
                        followers_list = resBody
                    )
                    _profileResultDatabase.value =
                        _profileResultDatabase.value!!.toMutableMap().apply {
                            put(username, updatedProfile!!)
                        }.toMap()
                }
                client.close()
            } catch (e: Exception) {
                _profileResultState.value = ValidationState.Invalid
            }
        }
    }

    private fun createHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(kotlinx.serialization.json.Json {
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                })
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        BearerTokens("TOKEN_HERE", "")
                    }
                }
            }
        }
    }


    sealed class ValidationState {
        object Idle : ValidationState()
        object Checking : ValidationState()
        object Valid : ValidationState()
        object Invalid : ValidationState()
    }
}