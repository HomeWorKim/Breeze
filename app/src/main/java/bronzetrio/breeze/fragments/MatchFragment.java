package bronzetrio.breeze.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import bronzetrio.breeze.Chat_Room;
import bronzetrio.breeze.Login;
import bronzetrio.breeze.MainActivity;
import bronzetrio.breeze.Open_Matching;
import bronzetrio.breeze.R;
import bronzetrio.breeze.Register;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

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
    private Button public_matching_btn;
    private Button private_matching_btn;
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
                   // txt.setText(mAuth.getCurrentUser().getEmail());
                    Log.d("tag", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    //.setText("없음");
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


        public_matching_btn = (Button)view.findViewById(R.id.public_matching);
        private_matching_btn=(Button)view.findViewById(R.id.private_matching);

        public_matching_btn.setAllCaps(false);
        private_matching_btn.setAllCaps(false);
        String text1="공개 매칭\n";
        String text2="-내가 좋아하는 연애인 닮은 사람 만나기-";
        SpannableString str = new SpannableString(text1+ text2);

        str.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.textSize1)), 0, text1.length(),  Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.textSize2)), text1.length(), text1.length()+text2.length(),  Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        public_matching_btn.setText(str);

        text1="비공개 매칭\n";
        text2="-누구와 만날지 아무도 몰라요-";

        str = new SpannableString(text1+ text2);

        str.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.textSize1)), 0, text1.length(),  Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.textSize2)), text1.length(), text1.length()+text2.length(),  Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        private_matching_btn.setText(str);

        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int i = v.getId();
                if(i == R.id.public_matching){
                    Intent intent = new Intent(getActivity(), Open_Matching.class);
                    startActivity(intent);
                }else if(i==R.id.private_matching){
                    Toast.makeText(getContext(),"Not Support",Toast.LENGTH_LONG).show();
                    //Intent intent = new Intent(getActivity(), Open_Matching.class);
                    //startActivity(intent);
                }
            }
        };

        public_matching_btn.setOnClickListener(listener);
        private_matching_btn.setOnClickListener(listener);
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
