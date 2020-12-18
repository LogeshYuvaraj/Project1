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

import com.example.ravelelectronics.PresalesActivity;
import com.example.ravelelectronics.Projectonscr;
import com.example.ravelelectronics.R;
import com.example.ravelelectronics.SalesQuotation;
import com.example.ravelelectronics.util.SessionManagement;
import com.example.ravelelectronics.model.ItemModel;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import java.util.ArrayList;
import java.util.Locale;

public class ItemList_Spinner extends AppCompatActivity {

    EditText editTextSearch;
    TextView nodatafound_TV;
    RecyclerView itemlist_spin_recyclerview;

    JSONArray jarraylist1;
    ArrayList<ItemModel> itemModelArrayList;
    ItemListSpinnerAdapter itemListSpinnerAdapter;
    ProgressDialog pDialog;

    String ScreenStatus = "",position1="";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.spinner_item_list);

        Intent intent = getIntent ();
        ScreenStatus = intent.getStringExtra ("ScreenStatus");
        position1 = intent.getStringExtra ("position1");
        Log.e ("ScreenStatus",ScreenStatus);
        Log.e ("position1",position1);

        itemModelArrayList = new ArrayList<> ();

        nodatafound_TV = findViewById (R.id.nodatafound_TV);
        itemlist_spin_recyclerview = findViewById (R.id.itemlist_spin_recyclerview);

        new Getitem ().execute ();

        editTextSearch = findViewById(R.id.editTextSearch);
        editTextSearch.setHint ("Search Here");

        editTextSearch.addTextChangedListener(new TextWatcher () {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (itemListSpinnerAdapter != null) {
                    itemListSpinnerAdapter.filter(String.valueOf(s));
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

    private class Getitem extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ItemList_Spinner.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSALES_GetQuoteItem";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSALES_GetQuoteItem";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSALES_GetQuoteItem";
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

                jarraylist1 = new JSONArray(responseJSON);

                for (int l = 0; l < jarraylist1.length(); l++) {
                    JSONObject jsonObject = jarraylist1.getJSONObject(l);
                    ItemModel model = new ItemModel();
                    model.setItemcode(jsonObject.getString("itemcode"));
                    model.setItemname(jsonObject.getString("itemname"));
                    model.setUom(jsonObject.getString("Uom"));
                    model.setSalUnitMsr(jsonObject.getString("SalUnitMsr"));
                    model.setLastquoteprice(jsonObject.getString("lastquoteprice"));
                    model.setHSN(jsonObject.getString("HSN"));
                    model.setPrice(jsonObject.getString("Price"));
                    itemModelArrayList.add(model);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (jarraylist1.length() == 0) {
                nodatafound_TV.setVisibility(View.VISIBLE);
                itemlist_spin_recyclerview.setVisibility(View.GONE);
                Toast.makeText (ItemList_Spinner.this, "No List", Toast.LENGTH_SHORT).show ( );
            } else {
                nodatafound_TV.setVisibility(View.GONE);
                itemlist_spin_recyclerview.setVisibility(View.VISIBLE);
                itemlist_spin_recyclerview.setLayoutManager(new LinearLayoutManager (ItemList_Spinner.this, LinearLayoutManager.VERTICAL, false));
                itemlist_spin_recyclerview.setItemAnimator(new DefaultItemAnimator ());
                itemlist_spin_recyclerview.setHasFixedSize(true);
                itemListSpinnerAdapter = new ItemListSpinnerAdapter(ItemList_Spinner.this, itemModelArrayList);
                itemlist_spin_recyclerview.setAdapter(itemListSpinnerAdapter);
            }
        }
    }

    private class ItemListSpinnerAdapter extends RecyclerView.Adapter {

        Context context;
        ArrayList<ItemModel> data;
        SessionManagement sessionManagement;

        ArrayList<ItemModel> searchdata;
        public String searchstring;

        public ItemListSpinnerAdapter(Context context, ArrayList<ItemModel> itemModelArrayList) {
            this.context = context;
            this.data = itemModelArrayList;

            sessionManagement = new SessionManagement (context);

            this.searchdata = new ArrayList<> ();
            this.searchdata.addAll (itemModelArrayList);
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(context);
            return new HomeMenuHolder(inflater.inflate(R.layout.itemlist_detail_layout, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {
            if (holder instanceof HomeMenuHolder) {
                ((HomeMenuHolder) holder).itemname.setText (data.get (i).getItemname ());

                ((HomeMenuHolder) holder).itemname.setOnClickListener (new View.OnClickListener ( ) {
                    @Override
                    public void onClick(View view) {

                        if (ScreenStatus.equalsIgnoreCase ("PS")) {
                            Intent intent = new Intent (context, Projectonscr.class);
                            intent.putExtra ("itemname", data.get (i).getItemname ());
                            intent.putExtra ("itemcode", data.get (i).getItemcode ());
                            intent.putExtra ("uom", data.get (i).getUom ());
                            intent.putExtra ("hsn", data.get (i).getHSN ());
                            intent.putExtra ("lastquoteprice", data.get (i).getLastquoteprice ());
                            intent.putExtra ("salunitmar", data.get (i).getSalUnitMsr ());
                            if (position1.equalsIgnoreCase ("")) {
                                intent.putExtra ("edititemadapterstatus", "");
                                intent.putExtra ("position1", position1);
                            } else {
                                intent.putExtra ("edititemadapterstatus", "edit");
                                intent.putExtra ("position1", position1);
                            }
                            setResult(1,intent);
                            finish ();
                        } else if (ScreenStatus.equalsIgnoreCase ("PA")) {
                            Intent intent = new Intent (context, PresalesActivity.class);
                            intent.putExtra ("itemname", data.get (i).getItemname ());
                            intent.putExtra ("itemcode", data.get (i).getItemcode ());
                            intent.putExtra ("uom", data.get (i).getUom ());
                            intent.putExtra ("hsn", data.get (i).getHSN ());
                            intent.putExtra ("lastquoteprice", data.get (i).getLastquoteprice ());
                            intent.putExtra ("salunitmar", data.get (i).getSalUnitMsr ());
                            if (position1.equalsIgnoreCase ("")) {
                                intent.putExtra ("edititemadapterstatus", "");
                                intent.putExtra ("position1", position1);
                            } else {
                                intent.putExtra ("edititemadapterstatus", "edit");
                                intent.putExtra ("position1", position1);
                            }
                            setResult(1,intent);
                            finish ();
                        } else if (ScreenStatus.equalsIgnoreCase ("SQ")) {
                            Intent intent = new Intent (context, SalesQuotation.class);
                            intent.putExtra ("itemname", data.get (i).getItemname ( ));
                            intent.putExtra ("itemcode", data.get (i).getItemcode ( ));
                            intent.putExtra ("uom", data.get (i).getUom ( ));
                            intent.putExtra ("hsn", data.get (i).getHSN ( ));
                            intent.putExtra ("lastquoteprice", data.get (i).getLastquoteprice ( ));
                            intent.putExtra ("salunitmar", data.get (i).getSalUnitMsr ( ));
                            intent.putExtra ("Price", data.get (i).getPrice ( ));
                            if (position1.equalsIgnoreCase ("")) {
                                intent.putExtra ("edititemadapterstatus", "");
                                intent.putExtra ("position1", position1);
                            } else {
                                intent.putExtra ("edititemadapterstatus", "edit");
                                intent.putExtra ("position1", position1);
                            }
                            setResult (2, intent);
                            finish ( );
                        } else {
                            Log.e ("Nothing Selected","EEEEE");
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

            TextView itemname;

            public HomeMenuHolder(View view) {
                super(view);

                itemname = view.findViewById(R.id.itemname);
            }
        }

        public void filter(String charText) {

            this.searchstring = charText;
            charText = charText.toLowerCase(Locale.getDefault());
            data.clear();
            if (charText.length() == 0) {
                data.addAll(searchdata);
            } else {
                for (ItemModel wp : searchdata) {
                    {
                        //  if (wp.getItemName().toLowerCase(Locale.getDefault()).contains(charText)||wp.getDocNum().toLowerCase(Locale.getDefault()).contains(charText))
                        if (wp.getItemname ().toLowerCase(Locale.getDefault()).contains(charText))
                            data.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }
}
