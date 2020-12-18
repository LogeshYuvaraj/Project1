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

import com.example.ravelelectronics.model.ViewActivityModel;
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

public class ViewFollowUp extends AppCompatActivity implements View.OnClickListener {
    EditText fromdate_et, todate_et;

    DatePickerDialog dp;
    DatePickerDialog dp1;
    RecyclerView viewactivityRecycle;
    TextView editimage,ActivityNo_tv,ActivityDate_tv,CustomerName_tv,Activity_tv,ContactNo_tv,StartTime_tv,EndTime_tv,Employee_tv,Status_tv,Sno_tv;
    ArrayList<ViewActivityModel> viewActivityModelArrayList;
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
        setContentView(R.layout.activity_view_follow_up);

        fromdate_et = findViewById(R.id.fromdate_et);
        todate_et = findViewById(R.id.todate_et);
        viewactivityRecycle=findViewById(R.id.viewactivityRecycle);
        ActivityNo_tv=findViewById(R.id.ActivityNo_tv);
        ActivityDate_tv=findViewById(R.id.ActivityDate_tv);
        CustomerName_tv=findViewById(R.id.CustomerName_tv);
        Activity_tv=findViewById(R.id.Activity_tv);
        ContactNo_tv=findViewById(R.id.ContactNo_tv);
        StartTime_tv=findViewById(R.id.StartTime_tv);
        EndTime_tv=findViewById(R.id.EndTime_tv);
        Employee_tv=findViewById(R.id.Employee_tv);
        Status_tv=findViewById(R.id.Status_tv);
        editimage=findViewById(R.id.editimage);

        viewActivityModelArrayList = new ArrayList<>();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>FollowUp Report  </small>"));
        }

        session = new SessionManagement(ViewFollowUp.this);
        HashMap<String, String> users = session.getUserDetails();
        sessionempname = users.get(SessionManagement.KEY_NAME);
        sessionempid = users.get(SessionManagement.KEY_CODE);

        if (ConnectivityReceiver.isConnected(ViewFollowUp.this)) {
            new GETFollowup_Report().execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Your Internet Connection!!!", Toast.LENGTH_SHORT).show();
        }

//        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
//        fromdate_et.setText(date);
//        String date1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
//        todate_et.setText(date);
//
//        fromdate_et.setFocusable(false);
//        todate_et.setFocusable(false);
//
//        fromdate_et.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final Calendar c = Calendar.getInstance();
//
//                final int y = c.get(Calendar.YEAR);
//                final int m = c.get(Calendar.MONTH);
//                final int d = c.get(Calendar.DAY_OF_MONTH);
//
//                dp = new DatePickerDialog(ViewFollowUp.this,
//                        new DatePickerDialog.OnDateSetListener() {
//
//                            @Override
//                            public void onDateSet(DatePicker view, int year,
//                                                  int monthOfYear, int dayOfMonth) {
//
//                                Calendar dateSelected = Calendar.getInstance();
//                                dateSelected.set(year, monthOfYear, dayOfMonth);
//
//                                SimpleDateFormat formatterfrm = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
//
//
//                                fromdate_et.setText(formatterfrm.format(dateSelected.getTime()));
//                                Log.d("Fromdate", fromdate_et.getText().toString());
//                            }
//                        }, y, m, d);
//                dp.show();
//            }
//        });
//        todate_et.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final Calendar c = Calendar.getInstance();
//
//                final int y = c.get(Calendar.YEAR);
//                final int m = c.get(Calendar.MONTH);
//                final int d = c.get(Calendar.DAY_OF_MONTH);
//
//                dp1 = new DatePickerDialog(ViewFollowUp.this,
//                        new DatePickerDialog.OnDateSetListener() {
//
//                            @Override
//                            public void onDateSet(DatePicker view, int year,
//                                                  int monthOfYear, int dayOfMonth) {
//
//                                Calendar dateSelected = Calendar.getInstance();
//                                dateSelected.set(year, monthOfYear, dayOfMonth);
//
//                                SimpleDateFormat formatterfrm = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
//
//                                todate_et.setText(formatterfrm.format(dateSelected.getTime()));
//                                Log.d("Todate", todate_et.getText().toString());
//                            }
//                        }, y, m, d);
//                dp1.show();
//            }
//        });
    }

    @Override
    public void onClick(View view) {

        new GETFollowup_Report().execute();
    }


    private class GETFollowup_Report extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(ViewFollowUp.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_GETFollowup_Report";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_GETFollowup_Report";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_GETFollowup_Report";
                Log.e("URL", URL);

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);


                Request.addProperty("User", "" + sessionempid);
                Log.e("User", "" + sessionempid);
