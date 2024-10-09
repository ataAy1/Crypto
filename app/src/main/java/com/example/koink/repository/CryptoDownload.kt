package com.example.koink.repository

import com.example.koink.model.CryptoModel
import com.example.koink.service.CryptoAPI
import com.example.koink.util.Resource

interface CryptoDownload {
    suspend fun downloadCryptos() : Resource<List<CryptoModel>>
}