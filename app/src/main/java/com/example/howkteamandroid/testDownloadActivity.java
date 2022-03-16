package com.example.howkteamandroid;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class testDownloadActivity extends AppCompatActivity {

    Uri fileuri;
    String finame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_download);



        Button btnReceiveFile=(Button)findViewById(R.id.btnDownFile);
        btnReceiveFile.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReceiveFileFromDatabase();
            }
        }));

        Button btnSendFile=(Button)findViewById(R.id.btnSendFile);
        btnSendFile.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendFileToDatabase();
            }
        }));

        Button btnChooseFile=(Button)findViewById(R.id.btnChooseFile);
        btnChooseFile.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseFile();
            }
        }));
    }
    private void ChooseFile() {
        Intent intent=new Intent();
        intent.setType("video/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100&&data!=null){
            fileuri=data.getData();
            finame=data.getDataString();
        }
    }


    private void SendFileToDatabase() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String filename=formatter.format(now);
        // Create a Cloud Storage reference from the app
        FirebaseStorage firebaseStorage=FirebaseStorage.getInstance("gs://howkteamandroid.appspot.com");
        StorageReference storageReference=firebaseStorage.getReference("vids/"+filename+" "+finame);

        storageReference.putFile(fileuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Success upload", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Fail upload", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void ReceiveFileFromDatabase() {
        FirebaseStorage firebaseStorage=FirebaseStorage.getInstance("gs://howkteamandroid.appspot.com/");
        StorageReference storageRef = firebaseStorage.getReference();
        StorageReference pathref = storageRef.child("9GAG-Go-Fun-The-World.mp4");
        pathref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url=uri.toString();
                downloadFile(testDownloadActivity.this,"test5",".mp4",DIRECTORY_DOWNLOADS,url);
                Toast.makeText(getApplicationContext(), "Success download", Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Fail download", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {
        DownloadManager downloadManager=(DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri=Uri.parse(url);
        DownloadManager.Request request=new DownloadManager.Request(uri);
        request.setDestinationInExternalFilesDir(context,destinationDirectory,fileName+fileExtension);
        downloadManager.enqueue(request);
    }

    private String GetLocalID(){
        String myID="";
        try {
            InetAddress ip= InetAddress.getLocalHost();

            myID=ip.getHostName(); //cái này mọi người thử suy nghĩ xem nên dùng host name hay IP máy hơn.
            //myIP=ip.getHostAddress();
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "fail get ID", Toast.LENGTH_SHORT).show();
        }

        return myID;
    }
}