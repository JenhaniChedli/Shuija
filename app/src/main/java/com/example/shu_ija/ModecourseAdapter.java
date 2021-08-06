package com.example.shu_ija;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ModecourseAdapter extends BaseAdapter {
    private ArrayList<ItemModeCourse> Items =new ArrayList<ItemModeCourse>();
    private LayoutInflater linflater ;
    Context context;

    public ModecourseAdapter(ArrayList<ItemModeCourse> Items ,Context context) {
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
        view=linflater.inflate(R.layout.item_mode_course,null);
        TextView txt_nameProduct = view.findViewById(R.id.txt_nameProduct);
        TextView txt_model = view.findViewById(R.id.txt_model);
        TextView txt_unit = view.findViewById(R.id.txt_unit);
        TextView txtcount = view.findViewById(R.id.txtcount);
        LinearLayout img_plus = view.findViewById(R.id.img_plus);
        LinearLayout img_mins= view.findViewById(R.id.img_mins);
        LinearLayout lvl_add= view.findViewById(R.id.lvl_add);
        String model = Items.get(i).getnameModel()+ " "+Items.get(i).getnameMark() ;

        txt_nameProduct.setText(Items.get(i).getnameProduct());
        txt_model.setText(model);
        txt_unit.setText(Items.get(i).getunit());
        txtcount.setText("" + Integer.parseInt(Items.get(i).getquantity()));
        img_plus.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                int q = Integer.parseInt(Items.get(i).getquantity()) +1;
                if(q>0){
                    Items.get(i).setquantity(""+q );
                }
                txtcount.setText("" + Integer.parseInt(Items.get(i).getquantity()));
            }
        });
        img_mins.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                int q = Integer.parseInt(Items.get(i).getquantity()) - 1;
                if(q>0){
                    Items.get(i).setquantity(""+q );
                }
                txtcount.setText("" + Integer.parseInt(Items.get(i).getquantity()));

            }
        });
        lvl_add.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {

             //   Toast.makeText(view.getContext(), "Add Panier ", Toast.LENGTH_LONG).show();
                ModeCourse.addpanier(i);
            }
        });


        return view ;

    }



}