package com.example.goldscavengingusers.Ui.Activity;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goldscavengingusers.Local_DB.LocalSession;
import com.example.goldscavengingusers.Model.LoginResponse;
import com.example.goldscavengingusers.Network.ApiClient;
import com.example.goldscavengingusers.Network.RequestInterface;
import com.example.goldscavengingusers.R;
import com.example.goldscavengingusers.Utilty.Utility;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Login extends AppCompatActivity {

    public EditText ed_phone, ed_password;
    Button button_login;
    String phone, password;
    ImageView imageView_visibilty, imageView_invisibilty;
    LocalSession localSession;
    private String android_id ;

    private static final String Tag_failure = "failure";
    private static final String TAG_server = "TAG_server";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ed_phone = findViewById(R.id.phone);
        ed_password = findViewById(R.id.password);
        button_login = findViewById(R.id.btn_login);
        imageView_visibilty = findViewById(R.id.visibiltyoff);
        imageView_invisibilty = findViewById(R.id.visibilty);


        localSession = new LocalSession(Login.this);
        ConnectivityManager connectivityManager = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));


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

        //WHEN CHANG PASSWORD FROM RESETPASWORD ACTIVITY
        Intent intent = getIntent();
        String phone_num = intent.getStringExtra("phone_number");
        String new_password = intent.getStringExtra("newpass");
        ed_phone.setText(phone_num);
        ed_password.setText(new_password);


        //<---EditText Hidden EditText Cursor When OnClick Done On Keyboard-->
        ed_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == ed_password.getId()) {
                    ed_password.setCursorVisible(true);
                }
            }
        });
        ed_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent event) {
                ed_password.setCursorVisible(false);
                if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(ed_password.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });


        //  <-- Onclick Login Button-->
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                //Get Mac Address
//                try {
//                    List<NetworkInterface> networkInterfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
//                    mac = "";
//                    for (NetworkInterface networkInterfacce : networkInterfaces) {
//                        if (networkInterfacce.getName().equalsIgnoreCase("wlan0")) {
//                            for (int i = 0; i < networkInterfacce.getHardwareAddress().length; i++) {
//                                String stringmac = Integer.toHexString(networkInterfacce.getHardwareAddress()[i] & 0xFF);
//                                if (stringmac.length() == 1) {
//                                    stringmac = "0" + stringmac;
//                                }
//                                mac = mac + stringmac.toLowerCase() + ":";
//                            }
//                            break;
//                        }
//
//                    }
//
//                } catch (SocketException e) {
//                    e.printStackTrace();
//                }
                android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                phone = ed_phone.getText().toString().trim();
                password = ed_password.getText().toString().trim();

                if (Valided(phone, password)) {
                    if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()) {
                        //<--Hidden Keyboard
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (inputMethodManager.isAcceptingText()) {
                            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                        Login(android_id,phone, password);

                    } else {
                        Utility.showAlertDialog(getString(R.string.error), getString(R.string.connect_internet), Login.this);
                    }
                }
            }
        });

    }

//    // <--Get Device ID -- >
//    public static String getDeviceID(Context context){
//        String device;
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
//            device = Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID);
//        }else{
//            final TelephonyManager telephonyManager =(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
//            if(telephonyManager.getDeviceId()!=null){
//                device = telephonyManager.getDeviceId();
//            }else{
//                device = Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID);
//            }
//        }
//        return device;
//    }

    // <-- Check Fields Function -->
    public Boolean Valided(String phone,String password){
        if(phone.isEmpty()){
            ed_phone.setError(getResources().getString(R.string.phone_empty));
            ed_phone.requestFocus();
            return false;
        }else if(!phone.matches("[0-9]{10}"))
        {
            ed_phone.setError(getString(R.string.phone_valid));
            ed_phone.requestFocus();
            return false;
        }

        if (password.isEmpty())
        {
            ed_password.setError(getString(R.string.password_empty));
            ed_password.requestFocus();
            return false;
        } else if(password.length() <8)
        {
            ed_password.setError(getString(R.string.password_check));
            ed_password.requestFocus();
            return false;
        }

        return true;
    }



     // <-- Send Data TO request And Git Response Status
    public void Login(String and_id,String phone,String password){
        ProgressDialog loading = ProgressDialog.show(this,null,getString(R.string.wait), false, false);
        loading.setContentView(R.layout.progressbar);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);


        // <-- Connect WIth Network And Check Response Successful or Failure -- >
        final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
        Call<LoginResponse> call= requestInterface.Login(and_id,phone,password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response)
            {
                if(response.isSuccessful())
                {
                    if(!response.body().isError())
                    {
                    loading.dismiss();
                     localSession.createSession(
                            response.body().getToken(),
                            response.body().getLoginModel().getId(),
                            response.body().getLoginModel().getName(),
                            response.body().getLoginModel().getPhone(),
                            response.body().getLoginModel().getShop(),
                            response.body().getLoginModel().getRole(),
                            response.body().getLoginModel().getMac_address(),
                            ed_password.getText().toString().trim());

                        Intent intent = new Intent(Login.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                     }
                     else
                     {
                      loading.dismiss();
                      //Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar()  + "\n" + response.body().getMessage_en(), Login.this);
                         SharedPreferences sharedPreferences = getSharedPreferences("langdb", Context.MODE_PRIVATE);
                         String lang = sharedPreferences.getString("lang", "ar");
                         if(lang.equals("ar")) {
                             Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar(), Login.this);
                         }else if(lang.equals("en")){
                             Utility.showAlertDialog(getString(R.string.error),  response.body().getMessage_en(), Login.this);
                         }
                     }
                }
                else
                {
                   loading.dismiss();
                   Utility.showAlertDialog(getString(R.string.error),getString(R.string.servererror),Login.this);
                   Log.i(TAG_server, response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loading.dismiss();
                Utility.showAlertDialog(getString(R.string.error),getString(R.string.connect_internet_slow),Login.this);
                Utility.printLog(Tag_failure, t.getMessage());
            }
        });
    }

    public void forgetpassword(View view) {
        startActivity(new Intent(Login.this, SendOtp.class));
    }

}

