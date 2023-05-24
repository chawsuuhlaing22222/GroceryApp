package com.padc.grocery.mvp.views

import com.padc.grocery.data.vos.GroceryVO

interface MainView : BaseView {
    fun showGroceryData(groceryList : List<GroceryVO>)
    fun showErrorMessage(message : String)
    fun showGroceryDialog(name: String, description: String, amount: String)
}