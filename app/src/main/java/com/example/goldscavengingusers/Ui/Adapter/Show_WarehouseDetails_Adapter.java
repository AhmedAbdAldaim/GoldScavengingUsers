package com.example.goldscavengingusers.Ui.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.ConnectivityManager;
import android.util.Log;
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
import com.example.goldscavengingusers.Model.DeleteResponse;
import com.example.goldscavengingusers.Model.GoldBarStatusResponse;
import com.example.goldscavengingusers.Model.RecoveryGoldbarWarehousesResponse;
import com.example.goldscavengingusers.Model.ShowWarehouseModel;
import com.example.goldscavengingusers.Model.UpdateResponse;
import com.example.goldscavengingusers.Model.WarehouseDetailsModel;
import com.example.goldscavengingusers.Network.ApiClient;
import com.example.goldscavengingusers.Network.RequestInterface;
import com.example.goldscavengingusers.R;
import com.example.goldscavengingusers.Ui.Activity.Expected_Caliber_List;
import com.example.goldscavengingusers.Ui.Activity.MainActivity;
import com.example.goldscavengingusers.Ui.Activity.Warehouse;
import com.example.goldscavengingusers.Ui.Activity.Warehouse_Details;
import com.example.goldscavengingusers.Ui.Activity.Weight_Edit;
import com.example.goldscavengingusers.Utilty.Utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static maes.tech.intentanim.CustomIntent.customType;

public class Show_WarehouseDetails_Adapter extends RecyclerView.Adapter<Show_WarehouseDetails_Adapter.ViewHolder> implements Filterable  {

    private Context context;
    private List<WarehouseDetailsModel> list;
    private List<WarehouseDetailsModel> filter;

    public Show_WarehouseDetails_Adapter(List<WarehouseDetailsModel> warehouseDetailsModels, Context context) {
        this.context = context;
        this.list = warehouseDetailsModels;
        filter = new ArrayList<>(warehouseDetailsModels);
    }

