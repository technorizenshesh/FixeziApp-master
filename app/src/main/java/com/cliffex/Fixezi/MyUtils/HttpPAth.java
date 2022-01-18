package com.cliffex.Fixezi.MyUtils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class HttpPAth {

    // public static String Urlpath="http://www.technorizen.co.in/WORKSPACE6/FIXEZI/webserv.php?";
    // public static String Urlpath="https://fixezi.com.au/fixezi_admin/FIXEZI/webserv.php?";
    public static String Urlpath = "https://fixezi.com.au/fixezi_admin/FIXEZI/webserv.php?";
    public static String IMAGE_PATH = "https://fixezi.com.au/fixezi_admin/FIXEZI/image/";

    public static void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem != null) {
                // This next line is needed before you call measure or else you won't get measured height at all. The listitem needs to be drawn first to know the height.
                listItem.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
