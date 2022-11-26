package com.kunaisn.rsa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

public class Decrypt extends AppCompatActivity implements View.OnClickListener {

    String plane;
    EditText secretText;
    EditText planeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt);

        ((Button)findViewById(R.id.copy)).setOnClickListener(this);
        ((Button)findViewById(R.id.back)).setOnClickListener(this);
        ((Button)findViewById(R.id.decrypt)).setOnClickListener(this);

        secretText = findViewById(R.id.secretText);
        planeText = findViewById(R.id.planeText);

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        switch (view.getId()) {
            case (R.id.decrypt):
                String str = String.valueOf(secretText.getText());
                Context context = getApplicationContext();
                try {
                    RSAKeys rsakeys = new RSAKeys(context);
                    plane = rsakeys.rsaDecryption(str);
                    planeText.setText(plane);
                } catch(Exception e) {
                    planeText.setText("鍵が見つかりません。");
                }
                break;
            case (R.id.back):
                startActivity(intent);
                break;
            case (R.id.copy):
                try {
                    ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("公開鍵", plane);
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