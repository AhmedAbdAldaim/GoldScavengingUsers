package com.example.goldscavengingusers.Ui.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import static maes.tech.intentanim.CustomIntent.customType;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;
import com.example.goldscavengingusers.Local_DB.LocalSession;
import com.example.goldscavengingusers.Local_DB.Sqllite_goldkarat;
import com.example.goldscavengingusers.Model.DeleteResponse;
import com.example.goldscavengingusers.Model.GoldBarStatusResponse;
import com.example.goldscavengingusers.Model.ShowGoldbarsModel;
import com.example.goldscavengingusers.Network.ApiClient;
import com.example.goldscavengingusers.Network.RequestInterface;
import com.example.goldscavengingusers.R;
import com.example.goldscavengingusers.Ui.Activity.Edit_Profile;
import com.example.goldscavengingusers.Ui.Activity.Goldbar_Add;
import com.example.goldscavengingusers.Ui.Activity.Login;
import com.example.goldscavengingusers.Ui.Activity.MainActivity;
import com.example.goldscavengingusers.Ui.Activity.Weight_Edit;
import com.example.goldscavengingusers.Utilty.Utility;

import java.text.DecimalFormat;
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

public class Show_Main_Adapter extends RecyclerView.Adapter<Show_Main_Adapter.ViewHolder> implements Filterable  {

    private Context context;
    private List<ShowGoldbarsModel> list;
    private List<ShowGoldbarsModel> filter;

    public Show_Main_Adapter(List<ShowGoldbarsModel> showGoldbarsModels, Context context) {
        this.context = context;
        this.list = showGoldbarsModels;
        filter = new ArrayList<>(showGoldbarsModels);
    }

