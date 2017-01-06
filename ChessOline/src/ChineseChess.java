import javax.swing.*;
import java.awt.*;
import java.net.Socket;

/**
 * Created by lianyuzhe on 2016/12/21.
 */
//棋盘的应用框架
public class ChineseChess extends JFrame{
    private ImagePanel ChessTable;
    private Socket mySocket;
    private int order;
    public JLabel detailLabel1;
    public JLabel DetailLabel2;
    public JLabel DetailLabel3;
    private static final int DEFAULT_WIDTH=800;
    private static final int DEFAULT_HEIGHT=650;
    private static final int DEFAULT_WIDTH_CHESSTABLE=600;
    private static final int DEFAULT_HEIGHT_CHESSTABLE=600;
    private Image ChessTableImage;
    public ChineseChess(Socket incoming,int SocketMessage){
        order=SocketMessage;
        mySocket=incoming;
        ChessTableImage=new ImageIcon("/Users/lianyuzhe/IdeaProjects/Chinese chess/ChessOline/Board.png").getImage();
        setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
        setLayout(null);
        JPanel jPanel=new JPanel();
        jPanel.setLayout(new BorderLayout());
        detailLabel1=new JLabel();
        DetailLabel3=new JLabel();
        jPanel.add(detailLabel1,BorderLayout.NORTH);
        jPanel.add(DetailLabel3,BorderLayout.CENTER);
        DetailLabel2=new JLabel();
        jPanel.add(DetailLabel2,BorderLayout.SOUTH);
        jPanel.setBounds(610,250,180,80);
        ChessTable=new ImagePanel(ChessTableImage,mySocket,order,this);
        ChessTable.setBounds(0,DEFAULT_HEIGHT-DEFAULT_HEIGHT_CHESSTABLE-20,DEFAULT_WIDTH_CHESSTABLE,DEFAULT_HEIGHT_CHESSTABLE);
        add(ChessTable);
        add(jPanel);
    }
}
