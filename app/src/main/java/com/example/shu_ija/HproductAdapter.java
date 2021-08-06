package com.example.shu_ija;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class HproductAdapter extends BaseAdapter {
    private ArrayList<ProductList> Items =new ArrayList<ProductList>();
    private LayoutInflater linflater ;
    Context context;

    public HproductAdapter(ArrayList<ProductList> Items ,Context context) {
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
        return Items.get(position).getnameProduct();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int i , View view, ViewGroup viewGroup){
        LayoutInflater linflater = LayoutInflater.from(context);
        view=linflater.inflate(R.layout.activity_hproductitem,null);
        TextView txt_nameProduct = view.findViewById(R.id.txtProduit);
        TextView txtCategory = view.findViewById(R.id.txtCategory);
        TextView txt_model = view.findViewById(R.id.txtmodel);
        TextView txtunit = view.findViewById(R.id.txtunit);
        TextView txtcount = view.findViewById(R.id.txtcount);
        LinearLayout img_plus = view.findViewById(R.id.img_plus);
        LinearLayout img_mins= view.findViewById(R.id.img_mins);
        String model = Items.get(i).getnameModel() +" "+Items.get(i).getnameMark();
        txtCategory.setText(Items.get(i).getnameCategory());
        txt_nameProduct.setText(Items.get(i).getnameProduct());
        txt_model.setText(model);
        txtunit.setText(Items.get(i).getunit());
        txtcount.setText("" + Integer.parseInt(Items.get(i).getquantity()));



        return view ;

    }



}