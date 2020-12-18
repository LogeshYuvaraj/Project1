package com.example.ravelelectronics;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.ravelelectronics.model.CategoryModel;
import com.example.ravelelectronics.model.ItemModel;
import com.example.ravelelectronics.model.OpportunityModel;
import com.example.ravelelectronics.model.PresalesModel;
import com.example.ravelelectronics.model.ProjectionHeaderModel;
import com.example.ravelelectronics.model.StatusModel;
import com.example.ravelelectronics.model.TypeModel;
import com.example.ravelelectronics.model.ViewPresalesModel;
import com.example.ravelelectronics.spinnerclasses.PresalesCategoryList_Spinner;
import com.example.ravelelectronics.spinnerclasses.ItemList_Spinner;
import com.example.ravelelectronics.spinnerclasses.OppurtunityTypeList_Spinner;
import com.example.ravelelectronics.spinnerclasses.StatusTypeList_Spinner;
import com.example.ravelelectronics.spinnerclasses.TypeList_Spinner;
import com.example.ravelelectronics.util.ConnectivityReceiver;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PresalesActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText itemname_et,type_et,category_et,oppurtunitytype_et,statustype_et;
    TextView oppor_Tv,edittv;
    EditText name_et,quantity_et,Price_et;
    EditText cDate_et,DocDate_et,potenamnt_et, closing_et, startdate_et, enddate_et, statusdate_et, reported_et, rempname_et, empdes_et,
            comdate_et, weightedamt_et, resdate_et, verifidate_et, grosspro_et, Resempname_et, Rempdesig_et, verifiemp_et,itemcode_et;
    MaterialSpinner typespinner, catogeryspinner, opportypespinner, statusspinner,itemspin;
    String PresalesCategory = "",PresalesOppType="",PresalesStatus="",PresalesType="";
    ProgressDialog pDialog;
    EditText Quantity_et,price_et;
    String PotentialAmt="0";
    String WeightedAmt="0";
    String GrossProfit="0";
    String HeaderDocno,HeaderDocDate,Headertype,HeaderOppType,Headercategory,
    HeaderTargetStartDate,HeaderTargetEndDate,HeaderTargetStatusDate,HeaderTargetClDate,HeaderPotentialAmt,
    HeaderTargetWeightedAmt,HeaderGrossProfit,HeaderReported,HeaderRepEmpName,HeaderRepEmpDesig,
    HeaderRespDate,HeaderRespEmpName,HeaderRespEmpDesig,HeaderVerifyDate,HeaderVerifyEmpName;

    String position1;

    Button save,cancel,add,clear;
    SessionManagement session;

    String sessionempname="",sessionempid="";
    RecyclerView presalesRecycle;
    RecyclerView.Adapter Adapter;
    ArrayList<PresalesModel> presalesModelArrayList;

    String Itemspin="";
    DatePickerDialog dp;
    DatePickerDialog dp1;
    DatePickerDialog dp2;
    DatePickerDialog dp3;
    DatePickerDialog dp4;
    DatePickerDialog dp5;
    DatePickerDialog dp6;
    String itemcode="",itemname="",SalUnitMsr="",lastquoteprice="",HSN="",Uom="";
    String Rowid="",docno="",Itemcode="",Itemname="",uom="",qty="",price="",CreatedBy="",CreatedDate="";


    List<String> ItemModel1 = new ArrayList<>();
    ArrayList<ItemModel> itemModelArrayList;

    List<String> CategoryModel1 = new ArrayList<>();
    ArrayList<CategoryModel> categoryModelArrayList;

    List<String> OpportunityModel1 = new ArrayList<>();
    ArrayList<OpportunityModel> opportunityModelArrayList;

    List<String> StatusModel1 = new ArrayList<>();
    ArrayList<StatusModel> statusModelArrayList;

    List<String> TypeModel1 = new ArrayList<>();
    ArrayList<TypeModel> typeModelArrayList;

    String strfault = "", launch = "";
    String editstatus="",DocNo="";
    JSONArray jsonArray1,jsonArray2,jsonArray3,jsonArray4,jarraylist,jsonArray6;
    ArrayList<PresalesHeaderModel> presalesHeaderModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presales);

        itemname_et = findViewById(R.id.itemname_et);
        type_et = findViewById(R.id.type_et);
        category_et = findViewById(R.id.category_et);
        oppurtunitytype_et = findViewById(R.id.oppurtunitytype_et);
        statustype_et = findViewById(R.id.statustype_et);
        name_et = findViewById(R.id.name);
        quantity_et = findViewById(R.id.quantity);
        Price_et = findViewById(R.id.Price);
        cDate_et = findViewById(R.id.cDate_et);

        itemname_et.setOnClickListener (this);
        type_et.setOnClickListener (this);
        category_et.setOnClickListener (this);
        oppurtunitytype_et.setOnClickListener (this);
        statustype_et.setOnClickListener (this);
        presalesModelArrayList = new ArrayList<>();

        oppor_Tv = (TextView) findViewById(R.id.oppor_Tv);
        potenamnt_et = findViewById(R.id.potenamnt_et);
        DocDate_et = (EditText) findViewById(R.id.DocDate_et);
        itemcode_et =  findViewById(R.id.itemcode_et);

        startdate_et = (EditText) findViewById(R.id.startdate_et);
        enddate_et = (EditText) findViewById(R.id.enddate_et);
        statusdate_et = (EditText) findViewById(R.id.statusdate_et);
        reported_et = (EditText) findViewById(R.id.reported_et);
        rempname_et = (EditText) findViewById(R.id.rempname_et);
        empdes_et = (EditText) findViewById(R.id.empdes_et);
        comdate_et = (EditText) findViewById(R.id.comdate_et);
        weightedamt_et = (EditText) findViewById(R.id.weightedamt_et);
        resdate_et = (EditText) findViewById(R.id.resdate_et);
        verifidate_et = (EditText) findViewById(R.id.verifidate_et);
        grosspro_et = (EditText) findViewById(R.id.grosspro_et);
        Resempname_et = (EditText) findViewById(R.id.Resempname_et);
        Rempdesig_et = (EditText) findViewById(R.id.Rempdesig_et);
        verifiemp_et = (EditText) findViewById(R.id.verifiemp_et);
        typespinner = findViewById(R.id.typespinner);
        catogeryspinner =findViewById(R.id.catogeryspinner);
        opportypespinner =  findViewById(R.id.opportypespinner);
        statusspinner = findViewById(R.id.statusspinner);
        Quantity_et=findViewById(R.id.Quantity_et);
        price_et=findViewById(R.id.price_et);
        itemspin=findViewById(R.id.itemspin);

        save=(Button)findViewById(R.id.save);
        cancel=(Button)findViewById(R.id.cancel);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        add=(Button)findViewById(R.id.add);
        clear=(Button)findViewById(R.id.clear);
        save.setOnClickListener(this);
        add.setOnClickListener(this);
        clear.setOnClickListener(this);

        edittv=findViewById(R.id.edittv);

