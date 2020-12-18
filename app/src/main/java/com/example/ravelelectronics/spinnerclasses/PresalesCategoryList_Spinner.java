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

import com.example.ravelelectronics.PresalesActivity;
import com.example.ravelelectronics.R;
import com.example.ravelelectronics.model.CategoryModel;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import java.util.ArrayList;
import java.util.Locale;

public class PresalesCategoryList_Spinner extends AppCompatActivity {

    EditText editTextSearch;
    TextView nodatafound_TV;
    RecyclerView categorylist_spin_recyclerview;

    ArrayList<CategoryModel> categoryModelArrayList;
    JSONArray jsonArray;
    CategoryListSpinnerAdapter categoryListSpinnerAdapter;
    ProgressDialog pDialog;

    String ScreenStatus = "",customercode = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.spinner_category_list);

        Intent intent = getIntent ();
        ScreenStatus = intent.getStringExtra ("ScreenStatus");
        Log.e ("ScreenStatus",ScreenStatus);
        Log.e ("customercode",customercode);

        categoryModelArrayList = new ArrayList<> ();

        nodatafound_TV = findViewById (R.id.nodatafound_TV);
        categorylist_spin_recyclerview = findViewById (R.id.categorylist_spin_recyclerview);

        new GetCategory ().execute ();

        editTextSearch = findViewById(R.id.editTextSearch);
        editTextSearch.setHint ("Search Here");

        editTextSearch.addTextChangedListener(new TextWatcher () {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (categoryListSpinnerAdapter != null) {
                    categoryListSpinnerAdapter.filter(String.valueOf(s));
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

    private class GetCategory extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PresalesCategoryList_Spinner.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Login...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSALES_GetPresalesCategory";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSALES_GetPresalesCategory";
                final String URL =  "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSALES_GetPresalesCategory";
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
                    CategoryModel model = new CategoryModel ();
                    model.setPresalesCategory (jsonObject.getString ("PresalesCategory"));
                    categoryModelArrayList.add (model);
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
                categorylist_spin_recyclerview.setVisibility(View.GONE);
                Toast.makeText (PresalesCategoryList_Spinner.this, "No List", Toast.LENGTH_SHORT).show ( );
            } else {

                nodatafound_TV.setVisibility(View.GONE);
                categorylist_spin_recyclerview.setVisibility(View.VISIBLE);
                categorylist_spin_recyclerview.setLayoutManager(new LinearLayoutManager (PresalesCategoryList_Spinner.this, LinearLayoutManager.VERTICAL, false));
                categorylist_spin_recyclerview.setItemAnimator(new DefaultItemAnimator ());
                categorylist_spin_recyclerview.setHasFixedSize(true);
                categoryListSpinnerAdapter = new CategoryListSpinnerAdapter(PresalesCategoryList_Spinner.this, categoryModelArrayList);
                categorylist_spin_recyclerview.setAdapter(categoryListSpinnerAdapter);
            }
        }
    }

    private class CategoryListSpinnerAdapter extends RecyclerView.Adapter {

        Context context;
        ArrayList<CategoryModel> data;

        ArrayList<CategoryModel> searchdata;
        public String searchstring;

        public CategoryListSpinnerAdapter(Context context, ArrayList<CategoryModel> categoryModelArrayList) {
            this.context = context;
            this.data = categoryModelArrayList;

            this.searchdata = new ArrayList<> ();
            this.searchdata.addAll (categoryModelArrayList);
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(context);
            return new HomeMenuHolder(inflater.inflate(R.layout.categorylist_detail_layout, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {
            if (holder instanceof HomeMenuHolder) {
                ((HomeMenuHolder) holder).category.setText (data.get (i).getPresalesCategory ());

                ((HomeMenuHolder) holder).category.setOnClickListener (new View.OnClickListener ( ) {
                    @Override
                    public void onClick(View view) {
                        if (ScreenStatus.equalsIgnoreCase ("PA")) {
                            Intent intent = new Intent (context, PresalesActivity.class);
                            intent.putExtra ("PresalesCategory", data.get (i).getPresalesCategory ());
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

            TextView category;

            public HomeMenuHolder(View view) {
                super(view);

                category = view.findViewById(R.id.category);
            }
        }
        public void filter(String charText) {

            this.searchstring = charText;
            charText = charText.toLowerCase(Locale.getDefault());
            data.clear();
            if (charText.length() == 0) {
                data.addAll(searchdata);
            } else {
                for (CategoryModel wp : searchdata) {
                    {
                        //  if (wp.getItemName().toLowerCase(Locale.getDefault()).contains(charText)||wp.getDocNum().toLowerCase(Locale.getDefault()).contains(charText))
                        if (wp.getPresalesCategory ().toLowerCase(Locale.getDefault()).contains(charText))
                            data.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }
}
