package com.example.shu_ija;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
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


public class FragmentHliste extends Fragment {


    private String codeimei;
    private ArrayList<hlistItem> Items =new ArrayList<hlistItem>();
    private hlisteAdapter myhlisteAdapter ;
    public static Dialog popupWindow;
    private Dialog myDialog;
    private ListView meshlistes;
    private SwipeRefreshLayout refreshhList ;

    public String getImei(){
        ActivitySession activitySession = new ActivitySession(getContext());
        codeimei = activitySession.getStringImei();
        //Toast.makeText(getContext(), codeimei, Toast.LENGTH_LONG).show();
        return codeimei;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_hliste, container, false);
        //Toast.makeText(getContext(), getImei(), Toast.LENGTH_LONG).show();
        refreshhList = v.findViewById(R.id.refreshList);
        showProgress(getContext());
        meshlistes = (ListView) v.findViewById(R.id.meshlistes);
        myhlisteAdapter = new hlisteAdapter(Items , getContext());
        myDialog = new Dialog(getContext());
        getHistorique();
        meshlistes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),ListPaniers.class);
                String idhList =Items.get(position).getidhList();
                String nameList = Items.get(position).getnameList();
                String date = Items.get(position).getdate();
                intent.putExtra("idhList",idhList);
                intent.putExtra("nameList",nameList);
                intent.putExtra("date",date);
                startActivity(intent);

            }
        });
        meshlistes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                ShowPopupsupp(position);
                return true;
            }
        });
        refreshhList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshhlistView();
            }
        });
        meshlistes.setAdapter(myhlisteAdapter);




        return v;


    }
    public void getHistorique(){
        final String userId =getImei() ;
        JSONObject jsonBodyObj = new JSONObject();
        try {
            jsonBodyObj.put("idUser", userId);
            jsonBodyObj.put("codeImei", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();

        StringRequest Stringrequest = new StringRequest(Request.Method.POST, ActivityLink.list + "/list/getHistory",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray allHistorique = null;
                        try {
                            allHistorique = new JSONArray(response);
                            int counthis = allHistorique.length();

                            if (counthis > 0){
                                for (int i = 0 ; i <counthis ; i++) {
                                    JSONObject jsonObj = allHistorique.getJSONObject(i);
                                    String idList = jsonObj .getString("idHistoriqueList");
                                    String date =jsonObj .getString("date");
                                    String nameList = jsonObj .getString("nameList");
                                    Items.add(new hlistItem(nameList,idList,date));
                                    //   Toast.makeText(getContext(), idList , Toast.LENGTH_LONG).show();
                                    myhlisteAdapter.notifyDataSetChanged ();
                                    hideProgress();

                                }
                            }else{
                                hideProgress();
                                Toast.makeText(getContext(), "No Historique Liste", Toast.LENGTH_LONG).show();
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
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
                params.put("idUser", userId);
                params.put("codeImei", userId);
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
    public void ShowPopupsupp(final int position) {

        myDialog.setContentView(R.layout.supphlist);
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
                // Toast.makeText(getContext(), "idList 2" , Toast.LENGTH_LONG).show();
                deletehList(position);
                refreshhlistView();
                myDialog.dismiss();


            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

    }
    public void deletehList(final int id){
        final String idhList = Items.get(id).getidhList();
        final JSONObject jsonBodyObj = new JSONObject();
        try {
            jsonBodyObj.put("idHistoriqueList", idhList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();

        StringRequest Stringrequest = new StringRequest(Request.Method.POST, ActivityLink.list + "/historiqueList/delete",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean isdeleted = jsonObject.getBoolean("isDeleted");
                            String msg = jsonBodyObj.getString("msg");
                            if (isdeleted) {
                                Items.remove(id);
                                myhlisteAdapter.notifyDataSetChanged();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(Stringrequest);

    }
    public void refreshhlistView(){
        refreshhList.setRefreshing(true);
        meshlistes.setAdapter(null);
        Items.removeAll(Items);
        getHistorique();
        meshlistes.setAdapter(myhlisteAdapter);
        refreshhList.setRefreshing(false);
    }
}