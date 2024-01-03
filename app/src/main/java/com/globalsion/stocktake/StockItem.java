package com.globalsion.stocktake;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StockItem {

    private String stockId;
    private String location;
    private int quantity;
    private Date scanAt;
    private Date createAt;

    public StockItem(String stockId, String location, int quantity, Date scanAt, Date createAt) {
        this.stockId = stockId;
        this.location = location;
        this.quantity = quantity;
        this.scanAt = scanAt;
        this.createAt = createAt;
    }

    public void setStockId(String stockId){
        this.stockId = stockId;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setScanAt(Date scanAt) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            this.scanAt = sdf.parse(String.valueOf(scanAt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setCreateAt(Date createAt) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            this.createAt = sdf.parse(String.valueOf(createAt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getStockId() {
        return stockId;
    }

    public String getLocation() {
        return location;
    }

    public int getQuantity() {
        return quantity;
    }

    public Date getScanAt() {
        return scanAt;
    }

    public Date getCreateAt() {
        return createAt;
    }
}


