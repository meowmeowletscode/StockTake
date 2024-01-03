package com.globalsion.stocktake;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// Adapter class to bind StockItems to the RecyclerView
public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.StockViewHolder> {

    private List<StockItem> stockItems;

    // Constructor to set the list of stock items
    public StockListAdapter(List<StockItem> stockItems) {
        this.stockItems = stockItems;
    }

    // ViewHolder class to hold the views for each item in the RecyclerView
    public static class StockViewHolder extends RecyclerView.ViewHolder {
        TextView textViewStockId;
        TextView textViewLocation;
        TextView textViewQuantity;
        TextView textViewScan_at;
        TextView textViewCreate_at;

        public StockViewHolder(View itemView) {
            super(itemView);

            textViewStockId = itemView.findViewById(R.id.stockIdTextView);
            textViewLocation = itemView.findViewById(R.id.locationTextView);
            textViewQuantity = itemView.findViewById(R.id.quantityTextView);
            textViewScan_at = itemView.findViewById(R.id.scanAtTextView);
            textViewCreate_at = itemView.findViewById(R.id.createAtTextView);
        }
    }

    public void setStockItems(List<StockItem> stockItems) {
        this.stockItems = stockItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate item layout and create a ViewHolder
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stock, parent, false);
        return new StockViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
        StockItem stockItem = stockItems.get(position);

        if (holder instanceof StockViewHolder) {
            StockViewHolder stockViewHolder = holder;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

            stockViewHolder.textViewStockId.setText(String.valueOf(stockItem.getStockId()));
            stockViewHolder.textViewLocation.setText(String.valueOf(stockItem.getLocation()));
            stockViewHolder.textViewQuantity.setText(String.valueOf(stockItem.getQuantity()));

            Date scanAt = stockItem.getScanAt();
            Date createAt = stockItem.getCreateAt();

            if (scanAt != null) {
                stockViewHolder.textViewScan_at.setText(sdf.format(scanAt));
            } else {
                stockViewHolder.textViewScan_at.setText("N/A");
            }

            if (createAt != null) {
                stockViewHolder.textViewCreate_at.setText(sdf.format(createAt));
            } else {
                stockViewHolder.textViewCreate_at.setText("N/A");
            }
        }
    }

    @Override
    public int getItemCount() {
        return stockItems.size();
    }
}


