package com.akshit.shgardi.view.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
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
    private var lang = "en-US"

    //For Search Person Feature
    private lateinit var searchPersonAdapter: PopularPersonAdapter
    private var currSearchPageNo = 1
    private var isSearchLoading = false
    private var maxSearchPageSize = 1 // later we're taking from API, search api supporting based on total_pages
    private var isSearchLastItem = false
    private var query = ""
    private var includeAdult = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home)
        dialog = ProgressDialog.progressDialog(this)


        callPopularPersonListAPIs(currPageNo)

        setUpSearchListener()
    }

    private fun loadPopularPersonList(results: List<ResultsItem?>) {

        popularPersonAdapter = PopularPersonAdapter(results.toMutableList()){ personShortInfo->
            val intent = Intent(this,ProfileInfoActivity::class.java)
            intent.putExtra(getString(R.string.args_person_id),personShortInfo?.id)
            startActivity(intent)
        }
        binding.rvPopularPerson.adapter = popularPersonAdapter

        addPopularPersonListScrollListener()
    }

    private fun callPopularPersonListAPIs(pageNo: Int){
        if (connectivityManager.internetAvailable(CoreApplication.appContext)){
            homeViewModel.getPopularPersonList(lang,pageNo)
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

    private fun setUpSearchListener() {
        binding.etSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val searchQuery = binding.etSearch.text.toString()
                    if (!TextUtils.isEmpty(searchQuery)) {
                        startSearching(searchQuery)
                    }
                    return true
                }
                return false
            }
        })

        binding.ivSearch.setOnClickListener {
            val searchQuery = binding.etSearch.text.toString()
            if (!TextUtils.isEmpty(searchQuery)) {
                startSearching(searchQuery)
            }
        }

        binding.ivClose.setOnClickListener {
            stopSearching()
        }
    }

    private fun startSearching(_searchQuery: String) {
        binding.rvSearchPerson.visibility = View.VISIBLE
        binding.rvPopularPerson.visibility = View.GONE
        query = _searchQuery
        currSearchPageNo = 1
        searchPersonListAPIs()

        //handling Search/Cancel UI
        binding.ivSearch.visibility = View.GONE
        binding.ivClose.visibility = View.VISIBLE
        binding.etSearch.clearFocus()
    }

    private fun stopSearching(){
        searchPersonAdapter.clearPopularPersonList()
        binding.rvSearchPerson.visibility = View.GONE
        binding.rvPopularPerson.visibility = View.VISIBLE
        query=""
        binding.etSearch.text?.clear()
        currSearchPageNo = 1
        maxSearchPageSize = 1
        isSearchLoading = false
        isSearchLastItem = false

        //handling Search/Cancel UI
        binding.ivSearch.visibility = View.VISIBLE
        binding.ivClose.visibility = View.GONE
    }

    private fun searchPersonListAPIs(){
        if (connectivityManager.internetAvailable(CoreApplication.appContext)){
            homeViewModel.searchPersonList(query,includeAdult,lang,currSearchPageNo)
        }

        homeViewModel.searchPersonListResponse.observe(this){response->
            when(response){
                is NetworkResult.Error -> {
                    dialog.dismiss()
                    isSearchLoading = false
                    Toast.makeText(this,response.message.toString(),Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    dialog.show()
                    isSearchLoading = true
                }
                is NetworkResult.Success -> {
                    searchPaginationValidation(response.data)
                    dialog.dismiss()
                }
            }
        }
    }

    private fun searchPaginationValidation(_response: PopularPersonListResponse?) {
        if((_response?.page ?: 0) > 1){
            _response?.results?.let {
                addSearchPersonList(it)
            }
        }else if((_response?.page ?: 0) == 1){
            maxSearchPageSize = _response?.totalPages ?: 1
            _response?.results?.let {
                loadSearchPersonList(it)
            }
        }

        if ((_response?.page ?: 0) >= maxSearchPageSize) {
            isSearchLastItem = true
        }

        isSearchLoading = false
    }

    private fun loadSearchPersonList(results: List<ResultsItem?>) {

        searchPersonAdapter = PopularPersonAdapter(results.toMutableList()){ personShortInfo->
            val intent = Intent(this,ProfileInfoActivity::class.java)
            intent.putExtra(getString(R.string.args_person_id),personShortInfo?.id)
            startActivity(intent)
        }
        binding.rvSearchPerson.adapter = searchPersonAdapter

        addSearchPersonListScrollListener()
    }

    private fun addSearchPersonList(searchPersonList: List<ResultsItem?>) {
        searchPersonAdapter.addPopularPersonList(searchPersonList)
    }

    private fun addSearchPersonListScrollListener(){
        binding.rvSearchPerson.addOnScrollListener(object : PaginationListener(binding.rvSearchPerson.layoutManager as GridLayoutManager){
            override fun loadMoreItems() {
                if(!isSearchLastItem){
                    currSearchPageNo++
                    searchPersonListAPIs()
                }
            }

            override fun isLastPage(): Boolean {
                return isSearchLastItem
            }

            override fun isLoading(): Boolean {
                return isSearchLoading
            }

        })
    }
}