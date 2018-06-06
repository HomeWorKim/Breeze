package bronzetrio.ojakgyo;

/**
 * Created by 명윤 on 2018-06-06.
 */

//메시지 객체.
public class Msg {
    private String user;
    private String msg;
    public Msg(){
        //기본 생성자.
    }
    public Msg(String user, String msg){
        this.user = user;
        this.msg = msg;
    }
    public String getUser(){
        return user;
    }
    public String getMSg(){
        return msg;
    }
}
