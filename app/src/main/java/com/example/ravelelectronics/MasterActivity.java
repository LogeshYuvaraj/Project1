package com.example.ravelelectronics;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ravelelectronics.model.CurrencyModel;
import com.example.ravelelectronics.model.GroupModel;
import com.example.ravelelectronics.spinnerclasses.CurrencyList_Spinner;
import com.example.ravelelectronics.spinnerclasses.GroupList_Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MasterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    MaterialSpinner  Currency_spin, Group_spin;
    Spinner Custype_spinner;
    EditText Code_et, Name_et, ConName_et, Email_et, ConNo_et;
    EditText group_et,currency_et,grpCode_et,currCode_et;
    String[] Custypespin = {"Lead"};
    String Custype="";
    Button save, cancel;
    ProgressDialog pDialog;
    JSONArray jarraylist,jarraylist1;
    String Currname = "", Currcode = "";
    String GroupName = "", GroupCode = "",Master_Code="";
    SessionManagement session;
    String sessionempid="";
    String Groupspin = "", Currencyspin = "";
    List<String> CurrencyModel1 = new ArrayList<>();
    ArrayList<CurrencyModel> currencyModelArrayList;
    String strfault = "", launch = "";

    List<String> GroupModel1 = new ArrayList<>();
    ArrayList<GroupModel> groupModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);

        Custype_spinner =  findViewById(R.id.Custype_spinner);
        ConNo_et =  findViewById(R.id.ConNo_et);
        Currency_spin =  findViewById(R.id.Currency_spin);
        Group_spin =  findViewById(R.id.Group_spin);
        group_et =  findViewById(R.id.group_et);
        currency_et =  findViewById(R.id.currency_et);
        Code_et = (EditText) findViewById(R.id.Code_et);
        Name_et = (EditText) findViewById(R.id.Name_et);
//        ConName_et = (EditText) findViewById(R.id.ConName_et);
        Email_et = (EditText) findViewById(R.id.Email_et);
        grpCode_et = (EditText) findViewById(R.id.grpCode_et);
        currCode_et =  findViewById(R.id.currCode_et);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small> Master </small>"));
        }
//        Custype_spinner.setOnItemSelectedListener(this);

        new GetLeadNextNumber().execute();

//        Custype_spinner.setItems(Custypespin);

        save = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        group_et.setOnClickListener (this);
        currency_et.setOnClickListener (this);
        currencyModelArrayList = new ArrayList<>();
        groupModelArrayList = new ArrayList<>();

        session = new SessionManagement(MasterActivity.this);
        HashMap<String, String> users = session.getUserDetails();
        sessionempid = users.get(SessionManagement.KEY_CODE);

        group_et.setText ("DOMESTIC CUSTOMER");
        grpCode_et.setText ("104");
        currency_et.setText ("Indian Rupee");
        currCode_et.setText ("INR");

//        new GetCurrency().execute();
//        currency_et.setText(Currname);
//        Custype_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
//                Custype=item.toString();
//                Log.e("Custype",Custype);
//            }
//        });

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Custypespin);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Custype_spinner.setAdapter(aa);

        Custype_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Custype = Custype_spinner.getSelectedItem().toString();
                Log.e("Custype", Custype);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Currency_spin.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Currname = item.toString();
                Log.e("Currname", Currname);
                Currcode = currencyModelArrayList.get(position).getCurrcode();
                Log.e("Currcode", Currcode);
            }
        });

//
//        Currency_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                Currname = Currency_spin.getSelectedItem().toString();
//                Log.e("Currname", Currname);
//                Currcode = currencyModelArrayList.get(position).getCurrcode();
//                Log.e("Currcode", Currcode);
//
//            }
//
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
  Group_spin.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                GroupName = item.toString();
                Log.e("GroupName", GroupName);
                GroupCode = groupModelArrayList.get(position).getGroupCode();
                Log.e("GroupCode", GroupCode);
            }
        });

