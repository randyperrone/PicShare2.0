package com.example.randy.picshare20;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.randy.picshare20.Model.Upload;

import java.util.ArrayList;

/**
 * Created by Randy on 3/28/2017.
 */

public class PictureGalleryAdapter extends RecyclerView.Adapter<PictureGalleryViewHolder> {
    private Context context;
    private ArrayList<Upload> URL;

    public PictureGalleryAdapter(Context context, ArrayList<Upload> URL) {
        this.URL = URL;
        this.context = context;
    }

    @Override
    public PictureGalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new PictureGalleryViewHolder(card);
    }

    @Override
    public void onBindViewHolder(PictureGalleryViewHolder holder, int position) {
        Upload tempURL = URL.get(position);
        Glide.with(context)
         .load(tempURL.getUrl())
         .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return URL.size();
    }
}

class PictureGalleryViewHolder extends RecyclerView.ViewHolder {
    public ImageView image;

    public PictureGalleryViewHolder(View itemView) {
        super(itemView);
        image = (ImageView)itemView.findViewById(R.id.gallery_image);
    }
}
