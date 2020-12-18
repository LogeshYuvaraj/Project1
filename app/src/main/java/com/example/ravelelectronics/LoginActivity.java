package com.example.ravelelectronics;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ravelelectronics.util.SessionManagement;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtusername, edtpassword;
    ImageButton btnlogin;
    ProgressDialog pDialog;
    JSONArray jarraylist;
    SessionManagement session;
    String username = "", password = "";
    String sessionID = "";
    String sessionName = "";
    String sessionsalesPrson = "";
    String sessionmobile = "";
    String sessionemail = "";
    String sessionjobTitle = "";
    String sessionSlpName="",sessionmanager="",sessionCRMAdmin = "";

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        edtusername = (EditText) findViewById(R.id.edtusername);
        edtpassword = (EditText) findViewById(R.id.edtpassword);
        btnlogin=(ImageButton) findViewById(R.id.btnlogin);
        session=new SessionManagement(LoginActivity.this);
        btnlogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnlogin) {
            if (edtusername.getText().toString().equals("") || edtpassword.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Kindly enter your Username & Password!", Toast.LENGTH_SHORT).show();
            } else {
                new authendicate().execute();
            }
        }
    }

    private class authendicate extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
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
                final String METHOD_NAME = "IndusMobileSALES_EmpLogin";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSALES_EmpLogin";
                final String URL =  "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSALES_EmpLogin";
                Log.e("URL", URL);
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("UserID", "" + edtusername.getText().toString());
                Log.e("UserID", "" + edtusername.getText().toString());

                Request.addProperty("UserPassword", "" + edtpassword.getText().toString());
                Log.e("UserPassword", "" + edtpassword.getText().toString());

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                AndroidHttpTransport aht = new AndroidHttpTransport(URL);
                aht.call(SOAP_ACTION, soapEnvelope);
                SoapPrimitive resultString = (SoapPrimitive) soapEnvelope.getResponse();

                String responseJSON = resultString.toString();
                Log.e("RESPONSE", responseJSON);

                jarraylist = new JSONArray(responseJSON);
                for (int i = 0; i < jarraylist.length(); i++) {

                    JSONObject jsonObject = jarraylist.getJSONObject(i);
                    sessionID = jsonObject.getString("empID");
                    sessionName = jsonObject.getString("firstName");
                    sessionsalesPrson = jsonObject.getString("salesPrson");
                    sessionmobile = jsonObject.getString("mobile");
                    sessionemail = jsonObject.getString("email");
                    sessionjobTitle = jsonObject.getString("jobTitle");
                    sessionSlpName = jsonObject.getString("SlpName");
                    sessionmanager = jsonObject.getString("Manager");
                    sessionCRMAdmin = jsonObject.getString("CRMAdmin");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (jarraylist.length() == 0) {
                AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Login Error")
                        .setMessage("Username or Password is Wrong!!!")
                        .setNegativeButton(android.R.string.ok, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                Log.e("empID", sessionID);
                Log.e("firstName", sessionName);
                Log.e("salesPrson", sessionsalesPrson);
                Log.e("mobile", sessionmobile);
                Log.e("email", sessionemail);
                Log.e("jobTitle", sessionjobTitle);
                Log.e("SlpName", sessionSlpName);
                Log.e("manager", sessionmanager);
                Log.e("CRMAdmin", sessionCRMAdmin);

                session.createLoginSession(sessionID, sessionName, sessionsalesPrson, sessionmobile, sessionemail, sessionjobTitle,sessionSlpName,sessionmanager,sessionCRMAdmin);
                session.isLoggedIn();
                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                finish();
            }
        }
    }
}


