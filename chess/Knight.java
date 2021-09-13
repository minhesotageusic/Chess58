package chess;

import static java.lang.Math.abs;
/**
 * @author Yunhao Xu
 * @author Minhesota Geusic
 * @version %I% %G%
 * @since 1.2
 */
public class Knight extends Piece {
	/**
	 * constructor
	 *
	 * @param player the player who holds the piece
	 */	
	public Knight (String player) {
		super(player);
		this.name = "N"; 
	}	

	/**
	 * determines if this Knight's movement is legal, including all kinds of L-shape movements
	 *
	 * @param curr_row current row that the Knight is in
	 * @param curr_col current column that the Knight is in
	 * @param new_row row the Knight moves to
	 * @param new_col column the Knight moves to
	 * @param cb chessboard
	 * @return true if the movement is legal, otherwise false
	 */
	@Override
	public boolean isMoveLegal(int curr_row, int curr_col, int new_row, int new_col, ChessBoard cb) {
		// knight moves two spaces in either of the 4 direction, then one space perpendicular to either direction
		if ((abs(curr_row-new_row) == 2 && abs(curr_col-new_col) == 1) || 
				(abs(curr_row-new_row) == 1 && abs(curr_col-new_col) == 2)) return true;
		return false;
	}

}
