package com.example.shu_ija;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.LinearLayout;
        import android.widget.TextView;

        import java.util.ArrayList;

public class ProductpanierAdapter extends BaseAdapter {
    private ArrayList<ItemProductpanier> Items =new ArrayList<ItemProductpanier>();
    private LayoutInflater linflater ;
    Context context;

    public ProductpanierAdapter(ArrayList<ItemProductpanier> Items ,Context context) {
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
        view=linflater.inflate(R.layout.activity_item_productpanier,null);
        TextView txt_nameProduct = view.findViewById(R.id.txt_nameProduct);
        TextView txt_model = view.findViewById(R.id.txt_model);
        TextView txt_unit = view.findViewById(R.id.txt_unit);
        TextView txtcount = view.findViewById(R.id.txtcount);
        TextView txtprice = view.findViewById(R.id.txtprice);
        String model = Items.get(i).getnameModel()+ " "+Items.get(i).getnameMark() ;

        txt_nameProduct.setText(Items.get(i).getnameProduct());
        txt_model.setText(model);
        txt_unit.setText(Items.get(i).getunit());
        StringBuffer prix = new StringBuffer(Items.get(i).getprice());

            int l = prix.length()-3;
            if(l==0){
                prix= prix.insert(l,'.');
                txtprice.setText("0"+prix);

            }else if(l==-1){
                txtprice.setText("0.0"+prix);
            }else if(l==-2){
                txtprice.setText("0.00"+prix);
            }else{
                prix= prix.insert(l,'.');
                txtprice.setText(prix);
            }
      //  txtprice.setText(Items.get(i).getprice());
        txtcount.setText("" + Integer.parseInt(Items.get(i).getquantity()));



        return view ;

    }



}