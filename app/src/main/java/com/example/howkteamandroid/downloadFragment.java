package com.example.howkteamandroid;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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

public class downloadFragment extends Fragment {
    Uri fileuri;
    String finame;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.frag_download,container,false);
        Button btnReceiveFile=view.findViewById(R.id.btnDownFile);
        btnReceiveFile.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReceiveFileFromDatabase();
            }
        }));

        Button btnSendFile=view.findViewById(R.id.btnSendFile);
        btnSendFile.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendFileToDatabase();
            }
        }));


        Button btnChooseFile=view.findViewById(R.id.btnChooseFile);
        btnChooseFile.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    ChooseFile();
            }
        }));
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==9&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            ChooseFile();
        }
        Toast.makeText(getActivity(),"ko co quyen",Toast.LENGTH_SHORT).show();
    }

    private void ChooseFile() {
        Intent intent=new Intent();
        intent.setType("video/mp4");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,86);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==86&&data!=null){
            fileuri=data.getData();
        }
        else{
            Toast.makeText(getActivity(),"chon lai file",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), "Success upload", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "Fail upload", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void ReceiveFileFromDatabase() {
        FirebaseStorage firebaseStorage=FirebaseStorage.getInstance("gs://howkteamandroid.appspot.com/");
        StorageReference storageRef = firebaseStorage.getReference();
        StorageReference pathref = storageRef.child("272814950_1339122439922161_4516238910750073995_n.mp4");
        pathref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url=uri.toString();
                downloadFile(getContext(),"test7",".mp4",DIRECTORY_DOWNLOADS,url);
                Toast.makeText(getContext(), "Success download", Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Fail download", Toast.LENGTH_SHORT).show();

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
            Toast.makeText(getContext(), "fail get ID", Toast.LENGTH_SHORT).show();
        }

        return myID;
    }
}
