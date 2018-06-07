package bronzetrio.breeze.config;

import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark;

import java.util.ArrayList;

/**
 * Created by Seongho on 2018-06-08.
 */

public class common {
    public static double cosinesimilarity(ArrayList<Double> a1, ArrayList<Double> a2){
        double result;
        double top=0.0,bottom1=0.0,bottom2=0.0;
        if(a1.size()!=a2.size())
            return 1;
        if(a1.size()<5)
            return 1;
        int cnt = 0;
        for(int i=0;i<a1.size();i++){
            if(a1.get(i)!=0&&a2.get(i)!=0) {
                top += Math.abs(a1.get(i) * a2.get(i));
                bottom1 += a1.get(i) * a1.get(i);
                bottom2 += a2.get(i) * a2.get(i);
                cnt++;
            }
        }
        result = top/(Math.pow(bottom1,0.5)*Math.pow(bottom2,0.5));

        return result;
    }

    public static ArrayList<Double> getFeature(FirebaseVisionFace face){
        ArrayList<Double> feature = new ArrayList<>();
        ArrayList<Float> featureX = new ArrayList<>();
        ArrayList<Float> featureY = new ArrayList<>();
        FirebaseVisionFaceLandmark leftEar = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EAR);
        FirebaseVisionFaceLandmark rightEar = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EAR);
        FirebaseVisionFaceLandmark bottomMouth = face.getLandmark(FirebaseVisionFaceLandmark.BOTTOM_MOUTH);
        FirebaseVisionFaceLandmark leftCheek = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_CHEEK);
        FirebaseVisionFaceLandmark rightCheek = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_CHEEK);
        FirebaseVisionFaceLandmark leftEye = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE);
        FirebaseVisionFaceLandmark rightEye = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EYE);
        FirebaseVisionFaceLandmark leftMouth = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_MOUTH);
        FirebaseVisionFaceLandmark rightMouth = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_MOUTH);
        FirebaseVisionFaceLandmark noseBase = face.getLandmark(FirebaseVisionFaceLandmark.NOSE_BASE);

        if(leftEar!=null) {
            featureX.add(leftEar.getPosition().getX());
            featureY.add(leftEar.getPosition().getY());
        }else{
            featureX.add((float)0.0);
            featureY.add((float)0.0);
        }
        if(rightEar!=null) {
            featureX.add(rightEar.getPosition().getX());
            featureY.add(rightEar.getPosition().getY());
        }else{
            featureX.add((float)0.0);
            featureY.add((float)0.0);
        }
        if(bottomMouth!=null) {
            featureX.add(bottomMouth.getPosition().getX());
            featureY.add(bottomMouth.getPosition().getY());
        }else{
            featureX.add((float)0.0);
            featureY.add((float)0.0);
        }
        if(leftCheek!=null) {
            featureX.add(leftCheek.getPosition().getX());
            featureY.add(leftCheek.getPosition().getY());
        }else{
            featureX.add((float)0.0);
            featureY.add((float)0.0);
        }
        if(rightCheek!=null) {
            featureX.add(rightCheek.getPosition().getX());
            featureY.add(rightCheek.getPosition().getY());
        }else{
            featureX.add((float)0.0);
            featureY.add((float)0.0);
        }
        if(leftEye!=null) {
            featureX.add(leftEye.getPosition().getX());
            featureY.add(leftEye.getPosition().getY());
        }else{
            featureX.add((float)0.0);
            featureY.add((float)0.0);
        }
        if(rightEye!=null) {
            featureX.add(rightEye.getPosition().getX());
            featureY.add(rightEye.getPosition().getY());
        }else{
            featureX.add((float)0.0);
            featureY.add((float)0.0);
        }
        if(leftMouth!=null) {
            featureX.add(leftMouth.getPosition().getX());
            featureY.add(leftMouth.getPosition().getY());
        }else{
            featureX.add((float)0.0);
            featureY.add((float)0.0);
        }
        if(rightMouth!=null) {
            featureX.add(rightMouth.getPosition().getX());
            featureY.add(rightMouth.getPosition().getY());
        }else{
            featureX.add((float)0.0);
            featureY.add((float)0.0);
        }
        if(noseBase!=null) {
            featureX.add(noseBase.getPosition().getX());
            featureY.add(noseBase.getPosition().getY());
        }else{
            featureX.add((float)0.0);
            featureY.add((float)0.0);
        }

        for(int i=0;i<10;i++){
            for(int j=i;j<10;j++){
                feature.add(Math.pow((double)(featureX.get(i)-featureX.get(j)),2)+Math.pow((double)(featureY.get(i)-featureY.get(j)),2));
            }
        }

        return feature;
    }
}
