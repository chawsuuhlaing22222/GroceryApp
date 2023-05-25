package com.padc.grocery.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.padc.grocery.R
import com.padc.grocery.activities.LoginActivity
import com.padc.grocery.data.vos.GroceryVO
import com.padc.grocery.delegates.GroceryViewItemActionDelegate
import com.padc.grocery.viewholders.GroceryViewHolder
import com.zg.burgerjoint.adapters.BaseRecyclerAdapter

class GroceryAdapter(private val mDelegate: GroceryViewItemActionDelegate) : BaseRecyclerAdapter<GroceryViewHolder, GroceryVO>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryViewHolder {
        var view:View?=null
        if(LoginActivity.displayStyle==0){
            view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_grocery_item,parent,false)
        }else{
            view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_vertical_grocery_item,parent,false)
        }
        return GroceryViewHolder(view, mDelegate)
    }
}