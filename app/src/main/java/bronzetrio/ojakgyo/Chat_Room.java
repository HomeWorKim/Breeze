package bronzetrio.ojakgyo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//참조 : http://altongmon.tistory.com/304
public class Chat_Room extends AppCompatActivity {
    LinearLayoutManager mLinearLayoutManager;
    FirebaseRecyclerAdapter<Msg, ViewHolder> mFirebaseAdapter;
    Button msgSendBtn;
    RecyclerView recyclerView;
    EditText msgEt;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat__room);

        //database 객체 가져오기.
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //리싸이클러뷰 넣을 LinearLayout 형태로 초기화 할 객체 가져오기.
        mLinearLayoutManager = new LinearLayoutManager(this);
        //리니어레이아웃 옵션 추가
        mLinearLayoutManager.setStackFromEnd(true);

        recyclerView = (RecyclerView)findViewById(R.id.msgLv);
        msgSendBtn = (Button)findViewById(R.id.sendMsgBtn);
        msgEt = (EditText)findViewById(R.id.msgEt);

        //이 어댑터는 Firebase의 database를 지속적으로 감시하며 메시지가 추가 됐을 때, 혹은
        //액티비티가 처음 로딩 됐을 때, 메시지들을 레이아웃에 추가.
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Msg, ViewHolder>() {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, Msg model, int position) {

            }
        };

        //onItemRangeInserted()안의 코드는 메시지가 많이 와서 화면에 표시할 수 있는 양보다
        //더 많은 양의 아이템들이 추가 됐을 때, 스크롤을 가장 아래로 내려주는 코드
        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
            }
        });
    }
}
