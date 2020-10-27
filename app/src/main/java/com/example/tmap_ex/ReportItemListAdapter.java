package com.example.tmap_ex;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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

    public static class ViewHolder {
        ImageView category_imageView;
        TextView category_textView;
        TextView location_textView;
        TextView date_textView;
        TextView state_textView;
        TextView resolved_date_textView;
    }
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

    // 아이템을 뷰에 출력
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();

        if(convertView == null) {
            LayoutInflater li = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
            convertView = li.inflate(R.layout.recyclerview_item, null);

            holder.category_imageView = (ImageView)convertView.findViewById(R.id.category_imageView);
            holder.category_textView = (TextView)convertView.findViewById(R.id.category_textView);
            holder.location_textView = (TextView)convertView.findViewById(R.id.location_textView);
            holder.date_textView = (TextView)convertView.findViewById(R.id.date_textView);
            holder.state_textView = (TextView)convertView.findViewById(R.id.state_textView);
            holder.resolved_date_textView = (TextView)convertView.findViewById(R.id.resolved_date_textView);

            convertView.setTag(holder);
        } else { holder = (ViewHolder) convertView.getTag(); }

        holder.category_imageView.setImageDrawable(list_itemArrayList.get(i).getCategory_image());
        holder.category_textView.setText(list_itemArrayList.get(i).getReport_category());
        holder.location_textView.setText(list_itemArrayList.get(i).getReport_location());
        holder.date_textView.setText(list_itemArrayList.get(i).getReport_date().toString());
        if(list_itemArrayList.get(i).getReport_state().equals("처리중")){
            holder.state_textView.setText(list_itemArrayList.get(i).getReport_state());
            holder.state_textView.setTextColor(Color.RED);
        }
        else if(list_itemArrayList.get(i).getReport_state().equals("해결됨")){
            holder.state_textView.setText(list_itemArrayList.get(i).getReport_state());
            holder.resolved_date_textView.setText(list_itemArrayList.get(i).getResolved_date());
            holder.state_textView.setTextColor(Color.BLUE);
        }

        return convertView;
    }

}
