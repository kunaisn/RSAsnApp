package com.kunaisn.rsa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.PrintWriter;

public class Encypt extends AppCompatActivity implements View.OnClickListener {

    String secretText;
    MultiAutoCompleteTextView secretTextView;
    EditText pubKey;
    EditText planeText;
    TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encypt);

        Log.d("log", "暗号化ウィンドウ");

        ((Button)findViewById(R.id.copy)).setOnClickListener(this);
        ((Button)findViewById(R.id.back)).setOnClickListener(this);
        ((Button)findViewById(R.id.encryptButton)).setOnClickListener(this);
        secretTextView = findViewById(R.id.secretText);

        pubKey = findViewById(R.id.pubKey);
        planeText = findViewById(R.id.editTextTextPersonName);

        error = findViewById(R.id.error);
        error.setTextColor(Color.RED);

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        switch (view.getId()) {
            case (R.id.copy):
                try {
                    ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("公開鍵", secretText);
                    clipboard.setPrimaryClip(clip);
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "コピー完了",
                            Toast.LENGTH_SHORT
                    );
                    toast.show();
                } catch(Exception e) {
                }
                break;

            case (R.id.encryptButton):
                try {
                    String tmp = String.valueOf(pubKey.getText());
                    RSAKeys rsakeys = new RSAKeys(tmp);
                    tmp = String.valueOf(planeText.getText());
                    secretText = rsakeys.rsaEncryption(tmp);
                    secretTextView.setText(secretText);

                } catch(Exception e) {
                    error.setText("……ん、あれ、おかしいな？……");
                }
                break;

            case (R.id.back):
                startActivity(intent);
                break;
            default:
        }

    }

}