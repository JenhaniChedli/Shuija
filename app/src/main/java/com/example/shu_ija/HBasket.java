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
        import com.android.volley.DefaultRetryPolicy;
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

public class HBasket extends AppCompatActivity {

    ImageView back;
    TextView txtnamelist;
    static TextView txt_countcard;
    static TextView txt_price;
    LinearLayout btn_buy ;
    ListView productview;
    ListView  listbasket;
    SearchView searchproductview ;
    String idList,date,idhList;
    String nameList;
    String nameIndexed;
    String Prixtotal;
    String namePOS;
    String idPOS;
    Dialog myDialog;
    ArrayAdapter arrayAdapter;
    static HBasketAdapter basketAdapter;
    static int pricetotal;
    static ArrayList<ItemModeCourse> ItemsProducts = new ArrayList<ItemModeCourse>();
    static ArrayList<ItemModeCourse> basket = new ArrayList<ItemModeCourse>();
    static ArrayList<Panier> panier = new ArrayList<Panier>();
    ArrayList<ItemSearchProduct>ItemsProduct=new ArrayList<ItemSearchProduct>();
    static ArrayList<String> AllModel = new ArrayList<>();
    static ArrayList<String> AllidModel = new ArrayList<>();
    static ArrayList<String> AllMark = new ArrayList<>();
    static ArrayList<String> AllidMark = new ArrayList<>();
    static ArrayList<String> AllUnit = new ArrayList<>();
    static ArrayList<String> Marks = new ArrayList<>();
    static ArrayList<String> idMarks = new ArrayList<>();
    static ArrayList<String> PrixModel = new ArrayList<>();
    private JSONArray Cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hbasket);
        back=findViewById(R.id.back);
        txtnamelist = findViewById(R.id.txtnamelist);
        txt_price=findViewById(R.id.txt_price);
        txt_countcard=findViewById(R.id.txt_countcard);
        listbasket=findViewById(R.id.listbasket);
        searchproductview=findViewById(R.id.searchproductview );
        productview=findViewById(R.id.productview);
        btn_buy=findViewById(R.id.btn_buy);
        PrixModel.clear();
        ItemsProducts.addAll(Hmode_course.getitemlist());
        basket.addAll(Hmode_course.getbasket());
        panier.addAll(Hmode_course.getpanier());
        txt_countcard.setText(""+basket.size());
        basketAdapter = new HBasketAdapter(basket, getApplicationContext());
        listbasket.setAdapter(basketAdapter);
        Getintent();
        myDialog = new Dialog(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        searchproductview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

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
        productview.setOnItemClickListener(new AdapterView.OnItemClickListener( ) {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShowPopupModel(position);
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
        Intent i = new Intent(getApplicationContext(), Hmode_course.class);
        i.putExtra("idList", idList);
        i.putExtra("idhList", idhList);
        i.putExtra("date", date);
        i.putExtra("nameList", nameList);
        i.putExtra("idPOS", idPOS);
        i.putExtra("namePOS", namePOS);
        i.putExtra("Prixtotal",txt_price.getText().toString());
        i.putExtra("price",""+pricetotal);
        i.putExtra("test",false);
        startActivity(i);

    }
    public void Getintent() {
        Intent intent = getIntent();
        idList = intent.getStringExtra("idList");
        idhList = intent.getStringExtra("idhList");
        date = intent.getStringExtra("date");
        nameList = intent.getStringExtra("nameList");
        Prixtotal = intent.getStringExtra("Prixtotal");
        String price= intent.getStringExtra("price");
        pricetotal= Integer.parseInt(price);
        namePOS = intent.getStringExtra("namePOS");
        idPOS = intent.getStringExtra("idPOS");
        txtnamelist.setText(nameList);
        txt_price.setText(Prixtotal);
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
        //System.out.println( AllModel);
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
                        System.out.println( "1.2");
                    }
                }else if(position == 0){
                    System.out.println( "1.3");
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
                    try
                    {
                        nameMark= spinmark.getSelectedItem().toString();
                        unit = spinunit.getSelectedItem().toString();
                        nameModel =  spinmodel.getSelectedItem().toString();

                        setitems( nameProduct, nameCategory , idProduct , nameModel, nameMark, Quantity, unit);
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
    public void setitems(String nameProduct,String nameCategory ,String idProduct ,String nameModel,String nameMark,String Quantity,String unit ){

        int p = AllModel.indexOf(nameModel);
        String idModel=  AllidModel.get(p);
        int m = Marks.indexOf(nameMark);
        String idMark = idMarks.get(m);
        if(p==-1){
            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
        }else{
            getprixmodel(idPOS,idModel,unit,idProduct,idMark,nameProduct,nameCategory,nameMark,nameModel,Quantity);
            basketAdapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "add ", Toast.LENGTH_LONG).show();
        }



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
    public void getprixmodel(final String idPOS, String idModel, String unit, String idProduct, String idMark, String nameProduct, String nameCategory, String nameMark, String nameModel, String Quantity){
        PrixModel.clear();
        JSONObject jsonBodyObj = new JSONObject();
        try {
            jsonBodyObj.put("idPointOfSale","0");
            jsonBodyObj.put("idModel",idModel);
            jsonBodyObj.put("unit",unit);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();

        StringRequest Stringrequest = new StringRequest(Request.Method.POST, ActivityLink.pointOfSale + "/ProductsPerPos/prixProductById",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObj ;
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(!jsonObject.getBoolean("isFound")){
                                Toast.makeText(getApplicationContext(), "prix not found", Toast.LENGTH_LONG).show();
                            }else {
                                String price= jsonObject.getString("prix");
                                String mots[] = nameModel.split("-");
                                basket.add(new ItemModeCourse(nameProduct,nameCategory,nameMark,mots[0],unit,Quantity,idModel,idProduct,idMark,price)  );
                                txt_countcard.setText(""+basket.size());
                                int prixparunit = Integer.parseInt(price);
                                int Quant = Integer.parseInt(Quantity);
                                int pricett =prixparunit*Quant;
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
                params.put("idPointOfSale",idPOS);
                params.put("idModel",idModel);
                params.put("unit",unit);
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
    public static ArrayList getBasket(){
        return basket;
    }
    public static ArrayList getItemlist(){
        return ItemsProducts;
    }
    public static ArrayList getPanier(){
        return panier;
    }
    public static void clearbasket(){
        basket.clear();
        ItemsProducts.clear();
        panier.clear();
    }
    public static void addproduit(int i){
        String price = basket.get(i).getprice();
        int prixparunit = Integer.parseInt(price);
        int pricett =prixparunit;
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

    }
    public static void minproduit(int i){
        String price = basket.get(i).getprice();
        int prixparunit = Integer.parseInt(price);
        int pricett =prixparunit;
        pricetotal=pricetotal-pricett;
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

    }
    public static void addmodecourse(int i){
        ItemsProducts.add(basket.get(i));
        String price = basket.get(i).getprice();
        String quantity= basket.get(i).getquantity();
        basket.remove(i);
        int countcard = basket.size();
        int prixparunit = Integer.parseInt(price);
        int nbQun = Integer.parseInt(quantity);
        int pricett =prixparunit*nbQun;
        pricetotal=pricetotal-pricett;
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
        basketAdapter.notifyDataSetChanged();
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
            jsonBodyObj.put("idHistoriqueList", idhList);
            jsonBodyObj.put("prixTotal",sommetotal );
            jsonBodyObj.put("listAchat", Cart);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();

        StringRequest Stringrequest = new StringRequest(Request.Method.POST, ActivityLink.list + "/Panier/add",
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
                params.put("idHistoriqueList", idhList);
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