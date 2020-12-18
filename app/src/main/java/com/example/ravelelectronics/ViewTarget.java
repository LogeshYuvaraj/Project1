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

import com.example.ravelelectronics.model.ViewTargetModel;
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

public class ViewTarget extends AppCompatActivity implements View.OnClickListener {

    TextView editimage;
    EditText fromdate_et, todate_et;
    Button Btnsubmit;
    DatePickerDialog dp;
    DatePickerDialog dp1;
    RecyclerView viewtargetRecycle;
    TextView Sno_tv,TargetNo_tv,CreateDate_tv,TargetStart_tv,TargetEnd_tv,TargetDuration_tv,TarPotenAmt_tv,Employee_tv;
    ArrayList<ViewTargetModel> viewTargetModelArrayList;
    Button editbtn,cancelbtn;
    RecyclerView.LayoutManager LinearLayoutManager;
    JSONArray jsonArray2;
    ProgressDialog pDialog;
    RecyclerView.Adapter Adapter;
    String sessionempname="",sessionempid="",sessionempmanager="",sessionempCRMAdmin="";
    SessionManagement session;
    String NoDataFound="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_target);

        editimage = findViewById(R.id.editimage);
        Btnsubmit = findViewById(R.id.Btnsubmit);
        fromdate_et = findViewById(R.id.fromdate_et);
        todate_et = findViewById(R.id.todate_et);
        TargetNo_tv=findViewById(R.id.TargetNo_tv);
        CreateDate_tv=findViewById(R.id.CreateDate_tv);
        TargetStart_tv=findViewById(R.id.TargetStart_tv);
        TargetEnd_tv=findViewById(R.id.TargetEnd_tv);
        TargetDuration_tv=findViewById(R.id.TargetDuration_tv);
        TarPotenAmt_tv=findViewById(R.id.TarPotenAmt_tv);
        Employee_tv=findViewById(R.id.Employee_tv);
        viewtargetRecycle=findViewById(R.id.viewtargetRecycle);

