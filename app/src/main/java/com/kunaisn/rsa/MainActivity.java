package com.kunaisn.rsa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import java.io.File;
import java.math.BigInteger;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String pubKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Button)findViewById(R.id.copy)).setOnClickListener(this);
        ((Button)findViewById(R.id.reset)).setOnClickListener(this);
        ((Button)findViewById(R.id.whatIs)).setOnClickListener(this);
        ((Button)findViewById(R.id.decryptionButton)).setOnClickListener(this);
        ((Button)findViewById(R.id.encryptionButton)).setOnClickListener(this);

        MultiAutoCompleteTextView pubKeyText = findViewById(R.id.textPublicKey);

        Context context = getApplicationContext();
        try {
            RSAKeys rsakeys = new RSAKeys(context);
            pubKey = rsakeys.getKeyLen() + "&" + rsakeys.getNHex() + "&" + rsakeys.getEHex() + "&";
            pubKeyText.setText(pubKey);
        } catch(Exception e) {
            pubKeyText.setText("公開鍵が見つかりません。");
        }

    }

    @Override
    public void onClick(View view) {
        Intent intentKeys;

        switch (view.getId()) {
            case (R.id.encryptionButton):
                intentKeys = new Intent(this, Encypt.class);
                startActivity(intentKeys);
                break;
            case (R.id.reset):
                intentKeys = new Intent(this, CreateKeys.class);
                startActivity(intentKeys);
                break;
            case (R.id.copy):
                try {
                    ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("公開鍵", pubKey);
                    clipboard.setPrimaryClip(clip);
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "コピー完了",
                            Toast.LENGTH_SHORT
                    );
                    toast.show();
                } catch(Exception e) {

                }

                break;
            default:
        }

    }

}