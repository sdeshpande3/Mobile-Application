package com.sonali.stockwatch.Util;

import android.content.Context;
import android.util.Log;

import com.sonali.stockwatch.AsyncTask.StockDownloader;
import com.sonali.stockwatch.DatabaseUtil.DatabaseHelper;
import com.sonali.stockwatch.Model.Stock;

import java.util.ArrayList;
import java.util.List;

public class Helper{

    DatabaseHelper databaseHelper;

    public  Helper(Context context){
        databaseHelper = new DatabaseHelper(context);
    }

    public void addStock(TaskComplete complete,String symbol,String companyName){
        if(!databaseHelper.selectStock(symbol))
        databaseHelper.insertStock(symbol,companyName);
        new StockDownloader(complete,symbol);
    }

    public List<Stock> deleteStock(List<Stock> stockList, String symbol){
        databaseHelper.deleteStock(symbol);
        for (int i=0;i<stockList.size();i++){
            if(stockList.get(i).getSymbol().equals(symbol.trim())){
                stockList.remove(i);
                break;
            }
        }
        return  stockList;
    }

    public List<Stock> refreshStocksData(TaskComplete complete,List<Stock> stockList){
        final List<Stock> returnStockList = new ArrayList<>();
        for(Stock stock : stockList){
            new StockDownloader(complete,stock.getSymbol());
        }
        return  returnStockList;
    }

    public void loadStocksFromDB(TaskComplete complete){
        List<Stock> stockList = databaseHelper.loadStocks();
        for(Stock stock : stockList){
            Log.d("INFO", "LOADING DATA FROM DB FOR " + stockList);
            new StockDownloader(complete,stock.getSymbol());
        }
    }

}
