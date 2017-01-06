import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by lianyuzhe on 2017/1/3.
 */
    public class webServer {
    private static final String name="com.mysql.jdbc.Driver";
    private static final String user="root";
    private static final String password="637631";
    private static final String url="jdbc:mysql://localhost:3306/ChineseChess?useUnicode=true&characterEncoding=utf-8&useSSL=false";
    private static Connection connection=null;
    private static PreparedStatement preparedStatement=null;
        public static void main(String[] args) throws IOException{
            HashMap<String,Socket> socketStringHashMap=new HashMap<>();
            ServerSocket serverSocket=new ServerSocket(10000);
            System.out.println("服务器启动..");
            while(true){
                Socket socket=serverSocket.accept();
                InputStream inputStream=socket.getInputStream();
                OutputStream outputStream=socket.getOutputStream();
                PrintWriter printWriter=new PrintWriter(outputStream,true);
                Scanner in=new Scanner(inputStream);
                String username=in.nextLine();
                String password=in.nextLine();
                boolean canConnect=connect();
                if(!canConnect){
                    printWriter.println(SocketMessage.NOT_CONNECTED.getNumberMeaage());
                    socket.close();
                    continue;
                }
                int searchMessage=searchUser(username,password);
                printWriter.println(searchMessage);
                if(searchMessage!=SocketMessage.SUCCESS.getNumberMeaage()){
                    socket.close();
                    continue;
                }
                String socketKey=in.nextLine();
                if(socketStringHashMap.containsKey(socketKey)){
                    Socket firstSocket=socketStringHashMap.get(socketKey);
                    socketStringHashMap.remove(socketKey);
                    ThreadHandler threadHandler=new ThreadHandler(firstSocket,socket);
                    Thread thread=new Thread(threadHandler);
                    thread.start();
                }else{
                    socketStringHashMap.put(socketKey,socket);
                }
            }
        }
        public static boolean connect(){
            try{
                Class.forName(name);
                connection= DriverManager.getConnection(url,user,password);
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
            return true;
        }
        public static int searchUser(String userName,String Password){
            try{
                String sql="SELECT * FROM chessuser WHERE USER="+"\""+userName+"\""+";";
                System.out.println(sql);
                preparedStatement=connection.prepareStatement(sql);
            }catch(SQLException e){
                e.printStackTrace();
                return SocketMessage.SEARCH_ERROR.getNumberMeaage();
            }
            try {
                ResultSet resultSet = preparedStatement.executeQuery();
                if(!resultSet.next()){
                    return SocketMessage.NOT_HAVE_USER.getNumberMeaage();
                }else{
                    String dataPassword=resultSet.getString("password");
                    if(dataPassword.equals(Password)){
                        return SocketMessage.SUCCESS.getNumberMeaage();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                return SocketMessage.SEARCH_ERROR.getNumberMeaage();
            }
            return SocketMessage.PASSWORD_ERROR.getNumberMeaage();
        }
}
