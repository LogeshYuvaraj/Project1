package com.example.ravelelectronics;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ravelelectronics.model.PrjEmployeeModel;
import com.example.ravelelectronics.model.TargetdurationModel;
import com.example.ravelelectronics.spinnerclasses.DurationList_Spinner;
import com.example.ravelelectronics.spinnerclasses.EmployeeList_Spinner;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SalesmanTarget extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    EditText TarPotenAmt_et,Targetstrdate_et, TargetEnddate_et, docdate_et, description_et, weekly_et, Week1_et, Week1_et1, Week2_et, Week2_et1, Week3_et,
            Week3_et1, Week4_et, Week4_et1, Month1_et, Month1_et1, Month2_et, Month2_et1, Month3_et, Month3_et1,
            Month1_Et, Month1_Et1, Month2_Et, Month2_Et1, Month3_Et, Month3_Et1, Month4_Et, Month4_Et1, Month5_Et, Month5_Et1,
            Month6_Et, Month6_Et1, Y_Month1_Et, Y_Month1_Et1, Y_Month2_Et, Y_Month2_Et1, Y_Month3_Et, Y_Month3_Et1, Y_Month4_Et,
            Y_Month4_Et1, Y_Month5_Et, Y_Month5_Et1, Y_Month6_Et, Y_Month6_Et1, Y_Month7_Et, Y_Month7_Et1, Y_Month8_Et, Y_Month8_Et1,
            Y_Month9_Et, Y_Month9_Et1, Y_Month10_Et, Y_Month10_Et1, Y_Month11_Et, Y_Month11_Et1, Y_Month12_Et, Y_Month12_Et1;
    MaterialSpinner targetspinner, TarStatusSpin, SaleEmpSpin;
    TextView editimage;
    EditText duration_et,employee_et;
    String TarStatus="";
    String editstatus="",DocNo="";
    Button savebtn, cancelbtn;
    LinearLayout Weekly_LL;
    SessionManagement session;
    String TargetDUration = "", sessionempid = "";
    ProgressDialog pDialog;
    String strfault = "", launch = "";
    JSONArray jarraylist,jarraylist1;
    DatePickerDialog dp;
    DatePickerDialog dp1;
    JSONArray jsonArray;

    String EmpName = "", empID = "", slpname = "";
    String SalesTargetNo="",SalesTargetDate="",TargetDesc="",SalesTargetStartDate="",SalesTargetEndDate="",TarDuration="",TargetStatus="",
            TarPotenAmt="",SaleEmp="",CreatedBy="",CreateDate="",SaleempName="";
    String[] statusspin = {"Select Type", "Open", "Hold","Partially","Completed"};
    List<String> TargetdurationModel1 = new ArrayList<>();
    ArrayList<TargetdurationModel> targetdurationModelArrayList;

    List<String> PrjEmployeeModel1 = new ArrayList<>();
    ArrayList<PrjEmployeeModel> prjEmployeeModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesman_target);

        Weekly_LL = (LinearLayout) findViewById(R.id.Weekly_LL);
        Targetstrdate_et = (EditText) findViewById(R.id.Targetstrdate_et);
        TargetEnddate_et = (EditText) findViewById(R.id.TargetEnddate_et);
        TarPotenAmt_et = (EditText) findViewById(R.id.TarPotenAmt_et);

        duration_et = (EditText) findViewById(R.id.duration_et);
        employee_et = (EditText) findViewById(R.id.employee_et);

        savebtn = (Button) findViewById(R.id.savebtn);
        cancelbtn = (Button) findViewById(R.id.cancelbtn);
        description_et = (EditText) findViewById(R.id.description_et);
        weekly_et = (EditText) findViewById(R.id.weekly_et);
        Week1_et = (EditText) findViewById(R.id.Week1_et);
        Week1_et1 = (EditText) findViewById(R.id.Week1_et1);
        Week2_et = (EditText) findViewById(R.id.Week2_et);
        Week2_et1 = (EditText) findViewById(R.id.Week2_et1);
        Week3_et = (EditText) findViewById(R.id.Week3_et);
        Week3_et1 = (EditText) findViewById(R.id.Week3_et1);
        Week4_et = (EditText) findViewById(R.id.Week4_et);
        Week4_et1 = (EditText) findViewById(R.id.Week4_et1);
        Month1_Et = (EditText) findViewById(R.id.Month1_Et);
        Month1_Et1 = (EditText) findViewById(R.id.Month1_Et1);
        Month2_Et = (EditText) findViewById(R.id.Month2_Et);
        Month2_Et1 = (EditText) findViewById(R.id.Month2_Et1);
        Month1_et = (EditText) findViewById(R.id.Month1_et);
        Month1_et1 = (EditText) findViewById(R.id.Month1_et1);
        Month2_et = (EditText) findViewById(R.id.Month2_et);
        Month2_et1 = (EditText) findViewById(R.id.Month2_et1);
        Month3_et = (EditText) findViewById(R.id.Month3_et);
        Month3_et1 = (EditText) findViewById(R.id.Month3_et1);
        Month3_Et = (EditText) findViewById(R.id.Month3_Et);
        Month3_Et1 = (EditText) findViewById(R.id.Month3_Et1);

        Month4_Et = (EditText) findViewById(R.id.Month4_Et);
        Month4_Et1 = (EditText) findViewById(R.id.Month4_Et1);
        Month5_Et = (EditText) findViewById(R.id.Month5_Et);
        Month5_Et1 = (EditText) findViewById(R.id.Month5_Et1);
        Month6_Et = (EditText) findViewById(R.id.Month6_Et);

        Month6_Et1 = (EditText) findViewById(R.id.Month6_Et1);
        Y_Month1_Et = (EditText) findViewById(R.id.Y_Month1_Et);
        Y_Month1_Et1 = (EditText) findViewById(R.id.Y_Month1_Et1);
        Y_Month2_Et = (EditText) findViewById(R.id.Y_Month2_Et);
        Y_Month2_Et1 = (EditText) findViewById(R.id.Y_Month2_Et1);


        Y_Month3_Et = (EditText) findViewById(R.id.Y_Month3_Et);
        Y_Month3_Et1 = (EditText) findViewById(R.id.Y_Month3_Et1);
        Y_Month4_Et = (EditText) findViewById(R.id.Y_Month4_Et);
        Y_Month4_Et1 = (EditText) findViewById(R.id.Y_Month4_Et1);
        Y_Month5_Et = (EditText) findViewById(R.id.Y_Month5_Et);

        Y_Month5_Et1 = (EditText) findViewById(R.id.Y_Month5_Et1);
        Y_Month6_Et = (EditText) findViewById(R.id.Y_Month6_Et);
        Y_Month6_Et1 = (EditText) findViewById(R.id.Y_Month6_Et1);
        Y_Month7_Et = (EditText) findViewById(R.id.Y_Month7_Et);
        Y_Month7_Et1 = (EditText) findViewById(R.id.Y_Month7_Et1);

        Y_Month8_Et = (EditText) findViewById(R.id.Y_Month8_Et);
        Y_Month8_Et1 = (EditText) findViewById(R.id.Y_Month8_Et1);
        Y_Month9_Et = (EditText) findViewById(R.id.Y_Month9_Et);
        Y_Month9_Et1 = (EditText) findViewById(R.id.Y_Month9_Et1);
        Y_Month10_Et = (EditText) findViewById(R.id.Y_Month10_Et);

        Y_Month10_Et1 = (EditText) findViewById(R.id.Y_Month10_Et1);
        Y_Month11_Et = (EditText) findViewById(R.id.Y_Month11_Et);
        Y_Month11_Et1 = (EditText) findViewById(R.id.Y_Month11_Et1);
        Y_Month12_Et = (EditText) findViewById(R.id.Y_Month12_Et);
        Y_Month12_Et1 = (EditText) findViewById(R.id.Y_Month12_Et1);
        editimage=findViewById(R.id.editimage);

        TarStatusSpin =  findViewById(R.id.TarStatusSpin);
        SaleEmpSpin =  findViewById(R.id.SaleEmpSpin);

        Intent intent=getIntent();
        DocNo=intent.getStringExtra("DocNo");
        editstatus=intent.getStringExtra("editstatus");
        Log.e("editstatus",""+editstatus);

        TarStatusSpin.setItems(statusspin);
        TarStatusSpin.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                TarStatus=item.toString();
                Log.e("TarStatus",TarStatus);
            }
        });

        targetspinner =  findViewById(R.id.targetspinner);
        savebtn.setOnClickListener(this);
        employee_et.setOnClickListener(this);
        duration_et.setOnClickListener(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>Salesman Target</small>"));
        }
        targetdurationModelArrayList = new ArrayList<>();

        session = new SessionManagement(SalesmanTarget.this);
        HashMap<String, String> users = session.getUserDetails();
        sessionempid = users.get(SessionManagement.KEY_CODE);

        prjEmployeeModelArrayList = new ArrayList<>();

        if (editstatus.equalsIgnoreCase("0")){
            Log.e("From","Dashboard");
        } else {
            savebtn.setText ("Update");
            new GETSalesmanTargetDetails().execute();
            Log.e("From","Edit");
        }

        Targetstrdate_et.setFocusable(false);
        TargetEnddate_et.setFocusable(false);

        Targetstrdate_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                final int y = c.get(Calendar.YEAR);
                final int m = c.get(Calendar.MONTH);
                final int d = c.get(Calendar.DAY_OF_MONTH);

                dp = new DatePickerDialog(SalesmanTarget.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                Calendar dateSelected = Calendar.getInstance();
                                dateSelected.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat formatterfrm = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
                                Targetstrdate_et.setText(formatterfrm.format(dateSelected.getTime()));
                                Log.d("Fromdate", Targetstrdate_et.getText().toString());
                            }

                        }, y, m, d);
                dp.show();
            }
        });

        TargetEnddate_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                final int y = c.get(Calendar.YEAR);
                final int m = c.get(Calendar.MONTH);
                final int d = c.get(Calendar.DAY_OF_MONTH);

                dp1 = new DatePickerDialog(SalesmanTarget.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {


                                Calendar dateSelected = Calendar.getInstance();
                                dateSelected.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat formatterfrm = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);

                                TargetEnddate_et.setText(formatterfrm.format(dateSelected.getTime()));
                                Log.d("Todate", TargetEnddate_et.getText().toString());
                            }

                        }, y, m, d);
                dp1.show();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && data != null) {
            try {
                TargetDUration = data.getStringExtra ("TargetDUration");
                duration_et.setText (""+TargetDUration);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (requestCode == 2 && data != null) {
            try {
                EmpName = data.getStringExtra ("EmpName");
                empID = data.getStringExtra ("empID");
//                slpname = data.getStringExtra ("slpname");
                employee_et.setText (""+EmpName);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {

        if (v == duration_et) {
            Intent intent = new Intent (SalesmanTarget.this, DurationList_Spinner.class);
            startActivityForResult(intent, 1);
        }
        if (v == employee_et) {
            Intent intent = new Intent (SalesmanTarget.this, EmployeeList_Spinner.class);
            startActivityForResult(intent, 2);
        }
        if (v == savebtn) {
            TarStatus = TarStatusSpin.getText ().toString ();
            if (Targetstrdate_et.getText().toString().trim().equalsIgnoreCase("")){
                Toast.makeText(this, "Kindly enter TargetStartdate", Toast.LENGTH_SHORT).show();
            } else if (TargetEnddate_et.getText().toString().trim().equalsIgnoreCase("")){
                Toast.makeText(this, "Kindly enter TargetEnddate", Toast.LENGTH_SHORT).show();
            } else if (description_et.getText().toString().trim().equalsIgnoreCase("")){
                Toast.makeText(this, "Kindly enter Description", Toast.LENGTH_SHORT).show();
            } else if (TarPotenAmt_et.getText().toString().trim().equalsIgnoreCase("")){
                Toast.makeText(this, "Kindly enter Potential Amount", Toast.LENGTH_SHORT).show();
            } else if (TarStatus.equalsIgnoreCase("Select Type")){
                Toast.makeText(this, "Kindly Select Target Status", Toast.LENGTH_SHORT).show();
            } else if (TargetDUration.equalsIgnoreCase("Select Type")){
                Toast.makeText(this, "Kindly Select Target Duration", Toast.LENGTH_SHORT).show();
            } else if (EmpName.equalsIgnoreCase("Select Type")){
                Toast.makeText(this, "Kindly Select SalesEmp", Toast.LENGTH_SHORT).show();
            } else {
                new AddTarget().execute();
            }
        }
        if (v==cancelbtn){
            finish();
        }
    }

    private class GETSalesmanTargetDetails extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SalesmanTarget.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_GETSalesmanTargetDetails";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_GETSalesmanTargetDetails";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_GETSalesmanTargetDetails";
                Log.e("URL", URL);

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("DocNo", ""+DocNo);
                Log.e("DocNo", ""+DocNo);


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

                jsonArray = new JSONArray(responseJSON);

                for (int l = 0; l < jsonArray.length(); l++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(l);

                    SalesTargetNo=jsonObject.getString("SalesTarget No.");
                    SalesTargetDate=jsonObject.getString("SalesTarget Date");
                    TargetDesc=jsonObject.getString("TargetDesc");
                    SalesTargetStartDate=jsonObject.getString("SalesTarget Start Date");
                    SalesTargetEndDate=jsonObject.getString("SalesTarget End Date");
                    TarDuration=jsonObject.getString("TarDuration");
                    TargetStatus=jsonObject.getString("TarStatus");
                    TarPotenAmt=jsonObject.getString("TarPotenAmt");
                    empID=jsonObject.getString("SaleEmp");
                    CreatedBy=jsonObject.getString("CreatedBy");
                    CreateDate=jsonObject.getString("CreateDate");
                    SaleempName=jsonObject.getString("SaleempName");

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
                Toast.makeText(SalesmanTarget.this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
            } else {
                description_et.setText(TargetDesc);
                Targetstrdate_et.setText(SalesTargetStartDate);
                TargetEnddate_et.setText(SalesTargetEndDate);
                duration_et.setText(TarDuration);
                TarStatusSpin.setText(TargetStatus);
                TarPotenAmt_et.setText(TarPotenAmt);
                employee_et.setText(SaleempName);
            }
        }
    }

    private class AddTarget extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(SalesmanTarget.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_AddTarget";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_AddTarget";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_AddTarget";
                Log.e("URL", URL);

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                if(DocNo.equalsIgnoreCase("")){
                    Request.addProperty("DocNo", "0" );
                    Log.e("DocNo", "0");
                }else {
                    Request.addProperty("DocNo", "" +DocNo);
                    Log.e("DocNo", "" + DocNo);
                }

                Request.addProperty("TargetDesc", "" + description_et.getText().toString());
                Log.e("TargetDesc", "" + description_et.getText().toString());

                Request.addProperty("TarStartDt", "" + Targetstrdate_et.getText().toString());
                Log.e("TarStartDt", Targetstrdate_et.getText().toString());

                Request.addProperty("TarEndDt", "" + TargetEnddate_et.getText().toString());
                Log.e("TarEndDt", "" + TargetEnddate_et.getText().toString());

                Request.addProperty("TarDuration", "" + TargetDUration);
                Log.e("TarDuration", "" + TargetDUration);

                Request.addProperty("TarStatus", ""+TarStatus);
                Log.e("TarStatus", ""+ TarStatus);

                Request.addProperty("TarPotenAmt", ""+TarPotenAmt_et.getText().toString());
                Log.e("TarPotenAmt", ""+ TarPotenAmt_et.getText().toString());

                Request.addProperty("SaleEmp", ""+empID);
                Log.e("SaleEmp", ""+empID);

                Request.addProperty("User", "" + sessionempid);
                Log.e("User", "" + sessionempid);

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
                AlertDialog.Builder builder = new AlertDialog.Builder(SalesmanTarget.this);
                builder.setMessage(" Saved ")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            } else if (launch.equalsIgnoreCase("2")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SalesmanTarget.this);
                builder.setMessage(" Updated ")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(SalesmanTarget.this);
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









