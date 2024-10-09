package com.example.koink.di

import com.example.koink.repository.CryptoDownload
import com.example.koink.repository.CryptoDownloadImpl
import com.example.koink.service.CryptoAPI
import com.example.koink.viewmodel.CrpytoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory




val appModule  = module {

    single {
        val BASE_URL = "https://raw.githubusercontent.com/"
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CryptoAPI::class.java)
    }

    single<CryptoDownload> {
        CryptoDownloadImpl(get())

    }

    viewModel{
        CrpytoViewModel(get())
    }

    factory {

    }
}