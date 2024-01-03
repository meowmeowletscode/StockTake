package com.globalsion.stocktake.ui.stocklist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.globalsion.stocktake.StockItem;

import java.util.List;

public class StockListViewModel extends ViewModel {

    private MutableLiveData<List<StockItem>> stockItemListLiveData = new MutableLiveData<>();

    public LiveData<List<StockItem>> getStockItemListLiveData() {
        return stockItemListLiveData;
    }

    // Update LiveData with new data
    public void updateStockItemList(List<StockItem> stockItems) {
        stockItemListLiveData.setValue(stockItems);
    }
}