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
import com.akshit.shgardi.models.ResultsItem
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
    private var currPageNo = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home)
        dialog = ProgressDialog.progressDialog(this)


        callPopularPersonListAPIs(currPageNo)
    }

    private fun loadPopularPersonList(results: List<ResultsItem?>) {
        binding.rvPopularPerson.adapter = PopularPersonAdapter(results){ personShortInfo->
            Toast.makeText(this,personShortInfo?.name,Toast.LENGTH_SHORT).show()
        }
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
                }
                is NetworkResult.Success -> {
                    response.data?.results?.let { loadPopularPersonList(it) }
                    dialog.dismiss()
                }
            }
        }
    }
}