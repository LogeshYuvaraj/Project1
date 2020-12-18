package com.example.ravelelectronics;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ravelelectronics.model.ActivityDetailsModel;
import com.example.ravelelectronics.model.ActivityModel;
import com.example.ravelelectronics.model.CustomerModel;
import com.example.ravelelectronics.model.PrjEmployeeModel;
import com.example.ravelelectronics.spinnerclasses.ActivityList_Spinner;
import com.example.ravelelectronics.spinnerclasses.CustomerList_Spinner;
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

public class ActivityScreen extends AppCompatActivity implements View.OnClickListener {

    EditText activity_et,customer_et;
    LinearLayout checkin_layout,checkout_layout,reference_layout;
    EditText fromdate_et,todate_et,Description_et,contact_et,checkin_et,checkout_et,followupdte_et,referenceno_et;
    Button save,cancel,saveAS;
    MaterialSpinner activity_spin,bpname_spin,customer_spin,statusspinner;
    String [] statusspin={"Select Type","Open","Closed"};
    String StatusSpin="";

    ProgressDialog pDialog;
    JSONArray jarraylist,jsonArray1,jsonArray,jsonArray6;
    private TimePicker timePicker1;
    int checkin=0,checkout=0;
    SessionManagement session;
    String sessionempid="";
    DatePickerDialog dp;
    DatePickerDialog dp1;
    DatePickerDialog dp2;
    String ActivityNo="",ActivityDate="",CustomerName="", Activity="", ContactNo="",StartTime="",EndTime="",
            Employee="",Status="",CustomerCode="",FromDt="",ToDt="",Description="",FollowupDate="";
    String Action="";
    String EmpName = "", empID = "",slpname="";

    String strfault = "", launch = "";
    String CardCode = "", CardName = "",  Column2 = "",Column1="";
    List<String> CustomerModel1 = new ArrayList<>();
    ArrayList<CustomerModel> customerModelArrayList;

    List<String> ActivityDetailsModel1 = new ArrayList<>();
    ArrayList<ActivityDetailsModel> activityDetailsModelArrayList;

    List<String> PrjEmployeeModel1 = new ArrayList<>();
    ArrayList<PrjEmployeeModel> prjEmployeeModelArrayList;

