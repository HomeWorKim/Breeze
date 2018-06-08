package bronzetrio.breeze;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Open_Matching extends AppCompatActivity {
    private static final String TAG = "Open_Matching";

    private Button reject;
    private Button accept;
    private ImageView profile_img;
    private TextView Name;
    private TextView similarity_tv;
    private boolean flag = true;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> majors = new ArrayList<>();
    ArrayList<String> hobbys = new ArrayList<>();
    ArrayList<String> birthdays = new ArrayList<>();
    ArrayList<String> sexes = new ArrayList<>();
    ArrayList<String> imges = new ArrayList<>();
    ArrayList<String> UID = new ArrayList<>();
    int pop_idx = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open__matching);

        reject = (Button) findViewById(R.id.reject);
        accept = (Button) findViewById(R.id.accept);
        profile_img = (ImageView) findViewById(R.id.profile_img);
        Name = (TextView) findViewById(R.id.name);
        similarity_tv = (TextView)findViewById(R.id.similarity_tv);
        //database 객체 가져오기.
        databaseReference = FirebaseDatabase.getInstance().getReference("profile");
        mAuth = FirebaseAuth.getInstance();


        if (flag) {
            Map<String, Object> taskMap = new HashMap<String, Object>();
            taskMap.put("/token", 1);
            databaseReference.updateChildren(taskMap);
            Map<String, Object> taskMap2 = new HashMap<String, Object>();
            taskMap2.put("/token", null);
            databaseReference.updateChildren(taskMap2);
            flag = false;
        }
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot first : dataSnapshot.getChildren()) {
                            for (DataSnapshot second : first.getChildren()) {
                                {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if(first.getKey().toString().equals(user.getUid())){
                                        continue;
                                    }

                                    Log.d("match : ", first.getKey().toString());
                                    String name = (String) second.child("name").getValue();
                                    String major = (String) second.child("major").getValue();
                                    String hobby = (String) second.child("hobby").getValue();
                                    String year = (String) second.child("year").getValue();
                                    String month = (String) second.child("month").getValue();
                                    String day = (String) second.child("day").getValue();
                                    String sex = (String) second.child("sex").getValue();
                                    String img = (String) second.child("img").getValue();
                                    String uid = (String) first.getKey().toString();

                                    String birth = year + "-" + month + "-" + day;
                                    String talent;String similarity_score;
                                    try{
                                        talent=(String) second.child("talent").getValue();
                                        similarity_score = (second.child("similarity").getValue())+"%";
                                        similarity_tv.setText(talent+" 와 "+similarity_score+" 매칭");
                                    }catch (NullPointerException e){
                                        Log.e(TAG,"error : "+e);
                                    }

                                    names.add(name);
                                    majors.add(major);
                                    hobbys.add(hobby);
                                    birthdays.add(birth);
                                    sexes.add(sex);
                                    imges.add(img);
                                    UID.add(uid);

                                }
                            }
                        }
                        int random = (int) (Math.random() * (names.size() - 1));
                        Name.setText(names.get(random));
                        profile_img.setImageBitmap(StringToBitMap(imges.get(random)));
                        pop_idx = random;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = v.getId();
                //재매칭 클릭.
                if (i == R.id.reject) {
                    Log.d("Size : ", Integer.toString(names.size())+"      "+Integer.toString(pop_idx));
                    if (names.size() == 1) {
                        Toast.makeText(getApplicationContext(), "매칭을 진행 할 수 없습니다.", Toast.LENGTH_SHORT).show();

                    } else {
                        names.remove(pop_idx);
                        majors.remove(pop_idx);
                        hobbys.remove(pop_idx);
                        birthdays.remove(pop_idx);
                        sexes.remove(pop_idx);
                        imges.remove(pop_idx);
                        UID.remove(pop_idx);

                        int random = (int) (Math.random() * (names.size() - 1));
                        Log.d("Size : ",Integer.toString(random));
                        pop_idx = random;
                        Name.setText(names.get(random));
                        profile_img.setImageBitmap(StringToBitMap(imges.get(random)));
                    }
                }
                //승락 클릭.
                else if (i == R.id.accept) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    String uid = user.getUid();
                    Intent intent = new Intent(Open_Matching.this, Chat_Room_v2.class);
                    intent.putExtra("first_uid",uid);
                    intent.putExtra("second_uid",UID.get(pop_idx));
                    startActivity(intent);
                    finish();
                }

            }
        };
        reject.setOnClickListener(listener);
        accept.setOnClickListener(listener);
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

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