        Btnsubmit.setOnClickListener(this);
        viewTargetModelArrayList = new ArrayList<>();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small> View Salesman Target </small>"));
        }

        session = new SessionManagement(ViewTarget.this);
        HashMap<String, String> users = session.getUserDetails();
        sessionempname = users.get(SessionManagement.KEY_NAME);
        sessionempid = users.get(SessionManagement.KEY_CODE);
        sessionempmanager = users.get(SessionManagement.KEY_MANAGER);
        sessionempCRMAdmin = users.get(SessionManagement.KEY_CRMAdmin);

        if (sessionempCRMAdmin.equalsIgnoreCase("Y")) {
            editimage.setVisibility(View.VISIBLE);
        } else {
            editimage.setVisibility(View.GONE);
        }

        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        fromdate_et.setText(date);
        String date1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        todate_et.setText(date);

        if (ConnectivityReceiver.isConnected(ViewTarget.this)) {
            new GETTargetReport().execute();
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

                dp = new DatePickerDialog(ViewTarget.this,
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

                dp1 = new DatePickerDialog(ViewTarget.this,
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
        new GETTargetReport().execute();
    }


    private class GETTargetReport extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(ViewTarget.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_GETTargetReport";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_GETTargetReport";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_GETTargetReport";
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
                viewTargetModelArrayList.clear();
                jsonArray2 = new JSONArray(responseJSON);

                for (int l = 0; l < jsonArray2.length(); l++) {
                    JSONObject jsonObject = jsonArray2.getJSONObject(l);
                    ViewTargetModel model4 = new ViewTargetModel();

                    model4.setTarget_No(jsonObject.getString("Target No"));
                    model4.setCreate_Date(jsonObject.getString("Create Date"));
                    model4.setTarget_Start(jsonObject.getString("Target Start"));
                    model4.setTarget_End(jsonObject.getString("Target End"));
                    model4.setTarget_Duration(jsonObject.getString("Target Duration"));
                    model4.setTarPotenAmt(jsonObject.getString("TarPotenAmt"));
                    model4.setEmployee(jsonObject.getString("Employee"));

                    viewTargetModelArrayList.add(model4);
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
                Toast.makeText(ViewTarget.this, "NoDataFound", Toast.LENGTH_SHORT).show();
                viewtargetRecycle.setVisibility(View.GONE);
            } else {

                viewtargetRecycle.setVisibility(View.VISIBLE);
                viewtargetRecycle.setLayoutManager(new LinearLayoutManager(ViewTarget.this));
                Adapter = new ViewTargetAdapter(getApplicationContext(), viewTargetModelArrayList);
                viewtargetRecycle.setAdapter(Adapter);
                viewtargetRecycle.setHasFixedSize(true);
            }
        }
    }

    public class ViewTargetAdapter extends RecyclerView.Adapter {

        Context getApplicationContext;
        ArrayList<ViewTargetModel> viewTargetModelArrayList;


        public ViewTargetAdapter(Context getApplicationContext, ArrayList<ViewTargetModel> viewTargetModelArrayList) {
            this.getApplicationContext = getApplicationContext;
            this.viewTargetModelArrayList = viewTargetModelArrayList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.targetlist, parent, false);
            return new ViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position)
        {
            if(holder instanceof ViewHolder)
            {

                ((ViewHolder) holder).TargetNo_tv.setText(viewTargetModelArrayList.get(position).getTarget_No());
                ((ViewHolder) holder).CreateDate_tv.setText(viewTargetModelArrayList.get(position).getCreate_Date());
                ((ViewHolder) holder).TargetStart_tv.setText(viewTargetModelArrayList.get(position).getTarget_Start());
                ((ViewHolder) holder).TargetEnd_tv.setText(viewTargetModelArrayList.get(position).getTarget_End());
                ((ViewHolder) holder).TargetDuration_tv.setText(viewTargetModelArrayList.get(position).getTarget_Duration());
                ((ViewHolder) holder).TarPotenAmt_tv.setText(viewTargetModelArrayList.get(position).getTarPotenAmt());
                ((ViewHolder) holder).Employee_tv.setText(viewTargetModelArrayList.get(position).getEmployee());

                if (sessionempCRMAdmin.equalsIgnoreCase("Y")) {
                    ((ViewHolder) holder).editimage.setVisibility(View.VISIBLE);
                } else {
                    ((ViewHolder) holder).editimage.setVisibility(View.GONE);
                }

                ((ViewHolder) holder).editimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getApplicationContext,SalesmanTarget.class);
                        intent.putExtra("DocNo",viewTargetModelArrayList.get(position).getTarget_No());
                        intent.putExtra("editstatus","1");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext.startActivity(intent);
                    }
                });
            }
        }
        @Override
        public int getItemCount()

        {
            return viewTargetModelArrayList.size();
        }
        public  class ViewHolder extends RecyclerView.ViewHolder
        {

            TextView Sno_tv,TargetNo_tv,CreateDate_tv,TargetStart_tv,TargetEnd_tv,TargetDuration_tv,TarPotenAmt_tv,Employee_tv;
            ImageView editimage;



            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                TargetNo_tv=(TextView)itemView.findViewById(R.id.TargetNo_tv);
                CreateDate_tv=(TextView)itemView.findViewById(R.id.CreateDate_tv);
                TargetStart_tv=(TextView) itemView.findViewById(R.id.TargetStart_tv);
                TargetEnd_tv=(TextView) itemView.findViewById(R.id.TargetEnd_tv);
                TargetDuration_tv=(TextView) itemView.findViewById(R.id.TargetDuration_tv);
                TarPotenAmt_tv=(TextView) itemView.findViewById(R.id.TarPotenAmt_tv);
                Employee_tv=(TextView) itemView.findViewById(R.id.Employee_tv);
                editimage=itemView.findViewById(R.id.editimage);


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
