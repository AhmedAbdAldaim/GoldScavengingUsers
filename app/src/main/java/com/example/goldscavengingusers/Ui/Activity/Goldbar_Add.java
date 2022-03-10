package com.example.goldscavengingusers.Ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goldscavengingusers.Local_DB.LocalSession;
import com.example.goldscavengingusers.Model.AddedResponse;
import com.example.goldscavengingusers.Network.ApiClient;
import com.example.goldscavengingusers.Network.RequestInterface;
import com.example.goldscavengingusers.R;
import com.example.goldscavengingusers.Utilty.Utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

public class Goldbar_Add extends AppCompatActivity {

    EditText ed_gold_bar_owner,ed_gold_ingot_weight,ed_sample_weight_,ed_gold_karat_weight,ed_price_gram;
    Button button_GoldbarAdd;


    private static final String TAG_server = "Server";
    private static final String Tag_failure = "failure";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goldbar_add);

        //Action Bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_goldbar_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ed_gold_bar_owner = findViewById(R.id.name);
        ed_gold_ingot_weight = findViewById(R.id.gold_wieght);
        ed_sample_weight_= findViewById(R.id.sample_weight);
        ed_gold_karat_weight = findViewById(R.id.gold_karat_weight);
        ed_price_gram = findViewById(R.id.price_gram);
        button_GoldbarAdd = findViewById(R.id.btn_add);


        ConnectivityManager connectivityManager = ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE));

        //<---EditText Hidden EditText Cursor When OnClick Done On Keyboard-->
        ed_price_gram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId()==ed_price_gram.getId()){
                    ed_price_gram.setCursorVisible(true);
                }
            }
        });
        ed_price_gram.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent event) {
                ed_price_gram.setCursorVisible(false);
                if(event !=null &&(event.getKeyCode()==KeyEvent.KEYCODE_ENTER)){
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(ed_price_gram.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });


        button_GoldbarAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String TV_gold_bar_owner = ed_gold_bar_owner.getText().toString().trim();
                String Tv_gold_ingot_weight = ed_gold_ingot_weight.getText().toString().trim();
                String Tv_sample_weight = ed_sample_weight_.getText().toString().trim();
                String Tv_gold_karat_weight = ed_gold_karat_weight.getText().toString().trim();
                String TV_price_gram = ed_price_gram.getText().toString().trim();

                    if (Valided(TV_gold_bar_owner,Tv_gold_ingot_weight,Tv_sample_weight,Tv_gold_karat_weight))
                    {
                        if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected())
                        {
                            if(!TV_price_gram.trim().isEmpty())
                            {
                                GoldbarAdd(TV_gold_bar_owner, Tv_gold_ingot_weight, Tv_sample_weight, Tv_gold_karat_weight, TV_price_gram);
                            }
                            else
                            {
                                GoldbarAdd(TV_gold_bar_owner, Tv_gold_ingot_weight, Tv_sample_weight, Tv_gold_karat_weight, "0");
                            }
                            }
                        else
                        {
                            Utility.showAlertDialog(getString(R.string.error), getString(R.string.connect_internet), Goldbar_Add.this);
                        }
                    }
                }
            });
        }



     // <-- Check Fields Function -->
    public Boolean Valided(String gold_bar_owner,String gold_ingot_weight,String sample_weight,String gold_karat_weight){
        if(gold_bar_owner.isEmpty()){
            ed_gold_bar_owner.setError(getResources().getString(R.string.gold_bar_owner_empty));
            ed_gold_bar_owner.requestFocus();
            return false;
        }

        if (gold_ingot_weight.isEmpty())
        {
            ed_gold_ingot_weight.setError(getString(R.string.gold_ingot_weight_empty));
            ed_gold_ingot_weight.requestFocus();
            return false;
        }
        if (sample_weight.isEmpty())
        {
            ed_sample_weight_.setError(getString(R.string.gold_sample_weight_empty));
            ed_sample_weight_.requestFocus();
            return false;
        }
        if (gold_karat_weight.isEmpty())
        {
            ed_gold_karat_weight.setError(getString(R.string.caliber_empty));
            ed_gold_karat_weight.requestFocus();
            return false;
        }
        return true;
    }


    // <-- Send Data TO request And Git Response Status -->
    private void GoldbarAdd(String gold_bar_owner,String gold_ingot_weight,String sample_weight,String gold_karat_weight,String price_gram){
        ProgressDialog loading = ProgressDialog.show(this,null,getString(R.string.wait), false, false);
        loading.setContentView(R.layout.progressbar);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);


        // <-- Connect WIth Network And Check Response Successful or Failure -- >
        final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
        Call<AddedResponse> call= requestInterface.Added_Owner(gold_bar_owner,gold_ingot_weight,sample_weight,gold_karat_weight,price_gram,date(),"Bearer "+ LocalSession.getToken());
        call.enqueue(new Callback<AddedResponse>() {
            @Override
            public void onResponse(Call<AddedResponse> call, Response<AddedResponse> response)
            {
                if(response.isSuccessful())
                {
                    if(!response.body().isError())
                    {
                        loading.dismiss();
                        Toast.makeText(Goldbar_Add.this, getResources().getString(R.string.goldbar_add_successfully)+"", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Goldbar_Add.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        loading.dismiss();
                   //     Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar() + "\n" + response.body().getMessage_en(), Goldbar_Add.this);
                        SharedPreferences sharedPreferences = getSharedPreferences("langdb", Context.MODE_PRIVATE);
                        String lang = sharedPreferences.getString("lang", "ar");
                        if(lang.equals("ar")) {
                            Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar(), Goldbar_Add.this);
                        }else if(lang.equals("en")){
                            Utility.showAlertDialog(getString(R.string.error),  response.body().getMessage_en(), Goldbar_Add.this);
                        }
                    }

                }
                else
                {
                    loading.dismiss();
                    Log.i(TAG_server, response.errorBody().toString());
                    Utility.showAlertDialog(getString(R.string.error), getString(R.string.servererror), Goldbar_Add.this);
                }
            }

            @Override
            public void onFailure(Call<AddedResponse> call, Throwable t) {
                loading.dismiss();
                Utility.showAlertDialog(getString(R.string.error),getString(R.string.connect_internet_slow),Goldbar_Add.this);
                Utility.printLog(Tag_failure, t.getMessage());
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

    private String date(){
        SharedPreferences sharedPreferences = getSharedPreferences("langdb", Context.MODE_PRIVATE);
        String lang = sharedPreferences.getString("lang", Locale.getDefault().getLanguage());

        Calendar calendar= Calendar.getInstance();
        if(lang.equals("en")) {
            DateFormat motf = new SimpleDateFormat("EE - dd MMM yyyy - HH:mm:ss a ", Locale.ENGLISH);
            String date = motf.format(calendar.getTime());
            return date;
        }else{
            DateFormat motf = new SimpleDateFormat("EE - dd MMM yyyy - HH:mm:ss a ");
            String date = motf.format(calendar.getTime());
            return date;
        }
    }
}
