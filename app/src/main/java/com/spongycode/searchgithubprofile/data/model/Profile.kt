package com.spongycode.searchgithubprofile.data.model

import kotlinx.serialization.Serializable

@Serializable()
data class Profile(
    val login: String = "",
    val id: Long = 0,
    val node_id: String = "",
    val avatar_url: String = "",
    val gravatar_id: String = "",
    val url: String = "",
    val html_url: String = "",
    val followers_url: String = "",
    val following_url: String = "",
    val gists_url: String = "",
    val starred_url: String = "",
    val subscriptions_url: String = "",
    val organizations_url: String = "",
    val repos_url: String = "",
    val events_url: String = "",
    val received_events_url: String = "",
    val type: String = "",
    val site_admin: Boolean = false,
    val name: String = "",
    val company: String? = null,
    val blog: String? = null,
    val location: String? = "",
    val email: String? = "",
    val hireable: Boolean? = false,
    val bio: String? = "",
    val twitter_username: String? = "",
    val public_repos: Int = 0,
    val public_gists: Int = 0,
    val followers: Int = 0,
    val following: Int = 0,
    val created_at: String = "",
    val updated_at: String = "",
    val followers_list: List<Profile>? = null,
    val following_list: List<Profile>? = null,
    val message: String = "",
    val documentation_url: String = ""
)
