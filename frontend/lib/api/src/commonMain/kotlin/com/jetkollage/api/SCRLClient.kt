package com.jetkollage.api

import com.jetkollage.api.model.SCRLOverlayCategory
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CancellationException

class SCRLClient(
    private val config: SCRLConfig,
    private val client: HttpClient,
) {

    suspend fun getOverlays(): NetworkResult<List<SCRLOverlayCategory>> = runCatching {
        val response = client.get("${config.baseUrl}/overlays")
        if (response.status == HttpStatusCode.OK) {
            val body = response.body<List<SCRLOverlayCategory>>()
            NetworkResult.Success(body)
        } else {
            NetworkResult.ServerError(response.status.value)
        }
    }.getOrElse {
        if (it is CancellationException) {
            throw it
        } else {
            NetworkResult.Error(it)
        }
    }

}


data class SCRLConfig(
    val baseUrl: String = "https://appostropheanalytics.herokuapp.com/scrl/test"
)

