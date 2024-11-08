package com.example.findlaptopowner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private TextView textRead, encodedFormat;
    private Button scanQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textRead = findViewById( R.id.textRead );
        encodedFormat = findViewById( R.id.encodedFormat );
        scanQR = findViewById( R.id.scanQR );

        scanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setPrompt("Scan Bar or QR code");
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setDesiredBarcodeFormats(String.valueOf(BarcodeFormat.QR_CODE));
                intentIntegrator.initiateScan();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult( requestCode, resultCode, data);

        IntentResult intentResult = IntentIntegrator.parseActivityResult( requestCode, resultCode, data);

        if( intentResult != null )
        {
            if( intentResult.getContents() == null )
            {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
            else
            {
                textRead.setText( intentResult.getContents() );
                encodedFormat.setText( intentResult.getFormatName() );
            }
        }
        else
        {
            super.onActivityResult( requestCode, resultCode, data);
        }
    }
}