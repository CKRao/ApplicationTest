package com.clark.rrimageview;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private BateImageview gado;
    private Button bt1, bt2, bt3, bt4, bt5, bt6, bt7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        gado = (BateImageview) findViewById(R.id.gado);
        bt1 = (Button) findViewById(R.id.bt1);
        bt2 = (Button) findViewById(R.id.bt2);
        bt3 = (Button) findViewById(R.id.bt3);
        bt4 = (Button) findViewById(R.id.bt4);
        bt5 = (Button) findViewById(R.id.bt5);
        bt6 = (Button) findViewById(R.id.bt6);
        bt7 = (Button) findViewById(R.id.bt7);

        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
        bt4.setOnClickListener(this);
        bt5.setOnClickListener(this);
        bt6.setOnClickListener(this);
        bt7.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt1:
                gado.setImageResource(R.drawable.g1);
                break;
            case R.id.bt2:
                gado.setImageResource(R.drawable.g2);
                break;
            case R.id.bt3:
                gado.setImageResource(R.drawable.g3);
                break;
            case R.id.bt4:
                gado.setImageResource(R.drawable.g5);
                break;
            case R.id.bt5:
                gado.setType(RRImageview.TYPE_ROUND);
                break;
            case R.id.bt6:
                gado.setType(RRImageview.TYPE_CIRCLE);
                break;
            case R.id.bt7:
                gado.setBorderRadius(90);
                break;
        }
    }
}




