import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.server.ExportException;
import java.util.Scanner;

/**
 * Created by lianyuzhe on 2017/1/4.
 */
public class LoadPage extends JFrame {
    private static  final String host="192.168.31.211";
    private static final int port=10000;
    private static final int DEFAULT_HEIGHT=200;
    private static final int DEFAULT_WIDTH=400;
    private JTextField userTextField;
    private JPasswordField passwordField;
    private String userName;
    private String password;
    private JPanel submitPanel;
    private JPanel userPasswordJpanel;
        public LoadPage(){
            setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
            this.setLayout(new BorderLayout());
            userPasswordJpanel=new JPanel();
            userPasswordJpanel.setLayout(new GridLayout(2,2));
            userTextField=new JTextField();
            passwordField=new JPasswordField();
            userPasswordJpanel.add(new JLabel("User name:",SwingConstants.RIGHT));
            userPasswordJpanel.add(userTextField);
            userPasswordJpanel.add(new JLabel("Password:",SwingConstants.RIGHT));
            userPasswordJpanel.add(passwordField);
            add(userPasswordJpanel,BorderLayout.NORTH);
            submitPanel=new JPanel();
            JButton submitButton=new JButton();
            submitButton.setText("登入");
            submitPanel.add(submitButton);
            this.add(submitPanel,BorderLayout.SOUTH);
            submitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    userName=userTextField.getText();
                    password=new String(passwordField.getPassword());
                    if(userName.equals("")||password.equals("")){
                        JOptionPane.showMessageDialog(LoadPage.this, "请填写正确用户名和密码信息", "中国象棋登入界面", JOptionPane.ERROR_MESSAGE, null);
                        userTextField.setText("");
                        passwordField.setText("");
                        return;
                    }
                    try {
                        Socket mySocket=new Socket(host,port);
                        OutputStream outputStream=mySocket.getOutputStream();
                        PrintWriter printWriter=new PrintWriter(outputStream,true);
                        printWriter.println(userName);
                        printWriter.println(password);
                        InputStream inputStream = mySocket.getInputStream();
                        Scanner scanner=new Scanner(inputStream);
                        int returnNumber=scanner.nextInt();
                        if(returnNumber==SocketMessage.SUCCESS.getNumberMeaage()){
                            LoadPage.this.setVisible(true);
                            String key;
                            do {
                                key = JOptionPane.showInputDialog(LoadPage.this, "请输入对战匹配号", "匹配号填写", JOptionPane.QUESTION_MESSAGE);
                                if(key==null){
                                    JOptionPane.showMessageDialog(LoadPage.this, "请重新输入匹配号", "中国象棋登入界面", JOptionPane.ERROR_MESSAGE, null);
                                }
                            }while(key==null);
                            printWriter.println(key);
                            LoadPage.this.remove(submitPanel);
                            LoadPage.this.remove(userPasswordJpanel);
                            LoadPage.this.setLayout(null);
                            JLabel waitJlabel=new JLabel("等待匹配",SwingConstants.CENTER);
                            waitJlabel.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
                            LoadPage.this.add(waitJlabel);
                            LoadPage.this.repaint();
                            Thread thread=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    int messageSocket=scanner.nextInt();
                                    if(messageSocket==SocketMessage.MATCH.getNumberMeaage()){
                                        LoadPage.this.setVisible(false);
                                        messageSocket=scanner.nextInt();
                                        ChineseChess chineseChess=new ChineseChess(mySocket,messageSocket);
                                        chineseChess.setTitle("Chinese Chess");
                                        chineseChess.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                        chineseChess.setVisible(true);
                                        chineseChess.detailLabel1.setText("用户："+userName);
                                    }
                                }
                            });
                            thread.start();
                        }else {
                            if(returnNumber==SocketMessage.NOT_HAVE_USER.getNumberMeaage()){
                                JOptionPane.showMessageDialog(LoadPage.this, "无此用户名", "中国象棋登入界面", JOptionPane.ERROR_MESSAGE, null);
                            }else if(returnNumber==SocketMessage.PASSWORD_ERROR.getNumberMeaage()){
                                JOptionPane.showMessageDialog(LoadPage.this, "密码错误", "中国象棋登入界面", JOptionPane.ERROR_MESSAGE, null);
                            }else if(returnNumber==SocketMessage.NOT_CONNECTED.getNumberMeaage()){
                                JOptionPane.showMessageDialog(LoadPage.this, "数据库连接错误", "中国象棋登入界面", JOptionPane.ERROR_MESSAGE, null);
                            }else if(returnNumber==SocketMessage.SEARCH_ERROR.getNumberMeaage()){
                                JOptionPane.showMessageDialog(LoadPage.this, "数据库搜索错误", "中国象棋登入界面", JOptionPane.ERROR_MESSAGE, null);
                            }
                            userTextField.setText("");
                            passwordField.setText("");
                        }
                    }catch (Exception exception){
                        exception.printStackTrace();
                    }
                }
            });
        }
}
