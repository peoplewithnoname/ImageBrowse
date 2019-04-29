package com.caihao.simple.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.caihao.simple.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

public class GanImageAdapter extends RecyclerArrayAdapter<String> {

    public GanImageAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_gan_img, null));
    }

    public class ViewHolder extends BaseViewHolder<String> {

        private ImageView ivCover;

        public ViewHolder(View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivCover);
        }

        @Override
        public void setData(String data) {
            super.setData(data);
            Glide.with(getContext()).load(data).into(ivCover);
        }
    }

}
