package com.chemistryaid.chemistryquizzer;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {
    View mView;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        mView=itemView;
    }
    public void setDetails(Context ctx,String title,String description,String image){
        TextView mTitleView=mView.findViewById(R.id.rTitle);
        TextView mDetailView=mView.findViewById(R.id.rDescription);
        ImageView mImageView=mView.findViewById(R.id.rImageView);
        mTitleView.setText(title);
        mDetailView.setText(description);
        Picasso.get().load(image).into(mImageView);
    }
}
