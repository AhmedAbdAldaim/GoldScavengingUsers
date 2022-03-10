package com.example.goldscavengingusers.Ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioButton;

import com.example.goldscavengingusers.R;

import java.util.Locale;

public class Language extends AppCompatActivity {
public RadioButton rad_ar,rad_en;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        //Action Bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_language);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rad_ar = findViewById(R.id.ar);
        rad_en = findViewById(R.id.en);
        Animation animationUtils = AnimationUtils.loadAnimation(this,R.anim.lang_ar_anim);
        Animation animationUtils1 = AnimationUtils.loadAnimation(this,R.anim.lang_en_anim);
        rad_ar.setAnimation(animationUtils);
        rad_en.setAnimation(animationUtils1);
        SharedPreferences sharedPreferences = getSharedPreferences("langdb", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();



            //check Lnaguage From SharePrefrences

        if(sharedPreferences.getString("lang",Locale.getDefault().getLanguage()).equals("ar")){
            rad_ar.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            rad_ar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_checked,0,0,0);

        }
         if(sharedPreferences.getString("lang",Locale.getDefault().getLanguage()).equals("en")){
            rad_en.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            rad_en.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_checked,0,0,0);

        }
         if(sharedPreferences.getString("lang",Locale.getDefault().getLanguage()).equals("ar")&&Locale.getDefault().getLanguage().equals("ar")){
            rad_ar.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            rad_ar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_checked,0,0,0);

        }  if(sharedPreferences.getString("lang",Locale.getDefault().getLanguage()).equals("en")&&Locale.getDefault().getLanguage().equals("en")){
            rad_en.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            rad_en.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_checked,0,0,0);

        }

        rad_ar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sharedPreferences.getString("lang",Locale.getDefault().getLanguage()).equals("ar")) {
                    return;
                }else{
                rad_ar.setTextColor(getResources().getColor(R.color.colorPrimaryDark));;
                rad_en.setTextColor(getResources().getColor(R.color.black));
                rad_ar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_checked,0,0,0);
                rad_en.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                Locale locale = new Locale("ar");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getResources().updateConfiguration(config,
                        getResources().getDisplayMetrics());

                editor.putString("lang",Locale.getDefault().getLanguage());
                editor.commit();
                    Intent intent = new Intent(Language.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }

            }
        });

        rad_en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sharedPreferences.getString("lang","ar").equals("en")){
                    return;
                }
                else{
                rad_en.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                rad_ar.setTextColor(getResources().getColor(R.color.black));
                rad_en.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_checked,0,0,0);
                rad_ar.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                Locale locale = new Locale("en");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getResources().updateConfiguration(config,
                        getResources().getDisplayMetrics());

                editor.putString("lang",Locale.getDefault().getLanguage());
                editor.commit();
                    recreate();
                    Intent intent = new Intent(Language.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
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
