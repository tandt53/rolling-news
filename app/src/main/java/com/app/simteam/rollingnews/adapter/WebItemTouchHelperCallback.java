package com.app.simteam.rollingnews.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.app.simteam.rollingnews.R;

/**
 * Created by MrNeo on 5/8/2016.
 */
public class WebItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private WebItemTouchHelperAdapter mAdapter;

    public WebItemTouchHelperCallback(WebItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = 0;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemOpen(viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            View itemView = viewHolder.itemView;

            TextView t = (TextView) itemView.findViewById(R.id.title);
            String str = (String) t.getText();

            Paint p = new Paint();
            if (dX > 0) {
            /* Set your color for positive displacement */
                p.setColor(Color.parseColor("#95a5a6"));
                // Draw Rect with varying right side, equal to displacement dX
                c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                        (float) itemView.getBottom(), p);

            } else {
            /* Set your color for negative displacement */
                p.setColor(Color.parseColor("#95a5a6"));
                // Draw Rect with varying left side, equal to the item's right side plus negative displacement dX
                c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                        (float) itemView.getRight(), (float) itemView.getBottom(), p);

            }
            // please check this - Kien
            float xText = itemView.getLeft() + 40;
            float yText = itemView.getTop() + (itemView.getBottom() - itemView.getTop())/2 + 20;
            Paint paint = new Paint();

            paint.setColor(Color.parseColor("#ffffff"));
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(55);
            paint.setAlpha(160);
            c.drawText("Đang mở " + str, xText, yText, paint);

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

    }
}