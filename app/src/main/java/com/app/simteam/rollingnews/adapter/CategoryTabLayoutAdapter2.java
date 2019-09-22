//package com.example.sev_user.cardviewexample.adapter;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.graphics.drawable.Drawable;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.widget.CompoundButton;
//
//import com.example.sev_user.cardviewexample.R;
//import com.example.sev_user.cardviewexample.constant.Constant;
//import com.example.sev_user.cardviewexample.data.CategoryItemData;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.StringTokenizer;
//
///**
// * Created by sev_user on 3/30/2016.
// */
//public class CategoryTabLayoutAdapter2 extends RecyclerView.Adapter<CategoryViewHolder> {
//    List<CategoryItemData> list;
//    Context context;
//
//    SharedPreferences sharedPreferences;
//
//    int listSize;
//
//    public CategoryTabLayoutAdapter2(List<CategoryItemData> list, Context context) {
//        this.list = list;
//        this.context = context;
//        sharedPreferences = context.getSharedPreferences(Constant.KEY_SHARED, Context.MODE_PRIVATE);
//    }
//
//    @Override
//    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        //Inflate the layout, initialize the View Holder
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category_item, parent, false);
//        CategoryViewHolder holder = new CategoryViewHolder(v);
//        v.setTag(holder);
//        return holder;
//
//    }
//
//
//    @Override
//    public void onBindViewHolder(final CategoryViewHolder holder, final int position) {
//        Log.d("TAN", "Come to view holder at: " + position);
//        final CategoryItemData item = list.get(position);
//        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
//        holder.categoryTitle.setText(item.categoryTitle);
//
//        String uri = "drawable/" + item.categoryImage;
//        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
//        Drawable img = context.getResources().getDrawable(imageResource);
//        holder.categoryImageView.setImageDrawable(img);
//        // animate(holder);
//        holder.categoryCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                item.categoryIsAdded = isChecked;
//
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                if (item.categoryIsAdded && sharedPreferences.getInt("CATEGORY_" + position, 0) != 1) {
//                    editor.putInt("CATEGORY_" + position, 1);
//                } else if (!item.categoryIsAdded && sharedPreferences.getInt("CATEGORY_" + position, 0) != 0) {
//                    editor.putInt("CATEGORY_" + position, 0);
//                }
//                editor.commit();
//                Log.d("TAN", "CATEGORY_" + position + ": " + list.get(position).categoryIsAdded);
//
//            }
//        });
//        holder.categoryCheckBox.setChecked(item.categoryIsAdded);
//    }
//
//    @Override
//    public int getItemCount() {
//        //returns the number of elements the RecyclerView will display
//        return list.size();
//    }
//
//    @Override
//    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//        super.onAttachedToRecyclerView(recyclerView);
//    }
//
//    // Insert a new item to the RecyclerView on a predefined position
//    public void insert(int position, CategoryItemData data) {
//        list.add(position, data);
//        notifyItemInserted(position);
//    }
//
//    // Remove a RecyclerView item containing a specified Data object
//    public void remove(CategoryItemData data) {
//        int position = list.indexOf(data);
//        list.remove(position);
//        notifyItemRemoved(position);
//    }
//
//    public void animate(RecyclerView.ViewHolder viewHolder) {
//        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(context, R.anim.anticipateovershoot_interpolator);
//        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
//    }
//}