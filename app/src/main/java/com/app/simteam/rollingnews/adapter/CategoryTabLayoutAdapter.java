package com.app.simteam.rollingnews.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;

import com.app.simteam.rollingnews.R;
import com.app.simteam.rollingnews.constant.Constant;
import com.app.simteam.rollingnews.data.CategoryItemData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sim on 3/30/2016.
 */
public class CategoryTabLayoutAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    List<CategoryItemData> list;
    Context context;
    ArrayList<Drawable> listDrawable;
    SharedPreferences sharedPreferences;

    int listSize;

    public CategoryTabLayoutAdapter(List<CategoryItemData> list, Context context) {
        this.list = list;
        this.context = context;
        sharedPreferences = context.getSharedPreferences(Constant.KEY_SHARED, Context.MODE_PRIVATE);
        listDrawable = new ArrayList<>();
//        getListDrawable(list);
    }

    private void getListDrawable(List<CategoryItemData> list) {
        for(CategoryItemData item : list){
            String uri = "drawable/icon_" + item.categoryImage +"_96x96";
            int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
            Drawable img = context.getResources().getDrawable(imageResource);
            listDrawable.add(img);
        }
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category_item, parent, false);
        CategoryViewHolder holder = new CategoryViewHolder(v);
        return holder;
    }


    @Override
    public void onBindViewHolder(final CategoryViewHolder holder, final int position) {
        final CategoryItemData item = list.get(position);
        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.categoryTitle.setText(item.categoryTitle);
//        holder.categoryImageView.setImageDrawable(listDrawable.get(position));

        String uri = "drawable/" + item.categoryImage;
        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
//        Drawable img = context.getResources().getDrawable(imageResource);
//        holder.categoryImageView.setImageDrawable(img);

        holder.categoryImageView.setImageResource(imageResource);
        // animate(holder);
        holder.categoryCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.categoryIsAdded = isChecked;

                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (item.categoryIsAdded && sharedPreferences.getInt("CATEGORY_" + position, 0) != 1) {
                    editor.putInt("CATEGORY_" + position, 1);
                } else if (!item.categoryIsAdded && sharedPreferences.getInt("CATEGORY_" + position, 0) != 0) {
                    editor.putInt("CATEGORY_" + position, 0);
                }
                editor.commit();
            }
        });
        holder.categoryCheckBox.setChecked(item.categoryIsAdded);
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
    public void insert(int position, CategoryItemData data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(CategoryItemData data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void animate(RecyclerView.ViewHolder viewHolder) {
        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(context, R.anim.anticipateovershoot_interpolator);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }
}