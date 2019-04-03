package com.aks4125.cachelibrary.ui

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.aks4125.cachelibrary.R
import com.aks4125.cachelibrary.util.Utils
import com.aks4125.cachex.AppLogger
import com.aks4125.cachex.DownloadUtils
import com.aks4125.cachex.interfaces.IDataBridge
import com.aks4125.cachex.model.DataBridge
import com.aks4125.cachex.model.ImageDataBridge
import kotlinx.android.synthetic.main.activity_full_screen_image.*

class FullScreenImageActivity : AppCompatActivity() {
    private var mProvider: DownloadUtils? = null
    private lateinit var mImageBridge: DataBridge
    private val mTag = FullScreenImageActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_full_screen_image)
        mProvider = DownloadUtils.getInstance() // get alive instance

        // considering in this case there will be img-url in intent at every possible case
        mImageBridge = ImageDataBridge(fullSizeImageView, intent.getStringExtra(Utils.IMAGE_URL),
                object : IDataBridge {
                    override fun onStart(mDownloadDataType: DataBridge) {
                        /*to do prior operations if required */
                    }

                    override fun onSuccess(mDownloadDataType: DataBridge) {
                        AppLogger.d(mTag, "onSuccess: came from" + mDownloadDataType.source)
                        val bitmap = BitmapFactory.decodeByteArray((mDownloadDataType as ImageDataBridge).dataArray, 0, mDownloadDataType.dataArray.size)
                        fullSizeImageView.setImageBitmap(bitmap)
                    }

                    override fun onFailure(mDownloadDataType: DataBridge, statusCode: Int, errorResponse: ByteArray, e: Throwable) {
                        if (fullSizeImageView != null)
                            fullSizeImageView.setImageResource(android.R.drawable.ic_menu_gallery)

                    }

                    override fun onRetry(mDownloadDataType: DataBridge, retryNo: Int) {

                    }
                })
        mProvider?.getRequest(mImageBridge) // request for image

    }
}
