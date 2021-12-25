package com.tiktokclone.ui.profile.fragment


import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.tiktokclone.R
import com.tiktokclone.ui.base.BaseFragment
import com.tiktokclone.viewmodel.model.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_profile_.*

@AndroidEntryPoint
class ProfileFragment : BaseFragment(R.layout.activity_profile_) {

    private val mainViewModel by viewModels<SharedViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view.context)
    }

    fun initView(context: Context) {

          addData()


    }









    fun addData() {
        mainViewModel.onGetUserDetailsClick()
        mainViewModel.getUser().observe(requireActivity(), {
            Glide.with(requireActivity())
                .load(it.userInfo?.user?.avatarLarger)
                .placeholder(android.R.color.white)
                .into(user_image)
            username2_txt.text= it.userInfo?.user?.nickname ?:""
            follow_count_txt.text=it.userInfo?.stats?.followingCount
            fan_count_txt.text=it.userInfo?.stats?.followerCount
            heart_count_txt.text=it.userInfo?.stats?.likesCount

        })


    }


}