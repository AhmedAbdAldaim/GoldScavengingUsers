package com.example.goldscavengingusers.Ui.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.goldscavengingusers.Local_DB.LocalSession;
import com.example.goldscavengingusers.Model.LoginResponse;
import com.example.goldscavengingusers.Network.ApiClient;
import com.example.goldscavengingusers.Network.RequestInterface;
import com.example.goldscavengingusers.R;
import com.example.goldscavengingusers.Utilty.Utility;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOldPassword_EditProfile extends AppCompatActivity {
    EditText ed_phone,ed_password;
    Button button_verifity;
    ImageView imageView_visibilty,imageView_invisibilty;
    LocalSession localSession;
    private static final String Tag_failure = "failure";
    private static final String TAG_server  = "TAG_server";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkoldpassword_edit_profile);

        //Action Bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_checkoldpassword_edit_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ed_phone = findViewById(R.id.phone);
        ed_password  = findViewById(R.id.oldpassword);
        button_verifity = findViewById(R.id.btn_verifity);
        imageView_visibilty = findViewById(R.id.visibiltyoff);
        imageView_invisibilty = findViewById(R.id.visibilty);


        localSession = new LocalSession(CheckOldPassword_EditProfile.this);
        ConnectivityManager connectivityManager = ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE));


        imageView_visibilty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView_visibilty.setVisibility(View.GONE);
                imageView_invisibilty.setVisibility(View.VISIBLE);
                ed_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
        });

        imageView_invisibilty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView_invisibilty.setVisibility(View.GONE);
                imageView_visibilty.setVisibility(View.VISIBLE);
                ed_password.setTransformationMethod(PasswordTransformationMethod.getInstance());


            }
        });

        ed_phone.setText(localSession.getPhone());

        //      <---EditText Hidden EditText Cursor When OnClick Done On Keyboard-->
        ed_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(view.getId()==ed_password.getId())
                {
                    ed_password.setCursorVisible(true);
                }
            }
        });
        ed_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent event) {
                ed_password.setCursorVisible(false);
                if(event !=null &&(event.getKeyCode()==KeyEvent.KEYCODE_ENTER)){
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(ed_password.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });


        //  <-- Onclick Register Button-->
        button_verifity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = ed_phone.getText().toString().trim();
                String password = ed_password.getText().toString().trim();

                if (Valided(password))
                {
                    if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected())
                    {
                        //<--Hidden Keyboard
                        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        if(inputMethodManager.isAcceptingText())
                        {
                            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                        PasswordVerifity(phone,password);
                    } else
                    {
                        Utility.showAlertDialog(getString(R.string.error), getString(R.string.connect_internet), CheckOldPassword_EditProfile.this);
                    }
                }

            }
        });
    }


    //<--   Check Fields Function -->
    public Boolean Valided(String password){
        if (password.isEmpty())
        {
            ed_password.setError(getString(R.string.oldpassword_edit_profile_empty));
            ed_password.requestFocus();
            return false;
        }
        return true;
    }


    // <-- Send Data TO request And Git Response Status
    private void PasswordVerifity(String phone,String password){
        ProgressDialog loading = ProgressDialog.show(this,null,getString(R.string.wait), false, false);
        loading.setContentView(R.layout.progressbar);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);

        // <-- Connect WIth Network And Check Response Successful or Failure -- >
        final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
        Call<LoginResponse> call= requestInterface.Login(localSession.getMactaddress(),phone,password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response)
            {
                if(response.isSuccessful()) {
                    if (!response.body().isError())
                    {

                        Intent intent = new Intent(getApplicationContext(), Edit_Password.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        loading.dismiss();
                        if(response.body().getMessage_ar().contains("رقم الهاتف")){
                            Utility.showAlertDialog(getString(R.string.error), getResources().getString(R.string.oldpassword_edit_profile_wrong), CheckOldPassword_EditProfile.this);
                        }else {
                            SharedPreferences sharedPreferences = getSharedPreferences("langdb", Context.MODE_PRIVATE);
                            String lang = sharedPreferences.getString("lang", "ar");
                            if(lang.equals("ar")) {
                                Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar(), CheckOldPassword_EditProfile.this);
                            }else if(lang.equals("en")){
                                Utility.showAlertDialog(getString(R.string.error),  response.body().getMessage_en(), CheckOldPassword_EditProfile.this);
                            }
                           // Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar() + "\n" + response.body().getMessage_en(), CheckOldPassword_EditProfile.this);
                        }
                    }
                }
                else {
                    loading.dismiss();
                    Log.i(TAG_server, response.errorBody().toString());
                    Utility.showAlertDialog(getString(R.string.error), getString(R.string.servererror), CheckOldPassword_EditProfile.this);
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loading.dismiss();
                Utility.showAlertDialog(getString(R.string.error),getString(R.string.connect_internet_slow),CheckOldPassword_EditProfile.this);
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
