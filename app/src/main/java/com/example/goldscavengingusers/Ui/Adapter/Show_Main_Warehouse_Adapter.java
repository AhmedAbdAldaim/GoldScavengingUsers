package com.example.goldscavengingusers.Ui.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goldscavengingusers.Local_DB.LocalSession;
import com.example.goldscavengingusers.Local_DB.Sqllite_goldkarat;
import com.example.goldscavengingusers.Model.ShowWarehouseModel;
import com.example.goldscavengingusers.R;
import com.example.goldscavengingusers.Ui.Activity.Warehouse_Details;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class Show_Main_Warehouse_Adapter extends RecyclerView.Adapter<Show_Main_Warehouse_Adapter.ViewHolder> implements Filterable  {

    private Context context;
    private List<ShowWarehouseModel> list;
    private List<ShowWarehouseModel> filter;

    public Show_Main_Warehouse_Adapter(List<ShowWarehouseModel> showWarehouseModels, Context context) {
        this.context = context;
        this.list = showWarehouseModels;
        filter = new ArrayList<>(showWarehouseModels);
    }

    @NonNull
    @Override
    public Show_Main_Warehouse_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.custom_main_warehouse_show, parent, false);
        return new Show_Main_Warehouse_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Show_Main_Warehouse_Adapter.ViewHolder holder, int position) {
        holder.Tv_added_date.setText(list.get(position).getDate_add_item());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Warehouse_Details.class);
                intent.putExtra("Date",list.get(position).getDate_add_item());
                context.startActivity(intent);
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
            List<ShowWarehouseModel> showWarehouseModels = new ArrayList<>();
            if (key.isEmpty() || key.length() == 0)
            {
                showWarehouseModels.addAll(filter);
            }
            else
            {
                for (ShowWarehouseModel item : list)
                {
                    if (item.getDate_add_item().toLowerCase().contains(key) || item.getDate_add_item().toLowerCase().contains(key))
                    {
                        showWarehouseModels.add(item);
                    }
                }

            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = showWarehouseModels;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list.clear();
            list.addAll((Collection<? extends ShowWarehouseModel>) filterResults.values);
             notifyDataSetChanged();
        }
    };



    class ViewHolder extends RecyclerView.ViewHolder {
        TextView Tv_added_date;
        CardView cardView;
        Sqllite_goldkarat sqlliteGoldkarat;
        LocalSession localSession;
        ConnectivityManager connectivityManager;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Tv_added_date = itemView.findViewById(R.id.added_date);
            cardView = itemView.findViewById(R.id.card);
            localSession = new LocalSession(context);
            sqlliteGoldkarat = new Sqllite_goldkarat(context);
            connectivityManager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
        }
    }
}




