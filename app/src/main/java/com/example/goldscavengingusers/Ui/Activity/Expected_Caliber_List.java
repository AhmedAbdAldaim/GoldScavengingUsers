package com.example.goldscavengingusers.Ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goldscavengingusers.Local_DB.LocalSession;
import com.example.goldscavengingusers.Local_DB.Sqllite_goldkarat;
import com.example.goldscavengingusers.Model.AddToWarehouseResponse;
import com.example.goldscavengingusers.Model.ShowGoldbarsModel;
import com.example.goldscavengingusers.Network.ApiClient;
import com.example.goldscavengingusers.Network.RequestInterface;
import com.example.goldscavengingusers.R;
import com.example.goldscavengingusers.Ui.Adapter.GoldKaratAdapter;
import com.example.goldscavengingusers.Utilty.Utility;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static android.view.View.VISIBLE;
import static maes.tech.intentanim.CustomIntent.customType;

public class Expected_Caliber_List extends AppCompatActivity {
    RecyclerView recyclerView;
    GoldKaratAdapter goldKaratAdapter;
    Cursor cursor_show,cursor_Gold_Wieght_Total ,cursor_Net_Gold_Total,cursor_pricr_Total ,cursor_goldbar_id;
    int cursor_goldbar_id_row  ;
    Sqllite_goldkarat sqlliteGoldkarat;
    Double Gold_Wieght,Gold_price,Gold_net,res;
    TextView net,gold,karat,price,tv_empty,NumOfGoldbars,price_s_p;
    LinearLayout linearLayout_successfull;
    CardView cardView_SAVE_IN_WAREHOUSE;
    String goldbar_id;
    private static final String Tag_failure = "failure";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expected_caliber_list);

        //Action Bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_expected_caliber);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        net = findViewById(R.id.netTotal);
        gold = findViewById(R.id.goldTotal);
        karat = findViewById(R.id.katrtTotal);
        price = findViewById(R.id.priceTotal);
        price_s_p = findViewById(R.id.price_sud_pound);
        tv_empty = findViewById(R.id.empty);
        linearLayout_successfull = findViewById(R.id.successful);
        NumOfGoldbars = findViewById(R.id.numofgoldbar);
        cardView_SAVE_IN_WAREHOUSE = findViewById(R.id.saved);


        ConnectivityManager connectivityManager = ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE));

        cardView_SAVE_IN_WAREHOUSE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Expected_Caliber_List.this);
                View view1 = getLayoutInflater().inflate(R.layout.savetowarehouse_massage, null);
                TextView massage1 = view1.findViewById(R.id.massage1);
                TextView massage2 = view1.findViewById(R.id.massage2);
                TextView massage3 = view1.findViewById(R.id.massage3);
                Button btn_sure = view1.findViewById(R.id.btn_yes);
                ImageView img_cancle = view1.findViewById(R.id.cancle);
                builder.setView(view1);
                massage1.setText(getResources().getString(R.string.savedinwarehouse_massage1));
                massage2.setText(getResources().getString(R.string.savedinwarehouse_massage2));
                massage3.setText(getResources().getString(R.string.savedinwarehouse_massage3));
                btn_sure.setText(getResources().getString(R.string.saved_confirm));
                final AlertDialog dialog = builder.create();
                sqlliteGoldkarat = new Sqllite_goldkarat(getApplicationContext());
                cursor_goldbar_id = sqlliteGoldkarat.getID();
                cursor_goldbar_id_row = sqlliteGoldkarat.getRowCount();

                btn_sure.setOnClickListener(v ->
                {
                    try {
                        ProgressDialog loading = ProgressDialog.show(Expected_Caliber_List.this, null, getString(R.string.wait), false, false);
                        loading.setContentView(R.layout.progressbar);
                        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        loading.setCancelable(false);
                        loading.setCanceledOnTouchOutside(false);
                      while (cursor_goldbar_id.moveToNext() && cursor_goldbar_id_row!=0) {
                         goldbar_id = cursor_goldbar_id.getString(0);
                          final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
                          if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()) {
                              Call<AddToWarehouseResponse> call = requestInterface.AddToWarehouse(goldbar_id, date(), "Bearer " + LocalSession.getToken());
                              dialog.dismiss();
                              call.enqueue(new Callback<AddToWarehouseResponse>() {
                                  @Override
                                  public void onResponse(Call<AddToWarehouseResponse> call, Response<AddToWarehouseResponse> response) {
                                      if (response.isSuccessful()) {
                                          if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()) {
                                              if (!response.body().isError()) {
                                                  if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()) {

                                                      if (cursor_goldbar_id.isLast() == false) {
                                                          if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()) {
                                                              loading.dismiss();
                                                              sqlliteGoldkarat.deleteall();
                                                              recyclerView.setVisibility(View.GONE);
                                                              linearLayout_successfull.setVisibility(VISIBLE);
                                                              Intent intent = new Intent(Expected_Caliber_List.this, MainActivity.class);
                                                              startActivity(intent);
                                                              finish();
                                                          } else {
                                                              Utility.showAlertDialog(getString(R.string.error), getString(R.string.connect_internet), Expected_Caliber_List.this);
                                                              loading.dismiss();
                                                          }
                                                      }


                                                      //  Toast.makeText(Expected_Caliber_List.this, getResources().getString(R.string.savedinwarehouse_succssefull) + "", Toast.LENGTH_SHORT).show();
                                                  } else {
                                                      Utility.showAlertDialog(getString(R.string.error), getString(R.string.connect_internet), Expected_Caliber_List.this);
                                                      loading.dismiss();
                                                  }
                                              } else {
                                                  //   Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar()  + "\n" + response.body().getMessage_en(), Expected_Caliber_List.this);
                                                  loading.dismiss();
                                                  SharedPreferences sharedPreferences = getSharedPreferences("langdb", Context.MODE_PRIVATE);
                                                  String lang = sharedPreferences.getString("lang", "ar");
                                                  if (lang.equals("ar")) {
                                                      Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar(), Expected_Caliber_List.this);
                                                  } else if (lang.equals("en")) {
                                                      Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_en(), Expected_Caliber_List.this);
                                                  }
                                              }
                                          } else {
                                              Utility.showAlertDialog(getString(R.string.error), getString(R.string.connect_internet), Expected_Caliber_List.this);
                                              loading.dismiss();
                                          }
                                      } else {
                                          Utility.showAlertDialog(getString(R.string.error), getString(R.string.servererror), Expected_Caliber_List.this);
                                          loading.dismiss();
                                      }
                                  }

                                  @Override
                                  public void onFailure(Call<AddToWarehouseResponse> call, Throwable t) {
                                      Utility.printLog(Tag_failure, t.getMessage());
                                      Utility.showAlertDialog(getString(R.string.error), getString(R.string.connect_internet_slow), Expected_Caliber_List.this);
                                      loading.dismiss();
                                  }
                              });
                          }
                          else{
                              Utility.showAlertDialog(getString(R.string.error), getString(R.string.connect_internet), Expected_Caliber_List.this);
                              loading.dismiss();
                          }

                        }

                    }finally {
                        cursor_goldbar_id.close();

                    }

                });

                img_cancle.setOnClickListener(v -> dialog.dismiss());
                dialog.show();
            }
        });

     Proccess();

    }

    // <-- GetDate Funcation -->
    private String date(){
             Calendar calendar= Calendar.getInstance();
             DateFormat motf = new SimpleDateFormat( "EE - dd MMM yyyy -  HH:mm:ss a ", Locale.forLanguageTag("ar"));
             DateFormat motf2 = new SimpleDateFormat( "EE - dd MMM yyyy - HH:mm:ss a ", Locale.ENGLISH);
             String date = motf.format(calendar.getTime());
             String date2 = motf2.format(calendar.getTime());
             return date+"\n"+date2;
    }

   public void Proccess()
   {
       recyclerView = findViewById(R.id.rec);
       GridLayoutManager linearLayoutManager = new GridLayoutManager(this,1,GridLayoutManager.VERTICAL,false);
       recyclerView.setLayoutManager(linearLayoutManager);
       ArrayList<ShowGoldbarsModel> arrayList = new ArrayList<ShowGoldbarsModel>();
       goldKaratAdapter = new GoldKaratAdapter(arrayList, Expected_Caliber_List.this);
       recyclerView.setAdapter(goldKaratAdapter);
       sqlliteGoldkarat = new Sqllite_goldkarat(this);
       recyclerView.setVisibility(VISIBLE);
       tv_empty.setVisibility(View.INVISIBLE);

       cursor_show = sqlliteGoldkarat.show();

       while (cursor_show.moveToNext()) {
           arrayList.add(new ShowGoldbarsModel(cursor_show.getString(0), cursor_show.getString(2), cursor_show.getString(3),
                   cursor_show.getString(4), cursor_show.getString(5), cursor_show.getString(6),cursor_show.getString(7),cursor_show.getString(8)));
       }
       //nuMofGoldbars
       NumOfGoldbars.setText(goldKaratAdapter.getItemCount()+"");
       cursor_Net_Gold_Total = sqlliteGoldkarat.getTotal_Gold_NET();
       if (cursor_Net_Gold_Total.moveToFirst()) {
           Gold_net = cursor_Net_Gold_Total.getDouble(0);
       }

       //Total GOLD WIEGHT
       cursor_Gold_Wieght_Total = sqlliteGoldkarat.getTotal_Gold();
       if (cursor_Gold_Wieght_Total.moveToFirst()) {
           Gold_Wieght = cursor_Gold_Wieght_Total.getDouble(0);
       }

       //TOTAL PRICE
       cursor_pricr_Total = sqlliteGoldkarat.getTotal_PRICE_NET();
       if (cursor_pricr_Total.moveToFirst()) {
           Gold_price = cursor_pricr_Total.getDouble(0);
       }


       //net
       net.setText(String.valueOf(Gold_net));


       //price
       price.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.ENGLISH).format(Gold_price)));
       price_s_p.setText(getResources().getString(R.string.sudaness_pound));

       //gold
       DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
       DecimalFormat decimalFormat =new DecimalFormat("0.0",decimalFormatSymbols);
       decimalFormat.setRoundingMode(RoundingMode.UP);
       gold.setText(String.valueOf(decimalFormat.format(Gold_Wieght)));

       //res
       res = (Gold_net / Gold_Wieght) * 875;


       //karkat
       karat.setText(String.valueOf(res));

       if (goldKaratAdapter.getItemCount()==0)
       {
           tv_empty.setVisibility(VISIBLE);

               recyclerView.setVisibility(View.INVISIBLE);
               tv_empty.setText(R.string.empty);
               Intent intent = new Intent(Expected_Caliber_List.this,MainActivity.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(intent);
               finish();

       }
   }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
