package com.example.ravelelectronics;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ravelelectronics.model.ItemModel;
import com.example.ravelelectronics.model.ProjectionCategoryModel;
import com.example.ravelelectronics.model.ProjectionHeaderDetailsModel;
import com.example.ravelelectronics.model.ProjectionHeaderModel;
import com.example.ravelelectronics.model.Projection_Model;
import com.example.ravelelectronics.spinnerclasses.ItemList_Spinner;
import com.example.ravelelectronics.spinnerclasses.ProjectionCategoryList_Spinner;
import com.example.ravelelectronics.util.SessionManagement;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Projectonscr extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    EditText itemname_et,empcategory_et;
    EditText frmdate_ET,todate_ET,Itemcode_et;
    EditText Quantity_et,price_et;
    TextView salesemp_tv,Quantity,Price,canceltv,edittv;
    Button add,save,cancel,clear;
    MaterialSpinner empcategory,itemspin;
    SessionManagement session;
    String Category="";
    String sessionempname="",sessionempid="";

    RecyclerView projectionRecycle;
    ProjectionAdapter Adapter;

    String Itemspin="";

    ArrayList<Projection_Model> projection_modelArrayList;

    String itemcode="",itemname="",SalUnitMsr="",lastquoteprice="",HSN="",Uom="";

    ProgressDialog pDialog;
    JSONArray jsonArray,jarraylist;
    String EmpName = "", empID = "";
    String strfault = "", launch = "";
    DatePickerDialog dp;
    DatePickerDialog dp1;

    List<String> ItemModel1 = new ArrayList<>();
    ArrayList<ItemModel> itemModelArrayList;
    ArrayList<ProjectionHeaderModel> projectionHeaderModelArrayList;
    ArrayList<ProjectionHeaderDetailsModel> projectionHeaderDetailsModelArrayList;

    ArrayList<ProjectionCategoryModel> projectionCategoryModelArrayList;
    List<String> ProjectionCategoryModel1 = new ArrayList<>();

    String DocNo="",editstatus="";

    String ProjHeaderDocno,ProjHeaderDocDate,ProjHeaderSalesEmp,ProjHeaderCategory,ProjHeaderFromDt,
            ProjHeaderToDt,ProjHeaderCreatedBy,ProjHeaderCreatedDate;

    String position1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projectonscr);

        Intent intent = getIntent ();
        DocNo = intent.getStringExtra ("DocNo");
        editstatus = intent.getStringExtra ("editstatus");
        Log.e ("Intent-DocNo", DocNo);
        Log.e ("Intent-editstatus", editstatus);

        itemname_et = findViewById(R.id.itemname_et);
        empcategory_et = findViewById(R.id.empcategory_et);
        itemname_et.setOnClickListener (this);
        empcategory_et.setOnClickListener (this);

        edittv=findViewById(R.id.edittv);
        if (editstatus.equalsIgnoreCase ("1")) {
            edittv.setVisibility (View.VISIBLE);
        } else {
            edittv.setVisibility (View.GONE);
        }

        frmdate_ET=(EditText)findViewById(R.id.frmdate);
        todate_ET=(EditText)findViewById(R.id.todate) ;
        Quantity_et=findViewById(R.id.Quantity_et) ;
        Itemcode_et=findViewById(R.id.Itemcode_et) ;
        price_et=findViewById(R.id.price_et) ;
        salesemp_tv=findViewById(R.id.salesemp_tv) ;
        canceltv=findViewById(R.id.canceltv) ;
        itemspin=findViewById(R.id.itemspin) ;
        save=(Button)findViewById(R.id.save);
        add=(Button)findViewById(R.id.add);
        clear=(Button)findViewById(R.id.clear);
        save.setOnClickListener(this);
        add.setOnClickListener(this);
        clear.setOnClickListener(this);
        canceltv.setOnClickListener(this);
        projectionRecycle =findViewById(R.id.projectionRecycle);

        cancel=(Button)findViewById(R.id.cancel);
        cancel.setOnClickListener (this);
        empcategory = findViewById(R.id.empcategory);
