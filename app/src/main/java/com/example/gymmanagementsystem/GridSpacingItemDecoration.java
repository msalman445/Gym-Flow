package com.example.gymmanagementsystem;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
    private int spanCount;
    private int spacing;

    public GridSpacingItemDecoration(int spanCount, int spacing){
        this.spanCount = spanCount;
        this.spacing = spacing;
    }


    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildAdapterPosition(view);
        int column = position % spanCount;

        outRect.left = spacing - column  * spacing/spanCount;
        outRect.right = (column + 1) * spacing/spanCount;
        if (position < spanCount){
            outRect.top = spacing;
        }
        outRect.bottom = spacing;

    }
}
