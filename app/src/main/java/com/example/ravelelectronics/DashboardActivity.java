package com.example.ravelelectronics;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ravelelectronics.model.TargetStatusReportModel;
import com.example.ravelelectronics.model.ViewActivityModel;
import com.example.ravelelectronics.util.SessionManagement;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    Toolbar toolbar;
    CardView home_cv, salesquotation_cv, salesordr_cv, presales_cv, projection_cv, master_cv, opportunity_cv, reports_cv,ViewPresales_cv,
            myactivity_cv,viewfollowupdate_cv, settings_cv,allowance_cv,attendance_cv,salestarget_cv,customerprofile_cv,chngepswd_cv,logout_cv,
            viewattendance_cv,ViewActivity_cv,viewQuotation_cv,ViewTarget_cv,ViewAllowance_cv,ViewProjection_cv,
            viewallowanceapproval_cv,viewquotationapprove_cv;
    SessionManagement session;
    String Empname="",Designation="",Email="",Mobileno="";
    String sessionempname="",sessionempemail="",sessionempmobile="",sessionempjobtitle="",sessionempmanager="",sessionempCRMAdmin="";
    TextView empname_TV,designation_TV,email_TV,mobileno_TV,count_tv;
    JSONArray jsonArray2;
    ProgressDialog pDialog;
    RecyclerView.Adapter Adapter;
    String sessionempid="";
    List<String> CountModel1 = new ArrayList<>();
    ArrayList<CountModel> countModelArrayList;
    ArrayList<TargetStatusReportModel> targetStatusReportModelArrayList;
    String followupcnt="";

    PieChart pieChart;

    String Target_Amount = "", Target_Acheived = "";

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
//        getEntries();

        pieChart = (PieChart) findViewById(R.id.pieChart);
        pieChart.setUsePercentValues(true);

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.

        //---------------Pie chart--------------

        home_cv = findViewById(R.id.home_cv);
        count_tv = findViewById(R.id.count_tv);
        salesquotation_cv = findViewById(R.id.salesquotation_cv);
        viewfollowupdate_cv = findViewById(R.id.viewfollowupdate_cv);
        salesordr_cv = findViewById(R.id.salesorde_cv);
        presales_cv = findViewById(R.id.presales_cv);
        projection_cv = findViewById(R.id.projection_cv);
        viewattendance_cv = findViewById(R.id.viewattendance_cv);
        ViewPresales_cv = findViewById(R.id.ViewPresales_cv);
        ViewActivity_cv = findViewById(R.id.ViewActivity_cv);
        viewQuotation_cv = findViewById(R.id.viewQuotation_cv);
        ViewTarget_cv = findViewById(R.id.ViewTarget_cv);
        ViewAllowance_cv = findViewById(R.id.ViewAllowance_cv);
        ViewProjection_cv = findViewById(R.id.ViewProjection_cv);
        master_cv = findViewById(R.id.masters_cv);
        allowance_cv = findViewById(R.id.allowance_cv);
        attendance_cv = findViewById(R.id.attendance_cv);
        myactivity_cv = findViewById(R.id.myactivity_cv);
        salestarget_cv = findViewById(R.id.salestarget_cv);
        customerprofile_cv = findViewById(R.id.customerprofile_cv);
        chngepswd_cv = findViewById(R.id.chngepswd_cv);
        logout_cv = findViewById(R.id.logout_cv);
        viewallowanceapproval_cv = findViewById(R.id.viewallowanceapproval_cv);
        viewquotationapprove_cv = findViewById(R.id.viewquotationapprove_cv);

        empname_TV = findViewById(R.id.empname_TV);
        designation_TV = findViewById(R.id.designation_TV);
        email_TV = findViewById(R.id.email_TV);
        mobileno_TV = findViewById(R.id.mobileno_TV);
        countModelArrayList = new ArrayList<>();
        targetStatusReportModelArrayList = new ArrayList<>();
        session=new SessionManagement(this);

        session = new SessionManagement(DashboardActivity.this);
        HashMap<String, String> users = session.getUserDetails();
        sessionempid = users.get(SessionManagement.KEY_CODE);
        sessionempname = users.get(SessionManagement.KEY_NAME);
        sessionempemail = users.get(SessionManagement.KEY_EMAIL);
        sessionempmobile = users.get(SessionManagement.KEY_MOBILE);
        sessionempjobtitle = users.get(SessionManagement.KEY_JOBTITLE);
        sessionempmanager = users.get(SessionManagement.KEY_MANAGER);
        sessionempCRMAdmin = users.get(SessionManagement.KEY_CRMAdmin);
        empname_TV.setText(sessionempname);
        email_TV.setText(sessionempemail);
        mobileno_TV.setText(sessionempmobile);
        designation_TV.setText(sessionempjobtitle);

        Log.e("sessionempmanager",sessionempmanager);
        if (sessionempmanager.equalsIgnoreCase("Y")) {
            viewallowanceapproval_cv.setVisibility(View.VISIBLE);
            viewquotationapprove_cv.setVisibility(View.VISIBLE);
        } else {
            viewallowanceapproval_cv.setVisibility(View.GONE);
            viewquotationapprove_cv.setVisibility(View.GONE);
        }
        if (sessionempCRMAdmin.equalsIgnoreCase("Y")) {
            salestarget_cv.setVisibility(View.VISIBLE);
        } else {
            salestarget_cv.setVisibility(View.GONE);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small> Dashboard </small>"));
        }

        new GETTargetStatus_Report().execute();

        home_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, HomeActivity.class);
                startActivity(intent);

            }
        });
        allowance_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(allowance_cv.getCardBackgroundColor().getDefaultColor()==-1){

                    allowance_cv.setCardBackgroundColor(Color.parseColor("#B6C55B"));
                    viewfollowupdate_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesquotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesordr_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    presales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    projection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewattendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewPresales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewActivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewQuotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewTarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewAllowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    master_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    attendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    myactivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salestarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    customerprofile_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    logout_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewProjection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewallowanceapproval_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewquotationapprove_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                }

                Intent intent = new Intent(DashboardActivity.this, AllowanceActivity.class);
                intent.putExtra ("DocNo","0");
                intent.putExtra ("editstatus","0");
                startActivity(intent);
            }
        });

        viewallowanceapproval_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(viewallowanceapproval_cv.getCardBackgroundColor().getDefaultColor()==-1){

                    viewallowanceapproval_cv.setCardBackgroundColor(Color.parseColor("#B6C55B"));
                    viewquotationapprove_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    allowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewfollowupdate_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesquotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesordr_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    presales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    projection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewattendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewPresales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewActivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewQuotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewTarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewAllowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    master_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    attendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    myactivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salestarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    customerprofile_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    logout_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewProjection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                }

                Intent intent = new Intent(DashboardActivity.this, ViewAllowanceApproval.class);
                startActivity(intent);
            }
        });

        viewquotationapprove_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(viewquotationapprove_cv.getCardBackgroundColor().getDefaultColor()==-1){

                    viewquotationapprove_cv.setCardBackgroundColor(Color.parseColor("#B6C55B"));
                    viewallowanceapproval_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    allowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewfollowupdate_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesquotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesordr_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    presales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    projection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewattendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewPresales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewActivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewQuotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewTarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewAllowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    master_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    attendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    myactivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salestarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    customerprofile_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    logout_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewProjection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                }

                Intent intent = new Intent(DashboardActivity.this, ViewQuotationApproval.class);
                startActivity(intent);
            }
        });

        viewfollowupdate_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(viewfollowupdate_cv.getCardBackgroundColor().getDefaultColor()==-1){

                    viewquotationapprove_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewallowanceapproval_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewfollowupdate_cv.setCardBackgroundColor(Color.parseColor("#B6C55B"));
                    allowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesquotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesordr_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    presales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    projection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewattendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewPresales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewActivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewQuotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewTarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewAllowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    master_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    attendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    myactivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salestarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    customerprofile_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    logout_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewProjection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                }

                Intent intent = new Intent(DashboardActivity.this, ViewFollowUp.class);
                intent.putExtra ("DocNo","0");
                intent.putExtra ("editstatus","0");
                startActivity(intent);
            }
        });
        logout_cv.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {


                if(logout_cv.getCardBackgroundColor().getDefaultColor()==-1){

                    logout_cv.setCardBackgroundColor(Color.parseColor("#B6C55B"));
                    viewfollowupdate_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewallowanceapproval_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewquotationapprove_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesquotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesordr_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    presales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    projection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewattendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewPresales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewActivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewQuotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewTarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewAllowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    master_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    allowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    attendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    myactivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salestarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    customerprofile_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    logout_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    logout_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    chngepswd_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewProjection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                }

                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                session.logoutUser();

            }
        });
        attendance_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(attendance_cv.getCardBackgroundColor().getDefaultColor()==-1){

                    attendance_cv.setCardBackgroundColor(Color.parseColor("#B6C55B"));
                    viewallowanceapproval_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewquotationapprove_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewfollowupdate_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesquotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesordr_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    presales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    projection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewattendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewPresales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewActivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewQuotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewTarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewAllowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    master_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    allowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    myactivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salestarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    customerprofile_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    logout_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    logout_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    chngepswd_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewProjection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                }
                Intent intent = new Intent(DashboardActivity.this, AttendanceActivity.class);
                startActivity(intent);
            }
        });
        salestarget_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(salestarget_cv.getCardBackgroundColor().getDefaultColor()==-1){

                    salestarget_cv.setCardBackgroundColor(Color.parseColor("#B6C55B"));
                    viewfollowupdate_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewallowanceapproval_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewquotationapprove_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesquotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesordr_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    presales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    projection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewattendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewPresales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewActivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewQuotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewTarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewAllowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    master_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    allowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    attendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    myactivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    customerprofile_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    logout_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    logout_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    chngepswd_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewProjection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                }
                Intent intent = new Intent(DashboardActivity.this, SalesmanTarget.class);
                intent.putExtra ("DocNo","0");
                intent.putExtra ("editstatus","0");
                startActivity(intent);
            }
        });
        customerprofile_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(customerprofile_cv.getCardBackgroundColor().getDefaultColor()==-1){

                    customerprofile_cv.setCardBackgroundColor(Color.parseColor("#B6C55B"));
                    viewfollowupdate_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewallowanceapproval_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewquotationapprove_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesquotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesordr_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    presales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    projection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewattendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewPresales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewActivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewQuotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewTarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewAllowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    master_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    allowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    attendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    myactivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salestarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    logout_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    logout_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    chngepswd_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewProjection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                }
                Intent intent = new Intent(DashboardActivity.this, CustomerProfile.class);
                startActivity(intent);

            }
        });
        chngepswd_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(chngepswd_cv.getCardBackgroundColor().getDefaultColor()==-1){

                    chngepswd_cv.setCardBackgroundColor(Color.parseColor("#B6C55B"));
                    viewfollowupdate_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewallowanceapproval_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewquotationapprove_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesquotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesordr_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    presales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    projection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewattendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewPresales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewActivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewQuotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewTarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewAllowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    master_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    allowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    attendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    myactivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salestarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    customerprofile_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    logout_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewProjection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                }
                Intent intent = new Intent(DashboardActivity.this, ChangePassword.class);
                startActivity(intent);
            }
        });
        master_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(master_cv.getCardBackgroundColor().getDefaultColor()==-1){

                    master_cv.setCardBackgroundColor(Color.parseColor("#B6C55B"));
                    viewfollowupdate_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewallowanceapproval_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewquotationapprove_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesquotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesordr_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    presales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    projection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewattendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewPresales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewActivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewQuotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewTarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewAllowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    allowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    attendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    myactivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salestarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    customerprofile_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    logout_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    chngepswd_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewProjection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));

                }
                Intent intent = new Intent(DashboardActivity.this, MasterActivity.class);
                startActivity(intent);
            }
        });
        salesordr_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        if(salesordr_cv.getCardBackgroundColor().getDefaultColor()==-1){

                    salesordr_cv.setCardBackgroundColor(Color.parseColor("#B6C55B"));
                            viewallowanceapproval_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                            viewquotationapprove_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                            viewfollowupdate_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                            salesquotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                            presales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                            projection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                            viewattendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                            ViewPresales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                            ViewActivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                            viewQuotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                            ViewTarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                            ViewAllowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                            master_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                            allowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                            attendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                            myactivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                            salestarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                            customerprofile_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                            logout_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                            chngepswd_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                            ViewProjection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                }
                Intent intent = new Intent(DashboardActivity.this, SalesOrderActivity.class);
                startActivity(intent);

            }
        });
        presales_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(presales_cv.getCardBackgroundColor().getDefaultColor()==-1){

                    presales_cv.setCardBackgroundColor(Color.parseColor("#B6C55B"));
                    viewallowanceapproval_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewquotationapprove_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewfollowupdate_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesquotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesordr_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    projection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewattendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewPresales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewActivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewQuotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewTarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewAllowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    master_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    allowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    attendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    myactivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salestarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    customerprofile_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    logout_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    chngepswd_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewProjection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                }
                Intent intent = new Intent(DashboardActivity.this, PresalesActivity.class);
                intent.putExtra ("DocNo","0");
                intent.putExtra ("editstatus","0");
                startActivity(intent);

            }
        });
        projection_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(projection_cv.getCardBackgroundColor().getDefaultColor()==-1){

                    projection_cv.setCardBackgroundColor(Color.parseColor("#B6C55B"));
                    viewallowanceapproval_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewquotationapprove_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewfollowupdate_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesquotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesordr_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    presales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewattendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewPresales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewActivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewQuotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewTarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewAllowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    master_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    allowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    attendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    myactivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salestarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    customerprofile_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    logout_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    chngepswd_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewProjection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                }
                Intent intent = new Intent(DashboardActivity.this, Projectonscr.class);
                intent.putExtra ("DocNo","0");
                intent.putExtra ("editstatus","0");
                startActivity(intent);
            }
        });
        myactivity_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(myactivity_cv.getCardBackgroundColor().getDefaultColor()==-1){

                    myactivity_cv.setCardBackgroundColor(Color.parseColor("#B6C55B"));
                    viewallowanceapproval_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewquotationapprove_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewfollowupdate_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesquotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesordr_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    presales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    projection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewattendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewPresales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewActivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewQuotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewTarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewAllowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    master_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    allowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    attendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salestarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    customerprofile_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    logout_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    chngepswd_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewProjection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                }
                Intent intent = new Intent(DashboardActivity.this, ActivityScreen.class);
                intent.putExtra ("DocNo","0");
                intent.putExtra ("editstatus","0");
                startActivity(intent);

            }
        });

        salesquotation_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(salesquotation_cv.getCardBackgroundColor().getDefaultColor()==-1){

                    salesquotation_cv.setCardBackgroundColor(Color.parseColor("#B6C55B"));
                    viewallowanceapproval_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewquotationapprove_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewfollowupdate_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesordr_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    presales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    projection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewattendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewPresales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewActivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewQuotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewTarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewAllowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    master_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    allowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    attendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    myactivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salestarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    customerprofile_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    logout_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    chngepswd_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewProjection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                }
                Intent intent = new Intent(DashboardActivity.this, SalesQuotation.class);
                intent.putExtra ("DocNo","0");
                intent.putExtra ("editstatus","0");
                startActivity(intent);
            }
        });

         viewattendance_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(viewattendance_cv.getCardBackgroundColor().getDefaultColor()==-1){

                    viewattendance_cv.setCardBackgroundColor(Color.parseColor("#B6C55B"));
                    viewallowanceapproval_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewquotationapprove_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewfollowupdate_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesquotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesordr_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    presales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    projection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewPresales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewActivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewQuotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewTarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewAllowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    master_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    allowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    attendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    myactivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salestarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    customerprofile_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    logout_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    chngepswd_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewProjection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                }
                Intent intent = new Intent(DashboardActivity.this, ViewAttendance.class);
                startActivity(intent);
            }
        });

        ViewActivity_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ViewActivity_cv.getCardBackgroundColor().getDefaultColor()==-1){

                    ViewActivity_cv.setCardBackgroundColor(Color.parseColor("#B6C55B"));
                    viewallowanceapproval_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewquotationapprove_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewfollowupdate_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesquotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesordr_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    presales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    projection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewattendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewPresales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewQuotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewTarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewAllowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    master_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    allowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    attendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    myactivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salestarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    customerprofile_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    logout_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    chngepswd_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewProjection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                }

                Intent intent = new Intent(DashboardActivity.this, ViewActivity.class);
                startActivity(intent);
            }

        });
        viewQuotation_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(viewQuotation_cv.getCardBackgroundColor().getDefaultColor()==-1){

                    viewQuotation_cv.setCardBackgroundColor(Color.parseColor("#B6C55B"));
                    viewallowanceapproval_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewquotationapprove_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewfollowupdate_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesquotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesordr_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    presales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    projection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewattendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewPresales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewActivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewTarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewAllowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    master_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    allowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    attendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    myactivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salestarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    customerprofile_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    logout_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    chngepswd_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewProjection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                }
                Intent intent = new Intent(DashboardActivity.this, ViewQuotation.class);
                startActivity(intent);
            }
        });
        ViewTarget_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ViewTarget_cv.getCardBackgroundColor().getDefaultColor()==-1){

                    ViewTarget_cv.setCardBackgroundColor(Color.parseColor("#B6C55B"));
                    viewallowanceapproval_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewquotationapprove_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewfollowupdate_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesquotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesordr_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    presales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    projection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewattendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewPresales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewActivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewQuotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewAllowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    master_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    allowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    attendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    myactivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salestarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    customerprofile_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    logout_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    chngepswd_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewProjection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                }
                Intent intent = new Intent(DashboardActivity.this, ViewTarget.class);
                startActivity(intent);
            }
        });
        ViewAllowance_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ViewAllowance_cv.getCardBackgroundColor().getDefaultColor()==-1){

                    ViewAllowance_cv.setCardBackgroundColor(Color.parseColor("#B6C55B"));
                    viewallowanceapproval_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewquotationapprove_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewfollowupdate_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesquotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesordr_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    presales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    projection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewattendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewPresales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewActivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewQuotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewTarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    master_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    allowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    attendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    myactivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salestarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    customerprofile_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    logout_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    chngepswd_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewProjection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));

                }
                Intent intent = new Intent(DashboardActivity.this, ViewAllowance.class);
                startActivity(intent);
            }
        });
        ViewProjection_cv .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ViewProjection_cv.getCardBackgroundColor().getDefaultColor()==-1){

                    ViewProjection_cv.setCardBackgroundColor(Color.parseColor("#B6C55B"));
                    viewallowanceapproval_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewquotationapprove_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewfollowupdate_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesquotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesordr_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    presales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    projection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewattendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewPresales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewActivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewQuotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewTarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewAllowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    master_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    allowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    attendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    myactivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salestarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    customerprofile_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    logout_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    chngepswd_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                }
                Intent intent = new Intent(DashboardActivity.this, ViewProjection.class);
                startActivity(intent);
            }
        });


        ViewPresales_cv .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(ViewPresales_cv.getCardBackgroundColor().getDefaultColor()==-1){

                    ViewPresales_cv.setCardBackgroundColor(Color.parseColor("#B6C55B"));
                    viewallowanceapproval_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewquotationapprove_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewfollowupdate_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesquotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salesordr_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    presales_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    projection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewattendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewProjection_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewActivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    viewQuotation_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewTarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    ViewAllowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    master_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    allowance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    attendance_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    myactivity_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    salestarget_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    customerprofile_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    logout_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    chngepswd_cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                }
                Intent intent = new Intent(DashboardActivity.this, ViewPresales.class);
                startActivity(intent);
            }
        });
    }

    private class GETFollowup_Report_Cnt extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(DashboardActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_GETFollowup_Report_Cnt";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_GETFollowup_Report_Cnt";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_GETFollowup_Report_Cnt";
                Log.e("URL", URL);

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("User", "" + sessionempid);
                Log.e("User", "" + sessionempid);
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

                jsonArray2 = new JSONArray(responseJSON);

                for (int l = 0; l < jsonArray2.length(); l++) {
                    JSONObject jsonObject = jsonArray2.getJSONObject(l);

                    followupcnt=jsonObject.getString("cnt");
                    CountModel model4 = new CountModel();
                    model4.setCnt(jsonObject.getString("cnt"));
                    countModelArrayList.add(model4);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();

//            new GETTargetStatus_Report ().execute ();

            if (jsonArray2.length() == 0) {

            } else {
                count_tv.setText(followupcnt);
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

    private class GETTargetStatus_Report extends AsyncTask<Void, Void, Void> {

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(DashboardActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "IndusMobileSales_GETTargetStatus_Report";
                final String SOAP_ACTION = "http://tempuri.org/IndusMobileSales_GETTargetStatus_Report";
                final String URL = "http://103.48.182.209/ravelqc/Service.asmx?op=IndusMobileSales_GETTargetStatus_Report";
                Log.e("URL", URL);

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("User", "" + sessionempid);
                Log.e("User", "" + sessionempid);

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

                jsonArray2 = new JSONArray(responseJSON);

                for (int l = 0; l < jsonArray2.length(); l++) {
                    JSONObject jsonObject = jsonArray2.getJSONObject(l);

                    Target_Amount = jsonObject.getString("Target Amount");
                    Target_Acheived = jsonObject.getString("Target Acheived");

                    TargetStatusReportModel model4 = new TargetStatusReportModel();

                    model4.setTarget_No(jsonObject.getString("Target No"));
                    model4.setTarget_Date(jsonObject.getString("Target Date"));
                    model4.setTarget_Desc(jsonObject.getString("Target Desc"));
                    model4.setTarget_Amount(jsonObject.getString("Target Amount"));
                    model4.setSale_EmpCode(jsonObject.getString("Sale EmpCode"));
                    model4.setSalesEMpName(jsonObject.getString("SalesEMpName"));
                    model4.setTarget_Acheived(jsonObject.getString("Target Acheived"));
                    targetStatusReportModelArrayList.add(model4);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (jsonArray2.length() == 0) {
                Toast.makeText(DashboardActivity.this, "There is No Target Status Given", Toast.LENGTH_SHORT).show();
            } else {
                getEntries();
                new GETFollowup_Report_Cnt().execute();
            }
        }
    }

    private void getEntries() {

        float targetamt = Float.parseFloat(Target_Amount);
        float targetachivedamt = Float.parseFloat(Target_Acheived);

        ArrayList<Entry> yvalues = new ArrayList<Entry>();
        yvalues.add(new Entry(targetamt, 0));
        yvalues.add(new Entry(targetachivedamt, 1));

        PieDataSet dataSet = new PieDataSet(yvalues, "");

        //  PieDataSet dataSet1 = new PieDataSet(yvalues,"ddd");

        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add("Target Amount - " + Target_Amount);
        xVals.add("Target Acheived - " + Target_Acheived);

        PieData data = new PieData(xVals, dataSet);
        // In Percentage term
        data.setValueFormatter(new PercentFormatter());
        // Default value
        //data.setValueFormatter(new DefaultValueFormatter(0));
        pieChart.setData(data);
        pieChart.setDescription("");

        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(5f);
        pieChart.setHoleRadius(5f);

        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);
//        pieChart.setOnChartValueSelectedListener(this);

        pieChart.animateXY(1400, 1400);
    }
}
