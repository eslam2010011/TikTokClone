package com.tiktokclone.adapter

 import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.tiktokclone.R
import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
 import androidx.recyclerview.widget.LinearLayoutManager
import com.tiktokclone.data.datasource.model.inner.Items
import java.util.ArrayList

class DiscoveryTiktokAdapter : RecyclerView.Adapter<DiscoveryTiktokAdapter.ViewHolder>() {
    var mVideoBeans: MutableList<Items>? = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.discover_layout, parent, false)
        return ViewHolder(itemView)
    }

    fun addAll(videoBeans: List<Items>?) {
        mVideoBeans!!.addAll(videoBeans!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val context = holder.itemView.context
        val (_, _, _, _, author) = mVideoBeans!![position]
         holder.text_view_hashtag.text = author!!.nickname
        holder.setData(mVideoBeans)
        holder.mPosition = position
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        val (id, desc, createTime, video, author, music, stats, originalItem, officialItem, secret, forFriend, liked, itemCommentStatus, showNotPass, vl1, itemMute, authorStats, privateItem, duetEnabled, stitchEnabled, shareEnabled, isAd) = mVideoBeans!![holder.mPosition]
    }

    override fun getItemCount(): Int {
        return if (mVideoBeans != null) mVideoBeans!!.size else 0
    }

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mPosition = 0
        var text_view_hashtag: AppCompatTextView
        var recycler_view_data: RecyclerView
        fun setData(list: List<Items>?) {
            val imageTiktokAdapter = ImageTiktokAdapter()
            recycler_view_data.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            recycler_view_data.adapter = imageTiktokAdapter
            recycler_view_data.post {
                imageTiktokAdapter.addAll(list)
                imageTiktokAdapter.notifyDataSetChanged()
            }
        }

        init {
            text_view_hashtag = itemView.findViewById(R.id.text_view_hashtag)
            recycler_view_data = itemView.findViewById(R.id.recycler_view_data)
            itemView.tag = this
        }
    }
}