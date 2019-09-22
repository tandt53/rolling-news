package com.app.simteam.rollingnews.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;

import com.app.simteam.rollingnews.R;
import com.app.simteam.rollingnews.activity.WebViewActivity;
import com.app.simteam.rollingnews.constant.Constant;
import com.app.simteam.rollingnews.data.WebItemData;

import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by sim on 3/30/2016.
 * Edited by mrneo on 5/8/2016: Implement WebItemTouchHelperAdapter
 */
public class WebTabLayoutAdapter extends RecyclerView.Adapter<WebViewHolder> implements WebItemTouchHelperAdapter{
    List<WebItemData> list;
    Context context;
    StringBuilder stringBuilder = new StringBuilder();

    SharedPreferences sharedPreferences;

    public WebTabLayoutAdapter(List<WebItemData> list, Context context) {
        this.list = list;
        this.context = context;
        sharedPreferences = context.getSharedPreferences(Constant.KEY_SHARED, Context.MODE_PRIVATE);
    }

    @Override
    public WebViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_web_item, parent, false);
        WebViewHolder holder = new WebViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final WebViewHolder holder, final int position) {
        final WebItemData item = list.get(position);

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.title.setText(item.title);

        String uri = "drawable/" + item.image;
        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
//        Drawable img = context.getResources().getDrawable(imageResource);
//        holder.imageView.setImageDrawable(img);
        holder.imageView.setImageResource(imageResource);
        holder.description.setText(item.description);
//        holder.checkBox.setChecked(list.get(position).isAdded);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.isAdded = isChecked;

                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (item.isAdded && sharedPreferences.getInt("WEB_" + position, 0) != 1) {
                    editor.putInt("WEB_" + position, 1);
                } else if (!item.isAdded && sharedPreferences.getInt("WEB_" + position, 0) != 0) {
                    editor.putInt("WEB_" + position, 0);
                }
                editor.commit();
            }
        });
        holder.checkBox.setChecked(item.isAdded);
        //animate(holder);
    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, WebItemData data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(WebItemData data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void animate(RecyclerView.ViewHolder viewHolder) {
        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(context, R.anim.anticipateovershoot_interpolator);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition, int itemIndex) {
        list.get(itemIndex).getLink().substring(0, list.get(itemIndex).getLink().indexOf("/rss"));
    }

    @Override
    public void onItemOpen(int position) {
        String itemLink = list.get(position).getLink();
        itemLink = itemLink.replace("http://","");
        StringTokenizer st = new StringTokenizer(itemLink, "/");

        String url = "http://" + st.nextToken();

        //Intent i = new Intent(Intent.ACTION_VIEW);
        Intent i = new Intent();
        i.setAction("com.app.simteam.rollingnews.action.WEBVIEW");
        //i.setData(Uri.parse(url));
        i.putExtra("url", url);
        context.startActivity(i);
        notifyDataSetChanged();
        Log.d("TAN", url);
    }
}