    @NonNull
    @Override
    public Show_Main_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.custom_main_show, parent, false);
        return new Show_Main_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Show_Main_Adapter.ViewHolder holder, int position) {
        holder.Tv_gold_bar_owner.setText(list.get(position).getGold_bar_owner());
        holder.Tv_gold_ingot_weight.setText(list.get(position).getGold_ingot_weight());
        holder.Tv_sample_weight.setText(list.get(position).getSample_weight());
        holder.Tv_gold_karat_weight.setText(list.get(position).getGold_karat_weight());
        holder.Tv_net.setText(list.get(position).getNet());
        holder.Tv_price_gram.setText(list.get(position).getPrice_gram() + " " + context.getResources().getString(R.string.sudaness_pound));
        holder.Tv_price.setText(list.get(position).getPrice() + " " + context.getResources().getString(R.string.sudaness_pound));
        holder.Tv_date.setText(list.get(position).getDate_add());
        holder.cursor = holder.sqlliteGoldkarat.show();

        while (holder.cursor.moveToNext()) {
            holder.i = String.valueOf(holder.cursor.getInt(holder.cursor.getColumnIndex("id")));
        }

        //ADD BUTTON
        holder.TV_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog loading = ProgressDialog.show(context,null,context.getString(R.string.wait), false, false);
                loading.setContentView(R.layout.progressbar);
                loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                loading.setCancelable(false);
                loading.setCanceledOnTouchOutside(false);

                final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
                Call<GoldBarStatusResponse> call = requestInterface.Goldbarstatus(list.get(position).getId(), "nonactive","Bearer " + LocalSession.getToken());
                call.enqueue(new Callback<GoldBarStatusResponse>() {
                    @Override
                    public void onResponse(Call<GoldBarStatusResponse> call, Response<GoldBarStatusResponse> response)
                    {
                        if (response.isSuccessful())
                        {
                            if(!response.body().isError())
                            {
                                if (holder.connectivityManager.getActiveNetworkInfo() != null && holder.connectivityManager.getActiveNetworkInfo().isConnected()) {
                                    holder.sqlliteGoldkarat.insert(list.get(position).getId(),list.get(position).getId() ,list.get(position).getGold_bar_owner(), list.get(position).getGold_ingot_weight()
                                            , list.get(position).getSample_weight(), list.get(position).getGold_karat_weight(), list.get(position).getNet(),list.get(position).getPrice_gram(),list.get(position).getPrice());
                                    context.startActivity(new Intent(context, MainActivity.class));
                                    customType(context,"fadein-to-fadeout");
                                    ((Activity)context).finish();
                                    loading.dismiss();
                                }
                                else {
                                    Utility.showAlertDialog(context.getString(R.string.error), context.getString(R.string.connect_internet), context);
                                }
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
                    public void onFailure(Call<GoldBarStatusResponse> call, Throwable t) {
                        loading.dismiss();
                        Utility.showAlertDialog(context.getString(R.string.error),context.getString(R.string.connect_internet_slow), context);
                    }
                });

            }
        });


        //EDIT BUTTON
        holder.Tv_upd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Weight_Edit.class);
                intent.putExtra("id",list.get(position).getId());
                intent.putExtra("gold_bar_owner",list.get(position).getGold_bar_owner());
                intent.putExtra("gold_ingot_weight",list.get(position).getGold_ingot_weight());
                intent.putExtra("sample_weight",list.get(position).getSample_weight());
                intent.putExtra("gold_karat_weight",list.get(position).getGold_karat_weight());
                intent.putExtra("gold_net",list.get(position).getNet());
                intent.putExtra("price_gram",list.get(position).getPrice_gram());
                context.startActivity(intent);

            }
        });


        //DELETE BUTTON
        holder.Tv_del.setOnClickListener(new View.OnClickListener() {
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
                hidder.setText(context.getResources().getString(R.string.user_remove_massage)+" "+ list.get(position).getGold_bar_owner());
                final AlertDialog dialog = builder.create();
                btn_yes.setOnClickListener(v ->
                {
                    dialog.dismiss();
                    ProgressDialog loading = ProgressDialog.show(context,null,context.getString(R.string.wait), false, false);
                    loading.setContentView(R.layout.progressbar);
                    loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    loading.setCancelable(false);
                    loading.setCanceledOnTouchOutside(false);
                    final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
                    Call<DeleteResponse> call = requestInterface.GoldbarDelete(list.get(position).getId(), "Bearer " + LocalSession.getToken());
                    call.enqueue(new Callback<DeleteResponse>() {
                        @Override
                        public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response)
                        {
                            if (response.isSuccessful())
                            {
                                if(!response.body().isError()) {
                                    loading.dismiss();
                                    Toast.makeText(context, context.getResources().getString(R.string.Done_remove_massage) + " " + list.get(position).getGold_bar_owner(), Toast.LENGTH_LONG).show();
                                    holder.sqlliteGoldkarat.delete(list.get(position).getId());
                                    list.remove(position);
                                    notifyDataSetChanged();
                                    notifyItemRemoved(position);
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
                                Utility.showAlertDialog(context.getString(R.string.error), context.getString(R.string.servererror), context);
                            }
                        }

                        @Override
                        public void onFailure(Call<DeleteResponse> call, Throwable t) {
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
            List<ShowGoldbarsModel> showGoldbarsModels = new ArrayList<>();
            if (key.isEmpty() || key.length() == 0)
            {
                showGoldbarsModels.addAll(filter);
            }
            else
            {
                for (ShowGoldbarsModel item : list)
                {
                    if (item.getGold_bar_owner().toLowerCase().contains(key)|| item.getGold_bar_owner().toUpperCase().contains(key) || item.getGold_ingot_weight().toLowerCase().contains(key) || item.getGold_karat_weight().toLowerCase().contains(key))
                    {
                        showGoldbarsModels.add(item);
                    }
                }

            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = showGoldbarsModels;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list.clear();
            list.addAll((Collection<? extends ShowGoldbarsModel>) filterResults.values);
             notifyDataSetChanged();
        }
    };



    class ViewHolder extends RecyclerView.ViewHolder {
        TextView Tv_gold_bar_owner,Tv_gold_ingot_weight, Tv_sample_weight,Tv_gold_karat_weight,Tv_net,Tv_price_gram,Tv_price,Tv_date,TV_add,Tv_upd,Tv_del;
        Sqllite_goldkarat sqlliteGoldkarat;
        Cursor cursor;
        String i;
        LocalSession localSession;
        ConnectivityManager connectivityManager;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Tv_gold_bar_owner = itemView.findViewById(R.id.name);
            Tv_gold_ingot_weight = itemView.findViewById(R.id.gold_wieght);
            Tv_sample_weight = itemView.findViewById(R.id.sample_weight);
            Tv_gold_karat_weight = itemView.findViewById(R.id.gold_karat_weight);
            Tv_net = itemView.findViewById(R.id.net_gold);
            Tv_price_gram = itemView.findViewById(R.id.price_gram);
            Tv_price = itemView.findViewById(R.id.price);
            Tv_date = itemView.findViewById(R.id.addeddate);
            TV_add = itemView.findViewById(R.id.addbtn);
            Tv_upd = itemView.findViewById(R.id.upddtn);
            Tv_del = itemView.findViewById(R.id.deltebtn);
            localSession = new LocalSession(context);
            sqlliteGoldkarat = new Sqllite_goldkarat(context);
            connectivityManager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
        }
    }
}




