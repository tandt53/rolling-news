package com.app.simteam.rollingnews.adapter2;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.app.simteam.rollingnews.R;

/**
 * Created by sev_user on 8/26/2016.
 */
public class WebCardViewHolder extends RecyclerView.ViewHolder {
    CardView cv;
    ImageView imgIcon;
    Button preview;
    Button homePage;
    Button btnSelected;

    public WebCardViewHolder(View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.cardView);
        imgIcon = (ImageView) itemView.findViewById(R.id.imgNewsIcon);
        preview = (Button) itemView.findViewById(R.id.preview);
        homePage = (Button) itemView.findViewById(R.id.openHomepage);
        btnSelected = (Button) itemView.findViewById(R.id.btn_selected);
    }
}
