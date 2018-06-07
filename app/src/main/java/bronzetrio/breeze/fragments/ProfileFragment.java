package bronzetrio.breeze.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import bronzetrio.breeze.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView Id;
    private TextView Name;
    private TextView Birthday;
    private TextView Sex;
    private TextView Major;
    private TextView Hobby;
    private ImageView profile_img;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private DataSnapshot snapshot;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Bitmap bmp;
    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        //database 객체 가져오기.


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = mAuth.getCurrentUser();
                //메일 나누기.
                if(user == null){
                    Id.setText("You must login first!");
                    return;
                }
                String email = mAuth.getCurrentUser().getEmail();
                int first_idx = email.indexOf("@");
                final String first = email.substring(0,first_idx);
                String tmp = email.substring(first_idx+1,email.length());

                int second_idx = tmp.indexOf(".");
                final String second = tmp.substring(0,second_idx);
                final String last = tmp.substring(second_idx+1,tmp.length());

                databaseReference = FirebaseDatabase.getInstance().getReference("profile/"+second+"/"+last+"/"+first);
                Log.d("tag", "profile/"+second+"/"+last);
                Map<String, Object> taskMap = new HashMap<String, Object>();
                taskMap.put("/token",1);
                databaseReference.updateChildren(taskMap);
                Map<String, Object> taskMap2 = new HashMap<String, Object>();
                taskMap2.put("/token",null);
                databaseReference.updateChildren(taskMap2);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Profile tmp = dataSnapshot.getValue(Profile.class);
                        //Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                        //String a = (String) map.get("day");
                        //String b = (String) map.get("month");
                        //textView.append(b + " -- " + a + "\n");
                        String a="",b="",c="",d="",e="",f="",g="";

                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            a = (String) dataSnapshot1.child("day").getValue();
                            b = (String) dataSnapshot1.child("month").getValue();
                            c = (String) dataSnapshot1.child("year").getValue();
                            d = (String) dataSnapshot1.child("name").getValue();
                            e = (String) dataSnapshot1.child("sex").getValue();
                            f = (String) dataSnapshot1.child("major").getValue();
                            g = (String) dataSnapshot1.child("hobby").getValue();
                            String str_bmp = (String)dataSnapshot1.child("img").getValue();
                            bmp = StringToBitMap(str_bmp);
                        }
                        String birth = c+"-"+b+"-"+a;
                        Id.setText(mAuth.getCurrentUser().getEmail());
                        Name.setText(d);
                        Birthday.setText(birth);
                        profile_img.setImageBitmap(bmp);
                        Sex.setText(e);
                        Major.setText(f);
                        Hobby.setText(g);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("data_error", "loadPost:onCancelled", databaseError.toException());

                    }
                });

                //Log.d("data",databaseReference.child("profile/"+second+"/"+last+"/"+first).getKey());
                //snapshot = databaseReference.child("profile/"+second+"/"+last+"/"+first);
                if (user != null) {
                    // User is signed in
                    Id.setText("123");
                    Log.d("tag", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Id.setText("없음");
                    Log.d("tag", "onAuthStateChanged:signed_out");
                }
            }
        };

    }

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Id = (TextView)view.findViewById(R.id.Id);
        Name = (TextView)view.findViewById(R.id.name);
        Birthday = (TextView)view.findViewById(R.id.Birthday);
        profile_img = (ImageView)view.findViewById(R.id.profile_img);
        Sex = (TextView)view.findViewById(R.id.sex);
        Major = (TextView)view.findViewById(R.id.major);
        Hobby = (TextView)view.findViewById(R.id.hobby);
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
}
