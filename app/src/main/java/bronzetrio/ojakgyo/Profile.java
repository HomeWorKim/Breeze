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

}
