package com.example.ravelelectronics.model;

public class ProjectionviewModel {

    String  DocNo,Create_Date,Employee,Category,From_Date,To_Date,Item_code,Item_Name,
            Projection_Given_Qty,Billed_Qty,Difference_Qty;

    public String getProjection_Given_Qty() {
        return Projection_Given_Qty;
    }

    public void setProjection_Given_Qty(String projection_Given_Qty) {
        Projection_Given_Qty = projection_Given_Qty;
    }

    public String getBilled_Qty() {
        return Billed_Qty;
    }

    public void setBilled_Qty(String billed_Qty) {
        Billed_Qty = billed_Qty;
    }

    public String getDifference_Qty() {
        return Difference_Qty;
    }

    public void setDifference_Qty(String difference_Qty) {
        Difference_Qty = difference_Qty;
    }

    public String getDocNo() {
        return DocNo;
    }

    public void setDocNo(String docNo) {
        DocNo = docNo;
    }

    public String getCreate_Date() {
        return Create_Date;
    }

    public void setCreate_Date(String create_Date) {
        Create_Date = create_Date;
    }

    public String getEmployee() {
        return Employee;
    }

    public void setEmployee(String employee) {
        Employee = employee;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getFrom_Date() {
        return From_Date;
    }

    public void setFrom_Date(String from_Date) {
        From_Date = from_Date;
    }

    public String getTo_Date() {
        return To_Date;
    }

    public void setTo_Date(String to_Date) {
        To_Date = to_Date;
    }

    public String getItem_code() {
        return Item_code;
    }

    public void setItem_code(String item_code) {
        Item_code = item_code;
    }

    public String getItem_Name() {
        return Item_Name;
    }

    public void setItem_Name(String item_Name) {
        Item_Name = item_Name;
    }

}
