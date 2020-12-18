package com.example.ravelelectronics.spinnerclasses;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ravelelectronics.R;
import com.example.ravelelectronics.SalesQuotation;
import com.example.ravelelectronics.model.SqTypeModel;
import com.example.ravelelectronics.model.WhseModel;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import java.util.ArrayList;
import java.util.Locale;

public class WarehouseList_Spinner extends AppCompatActivity {

    EditText editTextSearch;
    TextView nodatafound_TV;
    RecyclerView warehouselist_spin_recyclerview;

    ArrayList<WhseModel> whseModelArrayList;
    JSONArray jsonArray;
    WareHouseListSpinnerAdapter wareHouseListSpinnerAdapter;
    ProgressDialog pDialog;

    String ScreenStatus = "",position1 = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.spinner_warehouse_list);

        Intent intent = getIntent ();
        ScreenStatus = intent.getStringExtra ("ScreenStatus");
        position1 = intent.getStringExtra ("position1");
        Log.e ("ScreenStatus",ScreenStatus);
        Log.e ("position1",position1);

        whseModelArrayList = new ArrayList<> ();

        nodatafound_TV = findViewById (R.id.nodatafound_TV);
        warehouselist_spin_recyclerview = findViewById (R.id.warehouselist_spin_recyclerview);

        new GetWhse ().execute ();

        editTextSearch = findViewById(R.id.editTextSearch);
        editTextSearch.setHint ("Search Here");

        editTextSearch.addTextChangedListener(new TextWatcher () {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (wareHouseListSpinnerAdapter != null) {
                    wareHouseListSpinnerAdapter.filter(String.valueOf(s));
                    System.out.println("on text chnge text: " + s);
                } else {
                    System.out.println("Choose Name First");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class GetWhse extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(WarehouseList_Spinner.this);
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

                jsonArray = new JSONArray(responseJSON);

                for (int l = 0; l < jsonArray.length(); l++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(l);
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

            if(jsonArray.length() == 0){
                nodatafound_TV.setVisibility(View.VISIBLE);
                warehouselist_spin_recyclerview.setVisibility(View.GONE);
                Toast.makeText (WarehouseList_Spinner.this, "No List", Toast.LENGTH_SHORT).show ( );
            } else {

                nodatafound_TV.setVisibility(View.GONE);
                warehouselist_spin_recyclerview.setVisibility(View.VISIBLE);
                warehouselist_spin_recyclerview.setLayoutManager(new LinearLayoutManager (WarehouseList_Spinner.this, LinearLayoutManager.VERTICAL, false));
                warehouselist_spin_recyclerview.setItemAnimator(new DefaultItemAnimator ());
                warehouselist_spin_recyclerview.setHasFixedSize(true);
                wareHouseListSpinnerAdapter = new WareHouseListSpinnerAdapter(WarehouseList_Spinner.this, whseModelArrayList);
                warehouselist_spin_recyclerview.setAdapter(wareHouseListSpinnerAdapter);
            }
        }
    }

    private class WareHouseListSpinnerAdapter extends RecyclerView.Adapter {

        Context context;
        ArrayList<WhseModel> data;

        ArrayList<WhseModel> searchdata;
        public String searchstring;

        public WareHouseListSpinnerAdapter(Context context, ArrayList<WhseModel> whseModelArrayList) {
            this.context = context;
            this.data = whseModelArrayList;

            this.searchdata = new ArrayList<> ();
            this.searchdata.addAll (whseModelArrayList);
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(context);
            return new HomeMenuHolder(inflater.inflate(R.layout.warehouselist_detail_layout, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {
            if (holder instanceof HomeMenuHolder) {
                ((HomeMenuHolder) holder).warehouse.setText (data.get (i).getWhsCode ());

                ((HomeMenuHolder) holder).warehouse.setOnClickListener (new View.OnClickListener ( ) {
                    @Override
                    public void onClick(View view) {
                        if (ScreenStatus.equalsIgnoreCase ("SQ")) {
                            Intent intent = new Intent (context, SalesQuotation.class);
                            intent.putExtra ("WhsName", data.get (i).getWhsName ());
                            intent.putExtra ("WhsCode", data.get (i).getWhsCode ());
                            if (position1.equalsIgnoreCase ("")) {
                                intent.putExtra ("edititemadapterstatus", "");
                                intent.putExtra ("WHSposition1", position1);
                            } else {
                                intent.putExtra ("edititemadapterstatus", "edit");
                                intent.putExtra ("WHSposition1", position1);
                            }
                            setResult(7,intent);
                            finish ();
                        }
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        private class HomeMenuHolder extends RecyclerView.ViewHolder {

            TextView warehouse;

            public HomeMenuHolder(View view) {
                super(view);

                warehouse = view.findViewById(R.id.warehouse);
            }
        }
        public void filter(String charText) {

            this.searchstring = charText;
            charText = charText.toLowerCase(Locale.getDefault());
            data.clear();
            if (charText.length() == 0) {
                data.addAll(searchdata);
            } else {
                for (WhseModel wp : searchdata) {
                    {
                        //  if (wp.getItemName().toLowerCase(Locale.getDefault()).contains(charText)||wp.getDocNum().toLowerCase(Locale.getDefault()).contains(charText))
                        if (wp.getWhsCode ().toLowerCase(Locale.getDefault()).contains(charText))
                            data.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }
}
