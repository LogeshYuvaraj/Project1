package com.example.ravelelectronics;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ravelelectronics.model.ViewQuotationModel;
import com.example.ravelelectronics.util.ConnectivityReceiver;
import com.example.ravelelectronics.util.SessionManagement;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ViewQuotation extends AppCompatActivity implements View.OnClickListener {
    EditText fromdate_et, todate_et;
    Button Btnsubmit;
    DatePickerDialog dp;
    DatePickerDialog dp1;
    RecyclerView viewquotationRecycle;
    TextView Sno_tv,DocNo_tv,QuoteDate_tv,Employee_tv,Customer_tv,QuoteType_tv,QuoteStatus_tv,Total_Bef_DIscount_tv,TaxAmt_tv,Total_tv;
    ArrayList<ViewQuotationModel> viewQuotationModelArrayList;
    Button editbtn,cancelbtn;
    RecyclerView.LayoutManager LinearLayoutManager;
    JSONArray jsonArray2;
    ProgressDialog pDialog;
    RecyclerView.Adapter Adapter;
    String sessionempname="",sessionempid="";
    SessionManagement session;
    String NoDataFound="";
    TextView editapprove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_quotation);

        editapprove = findViewById(R.id.editapprove);
        editapprove.setVisibility(View.GONE);

        Btnsubmit = findViewById(R.id.Btnsubmit);
        fromdate_et = findViewById(R.id.fromdate_et);
        todate_et = findViewById(R.id.todate_et);
        viewquotationRecycle = findViewById(R.id.viewquotationRecycle);
        DocNo_tv=findViewById(R.id.DocNo_tv);
        QuoteDate_tv=findViewById(R.id.QuoteDate_tv);
        Employee_tv=findViewById(R.id.Employee_tv);
        Customer_tv=findViewById(R.id.Customer_tv);
        QuoteType_tv=findViewById(R.id.QuoteType_tv);
        QuoteStatus_tv=findViewById(R.id.QuoteStatus_tv);
        Total_Bef_DIscount_tv=findViewById(R.id.Total_Bef_DIscount_tv);
        TaxAmt_tv=findViewById(R.id.TaxAmt_tv);
        Total_tv = findViewById(R.id.Total_tv);


        Btnsubmit.setOnClickListener(this);
        viewQuotationModelArrayList = new ArrayList<>();


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>View SalesQuotation  </small>"));
        }

        session = new SessionManagement(ViewQuotation.this);
        HashMap<String, String> users = session.getUserDetails();
        sessionempname = users.get(SessionManagement.KEY_NAME);
        sessionempid = users.get(SessionManagement.KEY_CODE);

        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        fromdate_et.setText(date);
        String date1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        todate_et.setText(date);

        fromdate_et.setFocusable(false);
        todate_et.setFocusable(false);

        if (ConnectivityReceiver.isConnected(ViewQuotation.this)) {
            new GETQuotationReport().execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Your Internet Connection!!!", Toast.LENGTH_SHORT).show();
        }

        fromdate_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                final int y = c.get(Calendar.YEAR);
                final int m = c.get(Calendar.MONTH);
                final int d = c.get(Calendar.DAY_OF_MONTH);

                dp = new DatePickerDialog(ViewQuotation.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                Calendar dateSelected = Calendar.getInstance();
                                dateSelected.set(year, monthOfYear, dayOfMonth);

                                SimpleDateFormat formatterfrm = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);


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

                dp1 = new DatePickerDialog(ViewQuotation.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                Calendar dateSelected = Calendar.getInstance();
                                dateSelected.set(year, monthOfYear, dayOfMonth);

                                SimpleDateFormat formatterfrm = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

                                todate_et.setText(formatterfrm.format(dateSelected.getTime()));
                                Log.d("Todate", todate_et.getText().toString());
                            }
                        }, y, m, d);
                dp1.show();
            }
        });

    }

    @Override
    public void onClick(View v) {

        new GETQuotationReport().execute();
    }


    private class GETQuotationReport extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(ViewQuotation.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_GETQuotationReport";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_GETQuotationReport";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_GETQuotationReport";
                Log.e("URL", URL);

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("User", "" + sessionempid);
                Log.e("User", "" + sessionempid);
                Request.addProperty("FromDt", ""+fromdate_et.getText().toString().trim());
                Log.e("FromDt", "" +fromdate_et.getText().toString().trim());
                Request.addProperty("ToDt", ""+todate_et.getText().toString().trim() );
                Log.e("ToDt", ""+todate_et.getText().toString().trim() );

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
                viewQuotationModelArrayList.clear();
                jsonArray2 = new JSONArray(responseJSON);

                for (int l = 0; l < jsonArray2.length(); l++) {
                    JSONObject jsonObject = jsonArray2.getJSONObject(l);
                    ViewQuotationModel model4 = new ViewQuotationModel();

                    model4.setDocNo(jsonObject.getString("DocNo"));
                    model4.setQuoteDate(jsonObject.getString("Quote Date"));
                    model4.setEmployee(jsonObject.getString("Employee"));
                    model4.setCustomer(jsonObject.getString("Customer"));
                    model4.setQuoteType(jsonObject.getString("Quote Type"));
                    model4.setQuoteStatus(jsonObject.getString("QuoteStatus"));
                    model4.setTotal_Bef_DIscount(jsonObject.getString("Total_Bef_DIscount"));
                    model4.setTaxAmt(jsonObject.getString("TaxAmt"));
                    model4.setTotal(jsonObject.getString("Total"));

                    viewQuotationModelArrayList.add(model4);
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
                Toast.makeText(ViewQuotation.this, "NoDataFound", Toast.LENGTH_SHORT).show();
                viewquotationRecycle.setVisibility(View.GONE);
            } else {

                viewquotationRecycle.setVisibility(View.VISIBLE);
                viewquotationRecycle.setLayoutManager(new LinearLayoutManager(ViewQuotation.this));
                Adapter = new ViewQuotationAdapter(getApplicationContext(), viewQuotationModelArrayList);
                viewquotationRecycle.setAdapter(Adapter);
                viewquotationRecycle.setHasFixedSize(true);
            }
        }
    }

    public class ViewQuotationAdapter extends RecyclerView.Adapter {

        Context getApplicationContext;
        ArrayList<ViewQuotationModel> viewQuotationModelArrayList;

        public ViewQuotationAdapter(Context getApplicationContext, ArrayList<ViewQuotationModel> viewQuotationModelArrayList) {
            this.getApplicationContext = getApplicationContext;
            this.viewQuotationModelArrayList = viewQuotationModelArrayList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.quotationlist, parent, false);
            return new ViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position)
        {
            if(holder instanceof ViewHolder)
            {
                ((ViewHolder) holder ).DocNo_tv.setText(viewQuotationModelArrayList.get(position).getDocNo());
                ((ViewHolder) holder).QuoteDate_tv.setText(viewQuotationModelArrayList.get(position).getQuoteDate());
                ((ViewHolder) holder).Employee_tv.setText(viewQuotationModelArrayList.get(position).getEmployee());
                ((ViewHolder) holder).Customer_tv.setText(viewQuotationModelArrayList.get(position).getCustomer());
                ((ViewHolder) holder).QuoteType_tv.setText(viewQuotationModelArrayList.get(position).getQuoteType());
                ((ViewHolder) holder).QuoteStatus_tv.setText(viewQuotationModelArrayList.get(position).getQuoteStatus());
                ((ViewHolder) holder).Total_Bef_DIscount_tv.setText(viewQuotationModelArrayList.get(position).getTotal_Bef_DIscount());
                ((ViewHolder) holder).TaxAmt_tv.setText(viewQuotationModelArrayList.get(position).getTaxAmt());
                ((ViewHolder) holder).Total_tv.setText(viewQuotationModelArrayList.get(position).getTotal());


                ((ViewHolder) holder).editimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent (getApplicationContext,SalesQuotation.class);
                        intent.putExtra ("DocNo", viewQuotationModelArrayList.get (position).getDocNo ());
                        intent.putExtra ("editstatus", "1");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish ();
                    }
                });

                ((ViewHolder) holder).editapprove.setVisibility(View.GONE);

                ((ViewHolder) holder).editapprove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent (getApplicationContext,SalesQuotation.class);
                        intent.putExtra ("DocNo", viewQuotationModelArrayList.get (position).getDocNo ());
                        intent.putExtra ("editstatus", "2");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
            }
        }

        @Override
        public int getItemCount()
        {
            return viewQuotationModelArrayList.size();
        }
        public  class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView Sno_tv,DocNo_tv,QuoteDate_tv,Employee_tv,Customer_tv,QuoteType_tv,QuoteStatus_tv,Total_Bef_DIscount_tv,TaxAmt_tv,Total_tv;
            ImageView editimage,editapprove;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                DocNo_tv=(TextView)itemView.findViewById(R.id.DocNo_tv);
                QuoteDate_tv=(TextView)itemView.findViewById(R.id.QuoteDate_tv);
                Employee_tv=(TextView) itemView.findViewById(R.id.Employee_tv);
                Customer_tv=(TextView) itemView.findViewById(R.id.Customer_tv);
                QuoteType_tv=(TextView) itemView.findViewById(R.id.QuoteType_tv);
                QuoteStatus_tv=(TextView) itemView.findViewById(R.id.QuoteStatus_tv);
                Total_Bef_DIscount_tv=(TextView) itemView.findViewById(R.id.Total_Bef_DIscount_tv);
                TaxAmt_tv=(TextView) itemView.findViewById(R.id.TaxAmt_tv);
                Total_tv=(TextView) itemView.findViewById(R.id.Total_tv);
                editimage= itemView.findViewById(R.id.editimage);
                editapprove= itemView.findViewById(R.id.editapprove);
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
