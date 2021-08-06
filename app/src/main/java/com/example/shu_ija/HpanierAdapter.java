package com.example.shu_ija;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class HpanierAdapter extends BaseAdapter {
    private  int ressource;
    private ArrayList<panierItem> Items =new ArrayList<panierItem>();
    private LayoutInflater linflater ;
    Context context;

    public HpanierAdapter(Context context, ArrayList<panierItem> Items ) {
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
        return Items.get(position).getdate();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int i , View productview, ViewGroup productviewGroup){
        LayoutInflater linflater = LayoutInflater.from(context);
        productview=linflater.inflate(R.layout.itempanier,productviewGroup,false);

        TextView txtdateAchat = (TextView) productview.findViewById(R.id.dateAchat);
        TextView txtpricepanier = (TextView) productview.findViewById(R.id.pricepanier);
        TextView txtnbproduit = (TextView) productview.findViewById(R.id.nbproduit);
        String date = Items.get(i).getdate();
        String[] arrOfStr = date.split(" ", 2);
        String pos = arrOfStr[0];
        //System.out.println( arrOfStr );

        int j =Integer.parseInt( Items.get(i).getNProduit());
        String nproduct ;
        if (j<=1){
            nproduct = Items.get(i).getNProduit() + " Produit";
        }else{
            nproduct = Items.get(i).getNProduit() + " Produits";
        }
        txtdateAchat.setText(Items.get(i).getPos()+" "+pos);
        txtpricepanier.setText(Items.get(i).getprixtotal());
        txtnbproduit.setText(nproduct);


        return productview ;
    }

}