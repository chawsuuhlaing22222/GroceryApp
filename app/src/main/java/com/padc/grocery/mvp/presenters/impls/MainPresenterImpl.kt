package com.padc.grocery.mvp.presenters.impls

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.padc.grocery.analytics.SCREEN_HOME
import com.padc.grocery.data.models.GroceryModelImpl
import com.padc.grocery.data.vos.GroceryVO
import com.padc.grocery.mvp.presenters.AbstractBasePresenter
import com.padc.grocery.mvp.presenters.MainPresenter
import com.padc.grocery.mvp.views.MainView

class MainPresenterImpl : MainPresenter, AbstractBasePresenter<MainView>() {

    val mGroceryModel = GroceryModelImpl
    private var mChosenGroceryForFileUpload: GroceryVO? = null

    override fun onTapAddGroceryWithImage(name: String, description: String, amount: Int, image:Bitmap?) {

        image?.let {
            mGroceryModel.uploadImageAndUpdateGrocery(GroceryVO(name, description, amount,""), image)
        }

    }

    override fun onTapAddGrocery(name: String, description: String, amount: Int, image: String) {
        mGroceryModel.addGrocery(name, description, amount, image)
    }


    override fun onPhotoTaken(bitmap: Bitmap) {
        mChosenGroceryForFileUpload?.let {
            mGroceryModel.uploadImageAndUpdateGrocery(it, bitmap)
        }
    }

    override fun onUiReady(context: Context,owner: LifecycleOwner) {

        sendEventsToFirebaseAnalytics(context, SCREEN_HOME)
        mGroceryModel.getGroceries(
            onSuccess = {
                mView.showGroceryData(it)
            },
            onFaiure = {
                mView.showErrorMessage(it)
            }
        )

        mView.displayToolbarTitle(mGroceryModel.getAppNameFromRemoteConfig())
    }

    override fun onTapDeleteGrocery(name: String) {
        mGroceryModel.removeGrocery(name)
    }

    override fun onTapEditGrocery(name: String, description: String, amount: Int,image:String) {
        mView.showGroceryDialog(name, description, amount.toString(),image)
    }

    override fun onTapFileUpload(grocery: GroceryVO) {
        mChosenGroceryForFileUpload = grocery
        mView.openGallery();
    }
}