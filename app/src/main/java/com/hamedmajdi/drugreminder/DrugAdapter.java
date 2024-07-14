package com.hamedmajdi.drugreminder;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DrugAdapter extends RecyclerView.Adapter<DrugAdapter.ViewHolder> {
    private List<DrugDataClass> drugList;

    public DrugAdapter(List<DrugDataClass> drugs) {
        this.drugList = drugs;
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDrugName;
        TextView tvDrugTime;
        TextView tvDrugRemaining;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDrugName = itemView.findViewById(R.id.tvDrugName);
            tvDrugTime = itemView.findViewById(R.id.tvDrugTime);
            tvDrugRemaining = itemView.findViewById(R.id.tvDrugRemaining);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_view, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DrugDataClass drug = drugList.get(position);
        holder.tvDrugName.setText(drug.drugName);
        holder.tvDrugTime.setText(drug.drugTime);
        holder.tvDrugRemaining.setText(String.valueOf(drug.quantity));

        // Set text color based on remaining quantity
        if (drug.quantity < 3) {
            holder.tvDrugRemaining.setTextColor(Color.RED);
        } else {
            holder.tvDrugRemaining.setTextColor(Color.WHITE);
        }

        // Handle row click (start ActivityDrug)
        holder.itemView.setOnClickListener(v -> {
            // TODO: Start ActivityDrug with drug details
            // You can pass the drug object using Intent extras
        });
    }

    @Override
    public int getItemCount() {
        return drugList.size();
    }
}
