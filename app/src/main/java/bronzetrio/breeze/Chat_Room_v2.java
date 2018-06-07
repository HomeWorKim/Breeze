package bronzetrio.breeze;

import android.content.Context;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Chat_Room_v2 extends AppCompatActivity {
    private String first_uid;
    private String second_uid;

    private Button SendBtn;
    private EditText SendTxt;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<Msg, ViewHolder> mFirebaseAdapter;
    private DatabaseReference databaseReference;

    private String room_name="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat__room_v2);

        Intent intent = getIntent();
        first_uid =intent.getExtras().getString("first_uid");
        second_uid =intent.getExtras().getString("second_uid");

        final String room_type_1 = first_uid + "_" + second_uid;
        final String room_type_2 = second_uid + "_" + first_uid;

        databaseReference = FirebaseDatabase.getInstance().getReference("chatroom");
        //리싸이클러뷰 넣을 LinearLayout 형태로 초기화 할 객체 가져오기.
        mLinearLayoutManager = new LinearLayoutManager(this);
        //리니어레이아웃 옵션 추가
        mLinearLayoutManager.setStackFromEnd(true);
        recyclerView = (RecyclerView)findViewById(R.id.chat_view);
        SendBtn = (Button)findViewById(R.id.sendBtn);
        SendTxt = (EditText)findViewById(R.id.send_txt);

        databaseReference
                .getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(room_type_1)) {
                    Log.e("Chat", "sendMessageToFirebaseUser: " + room_type_1 + " exists");
                    //databaseReference
                    //        .child(room_type_1)
                    //        .setValue("true");
                    room_name = room_type_1;
                } else if (dataSnapshot.hasChild(room_type_2)) {
                    Log.e("Chat", "sendMessageToFirebaseUser: " + room_type_2 + " exists");
                    //databaseReference
                    //        .child(room_type_2)
                    //        .setValue("true");
                    room_name = room_type_2;
                } else {
                    Log.e("Chat", "sendMessageToFirebaseUser: success");
                    //databaseReference
                    //        .child(room_type_1)
                    //        .setValue("true");
                    room_name = room_type_1;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //이 어댑터는 Firebase의 database를 지속적으로 감시하며 메시지가 추가 됐을 때, 혹은
        //액티비티가 처음 로딩 됐을 때, 메시지들을 레이아웃에 추가.
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Msg, ViewHolder>(Msg.class, R.layout.msg_item, ViewHolder.class,
                databaseReference.child(room_name)) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, Msg chatData, int position) {
                viewHolder.userTv.setText(chatData.getUser());
                viewHolder.msgTv.setText(chatData.getMSg());
                Log.d("message",databaseReference.child(room_name).getKey());
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

        SendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = SendTxt.getText().toString();
                if(!TextUtils.isEmpty(msg)){
                    //유저이름과 메세지로 chatData 만들기
                    Msg chatData = new Msg(first_uid,msg);
                    //기본 database 하위 message 라는 child chatData를 list로 만들기
                    databaseReference.child(room_name).push().setValue(chatData);
                    SendTxt.setText("");
                }

            }
        });

        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setAdapter(mFirebaseAdapter);

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
