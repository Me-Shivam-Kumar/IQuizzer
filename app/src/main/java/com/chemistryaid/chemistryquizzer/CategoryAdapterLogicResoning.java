package com.chemistryaid.chemistryquizzer;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryAdapterLogicResoning extends RecyclerView.Adapter<CategoryAdapterLogicResoning.viewHolder> {
    private List<CategoryModel> categoryModelList;
    public CategoryAdapterLogicResoning(List<CategoryModel> categoryModelList) {
        this.categoryModelList = categoryModelList;
    }
    @NonNull
    @Override
    public CategoryAdapterLogicResoning.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapterLogicResoning.viewHolder holder, int position) {
        holder.setData(categoryModelList.get(position).getUrl(),categoryModelList.get(position).getName(),categoryModelList.get(position).getSets());

    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    static class viewHolder extends RecyclerView.ViewHolder {
        private CircleImageView imageView;
        private TextView title;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.category_image_view);
            title=itemView.findViewById(R.id.cateogry_title);
        }
        void setData(String url, final String title, final int sets){
            Glide.with(itemView.getContext()).load(url).into(imageView);
            this.title.setText(title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent setIntent=new Intent(itemView.getContext(),SetsActivityLogicalResoning.class);
                    setIntent.putExtra("title",title);
                    setIntent.putExtra("sets",sets);
                    itemView.getContext().startActivity(setIntent);

                }
            });
        }
    }
    }


