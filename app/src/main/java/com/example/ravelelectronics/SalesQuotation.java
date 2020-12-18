package com.example.ravelelectronics;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ravelelectronics.model.CustomerModel;
import com.example.ravelelectronics.model.ItemModel;
import com.example.ravelelectronics.model.PlaceofsuppltModel;
import com.example.ravelelectronics.model.QuoteStatusModel;
import com.example.ravelelectronics.model.SalesQuo_Model;
import com.example.ravelelectronics.model.SqTypeModel;
import com.example.ravelelectronics.model.TaxcodeModel;
import com.example.ravelelectronics.model.WhseModel;
import com.example.ravelelectronics.spinnerclasses.CustomerList_Spinner;
import com.example.ravelelectronics.spinnerclasses.ItemList_Spinner;
import com.example.ravelelectronics.spinnerclasses.PlaceofSupplyList_Spinner;
import com.example.ravelelectronics.spinnerclasses.QuoteStatusList_Spinner;
import com.example.ravelelectronics.spinnerclasses.SQTypeList_Spinner;
import com.example.ravelelectronics.spinnerclasses.TaxCodeList_Spinner;
import com.example.ravelelectronics.spinnerclasses.WarehouseList_Spinner;
import com.example.ravelelectronics.util.ConnectivityReceiver;
import com.example.ravelelectronics.util.FilePath;
import com.example.ravelelectronics.util.SessionManagement;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;
import org.ksoap2.transport.HttpTransportSE;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SalesQuotation extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    LinearLayout reference_layout;
    EditText referenceno_et;
    EditText taxRate_et;

    TextView edittv,editapprove;
    EditText customer_et,itemname_et,placeofsupply_et,taxcode_et,sqtype_et,quotestatus_et,warehouse_et,projectname_et,hike_et;
    TextView docnum_Tv, total_Tv,canceltv;
    Spinner taxcategory_spinner;
    EditText edtcuscode, edtremarks, Tbefdisc_et,totaldisc_et,itemCode_et,wrhscode_et,docdate_et,validdate_et,grandtotal_et;
    MaterialSpinner edtqutstatus, edtsqtype, placeofsupply_spin, taxcode_spin, customer_spin, itemspin, wrhsspin;
    Button save, cancel, add, clear,saveas,approve,reject,cancel1;
    String itemcode = "", itemname = "", SalUnitMsr = "", lastquoteprice = "0", HSN = "", Uom = "",Price = "";
    String PriceofDis="0",Total="0",TotalBefDisc="0",TaxAmt="0";
    String State="",statecode="";
    EditText Quantity_et, price_et, total_et,totaltaxamnt_et;
    RecyclerView SalesQuotationrecycler;
    RecyclerView.Adapter Adapter;
    ArrayList<SalesQuo_Model> salesQuo_modelArrayList;
    String Column1_Totaltaxamnt="0",Column1_Totaldiscountamnt="0",Column1_hike_price_calc="0",Column1_discount_price_calc="0",Column1_DiscPercentage_Automatic="0";
    String ColUmn1="";
    List<String> SqTypeModel1 = new ArrayList<>();
    ArrayList<SqTypeModel> sqTypeModelArrayList;
    List<String> PlaceofsuppltModel1 = new ArrayList<>();
    ArrayList<PlaceofsuppltModel> placeofsuppltModelArrayList;

    List<String> QuoteStatusModel1 = new ArrayList<>();
    ArrayList<QuoteStatusModel> quoteStatusModelArrayList;

    List<String> TaxcodeModel1 = new ArrayList<>();
    ArrayList<TaxcodeModel> taxcodeModelArrayList;

    String CardCode = "", CardName = "", Column2 = "",Column1="";
    String WhsCode = "", WhsName = "";
    List<String> CustomerModel1 = new ArrayList<>();
    ArrayList<CustomerModel> customerModelArrayList;
    String [] Nodata={"No Data"};
//    String [] WhsCode1={"CS07",""};
    String strfault = "", launch = "";
    SessionManagement session;
    String sessionempid = "", SQType = "", QuoteStatus = "", code = "",sessionempname="";
    double Rate=0;
    ProgressDialog pDialog;
    JSONArray quotationheader,quotationdetails,jsonArray6,jsonArray7;

    List<String> ItemModel1 = new ArrayList<>();
    ArrayList<ItemModel> itemModelArrayList;

    List<String> WhseModel1 = new ArrayList<>();
    ArrayList<WhseModel> whseModelArrayList;
    double finaltotal=0;
    double finaltotaltaxamnt=0;
    double finaldiscount=0;
    double grandtotal=0;
    String placeofsupply="";

    String [] taxcategory={"Inter","Intra"};
    String Taxcategory="";

    DatePickerDialog dp;
    DatePickerDialog dp1;

    String customername="",customercode="";

    String DocNo = "",editstatus="";
    String position1="",Taxposition1,WHSposition1;
    double totalvalue=0;
    double totaltaxamntvalue=0;
    double totaldiscount=0;

    String quotationheader_DocNo,quotationheader_Quotation_Date,quotationheader_ValidDate,
            quotationheader_SQType,quotationheader_CustName,quotationheader_CustCode,
            quotationheader_PlaceSupp,quotationheader_QuoteStatus,quotationheader_Status,
            quotationheader_SapStatus,quotationheader_CreatedBy,quotationheader_modifiedby,
            quotationheader_ModifiedDate,quotationheader_SalesEmpCode,quotationheader_TotalBefDisc,
            quotationheader_TaxAmt,quotationheader_total,quotationheader_GTotal;

    double editedqty=0,editedprice=0,editeddiscount=0,editedtaxprcnt_Rate=0,editedHike=0;
    String edited_itemcode = "";
    String calculate_status = "",calculateposition="";
    Calendar cal,cal1;
    long maxDate;
    Date date;

    String Approvestatus="",ApproveRemarks="";
    String overalldiscamt = "0";
    double finaltotalamt = 0;

    double overalldiscamtdialog = 0, totaltaxamtdialog = 0, basetotalamtdialog = 0;

    String TaxAmt_AfterDiscount = "0";
    String FinalAmt = "0";
    String btnclickstatus = "";
    TextView calculatebtnTV;

    CharSequence[] items;
    Button imageupload_btn,imageupload_btn1;
    ImageView imageView_pic,imageView_pic1;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 401;
    private final int REQUEST_IMAGE=301;
    String attach_Image="",attach_Image1="";
//    private String SERVER_URL = "http://103.48.182.209:85/ravelupload/a.php";
    private String imagepath = "http://103.48.182.209:85/ravelupload/AndroidPdfUpload/";

    String selectedFilePath, attachemntpath = "", attachemntpath1 = "";
    String Imagestatus = "";

    private static final int PICK_FILE_REQUEST = 1;
    private static final String TAG = SalesQuotation.class.getSimpleName();
    private String SERVER_URL = "http://1.22.214.62:85/AndroidPdfUpload/anyfileupload.php";
    ProgressDialog dialog11;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesquotation);

        Intent intent = getIntent ();
        DocNo = intent.getStringExtra ("DocNo");
        editstatus = intent.getStringExtra ("editstatus");
        Log.e ("DocNo",DocNo);
        Log.e ("editstatus",editstatus);

        imageView_pic =  findViewById(R.id.imageView_pic);
        imageView_pic1 =  findViewById(R.id.imageView_pic1);

        imageupload_btn =  findViewById(R.id.imageupload_btn);
        imageupload_btn1 =  findViewById(R.id.imageupload_btn1);
        imageupload_btn.setOnClickListener(this);
        imageupload_btn1.setOnClickListener(this);

        referenceno_et =  findViewById(R.id.referenceno_et);
        reference_layout =  findViewById(R.id.reference_layout);

        calculatebtnTV = findViewById(R.id.calculatebtnTV);
        hike_et = findViewById(R.id.hike_et);
        projectname_et = findViewById(R.id.projectname_et);
        taxcategory_spinner = findViewById(R.id.taxcategory_spinner);
        edittv = findViewById(R.id.edittv);
        editapprove = findViewById(R.id.editapprove);
        approve = findViewById(R.id.approve);
        reject = findViewById(R.id.reject);
        saveas = findViewById(R.id.saveas);
        cancel1 = findViewById(R.id.cancel1);
        customer_et = findViewById(R.id.customer_et);
        itemname_et = findViewById(R.id.itemname_et);
        placeofsupply_et = findViewById(R.id.placeofsupply_et);
        taxcode_et = findViewById(R.id.taxcode_et);
        taxRate_et = findViewById(R.id.taxRate_et);
        sqtype_et = findViewById(R.id.sqtype_et);
        quotestatus_et = findViewById(R.id.quotestatus_et);
        warehouse_et = findViewById(R.id.warehouse_et);
        placeofsupply_spin = findViewById(R.id.placeofsupply_spin);
        itemspin = findViewById(R.id.itemspin);
        wrhsspin = findViewById(R.id.wrhsspin);
        Tbefdisc_et = (EditText) findViewById(R.id.Tbefdisc_et);
        itemCode_et = (EditText) findViewById(R.id.itemCode_et);
        totaltaxamnt_et = (EditText) findViewById(R.id.totaltaxamnt_et);
        wrhscode_et = (EditText) findViewById(R.id.wrhscode_et);
        totaldisc_et = (EditText) findViewById(R.id.totaldisc_et);
        taxcode_spin =  findViewById(R.id.taxcode_spin);
        total_et = findViewById(R.id.total_et);
        canceltv = findViewById(R.id.canceltv);
        Quantity_et = findViewById(R.id.Quantity_et);
        grandtotal_et = findViewById(R.id.grandtotal_et);
        price_et = findViewById(R.id.price_et);
        docdate_et = findViewById(R.id.docdate_et);
        validdate_et = findViewById(R.id.validdate_et);
        customer_spin = findViewById(R.id.customer_spin);
        edtcuscode = findViewById(R.id.edtcuscode);
        edtremarks = (EditText) findViewById(R.id.edtremarks);
        SalesQuotationrecycler = findViewById(R.id.SalesQuotationrecycler);

        edtqutstatus =  findViewById(R.id.edtqutstatus);
        edtsqtype =  findViewById(R.id.edtsqtype);
        save = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);
        add = (Button) findViewById(R.id.add);
        clear = (Button) findViewById(R.id.clear);

        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        canceltv.setOnClickListener(this);
        approve.setOnClickListener(this);
        reject.setOnClickListener(this);
        cancel1.setOnClickListener(this);
        add.setOnClickListener(this);
        clear.setOnClickListener(this);

        customer_et.setOnClickListener (this);
        itemname_et.setOnClickListener (this);
        placeofsupply_et.setOnClickListener (this);
        taxcode_et.setOnClickListener (this);
        sqtype_et.setOnClickListener (this);
        quotestatus_et.setOnClickListener (this);
        wrhscode_et.setOnClickListener (this);
        saveas.setOnClickListener (this);

        wrhscode_et.setText("WH07");
        warehouse_et.setText("FG DOMESTIC");
        quotestatus_et.setText("Live");
        taxcode_et.setText("IGST18");
        taxRate_et.setText("18.0");

        String date = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
        docdate_et.setText(date);
        String date1 = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
//        validdate_et.setText(date1);

        sqTypeModelArrayList = new ArrayList<>();
        quoteStatusModelArrayList = new ArrayList<>();
        taxcodeModelArrayList = new ArrayList<>();
        customerModelArrayList = new ArrayList<>();
        salesQuo_modelArrayList = new ArrayList<>();
        itemModelArrayList = new ArrayList<>();
        whseModelArrayList = new ArrayList<>();
        placeofsuppltModelArrayList = new ArrayList<>();

        session = new SessionManagement(SalesQuotation.this);
        HashMap<String, String> users = session.getUserDetails();
        sessionempid = users.get(SessionManagement.KEY_CODE);
        sessionempname = users.get(SessionManagement.KEY_NAME);

        ArrayAdapter c = new ArrayAdapter(SalesQuotation.this, android.R.layout.simple_spinner_item, taxcategory);
        c.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taxcategory_spinner.setAdapter(c);

        taxcategory_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Taxcategory=taxcategory_spinner.getSelectedItem().toString();
                Log.e("Taxcategory","Taxcategory"+taxcategory_spinner.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        taxcategory_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
//                Taxcategory = item.toString();
//                Log.e("Taxcategory", Taxcategory);
//            }
//        });

        if (editstatus.equalsIgnoreCase ("1")) {
            Log.e ("Coming for", "Edit" + editstatus);
            edittv.setVisibility (View.VISIBLE);
            new GetQuotationHeader ().execute ();
            save.setText ("Update");
            saveas.setVisibility(View.VISIBLE);
            approve.setVisibility (View.GONE);
            reject.setVisibility (View.GONE);
            cancel1.setVisibility (View.GONE);
            add.setVisibility (View.GONE);
            clear.setVisibility (View.GONE);

            reference_layout.setVisibility (View.VISIBLE);
            referenceno_et.setText(DocNo);

        } else if(editstatus.equalsIgnoreCase ("2")) {
            Log.e ("Coming for", "Edit" + editstatus);
            calculatebtnTV.setVisibility (View.GONE);
            edittv.setVisibility (View.GONE);
            add.setVisibility (View.GONE);
            clear.setVisibility (View.GONE);

            reference_layout.setVisibility (View.VISIBLE);
            referenceno_et.setText(DocNo);

            new GetQuotationHeader().execute();

            save.setVisibility (View.GONE);
            saveas.setVisibility (View.GONE);
            cancel.setVisibility (View.GONE);
            docdate_et.setFocusable(false);
            docdate_et.setFocusableInTouchMode(false);
            docdate_et.setEnabled(false);
            customer_et.setFocusable(false);
            customer_et.setFocusableInTouchMode(false);
            customer_et.setEnabled(false);
            placeofsupply_et.setFocusable(false);
            placeofsupply_et.setFocusableInTouchMode(false);
            placeofsupply_et.setEnabled(false);
            taxcode_et.setFocusable(false);
            taxcode_et.setFocusableInTouchMode(false);
            taxcode_et.setEnabled(false);
            sqtype_et.setFocusable(false);
            sqtype_et.setFocusableInTouchMode(false);
            sqtype_et.setEnabled(false);
            quotestatus_et.setFocusable(false);
            quotestatus_et.setFocusableInTouchMode(false);
            quotestatus_et.setEnabled(false);
            itemname_et.setFocusable(false);
            itemname_et.setFocusableInTouchMode(false);
            itemname_et.setEnabled(false);
            wrhscode_et.setFocusable(false);
            wrhscode_et.setFocusableInTouchMode(false);
            wrhscode_et.setEnabled(false);
            Quantity_et.setFocusable(false);
            Quantity_et.setFocusableInTouchMode(false);
            Quantity_et.setEnabled(false);
            Tbefdisc_et.setFocusable(false);
            Tbefdisc_et.setFocusableInTouchMode(false);
            Tbefdisc_et.setEnabled(false);
            edtremarks.setFocusable(false);
            edtremarks.setFocusableInTouchMode(false);
            edtremarks.setEnabled(false);
            total_et.setFocusable(false);
            total_et.setFocusableInTouchMode(false);
            total_et.setEnabled(false);
            price_et.setFocusable(false);
            price_et.setFocusableInTouchMode(false);
            price_et.setEnabled(false);
            hike_et.setFocusable(false);
            hike_et.setFocusableInTouchMode(false);
            hike_et.setEnabled(false);
            validdate_et.setFocusable(false);
            validdate_et.setFocusableInTouchMode(false);
            validdate_et.setEnabled(false);

        } else {
            calculatebtnTV.setVisibility (View.GONE);
            edittv.setVisibility (View.GONE);
            saveas.setVisibility (View.GONE);
            approve.setVisibility (View.GONE);
            reject.setVisibility (View.GONE);
            cancel1.setVisibility (View.GONE);
            Log.e ("Coming from", "DashBoard" + editstatus);
            new GetValidDate().execute();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small> SalesQuotation </small>"));
        }

        docdate_et.setFocusable(false);
        validdate_et.setFocusable(false);

        docdate_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                final int y = c.get(Calendar.YEAR);
                final int m = c.get(Calendar.MONTH);
                final int d = c.get(Calendar.DAY_OF_MONTH);

                dp = new DatePickerDialog(SalesQuotation.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Calendar dateSelected = Calendar.getInstance();
                                dateSelected.set(year, monthOfYear, dayOfMonth);

                                SimpleDateFormat formatterfrm = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);

                                docdate_et.setText(formatterfrm.format(dateSelected.getTime()));
                                Log.d("docdate", docdate_et.getText().toString());

                                new GetValidDate().execute();
                            }
                        }, y, m, d);
                dp.show();
            }
        });

        price_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                new GetDiscPercentage_Automatic (). execute ();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private class GetDiscPercentage_Automatic extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(SalesQuotation.this);
