package com.padc.grocery.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.padc.grocery.R
import com.padc.grocery.mvp.presenters.LoginPresenter
import com.padc.grocery.mvp.presenters.impls.LoginPresenterImpl
import com.padc.grocery.mvp.views.LoginView
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : BaseActivity(), LoginView {

    companion object {
        var displayStyle=0
        fun newIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    private lateinit var mPresenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
       // setSupportActionBar(toolbar))

        setUpPresenter()
        setUpActionListeners()
        mPresenter.onUiReady(this,this)
    }

    private fun setUpActionListeners() {
        btnLogin.setOnClickListener {
            mPresenter.onTapLogin(this,etEmail.text.toString().trim(), etPassword.text.toString())
        }

        btnRegister.setOnClickListener {
            mPresenter.onTapRegister()
        }
    }

    private fun setUpPresenter() {
        mPresenter = getPresenter<LoginPresenterImpl, LoginView>()
    }

    override fun navigateToHomeScreen() {
        var username=mPresenter.getUserName()
        startActivity(MainActivity.newIntent(this,"Hello $username"))
    }

    override fun navigateToRegisterScreen() {
        startActivity(RegisterActivity.newIntent(this))
    }

    override fun setDisplayStyle(style: Int) {
        displayStyle=style
    }

    override fun showError(error: String) {
       Snackbar.make(window.decorView,error,Snackbar.LENGTH_SHORT).show()
    }
}