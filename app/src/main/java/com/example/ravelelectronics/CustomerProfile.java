package com.example.ravelelectronics;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.EditText;

public class CustomerProfile extends AppCompatActivity {
EditText edtorgname,edtorgtype,placeofsuply_et,edtcardcode,edtitemname,edstate,edcity,edtgstin_no,
        edtpin,edtcunty,edtcontno,edtdelivery,edtemail,edtpayterm,edtstatus,edtretaddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);
        edtorgname=(EditText)findViewById(R.id.edtorgname);
        edtorgtype=(EditText)findViewById(R.id.edtorgtype);
        edtcardcode=(EditText)findViewById(R.id.edtcardcode);
        edtitemname=(EditText)findViewById(R.id.edtitemname);
        edstate=(EditText)findViewById(R.id.edstate);
        edcity=(EditText)findViewById(R.id.edcity);
        edtpin=(EditText)findViewById(R.id.edtpin);
        edtcunty=(EditText)findViewById(R.id.edtcunty);
        placeofsuply_et=(EditText)findViewById(R.id.placeofsuply_et);
        edtcontno=(EditText)findViewById(R.id.edtcontno);
        edtdelivery=(EditText)findViewById(R.id.edtdelivery);
        edtemail=(EditText)findViewById(R.id.edtemail);
        edtpayterm=(EditText)findViewById(R.id.edtpayterm);
        edtstatus=(EditText)findViewById(R.id.edtstatus);
        edtretaddress=(EditText)findViewById(R.id.edtretaddress);
        edtgstin_no=(EditText)findViewById(R.id.edtgstin_no);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>Customer Profile </small>"));
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
