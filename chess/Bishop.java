package chess;

import java.lang.Math;
/**
 * @author Yunhao Xu
 * @author Minhesota Geusic
 * @version %I% %G%
 * @since 1.2
 */
public class Bishop extends Piece {
	/**
	 * constructor
	 *
	 * @param player the player who holds this piece
	 */
	public Bishop (String player) {
		super(player);
		this.name = "B"; 
	}
	
	/**
	 * determines if this Bishop's movement is legal, including all kinds of diagonal movements
	 *
	 * @param curr_row current row that the Bishop is in
	 * @param curr_col current column that the Bishop is in
	 * @param new_row row the Bishop moves to
	 * @param new_col column the Bishop moves to
	 * @param cb chessboard
	 * @return true if the movement is legal, otherwise false
	 */	
	@Override
	public boolean isMoveLegal(int curr_row, int curr_col, int new_row, int new_col, ChessBoard cb) {
		// Bishop can only move diagonally unless there are pieces in its way 
		if (new_col-curr_col == new_row-curr_row) {    // first diagonal/right diagonal 
			// determining the start and end index
			int start_col = Math.min(new_col, curr_col)+1, end_col = Math.max(new_col, curr_col);
			int start_row = Math.min(new_row, curr_row)+1, end_row = Math.max(new_row, curr_row);
			while (start_row < end_row && start_col < end_col) {
				if (cb.getPiece(start_row, start_col) != null) return false;
				start_row++;
				start_col++;
			}
			return true;
		}
		if (new_col-curr_col+new_row-curr_row == 0) {  // second diagonal/left diagonal
			// determining the start and end index
			int start_col = Math.max(new_col, curr_col)-1, end_col = Math.min(new_col, curr_col);
			int start_row = Math.min(new_row, curr_row)+1, end_row = Math.max(new_row, curr_row);
			while (start_row > end_row && start_col < end_col) {
				if (cb.getPiece(start_row, start_col) != null) return false;
				start_row--;
				start_col++;
			}
			return true;
		}
		return false;
	}
}