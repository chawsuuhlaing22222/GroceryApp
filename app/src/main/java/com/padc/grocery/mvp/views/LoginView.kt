package com.padc.grocery.mvp.views

interface LoginView : BaseView {
    fun navigateToHomeScreen()
    fun navigateToRegisterScreen()
    fun setDisplayStyle(style:Int)
}