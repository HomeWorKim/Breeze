package bronzetrio.breeze;

import android.graphics.Bitmap;

/**
 * Created by 명윤 on 2018-06-07.
 */

public class Profile {
    String name;
    String year;
    String month;
    String day;
    String img;
    String sex;
    String major;
    String hobby;
    public Profile(){

    }
    public Profile(String name, String year, String month, String day, String img,String sex,String major, String hobby){
        this.name = name;
        this.year = year;
        this.month = month;
        this.day = day;
        this.img = img;
        this.sex = sex;
        this.major = major;
        this.hobby = hobby;
    }
    public String getName(){
        return name;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getYear(){
        return year;
    }
    public String getMonth(){
        return month;
    }
    public String getDay(){
        return day;
    }
    public String getImg(){
        return img;
    }
    public String getSex(){
        return sex;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setImg(String img) {

        this.img = img;
    }

    public void setDay(String day) {

        this.day = day;
    }
    public void setSex(String sex) {

        this.sex = sex;
    }
}
