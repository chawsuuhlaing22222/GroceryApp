package com.padc.grocery.dialogs

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.padc.grocery.R
import com.padc.grocery.mvp.presenters.MainPresenter
import com.padc.grocery.mvp.presenters.impls.MainPresenterImpl
import kotlinx.android.synthetic.main.dialog_add_grocery.*
import kotlinx.android.synthetic.main.dialog_add_grocery.view.*
import java.io.IOException

class GroceryDialogFragment : DialogFragment() {

    private lateinit var mPresenter: MainPresenter
    private var filePath: Uri?=null
    private var bitmap:Bitmap?=null
    private var originalFilePath:String?=""
    lateinit var mView: View

    companion object {

        const val TAG_ADD_GROCERY_DIALOG = "TAG_ADD_GROCERY_DIALOG"
        const val BUNDLE_NAME = "BUNDLE_NAME"
        const val BUNDLE_DESCRIPTION = "BUNDLE_DESCRIPTION"
        const val BUNDLE_AMOUNT = "BUNDLE_AMOUNT"
        const val BUNDLE_IMAGE="BUNDLE_IMAGE"


        const val PICK_IMAGE_REQUEST_DIALOG = 22222

        fun newFragment(): GroceryDialogFragment {
            return GroceryDialogFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.dialog_add_grocery, container, false)

        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpPresenter()

        view.etGroceryName?.setText(arguments?.getString(BUNDLE_NAME))
        view.etDescription?.setText(arguments?.getString(BUNDLE_DESCRIPTION))
        view.etAmount?.setText(arguments?.getString(BUNDLE_AMOUNT))
        originalFilePath= arguments?.getString(BUNDLE_IMAGE)
        originalFilePath?.let {image->
            context?.let { Glide.with(it).load(image).into(view.ivGroceryImageDialog) }
        }

        view.btnAddGrocery.setOnClickListener {

           bitmap?.let {

               mPresenter.onTapAddGroceryWithImage(
                   etGroceryName.text.toString(),
                   etDescription.text.toString(),
                   etAmount.text.toString().toInt(),
                   bitmap
               )
           } ?: mPresenter.onTapAddGrocery(
               etGroceryName.text.toString(),
               etDescription.text.toString(),
               etAmount.text.toString().toInt(),
               originalFilePath ?: ""
               )

            dismiss()
        }

        view.btnFileUploadDialog.setOnClickListener {
            openGallery()
        }
    }

    private fun setUpPresenter() {
        activity?.let {
            mPresenter = ViewModelProviders.of(it).get(MainPresenterImpl::class.java)
        }
    }

     fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            PICK_IMAGE_REQUEST_DIALOG
        )
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST_DIALOG && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }

            filePath = data.data
            changeImageToBitmap()

        }
    }

    private fun changeImageToBitmap(){
        try {

            filePath?.let {file->
                if (Build.VERSION.SDK_INT >= 29) {

                    context?.let {context->
                        val source: ImageDecoder.Source =
                            ImageDecoder.createSource(context.contentResolver, file)

                        bitmap = ImageDecoder.decodeBitmap(source)
                        ivGroceryImageDialog.setImageResource(android.R.color.transparent);
                        ivGroceryImageDialog.setImageBitmap(bitmap)

                    }

                } else {

                    context?.let {
                         bitmap = MediaStore.Images.Media.getBitmap(
                            it.contentResolver, filePath
                        )
                       ivGroceryImageDialog.setImageResource(android.R.color.transparent);
                        ivGroceryImageDialog.setImageBitmap(bitmap)

                    }

                }
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}