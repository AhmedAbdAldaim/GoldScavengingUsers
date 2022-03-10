package com.example.goldscavengingusers.Ui.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goldscavengingusers.Local_DB.LocalSession;
import com.example.goldscavengingusers.Model.GoldBarStatusResponse;
import com.example.goldscavengingusers.Network.ApiClient;
import com.example.goldscavengingusers.Network.RequestInterface;
import com.example.goldscavengingusers.Ui.Activity.Edit_Password;
import com.example.goldscavengingusers.Ui.Activity.Expected_Caliber_List;
import com.example.goldscavengingusers.Local_DB.Sqllite_goldkarat;
import com.example.goldscavengingusers.Model.ShowGoldbarsModel;
import com.example.goldscavengingusers.R;
import com.example.goldscavengingusers.Ui.Activity.MainActivity;
import com.example.goldscavengingusers.Utilty.Utility;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static maes.tech.intentanim.CustomIntent.customType;

public class GoldKaratAdapter extends RecyclerView.Adapter<GoldKaratAdapter.ViewHolder>  {

    private Context context;
    private List<ShowGoldbarsModel> list;

    public GoldKaratAdapter(List<ShowGoldbarsModel> showAddedModels, Context context) {
        this.context = context;
        this.list = showAddedModels;

    }

    @NonNull
    @Override
    public GoldKaratAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.custom_expected_caliber_list, parent, false);
        return new GoldKaratAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull GoldKaratAdapter.ViewHolder holder, int position) {
        holder.Tv_gold_bar_owner.setText(list.get(position).getGold_bar_owner());
        holder.Tv_gold_ingot_weight.setText(list.get(position).getGold_ingot_weight());
        holder.Tv_sample_weight.setText(list.get(position).getSample_weight());
        holder.Tv_gold_karat_weight.setText(list.get(position).getGold_karat_weight());
        holder.Tv_net.setText(list.get(position).getNet());
        holder.TV_price_gram.setText(list.get(position).getPrice_gram());
        holder.TV_price_gram_s_p.setText(context.getResources().getString(R.string.sudaness_pound));
        holder.TV_price.setText(list.get(position).getPrice());
        holder.TV_price_s_p.setText(context.getResources().getString(R.string.sudaness_pound));
        holder.img_recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog loading = ProgressDialog.show(context, null, context.getString(R.string.wait), false, false);
                loading.setContentView(R.layout.progressbar);
                loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                loading.setCancelable(false);
                loading.setCanceledOnTouchOutside(false);

                final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
                Call<GoldBarStatusResponse> call = requestInterface.Goldbarstatus(list.get(position).getId(), "active", "Bearer " + LocalSession.getToken());
                call.enqueue(new Callback<GoldBarStatusResponse>() {
                    @Override
                    public void onResponse(Call<GoldBarStatusResponse> call, Response<GoldBarStatusResponse> response) {
                        if (response.isSuccessful()) {
                            if (!response.body().isError())
                            {

                                holder.sqlliteGoldkarat.delete(list.get(position).getId());
                                context.startActivity(new Intent(context, Expected_Caliber_List.class));
                                customType(context,"fadein-to-fadeout");
                                ((Activity)context).finish();
                                loading.dismiss();
                                if (getItemCount() == 0)
                                {
                                    Intent intent = new Intent(context, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                    ((Activity) context).finish();
                                }

                            } else{
                                loading.dismiss();
                               // Utility.showAlertDialog(context.getString(R.string.error), response.body().getMessage_ar() + "\n" + response.body().getMessage_en(), context);
                                SharedPreferences sharedPreferences = context.getSharedPreferences("langdb", Context.MODE_PRIVATE);
                                String lang = sharedPreferences.getString("lang", "ar");
                                if(lang.equals("ar")) {
                                    Utility.showAlertDialog(context.getString(R.string.error), response.body().getMessage_ar(), context);
                                }else if(lang.equals("en")){
                                    Utility.showAlertDialog(context.getString(R.string.error),  response.body().getMessage_en(), context);
                                }

                            }
                        }
                        else
                            {
                                loading.dismiss();
                                Utility.showAlertDialog(context.getString(R.string.error), context.getString(R.string.servererror), context);
                            }
                    }

                    @Override
                    public void onFailure(Call<GoldBarStatusResponse> call, Throwable t) {
                        loading.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        View view1 = ((Activity) context).getLayoutInflater().inflate(R.layout.error_massage, null);
                        TextView title = view1.findViewById(R.id.title);
                        TextView content = view1.findViewById(R.id.tv_Massage);
                        title.setText(R.string.error);
                        content.setText(R.string.connect_internet_slow);
                        TextView btn = view1.findViewById(R.id.btn_post);
                        btn.setText(R.string.try_agin);
                        builder.setView(view1);

                        AlertDialog alertDialog = builder.create();
                        btn.setOnClickListener(v ->
                        {
                            alertDialog.dismiss();
                            alertDialog.setCancelable(true);

                        });
                        alertDialog.show();
                    }
                });

            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }






    class ViewHolder extends RecyclerView.ViewHolder {
        TextView Tv_gold_bar_owner,Tv_gold_ingot_weight, Tv_sample_weight,Tv_gold_karat_weight,Tv_net,TV_price_gram,TV_price,TV_price_gram_s_p,TV_price_s_p,img_recovery;
         Sqllite_goldkarat sqlliteGoldkarat;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);;
            Tv_gold_bar_owner = itemView.findViewById(R.id.name);
            Tv_gold_ingot_weight = itemView.findViewById(R.id.gold_wieght);
            Tv_sample_weight = itemView.findViewById(R.id.sample_weight);
            Tv_gold_karat_weight = itemView.findViewById(R.id.gold_karat_weight);
            img_recovery = itemView.findViewById(R.id.recovey);
            Tv_net = itemView.findViewById(R.id.net_gold);
            TV_price_gram = itemView.findViewById(R.id.price_gram);
            TV_price = itemView.findViewById(R.id.price);
            TV_price_gram_s_p = itemView.findViewById(R.id.price_sud_pound1);
            TV_price_s_p = itemView.findViewById(R.id.price_sud_pound2);

            sqlliteGoldkarat = new Sqllite_goldkarat(context);


        }
    }
}




