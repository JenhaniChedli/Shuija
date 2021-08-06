package com.example.shu_ija;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;

public class ListPaniers extends AppCompatActivity {
    ImageView Back;
    TextView txtnamelist,txtdate,txt_nbpaniers,txt_price;
    String date,idhList,nameList;
    static ArrayList<panierItem> listpanier = new ArrayList<panierItem>();
    ListView listpanierview;
    RelativeLayout rlt_cart;
    static TextView txt_countcard;
    HpanierAdapter hpanierAdapter;
    static ArrayList<ProductList> ItemsProducts = new ArrayList<ProductList>();
    float pricetotal = 0;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_paniers);
       // Back =  findViewById(R.id.back);
        txt_price=findViewById(R.id.txt_price);
        listpanierview = findViewById(R.id.listpanierview);
        txtnamelist =  findViewById(R.id.txtnamelist);
        txtdate =  findViewById(R.id.txtdate);
        txt_countcard = findViewById(R.id.txt_countcard);
        rlt_cart= findViewById(R.id.rlt_cart);
        txt_nbpaniers =  findViewById(R.id.txt_nbpaniers);
        hpanierAdapter= new HpanierAdapter(getApplicationContext(), listpanier);
        listpanierview.setAdapter(hpanierAdapter);
        Getintent();
        Getpanierhistorique(idhList);
        rlt_cart.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Hproduct.class);
                intent.putExtra("idhList",idhList);
                intent.putExtra("nameList",nameList);
                intent.putExtra("date",date);
                startActivity(intent);
            }
        });
        listpanierview.setOnItemClickListener(new AdapterView.OnItemClickListener( ) {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),Productpanier.class);
                intent.putExtra("idhList",idhList);
                intent.putExtra("nameList",nameList);
                intent.putExtra("date",listpanier.get(position).getdate());
                intent.putExtra("Pos",listpanier.get(position).getPos());
                intent.putExtra("nbproduct",listpanier.get(position).getNProduit());
                intent.putExtra("idPanier",listpanier.get(position).getIdPanier());
                intent.putExtra("prixtotal",listpanier.get(position).getprixtotal());
                intent.putExtra("IdPos",listpanier.get(position).getIdPos());
                startActivity(intent);
            }
        });


    }
    public void Getintent() {
        Intent intent = getIntent();

        date = intent.getStringExtra("date");
        idhList = intent.getStringExtra("idhList");
        nameList = intent.getStringExtra("nameList");
        txtnamelist.setText(nameList);
        txtdate.setText(date);
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(i);
    }

    public void Getpanierhistorique(String idhList){
        pricetotal= 0;
        listpanier.clear();
        ItemsProducts.clear();
        final JSONObject jsonBodyObj = new JSONObject();
        try {
            jsonBodyObj.put("idHistoriqueList", idhList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();

        StringRequest Stringrequest = new StringRequest(Request.Method.POST, ActivityLink.list + "/Paniers/Hlist",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("msg")) {

                                String nbpaniers = jsonObject.getString("nbpaniers");
                                txt_nbpaniers.setText(nbpaniers);
                                JSONArray jArray = new JSONArray(jsonObject.getString("arraymodel"));
                                for (int j = 0; j < jArray.length(); j++) {
                                    JSONObject jsonObj = jArray.getJSONObject(j);
                                    String  nameProduct=jsonObj.getString("nameProduct");
                                    String  nameCategory=jsonObj.getString("nameCategory");
                                    String nameMark=jsonObj.getString("nameMark");
                                    String nameModel=jsonObj.getString("nameModel");
                                    String unit=jsonObj.getString("unit");
                                    String quantity=jsonObj.getString("quantity");
                                    String idModel=jsonObj.getString("idModel");
                                    String idProduct=jsonObj.getString("idProduct");
                                    String idMark=jsonObj.getString("idMark");
                                    ItemsProducts.add(new ProductList(nameProduct,nameCategory,nameMark,nameModel,unit,quantity,idModel,idProduct,idMark));
                                }
                                JSONArray jsonArray = new JSONArray(jsonObject.getString("paniers"));
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    String  dateAchat =  jsonObject.getString("dateAchat");
                                    String  idPanier=       jsonObject.getString("idPanier");
                                    String idPos=        jsonObject.getString("idPos");
                                    String Pos=        jsonObject.getString("Pos");
                                    String nbproduit=        jsonObject.getString("nbproduit");
                                    String prixTotal=       jsonObject.getString("prixTotal");
                                    pricetotal=  pricetotal + parseFloat(prixTotal);

                                    listpanier.add(new panierItem(idPos,Pos,idPanier,dateAchat,nbproduit,prixTotal));
                                }
                                hpanierAdapter.notifyDataSetChanged();
                                txt_price.setText(" "+String.valueOf(pricetotal));
                                txt_countcard.setText(""+ItemsProducts.size());

                             //  Toast.makeText(getApplicationContext(), "T3det : " + ItemsProducts.size()+ " : "+ listpanier.size(), Toast.LENGTH_LONG).show();

                            }else{
                                Toast.makeText(getApplicationContext(), "mt3detch!" , Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },  new com.android.volley.Response.ErrorListener()  {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("idHistoriqueList", idhList);

                return super.getParams();
            }

            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("x-api-Key", "Dlihjm3SE9rhH6I8VB9jx3Roz6uP9r6tghnChe");
                return headers;
            }

            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(Stringrequest);

    }
    public static ArrayList getproducts(){
        return ItemsProducts;
    }
}