    ArrayList<ActivityModel> activityModelArrayList;
    List<String> ActivityModel1 = new ArrayList<> ();
    String editstatus="",DocNo="";
    String spinnerstatus = "";
    String activity = "",customername = "",customercode = "";
    int Referenceno = 0;
    String currentdate = "";
    String EarlierDateResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);

        session = new SessionManagement(ActivityScreen.this);
        HashMap<String, String> users = session.getUserDetails();
        sessionempid = users.get(SessionManagement.KEY_CODE);

        saveAS = findViewById(R.id.saveAS);
        reference_layout = findViewById(R.id.reference_layout);
        referenceno_et = findViewById(R.id.referenceno_et);
        followupdte_et = findViewById(R.id.followupdte_et);
        checkin_layout = findViewById(R.id.checkin_layout);
        checkout_layout = findViewById(R.id.checkout_layout);
        activity_et = findViewById(R.id.activity_et);
        customer_et = findViewById(R.id.customer_et);
        activity_spin= findViewById(R.id.activity_spin);
        bpname_spin=findViewById(R.id.bpname_spin);
        fromdate_et=(EditText) findViewById(R.id.fromdate_et);
        todate_et=(EditText) findViewById(R.id.todate_et);
        Description_et=(EditText) findViewById(R.id.Description_et);
        contact_et=(EditText) findViewById(R.id.contact_et);
        checkin_et=(EditText) findViewById(R.id.checkin_et);
        checkout_et=(EditText) findViewById(R.id.checkout_et);
        statusspinner=findViewById(R.id.statusspinner);
        customer_spin = findViewById(R.id.customer_spin);
        activityModelArrayList = new ArrayList<> ();
        prjEmployeeModelArrayList=new ArrayList<>();
        customerModelArrayList = new ArrayList<>();

        Intent intent=getIntent();
        spinnerstatus=intent.getStringExtra("spinnerstatus");
        DocNo=intent.getStringExtra("DocNo");
        editstatus=intent.getStringExtra("editstatus");

        Log.e("DocNo","DocNo"+DocNo);
        Log.e("editstatus",""+editstatus);

        activity_et.setOnClickListener (this);
        customer_et.setOnClickListener (this);
        save=(Button)findViewById(R.id.save);
        cancel=(Button)findViewById(R.id.cancel);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        checkin_et.setOnClickListener(this);
        checkout_et.setOnClickListener(this);
        saveAS.setOnClickListener(this);

        activityDetailsModelArrayList = new ArrayList<>();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small> My Activity </small>"));
        }

        ArrayAdapter s=new ArrayAdapter(this,android.R.layout.simple_spinner_item,statusspin);
        s.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusspinner.setAdapter(s);

        statusspinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                StatusSpin=item.toString();
                Log.e("StatusSpin",StatusSpin);
            }
        });

        if (editstatus.equalsIgnoreCase("0")){
            checkin_layout.setVisibility(View.VISIBLE);
            checkout_layout.setVisibility(View.GONE);
            Log.e("From","Dashboard");
        } else if(editstatus.equalsIgnoreCase("2")){
            save.setText ("Update");
            saveAS.setVisibility(View.GONE);
            reference_layout.setVisibility(View.GONE);
            referenceno_et.setText(DocNo);
            new GETActivityDetails().execute();
        } else if(editstatus.equalsIgnoreCase("3")){
            save.setVisibility(View.GONE);
            saveAS.setVisibility(View.VISIBLE);
            reference_layout.setVisibility(View.VISIBLE);
            referenceno_et.setText(DocNo);
            new GETActivityDetails().execute();
        } else {
            save.setText ("Update");
            new GETActivityDetails().execute();
            Log.e("From","Edit");
        }

        activity_spin.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Action=item.toString();
                Log.e("Action",Action);
            }
        });

        bpname_spin.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                slpname = item.toString();
                Log.e("slpname", slpname);

                empID = prjEmployeeModelArrayList.get(position).getEmpID();
                Log.e("empID", empID);
                EmpName = prjEmployeeModelArrayList.get(position).getEmpID();
                Log.e("EmpName", EmpName);
            }
        });

        customer_spin.setMaxWidth(250);

        customer_spin.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                CardName = item.toString();
                Log.e("CardName", CardName);
                CardCode = customerModelArrayList.get(position).getCardCode();
                Log.e("CardCode", CardCode);
                Column1 = customerModelArrayList.get(position).getColumn1();
                Log.e("Column1", Column1);
                Column2 = customerModelArrayList.get(position).getColumn2();
                Log.e("Column2", Column2);
            }
        });

        currentdate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
        fromdate_et.setText(currentdate);
        todate_et.setText(currentdate);

//        new ActivityControl_EarlierDate().execute();

//        checkin_et.setText(new SimpleDateFormat("HH:mm", Locale.US).format(new Date()));
//        checkout_et.setText(new SimpleDateFormat("HH:mm", Locale.US).format(new Date()));

        fromdate_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                final int y = c.get(Calendar.YEAR);
                final int m = c.get(Calendar.MONTH);
                final int d = c.get(Calendar.DAY_OF_MONTH);

                dp = new DatePickerDialog(ActivityScreen.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                Calendar dateSelected = Calendar.getInstance();
                                dateSelected.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat formatterfrm = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
                                fromdate_et.setText(formatterfrm.format(dateSelected.getTime()));
                                Log.d("Fromdate", fromdate_et.getText().toString());
                            }

                        }, y, m, d);
                dp.show();
            }
        });

        todate_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                final int y = c.get(Calendar.YEAR);
                final int m = c.get(Calendar.MONTH);
                final int d = c.get(Calendar.DAY_OF_MONTH);

                dp1 = new DatePickerDialog(ActivityScreen.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                Calendar dateSelected = Calendar.getInstance();
                                dateSelected.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat formatterfrm = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
                                todate_et.setText(formatterfrm.format(dateSelected.getTime()));
                                Log.d("Todate", todate_et.getText().toString());
                            }

                        }, y, m, d);
                dp1.show();
            }
        });
        followupdte_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                final int y = c.get(Calendar.YEAR);
                final int m = c.get(Calendar.MONTH);
                final int d = c.get(Calendar.DAY_OF_MONTH);

                dp2 = new DatePickerDialog(ActivityScreen.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                Calendar dateSelected = Calendar.getInstance();
                                dateSelected.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat formatterfrm = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
                                followupdte_et.setText(formatterfrm.format(dateSelected.getTime()));
                                Log.d("followupdte", followupdte_et.getText().toString());
                            }

                        }, y, m, d);
                dp2.show();
            }
        });

