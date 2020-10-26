package com.example.tmap_ex;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ReportItemListAdapter extends BaseAdapter {
    Context context;
    ArrayList<Report_item_list> list_itemArrayList;

    ImageView category_imageView;
    TextView category_textView;
    TextView location_textView;
    TextView date_textView;
    TextView state_textView;

    public ReportItemListAdapter(Context context, ArrayList<Report_item_list> list_itemArrayList) {
        this.context = context;
        this.list_itemArrayList = list_itemArrayList;
    }

    // 리스트뷰 아이템 개수
    @Override
    public int getCount() {
        return this.list_itemArrayList.size();
    }

    // 현재 아이템
    @Override
    public Object getItem(int i) {
        return this.list_itemArrayList.get(i);
    }

    // 아이템 인덱스
    @Override
    public long getItemId(int i) {
        return i;
    }

    //
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.listview_item, null);

            category_imageView = (ImageView)view.findViewById(R.id.category_imageView);
            category_textView = (TextView)view.findViewById(R.id.category_textView);
            location_textView = (TextView)view.findViewById(R.id.location_textView);
            date_textView = (TextView)view.findViewById(R.id.date_textView);
            state_textView = (TextView)view.findViewById(R.id.state_textView);
        }

        category_imageView.setImageDrawable(list_itemArrayList.get(i).getCategory_image());
        category_textView.setText(list_itemArrayList.get(i).getReport_category());
        location_textView.setText(list_itemArrayList.get(i).getReport_location());
        date_textView.setText(list_itemArrayList.get(i).getReport_date().toString());
        if(list_itemArrayList.get(i).getReport_state().equals("처리중")){
            state_textView.setText(list_itemArrayList.get(i).getReport_state());
            state_textView.setTextColor(Color.RED);
        }
        else if(list_itemArrayList.get(i).getReport_state().equals("해결됨")){
            state_textView.setText(list_itemArrayList.get(i).getReport_state());
            state_textView.setTextColor(Color.BLUE);
        }


        return view;
    }
}


