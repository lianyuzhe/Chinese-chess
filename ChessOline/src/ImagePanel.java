import sun.applet.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by lianyuzhe on 2016/12/21.
 */
//棋盘的一个类
public class ImagePanel extends JPanel {
    private Socket mySocket;
    private int order;
    private boolean myTurn;
    private Image image;
    private static final int DEFAULT_WIDTH_CHESSTABLE=600;
    private static final int DEFAULT_HEIGHT_CHESSTABLE=600;
    private int selectRow,selectColumn;
    private boolean isSelected;
    private boolean win;
    private boolean WhichStep;
    private static final int []xLevel={41,106,171,233,298,361,426,490,551};
    private static final int []yLevel={40,94,152,209,267,330,382,438,498,557};
    private static final String []BlackPieceName={"车","车","马","马","象","象","士","士","将","炮","炮","卒","卒","卒","卒","卒"};
    private static final String []RedPieceName=  {"車","車","馬","馬","相","相","仕","仕","帅","炮","炮","兵","兵","兵","兵","兵"};
    private ChessPoint [][]chessPoint;
    private ChessPieces PiecesRed[];
    private ChessPieces PiecesBlack[];
    public ImagePanel(Image image,Socket mySocket,int order){
        super();
        this.mySocket=mySocket;
        this.order=order;
        if(this.order==SocketMessage.FIRST_RED.getNumberMeaage()){
            myTurn=true;
        }else{
            myTurn=false;
        }
        win=false;
        WhichStep=true;
        isSelected=false;
        this.setLayout(null);
        setOpaque(true);//绘制边界内所有像素
        this.image=image;
        chessPoint=new ChessPoint[9][10];
        for(int i=0;i<9;i++){
            for(int j=0;j<10;j++){
                chessPoint[i][j]=new ChessPoint(xLevel[i]-20,yLevel[j]-20);
            }
        }
        PiecesRed=new ChessPieces[16];
        PiecesBlack=new ChessPieces[16];
        for(int i=0;i<16;i++){
            PiecesRed[i]=new ChessPieces(RedPieceName[i],Color.BLACK,Color.RED,40,40,true);
            PiecesBlack[i]=new ChessPieces(BlackPieceName[i],Color.BLACK,Color.BLACK,40,40,false);
        }
        chessPoint[0][0].addPiece(PiecesBlack[0]);//车
        chessPoint[8][0].addPiece(PiecesBlack[1]);//车
        chessPoint[1][0].addPiece(PiecesBlack[2]);//马
        chessPoint[7][0].addPiece(PiecesBlack[3]);//马
        chessPoint[2][0].addPiece(PiecesBlack[4]);//象
        chessPoint[6][0].addPiece(PiecesBlack[5]);//象
        chessPoint[3][0].addPiece(PiecesBlack[6]);//士
        chessPoint[5][0].addPiece(PiecesBlack[7]);//士
        chessPoint[4][0].addPiece(PiecesBlack[8]);//将
        chessPoint[1][2].addPiece(PiecesBlack[9]);//炮
        chessPoint[7][2].addPiece(PiecesBlack[10]);//炮
        //兵
        for(int i=0;i<=9;i+=2) {
            chessPoint[i][3].addPiece(PiecesBlack[11+i/2]);
        }
        chessPoint[0][9].addPiece(PiecesRed[0]);//車
        chessPoint[8][9].addPiece(PiecesRed[1]);//車
        chessPoint[1][9].addPiece(PiecesRed[2]);//馬
        chessPoint[7][9].addPiece(PiecesRed[3]);//馬
        chessPoint[2][9].addPiece(PiecesRed[4]);//相
        chessPoint[6][9].addPiece(PiecesRed[5]);//相
        chessPoint[3][9].addPiece(PiecesRed[6]);//仕
        chessPoint[5][9].addPiece(PiecesRed[7]);//仕
        chessPoint[4][9].addPiece(PiecesRed[8]);//帅
        chessPoint[1][7].addPiece(PiecesRed[9]);//炮
        chessPoint[7][7].addPiece(PiecesRed[10]);//炮
        //卒
        for(int i=0;i<=9;i+=2) {
            chessPoint[i][6].addPiece(PiecesRed[11+i/2]);
        }
        for(int i=0;i<9;i++){
            for(int j=0;j<10;j++){
                if(chessPoint[i][j].HavePiece()){
                    add(chessPoint[i][j].piecesAtPoint());
                }
            }
        }
        Thread mythread=new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner mySocketIn;
                InputStream inputStream;
                PrintWriter printWriter;
                OutputStream outputStream;
                try {
                    inputStream = mySocket.getInputStream();
                    mySocketIn=new Scanner(inputStream);
                    outputStream=mySocket.getOutputStream();
                    printWriter=new PrintWriter(outputStream);
                }catch (Exception e){
                    return;
                }
                while(true){
                    if(!myTurn){
                        String name;
                        int startX,startY,endX,endY;
                        startX=mySocketIn.nextInt();
                        startY=mySocketIn.nextInt();
                        endX=mySocketIn.nextInt();
                        endY=mySocketIn.nextInt();
                        ImagePanel.this.remove(chessPoint[startX][startY].piecesAtPoint());
                        ChessPieces chessPieces=chessPoint[startX][startY].removePiece();
                        if(chessPoint[endX][endY].HavePiece()){
                            name=chessPoint[endX][endY].piecesAtPoint().name;
                            ImagePanel.this.remove(chessPoint[endX][endY].piecesAtPoint());
                            chessPoint[endX][endY].removePiece();
                            chessPoint[endX][endY].addPiece(chessPieces);
                            ImagePanel.this.add(chessPieces);
                            ImagePanel.this.repaint();
                            if(name.equals("将")||name.equals("帅")){
                                win=true;
                                if(name.equals("将")){
                                    JOptionPane.showMessageDialog(ImagePanel.this,"红方胜利");
                                }else{
                                    JOptionPane.showMessageDialog(ImagePanel.this,"黑方胜利");
                                }
                            }
                        }else{
                            chessPoint[endX][endY].addPiece(chessPieces);
                            ImagePanel.this.add(chessPieces);
                            ImagePanel.this.repaint();
                        }
                        ImagePanel.this.myTurn=true;
                    }
                }
            }
        });
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!myTurn)
                    return;
                if(win)
                    return;
                String name=null;
                Point point=e.getPoint();
                double x,y;
                x=point.getX();
                y=point.getY();
                int row=-1,cloumn=-1;
                for(int i=0;i<9;i++){
                    if(x>=xLevel[i]-20&&x<=xLevel[i]+20){
                        row=i;
                        break;
                    }
                }
                for(int i=0;i<10;i++){
                    if(y>=yLevel[i]-20&&y<=yLevel[i]+20){
                        cloumn=i;
                        break;
                    }
                }
                if(row!=-1&&cloumn!=-1){
                    if((chessPoint[selectRow][selectColumn].piecesAtPoint().type==true&&order==SocketMessage.SECOND_BLACK.getNumberMeaage())||
                            (chessPoint[selectRow][selectColumn].piecesAtPoint().type==false&&order==SocketMessage.FIRST_RED.getNumberMeaage())){
                        return;
                    }
                    if(isSelected){
                       boolean move=moveable(chessPoint[selectRow][selectColumn].piecesAtPoint(),selectRow,selectColumn,row,cloumn);
                       if(move){
                            ImagePanel.this.remove(chessPoint[selectRow][selectColumn].piecesAtPoint());
                            ChessPieces chessPieces=chessPoint[selectRow][selectColumn].removePiece();
                            if(chessPoint[row][cloumn].HavePiece()){
                                name=chessPoint[row][cloumn].piecesAtPoint().name;
                                ImagePanel.this.remove(chessPoint[row][cloumn].piecesAtPoint());
                                chessPoint[row][cloumn].removePiece();
                                chessPoint[row][cloumn].addPiece(chessPieces);
                                ImagePanel.this.add(chessPieces);
                                if(name.equals("将")||name.equals("帅")){
                                    win=true;
                                    if(name.equals("将")){
                                        JOptionPane.showMessageDialog(ImagePanel.this,"红方胜利");
                                    }else{
                                        JOptionPane.showMessageDialog(ImagePanel.this,"黑方胜利");
                                    }
                                }
                            }else{
                                chessPoint[row][cloumn].addPiece(chessPieces);
                                ImagePanel.this.add(chessPieces);
                            }
                            ImagePanel.this.repaint();
                            myTurn=false;
                       }
                       isSelected=false;
                       chessPoint[selectRow][selectColumn].piecesAtPoint().backcolor=Color.BLACK;
                    }else if(chessPoint[row][cloumn].HavePiece()){
                        isSelected=true;
                        selectRow=row;
                        selectColumn=cloumn;
                    }
                }
                if(isSelected){
                    chessPoint[selectRow][selectColumn].piecesAtPoint().backcolor=Color.RED;
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }
    @Override
    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        setBackground(Color.white);
        if(image!=null){
            graphics.drawImage(image,0,0,DEFAULT_WIDTH_CHESSTABLE,DEFAULT_HEIGHT_CHESSTABLE,this);
        }
    }
    public boolean moveable(ChessPieces pieces,int startX,int startY,int endX,int endY){
        boolean canMove=true;
        int maxX=Math.max(startX,endX);
        int minX=Math.min(startX,endX);
        int maxY=Math.max(startY,endY);
        int minY=Math.min(startY,endY);
        //对于车进行判断其能不能进行移动
        if(pieces.name.equals("車")||pieces.name.equals("车")){
            if(startX==endX&&startY!=endY){
                for(int i=minY+1;i<maxY;i++){
                    if(chessPoint[startX][i].HavePiece()){
                        canMove=false;
                        break;
                    }
                }
                if(chessPoint[endX][endY].HavePiece()&&chessPoint[endX][endY].piecesAtPoint().type==pieces.type){
                    canMove=false;
                }
            }else if(startY==endY&&startX!=endX){
                for(int i=minX+1;i<maxX;i++){
                    if(chessPoint[i][startX].HavePiece()){
                        canMove=false;
                        break;
                    }
                }
                if(chessPoint[endX][endY].HavePiece()&&chessPoint[endX][endY].piecesAtPoint().type==pieces.type){
                    canMove=false;
                }
            }else{
                canMove=false;
            }
        }else if(pieces.name.equals("马")||pieces.name.equals("馬")){
            if(Math.abs(startX-endX)==1&&Math.abs(startY-endY)==2){
                if(chessPoint[startX][(startY+endY)/2].HavePiece()){
                    canMove=false;
                }else if(chessPoint[endX][endY].HavePiece()&&chessPoint[endX][endY].piecesAtPoint().type==pieces.type){
                    canMove=false;
                }
            }else if(Math.abs(startX-endX)==2&&Math.abs(startY-endY)==1){
                if(chessPoint[(startX+endX)/2][startY].HavePiece()){
                    canMove=false;
                }else if(chessPoint[endX][endY].HavePiece()&&chessPoint[endX][endY].piecesAtPoint().type==pieces.type){
                    canMove=false;
                }
            }else{
                canMove=false;
            }
        }else if(pieces.name.equals("象")){
            if(Math.abs(startX-endX)==2&&Math.abs(startY-endY)==2&&endY<=4){
                if(chessPoint[(startX+endX)/2][(startY+endY)/2].HavePiece()){
                    canMove=false;
                }else if(chessPoint[endX][endY].HavePiece()&&chessPoint[endX][endY].piecesAtPoint().type==pieces.type){
                    canMove=false;
                }
            }else{
                canMove=false;
            }
        }else if(pieces.name.equals("相")){
            if(Math.abs(startX-endX)==2&&Math.abs(startY-endY)==2&&endY>=5){
                if(chessPoint[(startX+endX)/2][(startY+endY)/2].HavePiece()){
                    canMove=false;
                }else if(chessPoint[endX][endY].HavePiece()&&chessPoint[endX][endY].piecesAtPoint().type==pieces.type){
                    canMove=false;
                }
            }else{
                canMove=false;
            }
        }else if(pieces.name.equals("士")){
            if(Math.abs(startX-endX)==1&&Math.abs(startY-endY)==1&&endX>=3&&endX<=5&&endY>=0&&endY<=2){
                if(chessPoint[endX][endY].HavePiece()&&chessPoint[endX][endY].piecesAtPoint().type==pieces.type){
                    canMove=false;
                }
            }else{
                canMove=false;
            }
        }else if(pieces.name.equals("仕")){
            if(Math.abs(startX-endX)==1&&Math.abs(startY-endY)==1&&endX>=3&&endX<=5&&endY>=7&&endY<=9){
                if(chessPoint[endX][endY].HavePiece()&&chessPoint[endX][endY].piecesAtPoint().type==pieces.type){
                    canMove=false;
                }
            }else{
                canMove=false;
            }
        }else if(pieces.name.equals("将")){
            if(endX>=3&&endX<=5&&endY>=0&&endY<=2){
                if((Math.abs(startX-endX)==0&&Math.abs(startY-endY)==1)||(Math.abs(startX-endX)==1&&Math.abs(startY-endY)==0)){
                    if(chessPoint[endX][endY].HavePiece()&&chessPoint[endX][endY].piecesAtPoint().type==pieces.type){
                        canMove=false;
                    }
                }
            }else{
                canMove=false;
            }
        }else if(pieces.name.equals("帅")){
            if(endX>=3&&endX<=5&&endY>=7&&endY<=9){
                if((Math.abs(startX-endX)==0&&Math.abs(startY-endY)==1)||(Math.abs(startX-endX)==1&&Math.abs(startY-endY)==0)){
                    if(chessPoint[endX][endY].HavePiece()&&chessPoint[endX][endY].piecesAtPoint().type==pieces.type){
                        canMove=false;
                    }
                }
            }else{
                canMove=false;
            }
        }else if(pieces.name.equals("炮")) {
            int midPieces = 0;
            if (Math.abs(startX - endX) == 0 && Math.abs(startY - endY) != 0) {
                for (int i = minY + 1; i < maxY; i++) {
                    if (chessPoint[startX][i].HavePiece()) {
                        midPieces++;
                    }
                }
                if (midPieces == 0 && !chessPoint[endX][endY].HavePiece()) {
                    canMove = true;
                } else if (midPieces == 1 && chessPoint[endX][endY].HavePiece() && chessPoint[endX][endY].piecesAtPoint().type != pieces.type) {
                    canMove = true;
                } else {
                    canMove = false;
                }
            } else if (Math.abs(startX - endX) != 0 && Math.abs(startY - endY) == 0) {
                for (int i = minX + 1; i < maxX; i++) {
                    if (chessPoint[i][startY].HavePiece()) {
                        midPieces++;
                    }
                }
                if (midPieces == 0 && !chessPoint[endX][endY].HavePiece()) {
                    canMove = true;
                } else if (midPieces == 1 && chessPoint[endX][endY].HavePiece() && chessPoint[endX][endY].piecesAtPoint().type != pieces.type) {
                    canMove = true;
                } else {
                    canMove = false;
                }
            }else {
                canMove=false;
            }
        }else if(pieces.name.equals("卒")){
            if((startX==endX&&endY-startY==1)||(endY==startY&&Math.abs(startX-endX)==1)){
                if(chessPoint[endX][endY].HavePiece()&&chessPoint[endX][endY].piecesAtPoint().type==pieces.type){
                    canMove=false;
                }
            }else{
                canMove=false;
            }
        }else if(pieces.name.equals("兵")){
            if(startX==endX&&endY-startY==-1||(endY==startY&&Math.abs(startX-endX)==1)){
                if(chessPoint[endX][endY].HavePiece()&&chessPoint[endX][endY].piecesAtPoint().type==pieces.type){
                    canMove=false;
                }
            }else {
                canMove=false;
            }
        }
        return canMove;
    }
    private class MouseAction extends MouseAdapter {
        private int x,y;
        public MouseAction(){
            x=0;
            y=0;
        }
        @Override
        public void mouseClicked(MouseEvent event){
            Point point=event.getLocationOnScreen();
            x=point.x;
            y=point.y;
            System.out.println("x= "+x+",y= "+y);
        }
    }
    private class MouseMotionHander extends MouseMotionAdapter{
        public void mouseDragged(MouseEvent event){
            super.mouseDragged(event);
            System.out.println("JPanel's X= "+event.getX()+",Y= "+event.getY());
        }
    }
}
