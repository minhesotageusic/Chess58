package chess;

import java.lang.Math;
/**
 * @author Yunhao Xu
 * @author Minhesota Geusic
 * @version %I% %G%
 * @since 1.2
 */
public class Queen extends Piece {
	/**
	 * constructor
	 *
	 * @param player the player who holds this Queen
	 */
	public Queen (String player) {
		super(player);
		this.name = "Q";
	}	

	/**
	 * determines if this Queen's movement is legal, including all kinds of special movements, such as vertically, horizontally,
	 * and diagonally
	 *
	 * @param curr_row current row that the Queen is in
	 * @param curr_col current column that the Queen is in
	 * @param new_row row the Queen moves to
	 * @param new_col column the Queen moves to
	 * @param cb chessboard
	 * @return true if the movement is legal, otherwise false
	 */
	@Override
	public boolean isMoveLegal(int curr_row, int curr_col, int new_row, int new_col, ChessBoard cb) {
		// Queen movement
		if (new_col-curr_col == new_row-curr_row) {    // first diagonal/right diagonal
			// determining the start and end index
			int start_row = Math.min(new_row, curr_row)+1, end_row = Math.max(new_row, curr_row);
			int start_col = Math.min(new_col, curr_col)+1, end_col = Math.max(new_col, curr_col);
			while (start_row < end_row && start_col < end_col) {
				if (cb.getPiece(start_row, start_col) != null) {
					return false;
				}
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
		if (curr_col == new_col) {	//vertical movement
			int end = Math.max(new_row, curr_row), start = Math.min(new_row, curr_row);
			for (int i = start+1; i < end; i++) {
				if (cb.getPiece(i, curr_col) != null) return false;
			}
			return true;	
		}
		if (curr_row == new_row) {	//horizontal movement
			int end = Math.max(new_col, curr_col), start = Math.min(new_col, curr_col);
			for (int i = start+1; i < end; i++) {
				if (cb.getPiece(curr_row, i) != null) return false;
			}
			return true;	
		}
		return false;
	}

}