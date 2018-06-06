package bronzetrio.ojakgyo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//참조 : http://altongmon.tistory.com/304
public class Chat_Room extends AppCompatActivity {
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseRecyclerAdapter<Msg, ViewHolder> mFirebaseAdapter;
    private Button msgSendBtn;
    private RecyclerView recyclerView;
    private EditText msgEt;
    private DatabaseReference databaseReference;
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
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Msg, ViewHolder>(Msg.class, R.layout.msg_item, ViewHolder.class, databaseReference.child("sample")) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, Msg chatData, int position) {
                viewHolder.userTv.setText(chatData.getUser());
                viewHolder.msgTv.setText(chatData.getMSg());

                //메시지를 레이아웃에 추가하는 동작들을 여기에 코드로 작성하면 된다고 함.
            }
        };

        //onItemRangeInserted()안의 코드는 메시지가 많이 와서 화면에 표시할 수 있는 양보다
        //더 많은 양의 아이템들이 추가 됐을 때, 스크롤을 가장 아래로 내려주는 코드
        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                int lasVisiblePosition =
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                if((lasVisiblePosition == -1) || (positionStart >= (friendlyMessageCount - 1)) && (lasVisiblePosition == (positionStart - 1))){
                    recyclerView.scrollToPosition(positionStart);
                }
            }
        });

        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setAdapter(mFirebaseAdapter);

        msgSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String msg = msgEt.getText().toString();
                    if(!TextUtils.isEmpty(msg)){
                        //유저이름과 메세지로 chatData 만들기
                        Msg chatData = new Msg("userName",msg);
                        //기본 database 하위 message 라는 child chatData를 list로 만들기
                        databaseReference.child("sample").push().setValue(chatData);
                        msgEt.setText("");
                    }

            }
        });
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        //뷰홀더 패턴에 사용할 ViewHolder
        TextView msgTv;
        TextView userTv;
        public ViewHolder(View v){
            super(v);
            msgTv = (TextView)v.findViewById(R.id.msgTv);
            userTv = (TextView)v.findViewById(R.id.userTv);
        }
    }
}
