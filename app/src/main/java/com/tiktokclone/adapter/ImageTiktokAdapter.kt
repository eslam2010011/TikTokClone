package com.tiktokclone.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.tiktokclone.R
import android.annotation.SuppressLint
import android.view.View
import com.bumptech.glide.Glide
import androidx.appcompat.widget.AppCompatImageView
import com.tiktokclone.data.datasource.model.inner.Items
import java.util.*

class ImageTiktokAdapter : RecyclerView.Adapter<ImageTiktokAdapter.ViewHolder>() {
    var mVideoBeans: MutableList<Items>? = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ViewHolder(itemView)
    }

    fun addAll(videoBeans: List<Items>?) {
        mVideoBeans!!.addAll(videoBeans!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val context = holder.itemView.context
        val (_, _, _, video) = randomImagesItem
        Glide.with(context)
            .load(video!!.cover)
            .placeholder(android.R.color.white).centerCrop()
            .into(holder.video_p)
        holder.mPosition = position
    }

    val randomImagesItem: Items
        get() = mVideoBeans!![Random().nextInt(mVideoBeans!!.size)]

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        val (id, desc, createTime, video, author, music, stats, originalItem, officialItem, secret, forFriend, liked, itemCommentStatus, showNotPass, vl1, itemMute, authorStats, privateItem, duetEnabled, stitchEnabled, shareEnabled, isAd) = mVideoBeans!![holder.mPosition]
    }

    override fun getItemCount(): Int {
        return if (mVideoBeans != null) mVideoBeans!!.size else 0
    }

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mPosition = 0
        var video_p: AppCompatImageView

        init {
            video_p = itemView.findViewById(R.id.video_p)
            itemView.tag = this
        }
    }
}