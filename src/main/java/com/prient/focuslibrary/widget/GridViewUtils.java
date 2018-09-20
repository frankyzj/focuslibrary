package com.prient.focuslibrary.widget;

import android.content.Context;
import android.widget.GridView;

import com.prient.focuslibrary.FunctionUtils;

/**
 * 作者：prient
 * 时间：2018/9/18 16:20
 * 功能介绍：
 * 修改时间：
 * 修改说明：
 */
public class GridViewUtils {

    public static int initilizeGridView(Context context, GridView gridView, int NUM_OF_COLUMNS) {
        int columnWidth = (FunctionUtils.getScreenWidth(context) - (NUM_OF_COLUMNS + 1)) / NUM_OF_COLUMNS;

        gridView.setNumColumns(NUM_OF_COLUMNS);
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setHorizontalSpacing(1);
        gridView.setVerticalSpacing(1);
        return columnWidth;
    }
}