//                Request.addProperty("FromDt", ""+fromdate_et.getText().toString().trim());
//                Log.e("FromDt", "" +fromdate_et.getText().toString().trim());
//                Request.addProperty("ToDt", ""+todate_et.getText().toString().trim() );
//                Log.e("ToDt", ""+todate_et.getText().toString().trim() );
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
                viewActivityModelArrayList.clear();
                jsonArray2 = new JSONArray(responseJSON);

                for (int l = 0; l < jsonArray2.length(); l++) {
                    JSONObject jsonObject = jsonArray2.getJSONObject(l);
                    ViewActivityModel model4 = new ViewActivityModel();

                    model4.setActivity_No(jsonObject.getString("Activity No."));
                    model4.setActivity_Date(jsonObject.getString("Activity Date"));
                    model4.setCustomer_Name(jsonObject.getString("Customer Name"));
                    model4.setActivity(jsonObject.getString("Activity"));
                    model4.setContact_No(jsonObject.getString("Contact No"));
                    model4.setStart_Time(jsonObject.getString("Start Time"));
                    model4.setEndTime(jsonObject.getString("EndTime"));
                    model4.setEmployee(jsonObject.getString("Employee"));
                    model4.setStatus(jsonObject.getString("Status"));
                    model4.setDescription(jsonObject.getString("Description"));
                    model4.setFollowupDate(jsonObject.getString("Followup Date"));

                    viewActivityModelArrayList.add(model4);
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
                Toast.makeText(ViewFollowUp.this, "NoDataFound", Toast.LENGTH_SHORT).show();
                viewactivityRecycle.setVisibility(View.GONE);
            } else {
                viewactivityRecycle.setVisibility(View.VISIBLE);
                viewactivityRecycle.setLayoutManager(new LinearLayoutManager(ViewFollowUp.this));
                Adapter = new ViewActivityAdapter(getApplicationContext(), viewActivityModelArrayList);
                viewactivityRecycle.setAdapter(Adapter);
                viewactivityRecycle.setHasFixedSize(true);
            }
        }
    }

    public class ViewActivityAdapter extends RecyclerView.Adapter {

        Context getApplicationContext;
        ArrayList<ViewActivityModel> viewActivityModelArrayList;

        public ViewActivityAdapter(Context getApplicationContext, ArrayList<ViewActivityModel> viewActivityModelArrayList) {
            this.getApplicationContext = getApplicationContext;
            this.viewActivityModelArrayList = viewActivityModelArrayList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.followupviewdetail, parent, false);
            return new ViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position)
        {
            if(holder instanceof ViewHolder)
            {
                ((ViewHolder) holder).ActivityNo_tv.setText(viewActivityModelArrayList.get(position).getActivity_No());
                ((ViewHolder) holder).ActivityDate_tv.setText(viewActivityModelArrayList.get(position).getActivity_Date());
                ((ViewHolder) holder).CustomerName_tv.setText(viewActivityModelArrayList.get(position).getCustomer_Name());
                ((ViewHolder) holder).Activity_tv.setText(viewActivityModelArrayList.get(position).getActivity());
                ((ViewHolder) holder).ContactNo_tv.setText(viewActivityModelArrayList.get(position).getContact_No());
                ((ViewHolder) holder).StartTime_tv.setText(viewActivityModelArrayList.get(position).getStart_Time());
                ((ViewHolder) holder).EndTime_tv.setText(viewActivityModelArrayList.get(position).getEndTime());
                ((ViewHolder) holder).Employee_tv.setText(viewActivityModelArrayList.get(position).getEmployee());
                ((ViewHolder) holder).Status_tv.setText(viewActivityModelArrayList.get(position).getStatus());
                ((ViewHolder) holder).Description_tv.setText(viewActivityModelArrayList.get(position).getDescription());
                ((ViewHolder) holder).FollowupDate_tv.setText(viewActivityModelArrayList.get(position).getFollowupDate());


                ((ViewHolder) holder).editimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getApplicationContext,ActivityScreen.class);
                        intent.putExtra("DocNo",viewActivityModelArrayList.get(position).getActivity_No());
                        intent.putExtra("editstatus","2");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                ((ViewHolder) holder).new_editimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getApplicationContext,ActivityScreen.class);
                        intent.putExtra("DocNo",viewActivityModelArrayList.get(position).getActivity_No());
                        intent.putExtra("editstatus","3");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
            }
        }
        @Override
        public int getItemCount()
        {
            return viewActivityModelArrayList.size();
        }
        public  class ViewHolder extends RecyclerView.ViewHolder
        {
            android.widget.LinearLayout LinearLayout;
            TextView ActivityNo_tv,ActivityDate_tv,CustomerName_tv,Activity_tv,ContactNo_tv,StartTime_tv,
                    EndTime_tv,Employee_tv,Sno_tv,Status_tv,Sno_,Description_tv,FollowupDate_tv;
            Button editbtn;
            ImageView editimage,new_editimage;



            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                ActivityNo_tv=(TextView)itemView.findViewById(R.id.ActivityNo_tv);
                ActivityDate_tv=(TextView)itemView.findViewById(R.id.ActivityDate_tv);
                CustomerName_tv=(TextView) itemView.findViewById(R.id.CustomerName_tv);
                Activity_tv=(TextView) itemView.findViewById(R.id.Activity_tv);
                ContactNo_tv=(TextView) itemView.findViewById(R.id.ContactNo_tv);
                StartTime_tv=(TextView) itemView.findViewById(R.id.StartTime_tv);
                EndTime_tv=(TextView) itemView.findViewById(R.id.EndTime_tv);
                Employee_tv=(TextView) itemView.findViewById(R.id.Employee_tv);
                Status_tv=(TextView) itemView.findViewById(R.id.Status_tv);
                Description_tv=(TextView) itemView.findViewById(R.id.Description_tv);
                FollowupDate_tv=(TextView) itemView.findViewById(R.id.FollowupDate_tv);

                editimage= itemView.findViewById(R.id.editimage);
                new_editimage= itemView.findViewById(R.id.new_editimage);

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
