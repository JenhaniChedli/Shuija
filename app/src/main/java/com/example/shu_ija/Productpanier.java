package com.example.shu_ija;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

import static java.lang.Float.parseFloat;

public class Productpanier extends AppCompatActivity {
    ImageView Back;
    TextView txtnamelist,txtdate,txt_nbproduct,txt_price;
    String date,idhList,nameList,Pos,nbproduct,idPanier,prixtotal,IdPos;
    ListView listpanierview;
    RelativeLayout rlt_cart;
    static TextView txt_countcard;
    ProductpanierAdapter productpanierAdapter;
    ArrayList<ItemProductpanier> Items= new ArrayList<ItemProductpanier>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productpanier);

        Back =  findViewById(R.id.back);
        txt_price=findViewById(R.id.txt_price);
        listpanierview = findViewById(R.id.listpanierview);
        txtnamelist =  findViewById(R.id.txtnamelist);
        txtdate =  findViewById(R.id.txtdate);
        txt_countcard = findViewById(R.id.txt_countcard);
        rlt_cart= findViewById(R.id.rlt_cart);
        txt_nbproduct =  findViewById(R.id.txt_nbproduct);
        Back.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Items.clear();
        productpanierAdapter = new ProductpanierAdapter(Items, getApplicationContext());
        listpanierview.setAdapter(productpanierAdapter);
        Getintent();
        Getpanierproduct(idPanier);
    }

    public void Getintent() {
        Intent intent = getIntent();
        date = intent.getStringExtra("date");
        idhList = intent.getStringExtra("idhList");
        nameList = intent.getStringExtra("nameList");
        Pos= intent.getStringExtra("Pos");
        nbproduct= intent.getStringExtra("nbproduct");
        idPanier = intent.getStringExtra("idPanier");
        prixtotal= intent.getStringExtra("prixtotal");
        IdPos= intent.getStringExtra("IdPos");
        String[] arrOfStr = date.split(" ", 2);
        String date2 = arrOfStr[0];
        txtnamelist.setText(nameList);
        txtdate.setText(Pos+" "+date2);
        txt_price.setText(prixtotal);
        txt_nbproduct.setText(nbproduct);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),ListPaniers.class);
        intent.putExtra("idhList",idhList);
        intent.putExtra("nameList",nameList);
        intent.putExtra("date",date);
        startActivity(intent);
    }

    public void Getpanierproduct(String idPanier){
        Items.clear();
        final JSONObject jsonBodyObj = new JSONObject();
        try {
            jsonBodyObj.put("idPanier", idPanier);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();

        StringRequest Stringrequest = new StringRequest(Request.Method.POST, ActivityLink.list + "/Panier/byid",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("msg")) {
                                JSONArray jArray = new JSONArray(jsonObject.getString("listAchat"));
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
                                    String prix =jsonObj.getString("prix");
                                    Items.add(new ItemProductpanier(nameProduct,nameCategory,nameMark,nameModel,unit,quantity,idModel,idProduct,idMark,prix));
                                }
                                productpanierAdapter.notifyDataSetChanged();

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

                params.put("idPanier", idPanier);

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

}