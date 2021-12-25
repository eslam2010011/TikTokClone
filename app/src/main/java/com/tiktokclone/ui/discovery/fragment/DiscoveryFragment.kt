package com.tiktokclone.ui.home.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.tiktokclone.R
import com.tiktokclone.adapter.DiscoveryTiktokAdapter
import com.tiktokclone.ui.base.BaseFragment
import com.tiktokclone.viewmodel.model.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_discover.*

@AndroidEntryPoint
class DiscoveryFragment : BaseFragment(R.layout.fragment_discover) {

    private val mainViewModel by viewModels<SharedViewModel>()

    private var mTiktok3Adapter: DiscoveryTiktokAdapter? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view.context)
    }

    fun initView(context: Context) {
        initViewPager()
        addData()


    }


    private fun initViewPager() {
        mTiktok3Adapter = DiscoveryTiktokAdapter()
        recycler_dis.layoutManager = LinearLayoutManager(requireContext())
        recycler_dis?.setAdapter(mTiktok3Adapter)
        Glide.with(requireActivity())
            .load("https://p77-sg.tiktokcdn.com/obj/tiktok-obj/f2cb4aced920e7a397e391af749ade05")
            .placeholder(android.R.color.white)
            .into(image_banner)

    }


    fun addData() {
        mainViewModel.getData()
        mainViewModel.getVideos().observe(requireActivity(), Observer {
            activity?.runOnUiThread {
                val size = mTiktok3Adapter!!.itemCount
                mTiktok3Adapter?.addAll(it)
                mTiktok3Adapter?.mVideoBeans?.let { it1 ->
                    mTiktok3Adapter?.notifyItemRangeChanged(
                        size,
                        it1.size
                    )
                }
                Log.d("adssdsaddsa", mTiktok3Adapter?.itemCount.toString())

            }

        })


    }


}