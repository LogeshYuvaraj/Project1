package com.example.ravelelectronics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

public class ApprovalScreen extends AppCompatActivity {
Spinner employeespin,approvalscrnspin,approvesetspin,approvalviewspin;
RecyclerView  approvalRecycle,approvset_rv,approveview_rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_screen);

        employeespin=findViewById(R.id.employeespin);
        approvalscrnspin=findViewById(R.id.approvalscrnspin);
        approvesetspin=findViewById(R.id.approvesetspin);
        approvalviewspin=findViewById(R.id.approvalviewspin);
        approvalRecycle=findViewById(R.id.approvalRecycle);
        approvset_rv=findViewById(R.id.approvset_rv);
        approveview_rv=findViewById(R.id.approveview_rv);

    }
}
