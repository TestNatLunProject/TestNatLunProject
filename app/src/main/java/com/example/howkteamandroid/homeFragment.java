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
    private ListView lstvPost;
    private static final String EMPTY_STRING = "";

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
        lstvPost=view.findViewById(R.id.lstvPost);

        btnSend=view.findViewById(R.id.btnSend);
        btnSend.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendDataToDatabase(txtvReceive);
                ReceiveDataFromDatabase();
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

        DatabaseReference dbrefNames=db.getReference("Meets7"/*Chỗ này là ID riêng biệt từng cuộc họp, nếu chưa tồn tại thì thêm vào*/)
                .child(timeStamp).child("Name");
        DatabaseReference dbrefIndexes=db.getReference("Meets7"/*Chỗ này là ID riêng biệt từng cuộc họp, nếu chưa tồn tại thì thêm vào*/)
                .child(timeStamp).child("Index")/*mục post, bao gồm giá trị bên trong là name + index*/;

                if((txtSend.getText().toString().trim()).compareTo("")==0){
                    return;
                }
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

        String sendName=txtName.getText().toString();
        if((txtName.getText().toString().trim()).compareTo("")==0){
            sendName="unknow";
        }
        dbrefNames.setValue(sendName).addOnCompleteListener(new OnCompleteListener<Void>() {
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



    }

    private void ReceiveDataFromDatabase(){
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://howkteamandroid-default-rtdb.asia-southeast1.firebasedatabase.app/");

        final ArrayList<String> listpost=new ArrayList<>();
        final ArrayAdapter adapterPost=new ArrayAdapter<String>(getContext(),R.layout.list_post,listpost);
        lstvPost.setAdapter(adapterPost);
        DatabaseReference testretrieve=db.getReference("Meets7");

        testretrieve.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listpost.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        listpost.add(snap.getValue(String.class));
                        adapterPost.notifyDataSetChanged();
                    }
                }
                //txtvReceive.setText(dataSnapshot.child("Index").getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