//            pDialog.setCancelable(false);
//            pDialog.setMessage("Loading...");
//            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_GetDiscPercentage_Automatic\n";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_GetDiscPercentage_Automatic\n";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_GetDiscPercentage_Automatic\n";
                Log.e("URL", URL);
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                if (editstatus.equalsIgnoreCase ("0")) {
                    double price=0;
                    price= Double.parseDouble(price_et.getText().toString());

                    Request.addProperty ("Price", "" + price);
                    Log.e ("Price", "editstatus0" + price);
                    Request.addProperty ("ItemCode", "" + itemCode_et.getText().toString().trim());
                    Log.e ("ItemCode", "editstatus0" + itemCode_et.getText().toString().trim());
                } else if (calculate_status.equalsIgnoreCase("1")) {
                    Request.addProperty ("Price", "" + editedprice);
                    Log.e ("Price", "calculate_status" + editedprice);
                    Request.addProperty ("ItemCode", "" + edited_itemcode);
                    Log.e ("ItemCode", "calculate_status" + edited_itemcode);
                } else if (editstatus.equalsIgnoreCase("1")) {
                    Request.addProperty ("Price", "" + price_et.getText().toString().trim());
                    Log.e ("Price", "editstatus1" + price_et.getText().toString().trim());
                    Request.addProperty ("ItemCode", "" + itemCode_et.getText().toString().trim());
                    Log.e ("ItemCode", "editstatus1" + itemCode_et.getText().toString().trim());
                } else {
                    Log.e("calculate_status","calculate_status"+calculate_status);
                    Log.e("editstatus","editstatus"+editstatus);
                }

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                AndroidHttpTransport aht = new AndroidHttpTransport(URL);
                aht.call(SOAP_ACTION, soapEnvelope);
                SoapPrimitive resultString = (SoapPrimitive) soapEnvelope.getResponse();

                String responseJSON = resultString.toString();
                Log.e("RESPONSE", responseJSON);

                jsonArray6 = new JSONArray(responseJSON);
                for (int i = 0; i < jsonArray6.length(); i++) {
                    JSONObject jsonObject = jsonArray6.getJSONObject(i);
                    Column1_DiscPercentage_Automatic=jsonObject.getString("Column1");
                    Log.e ("Column1_DiscPer_Auto", Column1_DiscPercentage_Automatic);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
//            pDialog.dismiss();
            if(jsonArray6.length()==0){
//                Toast.makeText(SalesQuotation.this, "Error", Toast.LENGTH_SHORT).show();
                Log.e("Column1_DiscPer_Auto","Empty");
            }else {
                Tbefdisc_et.setText("" + Column1_DiscPercentage_Automatic);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == PICK_FILE_REQUEST){
                if(data == null){
                    //no data present
                    return;
                }
                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(this,selectedFileUri);
                Log.i(TAG,"Selected File Path:" + selectedFilePath);

                if (selectedFilePath != null && !selectedFilePath.equals("")){
//                    tvFileName.setText(selectedFilePath);
                    //on upload button Click
                    if(selectedFilePath != null){
                        dialog11 = ProgressDialog.show(SalesQuotation.this,"","Uploading File...",true);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //creating new thread to handle Http Operations
                                uploadFile(selectedFilePath);
                            }
                        }).start();
                    }else{
                        Toast.makeText(SalesQuotation.this,"Please choose a File First",Toast.LENGTH_SHORT).show();
                    }
                } else{
                    Toast.makeText(this,"Cannot upload file to server",Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == 1 && data != null) {
            try {
                CardName = data.getStringExtra ("customername");
                CardCode = data.getStringExtra ("customercode");
                customer_et.setText (""+CardName);
                edtcuscode.setText(""+CardCode);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (requestCode == 2 && data != null) {
            try {
                itemname = data.getStringExtra ("itemname");
                itemcode = data.getStringExtra ("itemcode");
                Uom = data.getStringExtra ("uom");
                HSN = data.getStringExtra ("hsn");
                lastquoteprice = data.getStringExtra ("lastquoteprice");
                SalUnitMsr = data.getStringExtra ("salunitmar");
                Price = data.getStringExtra ("Price");

                String edititemadapterstatus = data.getStringExtra ("edititemadapterstatus");
                position1 = data.getStringExtra ("position1");
                if (edititemadapterstatus.equalsIgnoreCase ("edit")) {
                    Adapter.notifyItemChanged(Integer.parseInt (position1));
                } else {
                    itemname_et.setText (""+itemname);
                    itemCode_et.setText(""+itemcode);
                    price_et.setText(""+Price);

//                    if (Price.equalsIgnoreCase("0.0")) {
//                        price_et.setFocusable(true);
//                        price_et.setFocusableInTouchMode(true);
//                        price_et.setEnabled(true);
//                    } else {
//                        price_et.setFocusable(false);
//                        price_et.setFocusableInTouchMode(false);
//                        price_et.setEnabled(false);
//                    }
                    Log.e ("NoValue","edititemadapterstatus"+edititemadapterstatus);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (requestCode == 3 && data != null) {
            try {
                State = data.getStringExtra ("state");
                statecode = data.getStringExtra ("statecode");
                placeofsupply_et.setText (""+State);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (requestCode == 4 && data != null) {
            try {
                code = data.getStringExtra ("taxcode");
                Rate = Double.parseDouble (data.getStringExtra ("taxrate"));
                String edititemadapterstatus = data.getStringExtra ("edititemadapterstatus");
                Taxposition1 = data.getStringExtra ("Taxposition1");
                if (edititemadapterstatus.equalsIgnoreCase ("edit")) {
                    Adapter.notifyItemChanged(Integer.parseInt (Taxposition1));
                } else {
                    taxcode_et.setText (""+code);
                    taxRate_et.setText (""+Rate);
                    Log.e ("NoValue","edititemadapterstatus"+edititemadapterstatus);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (requestCode == 5 && data != null) {
            try {
                SQType = data.getStringExtra ("SQType");
                sqtype_et.setText (""+SQType);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (requestCode == 6 && data != null) {
            try {
                QuoteStatus = data.getStringExtra ("QuoteStatus");
                quotestatus_et.setText (""+QuoteStatus);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (requestCode == 7 && data != null) {
            try {
                WhsName = data.getStringExtra ("WhsName");
                WhsCode = data.getStringExtra ("WhsCode");

                String edititemadapterstatus = data.getStringExtra ("edititemadapterstatus");
                WHSposition1 = data.getStringExtra ("WHSposition1");
                if (edititemadapterstatus.equalsIgnoreCase ("edit")) {
                    Adapter.notifyItemChanged(Integer.parseInt (WHSposition1));
                } else {
                    wrhscode_et.setText (""+WhsCode);
                    warehouse_et.setText(""+WhsName);
                    Log.e ("NoValue","edititemadapterstatus"+edititemadapterstatus);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private boolean checkAndRequestPermissions() {
        int externalStoragePermission = ContextCompat.checkSelfPermission(SalesQuotation.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission=ContextCompat.checkSelfPermission(SalesQuotation.this,Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (externalStoragePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(SalesQuotation.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        //sets the select file to all types of files
        intent.setType("*/*");
        //allows to select data and return it
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //starts new activity to select file and return data
        startActivityForResult(Intent.createChooser(intent,"Choose File to Upload.."),PICK_FILE_REQUEST);
    }

    //android upload file to server
    public int uploadFile(final String selectedFilePath){

        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead,bytesAvailable,bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);


        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length-1];

        if (!selectedFile.isFile()){
            dialog11.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    tvFileName.setText("Source File Doesn't Exist: " + selectedFilePath);
                }
            });
            return 0;
        }else{
            try{
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(SERVER_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file",selectedFilePath);

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + selectedFilePath + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer,0,bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0){
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer,0,bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable,maxBufferSize);
                    bytesRead = fileInputStream.read(buffer,0,bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                final String serverResponseMessage = connection.getResponseMessage();

                Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                //response code of 200 indicates the server status OK
                if(serverResponseCode == 200){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("serverResponseMessage",serverResponseMessage.toString());
                            if (Imagestatus.equalsIgnoreCase("0")) {
                                Log.e("First File  ", "Uploaded" + fileName);
                                attach_Image = "" + fileName;
                            } else {
                                Log.e("Second File  ", "Uploaded" + fileName);
                                attach_Image = "" + fileName;
                            }
//                            tvFileName.setText("File Upload completed.\n\n You can see the uploaded file here: \n\n" + "http://coderefer.com/extras/uploads/"+ fileName);
                        }
                    });
                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SalesQuotation.this,"File Not Found",Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(SalesQuotation.this, "URL error!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(SalesQuotation.this, "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
            }
            dialog11.dismiss();
            return serverResponseCode;
        }
    }

    public void onClick(View v) {
        if (v == imageupload_btn) {
            if (checkAndRequestPermissions()) {
                Imagestatus = "0";
                showFileChooser();
            }
        }
        if (v == imageupload_btn1) {
            if (checkAndRequestPermissions()) {
                Imagestatus = "1";
                showFileChooser();
            }
        }
        if (v == customer_et) {
            Intent intent = new Intent (SalesQuotation.this, CustomerList_Spinner.class);
            intent.putExtra ("ScreenStatus","SQ");
            startActivityForResult(intent, 1);
        }
        if (v == itemname_et) {
            Intent intent = new Intent (SalesQuotation.this, ItemList_Spinner.class);
            intent.putExtra ("ScreenStatus","SQ");
            intent.putExtra ("position1","");
            startActivityForResult(intent, 2);
        }
        if (v == placeofsupply_et) {
            if (customer_et.getText().toString().trim().equalsIgnoreCase ("")) {
                Toast.makeText (this, "Kindly Select Customer", Toast.LENGTH_SHORT).show ( );
            } else {
                Intent intent = new Intent (SalesQuotation.this, PlaceofSupplyList_Spinner.class);
                intent.putExtra ("ScreenStatus", "SQ");
                intent.putExtra ("customercode", edtcuscode.getText().toString().trim());
                startActivityForResult (intent, 3);
            }
        }
        if (v == taxcode_et) {
            Intent intent = new Intent (SalesQuotation.this, TaxCodeList_Spinner.class);
            intent.putExtra ("ScreenStatus","SQ");
            intent.putExtra ("position1", ""+"");
            startActivityForResult(intent, 4);
        }
        if (v == sqtype_et) {
            Intent intent = new Intent (SalesQuotation.this, SQTypeList_Spinner.class);
            intent.putExtra ("ScreenStatus","SQ");
            startActivityForResult(intent, 5);
        }
        if (v == quotestatus_et) {
            Intent intent = new Intent (SalesQuotation.this, QuoteStatusList_Spinner.class);
            intent.putExtra ("ScreenStatus","SQ");
            startActivityForResult(intent, 6);
        }
        if (v == wrhscode_et) {
            Intent intent = new Intent (SalesQuotation.this, WarehouseList_Spinner.class);
            intent.putExtra ("ScreenStatus","SQ");
            intent.putExtra ("position1", ""+"");
            startActivityForResult(intent, 7);
        }
        if (add == v) {
            if(customer_et.getText().toString().trim().equalsIgnoreCase("")){
            Toast.makeText(this, "Kindly Select Any Customer", Toast.LENGTH_SHORT).show();
            } else if (Quantity_et.getText().toString().trim().equalsIgnoreCase("") || Quantity_et.getText().toString().trim().equalsIgnoreCase("0")) {
                Toast.makeText(this, "Kindly enter Qty", Toast.LENGTH_SHORT).show();
            } else if (price_et.getText().toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(this, "Kindly enter Price", Toast.LENGTH_SHORT).show();
//            }  else if (hike_et.getText().toString().trim().equalsIgnoreCase("")) {
//                Toast.makeText(this, "Kindly enter Hike", Toast.LENGTH_SHORT).show();
            } else if (itemname_et.getText().toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(this, "Kindly Select ItemName", Toast.LENGTH_SHORT).show();
            } else if (wrhscode_et.getText().toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(this, "Kindly Select WhsCode", Toast.LENGTH_SHORT).show();
            }else if (taxcode_et.getText().toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(this, "Kindly Select Taxcode", Toast.LENGTH_SHORT).show();
            } else if (sqtype_et.getText().toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(this, "Kindly Select SQType", Toast.LENGTH_SHORT).show();
            } else if (quotestatus_et.getText().toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(this, "Kindly Select QuoteStatus", Toast.LENGTH_SHORT).show();
            } else {
                finaldiscount=0;
                grandtotal=0;

                new GetDiscountAmnt().execute();
            }
            itemname_et.setText("");
            itemCode_et.setText("");
        }
        if (v == save) {
            btnclickstatus = "save";
            if (salesQuo_modelArrayList.size() == 0) {
                Toast.makeText(this, "Atleast Add one Row Go to save", Toast.LENGTH_SHORT).show();
            } else {
                // custom dialog
                final Dialog dialog = new Dialog(SalesQuotation.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                dialog.setContentView(R.layout.add_quotation_final_dialog);
                dialog.setCancelable(false);

                RelativeLayout close_layout = dialog.findViewById(R.id.close_layout);
                TextView header_TV = dialog.findViewById(R.id.header_TV);
                TextView totalamount_TV = dialog.findViewById(R.id.totalamount_TV);
                final TextView taxamount_TV = dialog.findViewById(R.id.taxamount_TV);
                final TextView finaltotalamount_TV1 = dialog.findViewById(R.id.finaltotalamount_TV1);
                final TextView finaltotalamount_TV = dialog.findViewById(R.id.finaltotalamount_TV);

                final EditText dialog_overall_disc_amt_et = dialog.findViewById(R.id.dialog_overall_disc_amt_et);
                Button dialogbtn_calculate = dialog.findViewById(R.id.dialogbtn_calculate);
                Button dialogbtn_savequaotation = dialog.findViewById(R.id.dialogbtn_savequaotation);

                header_TV.setText("Quotation Details");
                totalamount_TV.setText(total_et.getText().toString().trim());
                taxamount_TV.setText(totaltaxamnt_et.getText().toString().trim());
                finaltotalamount_TV1.setVisibility(View.GONE);
                finaltotalamount_TV.setVisibility(View.GONE);

                dialog_overall_disc_amt_et.setText("0");
                close_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialogbtn_calculate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog_overall_disc_amt_et.getText().toString().trim().equalsIgnoreCase("") || dialog_overall_disc_amt_et.getText().toString().trim().equalsIgnoreCase("0")) {
                            Toast.makeText(SalesQuotation.this, "Kindly Enter Overall Discount Amount", Toast.LENGTH_SHORT).show();
                        } else {
//                            overalldiscamt=dialog_overall_disc_amt_et.getText().toString().trim();
//                            new OverallDicsCalculation().execute();

                            totaltaxamtdialog = Double.parseDouble(totaltaxamnt_et.getText().toString().trim());
                            basetotalamtdialog = Double.parseDouble(total_et.getText().toString().trim());
                            overalldiscamtdialog = Double.parseDouble(dialog_overall_disc_amt_et.getText().toString().trim());

//                            finaltotalamt = grandtotalamt - overalldiscamtdialog;
//                            finaltotalamount_TV1.setVisibility(View.VISIBLE);
//                            finaltotalamount_TV.setVisibility(View.VISIBLE);
//                            finaltotalamount_TV.setText(""+finaltotalamt);
//                            grandtotal_et.setText(""+finaltotalamt);

                            new GetTaxAmt_AfterDiscount().execute();

                            taxamount_TV.setText("" + TaxAmt_AfterDiscount);
                            taxamount_TV.setTextColor(getResources().getColor(R.color.red));
                            finaltotalamount_TV1.setVisibility(View.VISIBLE);
                            finaltotalamount_TV.setVisibility(View.VISIBLE);
                            finaltotalamount_TV.setText(""+FinalAmt);
                            dialog.dismiss();
                        }
                    }
                });
                dialogbtn_savequaotation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog_overall_disc_amt_et.getText().toString().trim().equalsIgnoreCase("0.0") || dialog_overall_disc_amt_et.getText().toString().trim().equalsIgnoreCase("0") || dialog_overall_disc_amt_et.getText().toString().trim().equalsIgnoreCase("")) {
                            overalldiscamt = dialog_overall_disc_amt_et.getText().toString().trim();
                            new AddQuotation().execute();
                            dialog.dismiss();
                        } else {
                            if (FinalAmt.equalsIgnoreCase("0")) {
                                Log.e("finaltotalamt",""+finaltotalamt);
                                Toast.makeText(SalesQuotation.this, "Kindly Click on Calculate", Toast.LENGTH_SHORT).show();
                            } else {
                                overalldiscamt = dialog_overall_disc_amt_et.getText().toString().trim();
                                FinalAmt = finaltotalamount_TV.getText().toString().trim();
                                TaxAmt_AfterDiscount = taxamount_TV.getText().toString().trim();
                                new AddQuotation().execute();
                                dialog.dismiss();
                            }
                        }
                    }
                });
                dialog.show();
            }
        }
        if (v == saveas) {
            btnclickstatus = "saveas";
//            DocNo="";
//            new AddQuotation().execute();
            // custom dialog
            final Dialog dialog = new Dialog(SalesQuotation.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
            dialog.setContentView(R.layout.add_saveas_quotation_final_dialog);
            dialog.setCancelable(false);

            RelativeLayout close_layout = dialog.findViewById(R.id.close_layout);
            TextView header_TV = dialog.findViewById(R.id.header_TV);
            TextView totalamount_TV = dialog.findViewById(R.id.totalamount_TV);
            final TextView taxamount_TV = dialog.findViewById(R.id.taxamount_TV);
            final TextView finaltotalamount_TV1 = dialog.findViewById(R.id.finaltotalamount_TV1);
            final TextView finaltotalamount_TV = dialog.findViewById(R.id.finaltotalamount_TV);

            final EditText dialog_overall_disc_amt_et = dialog.findViewById(R.id.dialog_overall_disc_amt_et);
            Button dialogbtn_calculate = dialog.findViewById(R.id.dialogbtn_calculate);
            Button dialogbtn_savequaotation = dialog.findViewById(R.id.dialogbtn_savequaotation);

            header_TV.setText("Quotation Details");
            totalamount_TV.setText(total_et.getText().toString().trim());
            taxamount_TV.setText(totaltaxamnt_et.getText().toString().trim());
            finaltotalamount_TV1.setVisibility(View.GONE);
            finaltotalamount_TV.setVisibility(View.GONE);

            dialog_overall_disc_amt_et.setText("0");
            close_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialogbtn_calculate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog_overall_disc_amt_et.getText().toString().trim().equalsIgnoreCase("") || dialog_overall_disc_amt_et.getText().toString().trim().equalsIgnoreCase("0")) {
                        Toast.makeText(SalesQuotation.this, "Kindly Enter Overall Discount Amount", Toast.LENGTH_SHORT).show();
                    } else {
//                            overalldiscamt=dialog_overall_disc_amt_et.getText().toString().trim();
//                            new OverallDicsCalculation().execute();

                        totaltaxamtdialog = Double.parseDouble(totaltaxamnt_et.getText().toString().trim());
                        basetotalamtdialog = Double.parseDouble(total_et.getText().toString().trim());
                        overalldiscamtdialog = Double.parseDouble(dialog_overall_disc_amt_et.getText().toString().trim());

//                            finaltotalamt = grandtotalamt - overalldiscamtdialog;
//                            finaltotalamount_TV1.setVisibility(View.VISIBLE);
//                            finaltotalamount_TV.setVisibility(View.VISIBLE);
//                            finaltotalamount_TV.setText(""+finaltotalamt);
//                            grandtotal_et.setText(""+finaltotalamt);

                        new GetTaxAmt_AfterDiscount().execute();

                        taxamount_TV.setText("" + TaxAmt_AfterDiscount);
                        taxamount_TV.setTextColor(getResources().getColor(R.color.red));
                        finaltotalamount_TV1.setVisibility(View.VISIBLE);
                        finaltotalamount_TV.setVisibility(View.VISIBLE);
                        finaltotalamount_TV.setText(""+FinalAmt);
                        dialog.dismiss();
                    }
                }
            });
            dialogbtn_savequaotation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog_overall_disc_amt_et.getText().toString().trim().equalsIgnoreCase("0.0") || dialog_overall_disc_amt_et.getText().toString().trim().equalsIgnoreCase("0") || dialog_overall_disc_amt_et.getText().toString().trim().equalsIgnoreCase("")) {
                        overalldiscamt = dialog_overall_disc_amt_et.getText().toString().trim();
                        new AddQuotation().execute();
                        dialog.dismiss();
                    } else {
                        if (FinalAmt.equalsIgnoreCase("0")) {
                            Log.e("finaltotalamt",""+finaltotalamt);
                            Toast.makeText(SalesQuotation.this, "Kindly Click on Calculate", Toast.LENGTH_SHORT).show();
                        } else {
                            overalldiscamt = dialog_overall_disc_amt_et.getText().toString().trim();
                            FinalAmt = finaltotalamount_TV.getText().toString().trim();
                            TaxAmt_AfterDiscount = taxamount_TV.getText().toString().trim();
                            new AddQuotation().execute();
                            dialog.dismiss();
                        }
                    }
                }
            });
            dialog.show();
        }
        if (v == clear) {
            salesQuo_modelArrayList.clear();
            Adapter.notifyDataSetChanged();
            onRestart();
            total_et.setText("");
            totaltaxamnt_et.setText("");
            totaldisc_et.setText("");
            grandtotal_et.setText("");
        }
        if (v==cancel){
            finish();
        }
        if (v == approve) {
            Approvestatus="A";
            new AddQuotation_Approval().execute();
        }
        if (v == reject) {
            // custom dialog
            final Dialog dialog = new Dialog(SalesQuotation.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
            dialog.setContentView(R.layout.edit_approve_type_dialog);

            RelativeLayout close_layout = dialog.findViewById(R.id.close_layout);
            TextView header_TV = dialog.findViewById(R.id.header_TV);
            final EditText dialogremarks_et = dialog.findViewById(R.id.dialogremarks_et);
            Button dialogbtn_ok = dialog.findViewById(R.id.dialogbtn_ok);

            header_TV.setText("Do you want to Reject?");

            close_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialogbtn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialogremarks_et.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(SalesQuotation.this, "Kindly give reason for Rejecting", Toast.LENGTH_SHORT).show();
                    } else {
                        ApproveRemarks=dialogremarks_et.getText().toString().trim();
                        Approvestatus="R";
                        new AddQuotation_Approval().execute();
                        dialog.dismiss();
                    }
                }
            });
            dialog.show();
        }
    }

    private class GetTaxAmt_AfterDiscount extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(SalesQuotation.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_GetTaxAmt_AfterDiscount";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_GetTaxAmt_AfterDiscount";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_GetTaxAmt_AfterDiscount";
                Log.e("URL", URL);

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty ("Total", "" +basetotalamtdialog);
                Log.e ("Total", ""+basetotalamtdialog);

                Request.addProperty ("TotalDiscount", "" +overalldiscamtdialog);
                Log.e ("TotalDiscount", ""+overalldiscamtdialog);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                AndroidHttpTransport aht = new AndroidHttpTransport(URL);
                aht.call(SOAP_ACTION, soapEnvelope);
                SoapPrimitive resultString = (SoapPrimitive) soapEnvelope.getResponse();

                String responseJSON = resultString.toString();
                Log.e("RESPONSE", responseJSON);

                jsonArray7 = new JSONArray(responseJSON);
                for (int i = 0; i < jsonArray7.length(); i++) {
                    JSONObject jsonObject = jsonArray7.getJSONObject(i);
                    TaxAmt_AfterDiscount=jsonObject.getString("Column1");
                    Log.e("TaxAmt_AfterDiscount",TaxAmt_AfterDiscount);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (jsonArray7.length() == 0) {
                Toast.makeText(SalesQuotation.this, "No Value Found", Toast.LENGTH_SHORT).show();
            } else {
                new GetFinalAmt().execute();
            }
        }
    }

    private class GetFinalAmt extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(SalesQuotation.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_GetFinalAmt";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_GetFinalAmt";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_GetFinalAmt";
                Log.e("URL", URL);

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty ("Total", "" +basetotalamtdialog);
                Log.e ("Total", ""+basetotalamtdialog);

                Request.addProperty ("TotalDiscount", "" +overalldiscamtdialog);
                Log.e ("TotalDiscount", ""+overalldiscamtdialog);

                double TaxAmt_AfterDiscount1 = Double.parseDouble(TaxAmt_AfterDiscount);

                Request.addProperty ("TaxAmt", "" +TaxAmt_AfterDiscount1);
                Log.e ("TaxAmt", ""+TaxAmt_AfterDiscount1);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                AndroidHttpTransport aht = new AndroidHttpTransport(URL);
                aht.call(SOAP_ACTION, soapEnvelope);
                SoapPrimitive resultString = (SoapPrimitive) soapEnvelope.getResponse();

                String responseJSON = resultString.toString();
                Log.e("RESPONSE", responseJSON);

                jsonArray7 = new JSONArray(responseJSON);
                for (int i = 0; i < jsonArray7.length(); i++) {
                    JSONObject jsonObject = jsonArray7.getJSONObject(i);
                    FinalAmt=jsonObject.getString("Column1");
                    Log.e("FinalAmt",FinalAmt);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (jsonArray7.length() == 0) {
                Toast.makeText(SalesQuotation.this, "No List", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("Success","Final Amount" + FinalAmt);

                if (btnclickstatus.equalsIgnoreCase("save")) {
                    // custom dialog
                    final Dialog dialog = new Dialog(SalesQuotation.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                    dialog.setContentView(R.layout.add_quotation_final_dialog);
                    dialog.setCancelable(false);

                    RelativeLayout close_layout = dialog.findViewById(R.id.close_layout);
                    TextView header_TV = dialog.findViewById(R.id.header_TV);
                    TextView totalamount_TV = dialog.findViewById(R.id.totalamount_TV);
                    final TextView taxamount_TV = dialog.findViewById(R.id.taxamount_TV);
                    final TextView finaltotalamount_TV1 = dialog.findViewById(R.id.finaltotalamount_TV1);
                    final TextView finaltotalamount_TV = dialog.findViewById(R.id.finaltotalamount_TV);

                    final EditText dialog_overall_disc_amt_et = dialog.findViewById(R.id.dialog_overall_disc_amt_et);
                    Button dialogbtn_calculate = dialog.findViewById(R.id.dialogbtn_calculate);
                    Button dialogbtn_savequaotation = dialog.findViewById(R.id.dialogbtn_savequaotation);

                    header_TV.setText("Quotation Details");
                    totalamount_TV.setText(total_et.getText().toString().trim());
                    taxamount_TV.setText(totaltaxamnt_et.getText().toString().trim());
                    finaltotalamount_TV1.setVisibility(View.GONE);
                    finaltotalamount_TV.setVisibility(View.GONE);

                    dialog_overall_disc_amt_et.setText("0");

                    if (TaxAmt_AfterDiscount.equalsIgnoreCase("0")) {
                        Log.e("Save", "Dialog");
                    } else {
                        dialog_overall_disc_amt_et.setText("" + overalldiscamtdialog);
                        taxamount_TV.setText("" + TaxAmt_AfterDiscount);
                        taxamount_TV.setTextColor(getResources().getColor(R.color.red));
                        finaltotalamount_TV1.setVisibility(View.VISIBLE);
                        finaltotalamount_TV.setVisibility(View.VISIBLE);
                        finaltotalamount_TV.setText("" + FinalAmt);
                    }
                    close_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialogbtn_calculate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (dialog_overall_disc_amt_et.getText().toString().trim().equalsIgnoreCase("") || dialog_overall_disc_amt_et.getText().toString().trim().equalsIgnoreCase("0")) {
                                Toast.makeText(SalesQuotation.this, "Kindly Enter Overall Discount Amount", Toast.LENGTH_SHORT).show();
                            } else {
//                            overalldiscamt=dialog_overall_disc_amt_et.getText().toString().trim();
//                            new OverallDicsCalculation().execute();

                                totaltaxamtdialog = Double.parseDouble(totaltaxamnt_et.getText().toString().trim());
                                basetotalamtdialog = Double.parseDouble(total_et.getText().toString().trim());
                                overalldiscamtdialog = Double.parseDouble(dialog_overall_disc_amt_et.getText().toString().trim());

//                            finaltotalamt = grandtotalamt - overalldiscamtdialog;
//                            finaltotalamount_TV1.setVisibility(View.VISIBLE);
//                            finaltotalamount_TV.setVisibility(View.VISIBLE);
//                            finaltotalamount_TV.setText(""+finaltotalamt);
//                            grandtotal_et.setText(""+finaltotalamt);

                                new GetTaxAmt_AfterDiscount().execute();

                                dialog.dismiss();
                            }
                        }
                    });
                    dialogbtn_savequaotation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (dialog_overall_disc_amt_et.getText().toString().trim().equalsIgnoreCase("0.0") || dialog_overall_disc_amt_et.getText().toString().trim().equalsIgnoreCase("0") || dialog_overall_disc_amt_et.getText().toString().trim().equalsIgnoreCase("")) {
                                overalldiscamt = dialog_overall_disc_amt_et.getText().toString().trim();
                                new AddQuotation().execute();
                                dialog.dismiss();
                            } else {
                                if (FinalAmt.equalsIgnoreCase("0")) {
                                    Log.e("finaltotalamt", "" + finaltotalamt);
                                    Toast.makeText(SalesQuotation.this, "Kindly Click on Calculate", Toast.LENGTH_SHORT).show();
                                } else {
                                    overalldiscamt = dialog_overall_disc_amt_et.getText().toString().trim();
                                    FinalAmt = finaltotalamount_TV.getText().toString().trim();
                                    TaxAmt_AfterDiscount = taxamount_TV.getText().toString().trim();
                                    new AddQuotation().execute();
                                    dialog.dismiss();
                                }
                            }
                        }
                    });
                    dialog.show();
                } else {
                    // custom dialog
                    final Dialog dialog = new Dialog(SalesQuotation.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                    dialog.setContentView(R.layout.add_saveas_quotation_final_dialog);
                    dialog.setCancelable(false);

                    RelativeLayout close_layout = dialog.findViewById(R.id.close_layout);
                    TextView header_TV = dialog.findViewById(R.id.header_TV);
                    TextView totalamount_TV = dialog.findViewById(R.id.totalamount_TV);
                    final TextView taxamount_TV = dialog.findViewById(R.id.taxamount_TV);
                    final TextView finaltotalamount_TV1 = dialog.findViewById(R.id.finaltotalamount_TV1);
                    final TextView finaltotalamount_TV = dialog.findViewById(R.id.finaltotalamount_TV);

                    final EditText dialog_overall_disc_amt_et = dialog.findViewById(R.id.dialog_overall_disc_amt_et);
                    Button dialogbtn_calculate = dialog.findViewById(R.id.dialogbtn_calculate);
                    Button dialogbtn_savequaotation = dialog.findViewById(R.id.dialogbtn_savequaotation);

                    header_TV.setText("Quotation Details");
                    totalamount_TV.setText(total_et.getText().toString().trim());
                    taxamount_TV.setText(totaltaxamnt_et.getText().toString().trim());
                    finaltotalamount_TV1.setVisibility(View.GONE);
                    finaltotalamount_TV.setVisibility(View.GONE);

                    dialog_overall_disc_amt_et.setText("0");

                    if (TaxAmt_AfterDiscount.equalsIgnoreCase("0")) {
                        Log.e("Save", "Dialog");
                    } else {
                        dialog_overall_disc_amt_et.setText("" + overalldiscamtdialog);
                        taxamount_TV.setText("" + TaxAmt_AfterDiscount);
                        taxamount_TV.setTextColor(getResources().getColor(R.color.red));
                        finaltotalamount_TV1.setVisibility(View.VISIBLE);
                        finaltotalamount_TV.setVisibility(View.VISIBLE);
                        finaltotalamount_TV.setText("" + FinalAmt);
                    }
                    close_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialogbtn_calculate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (dialog_overall_disc_amt_et.getText().toString().trim().equalsIgnoreCase("") || dialog_overall_disc_amt_et.getText().toString().trim().equalsIgnoreCase("0")) {
                                Toast.makeText(SalesQuotation.this, "Kindly Enter Overall Discount Amount", Toast.LENGTH_SHORT).show();
                            } else {
//                            overalldiscamt=dialog_overall_disc_amt_et.getText().toString().trim();
//                            new OverallDicsCalculation().execute();

                                totaltaxamtdialog = Double.parseDouble(totaltaxamnt_et.getText().toString().trim());
                                basetotalamtdialog = Double.parseDouble(total_et.getText().toString().trim());
                                overalldiscamtdialog = Double.parseDouble(dialog_overall_disc_amt_et.getText().toString().trim());

//                            finaltotalamt = grandtotalamt - overalldiscamtdialog;
//                            finaltotalamount_TV1.setVisibility(View.VISIBLE);
//                            finaltotalamount_TV.setVisibility(View.VISIBLE);
//                            finaltotalamount_TV.setText(""+finaltotalamt);
//                            grandtotal_et.setText(""+finaltotalamt);

                                new GetTaxAmt_AfterDiscount().execute();

                                dialog.dismiss();
                            }
                        }
                    });
                    dialogbtn_savequaotation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (dialog_overall_disc_amt_et.getText().toString().trim().equalsIgnoreCase("0.0") || dialog_overall_disc_amt_et.getText().toString().trim().equalsIgnoreCase("0") || dialog_overall_disc_amt_et.getText().toString().trim().equalsIgnoreCase("")) {
                                overalldiscamt = dialog_overall_disc_amt_et.getText().toString().trim();
                                new AddQuotation().execute();
                                dialog.dismiss();
                            } else {
                                if (FinalAmt.equalsIgnoreCase("0")) {
                                    Log.e("finaltotalamt", "" + finaltotalamt);
                                    Toast.makeText(SalesQuotation.this, "Kindly Click on Calculate", Toast.LENGTH_SHORT).show();
                                } else {
                                    overalldiscamt = dialog_overall_disc_amt_et.getText().toString().trim();
                                    FinalAmt = finaltotalamount_TV.getText().toString().trim();
                                    TaxAmt_AfterDiscount = taxamount_TV.getText().toString().trim();
                                    new AddQuotation().execute();
                                    dialog.dismiss();
                                }
                            }
                        }
                    });
                    dialog.show();
                }
            }
        }
    }

    private class GetValidDate extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(SalesQuotation.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_GetValidDate";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_GetValidDate";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_GetValidDate";
                Log.e("URL", URL);

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty ("DocDate", "" +docdate_et.getText().toString().trim());
                Log.e ("DocDate", ""+docdate_et.getText().toString().trim());

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                AndroidHttpTransport aht = new AndroidHttpTransport(URL);
                aht.call(SOAP_ACTION, soapEnvelope);
                SoapPrimitive resultString = (SoapPrimitive) soapEnvelope.getResponse();

                String responseJSON = resultString.toString();
                Log.e("RESPONSE", responseJSON);

                jsonArray7 = new JSONArray(responseJSON);
                for (int i = 0; i < jsonArray7.length(); i++) {
                    JSONObject jsonObject = jsonArray7.getJSONObject(i);
                    ColUmn1=jsonObject.getString("Column1");
                    Log.e("ColUmn1",ColUmn1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (jsonArray7.length() == 0) {
                Toast.makeText(SalesQuotation.this, "No List", Toast.LENGTH_SHORT).show();
            } else {
                validdate_et.setText(ColUmn1);

            }
        }
    }

    private class GetQuotationHeader extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SalesQuotation.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_QuotationHeader";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_QuotationHeader";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_QuotationHeader";
                Log.e("URL", URL);
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("DocNo", "" +DocNo );
                Log.e("DocNo", "" +DocNo );

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                AndroidHttpTransport aht = new AndroidHttpTransport(URL);
                aht.call(SOAP_ACTION, soapEnvelope);
                SoapPrimitive resultString = (SoapPrimitive) soapEnvelope.getResponse();

                String responseJSON = resultString.toString();
                Log.e("RESPONSE", responseJSON);

                quotationheader = new JSONArray(responseJSON);
                for (int i = 0; i < quotationheader.length(); i++) {
                    JSONObject jsonObject = quotationheader.getJSONObject(i);

                    quotationheader_DocNo=jsonObject.getString("DocNo");
                    quotationheader_Quotation_Date=jsonObject.getString("Quotation Date");
                    quotationheader_ValidDate=jsonObject.getString("ValidDate");
                    quotationheader_SQType=jsonObject.getString("SQType");
                    quotationheader_CustName=jsonObject.getString("CustName");
                    quotationheader_CustCode=jsonObject.getString("CustCode");
                    quotationheader_PlaceSupp=jsonObject.getString("PlaceSupp");
                    quotationheader_QuoteStatus=jsonObject.getString("QuoteStatus");
                    quotationheader_Status=jsonObject.getString("Status");
                    quotationheader_SapStatus=jsonObject.getString("SapStatus");
                    quotationheader_CreatedBy=jsonObject.getString("CreatedBy");
                    quotationheader_modifiedby=jsonObject.getString("modifiedby");
                    quotationheader_ModifiedDate=jsonObject.getString("ModifiedDate");
                    quotationheader_SalesEmpCode=jsonObject.getString("SalesEmpCode");
                    quotationheader_TotalBefDisc=jsonObject.getString("TotalBefDisc");
                    quotationheader_TaxAmt=jsonObject.getString("TaxAmt");
                    quotationheader_total=jsonObject.getString("total");
                    quotationheader_GTotal=jsonObject.getString("GTotal");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if(quotationheader.length()==0){
                Toast.makeText(SalesQuotation.this, "No Data Found", Toast.LENGTH_SHORT).show();
            }else {
                docdate_et.setText (quotationheader_Quotation_Date);
                validdate_et.setText (quotationheader_ValidDate);
                sqtype_et.setText (quotationheader_SQType);
                customer_et.setText (quotationheader_CustName);
                edtcuscode.setText (quotationheader_CustCode);
                placeofsupply_et.setText (quotationheader_PlaceSupp);
                quotestatus_et.setText (quotationheader_QuoteStatus);
                total_et.setText(quotationheader_total);
                totaldisc_et.setText (quotationheader_TotalBefDisc);
                totaltaxamnt_et.setText (quotationheader_TaxAmt);
                grandtotal_et.setText (quotationheader_GTotal);
                Log.e ("quotationheader_total",""+quotationheader_total);
                Log.e ("quotationheader_TaxAmt",""+quotationheader_TaxAmt);
                Log.e ("quoheader_TotalBefDisc",""+quotationheader_TotalBefDisc);


                if (ConnectivityReceiver.isConnected(SalesQuotation.this)) {
                    new GetQuotationHeaderDetails().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Check Your Internet Connection!!!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class GetQuotationHeaderDetails extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SalesQuotation.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_GETQuotationDetails";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_GETQuotationDetails";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_GETQuotationDetails";
                Log.e("URL", URL);
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("DocNo", "" +DocNo );
                Log.e("DocNo", "" +DocNo );

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                AndroidHttpTransport aht = new AndroidHttpTransport(URL);
                aht.call(SOAP_ACTION, soapEnvelope);
                SoapPrimitive resultString = (SoapPrimitive) soapEnvelope.getResponse();

                String responseJSON = resultString.toString();
                Log.e("RESPONSE", responseJSON);

                quotationdetails = new JSONArray(responseJSON);
                for (int i = 0; i < quotationdetails.length(); i++) {
                    JSONObject jsonObject = quotationdetails.getJSONObject(i);
                    SalesQuo_Model model = new SalesQuo_Model();
                    model.setRowId (jsonObject.getString ("RowId"));
                    model.setDocNo (jsonObject.getString ("DocNo"));
                    model.setItemcode (jsonObject.getString ("ItemCode"));
                    model.setName (jsonObject.getString ("ItemName"));
                    model.setUom (jsonObject.getString ("uom"));
                    model.setQuantity (jsonObject.getString ("qty"));
                    model.setPrice (jsonObject.getString ("price"));
                    model.setLastquotprice (jsonObject.getString ("LastQPrice"));
                    model.setDiscount (jsonObject.getString ("Discount"));
                    model.setPriceofDis (jsonObject.getString ("Priceaftdisc"));
                    model.setTotal (jsonObject.getString ("Total"));
                    model.setTaxcode (jsonObject.getString ("taxcode"));
                    model.setWhseCode (jsonObject.getString ("whsecode"));
                    model.setWhseName (jsonObject.getString ("whsename"));
                    model.setHsncode (jsonObject.getString ("HsnCode"));
                    model.setTaxprcnt (jsonObject.getString ("Taxprcnt"));
                    model.setTotalTaxamnt (jsonObject.getString ("taxamt"));
                    model.setDiscAmt (jsonObject.getString ("DiscAmt"));
                    model.setProjectName (jsonObject.getString ("ProjectName"));
                    salesQuo_modelArrayList.add (model);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if(quotationdetails.length()==0){
                Toast.makeText(SalesQuotation.this, "No Data Found", Toast.LENGTH_SHORT).show();
            } else {

                SalesQuotationrecycler.setVisibility(View.VISIBLE);
                SalesQuotationrecycler.setLayoutManager(new LinearLayoutManager(SalesQuotation.this));
                Adapter = new SalesQuotationAdapter(getApplicationContext(), salesQuo_modelArrayList);
                SalesQuotationrecycler.setAdapter(Adapter);
                SalesQuotationrecycler.setHasFixedSize(true);
            }
        }
    }

    private class GetDiscountAmnt extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SalesQuotation.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_GetDiscAmt";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_GetDiscAmt";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_GetDiscAmt";
                Log.e("URL", URL);
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);


                if (editstatus.equalsIgnoreCase ("0")) {
                    double quantity=0;
                    double price=0;
                    quantity= Double.parseDouble(Quantity_et.getText().toString());
                    price= Double.parseDouble(price_et.getText().toString());

                    double discount=0;
                    if (Tbefdisc_et.getText().toString().trim().equalsIgnoreCase("")){
                        discount=0;
                    } else {
                        discount= Double.parseDouble(Tbefdisc_et.getText().toString().trim());
                    }
                    double Taxprcnt=0;

                    Request.addProperty ("Qty", "" + quantity);
                    Log.e ("Qty", "editstatus0" + quantity);
                    Request.addProperty ("Price", "" + price);
                    Log.e ("Price", "editstatus0" + price);
                    Request.addProperty ("Discount", "" + discount);
                    Log.e ("Discount", "editstatus0" + discount);
                } else if (calculate_status.equalsIgnoreCase("1")) {
                    Request.addProperty ("Qty", "" + editedqty);
                    Log.e ("Qty", "calculate_status" + editedqty);
                    Request.addProperty ("Price", "" + editedprice);
                    Log.e ("Price", "calculate_status" + editedprice);
                    Request.addProperty ("Discount", "" + editeddiscount);
                    Log.e ("Discount", "calculate_status" + editeddiscount);
                } else if (editstatus.equalsIgnoreCase("1")) {
                    Request.addProperty ("Qty", "" + Quantity_et.getText().toString().trim());
                    Log.e ("Qty", "editstatus1" + Quantity_et.getText().toString().trim());
                    Request.addProperty ("Price", "" + price_et.getText().toString().trim());
                    Log.e ("Price", "editstatus1" + price_et.getText().toString().trim());
                    Request.addProperty ("Discount", "" + Tbefdisc_et.getText().toString().trim());
                    Log.e ("Discount", "editstatus1" + Tbefdisc_et.getText().toString().trim());
                } else {
                    Log.e("calculate_status","calculate_status"+calculate_status);
                    Log.e("editstatus","editstatus"+editstatus);
                }

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                AndroidHttpTransport aht = new AndroidHttpTransport(URL);
                aht.call(SOAP_ACTION, soapEnvelope);
                SoapPrimitive resultString = (SoapPrimitive) soapEnvelope.getResponse();

                String responseJSON = resultString.toString();
                Log.e("RESPONSE", responseJSON);

                jsonArray6 = new JSONArray(responseJSON);
                for (int i = 0; i < jsonArray6.length(); i++) {
                    JSONObject jsonObject = jsonArray6.getJSONObject(i);
                    Column1_Totaldiscountamnt=jsonObject.getString("Column1");
                    Log.e ("Column1_Totalamnt", Column1_Totaldiscountamnt);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if(jsonArray6.length()==0){
                Toast.makeText(SalesQuotation.this, "Error", Toast.LENGTH_SHORT).show();
            }else {
                if (ConnectivityReceiver.isConnected(SalesQuotation.this)) {
                    new HikePriceCalculation().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Check Your Internet Connection!!!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class HikePriceCalculation extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SalesQuotation.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_GetQuotePrice";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_GetQuotePrice";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_GetQuotePrice";
                Log.e("URL", URL);
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                if (editstatus.equalsIgnoreCase ("0")) {
                    double hikepercent=0;
                    double price=0;
                    hikepercent = Double.parseDouble(hike_et.getText().toString());
                    price = Double.parseDouble(price_et.getText().toString());

                    Request.addProperty ("Price", "" + price);
                    Log.e ("Price", "" + price);
                    Request.addProperty ("hikepercent", "" + hikepercent);
                    Log.e ("hikepercent", "" + hikepercent);
                } else if (calculate_status.equalsIgnoreCase("1")) {
                    Request.addProperty ("Price", "" + editedqty);
                    Log.e ("Price", "" + editedqty);
                    Request.addProperty ("hikepercent", "" + editedHike);
                    Log.e ("hikepercent", "" + editedHike);
                } else {
                    Request.addProperty ("Price", "" + price_et.getText().toString().trim());
                    Log.e ("Price", "" + price_et.getText().toString().trim());
                    Request.addProperty ("hikepercent", "" + hike_et.getText().toString().trim());
                    Log.e ("hikepercent", "" + hike_et.getText().toString().trim());
                }

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                AndroidHttpTransport aht = new AndroidHttpTransport(URL);
                aht.call(SOAP_ACTION, soapEnvelope);
                SoapPrimitive resultString = (SoapPrimitive) soapEnvelope.getResponse();

                String responseJSON = resultString.toString();
                Log.e("RESPONSE", responseJSON);

                jsonArray6 = new JSONArray(responseJSON);
                for (int i = 0; i < jsonArray6.length(); i++) {
                    JSONObject jsonObject = jsonArray6.getJSONObject(i);
                    Column1_hike_price_calc=jsonObject.getString("Column1");
                    Log.e ("Column1_hike_price", Column1_hike_price_calc);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if(jsonArray6.length()==0){
                Toast.makeText(SalesQuotation.this, "Error", Toast.LENGTH_SHORT).show();
            }else {
                if (ConnectivityReceiver.isConnected(SalesQuotation.this)) {
                    new GetPriceAftDisc().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Check Your Internet Connection!!!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class GetPriceAftDisc extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SalesQuotation.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_GetPriceAftDisc";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_GetPriceAftDisc";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_GetPriceAftDisc";
                Log.e("URL", URL);
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);


                if (editstatus.equalsIgnoreCase ("0")) {
                    double discount=0;
                    double price=0;
                    discount = Double.parseDouble(Tbefdisc_et.getText().toString().trim());
//                    price = Double.parseDouble(price_et.getText().toString());
                    price = Double.parseDouble(Column1_hike_price_calc);

                    Request.addProperty ("Price", "" + price);
                    Log.e ("Price", "" + price);
                    Request.addProperty ("Discount", "" + discount);
                    Log.e ("Discount", "" + discount);
                } else if (calculate_status.equalsIgnoreCase("1")) {
                    Request.addProperty ("Price", "" + editedprice);
                    Log.e ("Price", "" + editedprice);
                    Request.addProperty ("Discount", "" + editeddiscount);
                    Log.e ("Discount", "" + editeddiscount);
                } else {
                    Request.addProperty ("Price", "" + price_et.getText().toString().trim());
                    Log.e ("Price", "" + price_et.getText().toString().trim());
                    Request.addProperty ("Discount", "" + Tbefdisc_et.getText().toString().trim());
                    Log.e ("hikepercent", "" + Tbefdisc_et.getText().toString().trim());
                }

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                AndroidHttpTransport aht = new AndroidHttpTransport(URL);
                aht.call(SOAP_ACTION, soapEnvelope);
                SoapPrimitive resultString = (SoapPrimitive) soapEnvelope.getResponse();

                String responseJSON = resultString.toString();
                Log.e("RESPONSE", responseJSON);

                jsonArray6 = new JSONArray(responseJSON);
                for (int i = 0; i < jsonArray6.length(); i++) {
                    JSONObject jsonObject = jsonArray6.getJSONObject(i);
                    Column1_discount_price_calc=jsonObject.getString("Column1");
                    Log.e ("Column1_dis_pri_calc", Column1_discount_price_calc);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if(jsonArray6.length()==0){
                Toast.makeText(SalesQuotation.this, "Error", Toast.LENGTH_SHORT).show();
            }else {
                if (ConnectivityReceiver.isConnected(SalesQuotation.this)) {
                    new GetTaxAmt().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Check Your Internet Connection!!!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class GetTaxAmt extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SalesQuotation.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_GetTaxAmt";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_GetTaxAmt";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_GetTaxAmt";
                Log.e("URL", URL);
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                if (editstatus.equalsIgnoreCase ("0")) {
                    double quantity=0;
                    double price=0;
                    double taxRate=0;
                    quantity= Double.parseDouble(Quantity_et.getText().toString());
                    price= Double.parseDouble(price_et.getText().toString());
                    taxRate= Double.parseDouble(taxRate_et.getText().toString());

                    double discount=0;

                    if (Tbefdisc_et.getText().toString().trim().equalsIgnoreCase("")){
                        discount=0;
                    } else {
                        discount= Double.parseDouble(Tbefdisc_et.getText().toString().trim());
                    }
                    double Taxprcnt=0;
                    Request.addProperty ("Qty", "" + quantity);
                    Log.e ("Qty", "" + quantity);
                    Request.addProperty ("Price", "" + price);
                    Log.e ("Price", "" + price);
                    Request.addProperty ("Discount", "" + discount);
                    Log.e ("Discount", "" + discount);
                    Request.addProperty ("TaxAmt", "" + 0);
                    Log.e ("TaxAmt", "" + 0);
                    Request.addProperty ("Taxprcnt", "" + taxRate);
                    Log.e ("Taxprcnt", "" + taxRate);
                } else if (calculate_status.equalsIgnoreCase("1")){
                    Request.addProperty ("Qty", "" + editedqty);
                    Log.e ("Qty", "" + editedqty);
                    Request.addProperty ("Price", "" + editedprice);
                    Log.e ("Price", "" + editedprice);
                    Request.addProperty ("Discount", "" + editeddiscount);
                    Log.e ("Discount", "" + editeddiscount);
                    Request.addProperty ("TaxAmt", "" + 0);
                    Log.e ("TaxAmt", "" + 0);
                    Request.addProperty ("Taxprcnt", "" + editedtaxprcnt_Rate);
                    Log.e ("Taxprcnt", "" + editedtaxprcnt_Rate);
                } else {
                    Request.addProperty ("Qty", "" + Quantity_et.getText().toString().trim());
                    Log.e ("Qty", "" + Quantity_et.getText().toString().trim());
                    Request.addProperty ("Price", "" + price_et.getText().toString().trim());
                    Log.e ("Price", "" + price_et.getText().toString().trim());
                    Request.addProperty ("Discount", "" + Tbefdisc_et.getText().toString().trim());
                    Log.e ("Discount", "" + Tbefdisc_et.getText().toString().trim());
                    Request.addProperty ("TaxAmt", "" + 0);
                    Log.e ("TaxAmt", "" + 0);
                    Request.addProperty ("Taxprcnt", "" + taxRate_et.getText().toString().trim());
                    Log.e ("Taxprcnt", "" + taxRate_et.getText().toString().trim());
                }

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                AndroidHttpTransport aht = new AndroidHttpTransport(URL);
                aht.call(SOAP_ACTION, soapEnvelope);
                SoapPrimitive resultString = (SoapPrimitive) soapEnvelope.getResponse();

                String responseJSON = resultString.toString();
                Log.e("RESPONSE", responseJSON);

                jsonArray6 = new JSONArray(responseJSON);
                for (int i = 0; i < jsonArray6.length(); i++) {
                    JSONObject jsonObject = jsonArray6.getJSONObject(i);
                    Column1_Totaltaxamnt=jsonObject.getString("Column1");
                    Log.e("Column1_Totaltaxamnt",Column1_Totaltaxamnt);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if(jsonArray6.length()==0){
                Toast.makeText(SalesQuotation.this, "Not Getting TaxAmount", Toast.LENGTH_SHORT).show();
            }else {
                if (editstatus.equalsIgnoreCase ("0")) {
                    if (total_et.getText ( ).toString ( ).trim ( ).equalsIgnoreCase ("")) {
                        Total = "0";
                    } else {
                        Total = total_et.getText ( ).toString ( ).trim ( );
                    }
                    SalesQuo_Model Model = new SalesQuo_Model ( );
                    Model.setRowId ("0");
                    Model.setTotalTaxamnt (Column1_Totaltaxamnt);
                    Model.setTotalDiscountamnt ("" + Column1_Totaldiscountamnt);
                    Model.setTaxprcnt ("" + taxRate_et.getText().toString().trim());
                    double quantity = 0;
                    double price = 0;
                    quantity = Double.parseDouble (Quantity_et.getText ( ).toString ( ));
//                    price = Double.parseDouble (price_et.getText ( ).toString ( ));
                    price = Double.parseDouble (Column1_discount_price_calc);

                    Model.setTotal ("" + quantity * price);
                    Model.setItemcode (itemcode);
                    Model.setName (itemname);
                    Model.setUom (Uom);
                    Model.setQuantity (Quantity_et.getText ( ).toString ( ).trim ( ));
                    Model.setHike (hike_et.getText ( ).toString ( ).trim ( ));
                    Model.setPrice (Column1_hike_price_calc);
                    Model.setLastquotprice (lastquoteprice);
                    Model.setProjectName (projectname_et.getText().toString().trim());

                    double discount = 0;

                    if (Tbefdisc_et.getText ( ).toString ( ).trim ( ).equalsIgnoreCase ("")) {
                        discount = 0;
                    } else {
                        discount = Double.parseDouble (Tbefdisc_et.getText ( ).toString ( ).trim ( ));
                    }
                    Model.setDiscount ("" + discount);
                    Model.setPriceofDis ("" + Column1_discount_price_calc);

                    Model.setTaxcode (taxcode_et.getText().toString());
                    Model.setWhseName (warehouse_et.getText().toString());
                    Model.setWhseCode (wrhscode_et.getText().toString());
                    Model.setHsncode (HSN);
                    Model.setHsncode (HSN);

                    salesQuo_modelArrayList.add (Model);
                    SalesQuotationrecycler.setVisibility (View.VISIBLE);
                    SalesQuotationrecycler.setLayoutManager (new LinearLayoutManager (SalesQuotation.this));
                    Adapter = new SalesQuotationAdapter (getApplicationContext ( ), salesQuo_modelArrayList);
                    SalesQuotationrecycler.setAdapter (Adapter);
                    SalesQuotationrecycler.setHasFixedSize (true);

                    Quantity_et.setText ("");
                    price_et.setText ("");
//                    Tbefdisc_et.setText ("");
                    edtremarks.setText ("");
                    finaltotal = 0;
                    finaltotaltaxamnt = 0;
                    grandtotal = 0;
                } else {
//                    Adapter.notifyItemChanged (Integer.parseInt (position1));
                    Adapter.notifyDataSetChanged ();
//                    if (total_et.getText ( ).toString ( ).trim ( ).equalsIgnoreCase ("")) {
//                        Total = "0";
//                    } else {
//                        Total = total_et.getText ( ).toString ( ).trim ( );
//                    }
//                    SalesQuo_Model Model = new SalesQuo_Model ( );
//                    Model.setRowId ("0");
//                    Model.setTotalTaxamnt (Column1_Totaltaxamnt);
//                    Model.setTotalDiscountamnt ("" + Column1_Totaldiscountamnt);
//                    Model.setTaxprcnt ("" + taxRate_et.getText().toString().trim());
//                    double quantity = 0;
//                    double price = 0;
//                    quantity = Double.parseDouble (Quantity_et.getText ( ).toString ( ));
////                    price = Double.parseDouble (price_et.getText ( ).toString ( ));
//                    price = Double.parseDouble (Column1_discount_price_calc);
//
//                    Model.setTotal ("" + quantity * price);
//                    Model.setItemcode (itemcode);
//                    Model.setName (itemname);
//                    Model.setUom (Uom);
//                    Model.setQuantity (Quantity_et.getText ( ).toString ( ).trim ( ));
//                    Model.setHike (hike_et.getText ( ).toString ( ).trim ( ));
//                    Model.setPrice (Column1_hike_price_calc);
//                    Model.setLastquotprice (lastquoteprice);
//                    Model.setProjectName (projectname_et.getText().toString().trim());
//
//                    double discount = 0;
//
//                    if (Tbefdisc_et.getText ( ).toString ( ).trim ( ).equalsIgnoreCase ("")) {
//                        discount = 0;
//                    } else {
//                        discount = Double.parseDouble (Tbefdisc_et.getText ( ).toString ( ).trim ( ));
//                    }
//                    Model.setDiscount ("" + discount);
//                    Model.setPriceofDis (""+Column1_discount_price_calc);
//
//                    Model.setTaxcode (taxcode_et.getText().toString());
//                    Model.setWhseName (warehouse_et.getText().toString());
//                    Model.setWhseCode (wrhscode_et.getText().toString());
//                    Model.setHsncode (HSN);
//                    Model.setHsncode (HSN);
//
//                    salesQuo_modelArrayList.add (Model);
//                    SalesQuotationrecycler.setVisibility (View.VISIBLE);
//                    SalesQuotationrecycler.setLayoutManager (new LinearLayoutManager (SalesQuotation.this));
//                    Adapter = new SalesQuotationAdapter (getApplicationContext ( ), salesQuo_modelArrayList);
//                    SalesQuotationrecycler.setAdapter (Adapter);
//                    SalesQuotationrecycler.setHasFixedSize (true);
//
//                    Quantity_et.setText ("");
//                    price_et.setText ("");
////                    Tbefdisc_et.setText ("");
//                    edtremarks.setText ("");
//                    finaltotal = 0;
//                    finaltotaltaxamnt = 0;
//                    grandtotal = 0;
//
//                    Adapter.notifyDataSetChanged ();
                }
            }
        }
    }

    public class SalesQuotationAdapter extends RecyclerView.Adapter {

        Context getApplicationContext;
        ArrayList<SalesQuo_Model> salesQuo_modelArrayList;
        String status1="",positionclicked;
        String edited_qty,edited_price,edited_discount,edited_Hike;

        public SalesQuotationAdapter(Context getApplicationContext, ArrayList<SalesQuo_Model> salesQuo_modelArrayList) {
            this.getApplicationContext = getApplicationContext;
            this.salesQuo_modelArrayList = salesQuo_modelArrayList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.quotationdetail, parent, false);
            return new SalesQuotationAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position)
        {
            if(holder instanceof ViewHolder) {

                double discuontXX = Double.parseDouble(salesQuo_modelArrayList.get(position).getDiscount());
                List l = new ArrayList();
                l.add(discuontXX);
                Log.e("discuontXX", "" + discuontXX);
                Log.e("discuontXX", "" + Collections.max(l));

                ((ViewHolder) holder).itemcode.setText(salesQuo_modelArrayList.get(position).getItemcode());
                ((ViewHolder) holder).name.setText(salesQuo_modelArrayList.get(position).getName());
                ((ViewHolder) holder).UOM.setText(salesQuo_modelArrayList.get(position).getUom());
                ((ViewHolder) holder).Quantity.setText(salesQuo_modelArrayList.get(position).getQuantity());
                ((ViewHolder) holder).Hike.setText(salesQuo_modelArrayList.get(position).getHike());
                ((ViewHolder) holder).Price.setText(salesQuo_modelArrayList.get(position).getPrice());
                ((ViewHolder) holder).priceafterDiscount.setText(salesQuo_modelArrayList.get(position).getPriceofDis());
                ((ViewHolder) holder).LastQuotePrice.setText(salesQuo_modelArrayList.get(position).getLastquotprice());
                ((ViewHolder) holder).Discount.setText(salesQuo_modelArrayList.get(position).getDiscount());
                ((ViewHolder) holder).TotalDiscAmnt.setText(salesQuo_modelArrayList.get(position).getTotalDiscountamnt());
                ((ViewHolder) holder).TotalTaxAmnt.setText(salesQuo_modelArrayList.get(position).getTotalTaxamnt());
                ((ViewHolder) holder).Total.setText(salesQuo_modelArrayList.get(position).getTotal());
                ((ViewHolder) holder).Taxcode.setText(salesQuo_modelArrayList.get(position).getTaxcode());
                ((ViewHolder) holder).WhseName.setText(salesQuo_modelArrayList.get(position).getWhseName());
                ((ViewHolder) holder).HSNcode.setText(salesQuo_modelArrayList.get(position).getHsncode());
                ((ViewHolder) holder).Attachment.setText(salesQuo_modelArrayList.get(position).getAttachment());
                ((ViewHolder) holder).projectname.setText(salesQuo_modelArrayList.get(position).getProjectName());

                if (editstatus.equalsIgnoreCase ("1")) {

                    Log.e ("editstatus",editstatus);
                    if (calculate_status.equalsIgnoreCase ("1")) {
                        Log.e ("calculate_status", "done" + calculate_status);

                        if (calculateposition.equalsIgnoreCase (String.valueOf (position))) {
                            totalvalue = editedqty * editedprice;
                            totaltaxamntvalue = Double.parseDouble (Column1_Totaltaxamnt);
                            totaldiscount = Double.parseDouble (Column1_Totaldiscountamnt);
                            Log.e ("edit--totalvalue", "" + totalvalue);
                            Log.e ("edit--totaltaxamntvalue", "" + totaltaxamntvalue);
                            Log.e ("edit--totaldiscount", "" + totaldiscount);
                            salesQuo_modelArrayList.get (position).setTotal (String.valueOf (totalvalue));
                            salesQuo_modelArrayList.get (position).setTotalTaxamnt (String.valueOf (totaltaxamntvalue));
                            salesQuo_modelArrayList.get (position).setTotalDiscountamnt (String.valueOf (totaldiscount));
                            ((ViewHolder) holder).Total.setText (salesQuo_modelArrayList.get (position).getTotal ( ));
                            ((ViewHolder) holder).TotalTaxAmnt.setText (salesQuo_modelArrayList.get (position).getTotalTaxamnt ( ));
                            ((ViewHolder) holder).TotalDiscAmnt.setText (salesQuo_modelArrayList.get (position).getTotalDiscountamnt ( ));
                            ((ViewHolder) holder).priceafterDiscount.setText (Column1_discount_price_calc);
                        } else {
                            Log.e ("Position", "Does Not Match");
                        }
                        if (status1.equalsIgnoreCase ("")) {

                            totalvalue = Double.parseDouble (salesQuo_modelArrayList.get (position).getTotal ( ));
                            totaltaxamntvalue = Double.parseDouble (salesQuo_modelArrayList.get (position).getTotalTaxamnt ( ));
                            Log.e ("getTotalDiscountamnt", "getTotalDiscountamntXX" +salesQuo_modelArrayList.get (position).getTotalDiscountamnt ( ));
                            if (salesQuo_modelArrayList.get (position).getTotalDiscountamnt ( ) == null || salesQuo_modelArrayList.get (position).getTotalDiscountamnt ( ).equalsIgnoreCase ("") || salesQuo_modelArrayList.get (position).getTotalDiscountamnt ( ).equalsIgnoreCase ("null")) {
                                totaldiscount = 0;
                            } else {
                                totaldiscount = Double.parseDouble (salesQuo_modelArrayList.get (position).getTotalDiscountamnt ( ));
                            }
                            finaltotal = (finaltotal) + (totalvalue);
                            total_et.setText ("" + finaltotal);
                            Log.e ("edit--finaltotal", "" + finaltotal);

                            finaltotaltaxamnt = (finaltotaltaxamnt) + (totaltaxamntvalue);
                            totaltaxamnt_et.setText ("" + finaltotaltaxamnt);
                            Log.e ("edit--finaltotaltaxamnt", "" + finaltotaltaxamnt);

                            finaldiscount = (finaldiscount) + (totaldiscount);
//                            totaldisc_et.setText ("" + finaldiscount);
                            Log.e ("edit--finaldiscount", "" + finaldiscount);

                            grandtotal = finaltotal + finaltotaltaxamnt;
                            grandtotal_et.setText ("" + grandtotal);
                            Log.e ("edit--grandtotal", "" + grandtotal);
                        } else {
                            Log.e ("validation", "Not done");
                        }
                    } else {
                        Log.e ("calculate_status", "Not done" + calculate_status);
                    }
                } else if (editstatus.equalsIgnoreCase ("2")) {
                    Log.e ("editstatusRRR",editstatus);
                    ((ViewHolder) holder).calculate_btn.setVisibility (View.GONE);
                    ((ViewHolder) holder).deletebtn.setEnabled (false);
                } else {

                    ((ViewHolder) holder).calculate_btn.setVisibility(View.GONE);
                    totalvalue = Double.parseDouble (salesQuo_modelArrayList.get (position).getTotal ( ));
                    totaltaxamntvalue = Double.parseDouble (salesQuo_modelArrayList.get (position).getTotalTaxamnt ( ));
                    Log.e ("getTotalDiscountamnt","getTotalDiscountamntFF" + salesQuo_modelArrayList.get (position).getTotalDiscountamnt());
                    if (salesQuo_modelArrayList.get (position).getTotalDiscountamnt ( ).equalsIgnoreCase ("") || salesQuo_modelArrayList.get (position).getTotalDiscountamnt ( ) == null) {
                        totaldiscount = 0;
                    } else {
                        totaldiscount = Double.parseDouble (salesQuo_modelArrayList.get (position).getTotalDiscountamnt ( ));
                    }
                    if (status1.equalsIgnoreCase("")) {

                        finaltotal = ( finaltotal ) + ( totalvalue );
                        total_et.setText("" + finaltotal);

                        finaltotaltaxamnt = ( finaltotaltaxamnt ) + ( totaltaxamntvalue );
                        totaltaxamnt_et.setText("" + finaltotaltaxamnt);

                        finaldiscount = ( finaldiscount ) + ( totaldiscount );
//                        totaldisc_et.setText("" + finaldiscount);

                        grandtotal = finaltotal + finaltotaltaxamnt;
                        grandtotal_et.setText("" + grandtotal);
                    } else {
                        Log.e("validation","Not done");
                    }
                }

                ((ViewHolder) holder ).deletebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        status1="1";
                        double totalvalue= Double.parseDouble(salesQuo_modelArrayList.get(position).getTotal());
                        double totaltaxamntvalue= Double.parseDouble(salesQuo_modelArrayList.get(position).getTotalTaxamnt());
                        double totaldiscount= Double.parseDouble(salesQuo_modelArrayList.get(position).getTotalDiscountamnt());
                        Log.e("totalvalueYY", String.valueOf(totalvalue));
                        Log.e("totaltaxamntvalueYY", String.valueOf(totaltaxamntvalue));
                        Log.e("totaldiscountYY", String.valueOf(totaldiscount));
                        Log.e("finaltotalXX", String.valueOf(finaltotal));
                        Log.e("finaltotaltaxamntXX", String.valueOf(finaltotaltaxamnt));
                        Log.e("finaldiscountXX", String.valueOf(finaldiscount));

                        finaltotal= (finaltotal)-(totalvalue);
                        total_et.setText(""+finaltotal);
                        Log.e("finaltotal", String.valueOf(finaltotal));
                        Log.e("totalvalue", String.valueOf(totalvalue));

                        finaltotaltaxamnt= (finaltotaltaxamnt)-(totaltaxamntvalue);
                        totaltaxamnt_et.setText(""+finaltotaltaxamnt);
                        Log.e("finaltotaltaxamnt", String.valueOf(finaltotaltaxamnt));
                        Log.e("totaltaxamntvalue", String.valueOf(totaltaxamntvalue));

                        finaldiscount= (finaldiscount)-(totaldiscount);
//                        totaldisc_et.setText(""+finaldiscount);
                        Log.e("finaldiscount", String.valueOf(finaldiscount));
                        Log.e("totaldiscount", String.valueOf(totaldiscount));

                        grandtotal= finaltotal + finaltotaltaxamnt;
                        grandtotal_et.setText(""+grandtotal);

                        salesQuo_modelArrayList.remove(position);
                        notifyDataSetChanged();
                    }
                });

                ((ViewHolder) holder).name.setFocusable (false);
                ((ViewHolder) holder).name.setFocusableInTouchMode (false);
                ((ViewHolder) holder).name.setEnabled (false);
                ((ViewHolder) holder).Quantity.setFocusable (false);
                ((ViewHolder) holder).Quantity.setFocusableInTouchMode (false);

                ((ViewHolder) holder).Hike.setFocusable (false);
                ((ViewHolder) holder).Hike.setFocusableInTouchMode (false);
                ((ViewHolder) holder).Price.setFocusable (false);
                ((ViewHolder) holder).Price.setFocusableInTouchMode (false);

                ((ViewHolder) holder).Discount.setFocusable (false);
                ((ViewHolder) holder).Discount.setFocusableInTouchMode (false);

                ((ViewHolder) holder).Taxcode.setFocusable (false);
                ((ViewHolder) holder).Taxcode.setFocusableInTouchMode (false);
                ((ViewHolder) holder).Taxcode.setEnabled (false);
                ((ViewHolder) holder).WhseName.setFocusable (false);
                ((ViewHolder) holder).WhseName.setFocusableInTouchMode (false);
                ((ViewHolder) holder).WhseName.setEnabled (false);

                if (position1.equalsIgnoreCase ("")) {
                    Log.e ("Not Yet"," Clicked");
                } else {
                    ((ViewHolder) holder).itemcode.setEnabled (false);

                    ((ViewHolder) holder).name.setEnabled (true);
                    ((ViewHolder) holder).name.setFocusableInTouchMode (false);
                    ((ViewHolder) holder).Taxcode.setEnabled (true);
                    ((ViewHolder) holder).Taxcode.setFocusableInTouchMode (false);
                    ((ViewHolder) holder).WhseName.setEnabled (true);
                    ((ViewHolder) holder).WhseName.setFocusableInTouchMode (false);

                    ((ViewHolder) holder).Quantity.setEnabled (true);
                    ((ViewHolder) holder).Quantity.setFocusableInTouchMode (true);
                    ((ViewHolder) holder).Hike.setEnabled (true);
                    ((ViewHolder) holder).Hike.setFocusableInTouchMode (true);
                    ((ViewHolder) holder).Price.setEnabled (true);
                    ((ViewHolder) holder).Price.setFocusableInTouchMode (true);
                    ((ViewHolder) holder).Discount.setEnabled (true);
                    ((ViewHolder) holder).Discount.setFocusableInTouchMode (true);
                }

                if (editstatus.equalsIgnoreCase ("1")) {
                    ((ViewHolder) holder).editimage.setVisibility (View.VISIBLE);

                    ((ViewHolder) holder).itemcode.setOnClickListener (new View.OnClickListener ( ) {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText (getApplicationContext, "Kindly Click on Edit to Change this Data", Toast.LENGTH_SHORT).show ( );
                        }
                    });
                    ((ViewHolder) holder).editimage.setOnClickListener (new View.OnClickListener ( ) {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText (getApplicationContext, "Now you can Edit this Row", Toast.LENGTH_SHORT).show ( );

                            ((ViewHolder) holder).itemcode.setEnabled (false);

                            ((ViewHolder) holder).name.setEnabled (true);
                            ((ViewHolder) holder).name.setFocusableInTouchMode (false);
                            ((ViewHolder) holder).Taxcode.setEnabled (true);
                            ((ViewHolder) holder).Taxcode.setFocusableInTouchMode (false);
                            ((ViewHolder) holder).WhseName.setEnabled (true);
                            ((ViewHolder) holder).WhseName.setFocusableInTouchMode (false);

                            ((ViewHolder) holder).Quantity.setEnabled (true);
                            ((ViewHolder) holder).Quantity.setFocusableInTouchMode (true);
                            ((ViewHolder) holder).Hike.setEnabled (true);
                            ((ViewHolder) holder).Hike.setFocusableInTouchMode (true);
                            ((ViewHolder) holder).Price.setEnabled (true);
                            ((ViewHolder) holder).Price.setFocusableInTouchMode (true);
                            ((ViewHolder) holder).Discount.setEnabled (true);
                            ((ViewHolder) holder).Discount.setFocusableInTouchMode (true);
                        }
                    });

                    ((ViewHolder) holder).name.setOnClickListener (new View.OnClickListener ( ) {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent (getApplicationContext, ItemList_Spinner.class);
                            intent.putExtra ("ScreenStatus", "SQ");
                            intent.putExtra ("position1", "" + position);
                            positionclicked = String.valueOf (position);
                            startActivityForResult (intent, 2);
                        }
                    });

                    if (itemname.equalsIgnoreCase ("")) {
                        Log.e ("ITEMNAMEEMPTY", "" + itemname);
                    } else {
                        Log.e ("position1", "position1" + position1);
                        Log.e ("positionclicked", "positionclicked" + positionclicked);
                        if (position1.equalsIgnoreCase (positionclicked)) {
                            ((ViewHolder) holder).name.setText (itemname);
                            ((ViewHolder) holder).itemcode.setText (itemcode);
                            ((ViewHolder) holder).UOM.setText (Uom);
                            ((ViewHolder) holder).LastQuotePrice.setText (lastquoteprice);
                            ((ViewHolder) holder).HSNcode.setText (HSN);
                            Log.e ("ChangedVlues", "itemname" + itemname);
                            Log.e ("ChangedVlues", "itemcode" + itemcode);
                            Log.e ("ChangedVlues", "Uom" + Uom);
                            Log.e ("ChangedVlues", "lastquoteprice" + lastquoteprice);
                            Log.e ("ChangedVlues", "HSN" + HSN);
                            salesQuo_modelArrayList.get (position).setName (itemname);
                            salesQuo_modelArrayList.get (position).setItemcode (itemcode);
                            salesQuo_modelArrayList.get (position).setUom (Uom);
                            salesQuo_modelArrayList.get (position).setLastquotprice (lastquoteprice);
                            salesQuo_modelArrayList.get (position).setHsncode (HSN);
                        } else {
                            Log.e ("Position", "Not Match");
                        }
                    }

                    ((ViewHolder) holder).Taxcode.setOnClickListener (new View.OnClickListener ( ) {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent (getApplicationContext, TaxCodeList_Spinner.class);
                            intent.putExtra ("ScreenStatus", "SQ");
                            intent.putExtra ("position1", "" + position);
                            positionclicked = String.valueOf (position);
                            startActivityForResult (intent, 4);
                        }
                    });
                    if (code.equalsIgnoreCase ("")) {
                        Log.e ("TAXCODEEMPTY", "" + code);
                    } else {
                        Log.e ("Taxposition1", "Taxposition1" + Taxposition1);
                        Log.e ("positionclicked", "positionclicked" + positionclicked);
                        if (Taxposition1.equalsIgnoreCase (positionclicked)) {
                            ((ViewHolder) holder).Taxcode.setText (code);
                            Log.e ("ChangedVlues", "TAxcode" + code);
                            Log.e ("ChangedVlues", "Rate" + Rate);
                            salesQuo_modelArrayList.get (position).setTaxcode (code);
                        } else {
                            Log.e ("Position", "Not Match");
                        }
                    }

                    ((ViewHolder) holder).WhseName.setOnClickListener (new View.OnClickListener ( ) {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent (SalesQuotation.this, WarehouseList_Spinner.class);
                            intent.putExtra ("ScreenStatus", "SQ");
                            intent.putExtra ("position1", "" + position);
                            positionclicked = String.valueOf (position);
                            startActivityForResult (intent, 7);
                        }
                    });
                    if (WhsName.equalsIgnoreCase ("")) {
                        Log.e ("WhsNameEMPTY", "" + WhsName);
                    } else {
                        Log.e ("WHSposition1", "WHSposition1" + WHSposition1);
                        Log.e ("positionclicked", "positionclicked" + positionclicked);
                        if (WHSposition1.equalsIgnoreCase (positionclicked)) {
                            ((ViewHolder) holder).WhseName.setText (WhsName);
                            Log.e ("ChangedVlues", "WhsName" + WhsName);
                            Log.e ("ChangedVlues", "WhsCode" + WhsCode);
                            salesQuo_modelArrayList.get (position).setWhseName (WhsName);
                            salesQuo_modelArrayList.get (position).setWhseCode (WhsCode);
                        } else {
                            Log.e ("Position", "Not Match");
                        }
                    }
                } else {
                    ((ViewHolder) holder).editimage.setVisibility (View.GONE);
                }

                ((ViewHolder) holder).Quantity.addTextChangedListener(new TextWatcher ()
                {
                    @Override
                    public void afterTextChanged(Editable mEdit)
                    {
                        edited_qty = mEdit.toString();
                        Log.e ("edited_qty",edited_qty);
                        salesQuo_modelArrayList.get(position).setQuantity (edited_qty);
//                        Log.e ("editedqty",salesQuo_modelArrayList.get (position).getQuantity ());
//                        editedqty = Double.parseDouble (salesQuo_modelArrayList.get (position).getQuantity ());
//                        editedprice = Double.parseDouble (salesQuo_modelArrayList.get (position).getPrice ());
//                        editeddiscount = Double.parseDouble (salesQuo_modelArrayList.get (position).getDiscount ());
//                        editedtaxprcnt_Rate = Double.parseDouble (salesQuo_modelArrayList.get (position).getTaxprcnt ());
//                        new GetDiscountAmnt ().execute ();
                    }
                    public void beforeTextChanged(CharSequence s, int start, int count, int after){

                    }
                    public void onTextChanged(CharSequence s, int start, int before, int count){

                    }
                });
                ((ViewHolder) holder).Price.addTextChangedListener(new TextWatcher ()
                {
                    @Override
                    public void afterTextChanged(Editable mEdit)
                    {
                        edited_price = mEdit.toString();
                        Log.e ("edited_price",edited_price);
                        salesQuo_modelArrayList.get(position).setPrice (edited_price);

//                        editedqty = Double.parseDouble (salesQuo_modelArrayList.get (position).getQuantity ());
//                        editedprice = Double.parseDouble (salesQuo_modelArrayList.get (position).getPrice ());
//                        editeddiscount = Double.parseDouble (salesQuo_modelArrayList.get (position).getDiscount ());
//                        editedtaxprcnt_Rate = Double.parseDouble (salesQuo_modelArrayList.get (position).getTaxprcnt ());
//                        new GetDiscountAmnt ().execute ();
                    }
                    public void beforeTextChanged(CharSequence s, int start, int count, int after){

                    }
                    public void onTextChanged(CharSequence s, int start, int before, int count){

                    }
                });
                ((ViewHolder) holder).Discount.addTextChangedListener(new TextWatcher ()
                {
                    @Override
                    public void afterTextChanged(Editable mEdit)
                    {
                        edited_discount = mEdit.toString();
                        Log.e ("edited_discount",edited_discount);
                        salesQuo_modelArrayList.get(position).setDiscount (edited_discount);

//                        editedqty = Double.parseDouble (salesQuo_modelArrayList.get (position).getQuantity ());
//                        editedprice = Double.parseDouble (salesQuo_modelArrayList.get (position).getPrice ());
//                        editeddiscount = Double.parseDouble (salesQuo_modelArrayList.get (position).getDiscount ());
//                        editedtaxprcnt_Rate = Double.parseDouble (salesQuo_modelArrayList.get (position).getTaxprcnt ());
//                        new GetDiscountAmnt ().execute ();
                    }
                    public void beforeTextChanged(CharSequence s, int start, int count, int after){

                    }
                    public void onTextChanged(CharSequence s, int start, int before, int count){

                    }
                });
                ((ViewHolder) holder).Hike.addTextChangedListener(new TextWatcher ()
                {
                    @Override
                    public void afterTextChanged(Editable mEdit)
                    {
                        edited_Hike = mEdit.toString();
                        Log.e ("edited_Hike",edited_Hike);
                        salesQuo_modelArrayList.get(position).setHike (edited_Hike);

//                        editedqty = Double.parseDouble (salesQuo_modelArrayList.get (position).getQuantity ());
//                        editedprice = Double.parseDouble (salesQuo_modelArrayList.get (position).getPrice ());
//                        editeddiscount = Double.parseDouble (salesQuo_modelArrayList.get (position).getDiscount ());
//                        editedtaxprcnt_Rate = Double.parseDouble (salesQuo_modelArrayList.get (position).getTaxprcnt ());
//                        new GetDiscountAmnt ().execute ();
                    }
                    public void beforeTextChanged(CharSequence s, int start, int count, int after){

                    }
                    public void onTextChanged(CharSequence s, int start, int before, int count){

                    }
                });

                ((ViewHolder) holder).calculate_btn.setOnClickListener (new View.OnClickListener ( ) {
                    @Override
                    public void onClick(View view) {
                        calculateposition = String.valueOf (position);
                        calculate_status = "1";
                        editedqty = Double.parseDouble (salesQuo_modelArrayList.get (position).getQuantity ());
                        editedprice = Double.parseDouble (salesQuo_modelArrayList.get (position).getPrice ());
                        editeddiscount = Double.parseDouble (salesQuo_modelArrayList.get (position).getDiscount ());
                        Log.e ("getHike","DDD"+salesQuo_modelArrayList.get (position).getHike ());
                        if (salesQuo_modelArrayList.get (position).getHike () == null || salesQuo_modelArrayList.get (position).getHike ().equalsIgnoreCase ("")) {
                            editedHike = 0;
                        } else {
                            editedHike = Double.parseDouble (salesQuo_modelArrayList.get (position).getHike ( ));
                        }
                        editedtaxprcnt_Rate = Double.parseDouble (salesQuo_modelArrayList.get (position).getTaxprcnt ());
                        edited_itemcode = salesQuo_modelArrayList.get(position).getItemcode();
                        Log.e ("editedqty",""+editedqty);
                        Log.e ("editedprice",""+editedprice);
                        Log.e ("editeddiscount",""+editeddiscount);
                        Log.e ("editedHike",""+editedHike);
                        Log.e ("editedtaxprcnt_Rate",""+editedtaxprcnt_Rate);
                        new GetDiscountAmnt ().execute ();
                        finaltotal = 0;
                        finaldiscount = 0;
                        finaltotaltaxamnt = 0;
                        grandtotal = 0;
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return salesQuo_modelArrayList.size();
        }

        public  class ViewHolder extends RecyclerView.ViewHolder
        {
            LinearLayout editing_linearlayout;
            public TextView sno,canceltv,itemcode,UOM,LastQuotePrice,Total,HSNcode,Attachment,TotalTaxAmnt,TotalDiscAmnt,
                    projectname,priceafterDiscount;
            EditText name,Quantity,Price,Discount,Taxcode,WhseName,Hike;
            ImageButton deletebtn;
            ImageView editimage,editapprove;
            Button calculate_btn;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                editing_linearlayout= itemView.findViewById(R.id.editing_linearlayout);
                editimage= itemView.findViewById(R.id.editimage);
                editapprove= itemView.findViewById(R.id.editapprove);

                priceafterDiscount= itemView.findViewById(R.id.priceafterDiscount);
                projectname= itemView.findViewById(R.id.projectname);
                itemcode= itemView.findViewById(R.id.itemcode);
                name= itemView.findViewById(R.id.name);
                deletebtn= itemView.findViewById(R.id.deletebtn);
                Quantity= itemView.findViewById(R.id.Quantity);
                Price= itemView.findViewById(R.id.Price);
                Hike= itemView.findViewById(R.id.Hike);

                UOM= itemView.findViewById(R.id.UOM);
                LastQuotePrice= itemView.findViewById(R.id.LastQuotePrice);
                Discount= itemView.findViewById(R.id.Discount);
                Total= itemView.findViewById(R.id.Total);
                Taxcode= itemView.findViewById(R.id.Taxcode);
                WhseName= itemView.findViewById(R.id.WhseName);
                HSNcode= itemView.findViewById(R.id.HSNcode);
                Attachment= itemView.findViewById(R.id.Attachment);
                TotalTaxAmnt= itemView.findViewById(R.id.TotalTaxAmnt);
                TotalDiscAmnt= itemView.findViewById(R.id.TotalDiscAmnt);
                canceltv= itemView.findViewById(R.id.canceltv);
                calculate_btn= itemView.findViewById(R.id.calculate_btn);
            }
        }
    }

    private class AddQuotation extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(SalesQuotation.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_AddQuotation";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_AddQuotation";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_AddQuotation";
                Log.e("URL", URL);

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                if (DocNo.equalsIgnoreCase ("")) {
                    Request.addProperty ("DocNo", "" + 0);
                    Log.e ("DocNo", "0");
                } else if (btnclickstatus.equalsIgnoreCase("saveas")) {
                    Request.addProperty ("DocNo", "" + 0);
                    Log.e ("DocNo", ""+"0");
                } else {
                    Request.addProperty ("DocNo", "" + DocNo);
                    Log.e ("DocNo", ""+DocNo);
                }

                Request.addProperty("DocDate", ""+docdate_et.getText().toString().trim());
                Log.e("DocDate", ""+docdate_et.getText().toString().trim());

                Request.addProperty("ValidDate", ""+validdate_et.getText().toString().trim() );
                Log.e("ValidDate", "" +validdate_et.getText().toString().trim());

                Request.addProperty("SQType", "" + sqtype_et.getText().toString().trim());
                Log.e("SQType", "" + sqtype_et.getText().toString().trim());

                Request.addProperty("CustName", "" + customer_et.getText().toString().trim());
                Log.e("CustName", "" +customer_et.getText().toString().trim() );

                Request.addProperty("CustCode", "" + edtcuscode.getText().toString().trim());
                Log.e("CustCode", "" + edtcuscode.getText ().toString ().trim ());

                Request.addProperty("PlaceSupp", ""+placeofsupply_et.getText().toString().trim());
                Log.e("PlaceSupp",""+placeofsupply_et.getText().toString().trim());

                Request.addProperty("QuoteStatus", ""+quotestatus_et.getText().toString().trim());
                Log.e("QuoteStatus", ""+quotestatus_et.getText().toString().trim());

                Request.addProperty("User", ""+sessionempid);
                Log.e("User",""+sessionempid);

                Request.addProperty("SaleEmpName", ""+sessionempid);
                Log.e("SaleEmpName",""+sessionempid);

//                double totaldisc = Integer.parseInt(totaldisc_et.getText().toString().trim());
//                Log.e("totaldisc", ""+totaldisc);

//                Request.addProperty("TotalBefDisc", ""+totaldisc_et.getText().toString().trim());
//                Log.e("TotalBefDisc", ""+totaldisc_et.getText().toString().trim());
                Request.addProperty("TotalBefDisc", ""+finaldiscount);
                Log.e("TotalBefDisc", ""+finaldiscount);

                if (TaxAmt_AfterDiscount.equalsIgnoreCase("0")) {
                    Request.addProperty("TaxAmt", "" + totaltaxamnt_et.getText().toString().trim());
                    Log.e("TaxAmt", "" + totaltaxamnt_et.getText().toString().trim());
                } else {
                    Request.addProperty("TaxAmt", "" + TaxAmt_AfterDiscount);
                    Log.e("TaxAmt", "" + TaxAmt_AfterDiscount);
                }

                Request.addProperty("Total", ""+total_et.getText().toString().trim());
                Log.e("Total",""+total_et.getText().toString().trim());

                if (FinalAmt.equalsIgnoreCase("0")) {
                    Request.addProperty("GTotal", "" + grandtotal_et.getText().toString().trim());
                    Log.e("GTotal", "" + grandtotal_et.getText().toString().trim());
                } else {
                    Request.addProperty("GTotal", "" + FinalAmt);
                    Log.e("GTotal", "" + FinalAmt);
                }

                Request.addProperty("overalldiscamt", ""+overalldiscamt);
                Log.e("overalldiscamt",""+overalldiscamt);

                if (referenceno_et.getText().toString().trim().equalsIgnoreCase("")) {
                    Request.addProperty("ReferenceNo", ""+ 0);
                    Log.e("ReferenceNo",""+"0");
                } else {
                    Request.addProperty("ReferenceNo", ""+referenceno_et.getText().toString().trim());
                    Log.e("ReferenceNo",""+referenceno_et.getText().toString().trim());
                }

                Request.addProperty("Attachment1", "" +attach_Image);
                Log.e("Attachment1", ""+attach_Image);

                Request.addProperty("Attachment2", "" +attach_Image1);
                Log.e("Attachment2", ""+attach_Image1);

                String bookingitem="";
                for (int io = 0; io < salesQuo_modelArrayList.size(); io++) {

                    String xmlItemCode="",xmlItemName="",xmlUOM="",xmlQty="",xmlPrice="",xmlLastQPrice="",
                            xmlrowid="",xmlDiscount="",xmlPriceaftdisc="0",
                            xmlTotal="",xmlTaxcode="",xmlWhseCode="",xmlWhseName="",xmlHsnCode="",
                            xmlTaxprcnt="",xmlTaxAmt="",xmlDiscAmt="",xmlhikepercent="0.0",xmlProjectName="";

                    if (DocNo.equalsIgnoreCase ("")) {
                        xmlrowid = "0";
                    } else if (btnclickstatus.equalsIgnoreCase("saveas")) {
                        xmlrowid = "0";
                    } else {
                        xmlrowid = salesQuo_modelArrayList.get(io).getRowId();
                    }

                    xmlItemCode = salesQuo_modelArrayList.get(io).getItemcode();
                    xmlItemName = salesQuo_modelArrayList.get(io).getName();
                    xmlUOM = salesQuo_modelArrayList.get(io).getUom();
                    xmlQty = salesQuo_modelArrayList.get(io).getQuantity();
                    xmlPrice = salesQuo_modelArrayList.get(io).getPrice();
                    xmlLastQPrice = salesQuo_modelArrayList.get(io).getLastquotprice();
                    xmlDiscount = salesQuo_modelArrayList.get(io).getDiscount();
                    Log.e("xmlPriceaftdisc" , "XXX" + salesQuo_modelArrayList.get(io).getPriceofDis());

                    if (salesQuo_modelArrayList.get(io).getPriceofDis () == null) {
                        xmlPriceaftdisc = "0";
                    } else {
                        xmlPriceaftdisc = salesQuo_modelArrayList.get(io).getPriceofDis();
                    }

                    xmlTotal = salesQuo_modelArrayList.get(io).getTotal();
                    xmlTaxcode = salesQuo_modelArrayList.get(io).getTaxcode();
                    xmlWhseCode = salesQuo_modelArrayList.get(io).getWhseCode();
                    xmlWhseName = salesQuo_modelArrayList.get(io).getWhseName();
                    xmlHsnCode = salesQuo_modelArrayList.get(io).getHsncode();
                    if (salesQuo_modelArrayList.get(io).getTaxprcnt ().equalsIgnoreCase ("")) {
                        xmlTaxprcnt = "0";
                    } else {
                        xmlTaxprcnt = salesQuo_modelArrayList.get (io).getTaxprcnt();
                    }
                    xmlTaxAmt = salesQuo_modelArrayList.get(io).getTotalTaxamnt();

                    Log.e ("getTotalDiscountamnt",""+salesQuo_modelArrayList.get(io).getTotalDiscountamnt());
                    if (salesQuo_modelArrayList.get(io).getTotalDiscountamnt () == null) {
                        xmlDiscAmt = "0";
                    } else {
                        xmlDiscAmt = salesQuo_modelArrayList.get(io).getTotalDiscountamnt();
                    }

                    if (editstatus.equalsIgnoreCase("1") || editstatus.equalsIgnoreCase("2")) {
                        Log.e("editstatus", "in xml document" + editstatus);
                    } else {
                        if (salesQuo_modelArrayList.get(io).getHike().equalsIgnoreCase("")) {
                            xmlhikepercent = "0.0";
                        } else {
                            xmlhikepercent = salesQuo_modelArrayList.get(io).getHike();
                        }
                        xmlProjectName = salesQuo_modelArrayList.get(io).getProjectName();
                    }

                    int Empid= Integer.parseInt(sessionempid);
                    String date = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
                    bookingitem = bookingitem + "<Table1><RowId>"+xmlrowid+"</RowId><ItemCode>"+ xmlItemCode +"</ItemCode><ItemName>" + xmlItemName + "</ItemName><UOM>" + xmlUOM + "</UOM><Qty>" + xmlQty + "</Qty><Price>" + xmlPrice + "</Price><LastQPrice>" + xmlLastQPrice + "</LastQPrice>" +
                            "<Discount>" + xmlDiscount + "</Discount><Priceaftdisc>" + xmlPriceaftdisc + "</Priceaftdisc><Total>" + xmlTotal + "</Total>" +
                            "<Taxcode>" + xmlTaxcode + "</Taxcode><WhseCode>" + xmlWhseCode + "</WhseCode><WhseName>" + xmlWhseName + "</WhseName><HsnCode>" + xmlHsnCode + "</HsnCode>" +
                            "<Taxprcnt>" + xmlTaxprcnt + "</Taxprcnt><TaxAmt>" + xmlTaxAmt + "</TaxAmt><DiscAmt>" + xmlDiscAmt + "</DiscAmt><hikepercent>" + xmlhikepercent + "</hikepercent><ProjectName>" + xmlProjectName + "</ProjectName></Table1>";
                }
                bookingitem = "<NewDataSet>" + bookingitem + "</NewDataSet>";

                Request.addProperty("ItemDetailXML", ""+bookingitem);
                Log.e("ItemDetailXML", ""+bookingitem);

                Request.addProperty("ItemDetailXMLID", ""+1);
                Log.e("ItemDetailXMLID",""+1 );

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(Request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                androidHttpTransport.call(SOAP_ACTION, envelope);
                strfault = "";
                if (envelope.bodyIn instanceof SoapFault) {
                    strfault = ( (SoapFault) envelope.bodyIn ).faultstring;
                    Log.i("Error", strfault);
                } else {
                    SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                    Log.e("WS", String.valueOf(resultsRequestSOAP));
                }
                SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                launch = ( "" + resultsRequestSOAP.getProperty(0));
            } catch (Exception exg) {
                exg.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (launch.equalsIgnoreCase("1")) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SalesQuotation.this);
                builder.setMessage(" Saved ")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
//                                new AddQuotation_Approval_Status_Sending (). execute ();
                                new AddQuotation_Approval_Status_Sending (). execute ();
                                dialog.cancel();
                                finish();
                            }
                        });
                android.app.AlertDialog alert = builder.create();
                alert.show();
            } else if (launch.equalsIgnoreCase("2")) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SalesQuotation.this);
                builder.setMessage(" Updated ")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                new CRMToSAPSalesQuotation ().execute ();
                                startActivity (new Intent (SalesQuotation.this, ViewQuotation.class));
                                dialog.cancel();
                                finish();
                            }
                        });
                android.app.AlertDialog alert = builder.create();
                alert.show();
            } else {
                android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(SalesQuotation.this);
                builder1.setMessage("Something Went Wrong");
                builder1.setCancelable(false);
                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        }
    }

    private class AddQuotation_Approval extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SalesQuotation.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_AddQuotation_Approval";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_AddQuotation_Approval";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_AddQuotation_Approval";
                Log.e("URL", URL);

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty ("DocNo", "" + DocNo);
                Log.e ("DocNo", "" + DocNo);

                Request.addProperty("User", "" +sessionempid);
                Log.e("User", ""+sessionempid);

                if (Approvestatus.equalsIgnoreCase("A")) {
                    Request.addProperty("AppStatus", ""+Approvestatus);
                    Log.e("AppStatus", ""+Approvestatus);
                } else {
                    Request.addProperty("AppStatus", ""+Approvestatus);
                    Log.e("AppStatus", ""+Approvestatus);
                }

                Request.addProperty("AppRemarks", ""+ApproveRemarks);
                Log.e("AppRemarks", ""+ApproveRemarks);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(Request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                androidHttpTransport.call(SOAP_ACTION, envelope);
                strfault = "";
                if (envelope.bodyIn instanceof SoapFault) {
                    strfault = ( (SoapFault) envelope.bodyIn ).faultstring;
                    Log.i("Error", strfault);
                } else {
                    SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                    Log.e("WS", String.valueOf(resultsRequestSOAP));
                }
                SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                launch = ( "" + resultsRequestSOAP.getProperty(0));
            } catch (Exception exg) {
                exg.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (launch.equalsIgnoreCase("1")) {
                if(Approvestatus.equalsIgnoreCase("A")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SalesQuotation.this);
                    builder.setMessage(" Approved ")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    startActivity(new Intent(SalesQuotation.this, ViewQuotationApproval.class));
                                    salesQuo_modelArrayList.clear();
                                    new CRMToSAPSalesQuotation ().execute ();
                                    finish();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SalesQuotation.this);
                    builder.setMessage(" Rejected ")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    startActivity(new Intent(SalesQuotation.this, ViewQuotationApproval.class));
                                    salesQuo_modelArrayList.clear();
                                    new CRMToSAPSalesQuotation ().execute ();
                                    finish();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }  else {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(SalesQuotation.this);
                builder1.setMessage("Something Went Wrong");
                builder1.setCancelable(false);
                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        }
    }

    private class AddQuotation_Approval_Status_Sending extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(SalesQuotation.this);
