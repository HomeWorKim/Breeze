package bronzetrio.breeze.fragments;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import bronzetrio.breeze.Chat_Room;
import bronzetrio.breeze.Login;
import bronzetrio.breeze.Open_Matching;
import bronzetrio.breeze.R;
import bronzetrio.breeze.Register;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FirebaseAuth mAuth;
    private Button logoutBtn;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatabaseReference databaseReference;
    private ImageView ranking1_img,ranking2_img,ranking3_img;
    private TextView ranking1_taltv,ranking2_taltv,ranking3_taltv;
    private TextView ranking1_simtv,ranking2_simtv,ranking3_simtv;
    private Bitmap bmp;
    ArrayList<Double> sim_arr;
    ArrayList<String> tal_name_arr;
    ArrayList<String> name_arr;
    ArrayList<String> major_arr;
    ArrayList<String> bitmap_arr;
    private OnFragmentInteractionListener mListener;
    String talent,sim,name,major;
    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sim_arr=new ArrayList<>();
        tal_name_arr=new ArrayList<>();
        name_arr=new ArrayList<>();
        major_arr=new ArrayList<>();
        bitmap_arr = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        logoutBtn = (Button) view.findViewById(R.id.logout);
        ranking1_img=(ImageView)view.findViewById(R.id.ranking1_img);
        ranking2_img=(ImageView)view.findViewById(R.id.ranking2_img);
        ranking3_img=(ImageView)view.findViewById(R.id.ranking3_img);
        ranking1_taltv=(TextView)view.findViewById(R.id.ranking1_taltv);
        ranking2_taltv=(TextView)view.findViewById(R.id.ranking2_taltv);
        ranking3_taltv=(TextView)view.findViewById(R.id.ranking3_taltv);
        ranking1_simtv=(TextView)view.findViewById(R.id.ranking1_simtv);
        ranking2_simtv=(TextView)view.findViewById(R.id.ranking2_simtv);
        ranking3_simtv=(TextView)view.findViewById(R.id.ranking3_simtv);
        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int i = v.getId();
                if(i == R.id.logout) {
                    mAuth.signOut();
                    Intent intent = new Intent(getActivity(), Login.class);
                    startActivity(intent);
                }

            }
        };
        logoutBtn.setOnClickListener(listener);

        databaseReference = FirebaseDatabase.getInstance().getReference("profile");

        Map<String, Object> taskMap = new HashMap<String, Object>();
        taskMap.put("/token", 1);
        databaseReference.updateChildren(taskMap);
        Map<String, Object> taskMap2 = new HashMap<String, Object>();
        taskMap2.put("/token", null);
        databaseReference.updateChildren(taskMap2);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){
                        Log.d("Profile:",dataSnapshot1.getKey());
                        try{
                            major = (String) dataSnapshot2.child("major").getValue();
                            name = (String) dataSnapshot2.child("name").getValue();
                            talent = (String) dataSnapshot2.child("talent").getValue();
                            sim = (String) dataSnapshot2.child("similarity").getValue();
                            String str_bmp = (String)dataSnapshot2.child("img").getValue();


                            sim_arr.add(Double.parseDouble(sim));
                            tal_name_arr.add(talent);
                            name_arr.add(name);
                            major_arr.add(major);
                            bitmap_arr.add(str_bmp);
                        }catch (NullPointerException e){

                        }
                    }

                    int r1=0,r2=0,r3=0;
                    double max =0.0;
                    ArrayList<Double> nstore = new ArrayList<Double>(sim_arr);
                    Collections.sort(nstore);
                    int[] indexes = new int[sim_arr.size()];
                    if(sim_arr.size()>=3) {
                        for (int n = 0; n < sim_arr.size(); n++) {
                            indexes[n] = nstore.indexOf(sim_arr.get(n));
                        }
                        for (int n = 0; n < sim_arr.size(); n++) {
                            Log.d("Profile","hello : "+indexes[n]);
                        }
                        ranking1_img.setImageBitmap(StringToBitMap(bitmap_arr.get(indexes[0])));
                        ranking1_taltv.setText(tal_name_arr.get(indexes[0]));
                        ranking1_simtv.setText(Double.toString(sim_arr.get(indexes[0])) + "% 동일");
                        ranking2_img.setImageBitmap(StringToBitMap(bitmap_arr.get(indexes[1])));
                        ranking2_taltv.setText(tal_name_arr.get(indexes[1]));
                        ranking2_simtv.setText(Double.toString(sim_arr.get(indexes[1])) + "% 동일");
                        ranking3_img.setImageBitmap(StringToBitMap(bitmap_arr.get(indexes[2])));
                        ranking3_taltv.setText(tal_name_arr.get(indexes[2]));
                        ranking3_simtv.setText(Double.toString(sim_arr.get(indexes[2])) + "% 동일");

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("data_error", "loadPost:onCancelled", databaseError.toException());

            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
}
