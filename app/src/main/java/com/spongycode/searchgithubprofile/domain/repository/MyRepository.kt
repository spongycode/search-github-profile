package com.spongycode.searchgithubprofile.domain.repository

import com.spongycode.searchgithubprofile.data.model.Profile

interface MyRepository {

    suspend fun makeProfileQuery(text: String): Profile?
    suspend fun populateFollowersList(endpoint: String): List<Profile>?
}