package com.example.ravelelectronics;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
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

import com.example.ravelelectronics.model.ViewAllowanceModel;
import com.example.ravelelectronics.util.ConnectivityReceiver;
import com.example.ravelelectronics.util.SessionManagement;
import com.squareup.picasso.Picasso;

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

public class ViewAllowance extends AppCompatActivity implements View.OnClickListener {
    EditText fromdate_et, todate_et;
    TextView editapprove;
    Button Btnsubmit;
    DatePickerDialog dp;
    DatePickerDialog dp1;
    RecyclerView viewallowanceRecycle;
    TextView Sno_tv,AllowanceNo_tv,AllowanceDate_tv,employee_tv,AdvanceType_tv,AllowanceType_tv,Fare_tv,Currency_tv;
    ArrayList<ViewAllowanceModel> viewAllowanceModelArrayList;
    Button editbtn,cancelbtn;
    RecyclerView.LayoutManager LinearLayoutManager;
    JSONArray jsonArray2;
    ProgressDialog pDialog;
    RecyclerView.Adapter Adapter;
    String sessionempname="",sessionempid="";
    SessionManagement session;
    String NoDataFound="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_allowance);

        editapprove = findViewById(R.id.editapprove);
        editapprove.setVisibility(View.GONE);

        Btnsubmit = findViewById(R.id.Btnsubmit);
        fromdate_et = findViewById(R.id.fromdate_et);
        todate_et = findViewById(R.id.todate_et);
        viewallowanceRecycle=findViewById(R.id.viewallowanceRecycle);

