package com.sonali.stockwatch.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.sonali.stockwatch.Model.Stock;
import com.sonali.stockwatch.Util.TaskComplete;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class StockDownloader {


    public StockDownloader(TaskComplete complete,String symbol){
        new DataFetch(complete).execute(symbol);
    }

    class DataFetch extends AsyncTask<String, Stock, Stock> {

        private TaskComplete callbackContext;
        private List<Stock> stockList;

        public DataFetch(TaskComplete context){
            callbackContext = context;
        }

        String jdata = "";
        @Override
        protected Stock doInBackground(String... strings) {
            try {
                String symbol = strings[0];
                Log.d("INFO","GETTING DATA FOR " + symbol);
                URL url = new URL("https://api.iextrading.com/1.0/stock/" + symbol + "/quote?displayPercent=true");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    jdata = jdata + line;
                }
                Log.d("INFO",jdata);
                JSONObject jsonObject = new JSONObject(jdata);
                Stock stock = new Stock();
                //Set stock symbol
                stock.setSymbol(jsonObject.getString("symbol").trim());

                //Set stock companyName
                stock.setCompanyName(jsonObject.getString("companyName").trim());

                //Set latest price
                stock.setLatestPrice(jsonObject.getDouble("latestPrice"));

                //Set change
                stock.setChange(jsonObject.getDouble("change"));

                //Set change percentage
                stock.setChangePercent(jsonObject.getDouble("changePercent"));

                return  stock;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Stock stock) {
            callbackContext.onTaskComplete(stock);
            Log.d("INFO", "COMPLETED LOADING " + stock.getSymbol() + " DATA");
        }

    }
}
