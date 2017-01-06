/**
 * Created by lianyuzhe on 2017/1/5.
 */
public enum  SocketMessage {
    NOT_CONNECTED(1),NOT_HAVE_USER(2),PASSWORD_ERROR(3),SUCCESS(4),SEARCH_ERROR(5),MATCH(6),FIRST_RED(7),SECOND_BLACK(8);
    private int  NumberMeaage;
    private SocketMessage(int numberMeaage){
        NumberMeaage=numberMeaage;
    }
    public int getNumberMeaage(){
        return NumberMeaage;
    }
}
