package view;

import javafx.beans.property.StringProperty;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Draggable {

    private double mouseAnchorX;
    private double mouseAnchorY;
    private double initialTranslateX;
    private double initialTranslateY;

    private GridPane grid;
    private ImageView imageView;
    private StringProperty letter;

    private boolean inGrid = false;
    private Rectangle rect;
    public static ArrayList<BoardTile> boardTiles = new ArrayList<>();

    public static StringBuilder word = new StringBuilder();

    public int row, col;



    public Draggable(GridPane grid, ImageView imageView, StringProperty letter) {
        this.grid = grid;
        this.imageView = imageView;
        this.letter = letter;
    }

    public void makeDraggable() {
        imageView.setOnMousePressed((MouseEvent event) -> {
            if(inGrid){
                imageView.setTranslateX(rect.getTranslateX());
                imageView.setTranslateY(rect.getTranslateY());
                return;
            }
            mouseAnchorX = event.getSceneX();
            mouseAnchorY = event.getSceneY();
            initialTranslateX = imageView.getTranslateX();
            initialTranslateY = imageView.getTranslateY();
            imageView.getParent().toFront();
        });

        imageView.setOnMouseDragged((MouseEvent event) -> {
            if(inGrid){
                imageView.setTranslateX(rect.getTranslateX());
                imageView.setTranslateY(rect.getTranslateY());
                return;
            }
            double deltaX = event.getSceneX() - mouseAnchorX;
            double deltaY = event.getSceneY() - mouseAnchorY;
            imageView.setTranslateX(initialTranslateX + deltaX);
            imageView.setTranslateY(initialTranslateY + deltaY);
        });

        imageView.setOnMouseReleased((MouseEvent event) -> {
            // Check if the ImageView was released on top of a rectangle
            if(inGrid){
                imageView.setTranslateX(rect.getTranslateX());
                imageView.setTranslateY(rect.getTranslateY());
                return;
            }
            Bounds imageViewBounds = imageView.localToScene(imageView.getBoundsInLocal());
            for (Node rectangle : grid.getChildren()) {
                Bounds rectangleBounds = rectangle.localToScene(rectangle.getBoundsInLocal());
                if (imageViewBounds.intersects(rectangleBounds)) {
                    if(checkIfGridIsTaken((Rectangle) rectangle)){
                        break;
                    }
                    int columnIndex = GridPane.getColumnIndex(rectangle);
                    int rowIndex = GridPane.getRowIndex(rectangle);
                    grid.add(imageView, columnIndex, rowIndex);
                    inGrid = true;
                    rect = (Rectangle) rectangle;
                    WindowController.tilesPlaced.add(this);
                    word.append(letter.get());

                    BoardTile boardTile = new BoardTile(letter, rowIndex, columnIndex);
                    boardTiles.add(boardTile);
                    row = rowIndex;
                    col = columnIndex;
                    break;
                }
            }
            // Reset the ImageView's position
            imageView.setTranslateX(initialTranslateX);
            imageView.setTranslateY(initialTranslateY);
        });

    }

    public boolean checkIfGridIsTaken(Rectangle rect){
        for(BoardTile bt: WindowController.totalTilesInBoard){
            int columnIndex = GridPane.getColumnIndex(rect);
            int rowIndex = GridPane.getRowIndex(rect);
            if(bt.getRow() == rowIndex && bt.getCol() == columnIndex)
                return true;
        }

        for(BoardTile bt: boardTiles){
            int columnIndex = GridPane.getColumnIndex(rect);
            int rowIndex = GridPane.getRowIndex(rect);
            if(bt.getRow() == rowIndex && bt.getCol() == columnIndex)
                return true;
        }
        return false;
    }

    public void undoPlacement(){
        imageView.setTranslateX(0.0);
        imageView.setTranslateY(0.0);
        inGrid = false;
        rect = null;
        grid.getChildren().remove(imageView);
        boardTiles.remove(word.length()-1);
        word.deleteCharAt(word.length()-1);
    }

    public ImageView getImageView(){return imageView;}

    public boolean getInGrid(){return inGrid;}

    public StringProperty getLetter(){return letter;}

    public GridPane getGrid(){return grid;}

}
