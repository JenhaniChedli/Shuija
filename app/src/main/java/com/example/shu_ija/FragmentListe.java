package com.example.shu_ija;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FragmentListe extends Fragment {

    private SwipeRefreshLayout refreshList ;
    private String codeimei;
    private Dialog myDialog;
    private FloatingActionButton FAB;
    private ArrayList<listItem> Items=new ArrayList<listItem>();
    private listeAdapter mylisteAdapter ;
    private ListView meslistes;
    public static Dialog popupWindow;
    ConstraintLayout fragment_liste;
    View v ;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_liste, container, false);
        FAB = (FloatingActionButton) v.findViewById(R.id.FAB);
        fragment_liste= v.findViewById(R.id.fragment_liste);
        meslistes = (ListView) v.findViewById(R.id.meslistes);
        mylisteAdapter = new listeAdapter(Items , getContext());
        FAB.setClickable(false);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createList_Microservice();

            }
        });
        refreshList = v.findViewById(R.id.refreshList);
        showProgress(getContext());
        myDialog = new Dialog(getContext());
      //  getListByUser();
        refreshlistView();
        meslistes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent = new Intent(getActivity(),ListProduct.class);
            String idList =Items.get(position).getidList();
            String nameList = Items.get(position).getnameList();
            String nbproduct = Items.get(position).getNProduit();
            intent.putExtra("nbproduct",nbproduct);
            intent.putExtra("idList",idList);
            intent.putExtra("nameList",nameList);
            startActivity(intent);


            }
      });

        meslistes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ShowPopuprenomsupp(position);
                return true;
            }
        });


        refreshList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshlistView();
            }
        });
        meslistes.setAdapter(mylisteAdapter);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onBackPressed() throws Throwable {
        finalize();
    }

    public void refreshlistView(){
        refreshList.setRefreshing(true);
        meslistes.setAdapter(null);
        Items.removeAll(Items);
        getListByUser();
        meslistes.setAdapter(mylisteAdapter);
        FAB.setClickable(true);
        refreshList.setRefreshing(false);

    }
    public String getImei(){
        ActivitySession activitySession = new ActivitySession(getContext());
        codeimei = activitySession.getStringImei();
        // Toast.makeText(getContext(), codeimei, Toast.LENGTH_LONG).show();
        return codeimei;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createList_Microservice(){
        FAB.setClickable(false);
        final String nameList =CreateNL() ;
        final String codeImei =getImei() ;
        final JSONObject jsonBodyObj = new JSONObject();
        try {
            jsonBodyObj.put("nameList", nameList);
            jsonBodyObj.put("codeImei", codeImei);
            jsonBodyObj.put("idUser", codeImei);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();

        StringRequest Stringrequest = new StringRequest(Request.Method.POST, ActivityLink.list + "/addList",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String idList = jsonObject.getString("idList");
                            String date =jsonObject.getString("dateAdded");
                            String nProduct = jsonObject.getString("nbProduct");
                            Intent intent = new Intent(getActivity(),ListProduct.class);
                            intent.putExtra("nbproduct",nProduct);
                            intent.putExtra("idList",idList);
                            intent.putExtra("nameList",nameList);
                            startActivity(intent);
                            FAB.setClickable(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            FAB.setClickable(true);

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

                params.put("nameList", nameList);
                params.put("codeImei", codeImei);
                params.put("idUser", codeImei);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(Stringrequest);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String CreateNL(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return "Mes Liste "+dtf.format(now);
    }

    public void getListByUser(){
        final String userId =getImei() ;

        final JSONObject jsonBodyObj = new JSONObject();
        try {
            jsonBodyObj.put("idUser", String.valueOf(userId));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();

        StringRequest Stringrequest = new StringRequest(Request.Method.POST, ActivityLink.list + "/list/getListByUser",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getBoolean("found")){

                                JSONArray jArray = new JSONArray(jsonObject.getString("list"));

                                for (int i = 0; i < jArray.length(); i++) {
                                    JSONObject jsonObj = jArray.getJSONObject(i);
                                    String idList = jsonObj.getString("idList");
                                    String nameList = jsonObj.getString("nameList");
                                    String date = jsonObj.getString("dateAdded");
                                    String nProduct = jsonObj.getString("nbProduct");

                                    Items.add(new listItem(nameList,idList,date,nProduct));

                                }
                                mylisteAdapter.notifyDataSetChanged();
                                hideProgress();
                                Toast.makeText(getContext(), "Liste : " + Items.size(), Toast.LENGTH_LONG).show();
                            }else{
                                hideProgress();
                                Toast.makeText(getContext(), "Il n'y a pas de liste", Toast.LENGTH_LONG).show();
                                fragment_liste.setBackgroundResource(R.drawable.listempty);
                            }

                        }catch(Exception e){
                            hideProgress();
                            Toast.makeText(getContext(), "Errer Cnx", Toast.LENGTH_LONG).show();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgress();
                Toast.makeText(getContext(), "Error Response ", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idUser", String.valueOf(userId));
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(Stringrequest);


    }
    
    
    public static void showProgress(final Context context) {
      try {
          if (!((Activity) context).isFinishing()) {
              View layout = LayoutInflater.from(context).inflate(R.layout.popup_loading, null);
              popupWindow = new Dialog(context);
              popupWindow.requestWindowFeature(Window.FEATURE_NO_TITLE);
              popupWindow.setContentView(layout);
              popupWindow.setCancelable(false);
              if (!((Activity) context).isFinishing()) {
                  popupWindow.show();
              }
          }

      } catch (Exception e)

      {
          e.printStackTrace();
      }
  }
    public static void hideProgress() {
        try {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    public void deleteList(final int id){
        final String idList = Items.get(id).getidList();
        final JSONObject jsonBodyObj = new JSONObject();
        try {
            jsonBodyObj.put("idList", idList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();

        StringRequest Stringrequest = new StringRequest(Request.Method.POST, ActivityLink.list + "/deletelist",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean isdeleted = jsonObject.getBoolean("isDeleted");
                            String msg = jsonBodyObj.getString("msg");
                            if (isdeleted) {
                                Items.remove(id);
                                mylisteAdapter.notifyDataSetChanged();
                                Toast.makeText(getContext(), msg , Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getContext(), msg , Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(Stringrequest);

    }
    public void updateNameList(final int id ,final String nameList){
        final String idList = Items.get(id).getidList();

        final JSONObject jsonBodyObj = new JSONObject();
        try {
            jsonBodyObj.put("idList", idList);
            jsonBodyObj.put("nameList", nameList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();

        StringRequest Stringrequest = new StringRequest(Request.Method.PUT, ActivityLink.list + "/list/updateNameList",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean isUpdated = jsonObject.getBoolean("isUpdated");
                            String msg = jsonBodyObj.getString("msg");
                            if (isUpdated) {
                                Items.get(id).setnameList(nameList);
                                mylisteAdapter.notifyDataSetChanged();
                                Toast.makeText(getContext(), msg , Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getContext(), msg , Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
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
                params.put("idList", idList);
                params.put("nameList",nameList);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(Stringrequest);

    }
    public void ShowPopuprenomsupp(int position) {

        myDialog.setContentView(R.layout.renomsupp);
        TextView btnannuler = myDialog.findViewById(R.id.annuler);
        Button btnsup= myDialog.findViewById(R.id.btnsup);
        Button btnrenom = myDialog.findViewById(R.id.btnrenom);

        btnannuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        final int finalPosition = position;
        btnsup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                ShowPopupsupp(finalPosition);


            }
        });
        btnrenom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                ShowPopuprenom(finalPosition);
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

    }
    public void ShowPopupsupp(final int position) {

        myDialog.setContentView(R.layout.supp);
        TextView btnannuler = myDialog.findViewById(R.id.annuler);
        TextView btnsup= myDialog.findViewById(R.id.supprimer);


        btnannuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        btnsup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteList(position);
                myDialog.dismiss();
                refreshlistView();

            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

    }
    public void ShowPopuprenom(int Position) {

        myDialog.setContentView(R.layout.renom);
        TextView btnannuler = myDialog.findViewById(R.id.annuler);
        TextView btnrenom= myDialog.findViewById(R.id.renommer);
        final EditText namelist = myDialog.findViewById(R.id.nameListe);
        final int position = Position;
        final String lastname = Items.get(position).getnameList();
        namelist.setText(lastname);

        btnannuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        btnrenom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newnamelist = namelist.getText().toString();
                if(newnamelist!=lastname){
                    updateNameList(position,newnamelist);
                }
                myDialog.dismiss();
                refreshlistView();

            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

    }

}