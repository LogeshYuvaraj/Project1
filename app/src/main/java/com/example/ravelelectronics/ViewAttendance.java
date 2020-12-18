package com.example.ravelelectronics;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.ravelelectronics.model.ViewAttendanceModel;
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

public class ViewAttendance extends AppCompatActivity implements View.OnClickListener {
    EditText fromdate_et, todate_et;
    Button Btnsubmit;
    DatePickerDialog dp;
    DatePickerDialog dp1;
    RecyclerView viewattendanceRecycle;
    CardView viewattendance_cv, ViewActivity_cv;
    TextView EmpName_tv, AttnDate_tv, StartTime_tv, StopTime_tv, StartCustomerName_tv, StopCustomerName_tv;
    ArrayList<ViewAttendanceModel> viewAttendanceModelArrayList;
    Button editbtn, cancelbtn;
    RecyclerView.LayoutManager LinearLayoutManager;
    JSONArray jsonArray1;
    ProgressDialog pDialog;
    RecyclerView.Adapter Adapter;
    String sessionempname = "", sessionempid = "";
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);
        Btnsubmit = findViewById(R.id.Btnsubmit);
        fromdate_et = findViewById(R.id.fromdate_et);
        todate_et = findViewById(R.id.todate_et);
        viewattendanceRecycle = findViewById(R.id.viewattendanceRecycle);
        EmpName_tv = findViewById(R.id.EmpName_tv);
        AttnDate_tv = findViewById(R.id.AttnDate_tv);
        StartTime_tv = findViewById(R.id.StartTime_tv);
        StopTime_tv = findViewById(R.id.StopTime_tv);
        StartCustomerName_tv = findViewById(R.id.StartCustomerName_tv);
        StopCustomerName_tv = findViewById(R.id.StopCustomerName_tv);

        viewAttendanceModelArrayList = new ArrayList<>();

        Btnsubmit.setOnClickListener(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>View Attendance  </small>"));
        }

        session = new SessionManagement(ViewAttendance.this);
        HashMap<String, String> users = session.getUserDetails();
        sessionempname = users.get(SessionManagement.KEY_NAME);
        sessionempid = users.get(SessionManagement.KEY_CODE);

        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        fromdate_et.setText(date);
        String date1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        todate_et.setText(date);

        new GETAttendanceReport(). execute();

        fromdate_et.setFocusable(false);
        todate_et.setFocusable(false);

        fromdate_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                final int y = c.get(Calendar.YEAR);
                final int m = c.get(Calendar.MONTH);
                final int d = c.get(Calendar.DAY_OF_MONTH);

                dp = new DatePickerDialog(ViewAttendance.this,
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

                dp1 = new DatePickerDialog(ViewAttendance.this,
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

            new GETAttendanceReport().execute();
    }


    private class GETAttendanceReport extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(ViewAttendance.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_GETAttendanceReport";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_GETAttendanceReport";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_GETAttendanceReport";
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
                viewAttendanceModelArrayList.clear();
                jsonArray1 = new JSONArray(responseJSON);

                for (int l = 0; l < jsonArray1.length(); l++) {
                    JSONObject jsonObject = jsonArray1.getJSONObject(l);
                    ViewAttendanceModel model4 = new ViewAttendanceModel();


                    model4.setEmp_Name(jsonObject.getString("Emp Name"));
                    model4.setAttnDate(jsonObject.getString("AttnDate"));
                    model4.setStart_Time(jsonObject.getString("Start Time"));
                    model4.setStop_Time(jsonObject.getString("Stop Time"));
                    model4.setStart_Customer_Name(jsonObject.getString("Start Customer Name"));
                    model4.setStop_Customer_Name(jsonObject.getString("Stop Customer Name"));

                    viewAttendanceModelArrayList.add(model4);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (jsonArray1.length() == 0) {

                    Toast.makeText(ViewAttendance.this, "NoDataFound", Toast.LENGTH_SHORT).show();
                viewattendanceRecycle.setVisibility(View.GONE);
                } else {
                viewattendanceRecycle.setVisibility(View.VISIBLE);
                viewattendanceRecycle.setLayoutManager(new LinearLayoutManager(ViewAttendance.this));
                Adapter = new ViewAttendanceAdapter(getApplicationContext(), viewAttendanceModelArrayList);
                viewattendanceRecycle.setAdapter(Adapter);
                viewattendanceRecycle.setHasFixedSize(true);
            }
        }
    }

    public class ViewAttendanceAdapter extends RecyclerView.Adapter {

        Context getApplicationContext;
        ArrayList<ViewAttendanceModel> viewAttendanceModelArrayList;


        public ViewAttendanceAdapter(Context getApplicationContext, ArrayList<ViewAttendanceModel> viewAttendanceModelArrayList) {
            this.getApplicationContext = getApplicationContext;
            this.viewAttendanceModelArrayList = viewAttendanceModelArrayList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendancelist, parent, false);
            return new ViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position)
        {
            if(holder instanceof ViewHolder)
            {
                ((ViewHolder) holder ).EmpName_tv.setText(viewAttendanceModelArrayList.get(position).getEmp_Name());
                ((ViewHolder) holder).AttnDate_tv.setText(viewAttendanceModelArrayList.get(position).getAttnDate());
                ((ViewHolder) holder).StartTime_tv.setText(viewAttendanceModelArrayList.get(position).getStart_Time());
                ((ViewHolder) holder).StopTime_tv.setText(viewAttendanceModelArrayList.get(position).getStop_Time());
                ((ViewHolder) holder).StartCustomerName_tv.setText(viewAttendanceModelArrayList.get(position).getStart_Customer_Name());
                ((ViewHolder) holder).StopCustomerName_tv.setText(viewAttendanceModelArrayList.get(position).getStop_Customer_Name());
//
//                ((ViewHolder) holder).editbtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent=new Intent(getApplicationContext,ProjectoionUpdate.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        getApplicationContext.startActivity(intent);
//                    }
//                });

            }
        }
        @Override
        public int getItemCount()
        {
            return viewAttendanceModelArrayList.size();
        }
        public  class ViewHolder extends RecyclerView.ViewHolder
        {
            android.widget.LinearLayout LinearLayout;
            TextView EmpName_tv,AttnDate_tv,StartTime_tv,StopTime_tv,StartCustomerName_tv,StopCustomerName_tv,Sno_tv;
            Button editbtn;



            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                EmpName_tv=(TextView)itemView.findViewById(R.id.EmpName_tv);
                AttnDate_tv=(TextView)itemView.findViewById(R.id.AttnDate_tv);
                StartTime_tv=(TextView) itemView.findViewById(R.id.StartTime_tv);
                StopTime_tv=(TextView) itemView.findViewById(R.id.StopTime_tv);
                StartCustomerName_tv=(TextView) itemView.findViewById(R.id.StartCustomerName_tv);
                StopCustomerName_tv=(TextView) itemView.findViewById(R.id.StopCustomerName_tv);



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
