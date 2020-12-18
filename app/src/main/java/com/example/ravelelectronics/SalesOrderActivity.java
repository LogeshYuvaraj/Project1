package com.example.ravelelectronics;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ravelelectronics.model.CustomerModel;
import com.example.ravelelectronics.model.ItemModel;
import com.example.ravelelectronics.model.PlaceofsuppltModel;
import com.example.ravelelectronics.model.QuoteStatusModel;
import com.example.ravelelectronics.model.SalesOrderModel;
import com.example.ravelelectronics.model.TaxcodeModel;
import com.example.ravelelectronics.model.WhseModel;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SalesOrderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    TextView docnum_Tv, total_Tv;
    EditText edtcuscode, edtremarks, Tbefdisc_et;
    Spinner edtstatus, placeofsupply_spin, taxcode_spin, customer_spin, itemspin, wrhsspin,salesemp_spin;
    Button save, cancel, add, clear;
    String itemcode = "", itemname = "", SalUnitMsr = "", lastquoteprice = "0", HSN = "", Uom = "";
    String PriceofDis="0",Total="0",TotalBefDisc="0",TaxAmt="0";
    String State="";

    EditText Quantity_et, price_et, total_et;
    RecyclerView SalesOrderrecycler;
    RecyclerView.Adapter Adapter;
    ArrayList<SalesOrderModel> salesOrderModelArrayList;


    List<String> PlaceofsuppltModel1 = new ArrayList<>();
    ArrayList<PlaceofsuppltModel> placeofsuppltModelArrayList;

    List<String> QuoteStatusModel1 = new ArrayList<>();
    ArrayList<QuoteStatusModel> quoteStatusModelArrayList;

    List<String> TaxcodeModel1 = new ArrayList<>();
    ArrayList<TaxcodeModel> taxcodeModelArrayList;

    String CardCode = "", CardName = "", SlpCode = "",Column2 = "",Column1="";
    String WhsCode = "", WhsName = "";
    List<String> CustomerModel1 = new ArrayList<>();
    ArrayList<CustomerModel> customerModelArrayList;

    String strfault = "", launch = "";
    SessionManagement session;
    String sessionempid = "", QuoteStatus = "", code = "",sessionempname="";
    ProgressDialog pDialog;
    JSONArray jsonArray1, jsonArray2, jsonArray, jarraylist1, jarraylist2, jsonArray5;

    List<String> ItemModel1 = new ArrayList<>();
    ArrayList<ItemModel> itemModelArrayList;

    List<String> WhseModel1 = new ArrayList<>();
    ArrayList<WhseModel> whseModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_order);


        placeofsupply_spin = (Spinner) findViewById(R.id.placeofsupply_spin);
        itemspin = findViewById(R.id.itemspin);
        wrhsspin = (Spinner) findViewById(R.id.wrhsspin);
        Tbefdisc_et = (EditText) findViewById(R.id.Tbefdisc_et);
        taxcode_spin = (Spinner) findViewById(R.id.taxcode_spin);
        salesemp_spin = (Spinner) findViewById(R.id.salesemp_spin);
        total_et = findViewById(R.id.total_et);
        Quantity_et = findViewById(R.id.Quantity_et);
        price_et = findViewById(R.id.price_et);

        customer_spin = (Spinner) findViewById(R.id.customer_spin);
        edtcuscode = (EditText) findViewById(R.id.edtcuscode);
        edtremarks = (EditText) findViewById(R.id.edtremarks);
        SalesOrderrecycler = findViewById(R.id.SalesOrderrecycler);
        edtstatus = (Spinner) findViewById(R.id.edtstatus);

        save = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);
        add = (Button) findViewById(R.id.add);
        clear = (Button) findViewById(R.id.clear);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        add.setOnClickListener(this);
        clear.setOnClickListener(this);

        itemspin.setOnItemSelectedListener(this);
        wrhsspin.setOnItemSelectedListener(this);
        placeofsupply_spin.setOnItemSelectedListener(this);
        salesemp_spin.setOnItemSelectedListener(this);

        quoteStatusModelArrayList = new ArrayList<>();
        taxcodeModelArrayList = new ArrayList<>();
        customerModelArrayList = new ArrayList<>();
        salesOrderModelArrayList = new ArrayList<>();

        itemModelArrayList = new ArrayList<>();
        whseModelArrayList = new ArrayList<>();
        placeofsuppltModelArrayList = new ArrayList<>();

        session = new SessionManagement(SalesOrderActivity.this);
        HashMap<String, String> users = session.getUserDetails();
        sessionempid = users.get(SessionManagement.KEY_CODE);
        sessionempname = users.get(SessionManagement.KEY_NAME);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small> SalesOrder </small>"));
        }


        itemspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                itemname = itemspin.getSelectedItem().toString();
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

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        wrhsspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                WhsName = wrhsspin.getSelectedItem().toString();
                Log.e("WhsName", WhsName);
                WhsCode = whseModelArrayList.get(position).getWhsCode();
                Log.e("WhsCode", WhsCode);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        edtstatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                QuoteStatus = quoteStatusModelArrayList.get(position).getQuoteStatus();
                Log.e("QuoteStatus", QuoteStatus);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        taxcode_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                code = taxcodeModelArrayList.get(position).getCode();
                Log.e("code", code);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        placeofsupply_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                State = placeofsuppltModelArrayList.get(position).getState();
                Log.e("State", State);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        new GetCustomer().execute();

        customer_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                CardName = customer_spin.getSelectedItem().toString();
                Log.e("CardName", CardName);
                CardCode = customerModelArrayList.get(position).getCardCode();
                Log.e("CardCode", CardCode);
                Column1 = customerModelArrayList.get(position).getColumn1();
                Log.e("Column1", Column1);
                Column2 = customerModelArrayList.get(position).getColumn2();
                Log.e("Column2", Column2);
                edtcuscode.setText(CardCode);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

    public void onClick(View v) {

        if (add == v) {
            if (Quantity_et.getText().toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(this, "Kindly enter Qty", Toast.LENGTH_SHORT).show();
            } else if (price_et.getText().toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(this, "Kindly enter Price", Toast.LENGTH_SHORT).show();
            }  else if (Tbefdisc_et.getText().toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(this, "Kindly enter Discount", Toast.LENGTH_SHORT).show();
            }else {
                if(total_et.getText().toString().trim().equalsIgnoreCase(""))
                {
                    Total="0";
                }else {
                    Total=total_et.getText().toString().trim();
                }
                SalesOrderModel Model = new SalesOrderModel();
                Model.setItemcode(itemcode);
                Model.setName(itemname);
                Model.setUom(Uom);
                Model.setQuantity(Quantity_et.getText().toString().trim());
                Model.setPrice(price_et.getText().toString().trim());
                Model.setLastquotprice(lastquoteprice);
                Model.setDiscount(Tbefdisc_et.getText().toString().trim());
                Model.setPriceofDis(PriceofDis);
                Model.setTotal(Total);
                Model.setTaxcode(code);
                Model.setWhseName(WhsName);
                Model.setWhseCode(WhsCode);
                Model.setHsncode(HSN);

                salesOrderModelArrayList.add(Model);

                SalesOrderrecycler.setVisibility(View.VISIBLE);
                SalesOrderrecycler.setLayoutManager(new LinearLayoutManager(SalesOrderActivity.this));
                Adapter = new SalesOrderAdapter(getApplicationContext(), salesOrderModelArrayList);
                SalesOrderrecycler.setAdapter(Adapter);
                SalesOrderrecycler.setHasFixedSize(true);

                Quantity_et.setText("");
                price_et.setText("");
                Tbefdisc_et.setText("");
            }
        }
        if (v == save) {
            if(CardName.equalsIgnoreCase("")){
                Toast.makeText(this, "Kindly Select Any Customer", Toast.LENGTH_SHORT).show();
            } else if (salesOrderModelArrayList.size() == 0) {
                Toast.makeText(this, "Atleast Add one Row Go to save", Toast.LENGTH_SHORT).show();
            } else {
                new AddOrder().execute();
            }
        }
        if (v == clear) {
            salesOrderModelArrayList.clear();
            Adapter.notifyDataSetChanged();
            onRestart();
        }

    }

    private class GetCustomer extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(SalesOrderActivity.this);
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
                Toast.makeText(SalesOrderActivity.this, "No List", Toast.LENGTH_SHORT).show();
            } else {

                Log.e("CUSTOMER", "" + customerModelArrayList.size());
                for (int i = 0; i < customerModelArrayList.size(); i++) {
                    CustomerModel1.add(customerModelArrayList.get(i).getCardName());
                    Log.e("CardName", customerModelArrayList.get(i).getCardName());
                }

                ArrayAdapter a = new ArrayAdapter(SalesOrderActivity.this, android.R.layout.simple_spinner_dropdown_item, CustomerModel1);
                customer_spin.setAdapter(a);

                new Getplaceofsupply().execute();

            }
        }
    }

    private class Getplaceofsupply extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SalesOrderActivity.this);
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
                final String METHOD_NAME = "IndusMobileSALES_GetPlaceOfSupply";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSALES_GetPlaceOfSupply";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSALES_GetPlaceOfSupply";
                Log.e("URL", URL);
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);


                Request.addProperty("CardCode", "" + CardCode);
                Log.e("CardCode", "" + CardCode);


                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                AndroidHttpTransport aht = new AndroidHttpTransport(URL);
                aht.call(SOAP_ACTION, soapEnvelope);
                SoapPrimitive resultString = (SoapPrimitive) soapEnvelope.getResponse();

                String responseJSON = resultString.toString();
                Log.e("RESPONSE", responseJSON);

                jsonArray5 = new JSONArray(responseJSON);
                for (int i = 0; i < jsonArray5.length(); i++) {

                    JSONObject jsonObject = jsonArray5.getJSONObject(i);
                    PlaceofsuppltModel model = new PlaceofsuppltModel();
                    placeofsuppltModelArrayList.add(model);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();

//            if(jsonArray5.length() == 0){
//                Toast.makeText (SalesQuotation.this, "No List", Toast.LENGTH_SHORT).show ( );
//            } else {
//                Log.e ("CardCode", "" + placeofsuppltModelArrayList.size ());
//                for (int i = 0;i<placeofsuppltModelArrayList.size();i++) {
//
//                    PlaceofsuppltModel1.add (placeofsuppltModelArrayList.get (i).getCardCode ());
//
//                    Log.e ("CardCode", placeofsuppltModelArrayList.get (i).getCardCode ());
//                }

//                ArrayAdapter d = new ArrayAdapter(SalesQuotation.this,android.R.layout.simple_spinner_dropdown_item, PlaceofsuppltModel1);
//                placeofsupply_spin.setAdapter(d);
            new GetTaxCode().execute();

//        }
        }

    }

    private class GetTaxCode extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SalesOrderActivity.this);
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
                final String METHOD_NAME = "IndusMobileSALES_GetQuoteTaxCode";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSALES_GetQuoteTaxCode";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSALES_GetQuoteTaxCode";
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
                    TaxcodeModel model = new TaxcodeModel();
                    model.setCode(jsonObject.getString("code"));

                    taxcodeModelArrayList.add(model);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();

            if (jsonArray2.length() == 0) {
                Toast.makeText(SalesOrderActivity.this, "No List", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("code", "" + taxcodeModelArrayList.size());
                for (int i = 0; i < taxcodeModelArrayList.size(); i++) {

                    TaxcodeModel1.add(taxcodeModelArrayList.get(i).getCode());

                    Log.e("code", taxcodeModelArrayList.get(i).getCode());
                }

                ArrayAdapter d = new ArrayAdapter(SalesOrderActivity.this, android.R.layout.simple_spinner_dropdown_item, TaxcodeModel1);
                taxcode_spin.setAdapter(d);


                new GetQuoteStatus().execute();

            }
        }

    }


    private class GetQuoteStatus extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SalesOrderActivity.this);
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
                final String METHOD_NAME = "IndusMobileSALES_GetQuoteStatus";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSALES_GetQuoteStatus";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSALES_GetQuoteStatus";
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
                    QuoteStatusModel model = new QuoteStatusModel();
                    model.setQuoteStatus(jsonObject.getString("QuoteStatus"));

                    quoteStatusModelArrayList.add(model);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();

            if (jsonArray2.length() == 0) {
                Toast.makeText(SalesOrderActivity.this, "No List", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("QuoteStatus", "" + quoteStatusModelArrayList.size());
                for (int i = 0; i < quoteStatusModelArrayList.size(); i++) {

                    QuoteStatusModel1.add(quoteStatusModelArrayList.get(i).getQuoteStatus());

                    Log.e("QuoteStatus", quoteStatusModelArrayList.get(i).getQuoteStatus());
                }

                ArrayAdapter d = new ArrayAdapter(SalesOrderActivity.this, android.R.layout.simple_spinner_dropdown_item, QuoteStatusModel1);
                edtstatus.setAdapter(d);
                new Getitem().execute();
            }
        }

    }

    private class Getitem extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SalesOrderActivity.this);
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

                jarraylist1 = new JSONArray(responseJSON);

                for (int l = 0; l < jarraylist1.length(); l++) {
                    JSONObject jsonObject = jarraylist1.getJSONObject(l);
                    ItemModel model = new ItemModel();
                    model.setItemcode(jsonObject.getString("itemcode"));
                    model.setItemname(jsonObject.getString("itemname"));
                    model.setUom(jsonObject.getString("Uom"));
                    model.setSalUnitMsr(jsonObject.getString("SalUnitMsr"));
                    model.setLastquoteprice(jsonObject.getString("lastquoteprice"));
                    model.setHSN(jsonObject.getString("HSN"));
                    itemModelArrayList.add(model);
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
                Toast.makeText(SalesOrderActivity.this, "No List", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("itemname", "" + itemModelArrayList.size());
                for (int i = 0; i < itemModelArrayList.size(); i++) {
                    ItemModel1.add(itemModelArrayList.get(i).getItemname());
                    Log.e("itemname", itemModelArrayList.get(i).getItemname());
                }

                ArrayAdapter a = new ArrayAdapter(SalesOrderActivity.this, android.R.layout.simple_spinner_dropdown_item, ItemModel1);
                itemspin.setAdapter(a);
                new GetWhse().execute();


            }
        }
    }

    private class GetWhse extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SalesOrderActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSALES_GetQuoteWhse";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSALES_GetQuoteWhse";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSALES_GetQuoteWhse";
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

                jarraylist2 = new JSONArray(responseJSON);

                for (int l = 0; l < jarraylist2.length(); l++) {
                    JSONObject jsonObject = jarraylist2.getJSONObject(l);
                    WhseModel model = new WhseModel();
                    model.setWhsCode(jsonObject.getString("WhsCode"));
                    model.setWhsName(jsonObject.getString("WhsName"));

                    whseModelArrayList.add(model);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (jarraylist2.length() == 0) {
                Toast.makeText(SalesOrderActivity.this, "No List", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("WhsName", "" + whseModelArrayList.size());
                for (int i = 0; i < whseModelArrayList.size(); i++) {
                    WhseModel1.add(whseModelArrayList.get(i).getWhsName());
                    Log.e("WhsName", whseModelArrayList.get(i).getWhsName());
                }

                ArrayAdapter a = new ArrayAdapter(SalesOrderActivity.this, android.R.layout.simple_spinner_dropdown_item, WhseModel1);
                wrhsspin.setAdapter(a);
            }
        }
    }

    public class SalesOrderAdapter extends RecyclerView.Adapter {

        Context getApplicationContext;
        ArrayList<SalesOrderModel> salesOrderModelArrayList;

        public SalesOrderAdapter(Context getApplicationContext, ArrayList<SalesOrderModel> salesOrderModelArrayList) {
            this.getApplicationContext = getApplicationContext;
            this.salesOrderModelArrayList = salesOrderModelArrayList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderdetail, parent, false);
            return new SalesOrderAdapter.ViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position)
        {
            if(holder instanceof ViewHolder)
            {

                int pos=position+1;

                ( (ViewHolder) holder ).sno.setText(""+pos);
                ((ViewHolder) holder).itemcode.setText(salesOrderModelArrayList.get(position).getItemcode());
                ((ViewHolder) holder).name.setText(salesOrderModelArrayList.get(position).getName());
                ((ViewHolder) holder).UOM.setText(salesOrderModelArrayList.get(position).getUom());
                ((ViewHolder) holder).Quantity.setText(salesOrderModelArrayList.get(position).getQuantity());
                ((ViewHolder) holder).Price.setText(salesOrderModelArrayList.get(position).getPrice());

                ((ViewHolder) holder).Discount.setText(salesOrderModelArrayList.get(position).getDiscount());
                ((ViewHolder) holder).Total.setText(salesOrderModelArrayList.get(position).getTotal());
                ((ViewHolder) holder).Taxcode.setText(salesOrderModelArrayList.get(position).getTaxcode());
                ((ViewHolder) holder).WhseName.setText(salesOrderModelArrayList.get(position).getWhseName());
                ((ViewHolder) holder).HSNcode.setText(salesOrderModelArrayList.get(position).getHsncode());
                ((ViewHolder) holder).Attachment.setText(salesOrderModelArrayList.get(position).getAttachment());





            }
        }
        @Override
        public int getItemCount()
        {
            return salesOrderModelArrayList.size();
        }
        public  class ViewHolder extends RecyclerView.ViewHolder
        {
            android.widget.LinearLayout LinearLayout;
            public TextView sno,itemcode,name, UOM,Quantity,Price,Discount,Total,Taxcode,WhseName,HSNcode,Attachment;




            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                itemcode= itemView.findViewById(R.id.itemcode);
                name= itemView.findViewById(R.id.name);
                Quantity= itemView.findViewById(R.id.Quantity);
                Price= itemView.findViewById(R.id.Price);

                UOM= itemView.findViewById(R.id.UOM);

                Discount= itemView.findViewById(R.id.Discount);
                Total= itemView.findViewById(R.id.Total);
                Taxcode= itemView.findViewById(R.id.Taxcode);
                WhseName= itemView.findViewById(R.id.WhseName);
                HSNcode= itemView.findViewById(R.id.HSNcode);
                Attachment= itemView.findViewById(R.id.Attachment);



            }
        }
    }

    private class AddOrder extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(SalesOrderActivity.this);
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

                Request.addProperty("DocNo", ""+0 );
                Log.e("DocNo", "0" );

                Request.addProperty("DocDate", "");
                Log.e("DocDate", "");

                Request.addProperty("ValidDate", "" );
                Log.e("ValidDate", "" );

                Request.addProperty("CustName", "" + CardName);
                Log.e("CustName", "" +CardName );

                Request.addProperty("CustCode", "" + CardCode);
                Log.e("CustCode", "" + CardCode);

                Request.addProperty("PlaceSupp", "");
                Log.e("PlaceSupp","");

                Request.addProperty("QuoteStatus", ""+QuoteStatus);
                Log.e("QuoteStatus", ""+QuoteStatus);

                Request.addProperty("User", ""+sessionempid);
                Log.e("User",""+sessionempid);

                Request.addProperty("SaleEmpName", ""+sessionempid);
                Log.e("SaleEmpName",""+sessionempid );

                Request.addProperty("TotalBefDisc", ""+TotalBefDisc);
                Log.e("TotalBefDisc", ""+TotalBefDisc);

                Request.addProperty("TaxAmt", ""+TaxAmt);
                Log.e("TaxAmt",""+TaxAmt );

                Request.addProperty("Total", ""+Total);
                Log.e("Total",""+Total );

                String bookingitem="";
                for (int io = 0; io < salesOrderModelArrayList.size(); io++) {

                    String xmlItemCode="",xmlItemName="",xmlUOM="",xmlQty="",xmlPrice="",xmlLastQPrice="",xmlDiscount="",xmlPriceaftdisc="",
                            xmlTotal="",xmlTaxcode="",xmlWhseCode="",xmlWhseName="",xmlHsnCode="";


                    xmlItemCode = salesOrderModelArrayList.get(io).getItemcode();
                    xmlItemName = salesOrderModelArrayList.get(io).getName();
                    xmlUOM = salesOrderModelArrayList.get(io).getUom();
                    xmlQty = salesOrderModelArrayList.get(io).getQuantity();
                    xmlPrice = salesOrderModelArrayList.get(io).getPrice();
                    xmlLastQPrice = salesOrderModelArrayList.get(io).getLastquotprice();
                    xmlDiscount = salesOrderModelArrayList.get(io).getDiscount();
                    xmlPriceaftdisc = salesOrderModelArrayList.get(io).getPriceofDis();
                    xmlTotal = salesOrderModelArrayList.get(io).getTotal();
                    xmlTaxcode = salesOrderModelArrayList.get(io).getTaxcode();
                    xmlWhseCode = salesOrderModelArrayList.get(io).getWhseCode();
                    xmlWhseName = salesOrderModelArrayList.get(io).getWhseName();
                    xmlHsnCode = salesOrderModelArrayList.get(io).getHsncode();


                    int Empid= Integer.parseInt(sessionempid);
                    String date = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
                    bookingitem = bookingitem + "<Table1><RowId>"+0+"</RowId><ItemCode>"+ xmlItemCode +"</ItemCode><ItemName>" + xmlItemName + "</ItemName><UOM>" + xmlUOM + "</UOM><Qty>" + xmlQty + "</Qty><Price>" + xmlPrice + "</Price><LastQPrice>" + xmlLastQPrice + "</LastQPrice>" +
                            "<Discount>" + xmlDiscount + "</Discount><Priceaftdisc>" + xmlPriceaftdisc + "</Priceaftdisc><Total>" + xmlTotal + "</Total>" +
                            "<Taxcode>" + xmlTaxcode + "</Taxcode><WhseCode>" + xmlWhseCode + "</WhseCode><WhseName>" + xmlWhseName + "</WhseName><HsnCode>" + xmlHsnCode + "</HsnCode></Table1>";
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
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SalesOrderActivity.this);
                builder.setMessage(" Saved ")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();

                            }
                        });
                android.app.AlertDialog alert = builder.create();
                alert.show();
            } else {
                android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(SalesOrderActivity.this);
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
}