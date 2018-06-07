package bronzetrio.ojakgyo;

import android.graphics.Bitmap;

/**
 * Created by 명윤 on 2018-06-07.
 */

public class Profile {
    String name;
    String year;
    String month;
    String day;
    Bitmap img;
    public Profile(){

    }
    public Profile(String name, String year, String month, String day, Bitmap img){
        this.name = name;
        this.year = year;
        this.month = month;
        this.day = day;
        this.img = img;
    }
    public String getName(){
        return name;
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
    public Bitmap getImg(){
        return img;
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

    public void setImg(Bitmap img) {

        this.img = img;
    }

    public void setDay(String day) {

        this.day = day;
    }
}
