package com.example.shu_ija;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModeCourse extends AppCompatActivity {
    ImageView back;
    TextView txtnamelist;
    TextView namePos;
    static TextView txt_countcard;
    static TextView txt_price;
    RelativeLayout rlt_cart;
    LinearLayout btn_buy ;
    ListView listproductsview;
    String idList;
    String nameList;
    String idPOS="0";
    String namePOS="Autre Point de vente";
    String Prixtotal="0.000";
    static int pricetotal;
    Dialog myDialog;
    static String localisation;
    boolean test =false;
    static ArrayList<ItemModeCourse> ItemsProducts = new ArrayList<ItemModeCourse>();
    static ArrayList<ItemModeCourse> basket = new ArrayList<ItemModeCourse>();
    static ArrayList<Panier> panier = new ArrayList<Panier>();
    static  ModecourseAdapter modecourseAdapter;
    PosAdapter posAdapter ;
    static ArrayList<ItemPos> listpos = new ArrayList<ItemPos>();
    int preSelectedIndex;
    FusedLocationProviderClient fusedLocationProviderClient;
    private JSONArray Cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mode_course);
        txtnamelist = findViewById(R.id.txtnamelist);
        listproductsview = findViewById(R.id.listproductsview);
        back=findViewById(R.id.back);
        namePos = findViewById(R.id.namePos);
        txt_price = findViewById(R.id.txt_price);
        txt_countcard = findViewById(R.id.txt_countcard);
        rlt_cart= findViewById(R.id.rlt_cart);
        btn_buy=findViewById(R.id.btn_buy);
        namePos.setText(namePOS);
        basket.clear();
        ItemsProducts.clear();
        panier.clear();
        myDialog = new Dialog(this);
        pricetotal=0000;
        Getintent();
        modecourseAdapter = new ModecourseAdapter(ItemsProducts, getApplicationContext());
        listproductsview.setAdapter(modecourseAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        rlt_cart.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                gobasket();
            }
        });
        btn_buy.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                addCart();
                Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(i);
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), ListProduct.class);
        ItemsProducts.clear();
        basket.clear();
        i.putExtra("nbproduct","0");
        i.putExtra("idList",idList);
        i.putExtra("nameList",nameList);
        startActivity(i);

    }
    public void Getintent() {
        Intent intent = getIntent();
        idList = intent.getStringExtra("idList");
        nameList = intent.getStringExtra("nameList");
        test =intent.getBooleanExtra("test",false);
        if(test){
            txtnamelist.setText(nameList);
            GetLocationMobile();
            getListdeprixMS(idList,idPOS);
        }else {
            basket.addAll(Basket.getBasket());
            ItemsProducts.addAll(Basket.getItemlist());
            idList = intent.getStringExtra("idList");
            nameList = intent.getStringExtra("nameList");
            Prixtotal = intent.getStringExtra("Prixtotal");
            String price= intent.getStringExtra("price");
            pricetotal= Integer.parseInt(price);
            namePOS = intent.getStringExtra("namePOS");
            idPOS = intent.getStringExtra("idPOS");
            txtnamelist.setText(nameList);
            txt_price.setText(Prixtotal);
            txt_countcard.setText(""+basket.size());
            namePos.setText(namePOS);
            Basket.clearbasket();
        }

    }
    public void GetLocationMobile(){
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>( ) {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if(location != null){
                        localisation = location.getLatitude()+","+location.getLongitude();
                        getPointsOfSale(localisation);
                    }
                }
            });
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }
    }
    public void getListdeprixMS(final String idList,final String idPOS) {
        final ArrayList<String> namePorducts = new ArrayList<String>();
        JSONObject jsonBodyObj = new JSONObject();
        try {
            jsonBodyObj.put("idList", idList);
            jsonBodyObj.put("idPointOfSale", "0");
        } catch (JSONException e) { }
        final String requestBody = jsonBodyObj.toString();

        StringRequest Stringrequest = new StringRequest(Request.Method.POST, ActivityLink.list + "/modeCourse",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj ;
                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.getString("status_code").equals("200")) {

                                 if (jsonObject.getString("arraymodel").equals("[]")){
                                    Toast.makeText(getApplicationContext(), "List Product Vide ", Toast.LENGTH_LONG).show();
                                } else{
                                     JSONArray jsonArray = new JSONArray(jsonObject.getString("arraymodel"));

                                     for (int i = 0; i < jsonArray.length(); i++) {

                                         jsonObj = jsonArray.getJSONObject(i);

                                         String  nameProduct=jsonObj.getString("nameProduct");
                                         String  nameCategory=jsonObj.getString("nameCategory");
                                         String nameMark=jsonObj.getString("nameMark");
                                         String nameModel=jsonObj.getString("nameModel");
                                         String unit=jsonObj.getString("unit");
                                         String quantity=jsonObj.getString("quantity");
                                         String idModel=jsonObj.getString("idModel");
                                         String idProduct=jsonObj.getString("idProduct");
                                         String idMark=jsonObj.getString("idMark");
                                         String price=jsonObj.getString("price");
                                         ItemsProducts.add(new ItemModeCourse(nameProduct,nameCategory,nameMark,nameModel,unit,quantity,idModel,idProduct,idMark,price));
                                     }

                                     modecourseAdapter.notifyDataSetChanged();
                                 }

                            }else{
                                if (jsonObject.getString("msg").equals("No list found!")) {
                                    Toast.makeText(getApplicationContext(), "No list found!", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), "check ip on list", Toast.LENGTH_LONG).show();
                                }

                            }
                        } catch (JSONException e) { }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idList", idList);
                params.put("idPointOfSale", idPOS);
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
    public void getPointsOfSale(final String localisation){
       listpos.clear();
        // location to get latitude and longtitude
        JSONObject jsonBodyObj = new JSONObject();
        try {
           // jsonBodyObj.put("localisation", localisation);
            jsonBodyObj.put("localisation", "36.809245,10.138077");//pour le test
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();

        StringRequest Stringrequest = new StringRequest(Request.Method.POST, ActivityLink.pointOfSale + "/pointOfSale/localisation",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObj ;
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(!jsonObject.getBoolean("isFound")){
                               // Toast.makeText(getApplicationContext(), "No point of sale found!", Toast.LENGTH_LONG).show();
                            }else {
                                if(!jsonObject.getBoolean("msg")){
                                    jsonObj = jsonObject.getJSONObject("result") ;
                                    listpos.add(new ItemPos(jsonObj.getString("idPointOfSale"),jsonObj.getString("designation"),jsonObj.getString("address"),true));
                                   // System.out.println( listpos.get(0).getdesignation());
                                }else{
                                    JSONArray jsonAry = jsonObject.getJSONArray("result") ;
                                    for (int i = 0; i < jsonAry.length(); i++) {
                                        jsonObj = jsonAry.getJSONObject(i);
                                        listpos.add(new ItemPos(jsonObj.getString("idPointOfSale"),jsonObj.getString("designation"),jsonObj.getString("address"),false));
                                    }
                                   listpos.add(new ItemPos("0","Autre Point de vente","",false));
                                    //  listpos.add(new ItemPos("0","Autre Point de vente","",true));
                                    popupPointOfSale(listpos);
                                }

                            }
                        } catch(JSONException e){

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error connexion Location not exact", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("localisation", localisation);
                return super.getParams();
            }

            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("x-api-Key", "ChEdLiZiEdKhOuLoUdYaSsInEsAiF");
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
    public void popupPointOfSale(ArrayList<ItemPos> listpos) {

        TextView txtok;
        TextView txtannuler;
        ListView poslist;
        preSelectedIndex=-1;
        myDialog.setContentView(R.layout.pointsdesventesdialog);

        txtok= myDialog.findViewById(R.id.txtok);
        txtannuler= myDialog.findViewById(R.id.txtannuler);
        poslist= (ListView) myDialog.findViewById(R.id.poslist);


       posAdapter = new PosAdapter(ModeCourse.this,listpos);
       poslist.setAdapter(posAdapter);

        poslist.setOnItemClickListener(new AdapterView.OnItemClickListener( ) {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             //   Toast.makeText(getApplicationContext(), "No point of sale found!"+preSelectedIndex, Toast.LENGTH_LONG).show();
                if(preSelectedIndex>-1){
                    listpos.get(preSelectedIndex).setcheck(false);
                    listpos.get(position).setcheck(true);
                    preSelectedIndex=position;
                }else{
                    listpos.get(position).setcheck(true);
                    preSelectedIndex=position;
                }
                posAdapter.notifyDataSetChanged();
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
        txtannuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        txtok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i =preSelectedIndex ;
                if(i==-1){
                    Toast.makeText(getApplicationContext(), "select Point de vente", Toast.LENGTH_LONG).show();
                }else{

                    idPOS = listpos.get(i).getidPointOfSale();
                    namePOS = listpos.get(i).getdesignation();
                    namePos.setText(namePOS);
                    myDialog.dismiss();
                }


            }
        });

    }
    public static void addpanier(int i){
        String price = ItemsProducts.get(i).getprice();
        System.out.println( price);
        String quantity= ItemsProducts.get(i).getquantity();
        basket.add(ItemsProducts.get(i));
        int countcard = basket.size();
        ItemsProducts.remove(i);
        int prixparunit = Integer.parseInt(price);
        int nbQun = Integer.parseInt(quantity);
        int pricett =prixparunit*nbQun;
        System.out.println( pricett);
        pricetotal=pricetotal+pricett;
        StringBuffer prix = new StringBuffer(""+pricetotal);
        if(pricetotal==0){
            txt_price.setText("0.000");
        }else{

            int l = prix.length()-3;
            if(l==0){
                prix= prix.insert(l,'.');
                txt_price.setText("0"+prix);

            }else if(l==-1){
                txt_price.setText("0.0"+prix);
            }else if(l==-2){
                txt_price.setText("0.00"+prix);
            }else{
                prix= prix.insert(l,'.');
                txt_price.setText(prix);
            }

        }

        txt_countcard.setText(""+countcard);
        modecourseAdapter.notifyDataSetChanged();
    }
    public void gobasket(){
        Intent i = new Intent(getApplicationContext(), Basket.class);
        i.putExtra("idList", idList);
        i.putExtra("nameList", nameList);
        i.putExtra("idPOS", idPOS);
        i.putExtra("namePOS", namePOS);
        i.putExtra("Prixtotal",txt_price.getText().toString());
        i.putExtra("price",""+pricetotal);
        startActivity(i);
    }
    public static ArrayList getbasket(){
        return basket;
    }
    public static ArrayList getitemlist(){
        return ItemsProducts;
    }
    public static ArrayList getpanier(){
        return panier;
    }
    public void addCart(){
        String sommetotal = txt_price.getText().toString();


        Cart = new JSONArray();
        try {
            for(int i=0;i<basket.size();i++){
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("idModel",basket.get(i).getidModel());
                jsonObj.put("unit", basket.get(i).getunit());
                jsonObj.put("quantity", basket.get(i).getquantity());
                jsonObj.put("prix", basket.get(i).getprice());
                panier.add(new Panier(basket.get(i).getidModel(),basket.get(i).getquantity(),basket.get(i).getunit(),basket.get(i).getprice()));
                Cart.put(jsonObj);
            }

        }  catch (JSONException e) {
            e.printStackTrace();
        }



        JSONObject jsonBodyObj = new JSONObject();

        try {
            jsonBodyObj.put("idPos", idPOS);
            jsonBodyObj.put("idList", idList);
            jsonBodyObj.put("prixTotal",sommetotal );
            jsonBodyObj.put("listAchat", Cart);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();

        StringRequest Stringrequest = new StringRequest(Request.Method.POST, ActivityLink.list + "/Panier/addPrimaire",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(),jsonObject.getString("msg"), Toast.LENGTH_LONG).show();

                        } catch(JSONException e){

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idPos", idPOS);
                params.put("idList", idList);
                params.put("prixTotal", sommetotal);
                params.put("listAchat", String.valueOf(Cart));
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
        Stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(Stringrequest);

    }
}


