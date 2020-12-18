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

import com.example.ravelelectronics.ActivityScreen;
import com.example.ravelelectronics.R;
import com.example.ravelelectronics.SalesQuotation;
import com.example.ravelelectronics.model.CustomerModel;
import com.example.ravelelectronics.model.PlaceofsuppltModel;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import java.util.ArrayList;
import java.util.Locale;

public class PlaceofSupplyList_Spinner extends AppCompatActivity {

    EditText editTextSearch;
    TextView nodatafound_TV;
    RecyclerView placeofsupplylist_spin_recyclerview;

    ArrayList<PlaceofsuppltModel> placeofsuppltModelArrayList;
    JSONArray jsonArray;
    PlaceofSupplyListSpinnerAdapter placeofSupplyListSpinnerAdapter;
    ProgressDialog pDialog;

    String ScreenStatus = "",customercode = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.spinner_placeofsupply_list);

        Intent intent = getIntent ();
        ScreenStatus = intent.getStringExtra ("ScreenStatus");
        customercode = intent.getStringExtra ("customercode");
        Log.e ("ScreenStatus",ScreenStatus);
        Log.e ("customercode",customercode);

        placeofsuppltModelArrayList = new ArrayList<> ();

        nodatafound_TV = findViewById (R.id.nodatafound_TV);
        placeofsupplylist_spin_recyclerview = findViewById (R.id.placeofsupplylist_spin_recyclerview);

        new Getplaceofsupply ().execute ();

        editTextSearch = findViewById(R.id.editTextSearch);
        editTextSearch.setHint ("Search Here");

        editTextSearch.addTextChangedListener(new TextWatcher () {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (placeofSupplyListSpinnerAdapter != null) {
                    placeofSupplyListSpinnerAdapter.filter(String.valueOf(s));
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

    private class Getplaceofsupply extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PlaceofSupplyList_Spinner.this);
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
                final String METHOD_NAME = "IndusMobileSALES_GetPlaceOfSupply";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSALES_GetPlaceOfSupply";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSALES_GetPlaceOfSupply";
                Log.e("URL", URL);
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("CardCode", "" + customercode);
                Log.e("CardCode", "" + customercode);

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
                    PlaceofsuppltModel model = new PlaceofsuppltModel();

                    Log.e("State",jsonObject.getString("State"));
                    model.setState(jsonObject.getString("State"));
                    Log.e("CardCode",jsonObject.getString("CardCode"));
                    model.setCardCode(jsonObject.getString("CardCode"));

                    placeofsuppltModelArrayList.add(model);
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
                placeofsupplylist_spin_recyclerview.setVisibility(View.GONE);
                Toast.makeText (PlaceofSupplyList_Spinner.this, "No List", Toast.LENGTH_SHORT).show ( );
            } else {

                nodatafound_TV.setVisibility(View.GONE);
                placeofsupplylist_spin_recyclerview.setVisibility(View.VISIBLE);
                placeofsupplylist_spin_recyclerview.setLayoutManager(new LinearLayoutManager (PlaceofSupplyList_Spinner.this, LinearLayoutManager.VERTICAL, false));
                placeofsupplylist_spin_recyclerview.setItemAnimator(new DefaultItemAnimator ());
                placeofsupplylist_spin_recyclerview.setHasFixedSize(true);
                placeofSupplyListSpinnerAdapter = new PlaceofSupplyListSpinnerAdapter(PlaceofSupplyList_Spinner.this, placeofsuppltModelArrayList);
                placeofsupplylist_spin_recyclerview.setAdapter(placeofSupplyListSpinnerAdapter);
            }
        }
    }

    private class PlaceofSupplyListSpinnerAdapter extends RecyclerView.Adapter {

        Context context;
        ArrayList<PlaceofsuppltModel> data;

        ArrayList<PlaceofsuppltModel> searchdata;
        public String searchstring;

        public PlaceofSupplyListSpinnerAdapter(Context context, ArrayList<PlaceofsuppltModel> placeofsuppltModelArrayList) {
            this.context = context;
            this.data = placeofsuppltModelArrayList;

            this.searchdata = new ArrayList<> ();
            this.searchdata.addAll (placeofsuppltModelArrayList);
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(context);
            return new HomeMenuHolder(inflater.inflate(R.layout.placeofsupplylist_detail_layout, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {
            if (holder instanceof HomeMenuHolder) {
                ((HomeMenuHolder) holder).placeofsupply.setText (data.get (i).getState ());

                ((HomeMenuHolder) holder).placeofsupply.setOnClickListener (new View.OnClickListener ( ) {
                    @Override
                    public void onClick(View view) {
                        if (ScreenStatus.equalsIgnoreCase ("SQ")) {
                            Intent intent = new Intent (context, SalesQuotation.class);
                            intent.putExtra ("state", data.get (i).getState ());
                            intent.putExtra ("statecode", data.get (i).getCardCode ());
                            setResult(3,intent);
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

            TextView placeofsupply;

            public HomeMenuHolder(View view) {
                super(view);

                placeofsupply = view.findViewById(R.id.placeofsupply);
            }
        }
        public void filter(String charText) {

            this.searchstring = charText;
            charText = charText.toLowerCase(Locale.getDefault());
            data.clear();
            if (charText.length() == 0) {
                data.addAll(searchdata);
            } else {
                for (PlaceofsuppltModel wp : searchdata) {
                    {
                        //  if (wp.getItemName().toLowerCase(Locale.getDefault()).contains(charText)||wp.getDocNum().toLowerCase(Locale.getDefault()).contains(charText))
                        if (wp.getState ().toLowerCase(Locale.getDefault()).contains(charText))
                            data.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }
}
