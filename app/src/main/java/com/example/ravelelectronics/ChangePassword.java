package com.example.ravelelectronics;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ravelelectronics.util.SessionManagement;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.HashMap;

public class ChangePassword extends AppCompatActivity implements View.OnClickListener {
    EditText oldpassword_ET,newpassword_ET,confirmpassword_ET;
    Button submit_BT;
    String sessionempid = "";
    SessionManagement session;
    ProgressDialog progressDialog;
    String  strfault="",launch="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        oldpassword_ET = (EditText) findViewById(R.id.oldpassword_ET);
        newpassword_ET = (EditText) findViewById(R.id.newpassword_ET);
        confirmpassword_ET = (EditText) findViewById(R.id.confirmpassword_ET);
        submit_BT = (Button) findViewById(R.id.submit_BT);
        submit_BT.setOnClickListener(this);
        session=new SessionManagement(this);
        HashMap<String, String> users = session.getUserDetails();
        sessionempid = users.get(SessionManagement.KEY_CODE);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small> Change Password </small>"));
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


    @Override
    public void onClick(View v) {
        if (v == submit_BT) {
            if (oldpassword_ET.getText().toString().trim().length() == 0) {
                oldpassword_ET.setError("Enter Old Password");
            } else if (newpassword_ET.getText().toString().trim().length() == 0) {
                newpassword_ET.setError("Enter New Password");
            } else if (confirmpassword_ET.getText().toString().trim().length() == 0) {
                confirmpassword_ET.setError("Enter Confirm Password");
            } else if (!newpassword_ET.getText().toString().trim().equals(confirmpassword_ET.getText().toString().trim())) {
                confirmpassword_ET.setError("New and  Confirm Password Mismatch");
            } else {

                new changepassword().execute();

            }
        }

    }
        private class changepassword extends AsyncTask<Void, Void, Void> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(ChangePassword.this);
                progressDialog.setMessage("Please Wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();

            }

            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_UpdateUserPassword";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_UpdateUserPassword";
                final String URL =  "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_UpdateUserPassword";
                Log.e("URL", URL);

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("EmployeeID", "" + sessionempid);
                Log.e("EmployeeID",sessionempid);

                Request.addProperty("OldPassword", "" + oldpassword_ET.getText().toString());
                Log.e("OldPassword",oldpassword_ET.getText().toString());

                Request.addProperty("NewPassword", "" + newpassword_ET.getText().toString());
                Log.e("NewPassword",newpassword_ET.getText().toString());

                Request.addProperty("ConfirmPassword", "" + confirmpassword_ET.getText().toString());
                Log.e("ConfirmPassword",confirmpassword_ET.getText().toString());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(Request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                try {
                    androidHttpTransport.call(SOAP_ACTION, envelope);
                    strfault = "";
                    if (envelope.bodyIn instanceof SoapFault) {
                        strfault = ((SoapFault) envelope.bodyIn).faultstring;
                        Log.i("Error", strfault);
                    } else {
                        SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                        Log.e("WS", String.valueOf(resultsRequestSOAP));
                    }
                    SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                    launch = ("" + resultsRequestSOAP.getProperty(0));
                }
                catch (Exception exg) {
                    exg.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                progressDialog.dismiss();
                if (launch.equalsIgnoreCase("3")) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ChangePassword.this);
                    builder.setMessage("Password changed successfully")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else if (launch.equalsIgnoreCase("1")) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ChangePassword.this);
                    builder.setMessage("Oldpassword is Wrong")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    finish();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassword.this);
                    builder.setTitle("Oops!");
                    builder.setMessage("something went wrong ")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }



            }

            }

        }
