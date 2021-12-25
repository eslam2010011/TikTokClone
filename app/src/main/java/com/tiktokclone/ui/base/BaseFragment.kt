package com.tiktokclone.ui.base

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import xyz.doikki.videoplayer.player.VideoView

open class BaseFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, windowInsets ->
            v.updatePadding(top = windowInsets.systemWindowInsetTop)
            windowInsets.consumeSystemWindowInsets()
        }
    }
}