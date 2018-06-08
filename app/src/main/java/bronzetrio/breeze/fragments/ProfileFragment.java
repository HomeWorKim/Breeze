package bronzetrio.breeze.fragments;


import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.common.FirebaseVisionPoint;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bronzetrio.breeze.Profile;
import bronzetrio.breeze.R;
import bronzetrio.breeze.config.common;

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
    private static final String TAG = "ProfileFragment";
    private static final int PICTURE_REQUEST_CODE = 100;
    public static final int RESULT_OK           = -1;

    private ArrayList<String> refer_vector,refer_vector2;
    private ArrayList<Double> test_vector;
    private ArrayList<ArrayList<Double>> refer_vector_feature,refer_vector_feature2;
    FirebaseVisionImage image;

    private TextView Id;
    private TextView Name;
    private TextView Birthday;
    private TextView Sex;
    private TextView Major;
    private TextView Hobby;
    private TextView Talent;
    private ImageView profile_img;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private DataSnapshot snapshot;
    ImageView testPicture;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Bitmap bmp;
    private OnFragmentInteractionListener mListener;

    private Button photochange_btn;
    private FirebaseVisionFaceDetectorOptions options;
    String a="",b="",c="",d="",e="",f="",g="",h="";
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
        test_vector = new ArrayList<>();
        refer_vector = new ArrayList<>();
        refer_vector2 = new ArrayList<>();
        refer_vector_feature = new ArrayList<>();
        refer_vector_feature2 = new ArrayList<>();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        options =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setModeType(FirebaseVisionFaceDetectorOptions.FAST_MODE)
                        .setLandmarkType(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                        .setClassificationType(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                        .setMinFaceSize(0.15f)
                        .setTrackingEnabled(true)
                        .build();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = mAuth.getCurrentUser();
                if(user == null){
                    Id.setText("You must login first!");
                    return;
                }
                //메일 나누기.
                String uid = mAuth.getCurrentUser().getUid();
                databaseReference = FirebaseDatabase.getInstance().getReference("feature");
                Map<String, Object> taskMap3 = new HashMap<String, Object>();
                taskMap3.put("/token",1);
                databaseReference.updateChildren(taskMap3);
                Map<String, Object> taskMap4 = new HashMap<String, Object>();
                taskMap4.put("/token",null);
                databaseReference.updateChildren(taskMap4);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        refer_vector = new ArrayList<>();
                        refer_vector2 = new ArrayList<>();
                        refer_vector_feature = new ArrayList<>();
                        refer_vector_feature2 = new ArrayList<>();
                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            //Log.d("Profile:",dataSnapshot1.getKey());
                            if(dataSnapshot1.child("gender").getValue().toString().equals("1")){
                                refer_vector.add(dataSnapshot1.getKey().toString());

                                for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){
                                    if(dataSnapshot2.getValue().toString().equals("1")||dataSnapshot2.getValue().toString().equals("2")){

                                    }else{
                                        GenericTypeIndicator<ArrayList<Double>> t = new GenericTypeIndicator<ArrayList<Double>>() {};
                                        refer_vector_feature.add(new ArrayList<Double>(dataSnapshot2.getValue(t)));
                                    }
                                }
                            }else{
                                refer_vector2.add(dataSnapshot1.getKey().toString());
                                for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){
                                    if(dataSnapshot2.getValue().toString().equals("1")||dataSnapshot2.getValue().toString().equals("2")){

                                    }else{
                                        GenericTypeIndicator<ArrayList<Double>> t = new GenericTypeIndicator<ArrayList<Double>>() {};
                                        refer_vector_feature2.add(new ArrayList<Double>(dataSnapshot2.getValue(t)));
                                    }
                                }
                            }
                        }
                        Log.d("Profile:","test_vector_size is : "+refer_vector_feature.size());
                        Log.d("Profile:","test_vector2_size is : "+refer_vector_feature2.size());
                        //Log.d("Profile:","test_vector_size is : "+refer_vector_feature.get(2).size());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("data_error", "loadPost:onCancelled", databaseError.toException());

                    }
                });

                databaseReference = FirebaseDatabase.getInstance().getReference("profile/"+uid);
                //Log.d("tag", "profile/"+second+"/"+last);
                Map<String, Object> taskMap = new HashMap<String, Object>();
                taskMap.put("/token",1);
                databaseReference.updateChildren(taskMap);
                Map<String, Object> taskMap2 = new HashMap<String, Object>();
                taskMap2.put("/token",null);
                databaseReference.updateChildren(taskMap2);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Log.d("Profile:",dataSnapshot1.getKey());
                            a = (String) dataSnapshot1.child("day").getValue();
                            b = (String) dataSnapshot1.child("month").getValue();
                            c = (String) dataSnapshot1.child("year").getValue();
                            d = (String) dataSnapshot1.child("name").getValue();
                            e = (String) dataSnapshot1.child("sex").getValue();
                            f = (String) dataSnapshot1.child("major").getValue();
                            g = (String) dataSnapshot1.child("hobby").getValue();
                            h = (String) dataSnapshot1.child("talent").getValue();
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
                        Talent.setText(h);
                        setTestPicture();
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
                  if(user == null){
                    Id.setText("You must login first!");
                    return;
                  }
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
        photochange_btn = (Button)view.findViewById(R.id.btn_photochange);

        testPicture = (ImageView)view.findViewById(R.id.testPicture);
        Id = (TextView)view.findViewById(R.id.Id);
        Name = (TextView)view.findViewById(R.id.name);
        Birthday = (TextView)view.findViewById(R.id.Birthday);
        profile_img = (ImageView)view.findViewById(R.id.profile_img);
        Sex = (TextView)view.findViewById(R.id.sex);
        Major = (TextView)view.findViewById(R.id.major);
        Hobby = (TextView)view.findViewById(R.id.hobby);
        Talent = (TextView)view.findViewById(R.id.talent);

        photochange_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,false);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICTURE_REQUEST_CODE);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == PICTURE_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                //기존 이미지 지우기.
                profile_img.setImageResource(0);
                Uri uri = data.getData();
                ClipData clipData = data.getClipData();

                //구글 드라이브같은데서 가져오는 걸로 예상됨.
                if(clipData != null){
                    try{
                        bmp = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                    }catch(FileNotFoundException e){
                        e.printStackTrace();
                    }catch(IOException e){
                        e.printStackTrace();
                    }

                    //profile_img.setImageBitmap(bmp);
                }
                //사진 폴더에서 선택할때.
                else if(uri != null){
                    try{
                        bmp = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                    }catch(FileNotFoundException e){
                        e.printStackTrace();
                    }catch(IOException e){
                        e.printStackTrace();
                    }

                    
                    profile_img.setImageBitmap(bmp);
                }
            }
            //사진 선택 안했을때?
            else{
                Toast.makeText(getContext(),"사진선택실패",Toast.LENGTH_SHORT).show();
            }
            Log.d(TAG,"hellp1");
            if(bmp.getHeight()<bmp.getWidth()){
                Log.d(TAG,"hellp2");
                bmp = imgRotate(bmp);

            }
            image = FirebaseVisionImage.fromBitmap(bmp);
            final FirebaseVisionFaceDetector detector = FirebaseVision.getInstance().getVisionFaceDetector(options);

        final Task<List<FirebaseVisionFace>> result =
                detector.detectInImage(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<FirebaseVisionFace>>() {
                                    @Override
                                    public void onSuccess(List<FirebaseVisionFace> faces) {
                                        Log.d(TAG,"success_detect");

                                        Log.d(TAG,"number : "+faces.size());
                                        testPicture.setImageBitmap(bmp);


                                        if(faces.size()==1){
                                            for (FirebaseVisionFace face : faces) {

                                                Rect bounds = face.getBoundingBox();
                                                float rotY = face.getHeadEulerAngleY();  // Head is rotated to the right rotY degrees
                                                float rotZ = face.getHeadEulerAngleZ();  // Head is tilted sideways rotZ degrees

                                                // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
                                                // nose available):
                                                //Log.e(TAG,"hello "+face.getLandmark(FirebaseVisionFaceLandmark.NOSE_BASE));
                                                test_vector = common.getFeature(face);


                                                FirebaseVisionFaceLandmark leftEar = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EAR);
                                                if (leftEar != null) {

                                                    FirebaseVisionPoint leftEarPos = leftEar.getPosition();
                                                }

                                                // If classification was enabled:
                                                if (face.getSmilingProbability() != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                                                    float smileProb = face.getSmilingProbability();
                                                }
                                                if (face.getRightEyeOpenProbability() != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                                                    float rightEyeOpenProb = face.getRightEyeOpenProbability();
                                                }

                                                // If face tracking was enabled:
                                                if (face.getTrackingId() != FirebaseVisionFace.INVALID_ID) {
                                                    int id = face.getTrackingId();
                                                }
                                                Log.d(TAG,"detected box"+rotY+","+rotZ);
                                                ArrayList<Double> similarity_arrayList = new ArrayList<>();
                                                double max = 0.0;
                                                String name ="";
                                                Log.d(TAG,"Gender is "+e);
                                                if(e.equals("남성")){

                                                    for(ArrayList<Double> arrayList:refer_vector_feature){

                                                        similarity_arrayList.add(common.cosinesimilarity(arrayList,test_vector));
                                                        Log.d(TAG,"similarity score is "+common.cosinesimilarity(arrayList,test_vector));
                                                    }

                                                    for(int i=0;i<similarity_arrayList.size();i++){
                                                        if(max<similarity_arrayList.get(i)) {
                                                            max = similarity_arrayList.get(i);
                                                            name = refer_vector.get(i);
                                                        }
                                                    }

                                                }else{
                                                    for(ArrayList<Double> arrayList:refer_vector_feature2){
                                                        similarity_arrayList.add(common.cosinesimilarity(arrayList,test_vector));
                                                    }

                                                    for(int i=0;i<similarity_arrayList.size();i++){
                                                        Log.d("similarity check","Check : " + similarity_arrayList.get(i) );
                                                        if(max<similarity_arrayList.get(i)) {

                                                            max = similarity_arrayList.get(i);
                                                            name = refer_vector2.get(i);
                                                        }
                                                    }
                                                }

                                                profile_img.setImageBitmap(bmp);
                                                Map<String, Object> taskMap = new HashMap<>();
                                                max = 100-(10000-10000*max)*8;
                                                max = max<0?0.0:max;
                                                taskMap.put("day",a);
                                                taskMap.put("hobby",b);
                                                taskMap.put("img",BitMapToString(bmp));
                                                taskMap.put("major",f);
                                                taskMap.put("month",b);
                                                taskMap.put("name",d);
                                                taskMap.put("sex",e);
                                                taskMap.put("year",c);
                                                taskMap.put("talent",name);
                                                taskMap.put("similarity",Double.toString(max));
                                                databaseReference.getRef().removeValue();
                                                databaseReference.push().updateChildren(taskMap);
                                            }
                                        }else{
                                            Log.e(TAG,"detect number over : "+faces.size());
                                            Toast.makeText(getContext(),"사진에 자기 자신만 나와야합니다.",Toast.LENGTH_LONG).show();
                                        }

                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG,"failure_detect");
                                        // Task failed with an exception
                                        // ...
                                        Toast.makeText(getContext(),"사진에 얼굴 검출이 실패하였습니다..",Toast.LENGTH_LONG).show();
                                    }
                                });

        }
    }

    private Bitmap imgRotate(Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        Matrix matrix = new Matrix();
        matrix.postRotate(270);

        Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
        bmp.recycle();

        return resizedBitmap;
    }

    //데이터베이스에 데이터 넣기.
    public boolean UpdateData(String email, String Name, String Year, String Month, String Day, String sex,Bitmap Img){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String major = Major.getText().toString();
        String hobby = Hobby.getText().toString();
        //Log.d("string2",second[0]+"  "+second[1]+"   ");
        String str_Img = BitMapToString(Img);
        Profile profile = new Profile(Name, Year, Month, Day, str_Img, sex,major,hobby, "");
        Log.d("string",Name+"  "+Name+"  "+Year+"   "+Month+"  "+Day);
        databaseReference.child("profile/"+currentUser.getUid()).push().setValue(profile);

        return true;
    }

    //Bitmap 을 String 형태로 변환.
    public String BitMapToString(Bitmap bitmap){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        int dstWidth = bitmap.getWidth()/4;
        int dstHeight = bitmap.getHeight()/4;
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeight, true);

        ByteArrayOutputStream ByteStream=new  ByteArrayOutputStream();
        resized.compress(Bitmap.CompressFormat.PNG,20, ByteStream);
        byte [] b=ByteStream.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    /**
     * Get the angle by which an image must be rotated given the device's current
     * orientation.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private int getRotationCompensation(String cameraId, Activity activity, Context context)
            throws CameraAccessException {
        // Get the device's current rotation relative to its "native" orientation.
        // Then, from the ORIENTATIONS table, look up the angle the image must be
        // rotated to compensate for the device's rotation.
        int deviceRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int rotationCompensation = ORIENTATIONS.get(deviceRotation);

        // On most devices, the sensor orientation is 90 degrees, but for some
        // devices it is 270 degrees. For devices with a sensor orientation of
        // 270, rotate the image an additional 180 ((270 + 270) % 360) degrees.
        CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        int sensorOrientation = cameraManager
                .getCameraCharacteristics(cameraId)
                .get(CameraCharacteristics.SENSOR_ORIENTATION);
        rotationCompensation = (rotationCompensation + sensorOrientation + 270) % 360;

        // Return the corresponding FirebaseVisionImageMetadata rotation value.
        int result;
        switch (rotationCompensation) {
            case 0:
                result = FirebaseVisionImageMetadata.ROTATION_0;
                break;
            case 90:
                result = FirebaseVisionImageMetadata.ROTATION_90;
                break;
            case 180:
                result = FirebaseVisionImageMetadata.ROTATION_180;
                break;
            case 270:
                result = FirebaseVisionImageMetadata.ROTATION_270;
                break;
            default:
                result = FirebaseVisionImageMetadata.ROTATION_0;
                Log.e(TAG, "Bad rotation value: " + rotationCompensation);
        }
        return result;
    }

    // 이미지 회전 함수
    public Bitmap rotateImage(Bitmap src, float degree) {

        // Matrix 객체 생성
        Matrix matrix = new Matrix();
        // 회전 각도 셋팅
        matrix.postRotate(degree);
        // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),src.getHeight(), matrix, true);
    }

    public void setTestPicture(){
        String name = Talent.getText().toString();
        if(name.equals("gdragon")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.gd1));
        }else if(name.equals("gdragon1")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.gd1));
        }else if(name.equals("hyuna")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.hyuna));
        }else if(name.equals("hyuna2")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.hyuna));
        }else if(name.equals("irin")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.irin1));
        }else if(name.equals("irin2")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.irin1));
        }else if(name.equals("iu")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.iu1));
        }else if(name.equals("iu2")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.iu1));
        }else if(name.equals("kangdaniel")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.kangdaniel));
        }else if(name.equals("kangdaniel2")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.kangdaniel));
        }else if(name.equals("kimgoeun")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.kimgoeun1));
        }else if(name.equals("kimsuhyun")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.kimsuhyun1));
        }else if(name.equals("kimsuhyun2")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.kimsuhyun1));
        }else if(name.equals("kimwoobin")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.kimwoobin1));
        }else if(name.equals("kimwoobin2")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.kimwoobin1));
        }else if(name.equals("kyungri")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.kyungri));
        }else if(name.equals("kyungri2")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.kyungri));
        }else if(name.equals("onebin")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.onebin));
        }else if(name.equals("onebin2")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.onebin));
        }else if(name.equals("parkbogum")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.parkbogum));
        }else if(name.equals("parkbogum2")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.parkbogum));
        }else if(name.equals("seulgi")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.seulgi));
        }else if(name.equals("seulgi2")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.seulgi));
        }else if(name.equals("songjihyo")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.songjihyo));
        }else if(name.equals("songjihyo2")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.songjihyo));
        }else if(name.equals("songjungki")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.songjki1));
        }else if(name.equals("songjungki2")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.songjki1));
        }else if(name.equals("taeyang")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.taeyang));
        }else if(name.equals("taeyang2")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.taeyang));
        }else if(name.equals("v")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.vy));
        }else if(name.equals("v2")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.vy));
        }else if(name.equals("yoona")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.yoona));
        }else if(name.equals("zwu")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.zzw1));
        }else if(name.equals("zwu2")){
            testPicture.setImageDrawable(getResources().getDrawable(R.drawable.zzw1));
        }


    }

}
