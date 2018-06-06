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
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Id_box = (EditText)findViewById(R.id.Id);
        password = (EditText)findViewById(R.id.password);
        login_btn = (Button)findViewById(R.id.login);

        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int i = v.getId();
                if(i == R.id.login) {
                    String email = Id_box.getText().toString().trim();
                    String pwd = password.getText().toString().trim();
                    signIn(email,pwd);
                }
            }
        };
        login_btn.setOnClickListener(listener);
        //Firebase instance
        mAuth = FirebaseAuth.getInstance();
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
