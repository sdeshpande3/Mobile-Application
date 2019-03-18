package com.sonali.stockwatch;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.sonali.stockwatch.Model.Stock;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class StockDataFetch extends AsyncTask<String, Void, String> {
    private MainActivity mainActivity;
    private static final String DATA_URL = "https://api.iextrading.com ";
    private static final String TAG = "StockDataFetch";
//    public NameDownloader nameDownloader;

    public StockDataFetch(MainActivity ma) {
        mainActivity = ma;
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(mainActivity, "Loading Stock Data...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(String... strings) {
        String searchdata = "";
            Uri dataUri = Uri.parse(DATA_URL);
            String rem = "/quote?displayPercent=true";
            String urlToUse = dataUri.toString();
            urlToUse = urlToUse + mainActivity.m_Text + rem;
            Log.d(TAG, "doInBackground: " + urlToUse);

            StringBuilder sb = new StringBuilder();
            try {
                URL url = new URL(urlToUse);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                Log.d(TAG, "doInBackground: ResponseCode: " + conn.getResponseCode());

                conn.setRequestMethod("GET");

                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }

                Log.d(TAG, "doInBackground:aa " + sb.toString());
                return sb.toString();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        ArrayList<Stock> stockList = parseJSON(s);
        if (stockList != null)
            Toast.makeText(mainActivity, "Loaded " + stockList.size() + " Stocks.", Toast.LENGTH_SHORT).show();
    }

    private ArrayList<Stock> parseJSON(String s) {

        ArrayList<Stock> stockList = new ArrayList<>();
        try {
            JSONArray jObjMain = new JSONArray(s);

            for (int i = 0; i < jObjMain.length(); i++) {
                JSONObject jstock = (JSONObject) jObjMain.get(i);
                String symbol = jstock.getString("symbol");
                String companyName = jstock.getString("companyName");

                String c = jstock.getString("change");
                Double change = null;
                if (c != null && !c.trim().isEmpty() && !c.trim().equals("null"))
                    change = Double.parseDouble(c);

                String l = jstock.getString("latestPrice");
                Double latestPrice = null;
                if (l != null && !l.trim().isEmpty() && !l.trim().equals("null"))
                    latestPrice = Double.parseDouble(l);

                String p = jstock.getString("changePercent");
                Double changePercent = null;
                if (p != null && !p.trim().isEmpty() && !p.trim().equals("null"))
                    changePercent = Double.parseDouble(p);

                stockList.add(
                        new Stock(symbol, companyName, change, latestPrice, changePercent));

            }
            return stockList;
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
