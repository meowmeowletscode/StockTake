package com.globalsion.stocktake.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.globalsion.stocktake.StockItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "StockTaker";
    public static final String PRODUCT_TABLE_NAME = "stock";
    public static final String COLUMN_ID = "id";
    public static final String STOCK_ID = "stock_id";
    public static final String LOCATION = "location";
    public static final String QUANTITY = "quantity";
    public static final String SCAN_AT = "scan_at";
    public static final String CREATE_AT = "create_at";
    public static final String USER_ID = "user_id";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table if not exists Stock (id integer primary key, stock_id text, location text, quantity integer, scan_at datetime, create_at datetime)"
        );
        Log.d("DBHelper", "Stock table created successfully");

        db.execSQL(
                "create table if not exists User (id integer primary key, user_id text, password text, login_at datetime, create_at datetime)"
        );
        Log.d("DBHelper", "User table created successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS Stock");
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS User");
        onCreate(db);
    }

    //region Asset Table

    //Save Stock
    public boolean insertStock(String stock_id, String location, Integer quantity) {
        try {
            // Check if the stock_id already exists
            if (stockIdExists(stock_id)) {
                return false;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("stock_id", stock_id);
            contentValues.put("location", location);
            contentValues.put("quantity", quantity);
            contentValues.put("create_at", currentDateandTime);
            db.insert("Stock", null, contentValues);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //Check Stock_Id
    private boolean stockIdExists(String stock_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Stock WHERE stock_id = ?", new String[]{stock_id});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    //Add new User
    public boolean addUser(String user_id, String password) {
        try {
            // Check if the user_id already exists
            if (userIdExists(user_id)) {
                return false;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("user_id", user_id);
            contentValues.put("password", password);
            contentValues.put("create_at", currentDateandTime);
            db.insert("User", null, contentValues);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //Check User_Id
    private boolean userIdExists(String user_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE user_id = ?", new String[]{user_id});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    //Retrieve Stock Data
    public Cursor getStockData(String stock_id) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());
            contentValues.put("scan_at", currentDateandTime);

            db.update("Stock", contentValues, "stock_id = ?", new String[]{stock_id});

            return db.rawQuery("SELECT * FROM Stock WHERE stock_id = ? ORDER BY id DESC LIMIT 1", new String[]{stock_id});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Retrieve User Data
    public Cursor getUserData(String user_id) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            return db.rawQuery("SELECT * FROM User WHERE user_id = ? ORDER BY id DESC LIMIT 1", new String[]{user_id});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateUserLogin(String user_id){

        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        contentValues.put("login_at", currentDateandTime);

        db.update("User", contentValues, "user_id = ?", new String[]{user_id});
    }

    //Check User Password
    public boolean validUser(String password) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT password FROM User WHERE password = ?", new String[]{password});

            boolean hasData = cursor != null && cursor.moveToFirst();

            // Close the cursor
            if (cursor != null) {
                cursor.close();
            }

            return hasData;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //Edit Stock data
    public boolean updateStock(String stock_id, String location, Integer quantity) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("stock_id", stock_id);
            contentValues.put("location", location);
            contentValues.put("quantity", quantity);
            contentValues.put("scan_at", currentDateandTime);
            db.update("Stock", contentValues, "stock_id = ? ", new String[]{stock_id});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //Edit User data
    public boolean updateUser(String user_id, String password) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("user_id", user_id);
            contentValues.put("password", password);
            contentValues.put("login_at", currentDateandTime);
            db.update("User", contentValues, "user_id = ? ", new String[]{user_id});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //Delete Stock data
    public boolean deleteStock(String stock_id) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("Stock", "stock_id = ? ", new String[]{stock_id});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //Delete User data
    public boolean deleteUser(String user_id) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("User", "user_id = ? ", new String[]{user_id});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //Delete All Stock data after Export
    public Integer deleteAllStock() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Stock", "1", null);
    }

    //Retrieve All Stock data to export
    public Cursor getAllStockCursor() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from Stock ", null);
        return res;
    }

//    public ArrayList<String> getAllStock() {
//        ArrayList<String> array_list = new ArrayList<String>();
//
//        //hp = new HashMap();
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select * from Stock", null);
//        res.moveToFirst();
//
//        while (res.isAfterLast() == false) {
//            array_list.add(res.getString(res.getColumnIndexOrThrow(STOCK_ID)));
//            res.moveToNext();
//        }
//        return array_list;
//    }

    //Retrieve All Stock data to show in listview
    public ArrayList<StockItem> getAllStock() {
        ArrayList<StockItem> stockItems = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from Stock", null);

        while (res.moveToNext()) {
            String stockId = res.getString(res.getColumnIndexOrThrow(STOCK_ID));
            String location = res.getString(res.getColumnIndexOrThrow(LOCATION));
            int quantity = res.getInt(res.getColumnIndexOrThrow(QUANTITY));
            String scanAtString = res.getString(res.getColumnIndexOrThrow(SCAN_AT));
            String createAtString = res.getString(res.getColumnIndexOrThrow(CREATE_AT));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date scanAt = null;
            Date createAt = null;

            // Check if the date strings are not null before parsing
            if (scanAtString != null) {
                try {
                    scanAt = sdf.parse(scanAtString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            // Handle create_at similarly
            if (createAtString != null) {
                try {
                    createAt = sdf.parse(createAtString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            StockItem stockItem = new StockItem(stockId, location, quantity, scanAt, createAt);
            stockItems.add(stockItem);
        }
        res.close();
        return stockItems;
    }

    //endregion
}
