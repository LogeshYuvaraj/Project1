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
import com.example.ravelelectronics.SalesmanTarget;
import com.example.ravelelectronics.model.PrjEmployeeModel;
import com.example.ravelelectronics.util.SessionManagement;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import java.util.ArrayList;
import java.util.Locale;

public class EmployeeList_Spinner extends AppCompatActivity {

    EditText editTextSearch;
    TextView nodatafound_TV;
    RecyclerView employeelist_spin_recyclerview;

    ArrayList<PrjEmployeeModel> prjEmployeeModelArrayList;
    JSONArray jsonArray;

    EmployeeListSpinnerAdapter employeeListSpinnerAdapter;

    ProgressDialog pDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.spinner_employee_list);

        prjEmployeeModelArrayList = new ArrayList<> ();

        nodatafound_TV = findViewById (R.id.nodatafound_TV);
        employeelist_spin_recyclerview = findViewById (R.id.employeelist_spin_recyclerview);

        new GetEmployee1 ().execute ();

        editTextSearch = findViewById(R.id.editTextSearch);
        editTextSearch.setHint ("Search Here");

        editTextSearch.addTextChangedListener(new TextWatcher () {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (employeeListSpinnerAdapter != null) {
                    employeeListSpinnerAdapter.filter(String.valueOf(s));
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

    private class GetEmployee1 extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog (EmployeeList_Spinner.this);
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
                final String METHOD_NAME = "IndusMobileSALES_GetPresalesEmployee";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSALES_GetPresalesEmployee";
                final String URL =  "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSALES_GetPresalesEmployee";
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

                jsonArray = new JSONArray (responseJSON);
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    PrjEmployeeModel model = new PrjEmployeeModel ();
                    model.setEmpName (jsonObject.getString ("EmpName"));
                    model.setEmpID (jsonObject.getString ("empID"));
                    model.setSlpname (jsonObject.getString ("slpname"));
                    prjEmployeeModelArrayList.add (model);

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
                employeelist_spin_recyclerview.setVisibility(View.GONE);
                Toast.makeText (EmployeeList_Spinner.this, "No List", Toast.LENGTH_SHORT).show ( );
            } else {
                nodatafound_TV.setVisibility(View.GONE);
                employeelist_spin_recyclerview.setVisibility(View.VISIBLE);
                employeelist_spin_recyclerview.setLayoutManager(new LinearLayoutManager (EmployeeList_Spinner.this, LinearLayoutManager.VERTICAL, false));
                employeelist_spin_recyclerview.setItemAnimator(new DefaultItemAnimator ());
                employeelist_spin_recyclerview.setHasFixedSize(true);
                employeeListSpinnerAdapter = new EmployeeListSpinnerAdapter(EmployeeList_Spinner.this, prjEmployeeModelArrayList);
                employeelist_spin_recyclerview.setAdapter(employeeListSpinnerAdapter);
            }
        }
    }

    private class EmployeeListSpinnerAdapter extends RecyclerView.Adapter {

        Context context;
        ArrayList<PrjEmployeeModel> data;
        SessionManagement sessionManagement;

        ArrayList<PrjEmployeeModel> searchdata;
        public String searchstring;

        public EmployeeListSpinnerAdapter(Context context, ArrayList<PrjEmployeeModel> data) {
            this.context = context;
            this.data = data;
            this.sessionManagement = sessionManagement;
            this.searchdata = searchdata;
            this.searchstring = searchstring;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(context);
            return new HomeMenuHolder(inflater.inflate(R.layout.employeelist_detail_layout, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {
            if (holder instanceof HomeMenuHolder) {
                ((HomeMenuHolder) holder).employee.setText (data.get (i).getEmpName ());

                ((HomeMenuHolder) holder).employee.setOnClickListener (new View.OnClickListener ( ) {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent (context, SalesmanTarget.class);
                        intent.putExtra ("EmpName", data.get (i).getEmpName ());
                        intent.putExtra ("empID", data.get (i).getEmpID ());
                        setResult(2,intent);
                        finish ();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        private class HomeMenuHolder extends RecyclerView.ViewHolder {

            TextView employee;

            public HomeMenuHolder(View view) {
                super(view);

                employee = view.findViewById(R.id.employee);
            }
        }

        public void filter(String charText) {

            this.searchstring = charText;
            charText = charText.toLowerCase(Locale.getDefault());
            data.clear();
            if (charText.length() == 0) {
                data.addAll(searchdata);
            } else {
                for (PrjEmployeeModel wp : searchdata) {
                    {
                        //  if (wp.getItemName().toLowerCase(Locale.getDefault()).contains(charText)||wp.getDocNum().toLowerCase(Locale.getDefault()).contains(charText))
                        if (wp.getEmpName ().toLowerCase(Locale.getDefault()).contains(charText))
                            data.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }
}
