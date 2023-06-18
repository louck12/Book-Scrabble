
package model.board;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Board {

    public class Positions{
        int row;
        int col;

        public Positions(int row, int col){
            this.col = col;
            this.row = row;
        }
    }

    private static Board board=null;


    public static Board getBoard() {
        if(board==null)
            board=new Board();
        return board;
    }

    // indexes
    final byte dl=2;	// double letter
    final byte tl=3;	// triple letter
    final byte dw=20;	// double word
    final byte tw=30;	// triple word

    private byte[][] bonus= {
            {tw,0,0,dl,0,0,0,tw,0,0,0,dl,0,0,tw},
            {0,dw,0,0,0,tl,0,0,0,tl,0,0,0,dw,0},
            {0,0,dw,0,0,0,dl,0,dl,0,0,0,dw,0,0},
            {dl,0,0,dw,0,0,0,dl,0,0,0,dw,0,0,dl},
            {0,0,0,0,dw,0,0,0,0,0,dw,0,0,0,0},
            {0,tl,0,0,0,tl,0,0,0,tl,0,0,0,tl,0},
            {0,0,dl,0,0,0,dl,0,dl,0,0,0,dl,0,0},
            {tw,0,0,dl,0,0,0,dw,0,0,0,dl,0,0,tw},
            {0,0,dl,0,0,0,dl,0,dl,0,0,0,dl,0,0},
            {0,tl,0,0,0,tl,0,0,0,tl,0,0,0,tl,0},
            {0,0,0,0,dw,0,0,0,0,0,dw,0,0,0,0},
            {dl,0,0,dw,0,0,0,dl,0,0,0,dw,0,0,dl},
            {0,0,dw,0,0,0,dl,0,dl,0,0,0,dw,0,0},
            {0,dw,0,0,0,tl,0,0,0,tl,0,0,0,dw,0},
            {tw,0,0,dl,0,0,0,tw,0,0,0,dl,0,0,tw}
    };

    Tile[][] tiles;

    ArrayList<Positions> approvedTiles = new ArrayList<>();

    boolean isEmpty;

    public ArrayList<Tile> tilesInBoard = new ArrayList<>();

    private Board() {
        tiles=new Tile[15][15];
        isEmpty=true;
    }

    public Tile[][] getTiles() {
        return tiles.clone();
    }



    private boolean inBoard(int row,int col) {
        return (col>=0 && col<15 && row>=0 && row<15);
    }

    private boolean onStar(Word w) {
        int i=w.getRow(),j=w.getCol();
        for(int k=0;k<w.getTiles().length;k++) {
            if(i==7 && j==7)
                return true;
            if(w.isVertical()) i++; else j++;
        }
        return false;
    }

    private boolean crossTile(Word w) {
        int i=w.getRow(),j=w.getCol();
        for(int k=0;k<w.getTiles().length;k++) {

            if(tiles[i][j]!=null ||
                    (inBoard(i+1, j) 	&& tiles[i+1][j]!=null)   ||
                    (inBoard(i+1, j+1) 	&& tiles[i+1][j+1]!=null) ||
                    (inBoard(i, j+1) 	&& tiles[i][j+1]!=null)   ||
                    (inBoard(i-1, j+1) 	&& tiles[i-1][j+1]!=null) ||
                    (inBoard(i-1, j) 	&& tiles[i-1][j]!=null)   ||
                    (inBoard(i-1, j-1) 	&& tiles[i-1][j-1]!=null) ||
                    (inBoard(i, j-1) 	&& tiles[i][j-1]!=null)   ||
                    (inBoard(i+1, j-1) 	&& tiles[i+1][j-1]!=null)
            )
                return true;

            if(w.isVertical()) i++; else j++;
        }
        return false;
    }

    /*private boolean crossTile(Word w) {
        int i=w.getRow(),j=w.getCol();
        for(int k=0;k<w.getTiles().length;k++) {

            if(tiles[i][j]!=null ||
                    (inBoard(i+1, j) 	&& tiles[i+1][j]!=null)   ||

                    (inBoard(i, j+1) 	&& tiles[i][j+1]!=null)   ||

                    (inBoard(i-1, j) 	&& tiles[i-1][j]!=null)   ||

                    (inBoard(i, j-1) 	&& tiles[i][j-1]!=null)

            )
                return true;

            if(w.isVertical()) i++; else j++;
        }
        return false;
    }*/

    private boolean changesTile(Word w) {
        int i=w.getRow(),j=w.getCol();
        for(Tile t : w.getTiles()) {
            if(tiles[i][j]!=null && tiles[i][j]!=t)
                return  true;
            if(w.isVertical()) i++; else j++;
        }
        return false;
    }


    public boolean boardLegal(Word w) {
        int row=w.getRow();
        int col=w.getCol();

        if(!inBoard(row, col))
            return false;
        int eCol,eRow;
        if(w.isVertical()) {
            eCol=col;
            eRow=row+w.getTiles().length-1;
        }else {
            eRow=row;
            eCol=col+w.getTiles().length-1;
        }
        if(!inBoard(eRow, eCol))
            return false;


        if(isEmpty && !onStar(w))
            return false;

        if(!isEmpty && !crossTile(w))
            return false;

        /*if(changesTile(w))
            return false;*/

        return true;
    }

    public boolean dictionaryLegal(Word w, Scanner inFromServer, PrintWriter outToServer) {
       String word = "";
       for(Tile t: w.getTiles())
           word+=t.letter;
        outToServer.write("Q,mobydick.txt,pg10.txt,shakespeare.txt," + word + "\n");
        outToServer.flush();
        String ans = inFromServer.nextLine();
        return ans.equals("true");
    }


    private ArrayList<Word> getAllWords(Tile[][] ts){
        ArrayList<Word> ws=new ArrayList<>();

        // Horizontal scan
        for(int i=0;i<ts.length;i++) {
            int j=0;
            while(j<ts[i].length) {
                ArrayList<Tile> tal=new ArrayList<>();
                int row=i,col=j;
                while(j<ts[i].length && ts[i][j]!=null) {
                    tal.add(ts[i][j]);
                    j++;
                }
                if(tal.size()>1) {
                    Tile[] tiles=new Tile[tal.size()];
                    ws.add(new Word(tal.toArray(tiles), row,col,false));
                }
                j++;
            }
        }

        // Vertical scan
        for(int j=0;j<ts[0].length;j++) {
            int i=0;
            while(i<ts.length) {
                ArrayList<Tile> tal=new ArrayList<>();
                int row=i,col=j;
                while(i<ts.length && ts[i][j]!=null) {
                    tal.add(ts[i][j]);
                    i++;
                }
                if(tal.size()>1) {
                    Tile[] tiles=new Tile[tal.size()];
                    ws.add(new Word(tal.toArray(tiles), row,col,true));
                }
                i++;
            }
        }

        return ws;
    }

    public ArrayList<Word> getWords(Word w) {
        /*Tile[][] ts=getTiles(); // a clone...
        ArrayList<Word> before=getAllWords(ts);
        // demo placement of new word
        int row=w.getRow();
        int col=w.getCol();
        for(Tile t : w.getTiles()) {
            ts[row][col]=t;
            if(w.isVertical()) row++; else col++;
        }
        ArrayList<Word> after=getAllWords(ts);
        after.removeAll(before); // only new words remain...
        return after;*/
        ArrayList<Word> before = new ArrayList<Word>();
        ArrayList<Word> after = new ArrayList<Word>();

        before = currentBoardWords();

        int row = w.getRow();
        int col = w.getCol();
        int wordLen = w.getTiles().length;

        for(int i = 0; i < wordLen; i++){

            if(w.isVertical() && w.getTiles()[i] != null){
                tiles[row + i][col] = w.getTiles()[i];
            }

            else if(!w.isVertical() && w.getTiles()[i] != null){
                tiles[row][col + i] = w.getTiles()[i];
            }
        }
        after = currentBoardWords();
        boolean foundExistedTile;

        //unplacing the word from the board
        for(int i = 0; i < wordLen; i++){
            /*if(w.getTiles()[i] == null) continue;*/
            foundExistedTile = false;
            if(w.isVertical()){
                for(Positions p: approvedTiles){
                    if(row+i == p.row && col == p.col){
                        foundExistedTile = true;
                        break;
                    }
                }
                if(!foundExistedTile)
                    tiles[row + i][col] = null;
            }
            else{

                for(Positions p: approvedTiles){
                    if(row == p.row && col+i == p.col){
                        foundExistedTile = true;
                        break;
                    }
                }
                if(!foundExistedTile)
                    tiles[row][col + i] = null;

            }
        }

        ArrayList<Word> difference = new ArrayList<Word>();
        boolean found = false;
        for(Word wAfter: after){
            found = false;
            for(Word wBefore: before){
                if(compareTwoWords(wAfter, wBefore)){
                    found = true;
                    break;
                }
            }

            if(!found){
                difference.add(wAfter);
            }
        }

        return difference;
    }

    public int getScore(Word w) {
        int row=w.getRow();
        int col=w.getCol();
        int sum=0;
        int tripleWord=0,doubleWord=0;
        for(Tile t : w.getTiles()) {
            sum+=t.score;
            if(bonus[row][col] == dl)
                sum+=t.score;
            if(bonus[row][col] == tl)
                sum+=t.score*2;
            if(bonus[row][col] == dw)
                doubleWord++;
            if(bonus[row][col] == tw)
                tripleWord++;
            if(w.isVertical()) row++; else col++;
        }

        if(doubleWord>0)
            sum*=(2*doubleWord);
        if(tripleWord>0)
            sum*=(3*tripleWord);
        return sum;

    }


    public int tryPlaceWord(Word w, Scanner inFromServer, PrintWriter outToServer, boolean successfullyChallenged) {

        Tile[] ts = w.getTiles();
        int row=w.getRow();
        int col=w.getCol();
        for(int i=0;i<ts.length;i++) {
            if(ts[i]==null)
                ts[i]=tiles[row][col];
            if(w.isVertical()) row++; else col++;
        }

        Word test=new Word(ts, w.getRow(), w.getCol(), w.isVertical());

        int sum=0;
        if(boardLegal(test) ) {
            ArrayList<Word> newWords=getWords(test);
            for(Word nw : newWords) {
                if(successfullyChallenged || dictionaryLegal(nw, inFromServer, outToServer))
                    sum+=getScore(nw);
                else
                    return 0;
            }
        }

        if(sum != 0){
            int r = w.getRow();
            int c = w.getCol();
            for(Tile t: w.getTiles()){
                if(w.isVertical())
                    approvedTiles.add(new Positions(r++, c));
                else
                    approvedTiles.add(new Positions(r, c++));
            }

        }

        // the placement
        row=w.getRow();
        col=w.getCol();
        for(Tile t : w.getTiles()) {
            tiles[row][col]=t;
            if(w.isVertical()) row++; else col++;
        }

        if(isEmpty) {
            isEmpty=false;
            bonus[7][7]=0;
        }
        return sum;
    }

    public void print() {
        for(Tile[] ts : tiles) {
            for(Tile t : ts) {
                if(t!=null)
                    System.out.print(t.letter);
                else
                    System.out.print("_");
            }
            System.out.println();
        }
    }


    public ArrayList<Word> currentBoardWords(){

        int start = 0;
        int end = 0;
        Word word ;
        ArrayList<Tile> t = new ArrayList<Tile>();
        ArrayList<Word> boardWords = new ArrayList<Word>();


        //checking horizontal words
        for(int row = 0; row < 15; row++){
            start = 0;

            end = 0;
            while(true){
                if(end > 14) break;
                for(start = end; start <= 14 && tiles[row][start] == null; start++);
                if(start > 14) break;
                for(end = start; end <= 14 && tiles[row][end] != null; end++);
                if(start != end - 1){
                    for(int i = start; i < end; i++){
                        t.add(tiles[row][i]);
                    }

                    word = new Word(t.toArray(new Tile[t.size()]), row, start, false);
                    boardWords.add(word);
                    t.clear();

                }
            }

        }

        //checking vertical words
        for(int col = 0; col < 15; col++){
            start = 0;
            end = 0;
            while(true){
                if(end > 14) break;
                for(start = end; start <= 14 && tiles[start][col] == null; start++);
                if(start > 14) break;
                for(end = start; end <= 14 && tiles[end][col] != null; end++);
                if(start != end - 1){
                    for(int i = start; i < end; i++){
                        t.add(tiles[i][col]);
                    }
                    word = new Word(t.toArray(new Tile[t.size()]), start, col, true);
                    boardWords.add(word);
                    t.clear();
                }
            }
        }

        return boardWords;
    }

    public boolean compareTwoWords(Word w1, Word w2){
        if(w1.getTiles().length != w2.getTiles().length) return false;
        for(int i = 0; i < w1.getTiles().length; i++){
            if(w1.getTiles()[i] != w2.getTiles()[i]) return false;
        }
        return true;
    }


}
