package bronzetrio.ojakgyo;

/**
 * Created by 명윤 on 2018-06-07.
 */

public class Chat_list {
    private int type;
    private String room_name;
    private String text;
    public Chat_list(){

    }
    public Chat_list(int type, String room_name, String text){
        this.type = type;
        this.room_name = room_name;
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }
}
