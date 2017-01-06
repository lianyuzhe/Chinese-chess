/**
 * Created by lianyuzhe on 2016/12/26.
 */
public class ChessPoint {
    private int x,y;
    private ChessPieces pieces;
    private boolean havePiece;
    public ChessPoint(int x,int y){
        this.x=x;
        this.y=y;
        pieces=null;
        havePiece=false;
    }
    public boolean HavePiece(){
        return havePiece;
    }
    public ChessPoint(int x,int y,ChessPieces chessPieces){
        this.x=x;
        this.y=y;
        pieces=chessPieces;
        pieces.setBounds(x,y,40,40);
        havePiece=true;
    }
    public void addPosition(int x,int y){
        this.x=x;
        this.y=y;
    }
    public ChessPieces piecesAtPoint(){
        return pieces;
    }
    public ChessPieces removePiece(){
        ChessPieces rPiece=pieces;
        pieces=null;
        havePiece=false;
        return rPiece;
    }
    public boolean addPiece(ChessPieces chessPieces){
        if(HavePiece()){
            return false;
        }
        pieces=chessPieces;
        pieces.setBounds(x,y,40,40);
        havePiece=true;
        return true;
    }
    public int getXPosition(){
        return x;
    }
    public int getYPosition(){
        return y;
    }
}
