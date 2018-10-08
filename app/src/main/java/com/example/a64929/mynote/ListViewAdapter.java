package com.example.a64929.mynote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {
    List<Note> list;
    LayoutInflater inflater=null;

    public ListViewAdapter(List<Note> list,Context context){
        this.list=list;
        this.inflater= LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View visible=null;
        if(view==null){
            visible=inflater.inflate(R.layout.list_item,null);
        }else{
            visible=view;
        }
        TextView title=(TextView)visible.findViewById(R.id.ItemTitle);
        TextView content=(TextView)visible.findViewById(R.id.ItemText);
        TextView time=(TextView)visible.findViewById(R.id.ItemTime);
        title.setText(list.get(i).getTitle());
        time.setText(list.get(i).getTime());
        content.setText(list.get(i).getContent());
        return visible;
    }
}
