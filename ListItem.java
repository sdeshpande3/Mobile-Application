package com.sonali.stockwatch;

public class ListItem {
    private String stock;
    private String company;
    private String price;
    private String risepercent;
    private String img;
    private String percentchange;

    public ListItem(String stock, String company, String price, String img, String risepercent, String percentchange) {
        this.stock = stock;
        this.company = company;
        this.price = price;
        this.img = img;
        this.risepercent = risepercent;
        this.percentchange = percentchange;
    }

    public String getStock() {
        return stock;
    }

    public String getCompany() {
        return company;
    }

    public String getPrice() {
        return price;
    }


    public String getRisepercent() {
        return risepercent;
    }

    public String getImg() {
        return img;
    }

    public String getPercentchange() {
        return percentchange;
    }
}
