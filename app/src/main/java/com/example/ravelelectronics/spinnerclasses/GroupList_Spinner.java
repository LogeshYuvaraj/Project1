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

import com.example.ravelelectronics.MasterActivity;
import com.example.ravelelectronics.R;
import com.example.ravelelectronics.model.GroupModel;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import java.util.ArrayList;
import java.util.Locale;

public class GroupList_Spinner extends AppCompatActivity {

    EditText editTextSearch;
    TextView nodatafound_TV;
    RecyclerView grouplist_spin_recyclerview;

    ArrayList<GroupModel> groupModelArrayList;
    JSONArray jsonArray;

    GroupListSpinnerAdapter groupListSpinnerAdapter;

    ProgressDialog pDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.spinner_group_list);

        groupModelArrayList = new ArrayList<> ();

        nodatafound_TV = findViewById (R.id.nodatafound_TV);
        grouplist_spin_recyclerview = findViewById (R.id.grouplist_spin_recyclerview);

        new GetCustomer ().execute ();

        editTextSearch = findViewById(R.id.editTextSearch);
        editTextSearch.setHint ("Search Here");

        editTextSearch.addTextChangedListener(new TextWatcher () {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (groupListSpinnerAdapter != null) {
                    groupListSpinnerAdapter.filter(String.valueOf(s));
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

    private class GetCustomer extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(GroupList_Spinner.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSALES_GetCustomerGroup";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSALES_GetCustomerGroup";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSALES_GetCustomerGroup";
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

                groupModelArrayList.clear();
                jsonArray = new JSONArray(responseJSON);

                for (int l = 0; l < jsonArray.length(); l++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(l);
                    GroupModel model = new GroupModel();
                    model.setGroupCode(jsonObject.getString("GroupCode"));
                    model.setGroupName(jsonObject.getString("GroupName"));
                    groupModelArrayList.add(model);
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
                grouplist_spin_recyclerview.setVisibility(View.GONE);
                Toast.makeText (GroupList_Spinner.this, "No List", Toast.LENGTH_SHORT).show ( );
            } else {

                nodatafound_TV.setVisibility(View.GONE);
                grouplist_spin_recyclerview.setVisibility(View.VISIBLE);
                grouplist_spin_recyclerview.setLayoutManager(new LinearLayoutManager (GroupList_Spinner.this, LinearLayoutManager.VERTICAL, false));
                grouplist_spin_recyclerview.setItemAnimator(new DefaultItemAnimator ());
                grouplist_spin_recyclerview.setHasFixedSize(true);
                groupListSpinnerAdapter = new GroupListSpinnerAdapter(GroupList_Spinner.this, groupModelArrayList);
                grouplist_spin_recyclerview.setAdapter(groupListSpinnerAdapter);
            }
        }
    }

    private class GroupListSpinnerAdapter extends RecyclerView.Adapter {

        Context context;
        ArrayList<GroupModel> data;

        ArrayList<GroupModel> searchdata;
        public String searchstring;

        public GroupListSpinnerAdapter(Context context, ArrayList<GroupModel> data) {
            this.context = context;
            this.data = data;
            this.searchdata = searchdata;
            this.searchstring = searchstring;
        }

//        public CustomerListSpinnerAdapter(Context context, ArrayList<CustomerModel> customerModelArrayList) {
//            this.context = context;
//            this.data = customerModelArrayList;
//
//            this.searchdata = new ArrayList<> ();
//            this.searchdata.addAll (customerModelArrayList);
//        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(context);
            return new HomeMenuHolder(inflater.inflate(R.layout.grouplist_detail_layout, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {
            if (holder instanceof HomeMenuHolder) {
                ((HomeMenuHolder) holder).group.setText (data.get (i).getGroupName ());

                ((HomeMenuHolder) holder).group.setOnClickListener (new View.OnClickListener ( ) {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent (context, MasterActivity.class);
                        intent.putExtra ("GroupName", data.get (i).getGroupName ());
                        intent.putExtra ("GroupCode", data.get (i).getGroupCode ());
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

            TextView group;

            public HomeMenuHolder(View view) {
                super(view);

                group = view.findViewById(R.id.group);
            }
        }
        public void filter(String charText) {

            this.searchstring = charText;
            charText = charText.toLowerCase(Locale.getDefault());
            data.clear();
            if (charText.length() == 0) {
                data.addAll(searchdata);
            } else {
                for (GroupModel wp : searchdata) {
                    {
                        //  if (wp.getItemName().toLowerCase(Locale.getDefault()).contains(charText)||wp.getDocNum().toLowerCase(Locale.getDefault()).contains(charText))
                        if (wp.getGroupName ().toLowerCase(Locale.getDefault()).contains(charText))
                            data.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }
}
