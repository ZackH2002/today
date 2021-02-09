package com.example.today;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.today.base.BaseData;

import java.util.LinkedList;
import java.util.List;

public class MyListAdapter extends BaseAdapter {
    List<BaseData>mbaseDateList;
    Context mContext;
    public MyListAdapter(Context context,List<BaseData>baseDateList){
        this.mbaseDateList=baseDateList;
        this.mContext=context;
    }
    @Override
    public int getCount() {
        return mbaseDateList.size();
    }

    @Override
    public Object getItem(int position) {
        return mbaseDateList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder();
        if (view==null){
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view =layoutInflater.inflate(R.layout.item_time_line,null);
            viewHolder.text_time= (TextView) view.findViewById(R.id.item_time);
            viewHolder.text_title=(TextView)view.findViewById(R.id.item_title);
            viewHolder.text_detail=(TextView)view.findViewById(R.id.item_detail);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.text_time.setText(mbaseDateList.get(position).getTime()+"-"+mbaseDateList.get(position).getMonth()+"-"+mbaseDateList.get(position).getDay());
        viewHolder.text_title.setText(mbaseDateList.get(position).getTitle());
        viewHolder.text_detail.setText(mbaseDateList.get(position).getDetail());
        return view;
    }
    public class ViewHolder{
        public TextView text_time;
        public TextView text_title;
        public TextView text_detail;
    }
}



























