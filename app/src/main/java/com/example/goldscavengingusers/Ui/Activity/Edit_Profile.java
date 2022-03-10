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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goldscavengingusers.Local_DB.LocalSession;
import com.example.goldscavengingusers.Model.UserUpdateResponse;
import com.example.goldscavengingusers.Network.ApiClient;
import com.example.goldscavengingusers.Network.RequestInterface;
import com.example.goldscavengingusers.R;
import com.example.goldscavengingusers.Utilty.Utility;

public class Edit_Profile extends AppCompatActivity {

    EditText ed_name,ed_phone,ed_shop,ed_password;
    Button button_editprofile,button_editprofile_before;
    TextView Tv_editpassword;
    LocalSession localSession;

    private static final String TAG_server = "Server";
    private static final String Tag_failure = "failure";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //Action Bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_edit_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ed_name = findViewById(R.id.name);
        ed_phone = findViewById(R.id.phone);
        ed_shop = findViewById(R.id.shop);
        ed_password = findViewById(R.id.password);
        button_editprofile_before = findViewById(R.id.btn_upd_before);
        button_editprofile = findViewById(R.id.btn_upd);
        Tv_editpassword = findViewById(R.id.editpassword);

        localSession = new LocalSession(this);
        ConnectivityManager connectivityManager = ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE));

        ed_name.setText(localSession.getName());
        ed_phone.setText(localSession.getPhone());
        ed_shop.setText(localSession.getShop());
        ed_password.setText(localSession.getPassword());



        // <-- Edit Password Button -->
        Tv_editpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Edit_Profile.this, CheckOldPassword_EditProfile.class));
            }
        });

        //<---EditText When Text Change-->
        ed_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().equals(localSession.getName())){
                    button_editprofile_before.setVisibility(View.INVISIBLE);
                    button_editprofile.setVisibility(View.VISIBLE);
                }else{
                    button_editprofile_before.setVisibility(View.VISIBLE);
                    button_editprofile.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //<---EditText When Text Change-->
        ed_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().equals(localSession.getPhone())){
                    button_editprofile_before.setVisibility(View.INVISIBLE);
                    button_editprofile.setVisibility(View.VISIBLE);
                }else{
                    button_editprofile_before.setVisibility(View.VISIBLE);
                    button_editprofile.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //<---EditText When Text Change-->
        ed_shop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().equals(localSession.getShop())){
                    button_editprofile_before.setVisibility(View.INVISIBLE);
                    button_editprofile.setVisibility(View.VISIBLE);
                }else{
                    button_editprofile_before.setVisibility(View.VISIBLE);
                    button_editprofile.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //<---EditText Hidden EditText Cursor When OnClick Done On Keyboard-->
        ed_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId()==ed_shop.getId()){
                    ed_shop.setCursorVisible(true);
                }
            }
        });
        ed_shop.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent event) {
                ed_shop.setCursorVisible(false);
                if(event !=null &&(event.getKeyCode()==KeyEvent.KEYCODE_ENTER)){
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(ed_shop.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });


        //  <-- Onclick EditProfile Button-->
        button_editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = ed_name.getText().toString().trim();
                String phone = ed_phone.getText().toString().trim();
                String shop = ed_shop.getText().toString().trim();
                String password = ed_password.getText().toString().trim();

                    if (Valided(name, phone,shop))
                    {
                        if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected())
                        {

                        //<--Hidden Keyboard
                        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        if(inputMethodManager.isAcceptingText()) {
                            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }

                        EditProfile(name,phone,shop,password);

                        }
                        else
                        {
                            Utility.showAlertDialog(getString(R.string.error), getString(R.string.connect_internet), Edit_Profile.this);
                        }
                    }
                }
           });
        }


    //   <--  Check Fields Function -->
    public Boolean Valided(String name,String phone,String shop){
        if(name.isEmpty()){
            ed_name.setError(getResources().getString(R.string.full_name_empty));
            ed_name.requestFocus();
            return false;
        }else if(name.length()>40){
            ed_name.setError(getResources().getString(R.string.check_fullname_length));
            ed_name.requestFocus();
            return false;
        }

        if(phone.isEmpty()){
            ed_phone.setError(getResources().getString(R.string.phone_edit_profile_empty));
            ed_phone.requestFocus();
            return false;
        }
        else if(!phone.matches("[0-9]{10}"))
        {
            ed_phone.setError(getString(R.string.phone_valid));
            ed_phone.requestFocus();
            return false;
        }

        if(shop.isEmpty()){
            ed_shop.setError(getResources().getString(R.string.shop_edit_profile_empty));
            ed_shop.requestFocus();
            return false;
        }

        return true;
    }



    // <-- Connect WIth Network And Check Response Successful or Failure -- >
    private void EditProfile(String name,String phone,String shop,String password){
        ProgressDialog loading = ProgressDialog.show(this,null,getString(R.string.wait), false, false);
        loading.setContentView(R.layout.progressbar);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);

        // <-- Connect WIth Network And Check Response Successful or Failure -- >
        final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
        Call<UserUpdateResponse> call= requestInterface.Edit_profile(LocalSession.getId(),name,phone,shop,password,"Bearer "+LocalSession.getToken());
        call.enqueue(new Callback<UserUpdateResponse>() {
            @Override
            public void onResponse(Call<UserUpdateResponse> call, Response<UserUpdateResponse> response){
                if(response.isSuccessful())
                {
                    if(!response.body().isError()) {
                        loading.dismiss();
                        localSession.createSession(
                                LocalSession.getToken(),
                                response.body().getUserUpdateModels().getId(),
                                response.body().getUserUpdateModels().getName(),
                                response.body().getUserUpdateModels().getPhone(),
                                response.body().getUserUpdateModels().getShop(),
                                response.body().getUserUpdateModels().getRole(),
                                response.body().getUserUpdateModels().getMac_address(),
                                ed_password.getText().toString().trim());

                        Toast.makeText(Edit_Profile.this, R.string.edit_profile_successfully, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Edit_Profile.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        loading.dismiss();
                     //   Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar() + "\n" + response.body().getMessage_en(), Edit_Profile.this);
                        SharedPreferences sharedPreferences = getSharedPreferences("langdb", Context.MODE_PRIVATE);
                        String lang = sharedPreferences.getString("lang", "ar");
                        if(lang.equals("ar")) {
                            Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar(), Edit_Profile.this);
                        }else if(lang.equals("en")){
                            Utility.showAlertDialog(getString(R.string.error),  response.body().getMessage_en(), Edit_Profile.this);
                        }
                    }
                }
                else
                {
                    loading.dismiss();
                    Log.i(TAG_server, response.errorBody().toString());
                    Utility.showAlertDialog(getString(R.string.error), getString(R.string.servererror), Edit_Profile.this);
                }
            }

            @Override
            public void onFailure(Call<UserUpdateResponse> call, Throwable t) {
                loading.dismiss();
                Utility.showAlertDialog(getString(R.string.error),getString(R.string.connect_internet_slow),Edit_Profile.this);
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
}
