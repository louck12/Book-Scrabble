package model.board;


import java.util.Arrays;

public class Word {
    private final Tile[] tiles;
    private final int row;
    private final int col;
    private final boolean vertical;

    public Word(Tile[] tiles, int row, int col, boolean vertical) {
        this.tiles = tiles;
        this.row = row;
        this.col = col;
        this.vertical = vertical;
    }

    public Tile[] getWord() {
        return tiles;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean getVertical(){return vertical;}

    public boolean isVertical() {
        return vertical;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return getRow() == word.getRow() && getCol() == word.getCol() && isVertical() == word.isVertical() && Arrays.equals(tiles, word.tiles);
    }


}
