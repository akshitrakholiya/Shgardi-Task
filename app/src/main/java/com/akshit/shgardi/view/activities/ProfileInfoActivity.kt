package com.akshit.shgardi.view.activities

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.akshit.shgardi.R
import com.akshit.shgardi.databinding.ActivityPersonInfoBinding
import com.akshit.shgardi.infra.CoreApplication
import com.akshit.shgardi.infra.network.NetworkResult
import com.akshit.shgardi.infra.utils.ConnectivityManager
import com.akshit.shgardi.models.PersonImagesResponse
import com.akshit.shgardi.models.PersonInfoResponse
import com.akshit.shgardi.utilities.ProgressDialog
import com.akshit.shgardi.viewmodels.PersonInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonInfoBinding
    private lateinit var dialog: Dialog
    @Inject
    lateinit var connectivityManager: ConnectivityManager
    private val personInfoViewModel: PersonInfoViewModel by viewModels<PersonInfoViewModel>()
    private var personId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_person_info)
        dialog = ProgressDialog.progressDialog(this)

        binding.clPersonInfo.visibility = View.INVISIBLE

        personId = intent.getIntExtra(getString(R.string.args_person_id),0)
        callPersonInfoAPIs(personId)
        callPersonImagesAPIs(personId)
    }

    private fun callPersonInfoAPIs(personId: Int) {
        if(connectivityManager.internetAvailable(CoreApplication.appContext)){
            personInfoViewModel.getPersonInfo("en-US",personId)
        }

        personInfoViewModel.personInfoResponse.observe(this){ response->
            when(response){
                is NetworkResult.Error -> {
                    dialog.dismiss()
                    Toast.makeText(this,response.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    dialog.show()
                }
                is NetworkResult.Success -> {
                    response.data?.let {
                        showPersonInfo(it)
                    }
                    dialog.dismiss()
                }
            }
        }
    }

    private fun callPersonImagesAPIs(personId: Int) {
        if(connectivityManager.internetAvailable(CoreApplication.appContext)){
            personInfoViewModel.getPersonImages(personId)
        }

        personInfoViewModel.personImagesResponse.observe(this){ response->
            when(response){
                is NetworkResult.Error -> {
                    dialog.dismiss()
                    Toast.makeText(this,response.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    dialog.show()
                }
                is NetworkResult.Success -> {
                    response.data?.let {
                        showPersonImages(it)
                    }
                    dialog.dismiss()
                }
            }
        }
    }

    private fun showPersonImages(personImagesResponse: PersonImagesResponse) {

    }

    private fun showPersonInfo(_personInfo: PersonInfoResponse) {
        binding.personInfo = _personInfo
        binding.clPersonInfo.visibility = View.VISIBLE
        Toast.makeText(this,_personInfo.name,Toast.LENGTH_SHORT).show()
    }
}