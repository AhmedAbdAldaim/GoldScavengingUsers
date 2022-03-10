package com.example.goldscavengingusers.Ui.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.ConnectivityManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goldscavengingusers.Local_DB.LocalSession;
import com.example.goldscavengingusers.Local_DB.Sqllite_goldkarat;
import com.example.goldscavengingusers.Model.RecoveryGoldbarWarehousesResponse;
import com.example.goldscavengingusers.Model.RecoveryToWarehouseModel;
import com.example.goldscavengingusers.Model.RecoveryToWarehousesResponse;
import com.example.goldscavengingusers.Model.UpdateResponse;
import com.example.goldscavengingusers.Model.WarehouseDetailsModel;
import com.example.goldscavengingusers.Model.WarehouseRecycleBinModel;
import com.example.goldscavengingusers.Network.ApiClient;
import com.example.goldscavengingusers.Network.RequestInterface;
import com.example.goldscavengingusers.R;
import com.example.goldscavengingusers.Ui.Activity.MainActivity;
import com.example.goldscavengingusers.Ui.Activity.WarehouseRecycleBin;
import com.example.goldscavengingusers.Ui.Activity.Warehouse_Details;
import com.example.goldscavengingusers.Utilty.Utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static maes.tech.intentanim.CustomIntent.customType;

public class Show_WarehouseRecycleBin_Adapter extends RecyclerView.Adapter<Show_WarehouseRecycleBin_Adapter.ViewHolder> implements Filterable  {

    private Context context;
    private List<WarehouseRecycleBinModel> list;
    private List<WarehouseRecycleBinModel> filter;

    public Show_WarehouseRecycleBin_Adapter(List<WarehouseRecycleBinModel> warehouseRecycleBinModels, Context context) {
        this.context = context;
        this.list = warehouseRecycleBinModels;
        filter = new ArrayList<>(warehouseRecycleBinModels);
    }

