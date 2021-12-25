package com.tiktokclone.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.tiktokclone.R
import android.annotation.SuppressLint
import android.view.View
import com.bumptech.glide.Glide
import com.tiktokclone.utils.cache.PreloadManager
import com.google.android.material.imageview.ShapeableImageView
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import com.tiktokclone.data.datasource.model.inner.Items
import java.util.ArrayList

class TiktokAdapter : RecyclerView.Adapter<TiktokAdapter.ViewHolder>() {
    var mVideoBeans: MutableList<Items>? = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.tiktok_layout, parent, false)
        return ViewHolder(itemView)
    }

    fun addAll(videoBeans: List<Items>?) {
        mVideoBeans!!.addAll(videoBeans!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val context = holder.itemView.context
        val (_, desc, _, video, author, _, stats) = mVideoBeans!![position]
        Glide.with(context)
            .load(author!!.avatarMedium)
            .placeholder(android.R.color.white)
            .into(holder.image_view_profile_pic)
        holder.text_view_account_handle.text = author.nickname
        holder.text_view_video_description.text = desc
        holder.image_view_option_like_title.text = stats!!.likesCount
        holder.image_view_option_comment_title.text = stats.videoCount
        PreloadManager.getInstance(context).addPreloadTask(video!!.downloadAddr, position)
        holder.mPosition = position
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        val (_, _, _, video) = mVideoBeans!![holder.mPosition]
        PreloadManager.getInstance(holder.itemView.context).removePreloadTask(video!!.downloadAddr)
    }

    override fun getItemCount(): Int {
        return if (mVideoBeans != null) mVideoBeans!!.size else 0
    }

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image_view_profile_pic: ShapeableImageView
        var mPosition = 0
        var mPlayerContainer: FrameLayout
        var text_view_account_handle: AppCompatTextView
        var text_view_video_description: AppCompatTextView
        var image_view_option_like_title: AppCompatTextView
        var image_view_option_comment_title: AppCompatTextView

        init {
            mPlayerContainer = itemView.findViewById(R.id.container)
            image_view_profile_pic = itemView.findViewById(R.id.image_view_profile_pic)
            text_view_account_handle = itemView.findViewById(R.id.text_view_account_handle)
            text_view_video_description = itemView.findViewById(R.id.text_view_video_description)
            image_view_option_like_title = itemView.findViewById(R.id.image_view_option_like_title)
            image_view_option_comment_title =
                itemView.findViewById(R.id.image_view_option_comment_title)
            itemView.tag = this
        }
    }
}