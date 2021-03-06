package bronzetrio.breeze;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
    private EditText Id_box;
    private EditText password;
    private Button login_btn;
    private Button register_btn;
    private TextView skip_txtview;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(bronzetrio.breeze.R.layout.activity_login);

        Id_box = (EditText)findViewById(bronzetrio.breeze.R.id.Id);
        password = (EditText)findViewById(bronzetrio.breeze.R.id.password);
        login_btn = (Button)findViewById(bronzetrio.breeze.R.id.login);
        register_btn = (Button)findViewById(bronzetrio.breeze.R.id.register);
        skip_txtview = (TextView)findViewById(R.id.skip);

        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int i = v.getId();
                if(i == bronzetrio.breeze.R.id.login) {
                    String email = Id_box.getText().toString().trim();
                    String pwd = password.getText().toString().trim();
                    signIn(email,pwd);
                }
                else if(i == bronzetrio.breeze.R.id.register){
                    Intent intent = new Intent(Login.this, Register.class);
                    startActivity(intent);
                } else if(i == R.id.skip){
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        login_btn.setOnClickListener(listener);
        register_btn.setOnClickListener(listener);
        skip_txtview.setOnClickListener(listener);
        //Firebase instance
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            Log.d("로근:", "");
        }else{
            Log.d("비로근:", "");
        }
            //if (mAuth != null)
        //{
        //    Intent intent = new Intent(Login.this, MainActivity.class);
        //    startActivity(intent);
        //    finish();
        //}
    }

    private void signIn(String email, String password){
        Log.e("Login","signIn : "+email);
        if(!validateForm(email, password)){
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.e("Login : ","signIn : Success!");

                            //signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Log.e("Login : ","signIn: Fail!", task.getException());
                            Toast.makeText(getApplicationContext(),"로그인에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //로그인 유효한지 확인..
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
//            Toast.makeText(getApplicationContext(), "한글이 포함 되었습니다!", Toast.LENGTH_SHORT).show();
//            return false;
        }

        return true;
    }
}
