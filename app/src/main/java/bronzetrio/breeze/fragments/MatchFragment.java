package bronzetrio.breeze.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import bronzetrio.breeze.Chat_Room;
import bronzetrio.breeze.Login;
import bronzetrio.breeze.MainActivity;
import bronzetrio.breeze.Open_Matching;
import bronzetrio.breeze.R;
import bronzetrio.breeze.Register;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MatchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MatchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MatchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button register_btn;
    private Button login_btn;
    private Button chat_btn;
    private Button open_matching_btn;
    private TextView txt;

    private OnFragmentInteractionListener mListener;

    public MatchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MatchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MatchFragment newInstance(String param1, String param2) {
        MatchFragment fragment = new MatchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        // ...
        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = mAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    txt.setText(mAuth.getCurrentUser().getEmail());
                    Log.d("tag", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    txt.setText("없음");
                    Log.d("tag", "onAuthStateChanged:signed_out");
                }
            }
        };

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_match, container, false);

        register_btn = (Button)view.findViewById(R.id.register);
        login_btn = (Button)view.findViewById(R.id.login);
        chat_btn = (Button)view.findViewById(R.id.chatroom);
        open_matching_btn = (Button)view.findViewById(R.id.open_matching);
        txt = (TextView)view.findViewById(R.id.mat);

        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int i = v.getId();
                if(i == R.id.register) {
                    Intent intent = new Intent(getActivity(), Register.class);
                    startActivity(intent);
                }
                else if(i == R.id.login){
                    Intent intent = new Intent(getActivity(), Login.class);
                    startActivity(intent);
                }
                else if(i == R.id.chatroom){
                    Intent intent = new Intent(getActivity(), Chat_Room.class);
                    startActivity(intent);
                }
                else if(i == R.id.open_matching){
                    Intent intent = new Intent(getActivity(), Open_Matching.class);
                    startActivity(intent);
                }
            }
        };
        register_btn.setOnClickListener(listener);
        login_btn.setOnClickListener(listener);
        chat_btn.setOnClickListener(listener);
        open_matching_btn.setOnClickListener(listener);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
@Override
public void onStart() {
    super.onStart();
    mAuth.addAuthStateListener(mAuthListener);
}
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}