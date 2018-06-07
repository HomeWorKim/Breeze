package bronzetrio.breeze.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<Chat_list, ViewHolder2> mFirebaseAdapter;
    private DatabaseReference databaseReference;
    private LinearLayoutManager mLinearLayoutManager;
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

        //database 객체 가져오기.
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //이 어댑터는 Firebase의 database를 지속적으로 감시하며 메시지가 추가 됐을 때, 혹은
        //액티비티가 처음 로딩 됐을 때, 메시지들을 레이아웃에 추가.
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Chat_list, ViewHolder2>(Chat_list.class, R.layout.fragment_chat, ViewHolder2.class, databaseReference.child("sample")) {
            @Override
            protected void populateViewHolder(ViewHolder2 viewHolder, Chat_list chatData, int position) {
                //viewHolder.imgv.setImageBitmap(null);

                Log.d("귀찮아 : ",chatData.getText()+"    "+chatData.getRoom_name());
                viewHolder.userTv.setText(chatData.getText());
                viewHolder.msgTv.setText(chatData.getRoom_name());

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

    }
    public static class ViewHolder2 extends RecyclerView.ViewHolder{
        //뷰홀더 패턴에 사용할 ViewHolder
        TextView msgTv;
        TextView userTv;
        ImageView imgv;
        public ViewHolder2(View v){
            super(v);
            imgv = (ImageView)v.findViewById(R.id.setting);
            msgTv = (TextView)v.findViewById(R.id.room_name);
            userTv = (TextView)v.findViewById(R.id.text);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        recyclerView = (RecyclerView)view;
        mLinearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setAdapter(mFirebaseAdapter);
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
