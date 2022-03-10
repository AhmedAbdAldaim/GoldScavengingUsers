package com.example.goldscavengingusers.Ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goldscavengingusers.Local_DB.LocalSession;
import com.example.goldscavengingusers.Local_DB.Sqllite_goldkarat;
import com.example.goldscavengingusers.Model.ShowGoldbarsResponse;
import com.example.goldscavengingusers.Model.WarehouseDetailsResponse;
import com.example.goldscavengingusers.Network.ApiClient;
import com.example.goldscavengingusers.Network.RequestInterface;
import com.example.goldscavengingusers.R;
import com.example.goldscavengingusers.Ui.Adapter.Show_Main_Adapter;
import com.example.goldscavengingusers.Ui.Adapter.Show_WarehouseDetails_Adapter;
import com.example.goldscavengingusers.Utilty.Utility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class Warehouse_Details extends AppCompatActivity {

    EditText edsearh;
    RecyclerView recyclerView;
    Show_WarehouseDetails_Adapter show_warehouseDetails_adapter;
    TextView nets,gold,expexted,price,price_s_p,tv_rowcount,tv_empty,tv_connect,TV_DATE_OFADDTOWAREHOUSE;
    Button button_connect;

    private static final String TAG_server = "Server";
    private static final String Tag_failure = "failure";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse__details);

        //Action Bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_warehouse_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nets = findViewById(R.id.netTotal);
        gold = findViewById(R.id.goldTotal);
        expexted = findViewById(R.id.katrtTotal);
        price = findViewById(R.id.priceTotal);
        price_s_p = findViewById(R.id.price_sud_pound);
        tv_connect = findViewById(R.id.connection);
        button_connect = findViewById(R.id.btnconnection);
        edsearh = findViewById(R.id.search);
        recyclerView = findViewById(R.id.rec);
        tv_empty = findViewById(R.id.empty);
        tv_rowcount = findViewById(R.id.rowcount);
        TV_DATE_OFADDTOWAREHOUSE = findViewById(R.id.addeddate);

        ConnectivityManager connectivityManager = ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE));

        //<-- SEARCH -->
        edsearh.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    show_warehouseDetails_adapter.getFilter().filter(charSequence);
                }catch (Exception e){ }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    show_warehouseDetails_adapter.getFilter().filter(charSequence);
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

        Intent intent = this.getIntent();
        String AddedDate = intent.getStringExtra("Date");


//        TV_DATE_OFADDTOWAREHOUSE.setText(AddedDate);

        GridLayoutManager linearLayoutManager = new GridLayoutManager(this,1,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected())
        {

                GetWarehouseDetails(AddedDate);

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
                    GetWarehouseDetails(AddedDate);
                }
            });
        }
    }

    //<--  Git All Warehouse Detaisl -->
    public void GetWarehouseDetails(String added_Date) {
        ProgressDialog loading = ProgressDialog.show(this, null, getString(R.string.wait), false, false);
        loading.setContentView(R.layout.progressbar);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);


        // <-- Connect WIth Network And Check Response Successful or Failure -- >
        final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
        Call<WarehouseDetailsResponse> call = requestInterface.GetWarehouseDetails(added_Date,"Bearer " + LocalSession.getToken());
        call.enqueue(new Callback<WarehouseDetailsResponse>() {
            @Override
            public void onResponse(Call<WarehouseDetailsResponse> call, Response<WarehouseDetailsResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        show_warehouseDetails_adapter = new Show_WarehouseDetails_Adapter(response.body().getWarehouseDetailsModel(), Warehouse_Details.this);
                        if (show_warehouseDetails_adapter.getItemCount() == 0) {
                                loading.dismiss();
                                finish();

                        } else if (show_warehouseDetails_adapter.getItemCount() > 0) {
                            loading.dismiss();
                            tv_empty.setVisibility(View.INVISIBLE);
                            show_warehouseDetails_adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(show_warehouseDetails_adapter);
                            nets.setText(response.body().getNets());
                            gold.setText(response.body().getGold_block());
                            expexted.setText(response.body().getExpected());
                            price.setText(response.body().getPrice());
                            price_s_p.setText(getResources().getString(R.string.sudaness_pound));
                            tv_rowcount.setText(String.valueOf(show_warehouseDetails_adapter.getItemCount()));
                        }
                    } else {
                        loading.dismiss();
                       // Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar() + "\n" + response.body().getMessage_en(), Warehouse_Details.this);
                        SharedPreferences sharedPreferences = getSharedPreferences("langdb", Context.MODE_PRIVATE);
                        String lang = sharedPreferences.getString("lang", "ar");
                        if(lang.equals("ar")) {
                            Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar(), Warehouse_Details.this);
                        }else if(lang.equals("en")){
                            Utility.showAlertDialog(getString(R.string.error),  response.body().getMessage_en(), Warehouse_Details.this);
                        }
                    }
                } else {
                    loading.dismiss();
                    Intent intent = new Intent(Warehouse_Details.this,Warehouse.class);
                    startActivity(intent);
                    finish();
                    Log.i(TAG_server, response.errorBody().toString());
                    //Utility.showAlertDialog(getString(R.string.error), getString(R.string.servererror), Warehouse_Details.this);
                }
            }

            @Override
            public void onFailure(Call<WarehouseDetailsResponse> call, Throwable t) {
                loading.dismiss();
                Utility.printLog(Tag_failure, t.getMessage());
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
                        GetWarehouseDetails(added_Date);
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}