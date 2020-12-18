package com.example.ravelelectronics.model;

public class ItemModel {

      String itemcode,itemname,SalUnitMsr,lastquoteprice,HSN,Uom,Price;

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getUom() {
        return Uom;
    }

    public void setUom(String uom) {
        Uom = uom;
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getSalUnitMsr() {
        return SalUnitMsr;
    }

    public void setSalUnitMsr(String salUnitMsr) {
        SalUnitMsr = salUnitMsr;
    }

    public String getLastquoteprice() {
        return lastquoteprice;
    }

    public void setLastquoteprice(String lastquoteprice) {
        this.lastquoteprice = lastquoteprice;
    }

    public String getHSN() {
        return HSN;
    }

    public void setHSN(String HSN) {
        this.HSN = HSN;
    }
}
