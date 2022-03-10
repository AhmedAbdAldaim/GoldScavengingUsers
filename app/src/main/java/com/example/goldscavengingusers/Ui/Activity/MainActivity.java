package com.example.goldscavengingusers.Ui.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goldscavengingusers.Local_DB.LocalSession;
import com.example.goldscavengingusers.Local_DB.Sqllite_goldkarat;
import com.example.goldscavengingusers.Model.ShowGoldbarsResponse;
import com.example.goldscavengingusers.Model.navigationModel;
import com.example.goldscavengingusers.Network.ApiClient;
import com.example.goldscavengingusers.Network.RequestInterface;
import com.example.goldscavengingusers.R;
import com.example.goldscavengingusers.Ui.Adapter.Show_Main_Adapter;
import com.example.goldscavengingusers.Ui.Adapter.navig_adapter;
import com.example.goldscavengingusers.Utilty.Utility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FloatingActionButton floatingActionButton;
    EditText edsearh;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    Show_Main_Adapter showAdapter;
    TextView tv_rowcount,tv_empty,tv_connect;
    Button button_connect;
    ImageView list_karkot;
    ListView listView;
    LocalSession localSession;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    Sqllite_goldkarat sqllite_goldkarat;


    private static final String TAG_server = "Server";
    private static final String Tag_failure = "failure";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_connect = findViewById(R.id.connection);
        button_connect = findViewById(R.id.btnconnection);
        listView = findViewById(R.id.listview);
        list_karkot = findViewById(R.id.list);
        edsearh = findViewById(R.id.search);
        recyclerView = findViewById(R.id.rec);
        tv_empty = findViewById(R.id.empty);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        swipeRefreshLayout = findViewById(R.id.swipe);
        tv_rowcount = findViewById(R.id.rowcount);

        sqllite_goldkarat = new Sqllite_goldkarat(this);
        localSession = new LocalSession(this);
        ConnectivityManager connectivityManager = ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE));



       //Header Of Navigaition
        ArrayList<navigationModel> arrayList = new ArrayList<navigationModel>();
        arrayList.add(new navigationModel(localSession.getName(),localSession.getPhone()));
        navig_adapter arrayAdapter = new navig_adapter(MainActivity.this,R.layout.activity_navig,arrayList);
        listView.setAdapter(arrayAdapter);

        //Navigation
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        navigationView = (NavigationView) findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);


        list_karkot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sqllite_goldkarat.getRowCount()==0)
                {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.empty), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    startActivity(new Intent(MainActivity.this, Expected_Caliber_List.class));
                }
            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Goldbar_Add.class));
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(recyclerView.getVisibility()==View.INVISIBLE)
                {
                    tv_connect.setVisibility(View.INVISIBLE);
                    button_connect.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    GetGoldBars();
                }
                tv_connect.setVisibility(View.INVISIBLE);
                button_connect.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                GetGoldBars();
            }
        });

        //<-- SEARCH -->
        edsearh.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    showAdapter.getFilter().filter(charSequence);
                }catch (Exception e){ }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    showAdapter.getFilter().filter(charSequence);
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
            GetGoldBars();
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
                    GetGoldBars();
                }
            });
        }
    }

   // <-- Navigation -->
    public void open_navigation(View view) {
        drawerLayout.openDrawer(GravityCompat.START);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.m1:
                startActivity(new Intent(MainActivity.this, Edit_Profile.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.m2:
                startActivity(new Intent(MainActivity.this, Language.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.m3:
                startActivity(new Intent(MainActivity.this, Warehouse.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.m7:
                startActivity(new Intent(MainActivity.this, WarehouseRecycleBin.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.m4:
                drawerLayout.closeDrawer(GravityCompat.START);
                String name = "GoldBars";
                String url = "http://com.play.store/app/?id=" + getPackageName();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, name + "\n" + url);
                startActivity(intent);
                break;
            case R.id.m5:
                startActivity(new Intent(MainActivity.this, Aboutapp.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.m6:
                drawerLayout.closeDrawer(GravityCompat.START);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View view1 = getLayoutInflater().inflate(R.layout.logout_massage, null);
                Button btn_yes = view1.findViewById(R.id.btn_yes);
                Button btn_no = view1.findViewById(R.id.btn_no);

                builder.setView(view1);
                final AlertDialog dialog = builder.create();
                btn_yes.setOnClickListener(v ->
                {
                    LocalSession.clearSession();
                    Intent intent1 = new Intent(MainActivity.this, Login.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);
                    finish();

                });
                btn_no.setOnClickListener(v -> dialog.dismiss());
                dialog.show();
        }
        return false;
    }


    //<--  Git All Goldbars -->
    public void GetGoldBars(){
        ProgressDialog loading = ProgressDialog.show(this,null,getString(R.string.wait), false, false);
        loading.setContentView(R.layout.progressbar);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);


        // <-- Connect WIth Network And Check Response Successful or Failure -- >
        final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
        Call<ShowGoldbarsResponse> call= requestInterface.Show_Goldbars("Bearer "+ LocalSession.getToken());
        call.enqueue(new Callback<ShowGoldbarsResponse>() {
            @Override
            public void onResponse(Call<ShowGoldbarsResponse> call, Response<ShowGoldbarsResponse> response)
            {
                if(response.isSuccessful())
                {
                    if(!response.body().isError())
                    {
                     showAdapter = new Show_Main_Adapter(response.body().getShowGoldbarsModels(), MainActivity.this);
                     if (showAdapter.getItemCount() == 0 )
                        {
                            tv_empty.setVisibility(View.VISIBLE);
                            tv_rowcount.setVisibility(View.VISIBLE);
                            tv_rowcount.setText(String.valueOf(sqllite_goldkarat.getRowCount()));
                            swipeRefreshLayout.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setRefreshing(false);
                            recyclerView.setVisibility(View.INVISIBLE);
                            tv_empty.setText(R.string.empty);
                            loading.dismiss();
                            return;
                        }
                        else if(showAdapter.getItemCount()>0)
                        {
                         tv_empty.setVisibility(View.INVISIBLE);
                         tv_rowcount.setVisibility(View.VISIBLE);
                         tv_rowcount.setText(String.valueOf(sqllite_goldkarat.getRowCount()));
                         showAdapter.notifyDataSetChanged();
                         recyclerView.setAdapter(showAdapter);
                         loading.dismiss();
                        }
                    }
                    else
                    {
                       loading.dismiss();
                       //  Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar() + "\n" + response.body().getMessage_en(), MainActivity.this);
                        SharedPreferences sharedPreferences = getSharedPreferences("langdb", Context.MODE_PRIVATE);
                        String lang = sharedPreferences.getString("lang", "ar");
                        if(lang.equals("ar")) {
                            Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar(), MainActivity.this);
                        }else if(lang.equals("en")){
                            Utility.showAlertDialog(getString(R.string.error),  response.body().getMessage_en(), MainActivity.this);
                        }
                    }
                }
                else
                {
                    loading.dismiss();
                    Log.i(TAG_server, response.errorBody().toString());
                    Utility.showAlertDialog(getString(R.string.error), getString(R.string.servererror), MainActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ShowGoldbarsResponse> call, Throwable t) {
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
                        GetGoldBars();
                    }
                });
            }
        });
    }


    public void listkartot(View view) {
        startActivity(new Intent(this,Expected_Caliber_List.class));
    }

}
