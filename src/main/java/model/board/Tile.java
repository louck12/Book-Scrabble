package model.board;


import java.util.Objects;


public class Tile {
    public final char letter;
    public final int score;

    private Tile(char letter, int score) {
        this.letter = letter;
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return letter == tile.letter && score == tile.score;
    }

    @Override
    public int hashCode() {
        return Objects.hash(letter, score);
    }

    public static class Bag{
        private static Bag _instance = null;
        int tilesAmount[] = {9,2,2,4,12,2,3,2,9,1,1,4,2,6,8,2,1,6,4,6,4,2,2,1,2,1};
        int legalAmount[] = {9,2,2,4,12,2,3,2,9,1,1,4,2,6,8,2,1,6,4,6,4,2,2,1,2,1};
        Tile[] tiles = {new Tile('A', 1), new Tile('B', 3), new Tile('C', 3), new Tile('D', 2), new Tile('E', 1), new Tile('F', 4), new Tile('G', 2), new Tile('H', 4), new Tile('I', 1), new Tile('J', 8), new Tile('K', 5), new Tile('L', 1), new Tile('M', 3), new Tile('N', 1), new Tile('O', 1), new Tile('P', 3), new Tile('Q', 10), new Tile('R', 1), new Tile('S', 1), new Tile('T', 1), new Tile('U', 1), new Tile('V', 4), new Tile('W', 4), new Tile('X', 8), new Tile('Y', 4), new Tile('Z', 10)};
        int bagCount = 98;

        private Bag(){}

        public Tile getRand(){

            if(bagCount == 0)
                return null;

            int rand = (int)(Math.random()*26);
            while(true){
                if(tilesAmount[rand] > 0){
                    tilesAmount[rand]--;
                    bagCount--;
                    return tiles[rand];
                }
                rand++;
                if(rand == 26){
                    rand = 0;
                }
            }
        }

        public Tile getTile(char letter){

            if(letter < 65 || letter > 90) return null;

            int tileIdx = letter - 'A';
            if(tilesAmount[tileIdx] > 0){
                tilesAmount[tileIdx]--;
                bagCount--;
                return tiles[tileIdx];
            }
            return null;
        }

        public void put(Tile t){

            if(t.letter < 65 || t.letter > 90) return;

            int tileIdx = t.letter - 'A';
            tilesAmount[tileIdx]++;
            bagCount++;
            if(tilesAmount[tileIdx] > legalAmount[tileIdx]){
                tilesAmount[tileIdx] = legalAmount[tileIdx];
                bagCount--;
                return;
            }
        }

        public int size(){return bagCount;}

        int[] getQuantities(){
            int[] copy = new int[26];
            System.arraycopy(this.tilesAmount,0,copy,0,26);
            return copy;
        }

        public static Bag getBag() {
            if(_instance == null){
                _instance = new Bag();
            }

            return _instance;
        }


    }
}























