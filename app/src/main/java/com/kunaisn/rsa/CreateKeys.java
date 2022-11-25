package com.kunaisn.rsa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigInteger;

public class CreateKeys extends AppCompatActivity implements View.OnClickListener {
    String keyLen;
    TextView titleLen;
    TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_keys);
        Spinner spinner = findViewById(R.id.spinner);

        keyLen = "";
        titleLen = findViewById(R.id.textView);
        ((Button)findViewById(R.id.create)).setOnClickListener(this);
        ((Button)findViewById(R.id.back)).setOnClickListener(this);

        error = findViewById(R.id.errorText);
        error.setTextColor(Color.RED);


        String[] keyLenAry = {
                "鍵の長さを選択",
                "128",
                "256",
                "512",
                "1024",
                "2048",
                "4096"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                keyLenAry
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner)parent;
                keyLen = (String)spinner.getSelectedItem();
                try {
                    titleLen.setText("鍵の長さを選択：" + Integer.parseInt(keyLen));
                } catch(Exception e) {
                    titleLen.setText("鍵の長さを選択：");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        switch (view.getId()) {
            case (R.id.create):
                try {
                    RSAKeys key = new RSAKeys(Integer.parseInt(keyLen));
                    String str = key.toString();
                    Context context = getApplicationContext();
                    File file = new File(context.getFilesDir(), "keys.txt");
                    PrintWriter printWriter = new PrintWriter(file);
                    printWriter.println(str);
                    printWriter.close();
                    startActivity(intent);
                } catch (Exception e) {
                    error.setText("鍵の長さがわからないよ……");
                }
                break;

            case (R.id.back):
                startActivity(intent);
                break;
            default:
        }

    }
}