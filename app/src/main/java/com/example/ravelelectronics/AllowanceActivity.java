package com.example.ravelelectronics;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ravelelectronics.model.AllowanceModel;
import com.example.ravelelectronics.model.CurrencyModel;
import com.example.ravelelectronics.spinnerclasses.CurrencyList_Spinner;
import com.example.ravelelectronics.util.FilePath;
import com.example.ravelelectronics.util.SessionManagement;
import com.example.ravelelectronics.util.Utility;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AllowanceActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout referencenolayout;
    EditText referenceno_et;
    TextView edittv;
    RecyclerView recycleallowance;
    RecyclerView.LayoutManager LinearLayoutManager;
    JSONArray jsonArray;
    ProgressDialog pDialog;
    RecyclerView allowanceRecycle;
    RecyclerView.Adapter Adapter;

    EditText employee_et,cusname_et,fare_et,currency_et,docdate_et,appstatus_et,remarks_et;
    EditText allowancedate_et;

    String sessionempname="",sessionempid="",sessionmanager="";
    SessionManagement session;
    String strfault="",launch="";
    JSONArray jarraylist;
    Button Btnsave,Btncancel,Btnadd,Btnclear,approve,reject,cancel1,BtnsaveAS;

    MaterialSpinner advtype_spinner,allowancetype_spinner,emp_spinner,Currency_spin,traveltype_spinner,regulartype_spinner;
    ArrayList<AllowanceModel> allowanceModelArrayList;
    String Currname = "", Currcode = "";
    List<String> CurrencyModel1 = new ArrayList<>();
    ArrayList<CurrencyModel> currencyModelArrayList;

    String[] advtype = {"SelectType","Regular Advance", "Travel Advance"};
    String Advtype="",RegularAdvance="",TravelAdvance="";
    String[] allowancetype = {"SelectType","Bike", "Auto","Bus","Call Taxi"};
    String Allowancetype="";
    LinearLayout regulartypelayout,traveltypelayout;
    String[] regulartype = {"SelectType","Beverages", "food","complementory ","stationary"};
    String Regulartype="";
    String[] traveltype = {"SelectType","Flight", "Train","Bus","Auto","Cab"};
    String Traveltype="";
    String headerDocNo="",Allowance_Date="",Employee="",CreatedBy="",CreateDate="",AppStatus="",Remarks="",Act_Allowance_Date="";
    String DocNo = "",editstatus = "";
    String position1;
    String clickstatusadd = "0";
    String Approvestatus="",ApproveRemarks="";
    String allowancetype_param = "";
    String ReferenceNo = "";

    CharSequence[] items;

    Button imageupload_btn,imageupload_btn1;
    ImageView imageView_pic,imageView_pic1;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 401;
    private final int REQUEST_IMAGE=301;
    String attach_Image="",attach_Image1="";
    private String SERVER_URL = "http://103.48.182.209:85/ravelupload/a.php";
    private String imagepath = "http://103.48.182.209:85/ravelupload/AndroidPdfUpload/";

    String selectedFilePath, attachemntpath = "", attachemntpath1 = "";
    String Imagestatus = "";
    String btnclickstatus = "";
    DatePickerDialog dp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allowance);

        pDialog = new ProgressDialog(AllowanceActivity.this);

        Intent intent = getIntent ();
        DocNo = intent.getStringExtra ("DocNo");
        editstatus = intent.getStringExtra ("editstatus");
        Log.e ("DocNo",DocNo);
        Log.e ("editstatus",editstatus);

        edittv =  findViewById(R.id.edittv);

        imageView_pic =  findViewById(R.id.imageView_pic);
        imageView_pic1 =  findViewById(R.id.imageView_pic1);

        imageupload_btn =  findViewById(R.id.imageupload_btn);
        imageupload_btn1 =  findViewById(R.id.imageupload_btn1);
        imageupload_btn.setOnClickListener(this);
        imageupload_btn1.setOnClickListener(this);

        referenceno_et =  findViewById(R.id.referenceno_et);
        referencenolayout =  findViewById(R.id.referencenolayout);
        remarks_et =  findViewById(R.id.remarks_et);

        regulartypelayout =  findViewById(R.id.regulartypelayout);
        traveltypelayout =  findViewById(R.id.traveltypelayout);
        appstatus_et =  findViewById(R.id.appstatus_et);
        approve =  findViewById(R.id.approve);
        reject =  findViewById(R.id.reject);
        cancel1 =  findViewById(R.id.cancel1);
        employee_et =  findViewById(R.id.employee_et);
        cusname_et = (EditText) findViewById(R.id.cusname_et);
        docdate_et = (EditText) findViewById(R.id.docdate_et);
        allowancedate_et = (EditText) findViewById(R.id.allowancedate_et);
        Currency_spin =  findViewById(R.id.Currency_spin);
        Btnsave = (Button) findViewById(R.id.Btnsave);
        Btncancel = (Button) findViewById(R.id.Btncancel);
        Btnadd = (Button) findViewById(R.id.Btnadd);
        Btnclear = (Button) findViewById(R.id.Btnclear);
        fare_et = (EditText) findViewById(R.id.fare_et);
        advtype_spinner = findViewById(R.id.advtype_spinner);
        currency_et = findViewById(R.id.currency_et);
        allowancetype_spinner =findViewById(R.id.allowancetype_spinner);
        regulartype_spinner =findViewById(R.id.regulartype_spinner);
        traveltype_spinner =findViewById(R.id.traveltype_spinner);
        BtnsaveAS =findViewById(R.id.BtnsaveAS);

        BtnsaveAS.setOnClickListener(this);
        Btnsave.setOnClickListener(this);
        Btncancel.setOnClickListener(this);
        Btnadd.setOnClickListener(this);
        Btnclear.setOnClickListener(this);
        currency_et.setOnClickListener (this);
        approve.setOnClickListener (this);
        reject.setOnClickListener (this);

        allowanceModelArrayList = new ArrayList<AllowanceModel>();
        allowanceRecycle = (RecyclerView) findViewById(R.id.allowanceRecycle);

        session = new SessionManagement(AllowanceActivity.this);
        HashMap<String, String> user = session.getUserDetails();
        sessionempname = user.get(SessionManagement.KEY_NAME);
        sessionempid = user.get(SessionManagement.KEY_CODE);
        sessionmanager = user.get(SessionManagement.KEY_MANAGER);
        Log.e ("sessionempname",""+sessionempname);
        Log.e ("sessionempid",""+sessionempid);
        Log.e ("sessionmanager",""+sessionmanager);

         if (editstatus.equalsIgnoreCase ("1")) {
            edittv.setVisibility (View.VISIBLE);
            Btnadd.setVisibility(View.GONE);
            Btnclear.setVisibility(View.GONE);
            new GetAllowanceHeader ( ).execute ( );
            Btnsave.setText ("Update");
            Btnsave.setVisibility (View.VISIBLE);
            BtnsaveAS.setVisibility (View.VISIBLE);
            referencenolayout.setVisibility(View.VISIBLE);
            referenceno_et.setText(DocNo);
            approve.setVisibility (View.GONE);
            reject.setVisibility (View.GONE);
            cancel1.setVisibility (View.GONE);
        } else if(editstatus.equalsIgnoreCase ("2")) {
            Log.e ("Coming for", "Edit" + editstatus);
            edittv.setVisibility (View.GONE);
            Btnsave.setVisibility (View.GONE);
            Btnclear.setVisibility (View.GONE);

            new GetAllowanceHeader().execute();

            Btnadd.setVisibility (View.GONE);
            Btncancel.setVisibility (View.GONE);
            docdate_et.setFocusable(false);
            docdate_et.setFocusableInTouchMode(false);
            docdate_et.setEnabled(false);
            allowancedate_et.setFocusable(false);
//            allowancedate_et.setFocusableInTouchMode(false);
//            allowancedate_et.setEnabled(false);
            employee_et.setFocusable(false);
            employee_et.setFocusableInTouchMode(false);
            employee_et.setEnabled(false);
            cusname_et.setFocusable(false);
            cusname_et.setFocusableInTouchMode(false);
            cusname_et.setEnabled(false);
            advtype_spinner.setFocusable(false);
            advtype_spinner.setFocusableInTouchMode(false);
            advtype_spinner.setEnabled(false);
            allowancetype_spinner.setFocusable(false);
            allowancetype_spinner.setFocusableInTouchMode(false);
            allowancetype_spinner.setEnabled(false);
            fare_et.setFocusable(false);
            fare_et.setFocusableInTouchMode(false);
            fare_et.setEnabled(false);
            currency_et.setFocusable(false);
            currency_et.setFocusableInTouchMode(false);
            currency_et.setEnabled(false);
        } else {
            edittv.setVisibility (View.GONE);
            approve.setVisibility (View.GONE);
            reject.setVisibility (View.GONE);
            cancel1.setVisibility (View.GONE);
            Log.e ("Coming from", "DashBoard" + editstatus);
        }

        employee_et.setText(sessionempname);

        currencyModelArrayList = new ArrayList<>();

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        docdate_et.setText(date);
        allowancedate_et.setText(date);

        allowancedate_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                final int y = c.get(Calendar.YEAR);
                final int m = c.get(Calendar.MONTH);
                final int d = c.get(Calendar.DAY_OF_MONTH);

                dp = new DatePickerDialog(AllowanceActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Calendar dateSelected = Calendar.getInstance();
                                dateSelected.set(year, monthOfYear, dayOfMonth);

                                SimpleDateFormat formatterfrm = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                                allowancedate_et.setText(formatterfrm.format(dateSelected.getTime()));
                                Log.d("docdate", allowancedate_et.getText().toString());
                            }
                        }, y, m, d);
                dp.show();
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>Allowance</small>"));
        }

        ArrayAdapter s = new ArrayAdapter(this, android.R.layout.simple_spinner_item, advtype);
        s.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        advtype_spinner.setAdapter(s);

        advtype_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Advtype = item.toString();
                Log.e("advtype", Advtype);

               if(Advtype.equalsIgnoreCase("Regular Advance")) {
                    regulartypelayout.setVisibility(View.GONE);
                    traveltypelayout.setVisibility(View.VISIBLE);

                   ArrayAdapter c = new ArrayAdapter(AllowanceActivity.this, android.R.layout.simple_spinner_item, regulartype);
                   c.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                   traveltype_spinner.setAdapter(c);
                } else if(Advtype.equalsIgnoreCase("Travel Advance")) {
                    traveltypelayout.setVisibility(View.VISIBLE);
                    regulartypelayout.setVisibility(View.GONE);

                   ArrayAdapter c = new ArrayAdapter(AllowanceActivity.this, android.R.layout.simple_spinner_item, traveltype);
                   c.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                   traveltype_spinner.setAdapter(c);
//                   ArrayAdapter d = new ArrayAdapter(AllowanceActivity.this, android.R.layout.simple_spinner_item, traveltype);
//                   d.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                } else {
                    traveltypelayout.setVisibility(View.GONE);
                    regulartypelayout.setVisibility(View.GONE);
                }
              }
        });

        ArrayAdapter a = new ArrayAdapter(this, android.R.layout.simple_spinner_item, allowancetype);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        allowancetype_spinner.setAdapter(a);

        allowancetype_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Allowancetype = item.toString();
                Log.e("Allowancetype", Allowancetype);
            }
        });

