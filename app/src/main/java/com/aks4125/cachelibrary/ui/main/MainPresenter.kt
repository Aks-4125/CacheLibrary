package com.aks4125.cachelibrary.ui.main

import com.aks4125.cachelibrary.models.PinterestModel
import com.aks4125.cachex.AppLogger
import com.aks4125.cachex.DownloadUtils
import com.aks4125.cachex.interfaces.IDataBridge
import com.aks4125.cachex.model.DataBridge
import com.aks4125.cachex.model.FileDataBridge
import com.aks4125.cachex.model.FileTypeEnum
import com.google.gson.Gson
import java.nio.charset.Charset

class MainPresenter(var mainView: IMainView) : MainContractor.IMainPresenter {

    private var mProvider: DownloadUtils? = null
    private lateinit var mBridge: DataBridge
    private val mTag = MainPresenter::class.java.simpleName

    private fun initializeProviders() {
        mProvider = DownloadUtils.getInstance()
    }

    override fun getFile(mUrl: String) {
        initializeProviders()
        mBridge = FileDataBridge(mUrl, object : IDataBridge {
            override fun onStart(mData: DataBridge?) {
                AppLogger.d(mTag, "onStartCalled for File(Json)")
                AppLogger.d(mTag, "file type->${mData?.fileType}")
                mainView.showProgress(true)

            }

            override fun onSuccess(mData: DataBridge?) {
                AppLogger.d(mTag, "onSuccess for File(Json)")
                AppLogger.d(mTag, "source----------------->${mData?.source}")
                AppLogger.d(mTag, "file length->${mData?.dataArray?.size}")

                if (mData?.fileType == FileTypeEnum.JSON) {// handle for JSON
                    val str = String(mData.dataArray, Charset.defaultCharset())
                    val pinList: List<PinterestModel> = Gson().fromJson(str, Array<PinterestModel>::class.java).toList()
                    AppLogger.d(mTag, "Response size-->${pinList.size}")
                    onItemsLoaded(pinList)
                }
                mainView.showProgress(false)

            }

            override fun onFailure(mData: DataBridge?, statusCode: Int, errorResponse: ByteArray?, e: Throwable?) {
                AppLogger.d(mTag, "onFailure for File(Json)")
                AppLogger.d(mTag, e?.message)
                mainView.showProgress(false)
            }

            override fun onRetry(mData: DataBridge?, retryNumber: Int) {
                AppLogger.d(mTag, "onRetry Called")
                mainView.showProgress(false)
            }
        })
        mProvider?.getRequest(mBridge)

    }


    private fun onItemsLoaded(items: List<PinterestModel>) { // submit result to view
        mainView.apply {
            setItems(items)
        }
    }

}