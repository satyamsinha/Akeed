package com.application.akeed

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.application.akeed.network.DataList
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.android.synthetic.main.detailed_activity.*

class DetailedActivity :AppCompatActivity(){

    private lateinit var dataList: DataList
    private lateinit var data: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailed_activity)
        if(intent.hasExtra("data")) {
            data = intent.getStringExtra("data")
            dataList=Gson().fromJson<DataList>(data,DataList ::class.java)
            tvTitle.text=dataList.Title
            tvType.text="Type :"+ dataList.Type
            tvYear.text="Year :"+dataList.Year
            imgDetail.let {
                Glide.with(this)
                    .load(dataList.Poster)
                   // .circleCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .fallback(R.mipmap.ic_launcher)
                    .into(it)
            }
        }
    }
}