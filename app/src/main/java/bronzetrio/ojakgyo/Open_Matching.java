package bronzetrio.ojakgyo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Open_Matching extends AppCompatActivity {
    private Button reject;
    private Button accept;
    private ImageView profile_img;
    private TextView Name;

    private boolean flag = true;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open__matching);

        reject = (Button)findViewById(R.id.reject);
        accept = (Button)findViewById(R.id.accept);
        profile_img = (ImageView)findViewById(R.id.profile_img);
        Name = (TextView)findViewById(R.id.name);
        //database 객체 가져오기.
        databaseReference = FirebaseDatabase.getInstance().getReference("profile");
        mAuth = FirebaseAuth.getInstance();
        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int i = v.getId();
                if(i == R.id.reject){
                    Log.d("tag", "profile");
                    Map<String, Object> taskMap = new HashMap<String, Object>();
                    taskMap.put("/token",1);
                    databaseReference.updateChildren(taskMap);
                    Map<String, Object> taskMap2 = new HashMap<String, Object>();
                    taskMap2.put("/token",null);
                    databaseReference.updateChildren(taskMap2);
                }else if(i == R.id.accept){

                }
            }
        };

        if(flag){
            Log.d("tag", "profile");
            Map<String, Object> taskMap = new HashMap<String, Object>();
            taskMap.put("/token",1);
            databaseReference.updateChildren(taskMap);
            Map<String, Object> taskMap2 = new HashMap<String, Object>();
            taskMap2.put("/token",null);
            databaseReference.updateChildren(taskMap2);
            flag = false;
        }
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot first : dataSnapshot.getChildren()){
                            for(DataSnapshot second : first.getChildren()){
                                for(DataSnapshot third : second.getChildren()){
                                    for(DataSnapshot fourth : third.getChildren()) {
                                        String name = (String) fourth.getKey();
                                        Log.d("datachange", name);
                                    }
                                }
                            }
                        }
                        /*
                        for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                            Log.d("datachange", messageSnapshot.getKey());
                            for (DataSnapshot messageSnapshot2: messageSnapshot.getChildren()) {
                                Log.d("datachange_id", messageSnapshot2.getKey());

                                String name = (String) messageSnapshot2.child("day").getValue();
                                String message = (String) messageSnapshot2.child("month").getValue();
                                Log.d("datachange_dayandmonth", name + " , "+message);
                            }

                        }//이중포문에 의한 전체 프로필 불러오기 databaseReference = FirebaseDatabase.getInstance().getReference("profile/"+second+"/"+last);
                        */
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        };
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
