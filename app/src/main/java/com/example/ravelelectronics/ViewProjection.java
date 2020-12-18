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

import com.example.ravelelectronics.model.ProjectionviewModel;
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

public class ViewProjection extends AppCompatActivity implements View.OnClickListener {
    EditText fromdate_et, todate_et;
    Button Btnsubmit;
    DatePickerDialog dp;
    DatePickerDialog dp1;
    RecyclerView projectionviewrecycle;
    TextView DocNo_tv,ItemName_tv,Qty_tv,Itemcode_tv,FromDate_tv,ToDate_tv,Employee_tv,Category_tv,CreateDate_tv,Sno_tv;
    ArrayList<ProjectionviewModel> projectionviewModelArrayList;
    Button editbtn,cancelbtn;
    RecyclerView.LayoutManager LinearLayoutManager;
    JSONArray jsonArray5;
    ProgressDialog pDialog;
    RecyclerView.Adapter Adapter;
    String sessionempname="",sessionempid="";
    SessionManagement session;
    String NoDataFound="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_projection);
        Btnsubmit = findViewById(R.id.Btnsubmit);
        fromdate_et = findViewById(R.id.fromdate_et);
        todate_et = findViewById(R.id.todate_et);
        projectionviewrecycle=findViewById(R.id.projectionviewRecycle);
        DocNo_tv=findViewById(R.id.DocNo_tv);
        CreateDate_tv=findViewById(R.id.CreateDate_tv);
        Employee_tv=findViewById(R.id.Employee_tv);
        Category_tv=findViewById(R.id.Category_tv);
        FromDate_tv=findViewById(R.id.FromDate_tv);
        ToDate_tv=findViewById(R.id.ToDate_tv);
        Itemcode_tv=findViewById(R.id.Itemcode_tv);
        ItemName_tv=findViewById(R.id.ItemName_tv);
        Qty_tv=findViewById(R.id.Qty_tv);
        Btnsubmit.setOnClickListener(this);
        projectionviewModelArrayList = new ArrayList<>();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>View Projection  </small>"));
        }

        session = new SessionManagement(ViewProjection.this);
        HashMap<String, String> users = session.getUserDetails();
        sessionempname = users.get(SessionManagement.KEY_NAME);
        sessionempid = users.get(SessionManagement.KEY_CODE);

        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        fromdate_et.setText(date);
        String date1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        todate_et.setText(date);

        if (ConnectivityReceiver.isConnected(ViewProjection.this)) {
            new GETProjectionReport().execute();
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

                dp = new DatePickerDialog(ViewProjection.this,
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

                dp1 = new DatePickerDialog(ViewProjection.this,
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
        new GETProjectionReport().execute();
    }

    private class GETProjectionReport extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewProjection.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_GETProjectionReport";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_GETProjectionReport";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_GETProjectionReport";
                Log.e("URL", URL);

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("User",""+sessionempid);
                Log.e("User", ""+sessionempid);
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
                projectionviewModelArrayList.clear();
                jsonArray5 = new JSONArray(responseJSON);

                for ( int l = 0; l < jsonArray5.length(); l++) {
                    JSONObject jsonObject = jsonArray5.getJSONObject(l);
                    ProjectionviewModel model4 = new ProjectionviewModel();

                    model4.setDocNo(jsonObject.getString ("DocNo"));
                    model4.setCreate_Date(jsonObject.getString ("Create Date"));
                    model4.setEmployee(jsonObject.getString ("Employee"));
                    model4.setFrom_Date(jsonObject.getString ("From Date"));
                    model4.setTo_Date(jsonObject.getString ("To Date"));
                    model4.setCategory(jsonObject.getString ("Category"));
                    model4.setItem_code(jsonObject.getString ("Item code"));
                    model4.setItem_Name(jsonObject.getString ("Item Name"));
                    model4.setProjection_Given_Qty(jsonObject.getString ("Projection Given Qty"));
                    model4.setBilled_Qty(jsonObject.getString ("Billed Qty"));
                    model4.setDifference_Qty(jsonObject.getString ("Difference Qty"));

                    projectionviewModelArrayList.add (model4);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if(jsonArray5.length()==0)
            {
                Toast.makeText(ViewProjection.this, "NoDataFound", Toast.LENGTH_SHORT).show();
                projectionviewrecycle.setVisibility(View.GONE);
            } else {
                projectionviewrecycle.setVisibility(View.VISIBLE);
                projectionviewrecycle.setLayoutManager(new LinearLayoutManager(ViewProjection.this));
                Adapter = new ViewProjectionAdapter(getApplicationContext(), projectionviewModelArrayList);
                projectionviewrecycle.setAdapter(Adapter);
                projectionviewrecycle.setHasFixedSize(true);
            }
        }
    }

    public class ViewProjectionAdapter extends RecyclerView.Adapter {

        Context getApplicationContext;
        ArrayList<ProjectionviewModel> projectionviewModelArrayList;

        public ViewProjectionAdapter(Context getApplicationContext, ArrayList<ProjectionviewModel> projectionviewModelArrayList) {
            this.getApplicationContext = getApplicationContext;
            this.projectionviewModelArrayList = projectionviewModelArrayList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.projectionlist, parent, false);
            return new ViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position)
        {
            if(holder instanceof ViewHolder)
            {

                ((ViewHolder) holder).DocNo_tv.setText(projectionviewModelArrayList.get(position).getDocNo());
                ((ViewHolder) holder).CreateDate_tv.setText(projectionviewModelArrayList.get(position).getCreate_Date());
                ((ViewHolder) holder).Employee_tv.setText(projectionviewModelArrayList.get(position).getEmployee());
                ((ViewHolder) holder).Category_tv.setText(projectionviewModelArrayList.get(position).getCategory());
                ((ViewHolder) holder).FromDate_tv.setText(projectionviewModelArrayList.get(position).getFrom_Date());
                ((ViewHolder) holder).ToDate_tv.setText(projectionviewModelArrayList.get(position).getTo_Date());
                ((ViewHolder) holder).ItemName_tv.setText(projectionviewModelArrayList.get(position).getItem_Name());
                ((ViewHolder) holder).Itemcode_tv.setText(projectionviewModelArrayList.get(position).getItem_code());
                ((ViewHolder) holder).Projection_Given_Qty_tv.setText(projectionviewModelArrayList.get(position).getProjection_Given_Qty());
                ((ViewHolder) holder).Billed_Qty_tv.setText(projectionviewModelArrayList.get(position).getBilled_Qty());
                ((ViewHolder) holder).Difference_Qty_tv.setText(projectionviewModelArrayList.get(position).getDifference_Qty());

                ((ViewHolder) holder).editimage.setOnClickListener (new View.OnClickListener ( ) {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent (getApplicationContext, Projectonscr.class);
                        intent.putExtra ("DocNo", projectionviewModelArrayList.get(position).getDocNo());
                        intent.putExtra ("editstatus", "1");
                        startActivity (intent);
                        finish ();
                    }
                });
            }
        }
        @Override
        public int getItemCount()
        {
            return projectionviewModelArrayList.size();
        }
        public  class ViewHolder extends RecyclerView.ViewHolder
        {

            TextView DocNo_tv,ItemName_tv,Itemcode_tv,FromDate_tv,ToDate_tv,Employee_tv,Category_tv,CreateDate_tv,
                    Projection_Given_Qty_tv,Billed_Qty_tv,Difference_Qty_tv;
            ImageView editimage;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                DocNo_tv=(TextView)itemView.findViewById(R.id.DocNo_tv);
                FromDate_tv=(TextView)itemView.findViewById(R.id.FromDate_tv);
                ToDate_tv=(TextView) itemView.findViewById(R.id.ToDate_tv);
                Employee_tv=(TextView) itemView.findViewById(R.id.Employee_tv);
                CreateDate_tv=(TextView) itemView.findViewById(R.id.CreateDate_tv);
                Category_tv= itemView.findViewById(R.id.Category_tv);
                ItemName_tv= itemView.findViewById(R.id.ItemName_tv);
                Itemcode_tv= itemView.findViewById(R.id.Itemcode_tv);
                Projection_Given_Qty_tv= itemView.findViewById(R.id.Projection_Given_Qty_tv);
                Billed_Qty_tv= itemView.findViewById(R.id.Billed_Qty_tv);
                Difference_Qty_tv= itemView.findViewById(R.id.Difference_Qty_tv);

                editimage= itemView.findViewById(R.id.editimage);
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
