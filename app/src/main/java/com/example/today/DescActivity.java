package com.example.today;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DescActivity extends AppCompatActivity {
    TextView TimeTv,TitleTv,DetailTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desc);
        TimeTv=findViewById(R.id.Desc_time);
        TitleTv=findViewById(R.id.Desc_title);
        DetailTv=findViewById(R.id.Desc_desc);
        final Intent intent = getIntent();
        String time1=intent.getStringExtra("time");
        String detail1=intent.getStringExtra("detail");
        String title1=intent.getStringExtra("title");
        TimeTv.setText(time1);
        TitleTv.setText(title1);
        DetailTv.setText(detail1);
    }
}
