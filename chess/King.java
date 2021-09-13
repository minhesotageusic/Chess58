package chess;

/**
 * @author Yunhao Xu
 * @author Minhesota Geusic
 * @version %I% %G%
 * @since 1.2
 */
public class King extends Piece {
	/**
	 * a boolean field to determine whether king has moved yet
	 */
	private boolean firstMove;

	/**
	 * constructor
	 *
	 * @param player the player that holds the this king
	 */
	public King(String player){
		super(player);
		this.name = "K";
		this.firstMove = true;
	}

	/**
	 * determines if this king's movement is legal, including all kinds of special movements, for example, castling
	 *
	 * @param curr_row current row that the king is in
	 * @param curr_col current column that the king is in
	 * @param new_row row the king moves to
	 * @param new_col column the king moves to
	 * @param cb chessboard
	 * @return true if the movement is legal, otherwise false
	 */
	@Override
	public boolean isMoveLegal(int curr_row, int curr_col, int new_row, int new_col, ChessBoard cb) {
		int resCol = Math.abs(new_col - curr_col);
		int resRow = Math.abs(new_row - curr_row);
		if(resCol == 0 && resRow == 0) return false;
		//only move on one coordinate away
		// castling
		// 1. it is king's first move 
		// 2. it is the rook's first move, towards which the king castles 
		// 3. there is empty in between king and the rook
		// 4. King cannot go through the check square or go onto the check square
		// 5. King cannot castle if it is currently under check
		if(( resCol == 1 && resRow <= 1) || (resRow == 1 && resCol <= 1)) {
			firstMove = false;
			return true;
		}
		if (resRow == 0 && curr_col - new_col == 2 && this.firstMove) {  // castle left
			Piece rook = cb.getPiece(curr_row, 0);
			if (rook == null || !(rook instanceof Rook) || ((Rook)rook).hasMoved()) { // check if rook side is valid
				return false;
			}
			for (int j = curr_col-1; j > 0; j--) if (cb.getPiece(curr_row, j) != null) return false;
			if (underCheck(curr_row, curr_col,cb) || underCheck(curr_row, curr_col-1, cb) || underCheck(new_row, new_col, cb)) { 
				// check if anywhere in the path from king's current position to destination is under check 
				return false;
			}

			// castling successfully. Rook will move accordingly.
			rook = cb.removePiece(curr_row, 0);
			if (rook != null) cb.addPiece(curr_row, new_col+1, rook); 
			this.firstMove = false;  // do not have to update rook's firstMove, since king has moved, in no way can castling be done for this player again	
			return true;
		}
		if (resRow == 0 && new_col - curr_col == 2 && this.firstMove) { // castle right
			Piece rook = cb.getPiece(curr_row, 7);
			if (rook == null || !(rook instanceof Rook) || ((Rook)rook).hasMoved()) { // check if rook side is valid
				return false;
			}
			for (int j = curr_col+1; j < 7; j++) if (cb.getPiece(curr_row, j) != null) return false;
			if (underCheck(curr_row, curr_col,cb) || underCheck(curr_row, curr_col+1,cb) || underCheck(new_row, new_col,cb)) { 
		        // check if anywhere in the path from king's current position to destination is under check 
				return false;
			}
			
			// castling successfully. Rook will move accordingly.
			rook = cb.removePiece(curr_row, 7);
			if (rook != null) cb.addPiece(curr_row, new_col-1, rook); 
			this.firstMove = false;  // do not have to update rook's firstMove, since king has moved, in no way can castling be done for this player again	
			return true;
		}
		return false;
	}
	
	/** 
	 * check if the square king is in is under check
	 *
	 * @param kingRow row the king is in
	 * @param kingCol column the king is in
	 * @param cb chessboard
	 * @return true if the current square is under check, or else false
	 */ 
	private boolean underCheck (int kingRow, int kingCol, ChessBoard cb) {
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				Piece curr_piece = cb.getPiece(r,c);
				// if it is null or current player's piece, we can continue
				if (curr_piece == null) continue; 
				if (curr_piece.getPlayer().equals(getPlayer())) continue;

				// test if the current piece can put the opponent's king under check
				if (curr_piece.isMoveLegal(r, c, kingRow, kingCol, cb)) return true;

			}
		}
		return false;
	}

	/**
	 * determines if it is this king's first move
	 *
	 * @return true if king has not moved yet, or else false
	 */
	boolean isFirstMove() {
		return this.firstMove;
	}

}
