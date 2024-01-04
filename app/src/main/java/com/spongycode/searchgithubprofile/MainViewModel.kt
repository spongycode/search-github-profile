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

    private val BASE_URL = "https://api.github.com/"
    private val _queryState = MutableLiveData<QueryState>(QueryState.Idle)
    val queryState: LiveData<QueryState> = _queryState

    private val _profileResultDatabase = MutableLiveData<Map<String, Profile>>(mutableMapOf())
    val profileResultDatabase: LiveData<Map<String, Profile>> = _profileResultDatabase

    fun makeProfileQuery(text: String, search: Boolean = false) {
        val username = text.trim()
        viewModelScope.launch {
            try {
                if (search) _queryState.value = QueryState.Checking
                val client = createHttpClient()
                val response: HttpResponse = client.get {
                    url("${BASE_URL}users/${username}")
                }
                val resBody: Profile = response.body<Profile>()
                if (resBody.login.isNotBlank()) {
                    _profileResultDatabase.value =
                        _profileResultDatabase.value!!.toMutableMap().apply {
                            put(username, resBody)
                        }.toMap()
                    if (search) {
                        _queryState.value = QueryState.Found
                    }
                } else {
                    if (search) _queryState.value = QueryState.NotFound
                }
                client.close()
            } catch (e: Exception) {
                if (search) _queryState.value = QueryState.Error
            }
        }
    }

    fun populateFollowersList(endpoint: String) {
        val username = endpoint.split(".").firstOrNull().toString()
        val freshEndpoint = endpoint.replace(".", "/")
        viewModelScope.launch {
            try {
                val client = createHttpClient()
                val response: HttpResponse = client.get {
                    url("${BASE_URL}users/${freshEndpoint}")
                }
                val resBody: List<Profile> = response.body<List<Profile>>()
                val updatedProfile = _profileResultDatabase.value?.get(username)?.copy(
                    followers_list = resBody
                )
                _profileResultDatabase.value =
                    _profileResultDatabase.value!!.toMutableMap().apply {
                        put(username, updatedProfile!!)
                    }.toMap()
                client.close()
            } catch (_: Exception) {
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
                        BearerTokens("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", "")
                    }
                }
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