//        empcategory.setOnItemSelectedListener(this);
//        itemspin.setOnItemSelectedListener(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small> Projection </small>"));
        }

        if (editstatus.equalsIgnoreCase ("1")) {
            Log.e ("Coming for", "Edit" + editstatus);
            new GetProjectionHeader ().execute ();
            save.setText ("Update");
        } else {
            Log.e ("Coming from", "DashBoard" + editstatus);
        }

        projectionHeaderModelArrayList = new ArrayList<>();
        projectionHeaderDetailsModelArrayList = new ArrayList<>();
        projectionCategoryModelArrayList = new ArrayList<>();
        itemModelArrayList = new ArrayList<>();
        projection_modelArrayList = new ArrayList<>();

        session = new SessionManagement(Projectonscr.this);
        HashMap<String, String> users = session.getUserDetails();
        sessionempname = users.get(SessionManagement.KEY_NAME);
        sessionempid = users.get(SessionManagement.KEY_CODE);
        salesemp_tv.setText(sessionempname);

        empcategory.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Category=item.toString();
                Log.e("Category",Category);
            }
        });

        frmdate_ET.setFocusable(false);
        todate_ET.setFocusable(false);

        frmdate_ET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (projection_modelArrayList.size()==0) {
                final Calendar c = Calendar.getInstance();

                final int y = c.get(Calendar.YEAR);
                final int m = c.get(Calendar.MONTH);
                final int d = c.get(Calendar.DAY_OF_MONTH);

                dp = new DatePickerDialog(Projectonscr.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                Calendar dateSelected = Calendar.getInstance();
                                dateSelected.set(year, monthOfYear, dayOfMonth);

                                SimpleDateFormat formatterfrm = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);

                                frmdate_ET.setText(formatterfrm.format(dateSelected.getTime()));
                                Log.d("Fromdate", frmdate_ET.getText().toString());
                            }

                        }, y, m, d);
                dp.show();
            }
        }});

        todate_ET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (projection_modelArrayList.size()==0) {

                final Calendar c = Calendar.getInstance();

                final int y = c.get(Calendar.YEAR);
                final int m = c.get(Calendar.MONTH);
                final int d = c.get(Calendar.DAY_OF_MONTH);

                dp1 = new DatePickerDialog(Projectonscr.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                Calendar dateSelected = Calendar.getInstance();
                                dateSelected.set(year, monthOfYear, dayOfMonth);

                                SimpleDateFormat formatterfrm = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);

                                todate_ET.setText(formatterfrm.format(dateSelected.getTime()));
                                Log.d("Todate", todate_ET.getText().toString());
                            }

                        }, y, m, d);
                dp1.show();
            }
        }});
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && data != null) {
            try {
                itemname = data.getStringExtra ("itemname");
                itemcode = data.getStringExtra ("itemcode");
                Uom = data.getStringExtra ("uom");
                HSN = data.getStringExtra ("hsn");
                lastquoteprice = data.getStringExtra ("lastquoteprice");
                SalUnitMsr = data.getStringExtra ("salunitmar");

                String edititemadapterstatus = data.getStringExtra ("edititemadapterstatus");
                position1 = data.getStringExtra ("position1");
                if (edititemadapterstatus.equalsIgnoreCase ("edit")) {
                    Adapter.notifyItemChanged(Integer.parseInt (position1));
                } else {
                    itemname_et.setText (""+itemname);
                    Itemcode_et.setText(""+itemcode);
                    Log.e ("NoValue","edititemadapterstatus"+edititemadapterstatus);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (requestCode == 2 && data != null) {
            try {
                Category = data.getStringExtra ("ProjectionCategory");
                empcategory_et.setText(""+Category);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onClick ( View v) {
        if (v == itemname_et) {
            Intent intent = new Intent (Projectonscr.this, ItemList_Spinner.class);
            intent.putExtra ("ScreenStatus","PS");
            intent.putExtra ("position1", ""+"");
            startActivityForResult(intent, 1);
        }
        if (v == empcategory_et) {
            Intent intent = new Intent (Projectonscr.this, ProjectionCategoryList_Spinner.class);
            intent.putExtra ("ScreenStatus","PS");
            startActivityForResult(intent, 2);
        }
        if(add==v) {
            if(frmdate_ET.getText().toString().trim().equalsIgnoreCase("")){
                Toast.makeText(this, "Kindly enter Fromdate", Toast.LENGTH_SHORT).show();
            }else  if(todate_ET.getText().toString().trim().equalsIgnoreCase("")){
                Toast.makeText(this, "Kindly enter Todate", Toast.LENGTH_SHORT).show();
            }else if (Quantity_et.getText().toString().trim().equalsIgnoreCase("")){
                Toast.makeText(this, "Kindly enter Qty", Toast.LENGTH_SHORT).show();
            }else  if(Itemcode_et.getText().toString().trim().equalsIgnoreCase("")){
                Toast.makeText(this, "Kindly Select Item", Toast.LENGTH_SHORT).show();
            }else  if(Category.equalsIgnoreCase("")){
                Toast.makeText(this, "Kindly Select Category", Toast.LENGTH_SHORT).show();
            }else  if(itemname.equalsIgnoreCase("")){
                Toast.makeText(this, "Kindly Select ItemName", Toast.LENGTH_SHORT).show();
            }else {

                Projection_Model Model = new Projection_Model();
                Model.setRowid ("0");
                Model.setItemcode(itemcode);
                Model.setName(itemname);
                Model.setQuantity(Quantity_et.getText().toString().trim());
                String price="";
                if(price_et.getText().toString().trim().equalsIgnoreCase("")){
                    price="0";

                }else {
                    price=price_et.getText().toString().trim();

                }
                Model.setPrice(price);
                projection_modelArrayList.add(Model);

                projectionRecycle.setVisibility(View.VISIBLE);
                projectionRecycle.setLayoutManager(new LinearLayoutManager(Projectonscr.this));
                Adapter = new ProjectionAdapter(getApplicationContext(), projection_modelArrayList);
                projectionRecycle.setAdapter(Adapter);
                projectionRecycle.setHasFixedSize(true);

                Quantity_et.setText("");
                price_et.setText("");

            }
        }
        if (v == save) {
            if (projection_modelArrayList.size()==0){
                Toast.makeText(this, "Atleast Add one Row Go to save", Toast.LENGTH_SHORT).show();
            } else {
                new AddProjection().execute();
            }
        }
        if (v==clear){
            projection_modelArrayList.clear();
            Adapter.notifyDataSetChanged();
            onRestart();
        }
        if (v==cancel){
            finish();
        }
    }

    private class GetProjectionHeader extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Projectonscr.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_GETProjectionHeader";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_GETProjectionHeader";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_GETProjectionHeader";
                Log.e("URL", URL);

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("DocNo",""+DocNo);
                Log.e("DocNo", ""+DocNo);

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

                jarraylist = new JSONArray(responseJSON);

                for (int l = 0; l < jarraylist.length(); l++) {
                    JSONObject jsonObject = jarraylist.getJSONObject (l);
                    ProjectionHeaderModel model = new ProjectionHeaderModel ( );

                    model.setDocno (jsonObject.getString ("Docno"));
                    model.setDocDate (jsonObject.getString ("DocDate"));
                    model.setSalesEmp (jsonObject.getString ("SalesEmp"));
                    model.setCategory (jsonObject.getString ("Category"));
                    model.setFromDt (jsonObject.getString ("FromDt"));
                    model.setToDt (jsonObject.getString ("ToDt"));
                    model.setCreatedBy (jsonObject.getString ("CreatedBy"));
                    model.setCreatedDate (jsonObject.getString ("CreatedDate"));
                    projectionHeaderModelArrayList.add (model);

                    ProjHeaderDocno = jsonObject.getString ("Docno");
                    ProjHeaderDocDate = jsonObject.getString ("DocDate");
                    ProjHeaderSalesEmp = jsonObject.getString ("SalesEmp");
                    ProjHeaderCategory = jsonObject.getString ("Category");
                    ProjHeaderFromDt = jsonObject.getString ("FromDt");
                    ProjHeaderToDt = jsonObject.getString ("ToDt");
                    ProjHeaderCreatedBy = jsonObject.getString ("CreatedBy");
                    ProjHeaderCreatedDate = jsonObject.getString ("CreatedDate");

                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (jarraylist.length() == 0) {
                Toast.makeText(Projectonscr.this, "No List", Toast.LENGTH_SHORT).show();
            } else {

                empcategory_et.setText (""+ProjHeaderCategory);
                frmdate_ET.setText (""+ProjHeaderFromDt);
                todate_ET.setText (""+ProjHeaderToDt);
                empcategory_et.setText (""+ProjHeaderCategory);
                empcategory_et.setText (""+ProjHeaderCategory);

                new GetProjectionHeaderDetails ().execute ();
            }
        }
    }

    private class GetProjectionHeaderDetails extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Projectonscr.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_GETProjectionDetails";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_GETProjectionDetails";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_GETProjectionDetails";
                Log.e("URL", URL);

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("DocNo",""+DocNo);
                Log.e("DocNo", ""+DocNo);

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
                projection_modelArrayList.clear ();
                jsonArray = new JSONArray(responseJSON);

                for (int l = 0; l < jsonArray.length(); l++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(l);
                    Projection_Model model = new Projection_Model();

                    model.setRowid(jsonObject.getString("Rowid"));
                    model.setDocNo(jsonObject.getString("DocNo"));
                    model.setItemcode (jsonObject.getString("ItemCode"));
                    model.setName (jsonObject.getString("ItemName"));
                    model.setQuantity (jsonObject.getString("Qty"));
                    model.setPrice(jsonObject.getString("Price"));
                    model.setCreatedBy(jsonObject.getString("CreatedBy"));
                    model.setCreatedDate(jsonObject.getString("CreatedDate"));
                    projection_modelArrayList.add(model);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (jsonArray.length() == 0) {
                Toast.makeText(Projectonscr.this, "No List", Toast.LENGTH_SHORT).show();
            } else {

                projectionRecycle.setVisibility(View.VISIBLE);
                projectionRecycle.setLayoutManager(new LinearLayoutManager(Projectonscr.this));
                Adapter = new ProjectionAdapter(getApplicationContext(), projection_modelArrayList);
                projectionRecycle.setAdapter(Adapter);
                projectionRecycle.setHasFixedSize(true);
            }
        }
    }

    public class ProjectionAdapter extends RecyclerView.Adapter {

        Context getApplicationContext;
        ArrayList<Projection_Model> projection_modelArrayList;
        String edited_qty = "",edited_price="", positionclicked;

        public ProjectionAdapter(Context getApplicationContext, ArrayList<Projection_Model> projection_modelArrayList) {
            this.getApplicationContext = getApplicationContext;
            this.projection_modelArrayList = projection_modelArrayList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.projectiondetail, parent, false);
            return new ProjectionAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position)
        {
            if(holder instanceof Projectonscr.ProjectionAdapter.ViewHolder)
            {
                int pos=position+1;

//                ((ViewHolder) holder ).sno.setText(""+pos);
                ((ViewHolder) holder).itemcode.setText(projection_modelArrayList.get(position).getItemcode());
                ((ViewHolder) holder).name.setText(projection_modelArrayList.get(position).getName());
                ((ViewHolder) holder).Quantity.setText(projection_modelArrayList.get(position).getQuantity());
                ((ViewHolder) holder).Price.setText(projection_modelArrayList.get(position).getPrice());

                ((ViewHolder) holder ).deletebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        projection_modelArrayList.remove(position);
                        notifyDataSetChanged();
                    }
                });

                ((ViewHolder) holder).name.setFocusable (false);
                ((ViewHolder) holder).name.setFocusableInTouchMode (false);
                ((ViewHolder) holder).name.setClickable (false);
                ((ViewHolder) holder).Price.setFocusable (false);
                ((ViewHolder) holder).Price.setFocusableInTouchMode (false);
                ((ViewHolder) holder).Quantity.setFocusable (false);
                ((ViewHolder) holder).Quantity.setFocusableInTouchMode (false);
                ((ViewHolder) holder).itemcode.setFocusable (false);
                ((ViewHolder) holder).itemcode.setFocusableInTouchMode (false);

                if (editstatus.equalsIgnoreCase ("1")) {
                    ((ViewHolder) holder).editrowbtn.setVisibility (View.VISIBLE);

                    ((ViewHolder) holder ).name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent (getApplicationContext, ItemList_Spinner.class);
                            intent.putExtra ("ScreenStatus","PS");
                            intent.putExtra ("position1", ""+position);
                            positionclicked = String.valueOf (position);
                            startActivityForResult(intent, 1);
                            //Toast.makeText (getApplicationContext, "Kindly Click on Edit to Change this Data", Toast.LENGTH_SHORT).show ( );
                        }
                    });
                    ((ViewHolder) holder ).Price.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText (getApplicationContext, "Kindly Click on Edit to Change this Data", Toast.LENGTH_SHORT).show ( );
                        }
                    });
                    ((ViewHolder) holder ).Quantity.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText (getApplicationContext, "Kindly Click on Edit to Change this Data", Toast.LENGTH_SHORT).show ( );
                        }
                    });
                    ((ViewHolder) holder ).itemcode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText (getApplicationContext, "Kindly Click on Edit to Change this Data", Toast.LENGTH_SHORT).show ( );
                        }
                    });

                    ((ViewHolder) holder).editrowbtn.setOnClickListener (new View.OnClickListener ( ) {
                        @Override
                        public void onClick(View view) {
                            String editposition = String.valueOf (position);
                            Log.e ("editposition",""+editposition);
                            Toast.makeText (getApplicationContext, "Now you can Edit this Row", Toast.LENGTH_SHORT).show ( );

                            ((ViewHolder) holder).name.setFocusable (true);
                            ((ViewHolder) holder).name.setFocusableInTouchMode (true);
                            ((ViewHolder) holder).name.setClickable (true);
                            ((ViewHolder) holder).Price.setFocusable (true);
                            ((ViewHolder) holder).Price.setFocusableInTouchMode (true);
                            ((ViewHolder) holder).Quantity.setFocusable (true);
                            ((ViewHolder) holder).Quantity.setFocusableInTouchMode (true);
                            ((ViewHolder) holder).itemcode.setFocusable (true);
                            ((ViewHolder) holder).itemcode.setFocusableInTouchMode (true);
                        }
                    });

                    ((ViewHolder) holder).Quantity.addTextChangedListener(new TextWatcher ()
                    {
                        @Override
                        public void afterTextChanged(Editable mEdit)
                        {
                            edited_qty = mEdit.toString();
                            Log.e ("edited_qty",edited_qty);
                            projection_modelArrayList.get(position).setQuantity (edited_qty);
                        }
                        public void beforeTextChanged(CharSequence s, int start, int count, int after){

                        }
                        public void onTextChanged(CharSequence s, int start, int before, int count){

                        }
                    });
                    ((ViewHolder) holder).Price.addTextChangedListener(new TextWatcher ()
                    {
                        @Override
                        public void afterTextChanged(Editable mEdit)
                        {
                            edited_price = mEdit.toString();
                            Log.e ("edited_price",edited_price);
                            projection_modelArrayList.get(position).setPrice (edited_price);
                        }
                        public void beforeTextChanged(CharSequence s, int start, int count, int after){

                        }
                        public void onTextChanged(CharSequence s, int start, int before, int count){

                        }
                    });

                    if (itemname.equalsIgnoreCase ("")) {
                        Log.e ("ITEMNAMEEMPTY","" + itemname);
                    } else {
                        Log.e ("position1","position1"+position1);
                        Log.e ("positionclicked","positionclicked"+positionclicked);
                        if (position1.equalsIgnoreCase (positionclicked)) {
                            ((ViewHolder) holder).name.setText (itemname);
                            ((ViewHolder) holder).itemcode.setText (itemcode);
                            Log.e ("ChangedVlues","itemname"+itemname);
                            Log.e ("ChangedVlues","itemcode"+itemcode);
                            projection_modelArrayList.get(position).setName (itemname);
                            projection_modelArrayList.get(position).setItemcode (itemcode);
                        } else {
                            Log.e ("Position","Not Match");
                        }
                    }
                } else {
                    ((ViewHolder) holder).editrowbtn.setVisibility (View.GONE);
                }
            }
        }
        @Override
        public int getItemCount()
        {
            return projection_modelArrayList.size();
        }
        public  class ViewHolder extends RecyclerView.ViewHolder
        {
            android.widget.LinearLayout LinearLayout;
            public TextView sno,itemcode,canceltv;
            EditText name, Quantity,Price;
            ImageButton deletebtn;
            ImageView editrowbtn;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                itemcode= itemView.findViewById(R.id.itemcode);
                name= itemView.findViewById(R.id.name);
                Quantity= itemView.findViewById(R.id.Quantity);
                Price= itemView.findViewById(R.id.Price);

                canceltv= itemView.findViewById(R.id.canceltv);

                deletebtn= itemView.findViewById(R.id.deletebtn);
                editrowbtn= itemView.findViewById(R.id.editrowbtn);
            }
        }
    }

    private class AddProjection extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Projectonscr.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_AddProjection";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_AddProjection";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_AddProjection";
                Log.e("URL", URL);

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                if (editstatus.equalsIgnoreCase ("1")) {
                    Request.addProperty ("DocNo", "" + DocNo);
                    Log.e ("DocNo", ""+DocNo);
                } else {
                    Request.addProperty ("DocNo", "" + 0);
                    Log.e ("DocNo", "0");
                }
                Request.addProperty("DocDate", "");
                Log.e("DocDate", "");

                Request.addProperty("SalesEmp", "" + sessionempid);
                Log.e("SalesEmp", "" + sessionempid);

                Request.addProperty("Category", "" + empcategory_et.getText ().toString ());
                Log.e("Category", "" + empcategory_et.getText ().toString ());

                Request.addProperty("FromDt", ""+ frmdate_ET.getText().toString());
                Log.e("FromDt", "" + frmdate_ET.getText().toString());

                Request.addProperty("ToDt", "" + todate_ET.getText().toString());
                Log.e("ToDt", "" + todate_ET.getText().toString());

                Request.addProperty("User", ""+sessionempid);
                Log.e("User",""+sessionempid );

                String bookingitem="";
                for (int io = 0; io < projection_modelArrayList.size(); io++) {

                    String xmlrowid="",xmlitemcode="",xmlname="",xmlQuantity="",xmlPrice="";

                    xmlrowid = projection_modelArrayList.get(io).getRowid ();
                    xmlitemcode = projection_modelArrayList.get(io).getItemcode();
                    xmlname = projection_modelArrayList.get(io).getName();
                    xmlQuantity = projection_modelArrayList.get(io).getQuantity();
                    xmlPrice = projection_modelArrayList.get(io).getPrice();

                    int Empid= Integer.parseInt(sessionempid);
                    String date = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
                    bookingitem = bookingitem + "<Table1><RowId>"+xmlrowid+"</RowId><ItemCode>"+ xmlitemcode +"</ItemCode><ItemName>" + xmlname + "</ItemName><Qty>" + ""+xmlQuantity + "</Qty><Price>" + ""+xmlPrice + "</Price>" +
                            "<CreatedBy>" + Empid + "</CreatedBy><CreateDate>" + date + "</CreateDate></Table1>";
                }
                bookingitem = "<NewDataSet>" + bookingitem + "</NewDataSet>";

                Request.addProperty("ItemDetailXML", ""+bookingitem);
                Log.e("ItemDetailXML", ""+bookingitem);

                Request.addProperty("ItemDetailXMLID", ""+1 );
                Log.e("ItemDetailXMLID", ""+1 );

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(Request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                androidHttpTransport.call(SOAP_ACTION, envelope);
                strfault = "";
                if (envelope.bodyIn instanceof SoapFault) {
                    strfault = ( (SoapFault) envelope.bodyIn ).faultstring;
                    Log.i("Error", strfault);
                } else {
                    SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                    Log.e("WS", String.valueOf(resultsRequestSOAP));
                }
                SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                launch = ( "" + resultsRequestSOAP.getProperty(0));
            } catch (Exception exg) {
                exg.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (launch.equalsIgnoreCase("1")) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Projectonscr.this);
                builder.setMessage(" Saved ")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                projection_modelArrayList.clear();
                                finish();
                            }
                        });
                android.app.AlertDialog alert = builder.create();
                alert.show();
            } else if (launch.equalsIgnoreCase("2")) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Projectonscr.this);
                builder.setMessage(" Updated ")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                startActivity (new Intent (Projectonscr.this, ViewProjection.class));
                                finish();
                            }
                        });
                android.app.AlertDialog alert = builder.create();
                alert.show();
            } else {
                android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(Projectonscr.this);
                builder1.setMessage("Something Went Wrong");
                builder1.setCancelable(false);
                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        }
    }
}