    @NonNull
    @Override
    public Show_WarehouseRecycleBin_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.custom_warehouse_recyclebin, parent, false);
        return new Show_WarehouseRecycleBin_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Show_WarehouseRecycleBin_Adapter.ViewHolder holder, int position) {
        holder.Tv_gold_bar_owner.setText(list.get(position).getGold_bar_owner());
        holder.Tv_gold_ingot_weight.setText(list.get(position).getGold_ingot_weight());
        holder.Tv_sample_weight.setText(list.get(position).getSample_weight());
        holder.Tv_gold_karat_weight.setText(list.get(position).getGold_karat_weight());
        holder.Tv_net.setText(list.get(position).getNet());
        holder.TV_price_gram.setText(list.get(position).getPrice_gram()+ " " + context.getResources().getString(R.string.sudaness_pound));
        holder.TV_Price.setText(list.get(position).getPrice()+ " " + context.getResources().getString(R.string.sudaness_pound));
        holder.Tv_date.setText(list.get(position).getDate_add());


        holder.Tv_recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog loading = ProgressDialog.show(context, null, context.getString(R.string.wait), false, false);
                loading.setContentView(R.layout.progressbar);
                loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                loading.setCancelable(false);
                loading.setCanceledOnTouchOutside(false);

                final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
                Call<RecoveryToWarehousesResponse> call = requestInterface.RecoveryToWarehouses(list.get(position).getDate_add_item(),list.get(position).getGold_bar_id(), "Bearer " + LocalSession.getToken());
                call.enqueue(new Callback<RecoveryToWarehousesResponse>() {
                    @Override
                    public void onResponse(Call<RecoveryToWarehousesResponse> call, Response<RecoveryToWarehousesResponse> response) {
                        if (response.isSuccessful()) {
                            if (!response.body().isError())
                            {
                                Toast.makeText(context, context.getResources().getString(R.string.Recovered_successfully_warehousebin) + "", Toast.LENGTH_SHORT).show();
                                context.startActivity(new Intent(context, WarehouseRecycleBin.class));
                                customType(context,"fadein-to-fadeout");
                                ((Activity)context).finish();
                                loading.dismiss();

                            }
                            else
                             {
                                loading.dismiss();
                             //   Utility.showAlertDialog(context.getString(R.string.error), response.body().getMessage_ar() + "\n" + response.body().getMessage_en(), context);
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
                    public void onFailure(Call<RecoveryToWarehousesResponse> call, Throwable t) {
                        loading.dismiss();
                        Utility.showAlertDialog(context.getString(R.string.error),context.getString(R.string.connect_internet_slow), context);
                    }
                });

            }
        });

        holder.Tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.connectivityManager.getActiveNetworkInfo() != null && holder.connectivityManager.getActiveNetworkInfo().isConnected())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    View view1 = ((Activity) context).getLayoutInflater().inflate(R.layout.delete_goldbar_recyclebin, null);
                    TextView hidder = view1.findViewById(R.id.tv_hidder);
                    TextView massagenum1 = view1.findViewById(R.id.tv_content_num1);
                    TextView massagenum2 = view1.findViewById(R.id.tv_content_num2);
                    TextView massagenum3 = view1.findViewById(R.id.tv_content_num3);
                    Button btn_yes = view1.findViewById(R.id.btn_yes);
                    Button btn_no = view1.findViewById(R.id.btn_no);
                    hidder.setText(context.getResources().getString(R.string.remove_goldbar_recyclebin)+" "+ list.get(position).getGold_bar_owner());
                    massagenum1.setText(context.getResources().getString(R.string.remove_goldbar_recyclebin_num1));
                    massagenum2.setText(context.getResources().getString(R.string.remove_goldbar_recyclebin_num2));
                    massagenum3.setText(context.getResources().getString(R.string.remove_goldbar_recyclebin_num3));
                    btn_yes.setText(context.getResources().getString(R.string.remove_goldbar_recyclebin_sure));
                    btn_no.setText(context.getResources().getString(R.string.remove_goldbar_recyclebin_no));
                    builder.setView(view1);
                    final AlertDialog dialog = builder.create();
                    InsetDrawable insetDrawable = new InsetDrawable(new ColorDrawable(Color.TRANSPARENT),20);
                    dialog.getWindow().setBackgroundDrawable(insetDrawable);
                    btn_yes.setOnClickListener(v ->
                    {
                        dialog.dismiss();
                        ProgressDialog loading = ProgressDialog.show(context, null, context.getString(R.string.wait), false, false);
                        loading.setContentView(R.layout.progressbar);
                        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        loading.setCancelable(false);
                        loading.setCanceledOnTouchOutside(false);

                        final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
                        Call<RecoveryToWarehousesResponse> call = requestInterface.DeleteToMain(list.get(position).getId_warehouse(), "Bearer " + LocalSession.getToken());
                        call.enqueue(new Callback<RecoveryToWarehousesResponse>() {
                            @Override
                            public void onResponse(Call<RecoveryToWarehousesResponse> call, Response<RecoveryToWarehousesResponse> response) {
                                if (response.isSuccessful())
                                {
                                    if (!response.body().isError())
                                    {
                                        Toast.makeText(context, context.getResources().getString(R.string.Removed_successfully_warehouse_detail) + "", Toast.LENGTH_SHORT).show();
                                        context.startActivity(new Intent(context, WarehouseRecycleBin.class));
                                        customType(context,"fadein-to-fadeout");
                                        ((Activity)context).finish();
                                        loading.dismiss();

                                    }
                                    else
                                    {
                                        loading.dismiss();
                                //        Utility.showAlertDialog(context.getString(R.string.error), response.body().getMessage_ar() + "\n" + response.body().getMessage_en(), context);
                                        SharedPreferences sharedPreferences = context.getSharedPreferences("langdb", Context.MODE_PRIVATE);
                                        String lang = sharedPreferences.getString("lang", "ar");
                                        if(lang.equals("ar")) {
                                            Utility.showAlertDialog(context.getString(R.string.error), response.body().getMessage_ar(), context);
                                        }else if(lang.equals("en")){
                                            Utility.showAlertDialog(context.getString(R.string.error),  response.body().getMessage_en(), context);
                                        }
                                    }
                                } else {
                                    loading.dismiss();
                                    Utility.showAlertDialog(context.getString(R.string.error), context.getString(R.string.servererror), context);
                                }
                            }

                            @Override
                            public void onFailure(Call<RecoveryToWarehousesResponse> call, Throwable t) {
                                loading.dismiss();
                                Utility.showAlertDialog(context.getString(R.string.error), context.getString(R.string.connect_internet_slow), context);
                            }
                        });
                    });

                    btn_no.setOnClickListener(v -> dialog.dismiss());
                    dialog.show();
                }
                else
                {
                    Utility.showAlertDialog(context.getString(R.string.error), context.getString(R.string.connect_internet), context);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    //Search
    @Override
    public Filter getFilter() {
        return filterr;
    }

    public Filter filterr = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String key = charSequence.toString();
            List<WarehouseRecycleBinModel> warehouseRecycleBinModels = new ArrayList<>();
            if (key.isEmpty() || key.length() == 0)
            {
                warehouseRecycleBinModels.addAll(filter);
            }
            else
            {
                for (WarehouseRecycleBinModel item : list)
                {
                    if (item.getGold_bar_owner().toLowerCase().contains(key)|| item.getGold_bar_owner().toUpperCase().contains(key) || item.getGold_ingot_weight().toLowerCase().contains(key) || item.getGold_karat_weight().toLowerCase().contains(key))
                    {
                        warehouseRecycleBinModels.add(item);
                    }
                }

            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = warehouseRecycleBinModels;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list.clear();
            list.addAll((Collection<? extends WarehouseRecycleBinModel>) filterResults.values);
             notifyDataSetChanged();
        }
    };



    class ViewHolder extends RecyclerView.ViewHolder {
        TextView Tv_gold_bar_owner,Tv_gold_ingot_weight, Tv_sample_weight,Tv_gold_karat_weight,Tv_net,TV_price_gram,TV_Price,Tv_date,Tv_recovery,Tv_delete;
        ConnectivityManager connectivityManager;
        Sqllite_goldkarat sqllite_goldkarat;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Tv_gold_bar_owner = itemView.findViewById(R.id.name);
            Tv_gold_ingot_weight = itemView.findViewById(R.id.gold_wieght);
            Tv_sample_weight = itemView.findViewById(R.id.sample_weight);
            Tv_gold_karat_weight = itemView.findViewById(R.id.gold_karat_weight);
            Tv_net = itemView.findViewById(R.id.net_gold);
            TV_price_gram = itemView.findViewById(R.id.price_gram);
            TV_Price = itemView.findViewById(R.id.price);
            Tv_date = itemView.findViewById(R.id.addeddate);
            Tv_delete = itemView.findViewById(R.id.delete);
            Tv_recovery = itemView.findViewById(R.id.recovey);
            connectivityManager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
            sqllite_goldkarat = new Sqllite_goldkarat(context);

        }
    }
}





