package com.sonali.stockwatch.DatabaseUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sonali.stockwatch.Model.Stock;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "StockAppDB";
    private static final String TABLE_NAME = "StockWatchTable";
    private static final String SYMBOL = "StockSymbol";
    private static final String COMPANY = "CompanyName";
    private final String TAG = this.getClass().getName();

    private SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(  " + SYMBOL + " TEXT not null unique,  " + COMPANY + " TEXT not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertStock(String symbol, String company){
        SQLiteDatabase db = this.getWritableDatabase();
//        String sql1 = "INSERT INTO " + TABLE_NAME + " (" + SYMBOL + ", " + COMPANY
//                + ") values('', 'b');";
        ContentValues cv = new ContentValues();
        cv.put(SYMBOL,symbol);
        cv.put(COMPANY,company);
        db.insert(TABLE_NAME, null, cv);
    }

    public void deleteStock(String symbol) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG, "deleteStock: Deleting Stock " + symbol);
        int cnt = db.delete(TABLE_NAME, SYMBOL + "=?", new String[]{symbol});
        Log.d(TAG, "deleteStock: " + cnt);
    }

    public boolean selectStock(String symbol){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG, "selectStock: Selecting Stock " + symbol);
        String selectQuery = "SELECT * from " + TABLE_NAME + " WHERE " + SYMBOL + "='" + symbol +"'";
        Cursor c = db.rawQuery(selectQuery, null);
        Log.d(TAG, "selectStock: " + c);
        if(c.getCount() > 0)
            return true;
        else
            return false;
    }

    public ArrayList<Stock> loadStocks() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Stock> stocks = new ArrayList<>();
        Cursor cursor = db.query(
                TABLE_NAME, // The table to query
                new String[]{SYMBOL, COMPANY}, // The columns to return
                null, // The columns for the WHERE clause, null means “*”
                null, // The values for the WHERE clause, null means “*”
                null, // don't group the rows
                null, // don't filter by row groups
                null); // The sort order
        if (cursor != null) { // Only proceed if cursor is not null
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String symbol = cursor.getString(0); // 1 st returned column
                String company = cursor.getString(1); // 2 nd returned column
                Stock stock = new Stock(symbol,company);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return stocks;
    }

    public void shutDown() {
        db.close();
    }
}