//        checkin_et.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Calendar mcurrentTime = Calendar.getInstance();
//                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                int minute = mcurrentTime.get(Calendar.MINUTE);
//
//                TimePickerDialog mTimePicker;
//                mTimePicker = new TimePickerDialog(ActivityScreen.this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                        checkin_et.setText( selectedHour + ":" + selectedMinute);
//                    }
//                }, hour, minute, true);
//                mTimePicker.setTitle("Select Time");
//                mTimePicker.show();
//
//            }
//        });
//
//        checkout_et.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Calendar mcurrentTime = Calendar.getInstance();
//                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                int minute = mcurrentTime.get(Calendar.MINUTE);
//
//                TimePickerDialog mTimePicker;
//                mTimePicker = new TimePickerDialog(ActivityScreen.this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                        checkout_et.setText( selectedHour + ":" + selectedMinute);
//                    }
//                }, hour, minute, true);
//                mTimePicker.setTitle("Select Time");
//                mTimePicker.show();
//            }
//        });

        customer_spin.setOnClickListener(this);
        activity_spin.setOnClickListener(this);
        bpname_spin.setOnClickListener(this);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && data != null) {
            try {
                activity = data.getStringExtra ("activity");
                activity_et.setText (""+activity);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (requestCode == 2 && data != null) {
            try {
                customername = data.getStringExtra ("customername");
                customercode = data.getStringExtra ("customercode");
                customer_et.setText (""+customername);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        if (checkin_et==v){
            checkin_et.setText(new SimpleDateFormat("HH:mm", Locale.US).format(new Date()));
        }
        if (checkout_et==v){
            checkout_et.setText(new SimpleDateFormat("HH:mm", Locale.US).format(new Date()));
        }
        if (v == activity_et) {
            Intent intent = new Intent (ActivityScreen.this, ActivityList_Spinner.class);
            startActivityForResult(intent, 1);
        }
        if (v == customer_et) {
            Intent intent = new Intent (ActivityScreen.this, CustomerList_Spinner.class);
            intent.putExtra ("ScreenStatus","AS");
            startActivityForResult(intent, 2);
        }
        if (customer_spin==v){
//            if (customerModelArrayList.size()==0){
//                new GetCustomer().execute();
//            } else {
//                Log.e("XXX","");
//            }
        }
        if (activity_spin==v){
//            if (activityModelArrayList.size()==0) {
//                new activity().execute();
//            } else {
//                Log.e("XXX","");
//            }
        }
        if (bpname_spin==v){
            if (PrjEmployeeModel1.size()==0){
                new GetEmployee().execute();
            }else {
                Log.e("XXX","");
            }
        }
        if (v == save) {
            activity = activity_et.getText ().toString ().trim ();
            customername = customer_et.getText ().toString ().trim ();
            StatusSpin = statusspinner.getText ().toString ();
            Description = Description_et.getText ().toString ();

            if(activity.equalsIgnoreCase("") || activity.equalsIgnoreCase ("Select")){
                Toast.makeText(this, "Kindly Select Any Activity", Toast.LENGTH_SHORT).show();
//            } else  if(Description_et.getText().toString().trim().equalsIgnoreCase("")){
//                Toast.makeText(this, "Kindly Select Description", Toast.LENGTH_SHORT).show();
            } else  if(customername.equalsIgnoreCase("") || customername.equalsIgnoreCase ("Select")){
                Toast.makeText(this, "Kindly Select Customer", Toast.LENGTH_SHORT).show();
//            } else  if(slpname.equalsIgnoreCase("")){
//                Toast.makeText(this, "Kindly Select slpname ", Toast.LENGTH_SHORT).show();
            } else  if(StatusSpin.equalsIgnoreCase("") || StatusSpin.equalsIgnoreCase ("Select Type")){
                Toast.makeText(this, "Kindly Select Status", Toast.LENGTH_SHORT).show();
            } else  if(contact_et.getText().toString().trim().equalsIgnoreCase("")){
                Toast.makeText(this, "Kindly enter Contact number", Toast.LENGTH_SHORT).show();
            } else {
                new ActivityControl_EarlierDate().execute();
            }
        }
        if (v == saveAS) {
            activity = activity_et.getText ().toString ().trim ();
            customername = customer_et.getText ().toString ().trim ();
            StatusSpin = statusspinner.getText ().toString ();
            Description = Description_et.getText ().toString ();

            if(activity.equalsIgnoreCase("") || activity.equalsIgnoreCase ("Select")){
                Toast.makeText(this, "Kindly Select Any Activity", Toast.LENGTH_SHORT).show();
//            } else  if(Description_et.getText().toString().trim().equalsIgnoreCase("")){
//                Toast.makeText(this, "Kindly Select Description", Toast.LENGTH_SHORT).show();
            } else  if(customername.equalsIgnoreCase("") || customername.equalsIgnoreCase ("Select")){
                Toast.makeText(this, "Kindly Select Customer", Toast.LENGTH_SHORT).show();
//            } else  if(slpname.equalsIgnoreCase("")){
//                Toast.makeText(this, "Kindly Select slpname ", Toast.LENGTH_SHORT).show();
            } else  if(StatusSpin.equalsIgnoreCase("") || StatusSpin.equalsIgnoreCase ("Select Type")){
                Toast.makeText(this, "Kindly Select Status", Toast.LENGTH_SHORT).show();
            } else  if(contact_et.getText().toString().trim().equalsIgnoreCase("")){
                Toast.makeText(this, "Kindly enter Contact number", Toast.LENGTH_SHORT).show();
            } else {
                new ActivityControl_EarlierDate().execute();
            }
        }
        if (v == cancel){
            finish();
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

    private class ActivityControl_EarlierDate extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ActivityScreen.this);
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
                final String METHOD_NAME = "IndusMobileSales_ActivityControl_EarlierDate";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_ActivityControl_EarlierDate";
                final String URL =  "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_ActivityControl_EarlierDate";
                Log.e("URL", URL);
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("FromDt","" + fromdate_et.getText().toString());
                Log.e("FromDt","" + fromdate_et.getText().toString());

                Request.addProperty("Todate","" + todate_et.getText().toString());
                Log.e("Todate","" + todate_et.getText().toString());

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
                    EarlierDateResult = jsonObject.getString("result");
                    Log.e("EarlierDateResult == ","result = " + EarlierDateResult);
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
                Toast.makeText (ActivityScreen.this, "No Data", Toast.LENGTH_SHORT).show ( );
            } else {
                if (EarlierDateResult.equalsIgnoreCase("1")) {
//                    save.setVisibility(View.VISIBLE);
                    new AddActivities().execute();
                } else {
//                    save.setVisibility(View.GONE);
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ActivityScreen.this);
                    builder1.setMessage("The Date which is selected cannot be Added to Activity");
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
                    Toast.makeText(ActivityScreen.this, "The Date which is selected cannot be Added to Activity", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class activity extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ActivityScreen.this);
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
                final String METHOD_NAME = "IndusMobileSALES_GetActivity";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSALES_GetActivity";
                final String URL =  "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSALES_GetActivity";
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

             activityModelArrayList.clear();
              ActivityModel1.clear();
                jsonArray1 = new JSONArray(responseJSON);
                for (int i = 0; i < jsonArray1.length(); i++) {

                    JSONObject jsonObject = jsonArray1.getJSONObject(i);
                    ActivityModel model = new ActivityModel ();
                    model.setAction (jsonObject.getString ("Action"));

                    activityModelArrayList.add (model);

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
                Toast.makeText (ActivityScreen.this, "No List", Toast.LENGTH_SHORT).show ( );
            } else {
                Log.e ("Action", "" + activityModelArrayList.size ());
                for (int i = 0;i<activityModelArrayList.size();i++) {

                    ActivityModel1.add (activityModelArrayList.get (i).getAction());

                    Log.e ("Action", activityModelArrayList.get (i).getAction ());
                }

                ArrayAdapter d = new ArrayAdapter(ActivityScreen.this,android.R.layout.simple_spinner_dropdown_item, activityModelArrayList);
//                activity_spin.setAdapter(d);
                activity_spin.setItems(ActivityModel1);
            }
        }
    }

    private class GetEmployee extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ActivityScreen.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    final String NAMESPACE = "http://tempuri.org/";
                    final String METHOD_NAME = "IndusMobileSALES_GetPresalesEmployee";
                    final String SOAP_ACTION = "http://tempuri.org/IndusMobileSALES_GetPresalesEmployee";
                    final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSALES_GetPresalesEmployee";
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

                    prjEmployeeModelArrayList.clear();
                    PrjEmployeeModel1.clear();
                    jarraylist = new JSONArray(responseJSON);

                    for (int l = 0; l < jarraylist.length(); l++) {
                        JSONObject jsonObject = jarraylist.getJSONObject(l);
                        PrjEmployeeModel model = new PrjEmployeeModel();
                        model.setEmpName(jsonObject.getString("EmpName"));
                        model.setEmpID(jsonObject.getString("empID"));
                        model.setSlpname(jsonObject.getString("slpname"));

                        prjEmployeeModelArrayList.add(model);
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
                    Toast.makeText(ActivityScreen.this, "No List", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("EmpName", "" + prjEmployeeModelArrayList.size());
                    for (int i = 0; i < prjEmployeeModelArrayList.size(); i++) {
                        PrjEmployeeModel1.add(prjEmployeeModelArrayList.get(i).getEmpName());
                        Log.e("EmpName", prjEmployeeModelArrayList.get(i).getEmpName());
                    }

                    ArrayAdapter a = new ArrayAdapter(ActivityScreen.this, android.R.layout.simple_spinner_dropdown_item, PrjEmployeeModel1);
//                    bpname_spin.setAdapter(a);
                    bpname_spin.setItems(PrjEmployeeModel1);
                }
        }
    }

    private class GetCustomer extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ActivityScreen.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSALES_GetCustomer";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSALES_GetCustomer";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSALES_GetCustomer";
                Log.e("URL", URL);

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

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

                customerModelArrayList.clear();
                CustomerModel1.clear();
                jsonArray = new JSONArray(responseJSON);

                for (int l = 0; l < jsonArray.length(); l++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(l);
                    CustomerModel model = new CustomerModel();
                    model.setCardCode(jsonObject.getString("CardCode"));
                    model.setCardName(jsonObject.getString("CardName"));
                    model.setColumn1(jsonObject.getString("Column1"));
                    model.setColumn2(jsonObject.getString("Column2"));

                    customerModelArrayList.add(model);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (jsonArray.length() == 0) {
                Toast.makeText(ActivityScreen.this, "No List", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("CUSTOMER", "" + customerModelArrayList.size());
                for (int i = 0; i < customerModelArrayList.size(); i++) {
                    CustomerModel1.add(customerModelArrayList.get(i).getCardName());
                    Log.e("CardName", customerModelArrayList.get(i).getCardName());
                }

                ArrayAdapter a = new ArrayAdapter(ActivityScreen.this, android.R.layout.simple_spinner_dropdown_item, CustomerModel1);
//                customer_spin.setAdapter(a);
                customer_spin.setItems(CustomerModel1);
            }
        }
    }

    private class GETActivityDetails extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ActivityScreen.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_GETActivityDetails";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_GETActivityDetails";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_GETActivityDetails";
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
                    ActivityNo=jsonObject.getString("Activity No.");
                    ActivityDate=jsonObject.getString("Activity Date");
                    CustomerName=jsonObject.getString("Customer Name");
                    CustomerCode=jsonObject.getString("CardCode");
                    Activity=jsonObject.getString("Activity");
                    ContactNo=jsonObject.getString("Contact No");
                    StartTime=jsonObject.getString("Start Time");
                    EndTime=jsonObject.getString("EndTime");
                    Employee=jsonObject.getString("Employee");
                    Status=jsonObject.getString("Status");
                    FromDt=jsonObject.getString("FromDt");
                    ToDt=jsonObject.getString("ToDt");
                    Description=jsonObject.getString("Description");
                    FollowupDate=jsonObject.getString("FollowupDt");
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
                Toast.makeText(ActivityScreen.this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
            } else {
                activity_et.setText(Activity);
                customer_et.setText(CustomerName);
                contact_et.setText(ContactNo);
                fromdate_et.setText(FromDt);
                todate_et.setText(ToDt);
                bpname_spin.setText(Employee);
                statusspinner.setText(Status);
                Description_et.setText(Description);
                followupdte_et.setText(FollowupDate);
                customercode = CustomerCode;

                if (StartTime.equalsIgnoreCase("")){
                    checkin_layout.setVisibility(View.VISIBLE);
                    checkout_layout.setVisibility(View.VISIBLE);
                    checkin_et.setText(StartTime);
                    checkout_et.setText(EndTime);
                } else {
                    checkin_layout.setVisibility(View.VISIBLE);
                    checkout_layout.setVisibility(View.VISIBLE);
                    checkin_et.setText(StartTime);
                    checkout_et.setText(EndTime);
                    checkin_et.setClickable(false);
                    checkin_et.setFocusable(false);
                    checkin_et.setFocusableInTouchMode(false);
                    checkin_et.setEnabled(false);
                }
                if(EndTime.equalsIgnoreCase("")){
                    Log.e("EndTime",""+EndTime);
                    checkout_et.setText(EndTime);
                } else {
                    checkout_et.setClickable(false);
                    checkout_et.setFocusable(false);
                    checkout_et.setFocusableInTouchMode(false);
                    checkout_et.setEnabled(false);
                }
                if (editstatus.equalsIgnoreCase("3")) {
                    fromdate_et.setText(currentdate);
                    todate_et.setText(currentdate);
                }
            }
        }
    }

    private class AddActivities extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ActivityScreen.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_AddActivities";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_AddActivities";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_AddActivities";
                Log.e("URL", URL);

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                if (editstatus.equalsIgnoreCase ("1") || editstatus.equalsIgnoreCase("2")) {
                    Request.addProperty ("DocNo", "" + DocNo);
                    Log.e ("DocNo", ""+DocNo);
                } else {
                    Request.addProperty ("DocNo", "" + 0);
                    Log.e ("DocNo", "0");
                }

                Request.addProperty("CardCode", ""+customercode );
                Log.e("CardCode", ""+customercode );

                Request.addProperty("CardName", ""+customer_et.getText ().toString ());
                Log.e("CardName", ""+customer_et.getText ().toString ());

                Request.addProperty("Activity", "" + activity_et.getText ().toString ());
                Log.e("Activity", "" + activity_et.getText ().toString ());

                String Contactno=contact_et.getText().toString();
                Log.e("Contactno",Contactno);
                Request.addProperty("ContactNo", ""+Contactno );
                Log.e("ContactNo", "" +  Contactno);

                Request.addProperty("FromDt", "" + fromdate_et.getText().toString());
                Log.e("FromDt", "" + fromdate_et.getText().toString());

                Request.addProperty("ToDt", "" + todate_et.getText().toString());
                Log.e("ToDt", todate_et.getText().toString());

                Request.addProperty("CheckIn", "" +  checkin_et.getText().toString().trim());
                Log.e("CheckIn", "" + checkin_et.getText().toString().trim());

                Request.addProperty("CheckOut", "" + checkout_et.getText().toString().trim());
                Log.e("CheckOut", "" + checkout_et.getText().toString().trim());

                Request.addProperty("User", ""+sessionempid);
                Log.e("User",""+sessionempid );

                Request.addProperty("Description", ""+Description_et.getText().toString());
                Log.e("Description",""+Description_et.getText().toString());

                if (StatusSpin.equalsIgnoreCase("Open")){
                    StatusSpin="O";
                } else {
                    StatusSpin="C";
                }
                Request.addProperty("Status", ""+StatusSpin);
                Log.e("Status",""+StatusSpin);

                Request.addProperty("Followupdt", ""+followupdte_et.getText().toString());
                Log.e("Followupdt",""+followupdte_et.getText().toString());

                if (editstatus.equalsIgnoreCase("3")) {
                    Request.addProperty("Referenceno", "" + DocNo);
                    Log.e("Referenceno", "" + DocNo);
                } else {
                    Request.addProperty("Referenceno", "" + Referenceno);
                    Log.e("Referenceno", "" + Referenceno);
                }

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
                launch = ( "" + resultsRequestSOAP.getProperty(0) );
            } catch (Exception exg) {
                exg.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (launch.equalsIgnoreCase("1")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityScreen.this);
                builder.setMessage(" Saved ")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
//                                if (DocNo.equalsIgnoreCase ("")){
                                startActivity (new Intent (ActivityScreen.this, DashboardActivity.class));
                                    finish();
//                                } else {
//                                    startActivity (new Intent (ActivityScreen.this, ViewActivity.class));
//                                    finish ();
//                                }
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            } else if (launch.equalsIgnoreCase("2")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityScreen.this);
                builder.setMessage(" Updated ")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                if (DocNo.equalsIgnoreCase ("")){
                                    finish();
                                } else {
                                    startActivity (new Intent (ActivityScreen.this, ViewActivity.class));
                                    finish ();
                                }
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ActivityScreen.this);
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
}
