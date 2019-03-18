package com.sonali.stockwatch;

public class ListItemnew {
    String symbol;
    String companyName;
    Double change;
    Double latestPrice;
    Double changePercent;

    public ListItemnew(String symbol, String companyName, Double change, Double latestPrice, Double changePercent) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.change = change;
        this.latestPrice = latestPrice;
        this.changePercent = changePercent;
    }


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Double getChange() {
        return change;
    }

    public void setChange(Double change) {
        this.change = change;
    }

    public Double getLatestPrice() {
        return latestPrice;
    }

    public void setLatestPrice(Double latestPrice) {
        this.latestPrice = latestPrice;
    }

    public Double getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(Double changePercent) {
        this.changePercent = changePercent;
    }
}
