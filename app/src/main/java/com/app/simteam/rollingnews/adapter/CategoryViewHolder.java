package com.app.simteam.rollingnews.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.simteam.rollingnews.R;

/**
 * Created by sim on 3/30/2016.
 */
public class CategoryViewHolder extends RecyclerView.ViewHolder {
    public CardView categoryCardView;
    public TextView categoryTitle;
    public ImageView categoryImageView;
    public CheckBox  categoryCheckBox;

    public CategoryViewHolder(View itemView)  {
        super(itemView);
        categoryCardView = (CardView) itemView.findViewById(R.id.category_cardView);
        categoryTitle = (TextView) itemView.findViewById(R.id.category_title);
        categoryImageView = (ImageView) itemView.findViewById(R.id.category_imageView);
        categoryCheckBox = (CheckBox) itemView.findViewById(R.id.category_starAdded);
    }

}
