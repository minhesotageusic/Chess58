package chess;

/**
 * @author Yunhao Xu
 * @author Minhesota Geusic
 * @version %I% %G%
 * @since 1.2
 */
public abstract class Piece {
	/**
	 * player name that own this piece
	 */
	protected String player;
	/**
	 * name of this piece
	 */
	protected String name;
	
	/**
	 * Constructor for Piece
	 * 
	 * @param player name of player
	 */
	public Piece (String player) {
		this.player = player;
	}
	
	/**
	 *
	 * Determine if this piece can move from the given coordinate
	 * to another given coordinate. It is assumed that the two 
	 * coordinates are valid.
	 *
	 * 
	 * @param curr_col current column
	 * @param curr_row current row
	 * @param new_col new column
	 * @param new_row new row
	 * @param cb chessboard 
	 * 
	 * @return true if the move is legal, false otherwise
	 * 
	 */
	public abstract boolean isMoveLegal(int curr_row, int curr_col, int new_row, int new_col, ChessBoard cb);
	
	/**
	 * get the player name that owns this piece
	 * 
	 * @return name of the player that own this piece
	 */
	public String getPlayer() {
		return player;
	}
	
	/**
	 * get the name of this piece
	 * 
	 * @return name of this piece
	 */
	public String toString() {
		return name;
	}
}
