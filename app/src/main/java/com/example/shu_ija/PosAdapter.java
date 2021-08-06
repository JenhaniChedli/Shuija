package com.example.shu_ija;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PosAdapter  extends BaseAdapter {
    private  int ressource;
    private ArrayList<ItemPos> Items =new ArrayList<ItemPos>();
    private LayoutInflater linflater ;
    Context context;

    public PosAdapter(Context context, ArrayList<ItemPos> Items ) {
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
        return Items.get(position).getdesignation();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int i , View posview, ViewGroup posviewGroup){
        LayoutInflater linflater = LayoutInflater.from(context);
        posview=linflater.inflate(R.layout.listpos,posviewGroup,false);

        TextView adresspos = (TextView) posview.findViewById(R.id.adresspos);
        TextView namepos = (TextView) posview.findViewById(R.id.namepos);
        ImageView iv_check_box =(ImageView)  posview.findViewById(R.id.iv_check_box);

        adresspos.setText(Items.get(i).getaddress());
        namepos.setText(Items.get(i).getdesignation());
        boolean test =Items.get(i).getcheck();
        if(test){
            iv_check_box.setBackgroundResource(R.drawable.checked);
        }else{
            iv_check_box.setBackgroundResource(R.drawable.check);
        }



        return posview ;
    }

}
