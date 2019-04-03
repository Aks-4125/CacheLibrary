package com.aks4125.cachelibrary.ui.main

import com.aks4125.cachelibrary.models.PinterestModel

interface IMainView {
        fun showProgress(mVal: Boolean)
        fun setItems(items: List<PinterestModel>)
        fun showMessage(message: String)
    }