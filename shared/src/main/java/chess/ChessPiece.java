package chess;

import java.util.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessPiece.PieceType pieceType;
    private final ChessGame.TeamColor pieceColor;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceType = type;
        this.pieceColor = pieceColor;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> pieceMovesList = new HashSet<>();
        if (this.pieceType == PieceType.BISHOP){
            int currentRow = myPosition.getRow() - 1;
            int currentCol = myPosition.getColumn() - 1;

            while (currentRow >= 1 && currentCol >= 1){
                ChessPosition end_position = new ChessPosition(currentRow, currentCol);

                if (board.getPiece(end_position) != null){
                    if (board.getPiece(end_position).pieceColor != this.pieceColor){
                        pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                        break;
                    } else if (board.getPiece(end_position).pieceColor == this.pieceColor) {
                        break;
                    }
                } else {
                    pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                }

                currentRow = currentRow - 1;
                currentCol = currentCol - 1;
            }

            currentRow = myPosition.getRow() + 1;
            currentCol = myPosition.getColumn() - 1;

            while (currentRow <= 8 && currentCol >= 1){
                ChessPosition end_position = new ChessPosition(currentRow, currentCol);

                if (board.getPiece(end_position) != null){
                    if (board.getPiece(end_position).pieceColor != this.pieceColor){
                        pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                        break;
                    } else if (board.getPiece(end_position).pieceColor == this.pieceColor) {
                        break;
                    }
                } else {
                    pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                }

                currentRow = currentRow + 1;
                currentCol = currentCol - 1;
            }

            currentRow = myPosition.getRow() + 1;
            currentCol = myPosition.getColumn() + 1;

            while (currentRow <= 8 && currentCol <= 8){
                ChessPosition end_position = new ChessPosition(currentRow, currentCol);

                if (board.getPiece(end_position) != null){
                    if (board.getPiece(end_position).pieceColor != this.pieceColor){
                        pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                        break;
                    } else if (board.getPiece(end_position).pieceColor == this.pieceColor) {
                        break;
                    }
                } else {
                    pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                }

                currentRow = currentRow + 1;
                currentCol = currentCol + 1;
            }

            currentRow = myPosition.getRow() - 1;
            currentCol = myPosition.getColumn() + 1;

            while (currentRow >= 1 && currentCol <= 8){
                ChessPosition end_position = new ChessPosition(currentRow, currentCol);

                if (board.getPiece(end_position) != null){
                    if (board.getPiece(end_position).pieceColor != this.pieceColor){
                        pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                        break;
                    } else if (board.getPiece(end_position).pieceColor == this.pieceColor) {
                        break;
                    }
                } else {
                    pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                }

                currentRow = currentRow - 1;
                currentCol = currentCol + 1;
            }
        } else if (this.pieceType == PieceType.KING) {
            int[] rowDirections = {-1, -1, -1, 0, 0, 1, 1, 1};
            int[] colDirections = {-1, 0, 1, -1, 1, -1, 0, 1};

            for (int i = 0; i < rowDirections.length; i++) {
                int row = myPosition.getRow() + rowDirections[i];
                int col = myPosition.getColumn() + colDirections[i];

                if (row >= 1 && row <= 8 && col >= 1 && col <= 8){
                    ChessPosition end_position = new ChessPosition(row, col);

                    if (board.getPiece(end_position) != null){
                        if (board.getPiece(end_position).pieceColor != this.pieceColor){
                            pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                        }
                    } else {
                        pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                    }
                }
            }
        } else if (this.pieceType == PieceType.KNIGHT) {
            int[] rowDirections = {-2, -1, 1, 2, 2, 1, -1, -2};
            int[] colDirections = {1, 2, 2, 1, -1, -2, -2, -1};

            for (int i = 0; i < rowDirections.length; i++) {
                int row = myPosition.getRow() + rowDirections[i];
                int col = myPosition.getColumn() + colDirections[i];

                if (row >= 1 && row <= 8 && col >= 1 && col <= 8){
                    ChessPosition end_position = new ChessPosition(row, col);

                    if (board.getPiece(end_position) != null){
                        if (board.getPiece(end_position).pieceColor != this.pieceColor){
                            pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                        }
                    } else {
                        pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                    }
                }
            }

        } else if (this.pieceType == PieceType.QUEEN){
            int currentRow = myPosition.getRow() - 1;
            int currentCol = myPosition.getColumn() - 1;

            while (currentRow >= 1 && currentCol >= 1){
                ChessPosition end_position = new ChessPosition(currentRow, currentCol);

                if (board.getPiece(end_position) != null){
                    if (board.getPiece(end_position).pieceColor != this.pieceColor){
                        pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                        break;
                    } else if (board.getPiece(end_position).pieceColor == this.pieceColor) {
                        break;
                    }
                } else {
                    pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                }

                currentRow = currentRow - 1;
                currentCol = currentCol - 1;
            }

            currentRow = myPosition.getRow() + 1;
            currentCol = myPosition.getColumn() - 1;

            while (currentRow <= 8 && currentCol >= 1){
                ChessPosition end_position = new ChessPosition(currentRow, currentCol);

                if (board.getPiece(end_position) != null){
                    if (board.getPiece(end_position).pieceColor != this.pieceColor){
                        pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                        break;
                    } else if (board.getPiece(end_position).pieceColor == this.pieceColor) {
                        break;
                    }
                } else {
                    pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                }

                currentRow = currentRow + 1;
                currentCol = currentCol - 1;
            }

            currentRow = myPosition.getRow() + 1;
            currentCol = myPosition.getColumn() + 1;

            while (currentRow <= 8 && currentCol <= 8){
                ChessPosition end_position = new ChessPosition(currentRow, currentCol);

                if (board.getPiece(end_position) != null){
                    if (board.getPiece(end_position).pieceColor != this.pieceColor){
                        pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                        break;
                    } else if (board.getPiece(end_position).pieceColor == this.pieceColor) {
                        break;
                    }
                } else {
                    pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                }

                currentRow = currentRow + 1;
                currentCol = currentCol + 1;
            }

            currentRow = myPosition.getRow() - 1;
            currentCol = myPosition.getColumn() + 1;

            while (currentRow >= 1 && currentCol <= 8){
                ChessPosition end_position = new ChessPosition(currentRow, currentCol);

                if (board.getPiece(end_position) != null){
                    if (board.getPiece(end_position).pieceColor != this.pieceColor){
                        pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                        break;
                    } else if (board.getPiece(end_position).pieceColor == this.pieceColor) {
                        break;
                    }
                } else {
                    pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                }

                currentRow = currentRow - 1;
                currentCol = currentCol + 1;
            }

            currentRow = myPosition.getRow() - 1;
            currentCol = myPosition.getColumn();

            while (currentRow >= 1){
                ChessPosition end_position = new ChessPosition(currentRow, currentCol);

                if (board.getPiece(end_position) != null){
                    if (board.getPiece(end_position).pieceColor != this.pieceColor){
                        pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                        break;
                    } else if (board.getPiece(end_position).pieceColor == this.pieceColor) {
                        break;
                    }
                } else {
                    pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                }

                currentRow = currentRow -1;
            }

            currentRow = myPosition.getRow() + 1;
            currentCol = myPosition.getColumn();

            while (currentRow <= 8){
                ChessPosition end_position = new ChessPosition(currentRow, currentCol);

                if (board.getPiece(end_position) != null){
                    if (board.getPiece(end_position).pieceColor != this.pieceColor){
                        pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                        break;
                    } else if (board.getPiece(end_position).pieceColor == this.pieceColor) {
                        break;
                    }
                } else {
                    pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                }

                currentRow = currentRow +1;
            }

            currentRow = myPosition.getRow();
            currentCol = myPosition.getColumn() - 1;

            while (currentCol >= 1){
                ChessPosition end_position = new ChessPosition(currentRow, currentCol);

                if (board.getPiece(end_position) != null){
                    if (board.getPiece(end_position).pieceColor != this.pieceColor){
                        pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                        break;
                    } else if (board.getPiece(end_position).pieceColor == this.pieceColor) {
                        break;
                    }
                } else {
                    pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                }

                currentCol = currentCol -1;
            }

            currentRow = myPosition.getRow();
            currentCol = myPosition.getColumn() + 1;

            while (currentCol <= 8){
                ChessPosition end_position = new ChessPosition(currentRow, currentCol);

                if (board.getPiece(end_position) != null){
                    if (board.getPiece(end_position).pieceColor != this.pieceColor){
                        pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                        break;
                    } else if (board.getPiece(end_position).pieceColor == this.pieceColor) {
                        break;
                    }
                } else {
                    pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                }

                currentCol = currentCol +1;
            }
        } else if (this.pieceType == PieceType.ROOK) {
            int currentRow = myPosition.getRow() - 1;
            int currentCol = myPosition.getColumn();

            while (currentRow >= 1){
                ChessPosition end_position = new ChessPosition(currentRow, currentCol);

                if (board.getPiece(end_position) != null){
                    if (board.getPiece(end_position).pieceColor != this.pieceColor){
                        pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                        break;
                    } else if (board.getPiece(end_position).pieceColor == this.pieceColor) {
                        break;
                    }
                } else {
                    pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                }

                currentRow = currentRow -1;
            }

            currentRow = myPosition.getRow() + 1;
            currentCol = myPosition.getColumn();

            while (currentRow <= 8){
                ChessPosition end_position = new ChessPosition(currentRow, currentCol);

                if (board.getPiece(end_position) != null){
                    if (board.getPiece(end_position).pieceColor != this.pieceColor){
                        pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                        break;
                    } else if (board.getPiece(end_position).pieceColor == this.pieceColor) {
                        break;
                    }
                } else {
                    pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                }

                currentRow = currentRow +1;
            }

            currentRow = myPosition.getRow();
            currentCol = myPosition.getColumn() - 1;

            while (currentCol >= 1){
                ChessPosition end_position = new ChessPosition(currentRow, currentCol);

                if (board.getPiece(end_position) != null){
                    if (board.getPiece(end_position).pieceColor != this.pieceColor){
                        pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                        break;
                    } else if (board.getPiece(end_position).pieceColor == this.pieceColor) {
                        break;
                    }
                } else {
                    pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                }

                currentCol = currentCol -1;
            }

            currentRow = myPosition.getRow();
            currentCol = myPosition.getColumn() + 1;

            while (currentCol <= 8){
                ChessPosition end_position = new ChessPosition(currentRow, currentCol);

                if (board.getPiece(end_position) != null){
                    if (board.getPiece(end_position).pieceColor != this.pieceColor){
                        pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                        break;
                    } else if (board.getPiece(end_position).pieceColor == this.pieceColor) {
                        break;
                    }
                } else {
                    pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                }

                currentCol = currentCol +1;
            }

        } else if (this.pieceType == PieceType.PAWN) {
            int direction = -1;
            if (this.pieceColor == ChessGame.TeamColor.WHITE){
                direction = 1;
            }

            int row = myPosition.getRow() + direction;
            int col = myPosition.getColumn();

            if (row >= 1 && row <= 8 && col >= 1 && col <= 8){
                ChessPosition end_position = new ChessPosition(row, col);

                if (board.getPiece(end_position) == null) {
                    if ((direction == 1 && row == 8) || (direction == -1 && row == 1)) {
                        pieceMovesList.add(new ChessMove(myPosition, end_position, PieceType.KNIGHT));
                        pieceMovesList.add(new ChessMove(myPosition, end_position, PieceType.QUEEN));
                        pieceMovesList.add(new ChessMove(myPosition, end_position, PieceType.BISHOP));
                        pieceMovesList.add(new ChessMove(myPosition, end_position, PieceType.ROOK));
                    } else{
                        pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                    }

                    if ((direction == 1 && myPosition.getRow() == 2) || (direction == -1 && myPosition.getRow() == 7)) {
                        row += direction;

                        if (row >= 1 && row <= 8) {
                            end_position = new ChessPosition(row, col);

                            if (board.getPiece(end_position) == null) {
                                pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                            }
                        }
                    }
                }
            }

            row = myPosition.getRow() + direction;
            col = myPosition.getColumn() - 1;
            if (row >= 1 && row <= 8 && col >= 1 && col <= 8) {
                ChessPosition end_position = new ChessPosition(row, col);
                if (board.getPiece(end_position) != null && board.getPiece(end_position).pieceColor != this.pieceColor) {
                    if ((direction == 1 && row == 8) || (direction == -1 && row == 1)) {
                        pieceMovesList.add(new ChessMove(myPosition, end_position, PieceType.KNIGHT));
                        pieceMovesList.add(new ChessMove(myPosition, end_position, PieceType.BISHOP));
                        pieceMovesList.add(new ChessMove(myPosition, end_position, PieceType.QUEEN));
                        pieceMovesList.add(new ChessMove(myPosition, end_position, PieceType.ROOK));
                    } else{
                        pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                    }
                }
            }

            col = myPosition.getColumn() + 1;
            if (row >= 1 && row <= 8 && col >= 1 && col <= 8) {
                ChessPosition end_position = new ChessPosition(row, col);
                if (board.getPiece(end_position) != null && board.getPiece(end_position).pieceColor != this.pieceColor) {
                    if ((direction == 1 && row == 8) || (direction == -1 && row == 1)) {
                        pieceMovesList.add(new ChessMove(myPosition, end_position, PieceType.KNIGHT));
                        pieceMovesList.add(new ChessMove(myPosition, end_position, PieceType.BISHOP));
                        pieceMovesList.add(new ChessMove(myPosition, end_position, PieceType.QUEEN));
                        pieceMovesList.add(new ChessMove(myPosition, end_position, PieceType.ROOK));
                    } else{
                        pieceMovesList.add(new ChessMove(myPosition, end_position, null));
                    }
                }
            }
        }

        return pieceMovesList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessPiece that)) return false;
        return pieceType == that.pieceType && pieceColor == that.pieceColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceType, pieceColor);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceType=" + pieceType +
                ", pieceColor=" + pieceColor +
                '}';
    }
}
