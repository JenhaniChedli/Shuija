package com.example.shu_ija;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class listeAdapter extends BaseAdapter {
        private  ArrayList<listItem> Items =new ArrayList<listItem>();
        private LayoutInflater linflater ;
        Context context;

        public listeAdapter(ArrayList<listItem> Items ,Context context) {
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
             view=linflater.inflate(R.layout.itemlist,null);
            TextView txt_idlist = (TextView) view.findViewById(R.id.idListe);
            TextView txt_namelist = (TextView) view.findViewById(R.id.nameListe);
            TextView txt_dateAdded = (TextView) view.findViewById(R.id.dateAdded);
            TextView txt_nProduit = (TextView) view.findViewById(R.id.nProduit);
            txt_idlist.setText(Items.get(i).getidList());
            txt_namelist.setText(Items.get(i).getnameList());
            txt_dateAdded.setText(Items.get(i).getdate());
            int j =Integer.parseInt( Items.get(i).getNProduit());
            String nproduct ;
            if (j<=1){
                nproduct = Items.get(i).getNProduit() + " Produit";
            }else{
                nproduct = Items.get(i).getNProduit() + " Produits";
            }

            txt_nProduit.setText(nproduct);
            return view ;
        }



}
