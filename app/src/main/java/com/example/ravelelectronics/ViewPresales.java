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

import com.example.ravelelectronics.model.ViewPresalesModel;
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

public class ViewPresales extends AppCompatActivity implements View.OnClickListener {
    EditText fromdate_et, todate_et;
    Button Btnsubmit;
    DatePickerDialog dp;
    DatePickerDialog dp1;
    RecyclerView presalesviewRecycle;
    TextView Sno_tv,DocNo_tv,DocDate_tv,Type_tv,OppType_tv,Category_tv,TargetStartDate_tv,TargetEndDate_tv,TargetStatusDate_tv,TargetClDate_tv,Reported_tv,Itemcode_tv,ItemName_tv,Qty_tv,Price_tv;
    ArrayList<ViewPresalesModel> viewPresalesModelArrayList;
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
        setContentView(R.layout.activity_view_presales);

        Btnsubmit = findViewById(R.id.Btnsubmit);
        fromdate_et = findViewById(R.id.fromdate_et);
        todate_et = findViewById(R.id.todate_et);
        presalesviewRecycle=findViewById(R.id.presalesviewRecycle);
        DocNo_tv=findViewById(R.id.DocNo_tv);
        DocDate_tv=findViewById(R.id.DocDate_tv);
        Type_tv=findViewById(R.id.Type_tv);
        OppType_tv=findViewById(R.id.OppType_tv);
        Category_tv=findViewById(R.id.Category_tv);
        TargetStartDate_tv=findViewById(R.id.TargetStartDate_tv);
        TargetEndDate_tv=findViewById(R.id.TargetEndDate_tv);
        TargetStatusDate_tv=findViewById(R.id.TargetStatusDate_tv);
        TargetClDate_tv=findViewById(R.id.TargetClDate_tv);
        Reported_tv=findViewById(R.id.Reported_tv);
        Itemcode_tv=findViewById(R.id.Itemcode_tv);
        ItemName_tv=findViewById(R.id.ItemName_tv);
        Qty_tv=findViewById(R.id.Qty_tv);
        Price_tv=findViewById(R.id.Price_tv);

