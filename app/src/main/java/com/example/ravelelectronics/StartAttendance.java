package com.example.ravelelectronics;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ravelelectronics.model.CustomerModel;
import com.example.ravelelectronics.util.SessionManagement;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StartAttendance extends AppCompatActivity implements View.OnClickListener {
    TextView empcode_Tv,empname_Tv;
    Spinner customer_spin;
    Button start_button;
    ProgressDialog pDialog;
    SessionManagement session;
    String sessionempid="";
    String sessionempcode="",sessionempname="";
    String strfault = "", launch = "";
    JSONArray jsonArray;
    String CardCode = "", CardName = "", SlpCode = "", Column2 = "",Column1="";
    List<String> CustomerModel1 = new ArrayList<>();
    ArrayList<CustomerModel> customerModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_attendance);
        empcode_Tv = (TextView) findViewById(R.id.empcode_Tv);
        empname_Tv = (TextView) findViewById(R.id.empname_Tv);

        customer_spin = (Spinner) findViewById(R.id.customer_spin);
        customerModelArrayList = new ArrayList<>();


        start_button = (Button) findViewById(R.id.start_button);
        start_button.setOnClickListener(this);
        session = new SessionManagement(StartAttendance.this);
        HashMap<String, String> user = session.getUserDetails();
        sessionempcode = user.get(SessionManagement.KEY_CODE);
        sessionempname = user.get(SessionManagement.KEY_NAME);
        sessionempid = user.get(SessionManagement.KEY_CODE);

        empname_Tv.setText(sessionempname);
        empcode_Tv.setText(sessionempcode);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>  Start Attendance </small>"));
        }
        new GetCustomer().execute();

        customer_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                CardName=customer_spin.getSelectedItem().toString();
                Log.e("CardName",CardName);
                CardCode=customerModelArrayList.get(position).getCardCode();
                Log.e("CardCode",CardCode);
                Column1 = customerModelArrayList.get(position).getColumn1();
                Log.e("Column1", Column1);
                Column2 = customerModelArrayList.get(position).getColumn2();
                Log.e("Column2", Column2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
    private  class GetCustomer extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(StartAttendance.this);
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
                SoapPrimitive resultString = (SoapPrimitive) soapEnvelope
                        .getResponse();
                String responseJSON = resultString.toString();

                Log.e("RESPONSE", responseJSON);

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
                Toast.makeText(StartAttendance.this, "No List", Toast.LENGTH_SHORT).show();
            } else {

                Log.e("CUSTOMER", "" + customerModelArrayList.size());
                for (int i = 0; i < customerModelArrayList.size(); i++) {
                    CustomerModel1.add(customerModelArrayList.get(i).getCardName());
                    Log.e("CardName", customerModelArrayList.get(i).getCardName());
                }

                ArrayAdapter a = new ArrayAdapter(StartAttendance.this, android.R.layout.simple_spinner_dropdown_item, CustomerModel1);
                customer_spin.setAdapter(a);

            }
        }
    }
    @Override
    public void onClick(View view) {
        if (view == start_button) {
//            if(CardName.equalsIgnoreCase("")){
//                Toast.makeText(this, "Kindly Select Any Customer", Toast.LENGTH_SHORT).show();
//            }else {
//
                new Startattendance().execute();
//            }
        }

    }


    private class Startattendance extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StartAttendance.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_AddAttendance";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_AddAttendance";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_AddAttendance";
                Log.e("URL", URL);
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);


                Request.addProperty("CardCode", ""+CardCode );
                Log.e("CardCode", CardCode);
                Request.addProperty("CardName", ""+CardName );
                Log.e("CardName", CardName);
                Request.addProperty("SlpCode", ""+0);
                Log.e("SlpCode", ""+0);
                Request.addProperty("User", ""+sessionempid);
                Log.e("User",""+sessionempid );

                Request.addProperty("StartStop", "Start");
                Log.e("StartStop","Start" );
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
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(StartAttendance.this);
                builder.setMessage("Attendance Added Successfully")
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
                AlertDialog.Builder builder1 = new AlertDialog.Builder(StartAttendance.this);
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


