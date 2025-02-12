package com.example.findlaptopowner;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ShowQRCodeActivity extends AppCompatActivity {

    private ImageView qrImage;
    private Bitmap bm = null;
    private Button downloadQR;

    private String regNo;

    @Override
    public void onCreate(Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_qr_code);

        qrImage = findViewById( R.id.qrImage );
        downloadQR = findViewById( R.id.downloadQR );

        Intent data = getIntent();

        String name, serialNum, model, color;

        regNo = data.getStringExtra("regNo");
        name = data.getStringExtra("name");
        serialNum = data.getStringExtra("serialNum");
        model = data.getStringExtra("model");
        color = data.getStringExtra("color");

        try {
            String qrData = "{regNo:" + regNo + "," +
                    "name:" + name + "," +
                    "serialNum:" + serialNum + "," +
                    "model:" + model + "," +
                    "color:" + color + "}";

            downloadQR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadQRCode(qrData);
                }
            });

            downloadQRCode(qrData);
        }catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), "Error generating QR: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadQRCode( String qrData)
    {
        try {
            bm = generateQRCode(qrData);

            writeToExternalStorage();

            qrImage.setImageBitmap( bm );
        }catch ( WriterException e)
        {
            Toast.makeText(getApplicationContext(), "Error generating QR: " + e, Toast.LENGTH_SHORT).show();
        } catch ( Exception e)
        {
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    private void writeToExternalStorage()
    {
        try {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    44);

            File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            // Storing the data in file with name as geeksData.txt
            File file = new File(folder, "/" + regNo + "_qr_code.jpg");
            if( ! file.exists() )
            {
                file.createNewFile();
            }

            //Path path = Paths.get(file.toURI());
            //com.google.zxing.j2se.MatrixToImageWriter.writeToPath(bm, "jpg", path );
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            if( bm.compress( Bitmap.CompressFormat.JPEG, 90, out) )
            {
                Toast.makeText(getApplicationContext(), "Writing to: " + file.getAbsolutePath().toString(), Toast.LENGTH_SHORT).show();
                qrImage.setImageBitmap(bm);
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }


    private Bitmap generateQRCode(String data) throws WriterException {

        QRCodeWriter writer = new QRCodeWriter();

        BitMatrix matrix = writer.encode(data, BarcodeFormat.QR_CODE, 600, 600);

        return Bitmap.createBitmap(matrix.getWidth(), matrix.getHeight(), Bitmap.Config.ARGB_8888);
    }
}
