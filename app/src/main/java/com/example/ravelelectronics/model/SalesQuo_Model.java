package com.example.ravelelectronics.model;

public class SalesQuo_Model {

    private  String itemcode;
    private  String name;
    private  String uom;
    private  String quantity;
    private  String price;
    private  String lastquotprice;
    private  String discount;
    private  String PriceofDis;
    private  String total;
    private  String taxcode;
    private  String WhseCode;
    private  String WhseName;
    private  String hsncode;
    private  String attachment;
    private  String ProjectName;
    private  String Hike;

    public String getHike() {
        return Hike;
    }

    public void setHike(String hike) {
        Hike = hike;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    String RowId,DocNo,Taxprcnt,taxamt,DiscAmt;

    public String getRowId() {
        return RowId;
    }

    public void setRowId(String rowId) {
        RowId = rowId;
    }

    public String getDocNo() {
        return DocNo;
    }

    public void setDocNo(String docNo) {
        DocNo = docNo;
    }

    public String getTaxprcnt() {
        return Taxprcnt;
    }

    public void setTaxprcnt(String taxprcnt) {
        Taxprcnt = taxprcnt;
    }

    public String getTaxamt() {
        return taxamt;
    }

    public void setTaxamt(String taxamt) {
        this.taxamt = taxamt;
    }

    public String getDiscAmt() {
        return DiscAmt;
    }

    public void setDiscAmt(String discAmt) {
        DiscAmt = discAmt;
    }

    public String getTotalDiscountamnt() {
        return TotalDiscountamnt;
    }

    public void setTotalDiscountamnt(String totalDiscountamnt) {
        TotalDiscountamnt = totalDiscountamnt;
    }

    String TotalTaxamnt,TotalDiscountamnt;

    public String getTotalTaxamnt() {
        return TotalTaxamnt;
    }

    public void setTotalTaxamnt(String totalTaxamnt) {
        TotalTaxamnt = totalTaxamnt;
    }

    public String getPriceofDis() {
        return PriceofDis;
    }

    public void setPriceofDis(String priceofDis) {
        PriceofDis = priceofDis;
    }

    public String getWhseCode() {
        return WhseCode;
    }

    public void setWhseCode(String whseCode) {
        WhseCode = whseCode;
    }

    public String getWhseName() {
        return WhseName;
    }

    public void setWhseName(String whseName) {
        WhseName = whseName;
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLastquotprice() {
        return lastquotprice;
    }

    public void setLastquotprice(String lastquotprice) {
        this.lastquotprice = lastquotprice;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTaxcode() {
        return taxcode;
    }

    public void setTaxcode(String taxcode) {
        this.taxcode = taxcode;
    }


    public String getHsncode() {
        return hsncode;
    }

    public void setHsncode(String hsncode) {
        this.hsncode = hsncode;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}
