package com.application.akeed.ui.main

import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.akeed.MainActivity
import com.application.akeed.R
import com.application.akeed.network.ApiCallInterface
import com.application.akeed.network.ResponseData
import com.application.akeed.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private lateinit var context: Context
    private lateinit var responseData: MutableLiveData<ResponseData>

    fun getTask(context: Context?, text: String, pageCount: Int): MutableLiveData<ResponseData> {

        //this.context = context

        responseData = MutableLiveData<ResponseData>()

        val textTopass="?s="+text+"&page="+pageCount+"&apikey=eeefc96f"
        callTaskList(textTopass)

        return responseData
    }

    fun setContext(context: Context){
        this.context =context
    }


    private fun callTaskList(text:String) {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Loading Please wait .. ")
        progressDialog.setCancelable(false)
        progressDialog.setTitle("Movies")
        progressDialog.show()
        val a =  MainActivity()

        if(a.verifyAvailableNetwork(context as AppCompatActivity)) {
            val request =
                RetrofitInstance.getRetrofitInstance().create(ApiCallInterface::class.java)
            request.getItemsDetails(text).enqueue(object : Callback<ResponseData> {

                override fun onResponse(
                    call: Call<ResponseData>,
                    response: Response<ResponseData>
                ) {
                    Log.i("task", response.body().toString())
                    responseData.value = response.body()
                    progressDialog.dismiss()
                }

                override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                    progressDialog.dismiss()
                }
            })

        }
        else{
            Toast.makeText(context, R.string.internet_connnection, Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()}
    }
}