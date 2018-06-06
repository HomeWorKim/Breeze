package bronzetrio.ojakgyo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import bronzetrio.ojakgyo.fragments.ChatFragment;
import bronzetrio.ojakgyo.fragments.MatchFragment;
import bronzetrio.ojakgyo.fragments.ProfileFragment;
import bronzetrio.ojakgyo.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {

//    private Button register_btn;
//    private Button login_btn;
//    private Button chat_btn;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_match:
                    fragment = new MatchFragment();
                    break;
                case R.id.navigation_chat:
                    fragment = new ChatFragment();
                    break;
                case R.id.navigation_profile:
                    fragment = new ProfileFragment();
                    break;
                case R.id.navigation_settings:
                    fragment = new SettingsFragment();
                    break;
            }
//            return true;
            return loadFragment(fragment);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        register_btn = (Button)findViewById(R.id.register);
//        login_btn = (Button)findViewById(R.id.login);
//        chat_btn = (Button)findViewById(R.id.chatroom);

        //loading the default fragment
        loadFragment(new MatchFragment());

//        View.OnClickListener listener = new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                int i = v.getId();
//                if(i == R.id.register) {
//                    Intent intent = new Intent(MainActivity.this, Register.class);
//                    startActivity(intent);
//                }
//                else if(i == R.id.login){
//                    Intent intent = new Intent(MainActivity.this, Login.class);
//                    startActivity(intent);
//                }
//                else if(i == R.id.chatroom){
//                    Intent intent = new Intent(MainActivity.this, Chat_Room.class);
//                    startActivity(intent);
//                }
//            }
//        };
//        register_btn.setOnClickListener(listener);
//        login_btn.setOnClickListener(listener);
//        chat_btn.setOnClickListener(listener);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
