package bronzetrio.breeze.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.Inflater;

import bronzetrio.breeze.Chat_Room_v2;
import bronzetrio.breeze.Chat_list;
import bronzetrio.breeze.R;
import bronzetrio.breeze.fragments.dummy.DummyContent.DummyItem;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ChatFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;


    private DatabaseReference databaseReference;
    private ListView listview;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private LayoutInflater inflater;
    private ArrayAdapter Adapter;
    private ArrayList<String> a;
    private ArrayList<String> b;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ChatFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ChatFragment newInstance(int columnCount) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        a=new ArrayList<>();
        b = new ArrayList<>();
        //database 객체 가져오기.
        databaseReference = FirebaseDatabase.getInstance().getReference("chatroom");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        Map<String, Object> taskMap = new HashMap<String, Object>();
        taskMap.put("/token",1);
        databaseReference.updateChildren(taskMap);
        Map<String, Object> taskMap2 = new HashMap<String, Object>();
        taskMap2.put("/token",null);
        databaseReference.updateChildren(taskMap2);
        if(user.getUid().isEmpty()){
            return;
        }
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Log.d("chat : ",dataSnapshot1.getKey());
                    int idx = dataSnapshot1.getKey().indexOf("_");
                    String first = dataSnapshot1.getKey().substring(0,idx);
                    String second = dataSnapshot1.getKey().substring(idx+1,dataSnapshot1.getKey().length());
                    Log.d("chat : ",dataSnapshot1.getValue().toString());
                    if(user.getUid().equals(first)){
                        String tmp = first +"와"+second+"의 방";

                        a.add(tmp);
                    }else{
                        continue;
                    }
                }
                Log.d("a 사이즈3 : ",Integer.toString(a.size()));
                Adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, a);
                listview.setAdapter(Adapter);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                        adapterView.getItemAtPosition(pos);
                        int tmp = a.get(pos).indexOf("와");
                        int tmp2 = a.get(pos).indexOf("의");

                        String first = a.get(pos).substring(0,tmp);
                        String second = a.get(pos).substring(tmp+1,tmp2);
                            Intent intent = new Intent(getActivity(), Chat_Room_v2.class);
                            intent.putExtra("first_uid",first);
                            intent.putExtra("second_uid",second);
                            startActivity(intent);

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("data_error", "loadPost:onCancelled", databaseError.toException());

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        listview = (ListView) view.findViewById(R.id.list);


        Log.d("a 사이즈 : ",Integer.toString(a.size()));

        return view;
    }


//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnListFragmentInteractionListener) {
//            mListener = (OnListFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }

}
