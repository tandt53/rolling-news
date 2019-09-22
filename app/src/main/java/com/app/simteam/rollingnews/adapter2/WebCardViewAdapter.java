package com.app.simteam.rollingnews.adapter2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.simteam.rollingnews.R;
import com.app.simteam.rollingnews.activity.PreviewActivity;
import com.app.simteam.rollingnews.constant.Constant;

import java.util.ArrayList;

/**
 * Created by sev_user on 8/26/2016.
 */
public class WebCardViewAdapter extends RecyclerView.Adapter<WebCardViewHolder> {
    ArrayList<WebCardView> listWeb;
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public WebCardViewAdapter(Context context, ArrayList<WebCardView> listWeb) {
        //Log.d("TAN", "WebCardViewAdapter");
        this.context = context;
        this.listWeb = listWeb;
        sharedPreferences = context.getSharedPreferences(Constant.KEY_SHARED, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @Override
    public WebCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.d("TAN", "onCreateViewHolder");
        View v = LayoutInflater.from(context).inflate(R.layout.layout_web_item_edited, parent, false);
        WebCardViewHolder holder = new WebCardViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final WebCardViewHolder holder, final int position) {
        //Log.d("TAN", "onBindViewHolder");
        final WebCardView item = listWeb.get(position);

        holder.imgIcon.setImageDrawable(item.icon);

        if (item.isSelected) {
            holder.btnSelected.setBackgroundResource(R.drawable.bg_r_btn_blue);
//            holder.preview.setClickable(true);
//            holder.homePage.setClickable(true);
            holder.preview.setEnabled(true);
            holder.homePage.setEnabled(true);
        } else {
            holder.btnSelected.setBackgroundResource(R.drawable.bg_r_btn_grey);
//            holder.preview.setClickable(false);
//            holder.homePage.setClickable(false);
            holder.preview.setEnabled(false);
            holder.homePage.setEnabled(false);
        }


        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                updateSelectedItem(item, position);
                if (item.isSelected /*&& sharedPreferences.getInt("WEB_" + position, 0) != 1*/) {
                    item.isSelected = false;
                    editor.putInt("WEB_" + position, 0);
                    holder.preview.setEnabled(false);
                    holder.homePage.setEnabled(false);
                } else if (!item.isSelected /*&& sharedPreferences.getInt("WEB_" + position, 0) != 0*/) {
                    item.isSelected = true;
                    editor.putInt("WEB_" + position, 1);
                    holder.preview.setEnabled(true);
                    holder.homePage.setEnabled(true);
                }
                editor.commit();
                notifyDataSetChanged();
                Log.d("TAN", "position: " + position + "; web: " + item.name);
            }
        });

        holder.preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPreview(item);
            }
        });

        holder.homePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomePage(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listWeb.size();
    }

    void updateSelectedItem(WebCardView item, int position) {
        if (item.isSelected /*&& sharedPreferences.getInt("WEB_" + position, 0) != 1*/) {
            item.isSelected = false;
            editor.putInt("WEB_" + position, 0);
        } else if (!item.isSelected /*&& sharedPreferences.getInt("WEB_" + position, 0) != 0*/) {
            item.isSelected = true;
            editor.putInt("WEB_" + position, 1);
        }
        editor.commit();
        notifyDataSetChanged();
        Log.d("TAN", "position: " + position + "; web: " + item.name);
    }

    void openPreview(WebCardView item) {
        Log.d("TAN", "open preview: " + item.rssUrl);
        Intent intent = new Intent(context, PreviewActivity.class);
        intent.putExtra("RSS_URL", item.rssUrl);
        intent.putExtra("WEB_NAME", item.name);
        context.startActivity(intent);
    }

    void openHomePage(WebCardView item) {
        Log.d("TAN", "open home page: " + item.homeUrl);

        String url = item.homeUrl;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }
}
