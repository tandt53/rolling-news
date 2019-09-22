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
public class WebViewHolder extends RecyclerView.ViewHolder {

    public CardView cv;
    public TextView title;
    public TextView description;
    public ImageView imageView;
    public CheckBox checkBox;

    public WebViewHolder(View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.cardView);
        title = (TextView) itemView.findViewById(R.id.title);
        description = (TextView) itemView.findViewById(R.id.description);
        imageView = (ImageView) itemView.findViewById(R.id.imageView);
        checkBox = (CheckBox) itemView.findViewById(R.id.starAdded);
    }

    public TextView getTitle(){
        return this.title;
    }
}