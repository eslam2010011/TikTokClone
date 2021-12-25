package com.tiktokclone.ui.home.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.tiktokclone.utils.CustomExoMediaPlayer
import com.tiktokclone.R
import com.tiktokclone.utils.TikTokRenderViewFactory
import com.tiktokclone.adapter.TiktokAdapter
import com.tiktokclone.data.datasource.model.inner.Items
import com.tiktokclone.ui.base.BaseFragment
import com.tiktokclone.utils.Utils
import com.tiktokclone.utils.VerticalViewPager
import com.tiktokclone.utils.cache.PreloadManager
import com.tiktokclone.viewmodel.model.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.*
import xyz.doikki.videoplayer.util.L

@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val mainViewModel by viewModels<SharedViewModel>()

    private var mCurPos = 0
    private var mTiktok3Adapter: TiktokAdapter? = null
    private var mPreloadManager: PreloadManager? = null
    private var mVideoView: CustomExoMediaPlayer? = null
    private var mViewPagerImpl: RecyclerView? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view.context)
    }

    fun initView(context: Context) {
        initViewPager()
        initVideoView(context)
        mPreloadManager = PreloadManager.getInstance(context)
        addData()
        vp2.post(Runnable {
            startPlay(0)
        })

    }

    private fun initVideoView(context: Context) {
        mVideoView = CustomExoMediaPlayer(context)
        mVideoView?.setLooping(true)
        mVideoView?.setRenderViewFactory(TikTokRenderViewFactory.create())
    }


    private fun initViewPager() {
        vp2?.setOffscreenPageLimit(4)
        mTiktok3Adapter = TiktokAdapter()
        vp2?.setAdapter(mTiktok3Adapter)
        vp2?.setOverScrollMode(View.OVER_SCROLL_NEVER)
        vp2?.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            private var mCurItem = 0
            private var mIsReverseScroll = false
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (position == mCurItem) {
                    return
                }
                mIsReverseScroll = position < mCurItem
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == mCurPos) return
                vp2?.post(Runnable { startPlay(position) })
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == VerticalViewPager.SCROLL_STATE_DRAGGING) {
                    mCurItem = vp2?.getCurrentItem()!!
                }
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    mPreloadManager!!.resumePreload(mCurPos, mIsReverseScroll)
                } else {
                    mPreloadManager!!.pausePreload(mCurPos, mIsReverseScroll)
                }
            }
        })

         mViewPagerImpl = vp2?.getChildAt(0) as RecyclerView
    }


    private fun startPlay(position: Int) {
        val count = mViewPagerImpl!!.childCount
        for (i in 0 until count) {
            val itemView = mViewPagerImpl!!.getChildAt(i)
            val viewHolder: TiktokAdapter.ViewHolder = itemView.tag as TiktokAdapter.ViewHolder
            if (viewHolder.mPosition === position) {
                mVideoView!!.release()
                Utils.removeViewFormParent(mVideoView)
                val tiktokBean: Items = mTiktok3Adapter?.mVideoBeans?.get(position)!!
                val playUrl = mPreloadManager!!.getPlayUrl(tiktokBean.video?.downloadAddr)
                L.i("startPlay: position: $position  url: $playUrl")
                mVideoView!!.setUrl(playUrl)
                viewHolder.mPlayerContainer.addView(mVideoView, 0)
                mVideoView!!.start()
                mCurPos = position
                break
            }
        }
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


    public override fun onResume() {
        super.onResume()
        if (mVideoView != null) {
            mVideoView!!.resume()
        }
    }


    public override fun onPause() {
        super.onPause()
        if (mVideoView != null) {
            mVideoView!!.pause()
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (mVideoView != null) {
            mVideoView!!.release()
        }
    }

}