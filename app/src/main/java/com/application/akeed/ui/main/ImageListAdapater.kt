package com.application.akeed.ui.main

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.application.akeed.DetailedActivity
import com.application.akeed.R
import com.application.akeed.network.DataList
import com.bumptech.glide.Glide
import com.google.gson.Gson


internal class ImageListAdapter internal constructor(context: Context, private val resource: Int, private val itemList: ArrayList<DataList>) : ArrayAdapter<ImageListAdapter.ItemHolder>(context, resource) {

    override fun getCount(): Int {
        return if (this.itemList != null) this.itemList.size else 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        val holder: ItemHolder
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, null)
            holder = ItemHolder()
            holder.layoutItem=convertView!!.findViewById(R.id.layout_item)
            holder.name = convertView.findViewById(R.id.textViewTitle)
            holder.type = convertView.findViewById(R.id.textViewType)
            holder.year = convertView.findViewById(R.id.textViewYear)
            holder.icon = convertView.findViewById(R.id.icon)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ItemHolder
        }

        var year:String= (this.itemList[position].Year)
        if(year.contains("–")) {
            val temp:List<String> = year.split("–")

            year=if(TextUtils.isEmpty(temp[1]))temp[0] else temp[1]

        }
        holder.name!!.text = this.itemList[position].Title
        holder.type!!.text = "Type :"+this.itemList[position].Type
        if(!TextUtils.isEmpty(year))
            holder.year!!.text =  (2020-year.toInt()).toString()+" years ago"
        var url:String?=""

        //for Loading of image on downloading using Glide
        if(this.itemList[position].Poster!=null) {
            url = this.itemList[position].Poster

            holder.icon?.let {
                Glide.with(context)
                    .load(url)
                    .circleCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .fallback(R.mipmap.ic_launcher)
                    .into(it)
            }
        }

        holder.layoutItem?.setOnClickListener {
            val datapass:String= Gson().toJson(itemList[position])
            val intent = Intent(context, DetailedActivity::class.java)
            intent.putExtra("data",datapass)
            intent.flags =  FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
        return convertView
    }

    internal class ItemHolder {
        var layoutItem: ConstraintLayout?=null
        var name: TextView? = null
        var type: TextView? = null
        var year: TextView? = null
        var icon: ImageView? = null
    }
}
