import javax.swing.*;
import java.awt.*;

/**
 * Created by lianyuzhe on 2016/12/21
 */
public class ChessPieces extends JLabel {
    String name;//棋子名字
    Color backcolor,faceColor;//背景色和前景色
    boolean type;//类别
    public int width,hegiht;
    public ChessPieces(String name, Color backcolor,Color faceColor,int width,int height,boolean type){
        this.name=name;
        this.backcolor=backcolor;
        this.faceColor=faceColor;
        this.width=width;
        this.hegiht=height;
        this.type=type;
        setSize(width, height);
        setBackground(backcolor);
    }
    public void setDetail(String name,Color backcolor,Color faceColor,int width,int height,boolean type){
        this.name=name;
        this.backcolor=backcolor;
        this.faceColor=faceColor;
        this.width=width;
        this.hegiht=height;
        this.type=type;
        setSize(width, height);
        setBackground(backcolor);
    }
    //绘制棋子
    @Override
    public void paint(Graphics graphics){
        graphics.setColor(new Color(255,250,227));
        graphics.fillRoundRect(2,2,width-2,hegiht-2,width-2,hegiht-2);
        graphics.setColor(faceColor);
        graphics.setFont(new Font("楷体",Font.BOLD,26));
        graphics.drawString(name,7,hegiht-10);//在棋子上绘制名字
        graphics.setColor(backcolor);
        float lineWidth=2f;
        ((Graphics2D)graphics).setStroke(new BasicStroke(lineWidth));
        ((Graphics2D)graphics).drawOval(1,1,width-2,hegiht-2);
    }
}
