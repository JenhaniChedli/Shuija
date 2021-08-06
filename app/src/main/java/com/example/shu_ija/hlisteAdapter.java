package com.example.shu_ija;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class hlisteAdapter extends BaseAdapter {
    private ArrayList<hlistItem> Items =new ArrayList<hlistItem>();
    private LayoutInflater linflater ;
    Context context;

    public hlisteAdapter(ArrayList<hlistItem> Items ,Context context) {
        this.Items = Items;
        this.linflater = LayoutInflater.from(context);
        this.context = context ;

    }



    @Override
    public int getCount() {
        return Items.size();
    }
    @Override
    public String getItem(int position) {
        return Items.get(position).getnameList();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int i , View view, ViewGroup viewGroup){
        LayoutInflater linflater = LayoutInflater.from(context);
        View view1=linflater.inflate(R.layout.activity_hlist_item,null);
        TextView txt_idlist = (TextView) view1.findViewById(R.id.idhList);
        TextView txt_namelist = (TextView) view1.findViewById(R.id.nameList);
        TextView txt_dateAdded = (TextView) view1.findViewById(R.id.date);
        txt_idlist.setText(Items.get(i).getidhList());
        txt_namelist.setText(Items.get(i).getnameList());
        txt_dateAdded.setText(Items.get(i).getdate());
        return view1 ;
    }



}
