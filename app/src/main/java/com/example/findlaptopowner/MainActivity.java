package com.example.findlaptopowner;

import android.app.Activity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private TextView textRead, encodedFormat;


    private TabLayout tabLayout;
    private ViewPager viewPager;

    public final String IMAGE_TYPE = "findlaptopowner_imageType";
    public boolean STUDENT_IMAGE = false, LAPTOP_IMAGE = false, SCAN_QR = false;

    public String studentPhotoURI, laptopPhotoURI;

    public ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result->{

                if( result.getResultCode() == Activity.RESULT_OK )
                {
                    Intent data = result.getData();

                    if( data != null )
                    {
                        if( STUDENT_IMAGE )
                        {
                            Uri selectedImageUri = data.getData();
                            studentPhotoURI = new String(selectedImageUri.toString());

                            Bitmap selectedImageBitmap;

                            try {
                                selectedImageBitmap = MediaStore.Images.Media.getBitmap(
                                        this.getContentResolver(),
                                        selectedImageUri
                                );

                                EncodeQR.studentPhoto.setImageBitmap( selectedImageBitmap );

                            }catch ( Exception e )
                            {
                                Toast.makeText( MainActivity.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(getApplicationContext(), "student image..", Toast.LENGTH_SHORT).show();
                            STUDENT_IMAGE = false;
                        }
                        else if( LAPTOP_IMAGE )
                        {
                            Uri selectedImageUri = data.getData();
                            laptopPhotoURI = new String(selectedImageUri.toString());

                            Bitmap selectedImageBitmap;

                            try {
                                selectedImageBitmap = MediaStore.Images.Media.getBitmap(
                                        this.getContentResolver(),
                                        selectedImageUri
                                );


                                EncodeQR.laptopPhoto.setImageBitmap( selectedImageBitmap );

                            }catch ( Exception e )
                            {
                                Toast.makeText( MainActivity.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(getApplicationContext(), "laptop image..", Toast.LENGTH_SHORT).show();
                            LAPTOP_IMAGE = false;
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "unexpected result! image..", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Null results", Toast.LENGTH_SHORT).show();
                    }

                }
        }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create tabs
        tabLayout = findViewById( R.id.tab_layout );
        viewPager = findViewById( R.id.view_pager);

        TabLayout.Tab encodeTab = tabLayout.newTab();
        TabLayout.Tab decodeTab = tabLayout.newTab();

        encodeTab.setText("Create QR");
        decodeTab.setText("Read QR");

        tabLayout.addTab( decodeTab );
        tabLayout.addTab( encodeTab );


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

        if( data != null )
        {

            if( SCAN_QR ) {
                Toast.makeText(getApplicationContext(), "read QR successfully.", Toast.LENGTH_SHORT).show();
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
                    //Toast.makeText(getApplicationContext(), "intentResult is null!", Toast.LENGTH_SHORT).show();
                    super.onActivityResult( requestCode, resultCode, data);
                }
            }
            else{
                    Toast.makeText(getApplicationContext(), "unexpected result", Toast.LENGTH_SHORT).show();

            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Null results", Toast.LENGTH_SHORT).show();
        }

    }

}