//        ArrayAdapter c = new ArrayAdapter(this, android.R.layout.simple_spinner_item, regulartype);
//        c.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        regulartype_spinner.setAdapter(c);

        regulartype_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Regulartype = item.toString();
                allowancetype_param = item.toString();
                Log.e("Regulartype", Regulartype);
            }
        });

//        ArrayAdapter d = new ArrayAdapter(this, a
//        ndroid.R.layout.simple_spinner_item, traveltype);
//        d.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        traveltype_spinner.setAdapter(d);

        traveltype_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Traveltype = item.toString();
                allowancetype_param = item.toString();
                Log.e("Traveltype", Traveltype);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data!=null) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == 2 && data != null) {
            try {
                Currcode = data.getStringExtra ("Currcode");
                Currname = data.getStringExtra ("Currname");
                String editadapterstatus = "";
                editadapterstatus = data.getStringExtra ("editadapterstatus");
                position1 = data.getStringExtra ("position1");

                if (position1.equalsIgnoreCase ("")) {
                    currency_et.setText (""+Currname);
                } else {
                    Adapter.notifyItemChanged(Integer.parseInt (position1));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }  else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private boolean checkAndRequestPermissions() {
        int externalStoragePermission = ContextCompat.checkSelfPermission(AllowanceActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission=ContextCompat.checkSelfPermission(AllowanceActivity.this,Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (externalStoragePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(AllowanceActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private void selectImage() {

        items = new CharSequence[]{"Take Photo", "Choose from Library",
                    "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AllowanceActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(AllowanceActivity.this);

                    if (items[item].equals("Take Photo")) {
                        // userChoosenTask ="Take Photo";
                        if (result)
                            cameraIntent();

                    } else if (items[item].equals("Choose from Library")) {
                        //  userChoosenTask ="Choose from Library";
                        if (result)
                            galleryIntent();

                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
        Log.e("GALLERYINTENT", "GGGGGGG");
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
        Log.e("CAMERAINTENT","IIIIIII");
    }

    @SuppressLint("NewApi")
    private void onCaptureImageResult(Intent data) {
        Log.e("CAMERAINTENT","PPPPPP");
        Bitmap thumbnail = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            thumbnail = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        String path = MediaStore.Images.Media.insertImage(AllowanceActivity.this.getContentResolver(), thumbnail, "Title", null);
        Uri URI = Uri.parse(path);
        Log.e("TAG","Selected FilePATH:" + path);
        Log.e("TAG","Selected FileXX:" + URI);
//        Bitmap bm=null;
        if (data != null) {
            try {
//                bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
//                Uri selectedFileUri = data.getData();
//                String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), output, "Title", null);
//                selectedFilePath = path;
                selectedFilePath = FilePath.getPath(AllowanceActivity.this,URI);
                Log.e("TAG","Selected File Path:" + selectedFilePath);
                Log.e("TAG","Selected File Path LENGTH" + selectedFilePath.length());
                if (selectedFilePath.length() == 0) {
                    Log.e("TAG","Selected File Path LENGTHDDD" + selectedFilePath.length());
                } else {
//                    if (Imagestatus.equalsIgnoreCase("0")) {
                        Log.e("TAG", "Selected File Path LENGTHSSS" + selectedFilePath.length());
                        uploadFile(selectedFilePath);
//                    } else {
//                        Log.e("TAG", "Selected File Path LENGTHSSS" + selectedFilePath.length());
//                        uploadFile1(selectedFilePath);
//                    }
                }
//                if(selectedFilePath != null && !selectedFilePath.equals("")){
//                    uploadFile(selectedFilePath);
//                }else{
//                    Toast.makeText(getActivity(),"Cannot upload file to server",Toast.LENGTH_SHORT).show();
//                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void onSelectFromGalleryResult(Intent data) {

        Log.e("CAMERAINTENT","PPPPPP");
        Bitmap thumbnail = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            try {
                thumbnail = MediaStore.Images.Media.getBitmap(AllowanceActivity.this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        String path = MediaStore.Images.Media.insertImage(AllowanceActivity.this.getContentResolver(), thumbnail, "Title", null);
        Uri URI = Uri.parse(path);
        Log.e("TAG","Selected FilePATH:" + path);
        Log.e("TAG","Selected FileXX:" + URI);
//        Bitmap bm=null;
        if (data != null) {
            try {
//                bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
//                Uri selectedFileUri = data.getData();
//                String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), output, "Title", null);
//                selectedFilePath = path;
                selectedFilePath = FilePath.getPath(AllowanceActivity.this,URI);
                Log.e("TAG","Selected File Path:" + selectedFilePath);
                Log.e("TAG","Selected File Path LENGTH" + selectedFilePath.length());
                if (selectedFilePath.length() == 0) {
                    Log.e("TAG","Selected File Path LENGTHDDD" + selectedFilePath.length());
                } else {
//                    if (Imagestatus.equalsIgnoreCase("0")) {
                        Log.e("TAG", "Selected File Path LENGTHSSS" + selectedFilePath.length());
                        uploadFile(selectedFilePath);
//                    } else {
//                        Log.e("TAG", "Selected File Path LENGTHSSS" + selectedFilePath.length());
//                        uploadFile1(selectedFilePath);
//                    }
                }
//                if(selectedFilePath != null && !selectedFilePath.equals("")){
//                    uploadFile(selectedFilePath);
//                }else{
//                    Toast.makeText(getActivity(),"Cannot upload file to server",Toast.LENGTH_SHORT).show();
//                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public int uploadFile( final String selectedFilePath){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        int serverResponseCode = 0;
        final HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead,bytesAvailable,bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);
        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length-1];

        if (!selectedFile.isFile()){
//            dialog.dismiss();
            AllowanceActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                }
            });
            return 0;
        }else{
            try{
                FileInputStream fileInputStream = new FileInputStream(selectedFile);

                URL url = new URL(SERVER_URL);
                Log.e("URL", ""+ url);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file",selectedFilePath);
                //connection.setRequestProperty("sess_file",sessionempid);
                // connection.set
                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());
                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + selectedFilePath + "\"" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];
                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer,0,bufferSize);
                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0){
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer,0,bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable,maxBufferSize);
                    bytesRead = fileInputStream.read(buffer,0,bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();

                Log.i("TAG", "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                //response code of 200 indicates the server status OK
                if (serverResponseCode == 200) {
                    AllowanceActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //tvFileName.setText("File Upload completed."+ fileName);
                            Log.e("Success","TESTXXX1");
                            AlertDialog.Builder builder = new AlertDialog.Builder(AllowanceActivity.this);
                            builder.setMessage("File Upload completed.")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Log.e("Success","TESTXXX2");
                                            dialog.cancel();
                                            try {

                                                BufferedReader br = new BufferedReader(new InputStreamReader(
                                                        connection.getInputStream(), "utf-8"));
                                                String line = null;
                                                StringBuilder sb = new StringBuilder();
                                                while ((line = br.readLine()) != null) {
                                                    sb.append(line + "\n");
                                                }
                                                br.close();
                                                System.out.println("" + sb.toString());
                                                Log.e("Success",sb.toString());
                                                Log.e("Success","ATTACHEMNTFILE" + imagepath);

                                                if (Imagestatus.equalsIgnoreCase("0")) {
                                                    attachemntpath = imagepath + sb;
                                                    Log.e("Success", "ATTACHMENTPATH__0" + attachemntpath);
//                                                    Picasso.with(AllowanceActivity.this).load(attachemntpath).into(imageView_pic);
                                                    Picasso.with(AllowanceActivity.this).load(attachemntpath).into(imageView_pic);

                                                    attach_Image = attachemntpath;
                                                    Log.e("Success", "attach_Image0" + attach_Image);
                                                    Toast.makeText(getApplicationContext(), "Image Attached Successfully", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    attachemntpath1 = imagepath + sb;
                                                    Log.e("Success", "ATTACHMENTPATH__1" + attachemntpath1);
                                                    Picasso.with(AllowanceActivity.this).load(attachemntpath1).into(imageView_pic1);

                                                    attach_Image1 = attachemntpath1;
                                                    Log.e("Success", "attach_Image1" + attach_Image1);
                                                    Toast.makeText(getApplicationContext(), "Image Attached Successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (Exception ee) {
                                                ee.printStackTrace();
                                            }
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                            Log.e("Success","TESTXXX4");
                        }
                    });
                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                AllowanceActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AllowanceActivity.this,"File Not Found",Toast.LENGTH_SHORT).show();
                        Toast.makeText(AllowanceActivity.this, attachemntpath, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(AllowanceActivity.this, "URL error!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(AllowanceActivity.this, "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
            }
//            dialog.dismiss();
            return serverResponseCode;
        }
    }

    public void onClick ( View v) {
        if (v == imageupload_btn) {
            if (checkAndRequestPermissions()) {
                Imagestatus = "0";
                selectImage();
            }
        }
        if (v == imageupload_btn1) {
            if (checkAndRequestPermissions()) {
                Imagestatus = "1";
                selectImage();
            }
        }
        if (v == currency_et) {
            Intent intent = new Intent (AllowanceActivity.this, CurrencyList_Spinner.class);
            intent.putExtra ("ScreenStatus","AA");
            intent.putExtra ("position1", ""+"");
            startActivityForResult(intent, 2);
        }
        if(Btnadd==v) {
            if (cusname_et.getText().toString().trim().equalsIgnoreCase("")){
                Toast.makeText(this, "Kindly enter Customername", Toast.LENGTH_SHORT).show();
            } else if (fare_et.getText().toString().trim().equalsIgnoreCase("")){
                Toast.makeText(this, "Kindly enter Fare", Toast.LENGTH_SHORT).show();
            } else if (Advtype.equalsIgnoreCase("") || Advtype.equalsIgnoreCase ("SelectType")){
                Toast.makeText(this, "Kindly Select AdvType", Toast.LENGTH_SHORT).show();
            } else if (allowancetype_param.equalsIgnoreCase("") || Allowancetype.equalsIgnoreCase ("SelectType")){
                Toast.makeText(this, "Kindly Select Allowance type", Toast.LENGTH_SHORT).show();
            } else if (Currname.equalsIgnoreCase("")){
                Toast.makeText(this, "Kindly Select Currency", Toast.LENGTH_SHORT).show();
            } else {

                clickstatusadd = "1";
                AllowanceModel allowanceModel = new AllowanceModel();
                allowanceModel.setCustomer (cusname_et.getText().toString().trim());
                allowanceModel.setAdvtype(Advtype);
                allowanceModel.setAllowanceType(allowancetype_param);
                allowanceModel.setCurrency(Currname);
                allowanceModel.setFare(fare_et.getText().toString().trim());
//                allowanceModel.setApprovedBy (sessionmanager);
                allowanceModel.setId ("0");

                allowanceModelArrayList.add(allowanceModel);

                allowanceRecycle.setVisibility(View.VISIBLE);
                allowanceRecycle.setLayoutManager(new LinearLayoutManager(AllowanceActivity.this));
                Adapter = new AllowanceAdapter(getApplicationContext(), allowanceModelArrayList);
                allowanceRecycle.setAdapter(Adapter);
                allowanceRecycle.setHasFixedSize(true);

                cusname_et.setText("");
                fare_et.setText("");
            }
        }
        if (v == Btnsave) {
            if (allowanceModelArrayList.size()==0) {
                Toast.makeText(this, "Kindly add atleast one Row", Toast.LENGTH_SHORT).show();
            }else {
                new AddAllowance().execute();
            }
        }
        if (v == BtnsaveAS) {
            if (allowanceModelArrayList.size()==0) {
                Toast.makeText(this, "Kindly add atleast one Row", Toast.LENGTH_SHORT).show();
            } else {
                btnclickstatus = "saveas";
                new AddAllowance().execute();
            }
        }
        if (v==Btnclear){
            allowanceModelArrayList.clear();
            Adapter.notifyDataSetChanged();
            onRestart();
        }
        if (v==Btncancel){
            finish();
        }
        if (v==approve){
            Approvestatus="A";
            new AddAllowance_Approval().execute();
        }
        if (v==reject){
            // custom dialog
            final Dialog dialog = new Dialog(AllowanceActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
            dialog.setContentView(R.layout.edit_approve_type_dialog);

            RelativeLayout close_layout = dialog.findViewById(R.id.close_layout);
            TextView header_TV = dialog.findViewById(R.id.header_TV);
            final EditText dialogremarks_et = dialog.findViewById(R.id.dialogremarks_et);
            Button dialogbtn_ok = dialog.findViewById(R.id.dialogbtn_ok);

            header_TV.setText("Do you want to Reject?");

            close_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialogbtn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialogremarks_et.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(AllowanceActivity.this, "Kindly give reason for Rejecting", Toast.LENGTH_SHORT).show();
                    } else {
                        ApproveRemarks=dialogremarks_et.getText().toString().trim();
                        Approvestatus="R";
                        new AddAllowance_Approval().execute();
                        dialog.dismiss();
                    }
                }
            });
            dialog.show();
        }
    }

    private class GetAllowanceHeader extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AllowanceActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_GETAllowanceHeader";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_GETAllowanceHeader";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_GETAllowanceHeader";
                Log.e("URL", URL);

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("DocNo", ""+DocNo );
                Log.e("DocNo", ""+DocNo );

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
                    JSONObject jsonObject = jarraylist.getJSONObject(l);
                    headerDocNo = jsonObject.getString ("DocNo");
                    Allowance_Date = jsonObject.getString ("Allowance Date");
                    Employee = jsonObject.getString ("Employee");
                    CreatedBy = jsonObject.getString ("CreatedBy");
                    CreateDate = jsonObject.getString ("CreateDate");
                    AppStatus = jsonObject.getString ("AppStatus");
                    Remarks = jsonObject.getString ("Remarks");
                    Act_Allowance_Date = jsonObject.getString ("Act_Allowance Date");
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
                Toast.makeText(AllowanceActivity.this, "No List", Toast.LENGTH_SHORT).show();
            } else {
                docdate_et.setText (""+Allowance_Date);
                allowancedate_et.setText (""+Act_Allowance_Date);
                remarks_et.setText (""+Remarks);

                if (AppStatus.equalsIgnoreCase("A")) {
                    appstatus_et.setText("Approved");
                } else if (AppStatus.equalsIgnoreCase("R")) {
                    appstatus_et.setText("Rejected");
                } else {
                    appstatus_et.setText("Pending");
                }

                if (AppStatus.equalsIgnoreCase("P") || AppStatus.equalsIgnoreCase("")){
                    approve.setVisibility (View.VISIBLE);
                    reject.setVisibility (View.VISIBLE);
                    cancel1.setVisibility (View.VISIBLE);
                } else if (AppStatus.equalsIgnoreCase("R")) {
                    Btnsave.setVisibility(View.GONE);
                    BtnsaveAS.setVisibility(View.VISIBLE);
                    referencenolayout.setVisibility(View.VISIBLE);
                    referenceno_et.setText(headerDocNo);
                } else {
                    approve.setVisibility (View.GONE);
                    reject.setVisibility (View.GONE);
                    cancel1.setVisibility (View.GONE);
                }

                if (!sessionempid.equalsIgnoreCase (CreatedBy) && sessionmanager.equalsIgnoreCase ("Y")) {
                    if (AppStatus.equalsIgnoreCase("P") || AppStatus.equalsIgnoreCase("")  ){
                        approve.setVisibility (View.VISIBLE);
                        reject.setVisibility (View.VISIBLE);
                        cancel1.setVisibility (View.VISIBLE);
                    } else {

                        approve.setVisibility (View.GONE);
                        reject.setVisibility (View.GONE);
                        cancel1.setVisibility (View.GONE);
                    }
                } else {
                    approve.setVisibility (View.GONE);
                    reject.setVisibility (View.GONE);
                    cancel1.setVisibility (View.GONE);
                }

                new GetAllowanceHeaderDetails ().execute ();
            }
        }
    }

    private class GetAllowanceHeaderDetails extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AllowanceActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_GETAllowanceDetails";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_GETAllowanceDetails";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_GETAllowanceDetails";
                Log.e("URL", URL);

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("DocNo", ""+DocNo );
                Log.e("DocNo", ""+DocNo );

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
                    JSONObject jsonObject = jarraylist.getJSONObject(l);

                    AllowanceModel model = new AllowanceModel ();
                    model.setId (jsonObject.getString ("id"));
                    model.setDocno (jsonObject.getString ("docno"));
                    model.setAdvtype (jsonObject.getString ("AdvType"));
                    model.setAllowanceType (jsonObject.getString ("AllowanceType"));
                    model.setFare (jsonObject.getString ("Fare"));
                    model.setCurrency (jsonObject.getString ("Currency"));
                    model.setAppByCode (jsonObject.getString ("AppByCode"));
                    model.setAppByName (jsonObject.getString ("AppByName"));
                    model.setAppStatus (jsonObject.getString ("AppStatus"));
                    model.setSaleEmpName (jsonObject.getString ("SaleEmpName"));
                    model.setCreatedBy (jsonObject.getString ("CreatedBy"));
                    model.setCreateDate (jsonObject.getString ("CreateDate"));
                    model.setCustomer (jsonObject.getString ("Customer"));
                    allowanceModelArrayList.add (model);
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
                Toast.makeText(AllowanceActivity.this, "No List", Toast.LENGTH_SHORT).show();
            } else {
                allowanceRecycle.setVisibility(View.VISIBLE);
                allowanceRecycle.setHasFixedSize(true);
                allowanceRecycle.setLayoutManager(new LinearLayoutManager(AllowanceActivity.this));
                Adapter = new AllowanceAdapter(getApplicationContext(), allowanceModelArrayList);
                allowanceRecycle.setAdapter(Adapter);
            }
        }
    }

    public class AllowanceAdapter extends RecyclerView.Adapter {

        Context getApplicationContext;
        ArrayList<AllowanceModel> allowanceModelArrayList;
        String edited_customername,edited_fare,positionclicked;

        public AllowanceAdapter(Context getApplicationContext, ArrayList<AllowanceModel> allowanceModelArrayList) {
            this.getApplicationContext = getApplicationContext;
            this.allowanceModelArrayList = allowanceModelArrayList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.allowancedetail, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position)
        {
            if(holder instanceof ViewHolder)
            {
                int pos=position+1;

//                ((ViewHolder) holder ).sno.setText(""+pos);
                ((ViewHolder) holder).customername.setText(allowanceModelArrayList.get(position).getCustomer ());
                ((ViewHolder) holder).Advtype.setText(allowanceModelArrayList.get(position).getAdvtype());
                ((ViewHolder) holder).AllowanceType.setText(allowanceModelArrayList.get(position).getAllowanceType());
                ((ViewHolder) holder).Fare.setText(allowanceModelArrayList.get(position).getFare());
                ((ViewHolder) holder).Currency.setText(allowanceModelArrayList.get(position).getCurrency());
                ((ViewHolder) holder).Attachment.setText(allowanceModelArrayList.get(position).getAttachment());
                ((ViewHolder) holder).ApprovedBy.setText(allowanceModelArrayList.get(position).getApprovedBy());


                Log.e ("sessionempid",""+sessionempid);
                Log.e ("getCreatedBy","LLL"+allowanceModelArrayList.get (position).getCreatedBy ());
                if (!sessionempid.equalsIgnoreCase (allowanceModelArrayList.get (position).getCreatedBy ()) && sessionmanager.equalsIgnoreCase ("Y")) {
                    ((ViewHolder) holder).approve_btn.setVisibility (View.GONE);
                    ((ViewHolder) holder).reject_btn.setVisibility (View.GONE);
                } else {
                    ((ViewHolder) holder).approve_btn.setVisibility (View.GONE);
                    ((ViewHolder) holder).reject_btn.setVisibility (View.GONE);
                }

                if (clickstatusadd.equalsIgnoreCase ("1")) {
                    Log.e ("clickstatusadd","PPP"+clickstatusadd);
                    ((ViewHolder) holder).approve_btn.setVisibility (View.GONE);
                    ((ViewHolder) holder).reject_btn.setVisibility (View.GONE);
                } else {
                    Log.e ("clickstatusadd","OOO"+clickstatusadd);
                }

                ((ViewHolder) holder ).deletebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        allowanceModelArrayList.remove(position);
                        notifyDataSetChanged();
                    }
                });

                ((ViewHolder) holder).approve_btn.setOnClickListener (new View.OnClickListener ( ) {
                    @Override
                    public void onClick(View view) {
                        ((ViewHolder) holder).approve_btn.setTextColor (getResources ().getColor (R.color.green));
                        ((ViewHolder) holder).reject_btn.setTextColor (getResources ().getColor (R.color.red));
                        allowanceModelArrayList.get (position).setApprovedStatus ("Y");
                    }
                });
                ((ViewHolder) holder).reject_btn.setOnClickListener (new View.OnClickListener ( ) {
                    @Override
                    public void onClick(View view) {
                        ((ViewHolder) holder).approve_btn.setTextColor (getResources ().getColor (R.color.red));
                        ((ViewHolder) holder).reject_btn.setTextColor (getResources ().getColor (R.color.green));
                        allowanceModelArrayList.get (position).setApprovedStatus ("N");
                    }
                });

                ((ViewHolder) holder).customername.setFocusable (false);
                ((ViewHolder) holder).customername.setFocusableInTouchMode (false);
                ((ViewHolder) holder).Advtype.setFocusable (false);
                ((ViewHolder) holder).Advtype.setFocusableInTouchMode (false);
                ((ViewHolder) holder).Advtype.setEnabled (false);
                ((ViewHolder) holder).AllowanceType.setFocusable (false);
                ((ViewHolder) holder).AllowanceType.setFocusableInTouchMode (false);
                ((ViewHolder) holder).AllowanceType.setClickable (true);
                ((ViewHolder) holder).AllowanceType.setEnabled (false);
                ((ViewHolder) holder).Fare.setFocusable (false);
                ((ViewHolder) holder).Fare.setFocusableInTouchMode (false);
                ((ViewHolder) holder).Currency.setFocusable (false);
                ((ViewHolder) holder).Currency.setFocusableInTouchMode (false);
                ((ViewHolder) holder).Currency.setEnabled (false);

                if (editstatus.equalsIgnoreCase ("1")) {
                    ((ViewHolder) holder).editrowbtn.setVisibility (View.VISIBLE);

                    ((ViewHolder) holder ).customername.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText (getApplicationContext, "Kindly Click on Edit to Change this Data", Toast.LENGTH_SHORT).show ( );
                        }
                    });
                    ((ViewHolder) holder ).Advtype.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText (getApplicationContext, "Kindly Click on Edit to Change this Data", Toast.LENGTH_SHORT).show ( );
                        }
                    });
                    ((ViewHolder) holder ).AllowanceType.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText (getApplicationContext, "Kindly Click on Edit to Change this Data", Toast.LENGTH_SHORT).show ( );
                        }
                    });
                    ((ViewHolder) holder ).Fare.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText (getApplicationContext, "Kindly Click on Edit to Change this Data", Toast.LENGTH_SHORT).show ( );
                        }
                    });
                    ((ViewHolder) holder ).Currency.setOnClickListener(new View.OnClickListener() {
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

                            ((ViewHolder) holder).customername.setFocusable (true);
                            ((ViewHolder) holder).customername.setFocusableInTouchMode (true);
                            ((ViewHolder) holder).Advtype.setFocusable (false);
                            ((ViewHolder) holder).Advtype.setFocusableInTouchMode (false);
                            ((ViewHolder) holder).Advtype.setEnabled (true);
                            ((ViewHolder) holder).AllowanceType.setFocusable (false);
                            ((ViewHolder) holder).AllowanceType.setFocusableInTouchMode (false);
                            ((ViewHolder) holder).AllowanceType.setEnabled (true);
                            ((ViewHolder) holder).Fare.setFocusable (true);
                            ((ViewHolder) holder).Fare.setFocusableInTouchMode (true);
                            ((ViewHolder) holder).Currency.setFocusable (false);
                            ((ViewHolder) holder).Currency.setFocusableInTouchMode (false);
                            ((ViewHolder) holder).Currency.setEnabled (true);
                        }
                    });

                    ((ViewHolder) holder).customername.addTextChangedListener(new TextWatcher ()
                    {
                        @Override
                        public void afterTextChanged(Editable mEdit)
                        {
                            edited_customername = mEdit.toString();
                            Log.e ("edited_customername",edited_customername);
                            allowanceModelArrayList.get(position).setCustomername (edited_customername);
                        }
                        public void beforeTextChanged(CharSequence s, int start, int count, int after){

                        }
                        public void onTextChanged(CharSequence s, int start, int before, int count){

                        }
                    });
                    ((ViewHolder) holder).Fare.addTextChangedListener(new TextWatcher ()
                    {
                        @Override
                        public void afterTextChanged(Editable mEdit)
                        {
                            edited_fare = mEdit.toString();
                            Log.e ("edited_fare",edited_fare);
                            allowanceModelArrayList.get(position).setFare (edited_fare);
                        }
                        public void beforeTextChanged(CharSequence s, int start, int count, int after){

                        }
                        public void onTextChanged(CharSequence s, int start, int before, int count){

                        }
                    });

                    ((ViewHolder) holder).Currency.setOnClickListener (new View.OnClickListener ( ) {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent (AllowanceActivity.this, CurrencyList_Spinner.class);
                            intent.putExtra ("ScreenStatus", "AA");
                            intent.putExtra ("position1", ""+position);
                            startActivityForResult(intent, 2);
                            positionclicked = String.valueOf (position);
                        }
                    });

                    if (Currname.equalsIgnoreCase ("")) {
                        Log.e ("ITEMNAMEEMPTY","" + Currname);
                    } else {
                        Log.e ("position1","position1"+position1);
                        Log.e ("positionclicked","positionclicked"+positionclicked);
                        if (position1.equalsIgnoreCase (positionclicked)) {
                            ((ViewHolder) holder).Currency.setText (Currname);
                            Log.e ("ChangedVlues","Currname"+Currname);
                            allowanceModelArrayList.get(position).setCurrency (Currname);
                        } else {
                            Log.e ("Position","Not Match");
                        }
                    }

                    ((ViewHolder) holder).Advtype.setOnClickListener (new View.OnClickListener ( ) {
                        @Override
                        public void onClick(View view) {
                            // custom dialog
                            final Dialog dialog = new Dialog(AllowanceActivity.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar);
                            dialog.setContentView(R.layout.edit_advance_type_dialog);

                            RelativeLayout close_layout = dialog.findViewById(R.id.close_layout);
                            TextView regular_adv = dialog.findViewById(R.id.regular_adv);
                            TextView travel_adv = dialog.findViewById(R.id.travel_adv);
                            TextView header_TV = dialog.findViewById(R.id.header_TV);

                            header_TV.setText("Advance Type");

                            regular_adv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ((ViewHolder) holder).Advtype.setText ("Regular Advance");
                                    allowanceModelArrayList.get (position).setAdvtype ("Regular Advance");
                                    dialog.dismiss();
                                }
                            });
                            travel_adv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ((ViewHolder) holder).Advtype.setText ("Travel Advance");
                                    allowanceModelArrayList.get (position).setAdvtype ("Travel Advance");
                                    dialog.dismiss();
                                }
                            });
                            close_layout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                    });

                    ((ViewHolder) holder).AllowanceType.setOnClickListener (new View.OnClickListener ( ) {
                        @Override
                        public void onClick(View view) {

                            if (allowanceModelArrayList.get(position).getAdvtype().equalsIgnoreCase("Travel Advance") || ((ViewHolder) holder).Advtype.getText().toString().trim().equalsIgnoreCase("Travel Advance")) {
                                // custom dialog
                                final Dialog dialog = new Dialog(AllowanceActivity.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar);
                                dialog.setContentView(R.layout.edit_allowance_type_dialog);

                                RelativeLayout close_layout = dialog.findViewById(R.id.close_layout);
                                TextView Flight = dialog.findViewById(R.id.Flight);
                                TextView Train = dialog.findViewById(R.id.Train);
                                TextView Bus = dialog.findViewById(R.id.Bus);
                                TextView Auto = dialog.findViewById(R.id.Auto);
                                TextView Cab = dialog.findViewById(R.id.Cab);
                                TextView header_TV = dialog.findViewById(R.id.header_TV);

                                header_TV.setText("Allowance Type");

                                Flight.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ((ViewHolder) holder).AllowanceType.setText("Flight");
                                        allowanceModelArrayList.get(position).setAllowanceType("Flight");
                                        dialog.dismiss();
                                    }
                                });
                                Train.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ((ViewHolder) holder).AllowanceType.setText("Train");
                                        allowanceModelArrayList.get(position).setAllowanceType("Train");
                                        dialog.dismiss();
                                    }
                                });
                                Bus.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ((ViewHolder) holder).AllowanceType.setText("Bus");
                                        allowanceModelArrayList.get(position).setAllowanceType("Bus");
                                        dialog.dismiss();
                                    }
                                });
                                Auto.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ((ViewHolder) holder).AllowanceType.setText("Auto");
                                        allowanceModelArrayList.get(position).setAllowanceType("Auto");
                                        dialog.dismiss();
                                    }
                                });
                                Cab.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ((ViewHolder) holder).AllowanceType.setText("Cab");
                                        allowanceModelArrayList.get(position).setAllowanceType("Cab");
                                        dialog.dismiss();
                                    }
                                });
                                close_layout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                            } else if (allowanceModelArrayList.get(position).getAdvtype().equalsIgnoreCase("Regular Advance")  || ((ViewHolder) holder).Advtype.getText().toString().trim().equalsIgnoreCase("Regular Advance")) {

                                // custom dialog
                                final Dialog dialog = new Dialog(AllowanceActivity.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar);
                                dialog.setContentView(R.layout.edit_allowance_type_dialog);

                                RelativeLayout close_layout = dialog.findViewById(R.id.close_layout);
                                TextView Flight = dialog.findViewById(R.id.Flight);
                                TextView Train = dialog.findViewById(R.id.Train);
                                TextView Bus = dialog.findViewById(R.id.Bus);
                                TextView Auto = dialog.findViewById(R.id.Auto);
                                TextView Cab = dialog.findViewById(R.id.Cab);
                                TextView header_TV = dialog.findViewById(R.id.header_TV);

                                header_TV.setText("Allowance Type");
                                Flight.setText("Beverages");
                                Train.setText("Food");
                                Bus.setText("Complementory");
                                Auto.setText("Stationary");

                                Flight.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ((ViewHolder) holder).AllowanceType.setText("Beverages");
                                        allowanceModelArrayList.get(position).setAllowanceType("Beverages");
                                        dialog.dismiss();
                                    }
                                });
                                Train.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ((ViewHolder) holder).AllowanceType.setText("Food");
                                        allowanceModelArrayList.get(position).setAllowanceType("Food");
                                        dialog.dismiss();
                                    }
                                });
                                Bus.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ((ViewHolder) holder).AllowanceType.setText("Complementory");
                                        allowanceModelArrayList.get(position).setAllowanceType("Complementory");
                                        dialog.dismiss();
                                    }
                                });
                                Auto.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ((ViewHolder) holder).AllowanceType.setText("Stationary");
                                        allowanceModelArrayList.get(position).setAllowanceType("Stationary");
                                        dialog.dismiss();
                                    }
                                });
                                Cab.setVisibility(View.GONE);
                                Cab.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ((ViewHolder) holder).AllowanceType.setText("Cab");
                                        allowanceModelArrayList.get(position).setAllowanceType("Cab");
                                        dialog.dismiss();
                                    }
                                });
                                close_layout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                            }
                        }
                    });

                } else {
                    ((ViewHolder) holder).editrowbtn.setVisibility (View.GONE);
                }
            }
        }
        @Override
        public int getItemCount()
        {
            return allowanceModelArrayList.size();
        }
        public  class ViewHolder extends RecyclerView.ViewHolder {

            EditText customername,Fare,Currency,Advtype,AllowanceType;
            public TextView canceltv,sno,Attachment,ApprovedBy,approve_btn,reject_btn;
            ImageButton deletebtn;
            ImageView editrowbtn;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                editrowbtn = itemView.findViewById(R.id.editrowbtn);
                customername = itemView.findViewById(R.id.customername);
                Advtype = itemView.findViewById(R.id.Advtype);
                AllowanceType = itemView.findViewById(R.id.AllowanceType);
                Fare = itemView.findViewById(R.id.Fare);
                canceltv = itemView.findViewById(R.id.canceltv);
                deletebtn = itemView.findViewById(R.id.deletebtn);
                Attachment = itemView.findViewById(R.id.Attachment);
                ApprovedBy = itemView.findViewById(R.id.ApprovedBy);
                approve_btn = itemView.findViewById(R.id.approve_btn);
                reject_btn = itemView.findViewById(R.id.reject_btn);
                Currency = itemView.findViewById(R.id.Currency);

            }
        }
    }

    private class AddAllowance extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AllowanceActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_AddAllowance";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_AddAllowance";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_AddAllowance";
                Log.e("URL", URL);

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                if (DocNo.equalsIgnoreCase ("") || !referenceno_et.getText().toString().trim().equalsIgnoreCase("")) {
                    Request.addProperty ("DocNo", "" + 0);
                    Log.e ("DocNo", "" + 0);
                } else if (btnclickstatus.equalsIgnoreCase ("saveas")) {
                    Request.addProperty ("DocNo", "" + 0);
                    Log.e ("DocNo", "" + 0);
                } else {
                    Request.addProperty ("DocNo", "" + DocNo);
                    Log.e ("DocNo", "" + DocNo);
                }

                Request.addProperty("User", "" +sessionempid);
                Log.e("User", ""+sessionempid);

                Request.addProperty("Remarks", "" +remarks_et.getText().toString().trim());
                Log.e("Remarks", ""+remarks_et.getText().toString().trim());

                if (referenceno_et.getText().toString().equalsIgnoreCase("")) {
                    ReferenceNo = "0";
                } else {
                    ReferenceNo = referenceno_et.getText().toString().trim();
                }
                Request.addProperty("ReferenceNo", "" +ReferenceNo);
                Log.e("ReferenceNo", ""+ReferenceNo);

                Request.addProperty("Attachment1", "" +attach_Image);
                Log.e("Attachment1", ""+attach_Image);

                Request.addProperty("Attachment2", "" +attach_Image1);
                Log.e("Attachment2", ""+attach_Image1);

                Request.addProperty("AllowancDt", "" +allowancedate_et.getText().toString().trim());
                Log.e("AllowancDt", ""+allowancedate_et.getText().toString().trim());

                String bookingitem="";
                for (int io = 0; io < allowanceModelArrayList.size(); io++) {

                    String xmlcustomername="",xmlAdvtype="",xmlAllowanceType="",xmlCurrency="",xmlAttachment="",xmlApprovedBy="",xmlApprovedStatus="";

                    String xmlFare="",xmlrowid="";

                    if (btnclickstatus.equalsIgnoreCase ("saveas")) {
                        xmlrowid = "0";
                    } else {
                        xmlrowid = allowanceModelArrayList.get (io).getId ();
                    }

                    xmlcustomername = allowanceModelArrayList.get(io).getCustomer ();
                    xmlAdvtype = allowanceModelArrayList.get(io).getAdvtype();
                    xmlAllowanceType = allowanceModelArrayList.get(io).getAllowanceType();
                    xmlFare = allowanceModelArrayList.get(io).getFare();
                    xmlCurrency = allowanceModelArrayList.get(io).getCurrency();
                    xmlAttachment = allowanceModelArrayList.get(io).getAttachment();
                    xmlApprovedBy = allowanceModelArrayList.get(io).getApprovedBy();
                    xmlApprovedStatus = allowanceModelArrayList.get(io).getApprovedStatus();

                    int Empid= Integer.parseInt(sessionempid);
                    String date = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
                    bookingitem = bookingitem + "<Table1><Id>"+xmlrowid+"</Id><Customer>"+ xmlcustomername +"</Customer><AdvType>" + xmlAdvtype + "</AdvType><AllowanceType>" + xmlAllowanceType + "</AllowanceType><Fare>" + xmlFare + "</Fare><Currency>" + xmlCurrency + "</Currency><AppByCode>" + 0 + "</AppByCode>" +
                            "<AppByName>" + "" + "</AppByName><AppStatus>" + xmlApprovedStatus + "</AppStatus><SaleEmpName>" + sessionempname + "</SaleEmpName>" +
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
                AlertDialog.Builder builder = new AlertDialog.Builder(AllowanceActivity.this);
                builder.setMessage(" Saved ")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                allowanceModelArrayList.clear();
                                finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            } else if (launch.equalsIgnoreCase("2")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AllowanceActivity.this);
                builder.setMessage(" Updated ")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                allowanceModelArrayList.clear();
                                startActivity (new Intent (AllowanceActivity.this, ViewAllowance.class));
                                finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(AllowanceActivity.this);
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

    private class AddAllowance_Approval extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AllowanceActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_AddAllowance_Approval";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_AddAllowance_Approval";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_AddAllowance_Approval";
                Log.e("URL", URL);

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);


                Request.addProperty ("DocNo", "" + DocNo);
                Log.e ("DocNo", "" + DocNo);

                Request.addProperty("User", "" +sessionempid);
                Log.e("User", ""+sessionempid);

                if (Approvestatus.equalsIgnoreCase("A")) {
                    Request.addProperty("AppStatus", ""+Approvestatus);
                    Log.e("AppStatus", ""+Approvestatus);
                }else {
                    Request.addProperty("AppStatus", ""+Approvestatus);
                    Log.e("AppStatus", ""+Approvestatus);
                }

                Request.addProperty("AppRemarks", ""+ApproveRemarks);
                Log.e("AppRemarks", ""+ApproveRemarks);

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
                if(Approvestatus.equalsIgnoreCase("A")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AllowanceActivity.this);
                    builder.setMessage(" Approved ")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    startActivity(new Intent(AllowanceActivity.this,ViewAllowanceApproval.class));
                                    allowanceModelArrayList.clear();
                                    finish();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AllowanceActivity.this);
                    builder.setMessage(" Rejected ")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    startActivity(new Intent(AllowanceActivity.this,ViewAllowanceApproval.class));
                                    allowanceModelArrayList.clear();
                                    finish();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }  else {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(AllowanceActivity.this);
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






