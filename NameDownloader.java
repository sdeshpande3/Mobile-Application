package com.sonali.stockwatch.AsyncTask;

import android.os.AsyncTask;
import android.util.Log;

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
import java.util.HashMap;
import java.util.Map;

public class NameDownloader {

    public static HashMap<String, String> stockmap = new HashMap<>();

    public  NameDownloader(){
        new DataFetch().execute();
    }

    class DataFetch extends AsyncTask<Void, Void, Void> {
        String jdata = "";
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("https://api.iextrading.com/1.0/ref-data/symbols ");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    jdata = jdata + line;
                }
                JSONArray JA = new JSONArray(jdata);
                for (int i = 0; i < JA.length(); i++) {
                    JSONObject JO = (JSONObject) JA.get(i);
                    stockmap.put(JO.get("symbol").toString().trim(), JO.get("name").toString().trim());
                    Log.d("INFO" , "ADDED " + JO.get("symbol").toString());
                    //databaseHelper.addStock(stock);
                }
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
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("INFO", "COMPLETED LOADING DATAT IN HASMMASP");
        }

    }

    public HashMap<String,String> getMatchStock(String key){
        HashMap<String,String> stocks = new HashMap<>();
        Log.d("INFO","SEARCHING " + key);
        String keyUp = key.toUpperCase();
        for(Map.Entry<String,String> entry : stockmap.entrySet()){
            String symbol = entry.getKey();
            String name = entry.getValue();
            if(symbol.toUpperCase().contains(keyUp)){
                stocks.put(symbol,stockmap.get(symbol));
            }else{
                if(name.toUpperCase().contains(keyUp)){
                    stocks.put(symbol,stockmap.get(symbol));
                }else{
                    Log.i("INFO","DID NOT MATCH " + keyUp);
                }
            }
        }
        return  stocks;
    }
}
