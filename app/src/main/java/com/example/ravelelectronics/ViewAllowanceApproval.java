package com.example.ravelelectronics;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ViewAllowanceApproval extends AppCompatActivity implements View.OnClickListener {
    EditText fromdate_et, todate_et;
    LinearLayout fromdate_layout,todate_layout,submit_layout;
    TextView editimage;
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

        editimage = findViewById(R.id.editimage);
        editimage.setVisibility(View.GONE);

        Btnsubmit = findViewById(R.id.Btnsubmit);
        fromdate_et = findViewById(R.id.fromdate_et);
        todate_et = findViewById(R.id.todate_et);
        fromdate_layout = findViewById(R.id.fromdate_layout);
        todate_layout = findViewById(R.id.todate_layout);
        submit_layout = findViewById(R.id.submit_layout);
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

        fromdate_layout.setVisibility(View.GONE);
        todate_layout.setVisibility(View.GONE);
        submit_layout.setVisibility(View.GONE);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small> Allowance Approval </small>"));
        }

        session = new SessionManagement(ViewAllowanceApproval.this);
        HashMap<String, String> users = session.getUserDetails();
        sessionempname = users.get(SessionManagement.KEY_NAME);
        sessionempid = users.get(SessionManagement.KEY_CODE);

        if (ConnectivityReceiver.isConnected(ViewAllowanceApproval.this)) {
            new GETAllowanceReport().execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Your Internet Connection!!!", Toast.LENGTH_SHORT).show();
        }
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        fromdate_et.setText(date);
        String date1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        todate_et.setText(date);

        fromdate_et.setFocusable(false);
        todate_et.setFocusable(false);

        fromdate_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                final int y = c.get(Calendar.YEAR);
                final int m = c.get(Calendar.MONTH);
                final int d = c.get(Calendar.DAY_OF_MONTH);

                dp = new DatePickerDialog(ViewAllowanceApproval.this,
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

                dp1 = new DatePickerDialog(ViewAllowanceApproval.this,
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

            pDialog = new ProgressDialog(ViewAllowanceApproval.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_GETAllowanceApproval";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_GETAllowanceApproval";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_GETAllowanceApproval";
                Log.e("URL", URL);

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);


                Request.addProperty("User", "" + sessionempid);
                Log.e("User", "" + sessionempid);

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
                    model4.setAttachment1(jsonObject.getString("Attachment1"));
                    model4.setAttachment2(jsonObject.getString("Attachment2"));

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
                Toast.makeText(ViewAllowanceApproval.this, "NoDataFound", Toast.LENGTH_SHORT).show();
                viewallowanceRecycle.setVisibility(View.GONE);
            } else {

                viewallowanceRecycle.setVisibility(View.VISIBLE);
                viewallowanceRecycle.setLayoutManager(new LinearLayoutManager(ViewAllowanceApproval.this));
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
                } else if (viewAllowanceModelArrayList.get(position).getStatus().equalsIgnoreCase("R")) {
                    ((ViewHolder) holder).Status_tv.setText("Rejected");
                } else {
                    ((ViewHolder) holder).Status_tv.setText("Pending");
                }

                if (viewAllowanceModelArrayList.get(position).getAttachment1().equalsIgnoreCase("")) {
                    ((ViewHolder) holder).attachment1_tv.setText("");
                } else {
                    ((ViewHolder) holder).attachment1_tv.setText("View Image");
                }
                if (viewAllowanceModelArrayList.get(position).getAttachment2().equalsIgnoreCase("")) {
                    ((ViewHolder) holder).attachment2_tv.setText("");
                } else {
                    ((ViewHolder) holder).attachment2_tv.setText("View Image");
                }

                final String attachment1 = viewAllowanceModelArrayList.get(position).getAttachment1();
                final String attachment2 = viewAllowanceModelArrayList.get(position).getAttachment2();

                ((ViewHolder) holder).attachment1_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog dialog = new Dialog(ViewAllowanceApproval.this);
                        dialog.setContentView(R.layout.dialog_view_image);
                        dialog.setCancelable (true);
                        dialog.setCanceledOnTouchOutside (true);
                        dialog.setTitle("");

                        ImageView imageView = (ImageView) dialog.findViewById (R.id.imageView);

                        Picasso.with(ViewAllowanceApproval.this).load(attachment1).placeholder(R.drawable.imgeplaceholder).into(imageView);
//                                .load("https://images.unsplash.com/photo-1494548162494-384bba4ab999?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80")
                        dialog.show();
                    }
                });
                ((ViewHolder) holder).attachment2_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog dialog = new Dialog(ViewAllowanceApproval.this);
                        dialog.setContentView(R.layout.dialog_view_image);
                        dialog.setCancelable (true);
                        dialog.setCanceledOnTouchOutside (true);
                        dialog.setTitle("");

                        ImageView imageView = (ImageView) dialog.findViewById (R.id.imageView);

                        Picasso.with(ViewAllowanceApproval.this).load(attachment2).placeholder(R.drawable.imgeplaceholder).into(imageView);
//                                .load("https://images.unsplash.com/photo-1494548162494-384bba4ab999?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80")
                        dialog.show();
                    }
                });

                ((ViewHolder) holder).editimage.setVisibility(View.GONE);

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
                attachment1_tv=(TextView) itemView.findViewById(R.id.attachment1_tv);
                attachment2_tv=(TextView) itemView.findViewById(R.id.attachment2_tv);
                editimage= itemView.findViewById(R.id.editimage);
                editapprove= itemView.findViewById(R.id.editapprove);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(ViewAllowanceApproval.this, DashboardActivity.class));
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
