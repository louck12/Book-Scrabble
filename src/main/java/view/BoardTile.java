package view;

import javafx.beans.property.StringProperty;

import java.util.ArrayList;

public class BoardTile {
    public String letter;
    private int row;
    private int col;

    public BoardTile(StringProperty letter, int row, int col){
        this.letter = letter.get();
        this.row = row;
        this.col = col;
    }

    public int getCol(){return col;}
    public int getRow(){return row;}
    public String getLetter(){return letter;}

    public boolean equals(BoardTile bt){
        if(bt.getCol() == col && bt.getRow() == row)
            return true;
        return false;
    }

    /*public static boolean contains(ArrayList<BoardTile> l, BoardTile b){
        for(BoardTile bt: l){
            if(bt.equals(b))
                return true;
        }
        return false;
    }
*/
    public static boolean contains(ArrayList<BoardTile> l, int row, int col){
        for(BoardTile bt: l){
            if(bt.getCol() == col && bt.getRow() == row)
                return true;
        }
        return false;
    }

    public static BoardTile getTile(int row, int col, ArrayList<BoardTile> l){
        for(BoardTile bt: l){
            if(bt.getCol() == col && bt.getRow() == row)
                return bt;
        }
        return null;
    }


}
