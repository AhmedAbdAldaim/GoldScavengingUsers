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
import android.widget.Toast;

import com.example.goldscavengingusers.Local_DB.LocalSession;
import com.example.goldscavengingusers.Model.UserUpdateResponse;
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

public class Edit_Password extends AppCompatActivity {

    EditText ed_name,ed_phone,ed_shop,ed_newpassword,ed_passwordconfirm;
    Button button_upd;
    ImageView imageView_visibilty,imageView_invisibilty;
    LocalSession localSession;

    private static final String TAG_server = "Server";
    private static final String Tag_failure = "failure";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__password);

        //Action Bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_checkoldpassword_edit_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ed_name = findViewById(R.id.name);
        ed_phone = findViewById(R.id.phone);
        ed_shop = findViewById(R.id.shop);
        ed_newpassword = findViewById(R.id.newpassword);
        ed_passwordconfirm = findViewById(R.id.newpasswordconfirm);
        button_upd = findViewById(R.id.btn_upd);
        imageView_visibilty = findViewById(R.id.visibiltyoff);
        imageView_invisibilty = findViewById(R.id.visibilty);

        imageView_visibilty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView_visibilty.setVisibility(View.GONE);
                imageView_invisibilty.setVisibility(View.VISIBLE);
                ed_passwordconfirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
        });

        imageView_invisibilty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView_invisibilty.setVisibility(View.GONE);
                imageView_visibilty.setVisibility(View.VISIBLE);
                ed_passwordconfirm.setTransformationMethod(PasswordTransformationMethod.getInstance());


            }
        });


        localSession = new LocalSession(this);
        ConnectivityManager connectivityManager = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));

        ed_name.setText(localSession.getName());
        ed_phone.setText(localSession.getPhone());
        ed_shop.setText(localSession.getShop());

        //      <---EditText Hidden EditText Cursor When OnClick Done On Keyboard-->
        ed_passwordconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == ed_passwordconfirm.getId()) {
                    ed_passwordconfirm.setCursorVisible(true);
                }
            }
        });
        ed_passwordconfirm.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent event) {
                ed_passwordconfirm.setCursorVisible(false);
                if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(ed_passwordconfirm.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });


        // <-- Onclick Update Button -->
        button_upd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ed_name.getText().toString().trim();
                String phone = ed_phone.getText().toString().trim();
                String shop = ed_shop.getText().toString().trim();
                String newpassword = ed_newpassword.getText().toString().trim();
                String passwordconfrim = ed_passwordconfirm.getText().toString().trim();

                if (Valided(newpassword, passwordconfrim))
                {
                    if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()) {
                        //<--Hidden Keyboard
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (inputMethodManager.isAcceptingText()) {
                            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }

                        EditProfile_Password(name, phone, shop,ed_newpassword.getText().toString());
                    }
                    else
                    {
                        Utility.showAlertDialog(getString(R.string.error), getString(R.string.connect_internet), Edit_Password.this);
                    }
                }
            }
        });
    }


    //<--   Check Fields Function -->
    public Boolean Valided(String newPassword,String passwordconfirm){

        if (newPassword.isEmpty())
        {
            ed_newpassword.setError(getString(R.string.newpassword_edit_profile_empty));
            ed_newpassword.requestFocus();
            return false;
        }else if(newPassword.length() <8)
        {
            ed_newpassword.setError(getString(R.string.password_check));
            ed_newpassword.requestFocus();
            return false;
        }
        if(passwordconfirm.isEmpty()){
            ed_passwordconfirm.setError(getString(R.string.password_confirm_edit_profile_empty));
            ed_passwordconfirm.requestFocus();
            return false;
        }
        else if (!newPassword.isEmpty()&&!passwordconfirm.equals(newPassword))
        {
            ed_passwordconfirm.setError(getString(R.string.password_similarity_edit_profile));
            ed_passwordconfirm.requestFocus();
            return false;
        }

        return true;

    }



    // <--  Send Data TO request And Git Response Status -->
    private void EditProfile_Password(String name,String phone,String shop,String password){
        ProgressDialog loading = ProgressDialog.show(this,null,getString(R.string.wait), false, false);
        loading.setContentView(R.layout.progressbar);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);

        // <-- Connect WIth Network And Check Response Successful or Failure -- >
        final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
        Call<UserUpdateResponse> call= requestInterface.Edit_profile(localSession.getId(),name,phone,shop,password,"Bearer "+LocalSession.getToken());
        call.enqueue(new Callback<UserUpdateResponse>() {
            @Override
            public void onResponse(Call<UserUpdateResponse> call, Response<UserUpdateResponse> response){
                if(response.isSuccessful())
                {
                    if(!response.body().isError()) {
                        localSession.createSession(
                                LocalSession.getToken(),
                                response.body().getUserUpdateModels().getId(),
                                response.body().getUserUpdateModels().getName(),
                                response.body().getUserUpdateModels().getPhone(),
                                response.body().getUserUpdateModels().getShop(),
                                response.body().getUserUpdateModels().getRole(),
                                response.body().getUserUpdateModels().getMac_address(),
                                ed_newpassword.getText().toString());
                        Toast.makeText(Edit_Password.this, R.string.edit_profile_successfully, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Edit_Password.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }else{
                        loading.dismiss();
                      //  Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar() + "\n" + response.body().getMessage_en(), Edit_Password.this);
                        SharedPreferences sharedPreferences = getSharedPreferences("langdb", Context.MODE_PRIVATE);
                        String lang = sharedPreferences.getString("lang", "ar");
                        if(lang.equals("ar")) {
                            Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar(), Edit_Password.this);
                        }else if(lang.equals("en")){
                            Utility.showAlertDialog(getString(R.string.error),  response.body().getMessage_en(), Edit_Password.this);
                        }
                    }
                }
                else
                {
                    loading.dismiss();
                    Log.i(TAG_server, response.errorBody().toString());
                    Utility.showAlertDialog(getString(R.string.error), getString(R.string.servererror), Edit_Password.this);
                }
            }

            @Override
            public void onFailure(Call<UserUpdateResponse> call, Throwable t) {
                loading.dismiss();
                Utility.showAlertDialog(getString(R.string.error),getString(R.string.connect_internet_slow),Edit_Password.this);
                Utility.printLog(Tag_failure, t.getMessage());
            }
        });
    }


        @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            startActivity(new Intent(Edit_Password.this,MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Edit_Password.this,MainActivity.class));
        finish();
        super.onBackPressed();
    }
}
