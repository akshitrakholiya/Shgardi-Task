package com.akshit.shgardi.view.activities

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.akshit.shgardi.R
import com.akshit.shgardi.databinding.ActivityHomeBinding
import com.akshit.shgardi.infra.CoreApplication
import com.akshit.shgardi.infra.network.NetworkResult
import com.akshit.shgardi.infra.utils.ConnectivityManager
import com.akshit.shgardi.models.PopularPersonListResponse
import com.akshit.shgardi.models.ResultsItem
import com.akshit.shgardi.utilities.PaginationListener
import com.akshit.shgardi.utilities.ProgressDialog
import com.akshit.shgardi.view.adapters.PopularPersonAdapter
import com.akshit.shgardi.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var dialog: Dialog
    @Inject
    lateinit var connectivityManager: ConnectivityManager
    private val homeViewModel: HomeViewModel by viewModels<HomeViewModel>()
    private lateinit var popularPersonAdapter: PopularPersonAdapter

    private var currPageNo = 1
    private var isLoading = false
    private var maxPageSize = 500 //Usually pageSize need to be passed from API, but I manually tested this api not supporting after 500 page so.
    private var isLastItem = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home)
        dialog = ProgressDialog.progressDialog(this)


        callPopularPersonListAPIs(currPageNo)
    }

    private fun loadPopularPersonList(results: List<ResultsItem?>) {

        popularPersonAdapter = PopularPersonAdapter(results.toMutableList()){ personShortInfo->
            Toast.makeText(this,personShortInfo?.name,Toast.LENGTH_SHORT).show()
        }
        binding.rvPopularPerson.adapter = popularPersonAdapter

        addPopularPersonListScrollListener()
    }

    private fun callPopularPersonListAPIs(pageNo: Int){
        if (connectivityManager.internetAvailable(CoreApplication.appContext)){
            homeViewModel.getPopularPersonList("en-US",pageNo)
        }

        homeViewModel.popularPersonListResponse.observe(this){response->
            when(response){
                is NetworkResult.Error -> {
                    dialog.dismiss()
                    Toast.makeText(this,response.message.toString(),Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    dialog.show()
                    isLoading = true
                }
                is NetworkResult.Success -> {
                    paginationRelatedValidation(response.data)
                    dialog.dismiss()
                }
            }
        }
    }

    private fun paginationRelatedValidation(_response: PopularPersonListResponse?) {
        if ((_response?.page ?: 0) >= maxPageSize) {
            isLastItem = true
        }

        if((_response?.page ?: 0) > 1){
            _response?.results?.let {
                addPopularPersonList(it)
            }
        }else if((_response?.page ?: 0) == 1){
            _response?.results?.let {
                loadPopularPersonList(it)
            }
        }

        isLoading = false
    }

    private fun addPopularPersonList(popularPersonList: List<ResultsItem?>) {
        popularPersonAdapter.addPopularPersonList(popularPersonList)
    }

    private fun addPopularPersonListScrollListener(){
        binding.rvPopularPerson.addOnScrollListener(object : PaginationListener(binding.rvPopularPerson.layoutManager as GridLayoutManager){
            override fun loadMoreItems() {
                if(!isLastItem){
                    currPageNo++
                    callPopularPersonListAPIs(currPageNo)
                }
            }

            override fun isLastPage(): Boolean {
                return isLastItem
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

        })
    }
}