package bronzetrio.ojakgyo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//참조 : http://mingyeongun-dev.tistory.com/18
//참조 : http://javasampleapproach.com/android/firebase-authentication-sign-up-sign-in-sign-out-verify-email-android
public class Register extends AppCompatActivity {
    private EditText id;
    private EditText password;
    private Button register;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        id = (EditText)findViewById(R.id.id);
        password = (EditText)findViewById(R.id.password);
        register = (Button)findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String email = id.getText().toString().trim();
                String pwd = password.getText().toString().trim();
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

    //회원 가입 유효 한지 확인.
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
        if (!(m.find() && !email.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*"))){
            Toast.makeText(getApplicationContext(), "한글이 포함 되었습니다!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "비밀번호는 최소 6자리 이상 입니다.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    void createID(String mail, String Password){
        String email = mail;
        String pwd = Password;
        Log.d("tag","email : "+id);
        Log.d("tag","pwd : "+ password);
        if(!validateForm(email,pwd)){
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //Log.d("TAG", "createUserWithEmail:onComplete:" + task.isSuccessful());
                //Log.d("TAG", "error" + task.getException());
                if(task.isSuccessful()){
                    Toast.makeText(Register.this, "회원 가입을 성공하셨습니다!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Register.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(Register.this, "등록 에러.",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}
