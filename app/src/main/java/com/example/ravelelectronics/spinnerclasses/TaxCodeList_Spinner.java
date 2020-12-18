package com.example.ravelelectronics.spinnerclasses;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ravelelectronics.R;
import com.example.ravelelectronics.SalesQuotation;
import com.example.ravelelectronics.model.PlaceofsuppltModel;
import com.example.ravelelectronics.model.TaxcodeModel;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import java.util.ArrayList;
import java.util.Locale;

public class TaxCodeList_Spinner extends AppCompatActivity {

    EditText editTextSearch;
    TextView nodatafound_TV;
    RecyclerView taxcodelist_spin_recyclerview;

    ArrayList<TaxcodeModel> taxcodeModelArrayList;
    JSONArray jsonArray;
    TaxCodeListSpinnerAdapter taxCodeListSpinnerAdapter;
    ProgressDialog pDialog;

    String ScreenStatus = "",customercode = "",position1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.spinner_taxcode_list);

        Intent intent = getIntent ();
        ScreenStatus = intent.getStringExtra ("ScreenStatus");
        position1 = intent.getStringExtra ("position1");
        Log.e ("ScreenStatus",ScreenStatus);
        Log.e ("position1",position1);

        taxcodeModelArrayList = new ArrayList<> ();

        nodatafound_TV = findViewById (R.id.nodatafound_TV);
        taxcodelist_spin_recyclerview = findViewById (R.id.taxcodelist_spin_recyclerview);

        new GetTaxCode ().execute ();

        editTextSearch = findViewById(R.id.editTextSearch);
        editTextSearch.setHint ("Search Here");

        editTextSearch.addTextChangedListener(new TextWatcher () {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (taxCodeListSpinnerAdapter != null) {
                    taxCodeListSpinnerAdapter.filter(String.valueOf(s));
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

    private class GetTaxCode extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TaxCodeList_Spinner.this);
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

                jsonArray = new JSONArray(responseJSON);
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    TaxcodeModel model = new TaxcodeModel();
                    Log.e("code",jsonObject.getString("code"));
                    Log.e("Rate",jsonObject.getString("Rate"));
                    model.setCode(jsonObject.getString("code"));
                    model.setRate(jsonObject.getString("Rate"));

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

            if(jsonArray.length() == 0){
                nodatafound_TV.setVisibility(View.VISIBLE);
                taxcodelist_spin_recyclerview.setVisibility(View.GONE);
                Toast.makeText (TaxCodeList_Spinner.this, "No List", Toast.LENGTH_SHORT).show ( );
            } else {

                nodatafound_TV.setVisibility(View.GONE);
                taxcodelist_spin_recyclerview.setVisibility(View.VISIBLE);
                taxcodelist_spin_recyclerview.setLayoutManager(new LinearLayoutManager (TaxCodeList_Spinner.this, LinearLayoutManager.VERTICAL, false));
                taxcodelist_spin_recyclerview.setItemAnimator(new DefaultItemAnimator ());
                taxcodelist_spin_recyclerview.setHasFixedSize(true);
                taxCodeListSpinnerAdapter = new TaxCodeListSpinnerAdapter(TaxCodeList_Spinner.this, taxcodeModelArrayList);
                taxcodelist_spin_recyclerview.setAdapter(taxCodeListSpinnerAdapter);
            }
        }
    }

    private class TaxCodeListSpinnerAdapter extends RecyclerView.Adapter {

        Context context;
        ArrayList<TaxcodeModel> data;

        ArrayList<TaxcodeModel> searchdata;
        public String searchstring;

        public TaxCodeListSpinnerAdapter(Context context, ArrayList<TaxcodeModel> taxcodeModelArrayList) {
            this.context = context;
            this.data = taxcodeModelArrayList;

            this.searchdata = new ArrayList<> ();
            this.searchdata.addAll (taxcodeModelArrayList);
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(context);
            return new HomeMenuHolder(inflater.inflate(R.layout.taxcodelist_detail_layout, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {
            if (holder instanceof HomeMenuHolder) {
                ((HomeMenuHolder) holder).taxcode.setText (data.get (i).getCode ());

                ((HomeMenuHolder) holder).taxcode.setOnClickListener (new View.OnClickListener ( ) {
                    @Override
                    public void onClick(View view) {
                        if (ScreenStatus.equalsIgnoreCase ("SQ")) {
                            Intent intent = new Intent (context, SalesQuotation.class);
                            intent.putExtra ("taxcode", data.get (i).getCode ());
                            intent.putExtra ("taxrate", data.get (i).getRate ());
                            if (position1.equalsIgnoreCase ("")) {
                                intent.putExtra ("edititemadapterstatus", "");
                                intent.putExtra ("Taxposition1", position1);
                            } else {
                                intent.putExtra ("edititemadapterstatus", "edit");
                                intent.putExtra ("Taxposition1", position1);
                            }
                            setResult(4,intent);
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

            TextView taxcode;

            public HomeMenuHolder(View view) {
                super(view);

                taxcode = view.findViewById(R.id.taxcode);
            }
        }
        public void filter(String charText) {

            this.searchstring = charText;
            charText = charText.toLowerCase(Locale.getDefault());
            data.clear();
            if (charText.length() == 0) {
                data.addAll(searchdata);
            } else {
                for (TaxcodeModel wp : searchdata) {
                    {
                        //  if (wp.getItemName().toLowerCase(Locale.getDefault()).contains(charText)||wp.getDocNum().toLowerCase(Locale.getDefault()).contains(charText))
                        if (wp.getCode ().toLowerCase(Locale.getDefault()).contains(charText))
                            data.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }
}
