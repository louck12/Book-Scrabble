package model.board;

import java.util.ArrayList;

public class Board {
    private static Board _instance = null;
    private Tile[][] board= new Tile[15][15];
    private String bonus[][] = new String[15][15];
    private boolean firstWord = true;
    private Board(){
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                board[i][j] = null;
            }
        }
        bonus[0][0] = "TW";
        bonus[0][7] = "TW";
        bonus[0][14] = "TW";
        bonus[7][0] = "TW";
        bonus[7][14] = "TW";
        bonus[14][0] = "TW";
        bonus[14][7] = "TW";
        bonus[14][14] = "TW";
        bonus[1][1] = "DW";
        bonus[1][13] = "DW";
        bonus[2][2] = "DW";
        bonus[2][12] = "DW";
        bonus[3][3] = "DW";
        bonus[3][11] = "DW";
        bonus[4][4] = "DW";
        bonus[4][10] = "DW";
        bonus[7][7] = "DW";
        bonus[10][4] = "DW";
        bonus[10][10] = "DW";
        bonus[11][3] = "DW";
        bonus[11][11] = "DW";
        bonus[12][2] = "DW";
        bonus[12][12] = "DW";
        bonus[13][1] = "DW";
        bonus[13][13] = "DW";
        bonus[1][5] = "TL";
        bonus[1][9] = "TL";
        bonus[5][1] = "TL";
        bonus[5][5] = "TL";
        bonus[5][9] = "TL";
        bonus[5][13] = "TL";
        bonus[9][1] = "TL";
        bonus[9][5] = "TL";
        bonus[9][9] = "TL";
        bonus[9][13] = "TL";
        bonus[13][5] = "TL";
        bonus[13][9] = "TL";
        bonus[0][3] = "DL";
        bonus[0][11] = "DL";
        bonus[2][6] = "DL";
        bonus[2][8] = "DL";
        bonus[3][0] = "DL";
        bonus[3][7] = "DL";
        bonus[3][14] = "DL";
        bonus[6][2] = "DL";
        bonus[6][6] = "DL";
        bonus[6][8] = "DL";
        bonus[6][12] = "DL";
        bonus[7][3] = "DL";
        bonus[7][11] = "DL";
        bonus[8][2] = "DL";
        bonus[8][6] = "DL";
        bonus[8][8] = "DL";
        bonus[8][12] = "DL";
        bonus[11][0] = "DL";
        bonus[11][7] = "DL";
        bonus[11][14] = "DL";
        bonus[12][6] = "DL";
        bonus[12][8] = "DL";
        bonus[14][3] = "DL";
        bonus[14][11] = "DL";


    }

    public static Board getBoard(){
        if(_instance == null){
            _instance = new Board();
        }

        return _instance;
    }

    public Tile[][] getTiles(){
        Tile[][] boardCopy= new Tile[15][15];

        for (int i = 0; i < 15; i++) {
            System.arraycopy(board[i], 0, boardCopy[i], 0,15);
        }

        return boardCopy;
    }

    public boolean boardLegal(Word word){

        int i;
        boolean foundLinkedTile = false;
        int col = word.getCol();
        int row = word.getRow();
        int wordLen = word.getWord().length;

        //check if the word is in the bounds of the board

        if(col < 0 || col >= 15)
            return false;
        if(row < 0 || row >= 15)
            return false;


        //HORIZONTAL WORD
        if(!word.getVertical()){

            if( (col + wordLen - 1) >= 15)
                return false;

            //check the star slot
            if(firstWord){
                if(checkHorizontalFirstWord(word)){
                    return true;
                }
               return false;
            }

            else{

                //on the row of the star slot
                if(row == 7 && wordLen + col >= 6){
                    return true;
                }


                for(i = 0; i < wordLen; i++){


                    if (board[row][col + i] != null && word.getWord()[i] != null)
                        return false;

                    if(board[row][col + i] == null && word.getWord()[i] == null)
                        return false;

                    if(row != 14){
                        if(board[row + 1][col + i] != null){
                            foundLinkedTile = true;
                            break;
                        }


                    }

                    if(row != 0){
                        if(board[row - 1][col + i] != null){
                            foundLinkedTile = true;
                            break;
                        }
                    }

                }


                if(foundLinkedTile){
                    return true;
                }
                return false;


            }

        }

        //VERTICAL WORD
        else {
            if( (row + wordLen - 1) >= 15)
                return false;

            //check the star slot
            if(firstWord){
                if(checkVerticalFirstWord(word)){
                    return true;
                }
                return false;
            }

            else{

                //on the column of the star slot
                if(col == 7 && wordLen + row >= 6){
                    //boardWords.add(word);
                    return true;
                }

                for(i = 0; i < wordLen; i++){

                    if(board[row + i][col] != null && word.getWord()[i] != null)
                        return false;

                    if(board[row + i][col] == null && word.getWord()[i] == null)
                        return false;

                    if(col != 14){
                        if(board[row + i][col + 1] != null){
                            foundLinkedTile = true;
                            break;
                        }
                    }

                    if(col != 0){
                        if(board[row + i][col - 1] != null){
                            foundLinkedTile = true;
                            break;
                        }
                    }

                }


                if(foundLinkedTile){
                    return true;
                }
                return false;

            }
        }

    }

    public boolean dictionaryLegal(Word word){
        return true;
    }

    public ArrayList<Word> getWords(Word word){
        ArrayList<Word> before = new ArrayList<Word>();
        ArrayList<Word> after = new ArrayList<Word>();

        before = currentBoardWords();

        int row = word.getRow();
        int col = word.getCol();
        int wordLen = word.getWord().length;

        for(int i = 0; i < wordLen; i++){

            if(word.getVertical() && word.getWord()[i] != null){
                board[row + i][col] = word.getWord()[i];
            }

            else if(!word.getVertical() && word.getWord()[i] != null){
                board[row][col + i] = word.getWord()[i];

            }

        }


        after = currentBoardWords();

        for(int i = 0; i < wordLen; i++){

            if(word.getWord()[i] == null) continue;

            if(word.getVertical()){
                board[row + i][col] = null;
            }

            else{
                board[row][col + i] = null;

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

    public boolean compareTwoWords(Word w1, Word w2){
        if(w1.getWord().length != w2.getWord().length) return false;
        for(int i = 0; i < w1.getWord().length; i++){
            if(w1.getWord()[i] != w2.getWord()[i]) return false;
        }
        return true;
    }

    public int getScore(Word word){
        int row = word.getRow();
        int col = word.getCol();
        int wordLen = word.getWord().length;

        boolean doubleWordScore = false;
        int countDW = 0;

        boolean tripleWordScore = false;
        int countTW = 0;

        int totalScore = 0;
        int wordIdx = 0;



        if(word.isVertical()){
            for(int i = row; i < row + wordLen; i++){
                if(bonus[i][col] == "TW"){
                    totalScore += word.getWord()[wordIdx++].score;
                    countTW++;
                    tripleWordScore = true;
                }

                else if(bonus[i][col] == "DW"){

                    if(i == 7 && col == 7){
                        //on star square
                        if(board[i][col] != null) {
                            totalScore += word.getWord()[wordIdx++].score;
                            continue;
                        }

                    }
                    totalScore += word.getWord()[wordIdx++].score;
                    countDW++;
                    doubleWordScore = true;
                }

                else if(bonus[i][col] == "TL"){
                    totalScore = totalScore + (3 * word.getWord()[wordIdx++].score);
                }

                else if(bonus[i][col] == "DL"){
                    totalScore = totalScore + (2 * word.getWord()[wordIdx++].score);
                }
                else{
                    totalScore += word.getWord()[wordIdx++].score;
                }
            }
        }

        else{
            for(int i = col; i < col + wordLen; i++){
                if(bonus[row][i] == "TW"){
                    totalScore += word.getWord()[wordIdx++].score;
                    countTW++;
                    tripleWordScore = true;
                }

                else if(bonus[row][i] == "DW"){

                    if(row == 7 && i == 7){
                        if(board[row][i] != null){
                            totalScore += word.getWord()[wordIdx++].score;
                            continue;
                        }
                    }

                    totalScore += word.getWord()[wordIdx++].score;
                    countDW++;
                    doubleWordScore = true;
                }

                else if(bonus[row][i] == "TL"){
                    totalScore = totalScore + (3 * word.getWord()[wordIdx++].score);
                }

                else if(bonus[row][i] == "DL"){
                    totalScore = totalScore + (2 * word.getWord()[wordIdx++].score);
                }
                else{
                    totalScore += word.getWord()[wordIdx++].score;
                }
            }
        }

        if(tripleWordScore){
            for(int i = 0; i < countTW; i++){
                totalScore = totalScore * 3;
            }
        }

        if(doubleWordScore){
            for(int i = 0; i < countDW; i++){
                totalScore = totalScore * 2;
            }
        }

        return totalScore;
    }



    public boolean checkVerticalFirstWord(Word word){

        if(word.getCol() != 7) return false;

        for(int i = word.getRow(); i < word.getRow() + word.getWord().length; i++){
            if(i == 7){
                return true;
            }
        }
        return false;
    }

    public boolean checkHorizontalFirstWord(Word word){

        if(word.getRow() != 7) return false;
        for(int i = word.getCol(); i < word.getCol() + word.getWord().length; i++){
            if(i == 7){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Word> currentBoardWords(){

        int start = 0;
        int end = 0;
        Word word ;
        ArrayList<Tile> tiles = new ArrayList<Tile>();
        ArrayList<Word> boardWords = new ArrayList<Word>();


        //checking horizontal words
        for(int row = 0; row < 15; row++){
            start = 0;
            end = 0;
            while(true){
                if(end > 14) break;
                for(start = end; start <= 14 && board[row][start] == null; start++);
                if(start > 14) break;
                for(end = start; end <= 14 && board[row][end] != null; end++);
                if(start != end - 1){
                    for(int i = start; i < end; i++){
                        tiles.add(board[row][i]);
                    }

                    word = new Word(tiles.toArray(new Tile[tiles.size()]), row, start, false);
                    boardWords.add(word);
                    tiles.clear();

                }
            }

        }

        //checking vertical words
        for(int col = 0; col < 15; col++){
            start = 0;
            end = 0;
            while(true){
                if(end > 14) break;
                for(start = end; start <= 14 && board[start][col] == null; start++);
                if(start > 14) break;
                for(end = start; end <= 14 && board[end][col] != null; end++);
                if(start != end - 1){
                    for(int i = start; i < end; i++){
                        tiles.add(board[i][col]);
                    }
                    word = new Word(tiles.toArray(new Tile[tiles.size()]), start, col, true);
                    boardWords.add(word);
                    tiles.clear();
                }
            }
        }

        return boardWords;
    }



    public int tryPlaceWord(Word word) {

        int row = word.getRow();
        int col = word.getCol();
        int wordLen = word.getWord().length;


        if(!boardLegal(word))
            return 0;

        ArrayList<Word> newWords = new ArrayList<Word>();
        int totalScore = 0;
        newWords = getWords(word);
        for(Word w: newWords){
            if(!dictionaryLegal(w))
                return 0;


            totalScore += getScore(w);
        }

        if(firstWord)
            firstWord = false;

        //word placement

        for(int i = 0; i < wordLen; i++){

            //VERTICAL
            if(word.getVertical()){

                if(word.getWord()[i] == null && board[row + i][col] != null){
                    continue;
                }
                board[row + i][col] = word.getWord()[i];
            }

            else{
                if(word.getWord()[i] == null && board[row][col + i] != null){
                    continue;
                }
                board[row][col + i] = word.getWord()[i];
            }
        }


        return totalScore;

    }
}

