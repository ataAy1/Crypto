package com.example.koink.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.koink.R
import com.example.koink.databinding.FragmentListBinding
import com.example.koink.model.CryptoModel
import com.example.koink.service.CryptoAPI
import com.example.koink.viewmodel.CrpytoViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListFragment : Fragment(), RecyclerViewAdapter.Listener, AndroidScopeComponent {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private var cryptoAdapter = RecyclerViewAdapter(arrayListOf(), this)


    private val viewModel by viewModel<CrpytoViewModel>()

    override  val scope: Scope by fragmentScope()
    private val hello by inject<String>(qualifier = named("hello"))


    private val BASE_URL = "https://raw.githubusercontent.com/"
    private var cryptoModels: ArrayList<CryptoModel>? = null
    private var job: Job? = null
    private var recyclerViewAdapter: RecyclerViewAdapter? = null
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("error ${throwable.localizedMessage}")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel.getDataFromAPI()

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.cryptoList.observe(viewLifecycleOwner, Observer { cryptos ->
            cryptos?.let {
                binding.recyclerView.visibility = View.VISIBLE
                cryptoAdapter = RecyclerViewAdapter(ArrayList(cryptos.data), this@ListFragment)
                binding.recyclerView.adapter = cryptoAdapter
            }
        })

        viewModel.cryptoError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                if (it.data == true) {
                    binding.cryptoErrorText.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE

                } else {
                    binding.cryptoErrorText.visibility = View.GONE

                }
            }
        })
        viewModel.cryptoLoading.observe(viewLifecycleOwner, Observer { loading ->

            loading?.let {
                if (it.data == true) {
                    binding.cryptoProgressBar.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                    binding.cryptoErrorText.visibility = View.GONE

                } else {
                    binding.cryptoProgressBar.visibility = View.GONE
                }
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        job?.cancel()
    }

    override fun onItemClick(cryptoModel: CryptoModel) {
        TODO("Not yet implemented")
    }
}