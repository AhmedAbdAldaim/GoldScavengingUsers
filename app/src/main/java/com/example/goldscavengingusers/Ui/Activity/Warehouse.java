package com.example.goldscavengingusers.Ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.http.Headers;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.goldscavengingusers.Local_DB.LocalSession;
import com.example.goldscavengingusers.Local_DB.Sqllite_goldkarat;
import com.example.goldscavengingusers.Model.ShowWarehouseModel;
import com.example.goldscavengingusers.R;
import com.example.goldscavengingusers.Ui.Adapter.Show_Main_Adapter;
import com.example.goldscavengingusers.Ui.Adapter.Show_Main_Warehouse_Adapter;
import com.example.goldscavengingusers.Utilty.Utility;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class Warehouse extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText edsearch;
    TextView tv_empty,tv_connect,tv_rowcount;
    Button button_connect;

    RequestQueue requestQueue;
    Show_Main_Warehouse_Adapter show_main_warehouse_adapter;
    ArrayList<ShowWarehouseModel> arrayList = new ArrayList<ShowWarehouseModel>();

    private static final String Tag = "reg";
    private static final String TAG_server = "Server";
    private static final String Tag_failure = "failure";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse);

        //Action Bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_warehouse);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_connect = findViewById(R.id.connection);
        button_connect = findViewById(R.id.btnconnection);
        tv_empty = findViewById(R.id.empty);
        recyclerView = findViewById(R.id.rec);
        edsearch = findViewById(R.id.search);
        tv_rowcount = findViewById(R.id.rowcount);


        ConnectivityManager connectivityManager = ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE));
        //<-- SEARCH -->
        edsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    show_main_warehouse_adapter.getFilter().filter(charSequence);
                }catch (Exception e){ }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    show_main_warehouse_adapter.getFilter().filter(charSequence);

                }catch (Exception e){ }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //<--- EditText Hidden EditText Cursor When OnClick Done On Keyboard-->
        edsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId()==edsearch.getId()){
                    edsearch.setCursorVisible(true);
                }

            }
        });

        GridLayoutManager linearLayoutManager = new GridLayoutManager(this,1,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);


        if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected())
        {
            GetWarehouse();
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
                    GetWarehouse();
                }
            });
        }

    }

    //<--  Git All GetWarehouse -->
    public void GetWarehouse(){
        ProgressDialog loading = ProgressDialog.show(this,null,getString(R.string.wait), false, false);
        loading.setContentView(R.layout.progressbar);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);

        String url = "http://goldscavenging.herokuapp.com/api/warehouse";
        requestQueue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                    try {
                        JSONObject object = jsonObject.getJSONObject("goldBarOwners");
                        if (object.length() != 0) {
                            Iterator<String> key = object.keys();
                            while (key.hasNext()) {
                                String Date = key.next();

                                JSONArray jsonArray = object.getJSONArray(Date);
                                arrayList.add(new ShowWarehouseModel(Date));
                                show_main_warehouse_adapter = new Show_Main_Warehouse_Adapter(arrayList, Warehouse.this);
                                recyclerView.setAdapter(show_main_warehouse_adapter);
                                tv_rowcount.setVisibility(View.VISIBLE);
                                tv_rowcount.setText(String.valueOf(show_main_warehouse_adapter.getItemCount()));
                                loading.dismiss();
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
                                SharedPreferences sharedPreferences = getSharedPreferences("langdb", Context.MODE_PRIVATE);
                                String lang = sharedPreferences.getString("lang", "ar");
                                if(lang.equals("ar")) {
                                    Utility.showAlertDialog(getString(R.string.error),jsonObject.getString("message_ar"), Warehouse.this);
                                }else if(lang.equals("en")){
                                    Utility.showAlertDialog(getString(R.string.error),  jsonObject.getString("message_en"), Warehouse.this);
                                }
                                //    Utility.showAlertDialog(getString(R.string.error), jsonObject.getString("message_ar") + "\n" + jsonObject.getString("message_en"), Warehouse.this);
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                        }else{
                            Utility.showAlertDialog(getString(R.string.error), getString(R.string.servererror), Warehouse.this);
                        }
                    }
                }

        }, new Response.ErrorListener() {
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
                        GetWarehouse();
                    }
                });


            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String>hashMap = new HashMap<>();
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
