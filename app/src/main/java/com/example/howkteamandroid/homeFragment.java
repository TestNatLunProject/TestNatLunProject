package com.example.howkteamandroid;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class homeFragment extends Fragment {
    private Button btnSend;
    private View view;
    private TextView txtvReceive;
    private EditText txtSend;
    private EditText txtName;
    private ListView listViewPost;

    public static final String TAG=homeFragment.class.getSimpleName();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(savedInstanceState==null){
            Log.e(TAG,"bug: ");
        }
        else{
            Log.e(TAG,"");

        }
        view=inflater.inflate(R.layout.frag_home,container,false);

        txtvReceive =view.findViewById(R.id.txtvReceive);

        btnSend=view.findViewById(R.id.btnSend);
        btnSend.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendDataToDatabase(txtvReceive);
            }
        }));

        return view;

    }

    private void SendDataToDatabase(TextView txtvReceive) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = new Date();
        String timeStamp=formatter.format(today);

        FirebaseDatabase db = FirebaseDatabase.getInstance("https://howkteamandroid-default-rtdb.asia-southeast1.firebasedatabase.app/");

        txtName=view.findViewById(R.id.txtName);
        String nameSend=txtName.getText().toString();
        if(nameSend.trim().compareTo("")==0){
            nameSend="Unknow";
        }
        txtSend=view.findViewById(R.id.txtSend);

        DatabaseReference dbrefNames=db.getReference("Meets6"/*Chỗ này là ID riêng biệt từng cuộc họp, nếu chưa tồn tại thì thêm vào*/)
                .child(timeStamp).child("Name");
        DatabaseReference dbrefIndexes=db.getReference("Meets6"/*Chỗ này là ID riêng biệt từng cuộc họp, nếu chưa tồn tại thì thêm vào*/)
                .child(timeStamp).child("Index")/*mục post, bao gồm giá trị bên trong là name + index*/;

        dbrefNames.setValue(txtName.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(), "succNames", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(getContext(), "failNames", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        DatabaseReference dbrefTimestamps=db.getReference("Meets")
//                .child("IDs")
//                .child("Posts "+formatter.format(today))
//                .child("Timestamps"/*Thời điểm post*/);

        dbrefIndexes.setValue(txtSend.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(), "succIndexes", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(getContext(), "failIndexes", Toast.LENGTH_SHORT).show();
                }
            }
        });
        txtSend.getText().clear();
//        dbrefTimestamps.setValue(formatter.format(today).toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful()){
//                    Toast.makeText(getApplicationContext(), "succTimestamp", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Toast.makeText(getApplicationContext(), "failTimestamp", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        dbrefIndexes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    //listpost.add(snapshot.getValue().toString());
                    txtvReceive.setText(snapshot.getValue(String.class)+"/n");
                }
//                String value = dataSnapshot.getValue(String.class);
//                txtvReceive.setText(value);
//                Log.d(TAG, "Value is: " + value);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

//        ArrayList<String> listpost=new ArrayList<>();
//        ArrayAdapter adapterPost=new ArrayAdapter<String>(getContext(),R.layout.list_post,listpost);
//        listViewPost.setAdapter(adapterPost);
//        DatabaseReference refView=FirebaseDatabase.getInstance().getReference().child("Meets6");
//        refView.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                //listpost.clear();
//                //txtvReceive.setText(value);
//                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
//                    //listpost.add(snapshot.getValue().toString());
//                    txtvReceive.setText(snapshot.getValue().toString()+"/n");
//                }
//                adapterPost.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.w(TAG, "Failed to read value.", databaseError.toException());
//
//            }
//        });

//        dbrefTimestamps.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//
//                //Log.d(TAG, "Value is: " + value);
//                txtvReceive.append("\n"+value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });

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
