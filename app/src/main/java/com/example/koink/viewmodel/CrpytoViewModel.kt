package com.example.koink.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koink.model.CryptoModel
import com.example.koink.repository.CryptoDownload
import com.example.koink.service.CryptoAPI
import com.example.koink.util.Resource
import com.example.koink.view.RecyclerViewAdapter
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CrpytoViewModel(
        private val cryptoDownloadRepository : CryptoDownload
    ) : ViewModel() {

    val cryptoList = MutableLiveData<Resource<List<CryptoModel>>>()
    val cryptoError = MutableLiveData<Resource<Boolean>>()
    val cryptoLoading = MutableLiveData<Resource<Boolean>>()

    private val BASE_URL = "https://raw.githubusercontent.com/"
    private var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("error ${throwable.localizedMessage}")
        cryptoError.value = Resource.error(throwable.localizedMessage ?: "error", data = true)

    }


    fun getDataFromAPI() {
        cryptoLoading.value =  Resource.loading(data = true)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CryptoAPI::class.java)
        /* viewModelScope.launch(Dispatchers.IO + exceptionHandler) {

         }*/

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val resource = cryptoDownloadRepository.downloadCryptos()
            val response = retrofit.getData()
            withContext(Dispatchers.Main) {
                resource.data?.let {
                    cryptoList.value=resource
                    cryptoLoading.value=Resource.loading(data = false)
                    cryptoLoading.value=Resource.error("",data = false)

                }
            }
        }
    }


}