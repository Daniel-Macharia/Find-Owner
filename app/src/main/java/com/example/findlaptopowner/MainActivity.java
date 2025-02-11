package com.example.findlaptopowner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private TextView textRead, encodedFormat;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Button scanQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById( R.id.tab_layout );
        viewPager = findViewById( R.id.view_pager);

        TabLayout.Tab encodeTab = tabLayout.newTab();
        TabLayout.Tab decodeTab = tabLayout.newTab();

        encodeTab.setText("Create QR");
        decodeTab.setText("Read QR");

        tabLayout.addTab( encodeTab );
        tabLayout.addTab( decodeTab );


        PagerAdapter adapter = new PagerAdapter( getSupportFragmentManager(), tabLayout.getTabCount() );
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener( new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener( new TabLayout.OnTabSelectedListener()
        {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                try {
                    int position = tab.getPosition();


                    viewPager.setCurrentItem(position);
                }catch ( Exception e )
                {

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data);
        Toast.makeText( getApplicationContext(), "Getting results..", Toast.LENGTH_SHORT).show();

        IntentResult intentResult = IntentIntegrator.parseActivityResult( requestCode, resultCode, data);

        if( intentResult != null )
        {
            if( intentResult.getContents() == null )
            {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
            else
            {
                TextView textRead, encodedFormat;

                textRead = findViewById( R.id.textRead );
                encodedFormat = findViewById( R.id.encodedFormat);
                Toast.makeText( getApplicationContext(), "Data: " + intentResult.getContents(), Toast.LENGTH_SHORT).show();
                textRead.setText( intentResult.getContents() );
                encodedFormat.setText( intentResult.getFormatName() );
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "intentResult is null!", Toast.LENGTH_SHORT).show();
            super.onActivityResult( requestCode, resultCode, data);
        }
    }

}