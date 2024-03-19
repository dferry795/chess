package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

public class ChessboardUI {
    private final ChessBoard gameBoard;

    public ChessboardUI(ChessBoard gameBoard){
        this.gameBoard = gameBoard;
    }

    public void buildBoard(){
        System.out.println(ERASE_SCREEN);

        this.gameBoard.resetBoard();

        var columns = new String[]{"   ", " a " , " b ", " c ", " d ", " e ", " f ", " g ", " h ", "   "};

        String outputBoard = "";

        var checkerFlag = true;

        for (int i = 0; i <= 9; i++){
            for (int j = 9; j >= 0; j--){
                if (i == 0 || i == 9){
                    outputBoard += SET_TEXT_COLOR_BLACK;
                    outputBoard += SET_BG_COLOR_LIGHT_GREY;
                    outputBoard += columns[j];
                } else {
                    if (j == 0 || j == 9){
                        outputBoard += SET_TEXT_COLOR_BLACK;
                        outputBoard += SET_BG_COLOR_LIGHT_GREY;
                        outputBoard = outputBoard + " " + String.valueOf(i) + " ";
                    } else {
                        if (checkerFlag){
                            outputBoard += SET_BG_COLOR_WHITE;
                        } else {
                            outputBoard += SET_BG_COLOR_BLACK;
                        }

                        var spotPiece = this.gameBoard.getPiece(new ChessPosition(i, j));

                        if (spotPiece == null){
                            outputBoard += "   ";
                        } else {
                            outputBoard += evalPiece(spotPiece);
                        }
                    }
                    if (j != 9){
                        checkerFlag = !checkerFlag;
                    }
                }
            }
            outputBoard += SET_BG_COLOR_BLACK;
            outputBoard += "\n";
        }

        outputBoard += SET_BG_COLOR_BLACK;
        outputBoard += "\n";
        checkerFlag = true;

        for (int i = 9; i >= 0; i--){
            for (int j = 0; j <= 9; j++){
                if (i == 0 || i == 9){
                    outputBoard += SET_TEXT_COLOR_BLACK;
                    outputBoard += SET_BG_COLOR_LIGHT_GREY;
                    outputBoard += columns[j];
                } else {
                    if (j == 0 || j == 9){
                        outputBoard += SET_TEXT_COLOR_BLACK;
                        outputBoard += SET_BG_COLOR_LIGHT_GREY;
                        outputBoard = outputBoard + " " + String.valueOf(i) + " ";
                    } else {
                        if (checkerFlag){
                            outputBoard += SET_BG_COLOR_WHITE;
                        } else {
                            outputBoard += SET_BG_COLOR_BLACK;
                        }

                        var spotPiece = this.gameBoard.getPiece(new ChessPosition(i, j));

                        if (spotPiece == null){
                            outputBoard += "   ";
                        } else {
                            outputBoard += evalPiece(spotPiece);
                        }
                    }
                    if (j != 9){
                        checkerFlag = !checkerFlag;
                    }
                }
            }
            outputBoard += SET_BG_COLOR_BLACK;
            outputBoard += "\n";
        }

        System.out.print(outputBoard);
    }

    public String evalPiece(ChessPiece piece){
        String resultString = "";

        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            resultString += SET_TEXT_COLOR_BLUE;

            if (piece.getPieceType() == ChessPiece.PieceType.KING){
                resultString += WHITE_KING;
            } else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                resultString += WHITE_BISHOP;
            } else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                resultString += WHITE_QUEEN;
            } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                resultString += WHITE_ROOK;
            } else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                resultString += WHITE_KNIGHT;
            } else if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                resultString += WHITE_PAWN;
            }
        } else if (piece.getTeamColor() == ChessGame.TeamColor.BLACK){
            resultString += SET_TEXT_COLOR_RED;

            if (piece.getPieceType() == ChessPiece.PieceType.KING){
                resultString += BLACK_KING;
            } else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                resultString += BLACK_BISHOP;
            } else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                resultString += BLACK_QUEEN;
            } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                resultString += BLACK_ROOK;
            } else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                resultString += BLACK_KNIGHT;
            } else if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                resultString += BLACK_PAWN;
            }
        }

        return resultString;
    }
}
