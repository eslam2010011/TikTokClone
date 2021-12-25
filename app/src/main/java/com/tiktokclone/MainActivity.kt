package com.tiktokclone

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.tiktokclone.record.RecorderActivity
import com.tiktokclone.viewmodel.model.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = findNavController(R.id.nav_host_fragment)
        nav.setupWithNavController(navController)
        navController.addOnDestinationChangedListener(this)
        image_view_add_icon.setOnClickListener {
            startActivity(
                Intent(MainActivity@ this, RecorderActivity::class.java)
            )
        }
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.navigation_home -> {
                changeStatusBarColor(R.color.colorBlack)
                val colorDark = ContextCompat.getColorStateList(
                    this,
                    R.color.white
                )
                val colorBlack = ContextCompat.getColorStateList(
                    this,
                    R.color.colorBlack
                )

                nav.backgroundTintList = colorBlack
                nav.itemTextColor = colorDark
                nav.itemIconTintList = colorDark
                image_view_add_icon.setImageResource(R.drawable.ic_add_icon_dark)
            }

            else -> {
                changeStatusBarColor(R.color.colorWhite)
                val colorDark = ContextCompat.getColorStateList(
                    this,
                    R.color.black
                )

                val colorWhite = ContextCompat.getColorStateList(
                    this,
                    R.color.colorWhite
                )

                nav.backgroundTintList = colorWhite
                nav.itemTextColor = colorDark
                nav.itemIconTintList = colorDark
                image_view_add_icon.setImageResource(R.drawable.ic_add_icon_light)
            }


        }
    }

    fun setupContentWindow() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    fun changeStatusBarColor(@ColorRes barColor: Int) {
        window.apply {
            statusBarColor = getColorFromRes(barColor)
        }
    }

    private fun getColorFromRes(barColor: Int): Int {
        return ContextCompat.getColor(this, barColor)
    }

}