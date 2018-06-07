package bronzetrio.breeze;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//참조 : http://mingyeongun-dev.tistory.com/18
//참조 : http://javasampleapproach.com/android/firebase-authentication-sign-up-sign-in-sign-out-verify-email-android
public class Register extends AppCompatActivity {
    private EditText id;
    private EditText password;
    private EditText name;
    private Button register;
    private FirebaseAuth mAuth;
    private ArrayAdapter spinnerAdapter;
    private ArrayAdapter spinnerAdapter2;
    private ArrayAdapter spinnerAdapter3;
    private ArrayAdapter spinnerAdapter4;
    private ArrayAdapter spinnerAdapter5;
    private ArrayAdapter spinnerAdapter6;

    private ArrayList list1;
    private ArrayList list2;
    private ArrayList list3;
    private ArrayList GENDER;
    private ArrayList MAJOR;
    private ArrayList HOBBY;

    private Spinner spinner;
    private Spinner spinner2;
    private Spinner spinner3;
    private Spinner gender;
    private Spinner major;
    private Spinner hobby;

    private ImageView profile_img;
    final int PICTURE_REQUEST_CODE = 100;
    private DatabaseReference databaseReference;

    //데이터 베이스에 들어갈 데이터.
    private String Birth_Year="";
    private String Birth_Month="";
    private String Birth_Day="";
    private String Name="";
    private Bitmap bmp=null;
    private String sex="";
    private String Major="";
    private String Hobby="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(bronzetrio.breeze.R.layout.activity_register);

        id = (EditText)findViewById(bronzetrio.breeze.R.id.id);
        password = (EditText)findViewById(bronzetrio.breeze.R.id.password);
        name = (EditText)findViewById(bronzetrio.breeze.R.id.name);
        register = (Button)findViewById(bronzetrio.breeze.R.id.register);
        spinner = (Spinner)findViewById(bronzetrio.breeze.R.id.Birthday);
        spinner2 = (Spinner)findViewById(bronzetrio.breeze.R.id.Month);
        spinner3 = (Spinner)findViewById(bronzetrio.breeze.R.id.day);
        profile_img = (ImageView)findViewById(bronzetrio.breeze.R.id.Profile_img);
        gender = (Spinner)findViewById(bronzetrio.breeze.R.id.gender);
        major = (Spinner)findViewById(bronzetrio.breeze.R.id.major);
        hobby = (Spinner)findViewById(bronzetrio.breeze.R.id.hobby);
        //database 객체 가져오기.
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // 스피너에 들어갈 날짜 배열 초기화
        list1 = Year_Add();
        list2 = Month_ADD();
        list3 = new ArrayList();
        GENDER = new ArrayList();
        MAJOR = new ArrayList();
        GENDER.add("여성");
        GENDER.add("남성");
        HOBBY = new ArrayList();
        Major_Add();
        Hobby_Add();
        Day_Add(0,0,list3); //년, 월일 맞춰서 날짜 배열 리턴 다르게 해줄거임.

        //스피너 어댑터 초기화.
        spinnerAdapter = new ArrayAdapter(this, bronzetrio.breeze.R.layout.support_simple_spinner_dropdown_item, list1);
        spinnerAdapter2 = new ArrayAdapter(this, bronzetrio.breeze.R.layout.support_simple_spinner_dropdown_item, list2);
        spinnerAdapter3 = new ArrayAdapter(this, bronzetrio.breeze.R.layout.support_simple_spinner_dropdown_item, list3);
        spinnerAdapter4 = new ArrayAdapter(this, bronzetrio.breeze.R.layout.support_simple_spinner_dropdown_item, GENDER);
        spinnerAdapter5 = new ArrayAdapter(this, bronzetrio.breeze.R.layout.support_simple_spinner_dropdown_item, MAJOR);
        spinnerAdapter6 = new ArrayAdapter(this, bronzetrio.breeze.R.layout.support_simple_spinner_dropdown_item, HOBBY);

