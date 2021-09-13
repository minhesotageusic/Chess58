package chess;


/**
 * @author Yunhao Xu
 * @author Minhesota Geusic
 * @version %I% %G%
 * @since 1.2
 */
public class ChessBoard {
	/**
	 * Data structure for board
	 */
	private Piece[][] board;
	/**
	 * size of the board
	 */
	public final static int COLUMN = 8, ROW = 8;
	/**keep track of last moved piece's coordinates
	 */
	private Pair lastMovedPiece = new Pair(-1,-1); // initializing them to -1 to indicate that nobody has moved yet	
	/**
	 * constructor for chessboard
	 */
	public ChessBoard() {
		board = new Piece[ROW][COLUMN];
	}
	/**
	 * Determine if the given coordinate is valid
	 * 
	 * @param i row
	 * @param j column
	 * 
	 * @return return true if given coordinate is valid, 
	 * false otherwise 
	 */
	public boolean isCoordinateValid(int i, int j) {
		return !(i < 0 || j < 0 || j >= COLUMN || i >= ROW );
	}
	/**
	 * Return game piece located on i,j
	 * 
	 * @param i row
	 * @param j column
	 * 
	 * @return return the game piece on i,j. Null otherwise
	 */
	public Piece getPiece(int i, int j) {
		if(board == null) return null;
		if(!isCoordinateValid(i, j)) return null;
		return board[i][j];
	}
	/**
	 * return the coordinate pair i,j for the given Piece p
	 * 
	 * @param p piece
	 * 
	 * @return pair of coordinate i,j if Piece p exist, false otherwise.
	 */
	public Pair getPieceCoordinate (Piece p){
		if(board == null || p == null) return null;
		int i = 0, j = 0;
		for(i = 0; i < 8; i ++) 
			for(j = 0; j < 8; j++) 
				if(board[i][j] != null && board[i][j] == p) return new Pair(i, j);
		
		return null;
	}
	/**
	 * Add a given Piece p into the coordinate
	 * 
	 * @param i row
	 * @param j column
	 * @param p piece
	 * 
	 * @return true if the given piece p can be added to the
	 * given coordinate, false otherwise
	 */
	public boolean addPiece(int i, int j, Piece p) {
		if(board == null || p == null) return false;
		if(!isCoordinateValid(i,j)) return false;
		if(board[i][j] != null) return false;
		board[i][j] = p;
		return false;
	}
	/**
	 * remove the given Piece p from the board
	 * 
	 * @param p piece
	 * 
	 * @return true if the piece was removed, false otherwise 
	 */
	public boolean removePiece(Piece p) {
		if(board == null) return false;
		Pair pair = getPieceCoordinate(p);
		if(pair == null) return false;
		board[pair.x][pair.y] = null;
		return true;
	}
	/**
	 * remove a piece from the given coordinate
	 * 
	 * @param i row
	 * @param j column
	 * 
	 * @return the piece on the given coordinate, null if 
	 * none exist
	 */
	public Piece removePiece(int i, int j) {
		if(board == null) return null;
		if(!isCoordinateValid(i,j)) return null;
		if(board[i][j] == null) return null;
		Piece retp = board[i][j];
		board[i][j] = null;
		return retp;
	}
	/**
	 * print the board
	 */
	public void printBoard() {
		boolean blackSpot = false;
		for(int i = 7 ; i >= 0 ; i--) {
			for(int j = 0; j < 8; j++) {
				if(board[i][j] == null) {
					if(!blackSpot) {
						System.out.print("   ");
					}else {
						System.out.print("## ");
					}
				}else {
					System.out.print(board[i][j].getPlayer() + board[i][j].toString() + " ");
				}
				blackSpot = !blackSpot;
			}
			blackSpot = !blackSpot;
			System.out.println((i+1));
		}
		System.out.println(" a  b  c  d  e  f  g  h");
	}
	/**
	 * update the coordinate of most recently moved piece
	 *
	 * @param row row
	 * @param col column
	 *
	 **/
	public void updateLastMovedPiece(int row, int col) {
		lastMovedPiece.x = row;
		lastMovedPiece.y = col;
	}
	/**
	 * give the coordinate of last moved piece
	 *
	 * @return the pair row,column as its coordinate. null if no one has moved yet
	 **/	
	public Pair lastPieceMoved () {
		if (lastMovedPiece.x < 0 || lastMovedPiece.y > 7) return null;
		return lastMovedPiece;
	}
	
}
