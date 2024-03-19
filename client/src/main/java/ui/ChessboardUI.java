package ui;

import chess.ChessBoard;
import static ui.EscapeSequences.*;

public class ChessboardUI {
    private final ChessBoard gameBoard;

    public ChessboardUI(ChessBoard gameBoard){
        this.gameBoard = gameBoard;
    }

    public void buildBoard(){
        System.out.println(ERASE_SCREEN);

        var columns = new String[]{"   ", " a " , " b ", " c ", " d ", " e ", " f ", " g ", " h ", "   "};

        String outputBoard = "";

        for (int i = 0; i <= 9; i++){
            for (int j = 0; j<= 9; j++){
                if (i == 0 || i == 9){
                    outputBoard += SET_TEXT_COLOR_BLACK;
                    outputBoard += SET_BG_COLOR_LIGHT_GREY;
                    outputBoard += columns[j];
                }
            }
            outputBoard += SET_BG_COLOR_BLACK;
            outputBoard += "\n";
        }

        System.out.print(outputBoard);
    }
}