        spinner.setAdapter(spinnerAdapter);
        spinner2.setAdapter(spinnerAdapter2);
        spinner3.setAdapter(spinnerAdapter3);
        gender.setAdapter(spinnerAdapter4);
        major.setAdapter(spinnerAdapter5);
        hobby.setAdapter(spinnerAdapter6);

        spinner.setSelection(14);
        //생일 중 년도 데이터 선택.
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Birth_Year =String.valueOf(spinner.getItemAtPosition(position));
                //Toast.makeText(Register.this,"선택된 아이템 : "+Birth_Year,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //생일 중 월 선택.
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Birth_Month =String.valueOf(spinner2.getItemAtPosition(position));
                //Toast.makeText(Register.this,"선택된 아이템 : "+spinner.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //생일 중 일 선택.
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Birth_Day =String.valueOf(spinner3.getItemAtPosition(position));
                //Toast.makeText(Register.this,"선택된 아이템 : "+spinner.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //남녀 중 선택.
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sex =String.valueOf(gender.getItemAtPosition(position));
                //Toast.makeText(Register.this,"선택된 아이템 : "+spinner.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //전공 중 선택.
        major.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Major =String.valueOf(major.getItemAtPosition(position));
                //Toast.makeText(Register.this,"선택된 아이템 : "+spinner.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //취미 중 선택.
        hobby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Hobby =String.valueOf(hobby.getItemAtPosition(position));
                //Toast.makeText(Register.this,"선택된 아이템 : "+spinner.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //이미지 눌렀을 때,
        profile_img.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //출처: http://ghj1001020.tistory.com/368 [혁준 블로그]
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,false);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICTURE_REQUEST_CODE);
            }
        });
        //가입 버튼 눌렀을 때,
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String email = id.getText().toString().trim();
                String pwd = password.getText().toString().trim();
                Name = name.getText().toString().trim();
                if(Data_Empty() == 0){  //이름이 비어 있다면.
                    Toast.makeText(Register.this, "이름을 적어주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(Data_Empty() == 1){
                    Toast.makeText(Register.this, "사진을 넣어주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                createID(email,pwd);
            }
        });

        //Firebase instance
        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    private void updateUI(FirebaseUser user) {
        //로그인 되어있다면.
        if (user != null) {
        //    txtStatus.setText("User Email: " + user.getEmail() + "(verified: " + user.isEmailVerified() + ")");
        //    txtDetail.setText("Firebase User ID: " + user.getUid());

//            findViewById(R.id.email_password_buttons).setVisibility(View.GONE);
//            findViewById(R.id.email_password_fields).setVisibility(View.GONE);
//            findViewById(R.id.layout_signed_in_buttons).setVisibility(View.VISIBLE);

//            findViewById(R.id.btn_verify_email).setEnabled(!user.isEmailVerified());
        }
        else {  //로그인 되지 않았다면

            //findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
            //findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
            //findViewById(R.id.layout_signed_in_buttons).setVisibility(View.GONE);
        }
    }

    //회원 가입(아이디와 비밀번호 만) 유효 한지 확인.
    private boolean validateForm(String email, String password) {
        Pattern p = Pattern.compile("(^.*(?=.{6,100})(?=.*[0-9])(?=.*[a-zA-Z]).*$)");
        Matcher m = p.matcher(email);

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(getApplicationContext(), "이메일 형태로 입력해주세요!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "이메일 입력해주세요!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "비밀번호 입력해주세요!", Toast.LENGTH_SHORT).show();
            return false;
        }
        //if (!(m.find() && !email.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*"))){
        //    Toast.makeText(getApplicationContext(), "한글이 포함 되었습니다!", Toast.LENGTH_SHORT).show();
        //    return false;
        //}
        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "비밀번호는 최소 6자리 이상 입니다.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    private int Data_Empty(){
        if(Name.isEmpty()){
            return 0;
        }
        if(bmp == null){
            return 1;
        }
        return 2;
    }
    void createID(String mail, String Password){
        final String email = mail;
        String pwd = Password;
        //Log.d("tag","email : "+id);
        //Log.d("tag","pwd : "+ password);

        if(!validateForm(email,pwd)){
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //Log.d("TAG", "createUserWithEmail:onComplete:" + task.isSuccessful());
                //Log.d("TAG", "error" + task.getException());

                    if (task.isSuccessful()) {
                        AddData(email, Name, Birth_Year, Birth_Month, Birth_Day, bmp);
                        Toast.makeText(Register.this, "회원 가입을 성공하셨습니다!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Register.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Register.this, "등록 에러.", Toast.LENGTH_SHORT).show();
                        return;
                    }

            }
        });
    }
    public ArrayList Year_Add(){
        ArrayList a = new ArrayList();
        for(int i=1980;i<2000;i++){
            a.add(Integer.toString(i));
        }
        return a;
    }
    public ArrayList Month_ADD(){
        ArrayList a = new ArrayList();
        for(int i=1;i<13;i++){
            a.add(Integer.toString(i));
        }
        return a;
    }
    public void Day_Add(int year,int month,ArrayList a) {
        //한달에 28일 일때, 29, 30, 31일때 나눠서 보여준다.
        //구현하기 귀찮아서 아직 안함.
        int x=0;
        if(true){
            x=31;
        }
        for(int i=1;i<=x;i++){
            a.add(Integer.toString(i));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == PICTURE_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                //기존 이미지 지우기.
                profile_img.setImageResource(0);
                Uri uri = data.getData();
                ClipData clipData = data.getClipData();

                //구글 드라이브같은데서 가져오는 걸로 예상됨.
                if(clipData != null){
                    try{
                        bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    }catch(FileNotFoundException e){
                        e.printStackTrace();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                    profile_img.setImageBitmap(bmp);
                }
                //사진 폴더에서 선택할때.
                else if(uri != null){
                    try{
                        bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    }catch(FileNotFoundException e){
                        e.printStackTrace();
                    }catch(IOException e){
                       e.printStackTrace();
                    }
                    profile_img.setImageBitmap(bmp);
                }
            }
            //사진 선택 안했을때?
            else{

            }
        }
    }

    //데이터베이스에 데이터 넣기.
    public boolean AddData(String email, String Name, String Year, String Month, String Day, Bitmap Img){
        //Log.d("string2",second[0]+"  "+second[1]+"   ");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String str_Img = BitMapToString(Img);
        Profile profile = new Profile(Name, Year, Month, Day, str_Img, sex,Major,Hobby);
        Log.d("string",Name+"  "+Name+"  "+Year+"   "+Month+"  "+Day);
        databaseReference.child("profile/"+currentUser.getUid()).push().setValue(profile);


        return true;
    }

    //Bitmap 을 String 형태로 변환.
    public String BitMapToString(Bitmap bitmap){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        int dstWidth = bitmap.getWidth()/4;
        int dstHeight = bitmap.getHeight()/4;
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeight, true);

        ByteArrayOutputStream ByteStream=new  ByteArrayOutputStream();
        resized.compress(Bitmap.CompressFormat.PNG,20, ByteStream);
        byte [] b=ByteStream.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public void Major_Add(){
        MAJOR.add("컴퓨터공학부");
        MAJOR.add("정보통신학과");
        MAJOR.add("임베디드시스템공학과");
        MAJOR.add("국어국문학과");
        MAJOR.add("사회복지학과");
        MAJOR.add("수학과");
        MAJOR.add("법학부");
        MAJOR.add("기계공학과");
        MAJOR.add("경영학부");
        MAJOR.add("디자인학부");
        MAJOR.add("국어교육과");
        MAJOR.add("도시공학과");
        MAJOR.add("생명공학부");
        MAJOR.add("동북아국제통상학부");
    }

    public void Hobby_Add(){
        HOBBY.add("영화 보기");
        HOBBY.add("음악 감상");
        HOBBY.add("독서");
        HOBBY.add("운동");
        HOBBY.add("산책");
        HOBBY.add("게임");
        HOBBY.add("맛집 탐방");
        HOBBY.add("여행");
        HOBBY.add("등산");
    }
}