    @NonNull
    @Override
    public Show_WarehouseDetails_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.custom_warehouse_details, parent, false);
        return new Show_WarehouseDetails_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Show_WarehouseDetails_Adapter.ViewHolder holder, int position) {
        holder.Tv_gold_bar_owner.setText(list.get(position).getGold_bar_owner());
        holder.Tv_gold_ingot_weight.setText(list.get(position).getGold_ingot_weight());
        holder.Tv_sample_weight.setText(list.get(position).getSample_weight());
        holder.Tv_gold_karat_weight.setText(list.get(position).getGold_karat_weight());
        holder.Tv_net.setText(list.get(position).getNet());
        holder.TV_price_gram.setText(list.get(position).getPrice_gram()+ " " + context.getResources().getString(R.string.sudaness_pound));
        holder.TV_Price.setText(list.get(position).getPrice()+ " " + context.getResources().getString(R.string.sudaness_pound));
        holder.Tv_date.setText(list.get(position).getDate_add());

        holder.Tv_upd.setOnClickListener(new View.OnClickListener() {
            EditText ed_gold_bar_owner,ed_gold_ingot_weight,ed_sample_weight_,ed_gold_karat_weight,ed_price_gram;
            String Tv_gold_bar_owner,Tv_gold_ingot_weight,Tv_sample_weight,Tv_gold_karat_weight,TV_price_gram;
            Double Total_net,Res_net;
            Button button_wieght_edit;
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view1 = ((Activity) context).getLayoutInflater().inflate(R.layout.custom_warehouse_weight_edit, null);

                ed_gold_bar_owner = view1.findViewById(R.id.name);
                ed_gold_ingot_weight  = view1.findViewById(R.id.gold_wieght);
                ed_sample_weight_  = view1.findViewById(R.id.sample_weight);
                ed_gold_karat_weight  = view1.findViewById(R.id.gold_karat_weight);
                ed_price_gram = view1.findViewById(R.id.price_gram);
                button_wieght_edit = view1.findViewById(R.id.btn_upd);

                ed_gold_bar_owner.setText(list.get(position).getGold_bar_owner());
                ed_gold_ingot_weight.setText(list.get(position).getGold_ingot_weight());
                ed_sample_weight_.setText(list.get(position).getSample_weight());
                ed_gold_karat_weight.setText(list.get(position).getGold_karat_weight());
                ed_price_gram.setText(list.get(position).getPrice_gram());

                builder.setView(view1);
                final AlertDialog dialog = builder.create();
                InsetDrawable insetDrawable = new InsetDrawable(new ColorDrawable(Color.TRANSPARENT),20);
                dialog.getWindow().setBackgroundDrawable(insetDrawable);
                 button_wieght_edit.setOnClickListener(v ->
                 {
                     Tv_gold_bar_owner = ed_gold_bar_owner.getText().toString().trim();
                     Tv_gold_ingot_weight = ed_gold_ingot_weight.getText().toString().trim();
                     Tv_sample_weight = ed_sample_weight_.getText().toString().trim();
                     Tv_gold_karat_weight = ed_gold_karat_weight.getText().toString().trim();
                     TV_price_gram = ed_price_gram.getText().toString().trim();

                    if (Valided(Tv_gold_bar_owner,Tv_gold_ingot_weight,Tv_sample_weight,Tv_gold_karat_weight))
                    {
                        Total_net = (Double.valueOf(Tv_gold_ingot_weight)+Double.valueOf(Tv_sample_weight))*Double.valueOf(Tv_gold_karat_weight);
                        Res_net = Double.valueOf(Total_net/875);
                        if (holder.connectivityManager.getActiveNetworkInfo() != null && holder.connectivityManager.getActiveNetworkInfo().isConnected())
                        {
                            dialog.dismiss();
                            if(!TV_price_gram.trim().isEmpty())
                            {
                                WeightEdit(list.get(position).getGold_bar_id(), Tv_gold_bar_owner, Tv_gold_ingot_weight, Tv_sample_weight, Tv_gold_karat_weight, TV_price_gram);
                                holder.sqllite_goldkarat.update(list.get(position).getGold_bar_id(), Tv_gold_bar_owner, Tv_gold_ingot_weight, Tv_sample_weight, Tv_gold_karat_weight, String.valueOf(Res_net), TV_price_gram);
                            }
                            else
                            {
                                WeightEdit(list.get(position).getGold_bar_id(), Tv_gold_bar_owner, Tv_gold_ingot_weight, Tv_sample_weight, Tv_gold_karat_weight, "0");
                                holder.sqllite_goldkarat.update(list.get(position).getGold_bar_id(), Tv_gold_bar_owner, Tv_gold_ingot_weight, Tv_sample_weight, Tv_gold_karat_weight, String.valueOf(Res_net), TV_price_gram);

                            }
                            }
                        else
                        {
                            Utility.showAlertDialog(context.getString(R.string.error), context.getString(R.string.connect_internet), context);
                        }
                    }

                });
                dialog.show();

            }
            public Boolean Valided(String gold_bar_owner,String gold_ingot_weight,String sample_weight,String gold_karat_weight){
                if(gold_bar_owner.isEmpty()){
                    ed_gold_bar_owner.setError(context.getResources().getString(R.string.gold_bar_owner_empty));
                    ed_gold_bar_owner.requestFocus();
                    return false;
                }
                if (gold_ingot_weight.isEmpty())
                {
                    ed_gold_ingot_weight.setError(context.getString(R.string.gold_ingot_weight_empty));
                    ed_gold_ingot_weight.requestFocus();
                    return false;
                }
                if (sample_weight.isEmpty())
                {
                    ed_sample_weight_.setError(context.getString(R.string.gold_sample_weight_empty));
                    ed_sample_weight_.requestFocus();
                    return false;
                }
                if (gold_karat_weight.isEmpty())
                {
                    ed_gold_karat_weight.setError(context.getString(R.string.caliber_empty));
                    ed_gold_karat_weight.requestFocus();
                    return false;
                }
//                if (price_gram.isEmpty())
//                {
//                    ed_price_gram.setError(context.getString(R.string.price_gram_empty));
//                    ed_price_gram.requestFocus();
//                    return false;
//                }
                return true;
            }

            //<-- Send Data TO request And Git Response Status -->
            private void WeightEdit(String id,String gold_bar_owner,String gold_ingot_weight,String sample_weight,String gold_karat_weight,String price_gram){
                ProgressDialog loading = ProgressDialog.show(context,null,context.getString(R.string.wait), false, false);
                loading.setContentView(R.layout.progressbar);
                loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                loading.setCancelable(false);
                loading.setCanceledOnTouchOutside(false);


                // <-- Connect WIth Network And Check Response Successful or Failure -- >
                final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
                Call<UpdateResponse> call= requestInterface.WeightEdit(id,gold_bar_owner,gold_ingot_weight,sample_weight,gold_karat_weight,price_gram,"Bearer "+ LocalSession.getToken());
                call.enqueue(new Callback<UpdateResponse>() {
                    @Override
                    public void onResponse(Call<UpdateResponse> call, Response<UpdateResponse> response)
                    {
                        if(response.isSuccessful())
                        {
                            if(!response.body().isError())
                            {
                                Toast.makeText(context, context.getResources().getString(R.string.wiegth_edited_successfully)+"", Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                                ((Warehouse_Details)context).GetWarehouseDetails(list.get(position).getDate_add_item());

                            }
                            else
                            {
                                loading.dismiss();
                            //    Utility.showAlertDialog(context.getString(R.string.error), response.body().getMessage_ar() + "\n" + response.body().getMessage_en(), context);
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
                          // Log.i(TAG_server, response.errorBody().toString());
                            Utility.showAlertDialog(context.getString(R.string.error), context.getString(R.string.servererror), context);
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateResponse> call, Throwable t) {
                        loading.dismiss();
                        Utility.showAlertDialog(context.getString(R.string.error),context.getString(R.string.connect_internet_slow),context);
                       // Utility.printLog(Tag_failure, t.getMessage());
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
                    View view1 = ((Activity) context).getLayoutInflater().inflate(R.layout.delete_massage, null);
                    TextView hidder = view1.findViewById(R.id.tv_hidder);
                    Button btn_yes = view1.findViewById(R.id.btn_yes);
                    Button btn_no = view1.findViewById(R.id.btn_no);
                    builder.setView(view1);
                    hidder.setText(context.getResources().getString(R.string.ingot_remove)+" "+ list.get(position).getGold_bar_owner());
                    final AlertDialog dialog = builder.create();
                    btn_yes.setOnClickListener(v ->
                    {
                        dialog.dismiss();
                 ProgressDialog loading = ProgressDialog.show(context, null, context.getString(R.string.wait), false, false);
                loading.setContentView(R.layout.progressbar);
                loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                loading.setCancelable(false);
                loading.setCanceledOnTouchOutside(false);

                final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
                Call<RecoveryGoldbarWarehousesResponse> call = requestInterface.Recovey(list.get(position).getId_warehouse(), "Bearer " + LocalSession.getToken());
                call.enqueue(new Callback<RecoveryGoldbarWarehousesResponse>() {
                    @Override
                    public void onResponse(Call<RecoveryGoldbarWarehousesResponse> call, Response<RecoveryGoldbarWarehousesResponse> response) {
                        if (response.isSuccessful())
                        {
                                if (!response.body().isError())
                                {
                                    Toast.makeText(context, context.getResources().getString(R.string.Removed_successfully_warehouse_detail) + "", Toast.LENGTH_SHORT).show();
                                    loading.dismiss();
                                    ((Warehouse_Details) context).GetWarehouseDetails(list.get(position).getDate_add_item());
                                }
                                else
                                 {
                                    loading.dismiss();
                                  //  Utility.showAlertDialog(context.getString(R.string.error), response.body().getMessage_ar() + "\n" + response.body().getMessage_en(), context);
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
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            ((Warehouse_Details)context).finish();
                            Utility.showAlertDialog(context.getString(R.string.error), context.getString(R.string.servererror), context);
                        }
                    }

                    @Override
                    public void onFailure(Call<RecoveryGoldbarWarehousesResponse> call, Throwable t) {
                        loading.dismiss();
                        Utility.showAlertDialog(context.getString(R.string.error),context.getString(R.string.connect_internet_slow), context);
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
            List<WarehouseDetailsModel> warehouseDetailsModels = new ArrayList<>();
            if (key.isEmpty() || key.length() == 0)
            {
                warehouseDetailsModels.addAll(filter);
            }
            else
            {
                for (WarehouseDetailsModel item : list)
                {
                    if (item.getGold_bar_owner().toLowerCase().contains(key)|| item.getGold_bar_owner().toUpperCase().contains(key) || item.getGold_ingot_weight().toLowerCase().contains(key) || item.getGold_karat_weight().toLowerCase().contains(key))
                    {
                        warehouseDetailsModels.add(item);
                    }
                }

            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = warehouseDetailsModels;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list.clear();
            list.addAll((Collection<? extends WarehouseDetailsModel>) filterResults.values);
             notifyDataSetChanged();
        }
    };



    class ViewHolder extends RecyclerView.ViewHolder {
        TextView Tv_gold_bar_owner,Tv_gold_ingot_weight, Tv_sample_weight,Tv_gold_karat_weight,Tv_net,TV_price_gram,TV_Price,Tv_date,Tv_upd,Tv_delete;
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
            Tv_upd = itemView.findViewById(R.id.upddtn);
            Tv_delete = itemView.findViewById(R.id.recovey);
            connectivityManager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
            sqllite_goldkarat = new Sqllite_goldkarat(context);

        }
    }
}