        Btnsubmit.setOnClickListener(this);
        viewPresalesModelArrayList = new ArrayList<>();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>View Presales  </small>"));
        }

        session = new SessionManagement(ViewPresales.this);
        HashMap<String, String> users = session.getUserDetails();
        sessionempname = users.get(SessionManagement.KEY_NAME);
        sessionempid = users.get(SessionManagement.KEY_CODE);

        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        fromdate_et.setText(date);
        String date1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        todate_et.setText(date);

        if (ConnectivityReceiver.isConnected(ViewPresales.this)) {
            new GETPreSalesReport().execute();
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

                dp = new DatePickerDialog(ViewPresales.this,
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

                dp1 = new DatePickerDialog(ViewPresales.this,
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
        new GETPreSalesReport().execute();
    }

    private class GETPreSalesReport extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(ViewPresales.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_PresalesReport";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_PresalesReport";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_PresalesReport";
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
                viewPresalesModelArrayList.clear();
                jsonArray2 = new JSONArray(responseJSON);

                for (int l = 0; l < jsonArray2.length(); l++) {
                    JSONObject jsonObject = jsonArray2.getJSONObject(l);
                    ViewPresalesModel model4 = new ViewPresalesModel();

                    model4.setSno (String.valueOf (l+1));
                    model4.setDocNo(jsonObject.getString("DocNo"));
                    model4.setDocDate(jsonObject.getString("DocDate"));
                    model4.setType(jsonObject.getString("type"));
                    model4.setOppType(jsonObject.getString("OppType"));
                    model4.setCategory(jsonObject.getString("category"));
                    model4.setTargetStartDate(jsonObject.getString("TargetStartDate"));
                    model4.setTargetEndDate(jsonObject.getString("TargetEndDate"));
                    model4.setTargetStatusDate(jsonObject.getString("TargetStatusDate"));
                    model4.setTargetClDate(jsonObject.getString("TargetClDate"));
                    model4.setPotentialAmt(jsonObject.getString("PotentialAmt"));
                    model4.setWeightedAmt(jsonObject.getString("WeightedAmt"));
                    model4.setGrossProfit(jsonObject.getString("GrossProfit"));
                    model4.setReported(jsonObject.getString("Reported"));
                    model4.setRespDate(jsonObject.getString("RespDate"));
                    model4.setRespEmpName(jsonObject.getString("RespEmpName"));
                    model4.setVerifyDate(jsonObject.getString("VerifyDate"));
                    model4.setVerifyEmpName(jsonObject.getString("VerifyEmpName"));
                    model4.setItemCode(jsonObject.getString("ItemCode"));
                    model4.setItemName(jsonObject.getString("ItemName"));
                    model4.setQty(jsonObject.getString("Qty"));
                    model4.setPrice(jsonObject.getString("Price"));

                    viewPresalesModelArrayList.add(model4);
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
                Toast.makeText(ViewPresales.this, "NoDataFound", Toast.LENGTH_SHORT).show();
                presalesviewRecycle.setVisibility(View.GONE);
            } else {
                presalesviewRecycle.setVisibility(View.VISIBLE);
                presalesviewRecycle.setLayoutManager(new LinearLayoutManager(ViewPresales.this));
                Adapter = new ViewPresalesAdapter(getApplicationContext(), viewPresalesModelArrayList);
                presalesviewRecycle.setAdapter(Adapter);
                presalesviewRecycle.setHasFixedSize(true);
            }
        }
    }

    public class ViewPresalesAdapter extends RecyclerView.Adapter {

        Context getApplicationContext;
        ArrayList<ViewPresalesModel> viewPresalesModelArrayList;

        public ViewPresalesAdapter(Context getApplicationContext, ArrayList<ViewPresalesModel> viewPresalesModelArrayList) {
            this.getApplicationContext = getApplicationContext;
            this.viewPresalesModelArrayList = viewPresalesModelArrayList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.presaleslist, parent, false);
            return new ViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position)
        {
            if(holder instanceof ViewHolder)
            {
                ((ViewHolder) holder).DocNo_tv.setText(viewPresalesModelArrayList.get(position).getDocNo());
                ((ViewHolder) holder).DocDate_tv.setText(viewPresalesModelArrayList.get(position).getDocDate());
                ((ViewHolder) holder).Type_tv.setText(viewPresalesModelArrayList.get(position).getType());
                ((ViewHolder) holder).OppType_tv.setText(viewPresalesModelArrayList.get(position).getOppType());
                ((ViewHolder) holder).Category_tv.setText(viewPresalesModelArrayList.get(position).getCategory());
                ((ViewHolder) holder).TargetStartDate_tv.setText(viewPresalesModelArrayList.get(position).getTargetStartDate());
                ((ViewHolder) holder).TargetEndDate_tv.setText(viewPresalesModelArrayList.get(position).getTargetEndDate());
                ((ViewHolder) holder).TargetStatusDate_tv.setText(viewPresalesModelArrayList.get(position).getTargetStatusDate());
                ((ViewHolder) holder).TargetClDate_tv.setText(viewPresalesModelArrayList.get(position).getTargetClDate());
                ((ViewHolder) holder).Reported_tv.setText(viewPresalesModelArrayList.get(position).getReported());
                ((ViewHolder) holder).Itemcode_tv.setText(viewPresalesModelArrayList.get(position).getItemCode());
                ((ViewHolder) holder).ItemName_tv.setText(viewPresalesModelArrayList.get(position).getItemName());
                ((ViewHolder) holder).Qty_tv.setText(viewPresalesModelArrayList.get(position).getQty());
                ((ViewHolder) holder).Price_tv.setText(viewPresalesModelArrayList.get(position).getPrice());
                ((ViewHolder) holder).PotentialAmt_tv.setText(viewPresalesModelArrayList.get(position).getPotentialAmt());
                ((ViewHolder) holder).WeightedAmt_tv.setText(viewPresalesModelArrayList.get(position).getWeightedAmt());
                ((ViewHolder) holder).GrossProfit_tv.setText(viewPresalesModelArrayList.get(position).getGrossProfit());
                ((ViewHolder) holder).RespDate_tv.setText(viewPresalesModelArrayList.get(position).getRespDate());
                ((ViewHolder) holder).RespEmpName_tv.setText(viewPresalesModelArrayList.get(position).getRespEmpName());
                ((ViewHolder) holder).VerifyDate_tv.setText(viewPresalesModelArrayList.get(position).getVerifyDate());


                ((ViewHolder) holder).editimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getApplicationContext,PresalesActivity.class);
                        intent.putExtra("DocNo",viewPresalesModelArrayList.get(position).getDocNo());
                        intent.putExtra("editstatus","1");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       startActivity(intent);
                    }
                });



            }
        }
        @Override
        public int getItemCount()
        {
            return viewPresalesModelArrayList.size();
        }
        public  class ViewHolder extends RecyclerView.ViewHolder
        {

            TextView Sno_tv,DocNo_tv,DocDate_tv,Type_tv,OppType_tv,Category_tv,TargetStartDate_tv,TargetEndDate_tv,TargetStatusDate_tv,TargetClDate_tv,Reported_tv,Itemcode_tv,ItemName_tv,Qty_tv,Price_tv,
                    PotentialAmt_tv,WeightedAmt_tv,GrossProfit_tv,RespDate_tv,RespEmpName_tv,VerifyDate_tv;
            ImageView editimage;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                editimage=itemView.findViewById(R.id.editimage);
                DocNo_tv=itemView.findViewById(R.id.DocNo_tv);
                DocDate_tv=itemView.findViewById(R.id.DocDate_tv);
                Type_tv=itemView.findViewById(R.id.Type_tv);
                OppType_tv=itemView.findViewById(R.id.OppType_tv);
                Category_tv=itemView.findViewById(R.id.Category_tv);
                TargetStartDate_tv=itemView.findViewById(R.id.TargetStartDate_tv);
                TargetEndDate_tv=itemView.findViewById(R.id.TargetEndDate_tv);
                TargetStatusDate_tv=itemView.findViewById(R.id.TargetStatusDate_tv);
                TargetClDate_tv=itemView.findViewById(R.id.TargetClDate_tv);
                Reported_tv=itemView.findViewById(R.id.Reported_tv);
                Itemcode_tv=itemView.findViewById(R.id.Itemcode_tv);
                ItemName_tv=itemView.findViewById(R.id.ItemName_tv);
                Qty_tv=itemView.findViewById(R.id.Qty_tv);
                Price_tv=itemView.findViewById(R.id.Price_tv);
                PotentialAmt_tv=itemView.findViewById(R.id.PotentialAmt_tv);
                WeightedAmt_tv=itemView.findViewById(R.id.WeightedAmt_tv);
                GrossProfit_tv=itemView.findViewById(R.id.GrossProfit_tv);
                RespDate_tv=itemView.findViewById(R.id.RespDate_tv);
                RespEmpName_tv=itemView.findViewById(R.id.RespEmpName_tv);
                VerifyDate_tv=itemView.findViewById(R.id.VerifyDate_tv);
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
