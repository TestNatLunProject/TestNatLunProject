package com.example.howkteamandroid;

import static android.os.Environment.DIRECTORY_DOCUMENTS;
import static android.os.Environment.DIRECTORY_DOWNLOADS;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final String TAG=MainActivity.class.getSimpleName();

    private final int HOME=1;
    private final int DOWNLOAD=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((R.layout.activity_main));

        if(savedInstanceState==null){
            Log.e(TAG,"bug: ");
        }
        else{
            Log.e(TAG,"");

        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont,new homeFragment()).commit();
//        TextView txtvReceive =(TextView)findViewById(R.id.txtvReceive);
//
//        Button btnSend=(Button)findViewById(R.id.btnSend);
//        btnSend.setOnClickListener((new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SendDataToDatabase(txtvReceive);
//            }
//        }));
        BottomNavigationView bottomNavigationView=findViewById(R.id.navBar);
        bottomNavigationView.setOnNavigationItemSelectedListener((new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectFrag=null;
                switch(item.getItemId()){
                    case R.id.homeItem:
                        selectFrag=new homeFragment();
                        break;
                    case R.id.downloadItem:
                        selectFrag=new downloadFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont,selectFrag).commit();
                return true;
            }
        }));
}

//    private void SendDataToDatabase(TextView txtvReceive) {
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date today = new Date();
//        String timeStamp=formatter.format(today);
//
//        FirebaseDatabase db = FirebaseDatabase.getInstance("https://howkteamandroid-default-rtdb.asia-southeast1.firebasedatabase.app/");
//
//        DatabaseReference dbref=db.getReference(formatter.format(today));
//
//        EditText txtName=(EditText)findViewById(R.id.txtName);
//        String nameSend=txtName.getText().toString();
//        if(nameSend.trim().compareTo("")==0){
//            nameSend="Unknow";
//        }
//        DatabaseReference dbrefIndexes=db.getReference("Meets5"/*Chỗ này là ID riêng biệt từng cuộc họp, nếu chưa tồn tại thì thêm vào*/)
//                .child(timeStamp+" "+nameSend/*cái chỗ này mấy tên người dùng trong cuộc họp*/);
//
////        DatabaseReference dbrefTimestamps=db.getReference("Meets")
////                .child("IDs")
////                .child("Posts "+formatter.format(today))
////                .child("Timestamps"/*Thời điểm post*/);
//
//        EditText txtSend=(EditText)findViewById(R.id.txtSend);
//
//        dbrefIndexes.setValue(txtSend.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful()){
//                    Toast.makeText(getApplicationContext(), "succIndexes", Toast.LENGTH_SHORT).show();
//
//                }
//                else{
//                    Toast.makeText(getApplicationContext(), "failIndexes", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        txtSend.getText().clear();
////        dbrefTimestamps.setValue(formatter.format(today).toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
////            @Override
////            public void onComplete(@NonNull Task<Void> task) {
////                if(task.isSuccessful()){
////                    Toast.makeText(getApplicationContext(), "succTimestamp", Toast.LENGTH_SHORT).show();
////                }
////                else{
////                    Toast.makeText(getApplicationContext(), "failTimestamp", Toast.LENGTH_SHORT).show();
////                }
////            }
////        });
//
//        dbrefIndexes.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//
//                //Log.d(TAG, "Value is: " + value);
//                txtvReceive.setText(value);
//            }
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });
//
////        dbrefTimestamps.addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(DataSnapshot dataSnapshot) {
////                // This method is called once with the initial value and again
////                // whenever data at this location is updated.
////                String value = dataSnapshot.getValue(String.class);
////
////                //Log.d(TAG, "Value is: " + value);
////                txtvReceive.append("\n"+value);
////            }
////
////            @Override
////            public void onCancelled(DatabaseError error) {
////                // Failed to read value
////                Log.w(TAG, "Failed to read value.", error.toException());
////            }
////        });
//
//    }
//
//    private String GetLocalID(){
//        String myID="";
//        try {
//            InetAddress ip= InetAddress.getLocalHost();
//
//            myID=ip.getHostName(); //cái này mọi người thử suy nghĩ xem nên dùng host name hay IP máy hơn.
//            //myIP=ip.getHostAddress();
//        }catch (Exception ex){
//            Toast.makeText(getApplicationContext(), "fail get ID", Toast.LENGTH_SHORT).show();
//        }
//
//        return myID;
//    }


}