//        Group_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                GroupName = Group_spin.getSelectedItem().toString();
//                Log.e("GroupName", GroupName);
//                GroupCode = groupModelArrayList.get(position).getGroupCode();
//                Log.e("GroupCode", GroupCode);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

  }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && data != null) {
            try {
                GroupCode = data.getStringExtra ("GroupCode");
                GroupName = data.getStringExtra ("GroupName");
                group_et.setText (""+GroupName);
                grpCode_et.setText (""+GroupCode);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (requestCode == 2 && data != null) {
            try {
                Currcode = data.getStringExtra ("Currcode");
                Currname = data.getStringExtra ("Currname");
                currency_et.setText (""+Currname);
                currCode_et.setText (""+Currcode);
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

    private class GetCurrency extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MasterActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSALES_GetCurrency";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSALES_GetCurrency";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSALES_GetCurrency";
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

                     Currcode=jsonObject.getString("Currcode");
                     Currname=jsonObject.getString("Currname");

                    CurrencyModel model = new CurrencyModel();
                    model.setCurrcode(jsonObject.getString("Currcode"));
                    model.setCurrname(jsonObject.getString("Currname"));
                    currencyModelArrayList.add(model);
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
                Toast.makeText(MasterActivity.this, "No List", Toast.LENGTH_SHORT).show();
            } else {
                currency_et.setText(Currname);
                if (ConnectivityReceiver.isConnected(MasterActivity.this)) {
                    new GetGroup().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Check Your Internet Connection!!!", Toast.LENGTH_SHORT).show();
                }
//                Log.e("CURRENCY", "" + currencyModelArrayList.size());
//                for (int i = 0; i < currencyModelArrayList.size(); i++) {
//                    CurrencyModel1.add(currencyModelArrayList.get(i).getCurrname());
//                    Log.e("Currname", currencyModelArrayList.get(i).getCurrname());
//                }
//
//                ArrayAdapter a = new ArrayAdapter(MasterActivity.this, android.R.layout.simple_spinner_dropdown_item, CurrencyModel1);
////                Currency_spin.setAdapter(a);
//                Currency_spin.setItems(CurrencyModel1);

            }


        }
    }

    private class GetGroup extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MasterActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSALES_GetCustomerGroup";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSALES_GetCustomerGroup";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSALES_GetCustomerGroup";
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

                jarraylist1 = new JSONArray(responseJSON);

                for (int l = 0; l < jarraylist1.length(); l++) {
                    JSONObject jsonObject = jarraylist1.getJSONObject(l);
                    GroupModel model = new GroupModel();

                    GroupCode=jsonObject.getString("GroupCode");
                    GroupName=jsonObject.getString("GroupName");

                    model.setGroupCode(jsonObject.getString("GroupCode"));
                    model.setGroupName(jsonObject.getString("GroupName"));
                    groupModelArrayList.add(model);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (jarraylist1.length() == 0) {
                Toast.makeText(MasterActivity.this, "No List", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("Group", "" + groupModelArrayList.size());
                for (int i = 0; i < groupModelArrayList.size(); i++) {
                    GroupModel1.add(groupModelArrayList.get(i).getGroupName());
                    Log.e("GroupName", groupModelArrayList.get(i).getGroupName());
                }
                ArrayAdapter a = new ArrayAdapter(MasterActivity.this, android.R.layout.simple_spinner_dropdown_item, GroupModel1);
                Group_spin.setAdapter(a);
            }
            group_et.setText(GroupName);

        }
    }

    private class GetLeadNextNumber extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MasterActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSALES_GetLeadNextNumber";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSALES_GetLeadNextNumber";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSALES_GetLeadNextNumber";
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

                jarraylist1 = new JSONArray(responseJSON);

                for (int l = 0; l < jarraylist1.length(); l++) {
                    JSONObject jsonObject = jarraylist1.getJSONObject(l);

                    Master_Code=jsonObject.getString("Master_Code");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (jarraylist1.length() == 0) {
                Toast.makeText(MasterActivity.this, "No List", Toast.LENGTH_SHORT).show();
            } else {
                Code_et.setText(Master_Code);
            }

        }
    }

    @Override
    public void onClick(View v) {
        if (v == group_et) {
            Intent intent = new Intent (MasterActivity.this, GroupList_Spinner.class);
            startActivityForResult(intent, 1);
        }
        if (v == currency_et) {
            Intent intent = new Intent (MasterActivity.this, CurrencyList_Spinner.class);
            intent.putExtra ("ScreenStatus","AA");
            intent.putExtra ("position1", ""+"");
            startActivityForResult(intent, 2);
        }
        if (Currency_spin==v){
//            if (customerModelArrayList.size()==0){
//                new GetCustomer().execute();
//            } else {
//                Log.e("XXX","");
//            }
        }
        if (Group_spin==v){
//            if (activityModelArrayList.size()==0) {
//                new activity().execute();
//            } else {
//                Log.e("XXX","");
//            }
        }
        if (v == save) {
            if(Code_et.getText().toString().trim().equalsIgnoreCase("")){
                Toast.makeText(this, "Kindly enter Code", Toast.LENGTH_SHORT).show();
            }else if(Name_et.getText().toString().trim().equalsIgnoreCase("")){
                Toast.makeText(this, "Kindly enter Name", Toast.LENGTH_SHORT).show();
            }
//            else if(ConName_et.getText().toString().trim().equalsIgnoreCase("")){
//                Toast.makeText(this, "Kindly enter ContactName", Toast.LENGTH_SHORT).show();
//            }
            else  if(Email_et.getText().toString().trim().equalsIgnoreCase("")){
                Toast.makeText(this, "Kindly enter Email", Toast.LENGTH_SHORT).show();
            }else if(ConNo_et.getText().toString().trim().equalsIgnoreCase("")){
                Toast.makeText(this, "Kindly enter ContactNo", Toast.LENGTH_SHORT).show();
            }else if(Custype.equalsIgnoreCase("")){
                Toast.makeText(this, "Kindly Select Customer Type", Toast.LENGTH_SHORT).show();
            }else if(group_et.getText().toString().equalsIgnoreCase("")){
                Toast.makeText(this, "Kindly Select Group", Toast.LENGTH_SHORT).show();
            }else if(currency_et.getText().toString().equalsIgnoreCase("")){
                Toast.makeText(this, "Kindly Select Currency", Toast.LENGTH_SHORT).show();
            }else {
                new masteractivity().execute();
            }
        }
        if (v==cancel){
            finish();
        }
    }

    private class masteractivity extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(MasterActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_AddOCRD";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_AddOCRD";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_AddOCRD";
                Log.e("URL", URL);

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("CardCode", "" + Code_et.getText().toString());
                Log.e("CardCode", "" + Code_et.getText().toString());

                Request.addProperty("CardName", "" + Name_et.getText().toString());
                Log.e("CardName", Name_et.getText().toString());

                Request.addProperty("CusGroup", "" + group_et.getText().toString());
                Log.e("CusGroup", "" + group_et.getText().toString());

                Request.addProperty("Currency", "" + currency_et.getText().toString());
                Log.e("Currency", "" + currency_et.getText().toString());

                String Contactno=ConNo_et.getText().toString().trim();
                Log.e("Contactno",Contactno);

//                if(ConNo_et.getText().toString().equalsIgnoreCase("")) {
//                    ContactNo= 0;
//                }
//                else {
//                    ContactNo= Integer.parseInt(ConNo_et.getText().toString());
//                }
                Request.addProperty("ContactNo", "" +Contactno );
                Log.e("ContactNo", "" + Contactno);

                Request.addProperty("ContactMail", "" + Email_et.getText().toString());
                Log.e("ContactMail", "" + Email_et.getText().toString());

                Request.addProperty("CardType", ""+Custype);
                Log.e("CardType", ""+Custype);

                Request.addProperty("User", ""+sessionempid);
                Log.e("User",""+sessionempid );

                Request.addProperty("CurrCode", ""+currCode_et.getText().toString());
                Log.e("CurrCode", ""+currCode_et.getText().toString());

                Request.addProperty("CusGroupCode", ""+grpCode_et.getText().toString());
                Log.e("CusGroupCode",""+grpCode_et.getText().toString());

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
                AlertDialog.Builder builder = new AlertDialog.Builder(MasterActivity.this);
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
            } else {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MasterActivity.this);
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