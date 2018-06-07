package bronzetrio.breeze;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Chat_Room_v2 extends AppCompatActivity {
    private String first_uid;
    private String second_uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat__room_v2);

        Intent intent = getIntent();
        first_uid =intent.getExtras().getString("first_uid");
        second_uid =intent.getExtras().getString("second_uid");


    }
    public void sendMessageToFirebaseUser(final Context context,
                                          final Chat chat,
                                          final String receiverFirebaseToken) {
        final String room_type_1 = chat.senderUid + "_" + chat.receiverUid;
        final String room_type_2 = chat.receiverUid + "_" + chat.senderUid;

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference();

        databaseReference.child(Constants.ARG_CHAT_ROOMS)
                .getRef()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(room_type_1)) {
                            Log.e("Tag", "sendMessageToFirebaseUser: " + room_type_1 + " exists");
                            databaseReference.child(Constants.ARG_CHAT_ROOMS)
                                    .child(room_type_1)
                                    .child(String.valueOf(chat.timestamp))
                                    .setValue(chat);
                        } else if (dataSnapshot.hasChild(room_type_2)) {
                            Log.e("Tag", "sendMessageToFirebaseUser: " + room_type_2 + " exists");
                            databaseReference.child(Constants.ARG_CHAT_ROOMS)
                                    .child(room_type_2)
                                    .child(String.valueOf(chat.timestamp))
                                    .setValue(chat);
                        } else {
                            Log.e("Tag", "sendMessageToFirebaseUser: success");
                            databaseReference.child(Constants.ARG_CHAT_ROOMS)
                                    .child(room_type_1)
                                    .child(String.valueOf(chat.timestamp))
                                    .setValue(chat);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Unable to send message.
                    }
                });
    }
}
