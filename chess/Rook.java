package chess;

import java.lang.Math;
/**
 * @author Yunhao Xu
 * @author Minhesota Geusic
 * @version %I% %G%
 * @since 1.2
 */
public class Rook extends Piece {
	/**
	 * a boolean valud moved indicates if the piece has moved
	 */
	private boolean firstMove = false;
	/**
	 * Return whether or not this piece has moved from its original spot
	 *
	 * @return true if it has moved, otherwise false
	 */
	public boolean hasMoved (){
		return firstMove;
	}

	/**
	 * constructor
	 *
	 * @param player the player who holds this piece
	 */
	public Rook (String player) {
		super(player);
		this.name = "R"; 
	}	

	/**
	 * determines if this Rook's movement is legal, including all kinds of movements, such as vertically, and horizontally	
	 *
	 * @param curr_row current row that the Rook is in
	 * @param curr_col current column that the Rook is in
	 * @param new_row row the Rook moves to
	 * @param new_col column the Rook moves to
	 * @param cb chessboard
	 * @return true if the movement is legal, otherwise false
	 */
	@Override
	public boolean isMoveLegal(int curr_row, int curr_col, int new_row, int new_col, ChessBoard cb) {
		// implement castling
		// Rook can move 1 to 7 squares in any direction so long as there is no obstruct in its path
		if (curr_col == new_col) {//vertical movement
			//ensure no pieces are in the way
			int end = Math.max(new_row, curr_row), start = Math.min(new_row, curr_row);
			for (int i = start+1; i < end; i++) {
				if (cb.getPiece(i, curr_col) != null) return false;
			}
			firstMove = true;
			return true;	
		}
		//horizontal movement
		if (curr_row == new_row) {
			//ensure no pieces are in the way
			int end = Math.max(new_col, curr_col), start = Math.min(new_col, curr_col);
			for (int i = start+1; i < end; i++) {
				if (cb.getPiece(curr_row, i) != null) return false;
			}
			firstMove = true;
			return true;	
		}	
		return false;
	}
	

}