//        itemspin.setOnItemSelectedListener(this);
        presalesRecycle =findViewById(R.id.presalesRecycle);

        Intent intent=getIntent();
        DocNo=intent.getStringExtra("DocNo");
        editstatus=intent.getStringExtra("editstatus");

        Log.e("editstatus",""+editstatus);

        categoryModelArrayList = new ArrayList<>();
        opportunityModelArrayList = new ArrayList<>();
        statusModelArrayList = new ArrayList<>();
        typeModelArrayList = new ArrayList<>();
        itemModelArrayList = new ArrayList<>();

        String date = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
        DocDate_et.setText(date);
        presalesModelArrayList = new ArrayList<>();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small> Presales </small>"));
        }
        session = new SessionManagement(PresalesActivity.this);
        HashMap<String, String> users = session.getUserDetails();
        sessionempname = users.get(SessionManagement.KEY_NAME);
        sessionempid = users.get(SessionManagement.KEY_CODE);

        catogeryspinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                PresalesCategory=item.toString();
                Log.e("PresalesCategory",PresalesCategory);
            }
        });
        if (editstatus.equalsIgnoreCase("0")){
            Log.e("From","Dashboard");
            edittv.setVisibility (View.GONE);
        } else {
            save.setText("Update");
            new PresalesHeader().execute();
            edittv.setVisibility (View.VISIBLE);
            Log.e("From","Edit");
        }

        opportypespinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                PresalesOppType=item.toString();
                Log.e("PresalesOppType",PresalesOppType);
            }
        });


        statusspinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                PresalesStatus=item.toString();
                Log.e("PresalesStatus",PresalesStatus);
            }
        });

        typespinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                PresalesType=item.toString();
                Log.e("PresalesType",PresalesType);
            }
        });


        itemspin.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                itemname = item.toString();
                Log.e("itemname", itemname);
                itemcode = itemModelArrayList.get(position).getItemcode();
                Log.e("itemcode", itemcode);
                SalUnitMsr = itemModelArrayList.get(position).getSalUnitMsr();
                Log.e("SalUnitMsr", SalUnitMsr);
                lastquoteprice = itemModelArrayList.get(position).getLastquoteprice();
                Log.e("lastquoteprice", lastquoteprice);
                HSN = itemModelArrayList.get(position).getHSN();
                Log.e("HSN", HSN);
                Uom = itemModelArrayList.get(position).getUom();
                Log.e("Uom", Uom);
                itemcode_et.setText(itemcode);
            }
        });

        cDate_et.setFocusable(false);
        startdate_et.setFocusable(false);
        enddate_et.setFocusable(false);
        statusdate_et.setFocusable(false);
        comdate_et.setFocusable(false);
        resdate_et.setFocusable(false);
        verifidate_et.setFocusable(false);

        cDate_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                final int y = c.get(Calendar.YEAR);
                final int m = c.get(Calendar.MONTH);
                final int d = c.get(Calendar.DAY_OF_MONTH);

                dp2 = new DatePickerDialog(PresalesActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                Calendar dateSelected = Calendar.getInstance();
                                dateSelected.set(year, monthOfYear, dayOfMonth);

                                SimpleDateFormat formatterfrm = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);

                                cDate_et.setText(formatterfrm.format(dateSelected.getTime()));
                                Log.d("closingdate", cDate_et.getText().toString());
                            }

                        }, y, m, d);
                dp2.show();
            }
        });

        resdate_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                final int y = c.get(Calendar.YEAR);
                final int m = c.get(Calendar.MONTH);
                final int d = c.get(Calendar.DAY_OF_MONTH);

                dp5 = new DatePickerDialog(PresalesActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                Calendar dateSelected = Calendar.getInstance();
                                dateSelected.set(year, monthOfYear, dayOfMonth);

                                SimpleDateFormat formatterfrm = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);

                                resdate_et.setText(formatterfrm.format(dateSelected.getTime()));
                                Log.d("resdate", resdate_et.getText().toString());
                            }

                        }, y, m, d);
                dp5.show();
            }
        });

        verifidate_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                final int y = c.get(Calendar.YEAR);
                final int m = c.get(Calendar.MONTH);
                final int d = c.get(Calendar.DAY_OF_MONTH);

                dp6 = new DatePickerDialog(PresalesActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                Calendar dateSelected = Calendar.getInstance();
                                dateSelected.set(year, monthOfYear, dayOfMonth);

                                SimpleDateFormat formatterfrm = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);

                                verifidate_et.setText(formatterfrm.format(dateSelected.getTime()));
                                Log.d("verifidate", verifidate_et.getText().toString());
                            }

                        }, y, m, d);
                dp6.show();
            }
        });

        comdate_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                final int y = c.get(Calendar.YEAR);
                final int m = c.get(Calendar.MONTH);
                final int d = c.get(Calendar.DAY_OF_MONTH);

                dp4 = new DatePickerDialog(PresalesActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                Calendar dateSelected = Calendar.getInstance();
                                dateSelected.set(year, monthOfYear, dayOfMonth);

                                SimpleDateFormat formatterfrm = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);

                                comdate_et.setText(formatterfrm.format(dateSelected.getTime()));
                                Log.d("comdate", comdate_et.getText().toString());
                            }

                        }, y, m, d);
                dp4.show();
            }
        });
        startdate_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                final int y = c.get(Calendar.YEAR);
                final int m = c.get(Calendar.MONTH);
                final int d = c.get(Calendar.DAY_OF_MONTH);

                dp = new DatePickerDialog(PresalesActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                Calendar dateSelected = Calendar.getInstance();
                                dateSelected.set(year, monthOfYear, dayOfMonth);

                                SimpleDateFormat formatterfrm = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);

                                startdate_et.setText(formatterfrm.format(dateSelected.getTime()));
                                Log.d("Fromdate", startdate_et.getText().toString());
                            }

                        }, y, m, d);
                dp.show();
            }
        });

        enddate_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                final int y = c.get(Calendar.YEAR);
                final int m = c.get(Calendar.MONTH);
                final int d = c.get(Calendar.DAY_OF_MONTH);

                dp1 = new DatePickerDialog(PresalesActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                Calendar dateSelected = Calendar.getInstance();
                                dateSelected.set(year, monthOfYear, dayOfMonth);

                                Calendar currentDate = Calendar.getInstance();

                                SimpleDateFormat formatterfrm = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);

                                enddate_et.setText(formatterfrm.format(dateSelected.getTime()));
                                Log.d("Todate", enddate_et.getText().toString());
                            }

                        }, y, m, d);
                dp1.show();
            }
        });

        statusdate_et.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final Calendar c = Calendar.getInstance();

            final int y = c.get(Calendar.YEAR);
            final int m = c.get(Calendar.MONTH);
            final int d = c.get(Calendar.DAY_OF_MONTH);

            dp3 = new DatePickerDialog(PresalesActivity.this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            Calendar dateSelected = Calendar.getInstance();
                            dateSelected.set(year, monthOfYear, dayOfMonth);

                            SimpleDateFormat formatterfrm = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);

                            statusdate_et.setText(formatterfrm.format(dateSelected.getTime()));
                            Log.d("statusdate", statusdate_et.getText().toString());

                        }

                    }, y, m, d);
            dp3.show();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && data != null) {
            try {
                itemname = data.getStringExtra ("itemname");
                itemcode = data.getStringExtra ("itemcode");
                Uom = data.getStringExtra ("uom");
                HSN = data.getStringExtra ("hsn");
                lastquoteprice = data.getStringExtra ("lastquoteprice");
                SalUnitMsr = data.getStringExtra ("SalUnitMsr");

                String edititemadapterstatus="";
                edititemadapterstatus=data.getStringExtra("edititemadapterstatus");
                position1=data.getStringExtra("position1");
                if (edititemadapterstatus.equalsIgnoreCase("edit")){
                    Adapter.notifyItemChanged(Integer.parseInt(position1));
                }else {
                    itemname_et.setText("" + itemname);
                    itemcode_et.setText("" + itemcode);
                }
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (requestCode == 2 && data != null) {
            try {
                PresalesType = data.getStringExtra ("PresalesType");
                type_et.setText(""+PresalesType);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (requestCode == 3 && data != null) {
            try {
                PresalesCategory = data.getStringExtra ("PresalesCategory");
                category_et.setText(""+PresalesCategory);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (requestCode == 4 && data != null) {
            try {
                PresalesOppType = data.getStringExtra ("PresalesOppType");
                oppurtunitytype_et.setText(""+PresalesOppType);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (requestCode == 5 && data != null) {
            try {
                PresalesStatus = data.getStringExtra ("PresalesStatus");
                statustype_et.setText(""+PresalesStatus);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onClick ( View v) {
        if (v == itemname_et) {
            Intent intent = new Intent (PresalesActivity.this, ItemList_Spinner.class);
            intent.putExtra ("ScreenStatus","PA");
            intent.putExtra ("position1","");
            startActivityForResult(intent, 1);
        }
        if (v == type_et) {
            Intent intent = new Intent (PresalesActivity.this, TypeList_Spinner.class);
            intent.putExtra ("ScreenStatus","PA");
            startActivityForResult(intent, 2);
        }
        if (v == category_et) {
            Intent intent = new Intent (PresalesActivity.this, PresalesCategoryList_Spinner.class);
            intent.putExtra ("ScreenStatus","PA");
            startActivityForResult(intent, 3);
        }
        if (v == oppurtunitytype_et) {
            Intent intent = new Intent (PresalesActivity.this, OppurtunityTypeList_Spinner.class);
            intent.putExtra ("ScreenStatus","PA");
            startActivityForResult(intent, 4);
        }
        if (v == statustype_et) {
            Intent intent = new Intent (PresalesActivity.this, StatusTypeList_Spinner.class);
            intent.putExtra ("ScreenStatus","PA");
            startActivityForResult(intent, 5);
        }
        if (add==v) {

            Log.e("XXX", "YYY"+potenamnt_et.getText().toString().trim() );
            Log.e("XXX", "YYY"+weightedamt_et.getText().toString().trim());
            Log.e("XXX", "YYY"+grosspro_et.getText().toString().trim() );
            if(Quantity_et.getText().toString().trim().equalsIgnoreCase("")){
                Toast.makeText(this, "Kindly enter Qty", Toast.LENGTH_SHORT).show();
            }else  if(price_et.getText().toString().trim().equalsIgnoreCase("")){
                Toast.makeText(this, "Kindly enter Price", Toast.LENGTH_SHORT).show();
            }else  if(category_et.getText().toString().trim().equalsIgnoreCase("")){
                Toast.makeText(this, "Kindly Select Category", Toast.LENGTH_SHORT).show();
            }else  if(oppurtunitytype_et.getText().toString().trim().equalsIgnoreCase("")){
                Toast.makeText(this, "Kindly Select OppType", Toast.LENGTH_SHORT).show();
            }else  if(statustype_et.getText().toString().trim().equalsIgnoreCase("")){
                Toast.makeText(this, "Kindly Select Status", Toast.LENGTH_SHORT).show();
            }else  if(type_et.getText().toString().trim().equalsIgnoreCase("")){
                Toast.makeText(this, "Kindly Select Type", Toast.LENGTH_SHORT).show();
            }else  if(itemname_et.getText().toString().trim().equalsIgnoreCase("")){
                Toast.makeText(this, "Kindly Select Item", Toast.LENGTH_SHORT).show();
            }else {

                PotentialAmt=potenamnt_et.getText().toString().trim();
                WeightedAmt=weightedamt_et.getText().toString().trim();
                GrossProfit=grosspro_et.getText().toString().trim();
                Log.e("XXX", "SSS"+PotentialAmt);
                Log.e("XXX", "SSS"+WeightedAmt);
                Log.e("XXX", "SSS"+GrossProfit);

                PresalesModel Model = new PresalesModel();
                Model.setRowId("0");
                Model.setItemcode(itemcode);
                Model.setItemcode(itemcode);
                Model.setName(itemname);
                Model.setQuantity(Quantity_et.getText().toString().trim());
                Model.setPrice(price_et.getText().toString().trim());
                Model.setUom(Uom);
                presalesModelArrayList.add(Model);

                presalesRecycle.setVisibility(View.VISIBLE);
                presalesRecycle.setLayoutManager(new LinearLayoutManager(PresalesActivity.this));
                Adapter = new PresalesAdapter(getApplicationContext(), presalesModelArrayList);
                presalesRecycle.setAdapter(Adapter);
                presalesRecycle.setHasFixedSize(true);

                Quantity_et.setText("");
                price_et.setText("");
//                potenamnt_et.setText("");
//                grosspro_et.setText("");
//                weightedamt_et.setText("");
            }
        }
        if (v == save) {
            if (presalesModelArrayList.size()==0){
                Toast.makeText(this, "Atleast Add one Row Go to save", Toast.LENGTH_SHORT).show();
            }
            else {
                if (ConnectivityReceiver.isConnected(PresalesActivity.this)) {

                    new AddPresales().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Check Your Internet Connection!!!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (v==clear){
            presalesModelArrayList.clear();
            Adapter.notifyDataSetChanged();
            onRestart();
        }
        if (v==cancel)
        {
            finish();
        }
    }

    private class GetType extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PresalesActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Login...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSALES_GetPresalesType";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSALES_GetPresalesType";
                final String URL =  "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSALES_GetPresalesType";
                Log.e("URL", URL);
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);



                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                AndroidHttpTransport aht = new AndroidHttpTransport(URL);
                aht.call(SOAP_ACTION, soapEnvelope);
                SoapPrimitive resultString = (SoapPrimitive) soapEnvelope.getResponse();

                String responseJSON = resultString.toString();
                Log.e("RESPONSE", responseJSON);

                jsonArray4 = new JSONArray(responseJSON);
                for (int i = 0; i < jsonArray4.length(); i++) {

                    JSONObject jsonObject = jsonArray4.getJSONObject(i);
                    TypeModel model = new TypeModel ();
                    model.setPresalesType (jsonObject.getString ("PresalesType"));

                    typeModelArrayList.add (model);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();

            if(jsonArray4.length() == 0){
                Toast.makeText (PresalesActivity.this, "No List", Toast.LENGTH_SHORT).show ( );
            } else {
                Log.e ("PresalesType", "" + typeModelArrayList.size ());
                for (int i = 0;i<typeModelArrayList.size();i++) {

                    TypeModel1.add (typeModelArrayList.get (i).getPresalesType ());
                    Log.e ("PresalesType", typeModelArrayList.get (i).getPresalesType ());
                }

                ArrayAdapter d = new ArrayAdapter(PresalesActivity.this,android.R.layout.simple_spinner_dropdown_item, TypeModel1);
                typespinner.setAdapter(d);


            }
            if (ConnectivityReceiver.isConnected(PresalesActivity.this)) {
                new GetCategory().execute();
            } else {
                Toast.makeText(getApplicationContext(), "Check Your Internet Connection!!!", Toast.LENGTH_SHORT).show();
            }


        }

    }

    private class GetCategory extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PresalesActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Login...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSALES_GetPresalesCategory";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSALES_GetPresalesCategory";
                final String URL =  "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSALES_GetPresalesCategory";
                Log.e("URL", URL);
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);



                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                AndroidHttpTransport aht = new AndroidHttpTransport(URL);
                aht.call(SOAP_ACTION, soapEnvelope);
                SoapPrimitive resultString = (SoapPrimitive) soapEnvelope.getResponse();

                String responseJSON = resultString.toString();
                Log.e("RESPONSE", responseJSON);

                jsonArray1 = new JSONArray(responseJSON);
                for (int i = 0; i < jsonArray1.length(); i++) {

                    JSONObject jsonObject = jsonArray1.getJSONObject(i);
                    CategoryModel model = new CategoryModel ();
                    model.setPresalesCategory (jsonObject.getString ("PresalesCategory"));

                    categoryModelArrayList.add (model);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if(jsonArray1.length() == 0){
                Toast.makeText (PresalesActivity.this, "No List", Toast.LENGTH_SHORT).show ( );
            } else {
                Log.e ("Action", "" + categoryModelArrayList.size ());
                for (int i = 0;i<categoryModelArrayList.size();i++) {

                    CategoryModel1.add (categoryModelArrayList.get (i).getPresalesCategory ());
                    Log.e ("Action", categoryModelArrayList.get (i).getPresalesCategory ());
                }

                ArrayAdapter d = new ArrayAdapter(PresalesActivity.this,android.R.layout.simple_spinner_dropdown_item, CategoryModel1);
                catogeryspinner.setAdapter(d);
            }
            if (ConnectivityReceiver.isConnected(PresalesActivity.this)) {
                new GetOpportunity().execute();
            } else {
                Toast.makeText(getApplicationContext(), "Check Your Internet Connection!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class GetOpportunity extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PresalesActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Login...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSALES_GetPresalesOppType";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSALES_GetPresalesOppType";
                final String URL =  "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSALES_GetPresalesOppType";
                Log.e("URL", URL);
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                AndroidHttpTransport aht = new AndroidHttpTransport(URL);
                aht.call(SOAP_ACTION, soapEnvelope);
                SoapPrimitive resultString = (SoapPrimitive) soapEnvelope.getResponse();

                String responseJSON = resultString.toString();
                Log.e("RESPONSE", responseJSON);

                jsonArray2 = new JSONArray(responseJSON);
                for (int i = 0; i < jsonArray2.length(); i++) {

                    JSONObject jsonObject = jsonArray2.getJSONObject(i);
                    OpportunityModel model = new OpportunityModel ();
                    model.setPresalesOppType (jsonObject.getString ("PresalesOppType"));

                    opportunityModelArrayList.add (model);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();

            if(jsonArray2.length() == 0){
                Toast.makeText (PresalesActivity.this, "No List", Toast.LENGTH_SHORT).show ( );
            } else {
                Log.e ("PresalesOppType", "" + opportunityModelArrayList.size ());
                for (int i = 0;i<opportunityModelArrayList.size();i++) {

                    OpportunityModel1.add (opportunityModelArrayList.get (i).getPresalesOppType ());
                    Log.e ("PresalesOppType", opportunityModelArrayList.get (i).getPresalesOppType ());
                }

                ArrayAdapter d = new ArrayAdapter(PresalesActivity.this,android.R.layout.simple_spinner_dropdown_item, OpportunityModel1);
                opportypespinner.setAdapter(d);

            }
            if (ConnectivityReceiver.isConnected(PresalesActivity.this)) {
                new GetStatus().execute();
            } else {
                Toast.makeText(getApplicationContext(), "Check Your Internet Connection!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class GetStatus extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PresalesActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Login...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSALES_GetPresalesStatus";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSALES_GetPresalesStatus";
                final String URL =  "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSALES_GetPresalesStatus";
                Log.e("URL", URL);
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                AndroidHttpTransport aht = new AndroidHttpTransport(URL);
                aht.call(SOAP_ACTION, soapEnvelope);
                SoapPrimitive resultString = (SoapPrimitive) soapEnvelope.getResponse();

                String responseJSON = resultString.toString();
                Log.e("RESPONSE", responseJSON);

                jsonArray3 = new JSONArray(responseJSON);
                for (int i = 0; i < jsonArray3.length(); i++) {

                    JSONObject jsonObject = jsonArray3.getJSONObject(i);
                    StatusModel model = new StatusModel ();
                    model.setPresalesStatus (jsonObject.getString ("PresalesStatus"));

                    statusModelArrayList.add (model);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();

            if(jsonArray3.length() == 0){
                Toast.makeText (PresalesActivity.this, "No List", Toast.LENGTH_SHORT).show ( );
            } else {
                Log.e ("PresalesStatus", "" + statusModelArrayList.size ());
                for (int i = 0;i<statusModelArrayList.size();i++) {

                    StatusModel1.add (statusModelArrayList.get (i).getPresalesStatus ());
                    Log.e ("PresalesStatus", statusModelArrayList.get (i).getPresalesStatus ());
                }
                ArrayAdapter d = new ArrayAdapter(PresalesActivity.this,android.R.layout.simple_spinner_dropdown_item, StatusModel1);
                statusspinner.setAdapter(d);
            }
//            if (ConnectivityReceiver.isConnected(PresalesActivity.this)) {
//                new Getitem().execute();
//            } else {
//                Toast.makeText(getApplicationContext(), "Check Your Internet Connection!!!", Toast.LENGTH_SHORT).show();
//            }
        }

    }

    private class Getitem extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PresalesActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSALES_GetQuoteItem";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSALES_GetQuoteItem";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSALES_GetQuoteItem";
                Log.e("URL", URL);

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);


                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                AndroidHttpTransport aht = new AndroidHttpTransport(URL);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                aht.call(SOAP_ACTION, soapEnvelope);
                SoapPrimitive resultString = (SoapPrimitive) soapEnvelope
                        .getResponse();
                String responseJSON = resultString.toString();

                Log.e("RESPONSE", responseJSON);

                jarraylist = new JSONArray(responseJSON);

                for (int l = 0; l < jarraylist.length(); l++) {
                    JSONObject jsonObject = jarraylist.getJSONObject(l);
                    ItemModel model = new ItemModel();
                    model.setItemcode(jsonObject.getString("itemcode"));
                    model.setItemname(jsonObject.getString("itemname"));
                    model.setSalUnitMsr(jsonObject.getString("SalUnitMsr"));
                    model.setLastquoteprice(jsonObject.getString("lastquoteprice"));
                    model.setHSN(jsonObject.getString("HSN"));
                    model.setUom(jsonObject.getString("Uom"));
                    itemModelArrayList.add(model);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (jarraylist.length() == 0) {
                Toast.makeText(PresalesActivity.this, "No List", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("itemname", "" + itemModelArrayList.size());
                for (int i = 0; i < itemModelArrayList.size(); i++) {
                    ItemModel1.add(itemModelArrayList.get(i).getItemname());
                    Log.e("itemname", itemModelArrayList.get(i).getItemname());
                }

                ArrayAdapter a = new ArrayAdapter(PresalesActivity.this, android.R.layout.simple_spinner_dropdown_item, ItemModel1);
                itemspin.setAdapter(a);
            }
        }
    }

    private class PresalesHeader extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PresalesActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_PresalesHeader";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_PresalesHeader";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_PresalesHeader";
                Log.e("URL", URL);

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("DocNo", ""+DocNo );
                Log.e("DocNo", ""+DocNo );

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                AndroidHttpTransport aht = new AndroidHttpTransport(URL);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                aht.call(SOAP_ACTION, soapEnvelope);
                SoapPrimitive resultString = (SoapPrimitive) soapEnvelope.getResponse();
                String responseJSON = resultString.toString();

                Log.e("RESPONSE", responseJSON);

                jsonArray6 = new JSONArray(responseJSON);

                for (int l = 0; l < jsonArray6.length(); l++) {
                    JSONObject jsonObject = jsonArray6.getJSONObject(l);
                    PresalesHeaderModel model = new PresalesHeaderModel();

//                    model.setDocNo(jsonObject.getString("DocNo"));
//                    model.setDocDate(jsonObject.getString("DocDate"));
//                    model.setType(jsonObject.getString("type"));
//                    model.setOppType(jsonObject.getString("OppType"));
//                    model.setCategory(jsonObject.getString("category"));
//                    model.setTargetStartDate(jsonObject.getString("TargetStartDate"));
//                    model.setTargetEndDate(jsonObject.getString("TargetEndDate"));
//                    model.setTargetStatusDate(jsonObject.getString("TargetStatusDate"));
//                    model.setTargetClDate(jsonObject.getString("TargetClDate"));
//                    model.setPotentialAmt(jsonObject.getString("PotentialAmt"));
//                    model.setWeightedAmt(jsonObject.getString("WeightedAmt"));
//                    model.setGrossProfit(jsonObject.getString("GrossProfit"));
//                    model.setReported(jsonObject.getString("Reported"));
//                    model.setRespDate(jsonObject.getString("RespDate"));
//                    model.setRespEmpName(jsonObject.getString("RespEmpName"));
//                    model.setVerifyDate(jsonObject.getString("VerifyDate"));
//                    model.setVerifyEmpName(jsonObject.getString("VerifyEmpName"));

                    HeaderDocno = jsonObject.getString ("DocNo");
                    HeaderDocDate = jsonObject.getString ("DocDate");
                    Headertype = jsonObject.getString ("type");
                    HeaderOppType = jsonObject.getString ("OppType");
                    Headercategory = jsonObject.getString ("category");
                    HeaderTargetStartDate = jsonObject.getString ("TargetStartDate");
                    HeaderTargetEndDate = jsonObject.getString ("TargetEndDate");
                    HeaderTargetStatusDate = jsonObject.getString ("TargetStatusDate");
                    HeaderTargetClDate = jsonObject.getString ("TargetClDate");
                    HeaderPotentialAmt = jsonObject.getString ("PotentialAmt");
                    HeaderTargetWeightedAmt = jsonObject.getString ("WeightedAmt");
                    HeaderGrossProfit = jsonObject.getString ("GrossProfit");
                    HeaderReported = jsonObject.getString ("Reported");
                    HeaderRespDate = jsonObject.getString ("RespDate");
                    HeaderRespEmpName = jsonObject.getString ("RespEmpName");
                    HeaderVerifyDate = jsonObject.getString ("VerifyDate");
                    HeaderVerifyEmpName = jsonObject.getString ("VerifyEmpName");
//                    presalesHeaderModelArrayList.add(model);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (jsonArray6.length() == 0) {
                Toast.makeText(PresalesActivity.this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
            } else {
                type_et.setText (""+Headertype);
                oppurtunitytype_et.setText (""+HeaderOppType);
                category_et.setText (""+Headercategory);
                potenamnt_et.setText (""+HeaderPotentialAmt);
                weightedamt_et.setText (""+HeaderTargetWeightedAmt);
                grosspro_et.setText (""+HeaderGrossProfit);
                reported_et.setText (""+HeaderReported);
                resdate_et.setText (""+HeaderRespDate);
                cDate_et.setText (""+HeaderTargetClDate);
                startdate_et.setText (""+HeaderTargetStartDate);
                enddate_et.setText (""+HeaderTargetEndDate);
                statusdate_et.setText (""+HeaderTargetStatusDate);
                verifidate_et.setText (""+HeaderVerifyDate);

                new GETPreSalesDetails().execute ();
            }
        }
    }


    private class GETPreSalesDetails extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PresalesActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_GETPreSalesDetails";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_GETPreSalesDetails";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_GETPreSalesDetails";
                Log.e("URL", URL);

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("DocNo", ""+DocNo );
                Log.e("DocNo", ""+DocNo );

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                AndroidHttpTransport aht = new AndroidHttpTransport(URL);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                aht.call(SOAP_ACTION, soapEnvelope);
                SoapPrimitive resultString = (SoapPrimitive) soapEnvelope.getResponse();
                String responseJSON = resultString.toString();

                Log.e("RESPONSE", responseJSON);

                jsonArray6 = new JSONArray(responseJSON);

                for (int l = 0; l < jsonArray6.length(); l++) {
                    JSONObject jsonObject = jsonArray6.getJSONObject(l);
                    PresalesModel Model = new PresalesModel();

                    Model.setRowId(jsonObject.getString("RowId"));
                    Model.setDocno(jsonObject.getString("docno"));
                    Model.setItemcode(jsonObject.getString("itemcode"));
                    Model.setName(jsonObject.getString("itemname"));
                    Model.setUom(jsonObject.getString("uom"));
                    Model.setQuantity(jsonObject.getString("qty"));
                    Model.setPrice(jsonObject.getString("price"));
                    Model.setCreatedBy(jsonObject.getString("CreatedBy"));
                    Model.setCreatedDate(jsonObject.getString("CreatedDate"));

                    presalesModelArrayList.add(Model);

                    Rowid=(jsonObject.getString("RowId"));
                    docno=jsonObject.getString("docno");
                    Itemcode=jsonObject.getString("itemcode");
                    Itemname=jsonObject.getString("itemname");
                    uom=jsonObject.getString("uom");
                    qty=jsonObject.getString("qty");
                    price=jsonObject.getString("price");
                    CreatedBy=jsonObject.getString("CreatedBy");
                    CreatedDate=jsonObject.getString("CreatedDate");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (jsonArray6.length() == 0) {
                Toast.makeText(PresalesActivity.this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
            } else {
                presalesRecycle.setVisibility(View.VISIBLE);
                presalesRecycle.setLayoutManager(new LinearLayoutManager(PresalesActivity.this));
                Adapter = new PresalesAdapter(getApplicationContext(), presalesModelArrayList);
                presalesRecycle.setAdapter(Adapter);
                presalesRecycle.setHasFixedSize(true);
            }
        }
    }

    public class PresalesAdapter extends RecyclerView.Adapter {

        Context getApplicationContext;
        ArrayList<PresalesModel> presalesModelArrayList;
        String edited_qty = "",edited_price="", positionclicked;



        public PresalesAdapter(Context getApplicationContext, ArrayList<PresalesModel> presalesModelArrayList) {
            this.getApplicationContext = getApplicationContext;
            this.presalesModelArrayList = presalesModelArrayList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.presalesdetail, parent, false);
            return new ViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position)
        {
            if(holder instanceof ViewHolder)
            {
                int pos=position+1;

//                ((ViewHolder) holder).sno.setText(""+pos);
                ((ViewHolder) holder).itemcode.setText(presalesModelArrayList.get(position).getItemcode());
                ((ViewHolder) holder).name.setText(presalesModelArrayList.get(position).getName());
                ((ViewHolder) holder).Quantity.setText(presalesModelArrayList.get(position).getQuantity());
                ((ViewHolder) holder).Price.setText(presalesModelArrayList.get(position).getPrice());
                ((ViewHolder) holder).Uom.setText(presalesModelArrayList.get(position).getUom());

                ((ViewHolder) holder).deletebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        presalesModelArrayList.remove(position);
                        notifyDataSetChanged();
                    }
                });


                ((ViewHolder) holder).name.setFocusable (false);
                ((ViewHolder) holder).name.setFocusableInTouchMode (false);
                ((ViewHolder) holder).name.setClickable (false);
                ((ViewHolder) holder).Price.setFocusable (false);
                ((ViewHolder) holder).Price.setFocusableInTouchMode (false);
                ((ViewHolder) holder).Quantity.setFocusable (false);
                ((ViewHolder) holder).Quantity.setFocusableInTouchMode (false);
                ((ViewHolder) holder).itemcode.setFocusable (false);
                ((ViewHolder) holder).itemcode.setFocusableInTouchMode (false);

                if (editstatus.equalsIgnoreCase ("1")) {
                    ((ViewHolder) holder).editrowbtn.setVisibility (View.VISIBLE);

                    ((ViewHolder) holder ).name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent (getApplicationContext, ItemList_Spinner.class);
                            intent.putExtra ("ScreenStatus","PA");
                            intent.putExtra ("position1", ""+position);
                            positionclicked = String.valueOf (position);
                            startActivityForResult(intent, 1);
                        }
                    });
                    ((ViewHolder) holder ).Price.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText (getApplicationContext, "Kindly Click on Edit to Change this Data", Toast.LENGTH_SHORT).show ( );
                        }
                    });
                    ((ViewHolder) holder ).Quantity.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText (getApplicationContext, "Kindly Click on Edit to Change this Data", Toast.LENGTH_SHORT).show ( );
                        }
                    });
                    ((ViewHolder) holder ).itemcode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText (getApplicationContext, "Kindly Click on Edit to Change this Data", Toast.LENGTH_SHORT).show ( );
                        }
                    });

                    ((ViewHolder) holder).editrowbtn.setOnClickListener (new View.OnClickListener ( ) {
                        @Override
                        public void onClick(View view) {

                            Toast.makeText (getApplicationContext, "Now you can Edit this Row", Toast.LENGTH_SHORT).show ( );

                            String editposition = String.valueOf (position);
                            Log.e ("editposition",""+editposition);


                            ((ViewHolder) holder).name.setFocusable (true);
                            ((ViewHolder) holder).name.setFocusableInTouchMode (true);
                            ((ViewHolder) holder).name.setClickable (true);
                            ((ViewHolder) holder).Price.setFocusable (true);
                            ((ViewHolder) holder).Price.setFocusableInTouchMode (true);
                            ((ViewHolder) holder).Quantity.setFocusable (true);
                            ((ViewHolder) holder).Quantity.setFocusableInTouchMode (true);
                            ((ViewHolder) holder).itemcode.setFocusable (true);
                            ((ViewHolder) holder).itemcode.setFocusableInTouchMode (true);
                        }
                    });

                    ((ViewHolder) holder).Quantity.addTextChangedListener(new TextWatcher()
                    {
                        @Override
                        public void afterTextChanged(Editable mEdit)
                        {
                            edited_qty = mEdit.toString();
                            Log.e ("edited_qty",edited_qty);
                            presalesModelArrayList.get(position).setQuantity (edited_qty);
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
                            presalesModelArrayList.get(position).setPrice (edited_price);
                        }
                        public void beforeTextChanged(CharSequence s, int start, int count, int after){

                        }
                        public void onTextChanged(CharSequence s, int start, int before, int count){

                        }
                    });

                    if (itemname.equalsIgnoreCase ("")) {
                        Log.e ("ITEMNAMEEMPTY","" + itemname);
                    } else {
                        Log.e ("position1","position1"+position1);
                        Log.e ("positionclicked","positionclicked"+positionclicked);
                        if (position1.equalsIgnoreCase (positionclicked)) {
                            ((ViewHolder) holder).name.setText (itemname);
                            ((ViewHolder) holder).itemcode.setText (itemcode);
                            Log.e ("ChangedItemname","itemname"+itemname);
                            Log.e ("ChangedItemcode","itemcode"+itemcode);
                            presalesModelArrayList.get(position).setName(itemname);
                            presalesModelArrayList.get(position).setItemcode(itemcode);
                        } else {
                            Log.e ("Position","Not Match");
                        }
                    }
                } else {
                    ((ViewHolder) holder).editrowbtn.setVisibility (View.GONE);
                }
            }
        }
        @Override
        public int getItemCount()
        {
            return presalesModelArrayList.size();
        }
        public  class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView sno,itemcode,Uom,canceltv;
            EditText name, Quantity,Price;
            ImageButton deletebtn;
            ImageView editrowbtn;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                itemcode= itemView.findViewById(R.id.itemcode);
                name= itemView.findViewById(R.id.name);
                deletebtn= itemView.findViewById(R.id.deletebtn);
                Quantity= itemView.findViewById(R.id.quantity);
                Price= itemView.findViewById(R.id.Price);
                Uom= itemView.findViewById(R.id.Uom);
                canceltv= itemView.findViewById(R.id.canceltv);
                editrowbtn= itemView.findViewById(R.id.editrowbtn);
            }
        }
    }

    private class AddPresales extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PresalesActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_AddPresales";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_AddPresales";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_AddPresales";
                Log.e("URL", URL);

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);


                if (editstatus.equalsIgnoreCase ("1")) {
                    Request.addProperty ("DocNo", "" + DocNo);
                    Log.e ("DocNo", ""+DocNo);
                } else {
                    Request.addProperty ("DocNo", "" + 0);
                    Log.e ("DocNo", "0");
                }
                Request.addProperty("DocDate", ""+DocDate_et.getText().toString());
                Log.e("DocDate", ""+DocDate_et.getText().toString());

                Request.addProperty("OppType", "" + oppurtunitytype_et.getText().toString());
                Log.e("OppType", "" + oppurtunitytype_et.getText().toString());

                Request.addProperty("type", "" + type_et.getText().toString());
                Log.e("type", "" + type_et.getText().toString());

                Request.addProperty("Category", ""+  category_et.getText().toString() );
                Log.e("Category", "" +  category_et.getText().toString());

                Request.addProperty("TargetStartDate", "" + startdate_et.getText().toString());
                Log.e("TargetStartDate", "" + startdate_et.getText().toString());

                Request.addProperty("TargetEndDate", ""+ enddate_et.getText().toString());
                Log.e("TargetEndDate", "" + enddate_et.getText().toString());

                Request.addProperty("TargetStatusDate", "" + statusdate_et.getText().toString());
                Log.e("TargetStatusDate", "" + statusdate_et.getText().toString());

                Request.addProperty("TargetClDate", ""+ cDate_et.getText().toString());
                Log.e("TargetClDate", "" + cDate_et.getText().toString());

                Request.addProperty("PotentialAmt", "" +potenamnt_et.getText().toString() );
                Log.e("PotentialAmt", ""+potenamnt_et.getText().toString() );

                Request.addProperty("WeightedAmt", ""+weightedamt_et.getText().toString() );
                Log.e("WeightedAmt", ""+weightedamt_et.getText().toString() );

                Request.addProperty("GrossProfit", "" +grosspro_et.getText().toString());
                Log.e("GrossProfit", ""+grosspro_et.getText().toString() );

                Request.addProperty("Reported", ""+reported_et.getText().toString());
                Log.e("Reported", ""+reported_et.getText().toString());

                Request.addProperty("RepEmpName", "");
                Log.e("RepEmpName", "empty");

                Request.addProperty("RepEmpDesig", "" );
                Log.e("RepEmpDesig", "empty");

                Request.addProperty("RespDate", ""+resdate_et.getText().toString());
                Log.e("RespDate", ""+resdate_et.getText().toString());

                Request.addProperty("RespEmpName", "");
                Log.e("RespEmpName", "empty");

                Request.addProperty("RespEmpDesig", "" );
                Log.e("RespEmpDesig", "empty");

                Request.addProperty("VerifyDate", "" +verifidate_et.getText().toString());
                Log.e("VerifyDate", "" +verifidate_et.getText().toString());

                Request.addProperty("VerifyEmpName", "" );
                Log.e("VerifyEmpName", "empty");

                    String bookingitem="";
                    for (int io = 0; io < presalesModelArrayList.size(); io++) {

                        String xmlRowId="",xmlitemcode="",xmlname="",xmlQuantity="",xmlPrice="",xmlUom="";

                        xmlRowId = presalesModelArrayList.get(io).getRowId();
                        xmlitemcode = presalesModelArrayList.get(io).getItemcode();
                        xmlname = presalesModelArrayList.get(io).getName();
                        xmlQuantity = presalesModelArrayList.get(io).getQuantity();
                        xmlPrice = presalesModelArrayList.get(io).getPrice();
                        xmlUom = presalesModelArrayList.get(io).getUom();

                        int Empid= Integer.parseInt(sessionempid);
                        String date = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());

                        bookingitem = bookingitem + "<Table1><RowId>"+xmlRowId+"</RowId><ItemCode>"+ xmlitemcode +"</ItemCode><ItemName>" + xmlname + "</ItemName><UOM>" + xmlUom + "</UOM><Qty>" + ""+xmlQuantity + "</Qty><Price>" + ""+xmlPrice + "</Price>" +
                                "<CreatedBy>" + Empid + "</CreatedBy><CreateDate>" + date + "</CreateDate></Table1>";
                    }
                    bookingitem = "<NewDataSet>" + bookingitem + "</NewDataSet>";

                    Request.addProperty("ItemDetailXML", ""+bookingitem);
                    Log.e("ItemDetailXML", ""+bookingitem);

                Request.addProperty("ItemDetailXMLID", ""+1 );
                Log.e("ItemDetailXMLID", ""+1 );

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
                AlertDialog.Builder builder = new AlertDialog.Builder(PresalesActivity.this);
                builder.setMessage(" Saved ")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                presalesModelArrayList.clear();
                                finish();

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }  else if (launch.equalsIgnoreCase("2")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PresalesActivity.this);
                builder.setMessage(" Updated ")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                presalesModelArrayList.clear();
                                finish();

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(PresalesActivity.this);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
