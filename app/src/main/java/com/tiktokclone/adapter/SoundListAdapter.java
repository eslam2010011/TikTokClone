package com.tiktokclone.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tiktokclone.R;
import com.tiktokclone.data.datasource.model.sound.DataXXXX;

import java.util.ArrayList;


public class SoundListAdapter extends RecyclerView.Adapter<SoundListAdapter.CustomViewHolder> {
    public Context context;

    ArrayList<DataXXXX> datalist;
    AdapterClickListener adapterClickListener;

    public SoundListAdapter(Context context, ArrayList<DataXXXX> arrayList, AdapterClickListener listener) {
        this.context = context;
        datalist = arrayList;
        this.adapterClickListener = listener;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sound_layout, viewGroup, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }


    @Override
    public int getItemCount() {
        return datalist.size();
    }


    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int i) {

        DataXXXX item = (DataXXXX) datalist.get(i);
        holder.soundName.setText(item.getTitle());
        holder.descriptionTxt.setText(item.getTitle_short());
        holder.durationTimeTxt.setText(item.getDuration()+"");
        holder.bind(i, item, adapterClickListener);
        Glide.with(holder.itemView.getContext())
                .load(item.getAlbum().getCover())
                .placeholder(android.R.color.white)
                .into(holder.soundImage);


    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageButton done, favBtn;
        TextView soundName, descriptionTxt, durationTimeTxt;
        ImageView soundImage;

        public CustomViewHolder(View view) {
            super(view);
            done = view.findViewById(R.id.done);
            favBtn = view.findViewById(R.id.fav_btn);


            soundName = view.findViewById(R.id.sound_name);
            descriptionTxt = view.findViewById(R.id.description_txt);
            soundImage = view.findViewById(R.id.sound_image);

            durationTimeTxt = view.findViewById(R.id.duration_time_txt);
        }

        public void bind(final int pos, final DataXXXX item, final AdapterClickListener listener) {

            itemView.setOnClickListener(v -> {
                listener.onItemClick(v, pos, item);

            });

            done.setOnClickListener(v -> {
                listener.onItemClick(v, pos, item);

            });

            favBtn.setOnClickListener(v -> {
                listener.onItemClick(v, pos, item);

            });

        }


    }


}

