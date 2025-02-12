package com.example.findlaptopowner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.Blob;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firestore.v1.BloomFilterOrBuilder;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EncodeQR#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EncodeQR extends Fragment {

    private MainActivity mainActivity;
    public static ImageView studentPhoto, laptopPhoto;


    /* // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EncodeQR() {
        // Required empty public constructor
    }

    //
   //  * Use this factory method to create a new instance of
   //  * this fragment using the provided parameters.
   //  *
  //   * @param param1 Parameter 1.
  //   * @param param2 Parameter 2.
    // * @return A new instance of fragment EncodeQR.
  //   /
    // TODO: Rename and change types and number of parameters
    public static EncodeQR newInstance(String param1, String param2) {
        EncodeQR fragment = new EncodeQR();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        } */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EditText regNum, ownerName, laptopModel, laptopSerialNum, laptopColor;
        Button save;

        mainActivity = (MainActivity) getActivity();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.encode_to_qr_fragment, container, false);

        regNum = view.findViewById(R.id.regNum );
        ownerName = view.findViewById( R.id.ownerName );
        laptopSerialNum = view.findViewById(R.id.laptopSerialNum );
        laptopModel = view.findViewById(R.id.laptopModel );
        laptopColor = view.findViewById(R.id.laptopColor );

        studentPhoto = view.findViewById( R.id.studentPhoto );
        laptopPhoto = view.findViewById( R.id.laptopPhoto );

        save = view.findViewById( R.id.saveData );

        studentPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectStudentPhoto();
            }
        });

        laptopPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectLaptopPhoto();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reg = null, name = null;
                String serialNum = null , model = null, color = null;

                reg = regNum.getText().toString();
                name = ownerName.getText().toString();
                serialNum = laptopSerialNum.getText().toString();
                model = laptopModel.getText().toString();
                color = laptopColor.getText().toString();

                Toast.makeText(getContext(), "Details:\n\n" +
                        "Reg: " + reg +
                        "\nName: " + name +
                        "\nSerialNum: " + serialNum +
                        "\nmodel: " + model +
                        "\ncolor: " + color, Toast.LENGTH_SHORT).show();

                storeData( reg, name, mainActivity.studentPhotoURI, serialNum,
                        model, color, mainActivity.laptopPhotoURI );
                createQR( reg, name, serialNum, model, color );
            }
        });

        return view;
    }

    private void selectStudentPhoto()
    {
        Intent intent = new Intent();
        intent.setType( "image/*" );
        intent.setAction( Intent.ACTION_GET_CONTENT );

        mainActivity.STUDENT_IMAGE = true;
        mainActivity.launcher.launch(intent);
        //startActivityForResult(intent, mainActivity.STUDENT_IMAGE);
    }

    private void selectLaptopPhoto()
    {
        Intent intent = new Intent();
        intent.setType( "image/*" );
        intent.setAction( Intent.ACTION_GET_CONTENT );

        mainActivity.LAPTOP_IMAGE = true;
        mainActivity.launcher.launch(intent);
        //startActivityForResult(intent, mainActivity.LAPTOP_IMAGE);
    }
    private void sstoreData( String regNo, String ownerName, String studentPhotoURI,
                            String serialNum, String model, String color, String laptopPhotoURI )
    {
        try{
            InputStream studentPhotoInputStream = getContext().getContentResolver()
                    .openInputStream(Uri.parse(studentPhotoURI));
            InputStream laptopPhotoInputStream = getContext().getContentResolver()
                    .openInputStream(Uri.parse(laptopPhotoURI));

            String studentPhotoData = Arrays.toString( getBytes(studentPhotoInputStream ));
            String laptopPhotoData = Arrays.toString( getBytes( laptopPhotoInputStream ));


            //add other details
            Toast.makeText(getContext(), "storing details..", Toast.LENGTH_SHORT).show();
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference detailsRef = db.getReference("owner");

            Owner owner = new Owner( ownerName, studentPhotoData,
                    serialNum, model, color, laptopPhotoData);
            detailsRef.child(regNo).setValue(owner)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(mainActivity, "Details stored successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
        }catch(Exception  e)
        {
            Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    private void storeData( String regNo, String ownerName, String studentPhotoURI,
                            String serialNum, String model, String color, String laptopPhotoURI )
    {
        try{
            FirebaseApp.initializeApp(getContext().getApplicationContext());
            //add student and laptop photos
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference("owner");

            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference detailsRef = db.getReference("owner");

            String parsedRegNo = regNo.replaceAll("[/-]", "");

            storageRef.child("StudentPhoto/" + parsedRegNo )
                    .putFile(Uri.parse(studentPhotoURI))
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            storageRef.child("LaptopPhoto/" + parsedRegNo )
                                    .putFile( Uri.parse( laptopPhotoURI ) )
                                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            //add other details
                                            Toast.makeText(getContext(), "storing details..", Toast.LENGTH_SHORT).show();

                                            Owner owner = new Owner( ownerName, "",
                                                    serialNum, model, color, "");
                                            detailsRef.child(parsedRegNo).setValue(owner)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(mainActivity, "Details stored successfully", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(  getContext().getApplicationContext(), ShowQRCodeActivity.class);
                                                            intent.putExtra("regNo", regNo);
                                                            intent.putExtra("name", ownerName);
                                                            intent.putExtra("serialNum", serialNum);
                                                            intent.putExtra("model", model);
                                                            intent.putExtra("color", color);
                                                            startActivity(intent);
                                                        }
                                                    });
                                        }
                                    });
                        }
                    });

        }catch( Exception e )
        {
            Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    public byte[] getBytes(InputStream inputStream) {
        Toast.makeText(getContext(), "reading image from phone", Toast.LENGTH_SHORT).show();
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        try{
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            Toast.makeText(getContext(), "successfully read image from phone", Toast.LENGTH_SHORT).show();
        }catch (Exception e)
        {
            Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
        return byteBuffer.toByteArray();
    }

    private void createQR( String regNo, String name, String serialNum,
                           String model, String color )
    {
        try{

        }catch( Exception e )
        {
            Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

}

class Owner
{
    public String name, studentPhoto, serialNum, model, color, laptopPhoto;

    public Owner( String name, String studentPhoto,
                  String serialNum, String model, String color, String laptopPhoto )
    {
        this.name = name;
        this.studentPhoto = studentPhoto;
        this.serialNum = serialNum;
        this.model = model;
        this.color = color;
        this.laptopPhoto = laptopPhoto;
    }
}