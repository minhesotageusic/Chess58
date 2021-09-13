package chess;

import static java.lang.Math.abs;
/**
 * @author Yunhao Xu
 * @author Minhesota Geusic
 * @version %I% %G%
 * @since 1.2
 */
public class Pawn extends Piece {
	/**
	 * a boolean value indicates whether this piece has moved
	 */
	private boolean firstMove = true;

	/**
	 * a boolean value shows whether the last move was first time moving
	 */
	private boolean lastMoveWasFirstMove = false;

	/**
	 * a boolean value refers to specific player's piece
	 */
	private boolean moveUp = false;

	/**
	 * an integer keeps track of number of movements made
	 */
	private int moved = 0;

	/**
	 * determines if the last movement was first time moving for this piece
	 *
	 * @return true if last movement was the first one, otherwise false
	 */
	public boolean wasLastMoveFirstMove() {
		return lastMoveWasFirstMove;
	}

	/**
	 * constructor
	 *
	 * @param player the player who holds this piece
	 * @param moveUp indicates if this piece should move up or down
	 */
	public Pawn(String player, boolean moveUp) {
		super(player);
		this.player = player;
		this.name = "p";
		this.moveUp = moveUp;
	}
	
	/**
	 * determines if this Pawn's movement is legal, including all kinds of special movements, such as one-square, diagonal,
	 * and en passant movement 
	 *
	 * @param curr_row current row that the Pawn is in
	 * @param curr_col current column that the Pawn is in
	 * @param new_row row the Pawn moves to
	 * @param new_col column the Pawn moves to
	 * @param cb chessboard
	 * @return true if the movement is legal, otherwise false
	 */
	@Override
	public boolean isMoveLegal(int curr_row, int curr_col, int new_row, int new_col, ChessBoard cb) {
		//enpassant:
		// 1.ally's pawn must have moved exactly 3 squares
		// 2.opponent's pawn must have moved 2 squares as its first move
		// 3.opponent's pawn must be right next to the pawn
		// 4.most recent opponent's move must be pawn's 
		int diffRow = abs(curr_row - new_row);
		int diffCol = abs(curr_col - new_col);
		
		// ensure pawn cannot move on same tile, and cannot be moving "backward"
		if (diffRow == 0 && diffCol == 0 || (moveUp && new_row <= curr_row) || (!moveUp && new_row >= curr_row))
			return false;
		// pawn's movement
		// first move, can allow move up to 
		// two tile
		if (firstMove) {
			// determine if there are no pieces in the way,
			// pawn cannot capture using forward moves
			if (diffRow == 2 && diffCol == 0 && ((moveUp && cb.getPiece(curr_row + 1, curr_col) == null)
					|| (!moveUp && cb.getPiece(curr_row - 1, curr_col) == null)) && cb.getPiece(new_row, new_col) == null) {
				moved += 2;
				// move up
				if (moveUp) {
					firstMove = false;
					lastMoveWasFirstMove = true;
					return true;
				}
				// move down
				else {
					firstMove = false;
					lastMoveWasFirstMove = true;
					return true;
				}
			}
		}
		//move forward by one tile
		if (diffRow == 1) {
			// we can only move forward if there are no pieces in the way
			if (diffCol == 0 && cb.getPiece(new_row, new_col) == null && 
					((moveUp && new_row > curr_row)
					|| (!moveUp && new_row < curr_row))) {
				if(firstMove) lastMoveWasFirstMove = true;
				else lastMoveWasFirstMove = false;
				firstMove = false;
				moved += 1;	
				return true;
			}
			// we can move diagonally if there exist an enemy piece
			// this is our captor move
			if (diffCol == 1 && cb.getPiece(new_row, new_col) != null) {
				if(firstMove) lastMoveWasFirstMove = true;
				else lastMoveWasFirstMove = false;
				firstMove = false;
				moved += 1;
				return true;
			}
			
			// enpassant	
			if (diffCol == 1 && cb.getPiece(new_row, new_col) == null) {
				// check the piece on the left. if it is true, do enPassant on the left captured pawn and move accordingly
				if (conditionForEnPassant(curr_row, curr_col-1, cb.getPiece(curr_row, curr_col).getPlayer(), cb)) {
					moved += 1; 
					cb.removePiece(curr_row, curr_col-1);  // don't have to update firstMove or anything since, to use enpassant, pawn must have moved 3 squares
					return true;
				} 
				// check the piece on the right. if true, do enPassant on the right captured pawn and move accordingly
				if (conditionForEnPassant(curr_row, curr_col+1, cb.getPiece(curr_row, curr_col).getPlayer(), cb)) {
					moved += 1;
					cb.removePiece(curr_row, curr_col+1); // don't have to update firstMove or anything
					return true;
				}	
				
			}
		}
		return false;
	}
	/**
	 * check whether the condition for using En Passant gets fulfilled
	 *
	 * @param row row of captured pawn
	 * @param col column of captured pawn
	 * @param currentPlayer player who uses enPassant
	 * @param cb chessboard
	 * @return true if the current player can use enPassant, other false
	 */
	public boolean conditionForEnPassant (int row, int col, String currentPlayer, ChessBoard cb) {
		//if (numberOfMoves() != 3) return false;   // it must have moved exactly 3 squares to use enpassant
		Piece piece = cb.getPiece(row, col);
		if (piece == null || !(piece instanceof Pawn) || piece.getPlayer().equals(currentPlayer)) return false; // captured pawn does not exist 
		Pawn pawn = (Pawn)piece;
		Pair lastMovedPiece = cb.lastPieceMoved();
		if (lastMovedPiece.x != row || lastMovedPiece.y != col) return false; // the captured pawn was not the most recently moved one
		if (!pawn.wasLastMoveFirstMove() || pawn.numberOfMoves() != 2) return false; // The captured pawn's last move was not a jump two square
		return true;	
	} 
	/**
	 * get the number of squares that the pawn has moved
	 *
	 * @return a number that indicates how many squares the pawn has moved
	 */
	public int numberOfMoves () {
		return moved;
	}

}
