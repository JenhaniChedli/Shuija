package com.example.shu_ija;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
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

import static java.lang.Float.parseFloat;

public class Hproduct extends AppCompatActivity {
    ImageView Back;
    TextView txtnamelist;
    static TextView txt_nbproduct;
    String nameList;
    String idhList,date,idList;
    String nbproduct;
    LinearLayout modecourse ;
    ListView listproductview;
    static HproductAdapter hproductAdapter;
    static ArrayList<ProductList> ItemsProducts = new ArrayList<ProductList>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hproduct);
        Back =  findViewById(R.id.back);
        txtnamelist =  findViewById(R.id.txtnamelist);
        txt_nbproduct =findViewById(R.id.txt_nbproduct);
        modecourse =findViewById(R.id.modecourse);
        ItemsProducts.clear();
        ItemsProducts.addAll(ListPaniers.getproducts());
        txt_nbproduct.setText(""+ItemsProducts.size());
        listproductview=findViewById(R.id.listproductview);
        hproductAdapter = new HproductAdapter(ItemsProducts, getApplicationContext());
        listproductview.setAdapter(hproductAdapter);
        //Toast.makeText(getApplicationContext(), "T3det : " + ItemsProducts.size(), Toast.LENGTH_LONG).show();
        Getintent();
        Back.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (ItemsProducts.size()==0){
            modecourse.setEnabled(false);
        }
        modecourse.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Hmode_course.class);
                intent.putExtra("idList",idList);
                intent.putExtra("idhList",idhList);
                intent.putExtra("nameList",nameList);
                intent.putExtra("date",date);
                intent.putExtra("test",true);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), ListPaniers.class);
        i.putExtra("idhList",idhList);
        i.putExtra("nameList",nameList);
        i.putExtra("date",date);
        startActivity(i);
    }
    public void Getintent() {
        Intent intent = getIntent();
        idhList = intent.getStringExtra("idhList");
        nameList = intent.getStringExtra("nameList");
        GetIdList(idhList);
        date=intent.getStringExtra("date");
        txtnamelist.setText(nameList);

    }
    public void GetIdList(String idhList){
        final JSONObject jsonBodyObj = new JSONObject();
        try {
            jsonBodyObj.put("idHistoriqueList", idhList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();

        StringRequest Stringrequest = new StringRequest(Request.Method.POST, ActivityLink.list + "/historiqueList/list",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                             idList = jsonObject.getString("idList");
                          //  Toast.makeText(getApplicationContext(), "T3det : " + idList, Toast.LENGTH_LONG).show();
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

    public static ArrayList getproductpaniers(){
        return ItemsProducts;
    }
}