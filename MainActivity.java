package com.sonali.stockwatch;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.support.v4.widget.SwipeRefreshLayout;

import com.sonali.stockwatch.AsyncTask.NameDownloader;
import com.sonali.stockwatch.Model.Stock;
import com.sonali.stockwatch.Util.Helper;
import com.sonali.stockwatch.Util.TaskComplete;

public class MainActivity extends AppCompatActivity implements TaskComplete {

    private RecyclerView recycle;
    private RecyclerView.Adapter adapter;

    private List<ListItem> listItems;
    private List<Stock> stockList = new ArrayList<>();
    private static final String TAG = "MainActivity";
    String m_Text = "";
    private SwipeRefreshLayout swiper;
    private ConnectivityManager connManager;
    public NameDownloader nameDownloader;
    private boolean connected = false;

    private final static  String STOCK_UPDATE="UPDATE_STOCK";
    private final static  String STOCK_SEARCH="SEARCH_STOCK";

    AlertDialog.Builder builder;
    Helper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycle = (RecyclerView) findViewById(R.id.recycle);
        recycle.setHasFixedSize(true);
        recycle.setLayoutManager(new LinearLayoutManager(this));

        //Helper Class to perform all the functions
        helper = new Helper(getApplicationContext());
        helper.loadStocksFromDB(MainActivity.this);

        nameDownloader = new NameDownloader();
        builder =  new AlertDialog.Builder(MainActivity.this);
        adapter = new MyCustomAdapter(stockList, this);
        recycle.setAdapter(adapter);

        swiper = (SwipeRefreshLayout) findViewById(R.id.swiper);

        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stock, menu);
        return true;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public boolean checkConnection() {

//        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        try {
            connManager = (ConnectivityManager) this
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;


        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (checkConnection()) {
            if (id == R.id.options) {
                builder.setTitle("Stock Selection");
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                builder.setView(input);
                builder.setMessage("Please enter a Stock Symbol:").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //inside this function
                        m_Text = input.getText().toString();
                        Toast.makeText(MainActivity.this, "Working", Toast.LENGTH_LONG).show();
                        HashMap<String, String> result = nameDownloader.getMatchStock(m_Text);
                        if (result.size() != 0) {
                            if(result.size() == 1){
                                addStock(m_Text,result.get(m_Text));
                            }
                            else{

                            }
                            Toast.makeText(MainActivity.this, "Resultant", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "No Resultant", Toast.LENGTH_LONG).show();
                        }
                    }
                }).setNegativeButton("Cancel", null);
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                builder.setTitle("No Network Connection");
                builder.setMessage("Stocks Cannot Be Added Without A Network Connection");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void addStock(String symbol, String companyName) {
        if(checkDuplicate(symbol)){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Stocks symbol "+symbol+" is already displayed");
            builder.setTitle("Duplicate Stock");
            builder.setIcon(R.mipmap.ic_garbage);
            AlertDialog dialog1 = builder.create();
            dialog1.show();
        } else
        {
            helper.addStock(MainActivity.this,symbol,companyName);
            //refreshAdaptor();
        }
    }

    public boolean checkDuplicate(String symbol){
        Stock temp=null;
        for(int i=0;i<stockList.size();i++)
        {
            temp=stockList.get(i);
            if(temp.getSymbol().equals(symbol)) {
                return true;
            }
        }
        return  false;
    }

    public void showNoStockFoundAlert(String symbol){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Data for stock symbol");
        builder.setTitle("Symbol Not Found: "+symbol);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void doRefresh(){
        if(checkConnection()) {
            stockList = helper.refreshStocksData(MainActivity.this,stockList);
            //refreshAdaptor();
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Stocks cannot be refreshed without a network connection");
            builder.setTitle("No Network Connection");
            AlertDialog dialog = builder.create();
            dialog.show();
            swiper.setRefreshing(false);
        }
    }

    public void refreshAdaptor(){
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onTaskComplete(Stock stock) {
        int i=0;
        for(i=0;i<stockList.size();i++){
            if(stock.getSymbol().equals(stockList.get(i).getSymbol())){
                stockList.remove(i);
                stockList.add(stock);
                break;
            }
        }
        if(i==stockList.size())
            stockList.add(stock);
        refreshAdaptor();
    }
}



