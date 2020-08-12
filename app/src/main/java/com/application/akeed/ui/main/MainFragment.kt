package com.application.akeed.ui.main

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.application.akeed.R
import com.application.akeed.network.DataList
import com.application.akeed.network.ResponseData
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment(), AbsListView.OnScrollListener {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var totalRecords: String
    private var myLastVisiblePos: Int=0
    private  var listData=ArrayList<DataList>()
    private  var listTempData=ArrayList<DataList>()
    private lateinit var viewModel: MainViewModel
    private var pagecnt:Int=1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        edt_search.setText("Batman")

        getDataFromApi(pagecnt)

        img_search.setOnClickListener{
            if(!TextUtils.isEmpty(edt_search.text)) {
                pagecnt=1
                getDataFromApi(pagecnt)
            }
        }
    }
    private fun getDataFromApi(pageCount:Int){
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.setContext(requireContext())
        viewModel.getTask(activity,edt_search.text.toString(),pageCount).observe(viewLifecycleOwner, Observer { listtask:ResponseData ->
            totalRecords=listtask.totalResults
            listData=listtask.Search
            if(pageCount==1)
                initView()
            else
                updateView()
        })
    }
    private lateinit var adapter :ImageListAdapter
    private fun initView() {
        listTempData.clear()
        val count=listTempData.size
        for(i in 0..listData.size){
            if((i+count)<listData.size)
                listTempData.add(listData[i+count])
        }
        adapter = ImageListAdapter(requireContext(), R.layout.list_item, listTempData)
        gridview.adapter = adapter
        myLastVisiblePos = listTempData.size
        gridview.setOnScrollListener(this)
    }

    private fun updateView(){

        myLastVisiblePos = currentFirstVisPos
        val count=listTempData.size
        for(i in 0..listData.size-1){
            listTempData.add(listData[i])
        }
        adapter.notifyDataSetChanged()
        gridview?.invalidate()
        loading=false
    }
    private var currentFirstVisPos: Int=0
    override fun onScroll(
        view: AbsListView?,
        firstVisibleItem: Int,
        visibleItemCount: Int,
        totalItemCount: Int
    ) {
        val totalRecordsCount = totalRecords.toInt()
        if(totalRecordsCount==totalItemCount)
            gridview.setOnScrollListener(null)
        currentFirstVisPos = view!!.lastVisiblePosition
        if((firstVisibleItem + visibleItemCount >= totalItemCount)&&!loading && totalItemCount<totalRecordsCount){
            loading=true
            loadMore()
        }
    }

    private var loading:Boolean=false
    private fun loadMore() {
        pagecnt+=1
        getDataFromApi(pagecnt)
    }
    override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
        //TODO("Not yet implemented")
    }

}