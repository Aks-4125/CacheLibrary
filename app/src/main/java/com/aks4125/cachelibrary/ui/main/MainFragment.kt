package com.aks4125.cachelibrary.ui.main


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.aks4125.cachelibrary.BuildConfig
import com.aks4125.cachelibrary.R
import com.aks4125.cachelibrary.adapter.ImageAdapter
import com.aks4125.cachelibrary.models.PinterestModel
import com.aks4125.cachelibrary.ui.FullScreenImageActivity
import com.aks4125.cachelibrary.util.Utils
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 *
 */
class MainFragment : Fragment(), IMainView {


    override fun showProgress(mVal: Boolean) {
        if (mRefreshLayout.isRefreshing)
            mRefreshLayout.isRefreshing = mVal
    }

    override fun setItems(items: List<PinterestModel>) {
        mList.clear()
        mList.addAll(items)
        mImageAdapter?.notifyDataSetChanged()
    }

    override fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    private val mList: MutableList<PinterestModel> = ArrayList()
    private var mImageAdapter: ImageAdapter? = null
    private val presenter = MainPresenter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        presenter.getFile(BuildConfig.JSON_URL)
        mRefreshLayout.setOnRefreshListener {
            presenter.getFile(BuildConfig.JSON_URL)
        }

    }

    private fun initUI() {
        mImageAdapter = ImageAdapter(mList as List<Any>?, ImageAdapter.OnItemClickListener { imageView, imageUrl, pos ->
            mUserFeed.scrollToPosition(pos)
            val intent = Intent(context, FullScreenImageActivity::class.java)
            intent.putExtra(Utils.IMAGE_URL, imageUrl)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity, imageView, "image")
            startActivity(intent, options.toBundle())
            (context as Activity).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        })
        mUserFeed.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        mUserFeed.adapter = mImageAdapter
    }
}
