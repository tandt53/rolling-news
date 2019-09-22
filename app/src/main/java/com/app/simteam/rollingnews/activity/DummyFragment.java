package com.app.simteam.rollingnews.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.app.simteam.rollingnews.R;
import com.app.simteam.rollingnews.adapter.CategoryTabLayoutAdapter;
import com.app.simteam.rollingnews.adapter.WebItemTouchHelperCallback;
import com.app.simteam.rollingnews.constant.TabType;
import com.app.simteam.rollingnews.adapter.WebTabLayoutAdapter;

/**
 * Created by sim on 3/30/2016.
 */
public class DummyFragment extends Fragment {
    int color;
    int tabType;
    WebTabLayoutAdapter webAdapter;
    CategoryTabLayoutAdapter categoryAdapter;

    public DummyFragment(){

    }
    public DummyFragment(int color, int tab) {
        this.color = color;
        this.tabType = tab;
    }

    @SuppressLint("ValidFragment")
    public DummyFragment(int color) {
        this.color = color;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);

        final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.frame_recycle_view);
        frameLayout.setBackgroundColor(color);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        if (tabType == TabType.TAB_WEB) {
            webAdapter = new WebTabLayoutAdapter(MainActivity.weblist, getContext());
            //Added by mrneo on 05/08/2016
            ItemTouchHelper.Callback callback = new WebItemTouchHelperCallback(webAdapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(recyclerView);
            recyclerView.setAdapter(webAdapter);
        } else {
            categoryAdapter = new CategoryTabLayoutAdapter(MainActivity.categorylist, getContext());
            recyclerView.setAdapter(categoryAdapter);
        }
        return view;
    }


}
