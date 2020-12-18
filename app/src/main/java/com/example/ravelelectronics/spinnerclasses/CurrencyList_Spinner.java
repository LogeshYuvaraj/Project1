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

import com.example.ravelelectronics.AllowanceActivity;
import com.example.ravelelectronics.MasterActivity;
import com.example.ravelelectronics.R;
import com.example.ravelelectronics.model.CurrencyModel;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import java.util.ArrayList;
import java.util.Locale;

public class CurrencyList_Spinner extends AppCompatActivity {

    EditText editTextSearch;
    TextView nodatafound_TV;
    RecyclerView currencylist_spin_recyclerview;

    ArrayList<CurrencyModel> currencyModelArrayList;
    JSONArray jsonArray;

    CurrencyListSpinnerAdter currencyListSpinnerAdapter;

    ProgressDialog pDialog;
    String position1="",ScreenStatus="";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.spinner_currenxcy_list);

        Intent intent = getIntent ();
        ScreenStatus = intent.getStringExtra ("ScreenStatus");
        position1 = intent.getStringExtra ("position1");
        Log.e ("position1",""+position1);

        currencyModelArrayList = new ArrayList<> ();

        nodatafound_TV = findViewById (R.id.nodatafound_TV);
        currencylist_spin_recyclerview = findViewById (R.id.currencylist_spin_recyclerview);

        new GetCurrency ().execute ();

        editTextSearch = findViewById(R.id.editTextSearch);
        editTextSearch.setHint ("Search Here");

        editTextSearch.addTextChangedListener(new TextWatcher () {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (currencyListSpinnerAdapter != null) {
                    currencyListSpinnerAdapter.filter(String.valueOf(s));
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

    private class GetCurrency extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CurrencyList_Spinner.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSALES_GetCurrency";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSALES_GetCurrency";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSALES_GetCurrency";
                Log.e("URL", URL);

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                AndroidHttpTransport aht = new AndroidHttpTransport(URL);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                aht.call(SOAP_ACTION, soapEnvelope);
                SoapPrimitive resultString = (SoapPrimitive) soapEnvelope.getResponse();
                String responseJSON = resultString.toString();

                Log.e("RESPONSE", responseJSON);

                currencyModelArrayList.clear();
                jsonArray = new JSONArray(responseJSON);

                for (int l = 0; l < jsonArray.length(); l++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(l);
                    CurrencyModel model = new CurrencyModel();
                    model.setCurrcode(jsonObject.getString("Currcode"));
                    model.setCurrname(jsonObject.getString("Currname"));
                    currencyModelArrayList.add(model);

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
                currencylist_spin_recyclerview.setVisibility(View.GONE);
                Toast.makeText (CurrencyList_Spinner.this, "No List", Toast.LENGTH_SHORT).show ( );
            } else {

                nodatafound_TV.setVisibility(View.GONE);
                currencylist_spin_recyclerview.setVisibility(View.VISIBLE);
                currencylist_spin_recyclerview.setLayoutManager(new LinearLayoutManager (CurrencyList_Spinner.this, LinearLayoutManager.VERTICAL, false));
                currencylist_spin_recyclerview.setItemAnimator(new DefaultItemAnimator ());
                currencylist_spin_recyclerview.setHasFixedSize(true);
                currencyListSpinnerAdapter = new CurrencyListSpinnerAdter(CurrencyList_Spinner.this, currencyModelArrayList);
                currencylist_spin_recyclerview.setAdapter(currencyListSpinnerAdapter);
            }
        }
    }

    private class CurrencyListSpinnerAdter extends RecyclerView.Adapter {

        Context context;
        ArrayList<CurrencyModel> data;

        ArrayList<CurrencyModel> searchdata;
        public String searchstring;


        public CurrencyListSpinnerAdter(Context context, ArrayList<CurrencyModel> data) {
            this.context = context;
            this.data = data;
            this.searchdata = searchdata;
            this.searchstring = searchstring;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(context);
            return new HomeMenuHolder(inflater.inflate(R.layout.currencylist_detail_layout, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {
            if (holder instanceof HomeMenuHolder) {
                ((HomeMenuHolder) holder).currency.setText (data.get (i).getCurrname ());

                ((HomeMenuHolder) holder).currency.setOnClickListener (new View.OnClickListener ( ) {
                    @Override
                    public void onClick(View view) {
                        if (ScreenStatus.equalsIgnoreCase ("AA")) {
                            Intent intent = new Intent (context, AllowanceActivity.class);
                            intent.putExtra ("Currname", data.get (i).getCurrname ( ));
                            intent.putExtra ("Currcode", data.get (i).getCurrcode ( ));
                            intent.putExtra ("editadapterstatus", "edit");
                            intent.putExtra ("position1", position1);
                            setResult (2, intent);
                            finish ( );
                        } else {
                            Intent intent = new Intent (context, MasterActivity.class);
                            intent.putExtra ("Currname", data.get (i).getCurrname ( ));
                            intent.putExtra ("Currcode", data.get (i).getCurrcode ( ));
                            setResult (2, intent);
                            finish ( );
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

            TextView currency;

            public HomeMenuHolder(View view) {
                super(view);

                currency = view.findViewById(R.id.currency);
            }
        }
        public void filter(String charText) {

            this.searchstring = charText;
            charText = charText.toLowerCase(Locale.getDefault());
            data.clear();
            if (charText.length() == 0) {
                data.addAll(searchdata);
            } else {
                for (CurrencyModel wp : searchdata) {
                    {
                        //  if (wp.getItemName().toLowerCase(Locale.getDefault()).contains(charText)||wp.getDocNum().toLowerCase(Locale.getDefault()).contains(charText))
                        if (wp.getCurrname ().toLowerCase(Locale.getDefault()).contains(charText))
                            data.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }
}
