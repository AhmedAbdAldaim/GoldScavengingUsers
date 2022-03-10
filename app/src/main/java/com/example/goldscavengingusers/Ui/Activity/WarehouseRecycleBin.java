package com.example.goldscavengingusers.Ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.goldscavengingusers.Local_DB.LocalSession;
import com.example.goldscavengingusers.Model.ShowWarehouseModel;
import com.example.goldscavengingusers.Model.WarehouseDetailsResponse;
import com.example.goldscavengingusers.Model.WarehouseRecycleBinModel;
import com.example.goldscavengingusers.Model.WarehouseRecycleBinResponse;
import com.example.goldscavengingusers.Network.ApiClient;
import com.example.goldscavengingusers.Network.RequestInterface;
import com.example.goldscavengingusers.R;
import com.example.goldscavengingusers.Ui.Adapter.Show_Main_Warehouse_Adapter;
import com.example.goldscavengingusers.Ui.Adapter.Show_WarehouseDetails_Adapter;
import com.example.goldscavengingusers.Ui.Adapter.Show_WarehouseRecycleBin_Adapter;
import com.example.goldscavengingusers.Utilty.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WarehouseRecycleBin extends AppCompatActivity {
    EditText edsearh;
    RecyclerView recyclerView;
    Show_WarehouseRecycleBin_Adapter show_warehouseRecycleBin_adapter;
    TextView tv_rowcount,tv_empty,tv_connect;
    Button button_connect;


    RequestQueue requestQueue;
    ArrayList<WarehouseRecycleBinModel> arrayList = new ArrayList<WarehouseRecycleBinModel>();

    private static final String TAG_server = "Server";
    private static final String Tag_failure = "failure";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse_recycle_bin);

        //Action Bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_warehouserecyclebin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_connect = findViewById(R.id.connection);
        button_connect = findViewById(R.id.btnconnection);
        edsearh = findViewById(R.id.search);
        recyclerView = findViewById(R.id.rec);
        tv_empty = findViewById(R.id.empty);
        tv_rowcount = findViewById(R.id.rowcount);

        ConnectivityManager connectivityManager = ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE));

        //<-- SEARCH -->
        edsearh.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    show_warehouseRecycleBin_adapter.getFilter().filter(charSequence);
                }catch (Exception e){ }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    show_warehouseRecycleBin_adapter.getFilter().filter(charSequence);
                }catch (Exception e){ }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        //<--- EditText Hidden EditText Cursor When OnClick Done On Keyboard-->
        edsearh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId()==edsearh.getId()){
                    edsearh.setCursorVisible(true);
                }
            }
        });
        edsearh.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent event) {
                edsearh.setCursorVisible(false);
                if(event !=null &&(event.getKeyCode()==KeyEvent.KEYCODE_ENTER)){
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(edsearh.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });



        GridLayoutManager linearLayoutManager = new GridLayoutManager(this,1,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected())
        {
            GetWarehouseRecycleBin();
        }
        else
        {
            tv_connect.setText(R.string.connect_internet);
            tv_connect.setVisibility(View.VISIBLE);
            button_connect.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            tv_empty.setVisibility(View.INVISIBLE);
            button_connect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_connect.setVisibility(View.INVISIBLE);
                    button_connect.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    GetWarehouseRecycleBin();
                }
            });
        }

        }


    //<--  Git All GetWarehouse -->
    public void GetWarehouseRecycleBin(){
        ProgressDialog loading = ProgressDialog.show(this,null,getString(R.string.wait), false, false);
        loading.setContentView(R.layout.progressbar);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);

        String url = "http://goldscavenging.herokuapp.com/api/returned";
        requestQueue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONObject object = jsonObject.getJSONObject("goldBarOwners");
                    if (object.length() != 0) {
                        Iterator<String> key = object.keys();
                        while (key.hasNext()) {
                            String Date = key.next();
                            JSONArray jsonArray = object.getJSONArray(Date);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object1 = jsonArray.getJSONObject(i);
                                        String id_warehouse = object1.getString("id_warehouse");
                                        String gold_bar_id = object1.getString("gold_bar_id");
                                        String date_add_item = object1.getString("date_add_item");
                                        String gold_bar_owner = object1.getString("gold_bar_owner");
                                        String gold_ingot_weight = object1.getString("gold_ingot_weight");
                                        String sample_weight  = object1.getString("sample_weight");
                                        String gold_karat_weight = object1.getString("gold_karat_weight");
                                        String net = object1.getString("net");
                                        String price_gram = object1.getString("price_gram");
                                        String price = object1.getString("price");
                                        String date_add = object1.getString("date_add");

                                        arrayList.add(new WarehouseRecycleBinModel(id_warehouse,gold_bar_id,date_add,gold_bar_owner,gold_ingot_weight,sample_weight,gold_karat_weight,net,date_add_item,price_gram,price));
                                        show_warehouseRecycleBin_adapter = new Show_WarehouseRecycleBin_Adapter(arrayList, WarehouseRecycleBin.this);
                                        recyclerView.setAdapter(show_warehouseRecycleBin_adapter);
                                        tv_rowcount.setVisibility(View.VISIBLE);
                                        tv_rowcount.setText(String.valueOf(show_warehouseRecycleBin_adapter.getItemCount()));
                                        loading.dismiss();
                                   }
                              }
                        }

                } catch (JSONException e) {
                    loading.dismiss();
                    Log.i(TAG_server, e.getMessage());
                    if(e.getMessage().contains("Value [] at goldBarOwners")) {
                        tv_empty.setVisibility(View.VISIBLE);
                        tv_rowcount.setVisibility(View.VISIBLE);
                    }  else if(e.getMessage().contains("No value for goldBarOwners")) {
                        try {
                       //     Utility.showAlertDialog(getString(R.string.error), jsonObject.getString("message_ar") + "\n" + jsonObject.getString("message_en"), WarehouseRecycleBin.this);
                            SharedPreferences sharedPreferences = getSharedPreferences("langdb", Context.MODE_PRIVATE);
                            String lang = sharedPreferences.getString("lang", "ar");
                            if(lang.equals("ar")) {
                                Utility.showAlertDialog(getString(R.string.error),jsonObject.getString("message_ar"), WarehouseRecycleBin.this);
                            }else if(lang.equals("en")){
                                Utility.showAlertDialog(getString(R.string.error),  jsonObject.getString("message_en"), WarehouseRecycleBin.this);
                            }
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }else{
                        Utility.showAlertDialog(getString(R.string.error), getString(R.string.servererror), WarehouseRecycleBin.this);
                    }
                }
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                loading.dismiss();
                Utility.printLog(Tag_failure, volleyError.getMessage());
                tv_connect.setText(R.string.connect_internet_slow);
                tv_connect.setVisibility(View.VISIBLE);
                button_connect.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                tv_empty.setVisibility(View.INVISIBLE);
                button_connect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tv_connect.setVisibility(View.INVISIBLE);
                        button_connect.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        GetWarehouseRecycleBin();
                    }
                });


            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("Authorization","Bearer "+ LocalSession.getToken());
                return hashMap;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
