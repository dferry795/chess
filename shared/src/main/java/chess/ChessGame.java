package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    public Boolean resign = false;

    private TeamColor teamTurn;
    private ChessBoard board;

    public ChessGame() {
        this.teamTurn = setTeamTurn(TeamColor.WHITE);
        this.board = new ChessBoard();
    }

    public ChessGame copy(){
        ChessGame newGame = new ChessGame();
        ChessBoard newBoard = new ChessBoard();

        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPiece piece = this.board.getPiece(new ChessPosition(row, col));
                if (piece != null) {
                    newBoard.addPiece(new ChessPosition(row, col), piece);
                }
            }
        }

        newGame.setBoard(newBoard);
        newGame.setTeamTurn(this.getTeamTurn());
        return newGame;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public TeamColor setTeamTurn(TeamColor team) {
        return this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = this.board.getPiece(startPosition);

        if (piece == null){
            return null;
        }

        Collection<ChessMove> allMoves = piece.pieceMoves(this.board, startPosition);
        Collection<ChessMove> validMoves = new HashSet<>();

        for (ChessMove move : allMoves){
            try {
                ChessGame tempGame = this.copy();
                tempGame.setTeamTurn(piece.getTeamColor());
                tempGame.makeMove(move);

                if (!tempGame.isInCheck(piece.getTeamColor())) {
                    validMoves.add(move);
                }
            }catch (InvalidMoveException ex){
                continue;
            }
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = this.board.getPiece(move.getStartPosition());

        if (!resign) {
            if (piece != null) {
                if (piece.getTeamColor() != this.getTeamTurn()) {
                    throw new InvalidMoveException("Not teams turn");
                }

                Collection<ChessMove> movesSet = piece.pieceMoves(this.board, move.getStartPosition());

                if (movesSet.contains(move)) {
                    this.board.addPiece(move.getStartPosition(), null);
                    this.board.addPiece(move.getEndPosition(), piece);

                    if (this.isInCheck(piece.getTeamColor())) {
                        this.board.addPiece(move.getStartPosition(), piece);
                        this.board.addPiece(move.getEndPosition(), null);
                        throw new InvalidMoveException("Cannot put king in check");
                    } else {
                        if (move.getPromotionPiece() != null) {
                            ChessPiece promoPiece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
                            this.board.addPiece(move.getEndPosition(), promoPiece);
                        }
                        if (this.getTeamTurn() == TeamColor.WHITE) {
                            this.setTeamTurn(TeamColor.BLACK);
                        } else {
                            this.setTeamTurn(TeamColor.WHITE);
                        }
                    }
                } else {
                    throw new InvalidMoveException("Invalid Move");
                }
            } else {
                throw new InvalidMoveException();
            }
        } else {
            throw new InvalidMoveException("Cannot make move");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPos = null;

        outerloop:
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                ChessPosition checkPos = new ChessPosition(i, j);

                if (this.board.getPiece(checkPos) != null){
                    if (this.board.getPiece(checkPos).getPieceType() == ChessPiece.PieceType.KING && this.board.getPiece(checkPos).getTeamColor() == teamColor){
                        kingPos = checkPos;
                        break outerloop;
                    }
                }
            }
        }

        if (kingPos == null){
            return false;
        }

        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                ChessPosition checkPos = new ChessPosition(i, j);
                ChessPiece piece = this.board.getPiece(checkPos);

                if (piece != null){
                    if (piece.getTeamColor() != teamColor){
                        Collection<ChessMove> movesSet = piece.pieceMoves(this.board, checkPos);
                        ChessMove kingMove = new ChessMove(checkPos, kingPos, null);
                        ChessMove promoMove = new ChessMove(checkPos, kingPos, ChessPiece.PieceType.QUEEN);

                        if (movesSet.contains(kingMove) || movesSet.contains(promoMove)){
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (this.isInCheck(teamColor)) {
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 8; j++) {
                    ChessPosition piecePos = new ChessPosition(i, j);
                    ChessPiece piece = this.board.getPiece(piecePos);

                    if (piece != null && piece.getTeamColor() == teamColor) {
                        Collection<ChessMove> movesSet = this.validMoves(piecePos);
                        for (ChessMove move : movesSet) {
                            try {
                                ChessGame tempGame = this.copy();
                                tempGame.makeMove(move);
                                tempGame.setTeamTurn(teamColor);

                                if(!tempGame.isInCheck(teamColor)){
                                    return false;
                                }

                            } catch (InvalidMoveException e) {
                                continue;
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 8; j++) {
                    ChessPosition piecePos = new ChessPosition(i, j);
                    ChessPiece piece = this.board.getPiece(piecePos);

                    if (piece != null && piece.getTeamColor() == teamColor) {
                        Collection<ChessMove> movesSet = this.validMoves(piecePos);
                        if (!movesSet.isEmpty()) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessGame chessGame)) return false;
        return teamTurn == chessGame.teamTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "teamTurn=" + teamTurn +
                ", board=" + board +
                '}';
    }


}
