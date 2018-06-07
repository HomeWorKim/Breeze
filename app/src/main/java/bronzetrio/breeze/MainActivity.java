package bronzetrio.breeze;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import bronzetrio.breeze.fragments.ChatFragment;
import bronzetrio.breeze.fragments.MatchFragment;
import bronzetrio.breeze.fragments.ProfileFragment;
import bronzetrio.breeze.fragments.SettingsFragment;

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
                case bronzetrio.breeze.R.id.navigation_match:
                    fragment = new MatchFragment();
                    break;
                case bronzetrio.breeze.R.id.navigation_chat:
                    fragment = new ChatFragment();
                    break;
                case bronzetrio.breeze.R.id.navigation_profile:
                    fragment = new ProfileFragment();
                    break;
                case bronzetrio.breeze.R.id.navigation_settings:
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
        setContentView(bronzetrio.breeze.R.layout.activity_main);


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
        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab1, R.drawable.ic_public, R.color.colorPrimaryDark);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.tab1, R.drawable.ic_chat, R.color.colorPrimaryDark);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab1, R.drawable.ic_person, R.color.colorPrimaryDark);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.tab1, R.drawable.ic_tune, R.color.colorPrimaryDark);

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        bottomNavigation.setAccentColor(Color.parseColor("#9B51E0"));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));


        bottomNavigation.setCurrentItem(0);

        // Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                Fragment fragment = null;
                switch (position) {
                    case 0:
                        fragment = new MatchFragment();
                        break;
                    case 1:
                        fragment = new ChatFragment();
                        break;
                    case 2:
                        fragment = new ProfileFragment();
                        break;
                    case 3:
                        fragment = new SettingsFragment();
                        break;
                }
                return loadFragment(fragment);
//                return true;
            }
        });

//Original android bottom navigation
//        BottomNavigationView navigation = (BottomNavigationView) findViewById(bronzetrio.breeze.R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(bronzetrio.breeze.R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