//            pDialog.setCancelable(false);
//            pDialog.setMessage("Loading...");
//            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSALES_Update_Quote_ApprStatus";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSALES_Update_Quote_ApprStatus";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSALES_Update_Quote_ApprStatus";
                Log.e("URL", URL);

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(Request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                androidHttpTransport.call(SOAP_ACTION, envelope);
                strfault = "";
                if (envelope.bodyIn instanceof SoapFault) {
                    strfault = ( (SoapFault) envelope.bodyIn ).faultstring;
                    Log.i("Error", strfault);
                } else {
                    SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                    Log.e("WS", String.valueOf(resultsRequestSOAP));
                }
                SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                launch = ( "" + resultsRequestSOAP.getProperty(0));
            } catch (Exception exg) {
                exg.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
//            pDialog.dismiss();
            if (launch.equalsIgnoreCase("1")) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(SalesQuotation.this);
//                builder.setMessage("")
//                        .setCancelable(false)
//                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                                salesQuo_modelArrayList.clear();
//                                finish();
//                            }
//                        });
//                AlertDialog alert = builder.create();
//                alert.show();
                new CRMToSAPSalesQuotation ().execute ();
                Log.e("UPDATED","APPROVESTATUS"+ launch);
            } else {
                Log.e("NOTUPDATED","APPROVESTATUS"+ launch);
//                AlertDialog.Builder builder1 = new AlertDialog.Builder(SalesQuotation.this);
//                builder1.setMessage("Something Went Wrong");
//                builder1.setCancelable(false);
//                builder1.setPositiveButton(
//                        "OK",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//                AlertDialog alert11 = builder1.create();
//                alert11.show();
            }
        }
    }

    private class CRMToSAPSalesQuotation extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(SalesQuotation.this);
//            pDialog.setCancelable(false);
//            pDialog.setMessage("Loading...");
//            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "SalesQuotation";
                final String SOAP_ACTION = "http://tempuri.org/SalesQuotation";
                final String URL = "http://103.48.182.209:92/CRMToSAP.asmx?op=SalesQuotation";
                Log.e("URL", URL);

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty ("SOxml_str", "1");
                Log.e ("SOxml_str","" + "1");

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(Request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                androidHttpTransport.call(SOAP_ACTION, envelope);
                strfault = "";
                if (envelope.bodyIn instanceof SoapFault) {
                    strfault = ( (SoapFault) envelope.bodyIn ).faultstring;
                    Log.i("Error", strfault);
                } else {
                    SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                    Log.e("WS", String.valueOf(resultsRequestSOAP));
                }
                SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                launch = ( "" + resultsRequestSOAP.getProperty(0));
            } catch (Exception exg) {
                exg.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
//            pDialog.dismiss();
            Log.e ("launch","XXX" + launch);
            if (launch.equalsIgnoreCase("True")) {
                Log.e("UPDATED","CRMToSAP_SalesQuotation"+ launch);
            } else {
                Log.e("NOTUPDATED","CRMToSAP_SalesQuotation"+ launch);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}