        AllowanceNo_tv=findViewById(R.id.AllowanceNo_tv);
        AllowanceDate_tv=findViewById(R.id.AllowanceDate_tv);
        employee_tv=findViewById(R.id.employee_tv);
//        AdvanceType_tv=findViewById(R.id.AdvanceType_tv);
//        AllowanceType_tv=findViewById(R.id.AllowanceType_tv);
//        Fare_tv=findViewById(R.id.Fare_tv);
//        Currency_tv=findViewById(R.id.Currency_tv);
        Btnsubmit.setOnClickListener(this);
        viewAllowanceModelArrayList = new ArrayList<>();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>View Allowance  </small>"));
        }

        session = new SessionManagement(ViewAllowance.this);
        HashMap<String, String> users = session.getUserDetails();
        sessionempname = users.get(SessionManagement.KEY_NAME);
        sessionempid = users.get(SessionManagement.KEY_CODE);

        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        fromdate_et.setText(date);
        String date1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        todate_et.setText(date);

        if (ConnectivityReceiver.isConnected(ViewAllowance.this)) {
            new GETAllowanceReport().execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Your Internet Connection!!!", Toast.LENGTH_SHORT).show();
        }

        fromdate_et.setFocusable(false);
        todate_et.setFocusable(false);

        fromdate_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                final int y = c.get(Calendar.YEAR);
                final int m = c.get(Calendar.MONTH);
                final int d = c.get(Calendar.DAY_OF_MONTH);

                dp = new DatePickerDialog(ViewAllowance.this,
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

                dp1 = new DatePickerDialog(ViewAllowance.this,
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
    public void onClick(View view) {
        new GETAllowanceReport().execute();
    }

    private class GETAllowanceReport extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(ViewAllowance.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_GETAllowanceReport";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_GETAllowanceReport";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_GETAllowanceReport";
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
                viewAllowanceModelArrayList.clear();
                jsonArray2 = new JSONArray(responseJSON);

                for (int l = 0; l < jsonArray2.length(); l++) {
                    JSONObject jsonObject = jsonArray2.getJSONObject(l);
                    ViewAllowanceModel model4 = new ViewAllowanceModel();

                    model4.setAllowance_No(jsonObject.getString("Allowance No"));
                    model4.setAllowance_Date(jsonObject.getString("Allowance Date"));
                    model4.setEmployee(jsonObject.getString("Employee"));
                    model4.setTotal_Amount(jsonObject.getString("Total Amount"));
                    model4.setStatus(jsonObject.getString("Status"));

                    viewAllowanceModelArrayList.add(model4);
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
                Toast.makeText(ViewAllowance.this, "NoDataFound", Toast.LENGTH_SHORT).show();
                viewallowanceRecycle.setVisibility(View.GONE);
            } else {

                viewallowanceRecycle.setVisibility(View.VISIBLE);
                viewallowanceRecycle.setLayoutManager(new LinearLayoutManager(ViewAllowance.this));
                Adapter = new ViewAllowanceAdapter(getApplicationContext(), viewAllowanceModelArrayList);
                viewallowanceRecycle.setAdapter(Adapter);
                viewallowanceRecycle.setHasFixedSize(true);
            }
        }
    }

    public class ViewAllowanceAdapter extends RecyclerView.Adapter {

        Context getApplicationContext;
        ArrayList<ViewAllowanceModel> viewAllowanceModelArrayList;


        public ViewAllowanceAdapter(Context getApplicationContext, ArrayList<ViewAllowanceModel> viewAllowanceModelArrayList) {
            this.getApplicationContext = getApplicationContext;
            this.viewAllowanceModelArrayList = viewAllowanceModelArrayList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.allowancelist, parent, false);
            return new ViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position)
        {
            if(holder instanceof ViewHolder)
            {
                ((ViewHolder) holder ).AllowanceNo_tv.setText(viewAllowanceModelArrayList.get(position).getAllowance_No());
                ((ViewHolder) holder).AllowanceDate_tv.setText(viewAllowanceModelArrayList.get(position).getAllowance_Date());
                ((ViewHolder) holder).employee_tv.setText(viewAllowanceModelArrayList.get(position).getEmployee());
                ((ViewHolder) holder).Total_Amount_tv.setText(viewAllowanceModelArrayList.get(position).getTotal_Amount());

                if (viewAllowanceModelArrayList.get(position).getStatus().equalsIgnoreCase("A")) {
                    ((ViewHolder) holder).Status_tv.setText("Approved");
                    ((ViewHolder) holder).editimage.setEnabled(false);
                    Toast.makeText(getApplicationContext, "Approved Document cannot be Edited", Toast.LENGTH_SHORT).show();
                } else if (viewAllowanceModelArrayList.get(position).getStatus().equalsIgnoreCase("R")) {
                    ((ViewHolder) holder).Status_tv.setText("Rejected");
                } else {
                    ((ViewHolder) holder).Status_tv.setText("Pending");
                }

                ((ViewHolder) holder).editimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getApplicationContext,AllowanceActivity.class);
                        intent.putExtra ("DocNo", viewAllowanceModelArrayList.get (position).getAllowance_No ());
                        intent.putExtra ("editstatus", "1");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext.startActivity(intent);
                        finish ();
                    }
                });

                ((ViewHolder) holder).editapprove.setVisibility(View.GONE);

                ((ViewHolder) holder).editapprove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent (getApplicationContext,AllowanceActivity.class);
                        intent.putExtra ("DocNo", viewAllowanceModelArrayList.get (position).getAllowance_No ());
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
            return viewAllowanceModelArrayList.size();
        }
        public  class ViewHolder extends RecyclerView.ViewHolder
        {

            TextView Sno_tv,AllowanceNo_tv,AllowanceDate_tv,employee_tv,Total_Amount_tv,Status_tv, attachment1_tv, attachment2_tv;
            ImageView editimage,editapprove;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                AllowanceNo_tv=(TextView)itemView.findViewById(R.id.AllowanceNo_tv);
                AllowanceDate_tv=(TextView)itemView.findViewById(R.id.AllowanceDate_tv);
                employee_tv=(TextView) itemView.findViewById(R.id.employee_tv);
                Total_Amount_tv=(TextView) itemView.findViewById(R.id.Total_Amount_tv);
                Status_tv=(TextView) itemView.findViewById(R.id.Status_tv);
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
