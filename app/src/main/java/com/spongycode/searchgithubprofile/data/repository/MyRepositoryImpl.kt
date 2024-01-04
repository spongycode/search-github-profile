package com.spongycode.searchgithubprofile.data.repository

import com.spongycode.searchgithubprofile.data.model.Profile
import com.spongycode.searchgithubprofile.domain.repository.MyRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject

class MyRepositoryImpl @Inject constructor(
    private val client: HttpClient
) : MyRepository {

    private val BASE_URL = "https://api.github.com/"

    override suspend fun makeProfileQuery(text: String): Profile? {
        val username = text.trim()
        return try {
            val response: HttpResponse = client.get {
                url("${BASE_URL}users/${username}")
            }
            response.body<Profile>()
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun populateFollowersList(endpoint: String): List<Profile>? {
        val freshEndpoint = endpoint.replace(".", "/")
        return try {
            val response: HttpResponse = client.get {
                url("${BASE_URL}users/${freshEndpoint}")
            }
            response.body<List<Profile>>()
        } catch (_: Exception) {
            null
        }
    }
}