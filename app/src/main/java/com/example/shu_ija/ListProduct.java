package com.example.shu_ija;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
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

public class ListProduct extends AppCompatActivity  {
    ImageView Back;
    TextView txtnamelist;
    static TextView txt_nbproduct;
    SearchView searchView;
    String nameList;
    String idList;
    String nbproduct;
    Dialog myDialog;
    String nameIndexed;
    LinearLayout  modecourse ;
    ArrayList<ItemSearchProduct>ItemsProduct=new ArrayList<ItemSearchProduct>();
    static ArrayList<MSListProduct> MsProduct = new ArrayList<MSListProduct>();
    static ArrayList<ProductList> ItemsProducts = new ArrayList<ProductList>();
    ArrayAdapter arrayAdapter;
    static ListProductAdapter listProductAdapter;
    ListView productview;
    ListView listproductview;
    static ArrayList<String> AllModel = new ArrayList<>();
    static ArrayList<String> AllidModel = new ArrayList<>();
    static ArrayList<String> AllMark = new ArrayList<>();
    static ArrayList<String> AllidMark = new ArrayList<>();
    static ArrayList<String> AllUnit = new ArrayList<>();
    static ArrayList<String> Marks = new ArrayList<>();
    static ArrayList<String> idMarks = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);
        Back =  findViewById(R.id.back);
        txtnamelist =  findViewById(R.id.txtnamelist);
        searchView= findViewById(R.id.searchproductview);
        productview= findViewById(R.id.productview);
        listproductview=findViewById(R.id.listproductview);
        txt_nbproduct =findViewById(R.id.txt_nbproduct);
        modecourse =findViewById(R.id.modecourse);
         myDialog = new Dialog(this);
        listProductAdapter = new ListProductAdapter(ItemsProducts, getApplicationContext());
        listproductview.setAdapter(listProductAdapter);
        MsProduct.clear();
        AllModel.clear();
        AllidModel.clear();
        AllidMark.clear();
        AllMark.clear();
        AllUnit.clear();

        Getintent();
        getListMicroService(idList);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        modecourse.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                savelistProductMS( idList, MsProduct );
                Intent intent = new Intent(getApplicationContext(),ModeCourse.class);
                intent.putExtra("idList",idList);
                intent.putExtra("nameList",nameList);
                intent.putExtra("test",true);
                startActivity(intent);
            }
        });
        productview.setOnItemClickListener(new AdapterView.OnItemClickListener( ) {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              ShowPopupModel(position);
          }
      });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                    ItemsProduct.removeAll(ItemsProduct);
                    sendIndex(newText);

                return false;
            }


        });

    }
    @Override
    public void onBackPressed() {
        savelistProductMS( idList, MsProduct );
        Intent i = new Intent(getApplicationContext(), MenuActivity.class);
        i.putExtra("test","false");
        startActivity(i);
    }
    public void Getintent() {
        Intent intent = getIntent();
        nbproduct = intent.getStringExtra("nbproduct");
        idList = intent.getStringExtra("idList");
        nameList = intent.getStringExtra("nameList");
        txtnamelist.setText(nameList);
        txt_nbproduct.setText(nbproduct);
    }
    public void sendIndex(final String keyword) {
        final ArrayList<String> namePorducts = new ArrayList<String>();
        JSONObject jsonBodyObj = new JSONObject();
        try {
            jsonBodyObj.put("nameProduct", keyword);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();

        StringRequest Stringrequest = new StringRequest(Request.Method.POST, ActivityLink.Product + "/searchingProduct",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            ItemsProduct.clear();
                            nameIndexed = jsonObject.getString("Product");
                            if (!nameIndexed.equals("[]")) {
                                JSONArray jsonArr = new JSONArray(nameIndexed);

                                for (int i = 0; i < jsonArr.length(); i++) {

                                    JSONObject jsonObj = jsonArr.getJSONObject(i);
                                    String idProduct = jsonObj.getString("idProduct");
                                    String nameProduct = jsonObj.getString("nameProduct");
                                    String nameCategory = jsonObj.getString("nameCategory");
                                    String nameP = " " + nameProduct + "\n " + nameCategory;
                                    namePorducts.add(nameP);
                                    ItemsProduct.add(new ItemSearchProduct(idProduct,nameProduct,nameCategory));

                                }
                                 arrayAdapter= new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,namePorducts);
                                productview.setAdapter(arrayAdapter);
                            } else {
                                 arrayAdapter= new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,namePorducts);
                                productview.setAdapter(arrayAdapter);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Error Cnx", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error Response", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nameProduct", keyword);
                return super.getParams();
            }

            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("x-api-Key", "hjm3SE9rhH6I8VB9jx3Roz6uP9r6tghn");
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
    public void getListMicroService(final String idList) {
        final ArrayList<String> namePorducts = new ArrayList<String>();
        MsProduct.clear();
        ItemsProducts.clear();
        JSONObject jsonBodyObj = new JSONObject();
        try {
            jsonBodyObj.put("idList", idList);
        } catch (JSONException e) { }
        final String requestBody = jsonBodyObj.toString();

        StringRequest Stringrequest = new StringRequest(Request.Method.POST, ActivityLink.list + "/listProduct/getListProductbyid",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj ;
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("msg").equals("No list found!")) {
                                Toast.makeText(getApplicationContext(), "No list found!", Toast.LENGTH_LONG).show();
                            } else if(jsonObject.getString("msg").equals("404")) {
                                Toast.makeText(getApplicationContext(), "check ip on list", Toast.LENGTH_LONG).show();
                            }else if (jsonObject.getString("arraymodel").equals("[]")){

                                Toast.makeText(getApplicationContext(), "List Product Vide ", Toast.LENGTH_LONG).show();

                            }else{
                                JSONArray jsonArray = new JSONArray(jsonObject.getString("arraymodel"));

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    jsonObj = jsonArray.getJSONObject(i);

                                    String  nameProduct =  jsonObj.getString("nameProduct");
                                    String  nameCategory=       jsonObj.getString("nameCategory");
                                    String nameMark=        jsonObj.getString("nameMark");
                                    String nameModel=        jsonObj.getString("nameModel")+" "+nameMark;
                                    String unit=       jsonObj.getString("unit");
                                    String quantity=       jsonObj.getString("quantity");
                                    String idModel=      jsonObj.getString("idModel");
                                    String idProduct =jsonObj.getString("idProduct");
                                    String idMark =jsonObj.getString("idMark");
                                    ItemsProducts.add(new ProductList(nameProduct,nameCategory,nameMark,nameModel,unit,quantity,idModel,idProduct,idMark));
                                    MsProduct.add(new MSListProduct(idModel,quantity,unit));
                                }
                                txt_nbproduct.setText(""+ItemsProducts.size());
                                listProductAdapter.notifyDataSetChanged();
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
    public void getModel(final String idProduct ){
        AllModel.clear();
        AllModel.add("Select a Model");
        AllidModel.clear();
        AllidModel.add("0");
        AllMark.clear();
        AllMark.add("Select a Mark");
        AllidMark.clear();
        AllidMark.add("0");
        Marks.clear();
        Marks.add("Select a Mark");
        idMarks.clear();
        idMarks.add("0");
        JSONObject jsonBodyObj = new JSONObject();
        try {
            jsonBodyObj.put("idProduct", idProduct);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();

        StringRequest Stringrequest = new StringRequest(Request.Method.POST, ActivityLink.Product + "/model/byProduct",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArr = new JSONArray(jsonObject.getString("Model"));
                            for (int i = 0; i < jsonArr.length(); i++) {
                                JSONObject jsonObj = jsonArr.getJSONObject(i);
                                String idModal =jsonObj.getString("idModel");
                                String idMark = jsonObj.getString("idMark") ;
                                String mark = jsonObj.getString("nameMark") ;
                                String model = jsonObj.getString("nameModel") + " - "  + mark;
                                AllModel.add(model);
                                AllidModel.add(idModal);
                                AllMark.add(mark);
                                AllidMark.add(idMark);
                                if(!Marks.contains(mark)){
                                   Marks.add(mark);
                                   idMarks.add(idMark);
                                }

                            }
                        } catch(JSONException e){ }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idProduct", idProduct);
                return super.getParams();
            }

            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("x-api-Key", "hjm3SE9rhH6I8VB9jx3Roz6uP9r6tghn");
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
    public void getUnitModal(final String idModelSelect,Spinner spinunit){
        AllUnit.clear();
        AllUnit.add("Select a Unit");
        JSONObject jsonBodyObj = new JSONObject();
        try {
            jsonBodyObj.put("idModel", idModelSelect);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();

        StringRequest Stringrequest = new StringRequest(Request.Method.POST, ActivityLink.Product + "/model/unitbyIdModel",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray jsonArr = new JSONArray(jsonObject.getString("Unit"));


                            for (int i = 0; i < jsonArr.length(); i++) {
                                AllUnit.add(jsonArr.getString(i));
                            }

                            if(AllUnit.size()>=2){
                                spinunit.setSelection(1);
                            }
                           // System.out.println(AllUnit);
                        } catch(JSONException e){ }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idModel", idModelSelect);
                return super.getParams();
            }
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("x-api-Key", "hjm3SE9rhH6I8VB9jx3Roz6uP9r6tghn");
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
    public void getModalMark(final String idMarkSelect,final String MarkSelect,final String idProduct){
        AllModel.clear();
        AllidModel.clear();
        AllidModel.add("0");
        AllModel.add("Select a Model");
        JSONObject jsonBodyObj = new JSONObject();
        try {
            jsonBodyObj.put("idMark", idMarkSelect);
            jsonBodyObj.put("idProduct", idProduct);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();

        StringRequest Stringrequest = new StringRequest(Request.Method.POST, ActivityLink.Product + "/model/byMarkProduct",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArr = new JSONArray(jsonObject.getString("Model"));
                            for (int i = 0; i < jsonArr.length(); i++) {
                                JSONObject jsonObj = jsonArr.getJSONObject(i);
                                AllidModel.add(jsonObj.getString("idModel"));
                                String model = jsonObj.getString("nameModel")+" - "+ MarkSelect;
                                AllModel.add(model);
                            }

                        } catch(JSONException e){
                            System.out.println( "error getModalMark ***" );
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
                params.put("idMark", idMarkSelect);
                params.put("idProduct", idProduct);
                return super.getParams();
            }
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("x-api-Key", "hjm3SE9rhH6I8VB9jx3Roz6uP9r6tghn");
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
    public void ShowPopupModel(final int position) {
        myDialog.setContentView(R.layout.model);
        myDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView txtProduct = myDialog.findViewById(R.id.nameProduct);
        LinearLayout img_plus = myDialog.findViewById(R.id.img_plus);
        LinearLayout img_mins= myDialog.findViewById(R.id.img_mins);
        TextView txtCategory = myDialog.findViewById(R.id.nameCategory);
        TextView txtenregistrer = myDialog.findViewById(R.id.txtenregistrer);
        TextView txtannuler = myDialog.findViewById(R.id.txtannuler);


        final Spinner spinmodel = myDialog.findViewById(R.id.spinmodel);
        final Spinner spinmark = myDialog.findViewById(R.id.spinmark);
        final Spinner spinunit = myDialog.findViewById(R.id.spinunit);
        final TextView editTextNumberDecimal = myDialog.findViewById(R.id.editTextNumberDecimal);
        final String idProduct = ItemsProduct.get(position).getidProduct();
        final String nameProduct =ItemsProduct.get(position).getnameProduct();
        final String nameCategory =ItemsProduct.get(position).getnameCategory();
        txtProduct.setText(nameProduct);
        txtCategory.setText(nameCategory);

        AllModel.clear();
        AllMark.clear();
        Marks.clear();
        AllUnit.clear();
        AllidMark.clear();
        AllidModel.clear();
        AllUnit.add("Select a Unit");
        AllidMark.add("0");
        AllidModel.add("0");
        AllModel.add("Select a Model");
        getModel(idProduct);
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, AllModel);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinmodel.setAdapter(arrayAdapter1);
        spinmodel.setSelection(0);
        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, AllUnit);
        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinunit.setAdapter(arrayAdapter3);
        spinunit.setSelection(0);

        spinmodel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener( ) {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position > 0) {
                     getUnitModal(AllidModel.get(position),spinunit);
                     if(spinmark.getSelectedItemPosition() == 0){
                        int p = Marks.indexOf(AllMark.get(position));
                        spinmark.setSelection(p,false);
                     }
                    }else if(position == 0){
                      //  Toast.makeText(getApplicationContext(), "Select a Model", Toast.LENGTH_LONG).show();
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Marks);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinmark.setAdapter(arrayAdapter2);
        spinmark.setSelection(0);
        spinmark.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener( ) {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if(position != 0) {
                        if(spinmodel.getSelectedItemPosition() != 0){
                            getModalMark(idMarks.get(position),Marks.get(position),idProduct);
                        }

                    }else if(position == 0){
                            spinmodel.setSelection(0,false);
                            spinunit.setSelection(0,false);
                            getModel(idProduct);
                    }
               }



            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        img_plus.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                String Quantity= editTextNumberDecimal.getText().toString();
                int q = Integer.parseInt(Quantity) + 1;
                if(q>0){
                    editTextNumberDecimal.setText(""+q);
                }
            }
        });
        img_mins.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                String Quantity= editTextNumberDecimal.getText().toString();
                int q = Integer.parseInt(Quantity) - 1;
                if(q>0){
                    editTextNumberDecimal.setText(""+q);
                }


            }
        });
        txtenregistrer.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {

                String Quantity = "";
                String nameMark ="";
                String nameModel ="";
                String unit = "";
                Quantity= editTextNumberDecimal.getText().toString();
                if(Integer.parseInt(Quantity)<1){
                    Quantity="1";
                }
                if (spinmark.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), "Select a Mark", Toast.LENGTH_LONG).show();
                } else if (spinmodel.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), "Select a Model", Toast.LENGTH_LONG).show();
                } else if (spinunit.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), "Select a Unit", Toast.LENGTH_LONG).show();
                }else if (Quantity.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Edit Quantity ", Toast.LENGTH_LONG).show();
                }else if (Quantity.equals("0")){
                    Toast.makeText(getApplicationContext(), "change Quantity ", Toast.LENGTH_LONG).show();
                }else{
                    int l = ItemsProducts.size();
                   // System.out.println( "test =  "+ItemsProducts.size() );
                    try
                    {
                        nameMark= spinmark.getSelectedItem().toString();
                        unit = spinunit.getSelectedItem().toString();
                        nameModel =  spinmodel.getSelectedItem().toString();
                        int p = AllModel.indexOf(nameModel);
                        int m = Marks.indexOf(nameMark);
                        String idModel=  AllidModel.get(p);
                        String idMark = idMarks.get(m);


                         setitems( nameProduct, nameCategory , idProduct , nameModel, nameMark, Quantity, unit,idModel, idMark,l);
                         myDialog.dismiss();


                    }
                    catch(Exception e)
                    {
                        Toast.makeText(getApplicationContext(),""+e, Toast.LENGTH_LONG).show();

                    }

                }
            }
        });
        txtannuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });


        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

    }
    public void setitems(String nameProduct,String nameCategory ,String idProduct ,String nameModel,String nameMark,String Quantity,String unit,String idModel,String idMark ,int l){

        ProductList test  = new ProductList(nameProduct,nameCategory,nameMark,nameModel,unit,Quantity,idModel,idProduct,idMark);
        int t=-2;
        int j=-1;
        if(ItemsProducts.size()==0){
            t=-1;
        }else{
            while(t==-2){
            j++;
            String mod = ItemsProducts.get(j).getidModel();
            String u = ItemsProducts.get(j).getunit();
                if ((mod.equals(idModel)) && (u.equals(unit))) {
                    t=j;
                 }
                if(j==(l-1)){
                    if(t!=j){
                        t=-1;
                    }

                }
            }
        }
        if(t==-1){
            ItemsProducts.add(test);
            MsProduct.add(new MSListProduct(idModel,Quantity,unit));
            listProductAdapter.notifyDataSetChanged();
            int np = ItemsProducts.size() ;
            txt_nbproduct.setText(String.valueOf(np));
            Toast.makeText(getApplicationContext(), "add ", Toast.LENGTH_LONG).show();
        }else{
            ItemsProducts.get(t).setquantity(Quantity);
            MsProduct.get(t).setquantity(Quantity);
            listProductAdapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "update ", Toast.LENGTH_LONG).show( );
        }
    }
    public static void deleteItemProduct(int i){

        ItemsProducts.remove(i);
        MsProduct.remove(i);
        int np = ItemsProducts.size() ;
        txt_nbproduct.setText(String.valueOf(np));
        listProductAdapter.notifyDataSetChanged();


    }
    public static String plusItemProduct(int i){

        int q = Integer.parseInt(ItemsProducts.get(i).getquantity()) + 1;
        if(q>0){
            ItemsProducts.get(i).setquantity(""+q );
            MsProduct.get(i).setquantity(""+q );
        }
        String count ="" + Integer.parseInt(ItemsProducts.get(i).getquantity());
        return count;

    }
    public static String minsItemProduct(int i){

        int q = Integer.parseInt(ItemsProducts.get(i).getquantity()) - 1;
        if(q>0){
            ItemsProducts.get(i).setquantity(""+q );
            MsProduct.get(i).setquantity(""+q );
        }
        String count ="" + Integer.parseInt(ItemsProducts.get(i).getquantity());
        return count;

    }
    public void savelistProductMS( String idList,ArrayList<MSListProduct> MsProduct ){

            final JSONArray jsonList = new JSONArray();
        for (int i = 0; i < MsProduct.size(); i++) {
            JSONObject jsonListProduct = new JSONObject();
            try {
                jsonListProduct.put("idModel", MsProduct.get(i).getidModel());
                jsonListProduct.put("quantity",MsProduct.get(i).getquantity());
                jsonListProduct.put("unit", MsProduct.get(i).getunit());

                // insert ( idModel , quantity , unit ) to json
                jsonList.put(jsonListProduct);
            }catch (JSONException e) {
                e.printStackTrace( );
            }

        }
            JSONObject jsonBodyObj = new JSONObject();
            try {
                jsonBodyObj.put("idList", idList);
                jsonBodyObj.put("listProduct", jsonList); // jsonArray

            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String requestBody = jsonBodyObj.toString();
            StringRequest Stringrequest = new StringRequest(Request.Method.PUT, ActivityLink.list + "/updatelistProduct",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean test =jsonObject.getBoolean("isUpdated");
                                if (test) {
                                    Toast.makeText(getApplicationContext(),jsonObject.getString("msg") , Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace( );
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "ErrorResponse", Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("idList", idList);
                    params.put("listProduct", String.valueOf(jsonList));
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