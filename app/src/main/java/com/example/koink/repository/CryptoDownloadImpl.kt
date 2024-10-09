package com.example.koink.repository

import com.example.koink.model.CryptoModel
import com.example.koink.service.CryptoAPI
import com.example.koink.util.Resource

class CryptoDownloadImpl(private val api : CryptoAPI) : CryptoDownload {

    override suspend fun downloadCryptos() : Resource<List<CryptoModel>> {
        return try {
            val response = api.getData()
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Error",null)
            } else {
                Resource.error("Error",null)
            }
        } catch (e: Exception) {
            Resource.error("No data!",null)
        }
    }
}