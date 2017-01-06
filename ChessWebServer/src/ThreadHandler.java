import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by lianyuzhe on 2017/1/3.
 */
public class ThreadHandler implements Runnable {
    private Socket firstSocket,secordSocket;
    public ThreadHandler(Socket firstSocket,Socket secordSocket){
        this.firstSocket=firstSocket;
        this.secordSocket=secordSocket;
    }
    public void run(){
        try{
            try{
                String transportString;
                InputStream firstInputStream=firstSocket.getInputStream();
                InputStream secondInputStream=secordSocket.getInputStream();
                OutputStream firstOutputStream=firstSocket.getOutputStream();
                OutputStream secondOutputStream=secordSocket.getOutputStream();
                Scanner firstIn=new Scanner(firstInputStream);
                Scanner secondIn=new Scanner(secondInputStream);
                PrintWriter firstOut=new PrintWriter(firstOutputStream,true);
                PrintWriter secondOut=new PrintWriter(secondOutputStream,true);
                boolean done=false;
                firstOut.println(SocketMessage.MATCH.getNumberMeaage());
                secondOut.println(SocketMessage.MATCH.getNumberMeaage());
                firstOut.println(SocketMessage.FIRST_RED.getNumberMeaage());
                secondOut.println(SocketMessage.SECOND_BLACK.getNumberMeaage());
                while(!done){
                        while (!firstIn.hasNextInt()){
                        }
                        transportString=firstIn.nextLine();
                        secondOut.println(transportString);
                        System.out.println("first:"+transportString);
                        if(transportString.equals("done")&&transportString.equals("win")){
                            done=true;
                            break;
                        }
                        while (!secondIn.hasNextInt()){

                        }
                        transportString=secondIn.nextLine();
                        firstOut.println(transportString);
                        System.out.println("second:"+transportString);
                    if(transportString.equals("done")&&transportString.equals("win")){
                        done=true;
                        break;
                    }
                }
            }finally {
                firstSocket.close();
                secordSocket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
