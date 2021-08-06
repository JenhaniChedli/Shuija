package com.example.shu_ija;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ProductAdapter extends BaseAdapter {
    private  int ressource;
    private ArrayList<ItemSearchProduct> Items =new ArrayList<ItemSearchProduct>();
    private LayoutInflater linflater ;
    Context context;

    public ProductAdapter(Context context, ArrayList<ItemSearchProduct> Items ) {
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
    public View getView(final int i , View productview, ViewGroup productviewGroup){
        LayoutInflater linflater = LayoutInflater.from(context);
        productview=linflater.inflate(R.layout.activity_item_search_product,productviewGroup,false);

        TextView txtProduit = (TextView) productview.findViewById(R.id.txtProduit);
        TextView txtCategory = (TextView) productview.findViewById(R.id.txtCategory);

        txtProduit.setText(Items.get(i).getnameProduct());
        txtCategory.setText(Items.get(i).getnameCategory());


    /* productview.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "item "+ i , Toast.LENGTH_LONG).show();
            }
        });*/



        return productview ;
    }

}
