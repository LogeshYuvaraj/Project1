package com.example.ravelelectronics.model;

public class AllowanceModel {
    private  String sno;
    private  String customername;
    private  String Advtype;
    private  String AllowanceType;
    private  String Fare;

    private  String Currency,Attachment,ApprovedBy,ApprovedStatus;
    String id,docno,AppByCode,AppByName,AppStatus,SaleEmpName,CreatedBy,CreateDate,Customer;

    public String getCustomer() {
        return Customer;
    }

    public void setCustomer(String customer) {
        Customer = customer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocno() {
        return docno;
    }

    public void setDocno(String docno) {
        this.docno = docno;
    }

    public String getAppByCode() {
        return AppByCode;
    }

    public void setAppByCode(String appByCode) {
        AppByCode = appByCode;
    }

    public String getAppByName() {
        return AppByName;
    }

    public void setAppByName(String appByName) {
        AppByName = appByName;
    }

    public String getAppStatus() {
        return AppStatus;
    }

    public void setAppStatus(String appStatus) {
        AppStatus = appStatus;
    }

    public String getSaleEmpName() {
        return SaleEmpName;
    }

    public void setSaleEmpName(String saleEmpName) {
        SaleEmpName = saleEmpName;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getFare() {
        return Fare;
    }

    public void setFare(String fare) {
        Fare = fare;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getAttachment() {
        return Attachment;
    }

    public void setAttachment(String attachment) {
        Attachment = attachment;
    }

    public String getApprovedBy() {
        return ApprovedBy;
    }

    public void setApprovedBy(String approvedBy) {
        ApprovedBy = approvedBy;
    }

    public String getApprovedStatus() {
        return ApprovedStatus;
    }

    public void setApprovedStatus(String approvedStatus) {
        ApprovedStatus = approvedStatus;
    }



    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getAdvtype() {
        return Advtype;
    }

    public void setAdvtype(String advtype) {
        Advtype = advtype;
    }

    public String getAllowanceType() {
        return AllowanceType;
    }

    public void setAllowanceType(String allowanceType) {
        AllowanceType = allowanceType;
    }


    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }
}
