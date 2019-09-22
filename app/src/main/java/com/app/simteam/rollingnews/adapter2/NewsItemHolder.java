package com.app.simteam.rollingnews.adapter2;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.simteam.rollingnews.R;

/**
 * Created by sev_user on 9/1/2016.
 */
public class NewsItemHolder extends RecyclerView.ViewHolder {
    public ImageView img;
    public TextView title;
    public LinearLayout layoutItem;

    public NewsItemHolder(View itemView) {
        super(itemView);
        layoutItem = (LinearLayout) itemView.findViewById(R.id.news_item);
        img = (ImageView) itemView.findViewById(R.id.imgNewsIcon);
        title = (TextView) itemView.findViewById(R.id.title);
    }
}
