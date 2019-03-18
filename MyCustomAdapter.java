package com.sonali.stockwatch;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sonali.stockwatch.Model.Stock;

import org.w3c.dom.Text;

import java.util.List;

public class MyCustomAdapter extends RecyclerView.Adapter<MyCustomAdapter.ViewHolder> {
    private List<Stock> listItems;
    private Context context;
    private MainActivity mainActivity;

    public MyCustomAdapter(List<Stock> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

//    public MyCustomAdapter(List<Stock> stockList,MainActivity mainActivity) {
//        this.stockList=stockList;
//        this.mainActivity=mainActivity;
//    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);

//        itemView.setOnClickListener(mainActivity);
//        itemView.setOnLongClickListener(mainActivity);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Stock listItem = listItems.get(i);
        viewHolder.stock.setText(listItem.getSymbol());
        viewHolder.company.setText(listItem.getCompanyName());
        viewHolder.price.setText(listItem.getLatestPrice().toString());
//        viewHolder.img.setText(listItem.getImg().toString());
        viewHolder.risepercent.setText(listItem.getChange().toString());
        viewHolder.percentchange.setText(listItem.getChangePercent().toString());
        if (listItem.getChange() >= 0) {
            String risepercent = "▲ " + listItem.getChange() + " (" + listItem.getChangePercent() + "%)";
//            viewHolder.risepercent.setText(priceChange);
//            viewHolder.priceChange.setTextColor(Color.GREEN);
            viewHolder.stock.setTextColor(Color.GREEN);
            viewHolder.price.setTextColor(Color.GREEN);
            viewHolder.company.setTextColor(Color.GREEN);
        } else {
            String risepercent="▼ "+listItem.getChange()+" ("+listItem.getChangePercent()+"%)";
//            viewHolder.priceChange.setText(priceChange);
//            viewHolder.priceChange.setTextColor(Color.RED);
            viewHolder.stock.setTextColor(Color.RED);
            viewHolder.price.setTextColor(Color.RED);
            viewHolder.company.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView stock;
        public TextView company;
        public TextView price;
        public TextView risepercent;
        public TextView img;
        public TextView percentchange;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            stock = (TextView)itemView.findViewById(R.id.stock);
            company = (TextView)itemView.findViewById(R.id.company);
            price = (TextView)itemView.findViewById(R.id.price);
            img = (TextView)itemView.findViewById(R.id.img);
            risepercent = (TextView)itemView.findViewById(R.id.risepercent);
            percentchange = (TextView)itemView.findViewById(R.id.percentchange);
        }
    }
}

