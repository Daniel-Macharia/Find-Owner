package com.example.findlaptopowner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class DecodeQR extends Fragment {

    private TextView textRead, encodedFormat;
    private MainActivity mainActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.decode_qr_fragment, container, false);
        textRead = view.findViewById( R.id.textRead );
        encodedFormat = view.findViewById( R.id.encodedFormat );
        Button scanQR = view.findViewById( R.id.scanQR );

        mainActivity = (MainActivity) getActivity();

        scanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator((Activity) container.getContext());
                intentIntegrator.setPrompt("Scan QR code");
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setDesiredBarcodeFormats(String.valueOf(BarcodeFormat.QR_CODE));
                intentIntegrator.initiateScan();
                mainActivity.SCAN_QR = true;
            }
        });

        return view;
    }

}