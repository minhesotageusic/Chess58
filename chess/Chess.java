package chess;


import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * @author Yunhao Xu
 * @author Minhesota Geusic
 * @version %I% %G%
 * @since 1.2
 */
//Main game initializer and manager
public class Chess {
	/**
	 * the error message for illegal move
	 */
	public static final String ILLEGALMOVEERROR = "Illegal move, try again";

	/**
	 * maximum of number of players
	 */
	public static final int MAX_NUMBER_PLAYERS = 2;

	/**
	 * an array holds the kings from both sides of players
	 */
	private King[] kings;

	/**
	 * String array stores the references to the name of the two players
	 */
	private String[] players;

	/**
	 * a boolean gameover indicates if game is over
	 */
	private boolean gameOver = false;

	/**
	 * an integer indicates who won the game
	 */
	private int whoWon = 0;

	/**
	 * an integer indicates which player is under check
	 */
	private int whoInCheck = 0;

	/**
	 * an integer shows whose turn is currently
	 */
	private int playerTurn = 0;

	/**
	 * references the chessboard
	 */
	private ChessBoard cb;

	/**
	 * check thoroughly if the inputs provided by the users are valid inputs, by
	 * running through numbers of check methods
	 * 
	 * @param oldRow  the current row of the piece that is about to be moved
	 * @param oldCol  the current column of the piece that is about to be moved
	 * @param newRow  the row, to which the piece will move
	 * @param newCol  the column, to which the piece will move
	 * @param upgrade the piece that pawn would get promoted to if allowed
	 *
	 * @return true if input is valid, otherwise return false
	 */
	public boolean handleInput(int oldRow, int oldCol, int newRow, int newCol, char upgrade) {
		Piece currPiece = null;
		Piece nextPiece = null;
		int currPiece_Player = 0;
		int otherPlayerIndex = 1;
		// do a preliminary check to ensure coordinates are
		// 1. valid
		// 2. player are using their own pieces
		// 3. player cannot cannibalizes their own pieces
		if (!preliminaryCheck(oldRow, oldCol, newRow, newCol, playerTurn)) {
			System.out.println(ILLEGALMOVEERROR);
			return false;
		}
		currPiece = cb.getPiece(oldRow, oldCol);
		// test if move is legal
		if (!currPiece.isMoveLegal(oldRow, oldCol, newRow, newCol, cb)) {
			System.out.println(ILLEGALMOVEERROR);
			return false;
		}
		currPiece_Player = findPlayersIndex(currPiece.getPlayer());

		if (currPiece_Player == 1)
			otherPlayerIndex = 0;

		nextPiece = cb.getPiece(newRow, newCol);
		// move currPiece to new location
		if (nextPiece != null)
			cb.removePiece(newRow, newCol);
		cb.removePiece(oldRow, oldCol);
		cb.addPiece(newRow, newCol, currPiece);
		// update the newly moved piece's coordinate
		cb.updateLastMovedPiece(newRow, newCol);

		// determine if the given move forced us in check
		if (targetPlayerAchievedCheck(otherPlayerIndex)) {
			// if this move forced us in check
			// we need to reset the pieces
			System.out.println(ILLEGALMOVEERROR);
			cb.removePiece(newRow, newCol);
			cb.addPiece(oldRow, oldCol, currPiece);
			if (nextPiece != null)
				cb.addPiece(newRow, newCol, nextPiece);
			return false;
		}

		// if we are freed from check, then determine if we put other
		// player in checkmate

		// determine if given move put other player in check
		if (targetPlayerAchievedCheck(playerTurn)) {

			// test for checkmate
			if (targetPlayerAcheivedCheckmate(playerTurn)) {
				// if checkmate occurs then game over
				System.out.println();
				System.out.println("Checkmate");
				gameOver = true;
				whoWon = playerTurn;
				return true;
			}
			System.out.println("Check");
			return true;
		}

		// perform pawn upgrade if possible
		if (currPiece instanceof Pawn) {
			// we need to check if currPiece is allow to make upgrade
			// black side
			if (currPiece_Player == 1) {
				// pawn has reach the opposite side
				// row == 0
				if (newRow == 0) {
					// we can upgrade
					UpgradeTo(newRow, newCol, upgrade, currPiece.getPlayer());
				}
			}
			// white side
			else {
				// pawn has reach the opposite side
				if (newRow == 7) {
					UpgradeTo(newRow, newCol, upgrade, currPiece.getPlayer());
				}
			}
		}

		// recheck if pawn upgrading achieved check
		if (targetPlayerAchievedCheck(playerTurn)) {

			// test for checkmate
			if (targetPlayerAcheivedCheckmate(playerTurn)) {
				// if checkmate occurs then game over
				System.out.println();
				System.out.println("Checkmate");
				gameOver = true;
				whoWon = playerTurn;
				return true;
			}
			System.out.println("Check");
			return true;
		}

		return true;
	}

	/**
	 * Determine if the given target player has achieved checkmate
	 *
	 * @param targetPlayer the player who acheives the checkmate
	 * @return true if this player has achieved checkmate, otherwise false
	 */
	private boolean targetPlayerAcheivedCheckmate(int targetPlayer) {
		if (gameOver == true)
			return true;
		String otherPlayer = players[0];
		int otherPlayerIndex = 0;
		if (targetPlayer == 0) {
			otherPlayerIndex = 1;
			otherPlayer = players[1];
		}
		// we will brute force to check the opposite player's
		// pieces if they can stop the checkmate
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				Piece p = cb.getPiece(row, col);
				if (p == null)
					continue;
				if (!p.getPlayer().equals(otherPlayer))
					continue;
				// determine if this piece can cancel the checkmate
				for (int prow = 0; prow < 8; prow++) {
					for (int pcol = 0; pcol < 8; pcol++) {
						if (!preliminaryCheck(row, col, prow, pcol, otherPlayerIndex))
							continue;
						// if this piece can make a move, determine if that move
						// can block the check
						if (p.isMoveLegal(row, col, prow, pcol, cb)) {
							Piece nextPiece = cb.getPiece(prow, pcol);
							// put this piece at the prow, pcol and
							// test if check still occur
							cb.removePiece(nextPiece);
							cb.removePiece(row, col);
							cb.addPiece(prow, pcol, p);
							// test if the target player can still put
							// the opposite player in check
							if (!targetPlayerAchievedCheck(targetPlayer)) {
								// add the pieces back
								cb.removePiece(prow, pcol);
								cb.addPiece(row, col, p);
								cb.addPiece(prow, pcol, nextPiece);
								return false;
							}
							cb.removePiece(prow, pcol);
							cb.addPiece(row, col, p);
							cb.addPiece(prow, pcol, nextPiece);
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * Determine if the target player can put the other player in check
	 *
	 * @param targetPlayer the player who puts the opponent under check
	 * @return true if the player successfully puts the other player under check,
	 *         false otherwise
	 */
	private boolean targetPlayerAchievedCheck(int targetPlayer) {
		if (gameOver == true)
			return true;
		Pair otherPlayerKingPosition = null;
		if (targetPlayer == 0)
			otherPlayerKingPosition = cb.getPieceCoordinate(kings[1]);
		else
			otherPlayerKingPosition = cb.getPieceCoordinate(kings[0]);

		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				Piece p = cb.getPiece(row, col);
				int playerIndex = 0;
				if (p == null)
					continue;
				playerIndex = findPlayersIndex(p.getPlayer());
				if (playerIndex != targetPlayer)
					continue;
				if (!preliminaryCheck(row, col, otherPlayerKingPosition.x, otherPlayerKingPosition.y, targetPlayer))
					continue;
				// if this piece can put other player's
				// king in check then return true
				if (p.isMoveLegal(row, col, otherPlayerKingPosition.x, otherPlayerKingPosition.y, cb)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Determine if the given coordinates are valid, using only their pawn, and
	 * cannot move on an ally tile
	 * 
	 * @param curr_row the current row of this piece
	 * @param curr_col the current column of this piece
	 * @param new_row  the new row that this piece moves to
	 * @param new_col  the new column that this piece moves to
	 * @param player   the player that makes the move
	 * @return true if this movement has passed the preliminary check, false
	 *         otherwise
	 */
	private boolean preliminaryCheck(int curr_row, int curr_col, int new_row, int new_col, int player) {

		Piece currPiece = null;
		Piece nextPiece = null;
		int currPiece_Player = 0;
		// check if coordinates are valid and are not the the same coordinate
		if (!cb.isCoordinateValid(curr_row, curr_col) || !cb.isCoordinateValid(new_row, new_col)
				|| (curr_row == new_row && curr_col == new_col))
			return false;

		currPiece = cb.getPiece(curr_row, curr_col);
		// test if move is legal
		if (currPiece == null)
			return false;

		currPiece_Player = findPlayersIndex(currPiece.getPlayer());
		// ensure player only use their pawn
		if (currPiece_Player != player)
			return false;

		nextPiece = cb.getPiece(new_row, new_col);
		// check if the new location has been occupied by ally
		if (nextPiece != null && nextPiece.getPlayer().equals(currPiece.getPlayer()))
			return false;

		return true;
	}

	/**
	 * Advance to next player's turn
	 */
	public void nextPlayerTurn() {
		if (gameOver == true)
			return;
		playerTurn++;
		if (playerTurn >= MAX_NUMBER_PLAYERS)
			playerTurn = 0;
	}

	/**
	 * Find the given player's name index
	 * 
	 * @param player name of player
	 * @return return the index of the given player
	 */
	public int findPlayersIndex(String player) {
		if (player == null)
			return 0;
		if (players[0].equals(player))
			return 0;
		else
			return 1;
	}

	/**
	 * print the winner, print nothing if game is not over
	 */
	public void printWinner() {
		if (gameOver == false)
			return;
		if (whoWon == 0) {
			System.out.println("White wins");
		} else {
			System.out.println("Black wins");
		}
	}

	/**
	 * Print the game board
	 */
	public void printBoard() {
		cb.printBoard();
	}

	/**
	 * print the current player's turn; Print nothing if game is over
	 */
	public void printPlayerTurn() {
		if (gameOver == true)
			return;
		if (playerTurn == 0) {
			System.out.print("White's move: ");
		} else {
			System.out.print("Black's move: ");
		}
	}

	/**
	 * Get the index of the given file s
	 * 
	 * @param s file
	 * @return an integer representing the given file s. Return -1 s is an incorrect
	 *         file.
	 */
	public int getFileIndex(char s) {
		if (s == 'a' || s == 'A')
			return 0;
		if (s == 'b' || s == 'B')
			return 1;
		if (s == 'c' || s == 'C')
			return 2;
		if (s == 'd' || s == 'D')
			return 3;
		if (s == 'e' || s == 'E')
			return 4;
		if (s == 'f' || s == 'F')
			return 5;
		if (s == 'g' || s == 'G')
			return 6;
		if (s == 'h' || s == 'H')
			return 7;
		return -1;
	}

	/**
	 * Add the upgrade version given by p to the new location <newCol,newRow> added
	 * to the player
	 *
	 * @param newRow the row of current piece for upgrade
	 * @param newCol the column of current piece for upgrade
	 * @param p      the character that represents the piece the pawn gets promoted
	 *               to
	 * @param player the player whose pawn gets promoted
	 */
	private void UpgradeTo(int newRow, int newCol, char p, String player) {
		if (!cb.isCoordinateValid(newRow, newCol))
			return;
		// remove the current piece
		cb.removePiece(newRow, newCol);
		// replace with new piece
		switch (p) {
		case 'R':
			cb.addPiece(newRow, newCol, new Rook(player));
			break;
		case 'K':
			cb.addPiece(newRow, newCol, new Knight(player));
			break;
		case 'B':
			cb.addPiece(newRow, newCol, new Bishop(player));
			break;
		case 'Q':
			cb.addPiece(newRow, newCol, new Queen(player));
			break;
		default:
			break;
		}
	}

	/**
	 * constructor
	 */
	public Chess() {
		//initalize
		players = new String[MAX_NUMBER_PLAYERS];
		players[0] = "w";
		players[1] = "b";
		kings = new King[MAX_NUMBER_PLAYERS];
		cb = new ChessBoard();
		loadWhitePlayerPieces();
		loadBlackPlayerPieces();
		kings[0] = (King) cb.getPiece(0, getFileIndex('e'));
		kings[1] = (King) cb.getPiece(7, getFileIndex('e'));
		//move code to handle input?
	}

	/**
	 * load the initial pieces of player white on the chessboard
	 */
	private void loadWhitePlayerPieces() {

		cb.addPiece(0, getFileIndex('e'), new King(players[0]));
		cb.addPiece(0, getFileIndex('d'), new Queen(players[0]));
		cb.addPiece(0, getFileIndex('f'), new Bishop(players[0]));
		cb.addPiece(0, getFileIndex('c'), new Bishop(players[0]));
		cb.addPiece(0, getFileIndex('f'), new Bishop(players[0]));
		cb.addPiece(0, getFileIndex('b'), new Knight(players[0]));
		cb.addPiece(0, getFileIndex('g'), new Knight(players[0]));
		cb.addPiece(0, getFileIndex('h'), new Rook(players[0]));
		cb.addPiece(0, getFileIndex('a'), new Rook(players[0]));
		for (int col = 0; col < 8; col++) {
			cb.addPiece(1, col, new Pawn(players[0], true));
		}

	}

	/**
	 * load the initial pieces of player black on the chessboard
	 */
	private void loadBlackPlayerPieces() {

		cb.addPiece(7, getFileIndex('e'), new King(players[1]));
		cb.addPiece(7, getFileIndex('d'), new Queen(players[1]));
		cb.addPiece(7, getFileIndex('f'), new Bishop(players[1]));
		cb.addPiece(7, getFileIndex('c'), new Bishop(players[1]));
		cb.addPiece(7, getFileIndex('f'), new Bishop(players[1]));
		cb.addPiece(7, getFileIndex('b'), new Knight(players[1]));
		cb.addPiece(7, getFileIndex('g'), new Knight(players[1]));
		cb.addPiece(7, getFileIndex('h'), new Rook(players[1]));
		cb.addPiece(7, getFileIndex('a'), new Rook(players[1]));
		for (int col = 0; col < 8; col++) {
			cb.addPiece(6, col, new Pawn(players[1], false));
		}

	}

	/**
	 * creates and runs the whole game
	 *
	 * @param args arguments array
	 */
	public static void main(String[] args) {
		Chess gm = new Chess();
		Scanner scan = new Scanner(System.in);
		// matches anything in the form of <a-h><1-8> <a-h><1-8>
		Pattern p1 = Pattern.compile("(?:[[A-h][a-h]])(?:[1-8]) (?:[[A-h][a-h]])(?:[1-8])");
		// matches anything in the form of <a-h><1-8> <a-h><1-8> <N||R||B||Q>
		Pattern p2 = Pattern.compile("(?:[[A-h][a-h]])(?:[1-8]) (?:[[A-h][a-h]])(?:[1-8]) (?:[R,N,B,Q])");
		Pattern p3 = Pattern.compile("(?:[[A-h][a-h]])(?:[1-8]) (?:[[A-h][a-h]])(?:[1-8]) (?<name>draw[?])");
		Pattern p4 = Pattern.compile("(?<name>draw)");
		Pattern p5 = Pattern.compile("(?<name>!help)");
		String input = null;
		String[] arr = null;
		int currCol, currRow;
		int newCol, newRow;
		boolean drawOffer = false;
		boolean endedByDraw = false;
		boolean inValidMove = false;

		while (!gm.gameOver) {
			if (!inValidMove) {
				System.out.println();
				gm.printBoard();
				System.out.println();
			}
			gm.printPlayerTurn();
			input = scan.nextLine();
			// parse out input
			input = input.trim();
			if (input.equals("resign")) {
				// current player resign
				// opposite player won
				gm.gameOver = true;
				if (gm.playerTurn == 0)
					gm.whoWon = 1;
				else
					gm.whoWon = 0;
				break;
			}
			// input is in the form of <a-h><1-8> <a-h><1-8>
			if (p1.matcher(input).matches() && !drawOffer) {
				// might or might not require pawn upgrade
				arr = input.split(" ");
				// parse the first coordinate
				currCol = gm.getFileIndex(arr[0].charAt(0));
				currRow = Character.getNumericValue(arr[0].charAt(1)) - 1;
				// parge the second coordiante
				newCol = gm.getFileIndex(arr[1].charAt(0));
				newRow = Character.getNumericValue(arr[1].charAt(1)) - 1;
				// if the input was correct goto next player's turn
				if (gm.handleInput(currRow, currCol, newRow, newCol, 'Q')) {
					gm.nextPlayerTurn();
					inValidMove = false;
				} else {
					inValidMove = true;
				}
			}
			// input is in the form of <a-h><1-8> <a-h><1-8> <N||R||B||Q>
			else if (p2.matcher(input).matches() && !drawOffer) {
				char upgrade = 0;
				// require pawn upgrade
				// however, we dont know if the seleced piece is a pawn
				arr = input.split(" ");
				// parse the first coordinate
				currCol = gm.getFileIndex(arr[0].charAt(0));
				currRow = Character.getNumericValue(arr[0].charAt(1)) - 1;
				// parse the second coordinate
				newCol = gm.getFileIndex(arr[1].charAt(0));
				newRow = Character.getNumericValue(arr[1].charAt(1)) - 1;
				// parse the upgrade
				upgrade = arr[2].charAt(0);
				// if the input was correct goto next player's turn
				if (gm.handleInput(currRow, currCol, newRow, newCol, upgrade)) {
					gm.nextPlayerTurn();
					inValidMove = false;
				} else {
					inValidMove = true;
				}
			}
			// input is in the form of <a-h><1-8> <a-h><1-8> <draw?>
			else if (p3.matcher(input).matches() && !drawOffer) {
				// might or might not require pawn upgrade
				arr = input.split(" ");
				// parse the first coordinate
				currCol = gm.getFileIndex(arr[0].charAt(0));
				currRow = Character.getNumericValue(arr[0].charAt(1)) - 1;
				// parge the second coordiante
				newCol = gm.getFileIndex(arr[1].charAt(0));
				newRow = Character.getNumericValue(arr[1].charAt(1)) - 1;
				// if the input was correct goto next player's turn
				if (gm.handleInput(currRow, currCol, newRow, newCol, 'Q')) {
					drawOffer = true;
					gm.nextPlayerTurn();
					inValidMove = false;
				} else {
					inValidMove = true;
				}
			}
			// input is in the form of <draw>
			else if (p4.matcher(input).matches() && drawOffer) {
				gm.gameOver = true;
				endedByDraw = true;
			}
			else if (p5.matcher(input).matches() && !drawOffer) {
				System.out.print("List of commands:\n"
						+"\tMove Pieces: row1column1 row2column2\n"
						+"\tMove Pawn Pieces to Promote: row1column1 row2column2 N, Q, B, or R\n"
						+"\tResign: resign\n"
						+"\tAccept Draw: draw\n"
						+"\tMake Move Then Offer Draw: row1column1 row2column2 draw?\n");
			}
			// unknown command
			else {
				inValidMove = true;
				System.out.println(ILLEGALMOVEERROR);
				System.out.println("Type !help to see all possible commands");
			}
		}
		if (!endedByDraw)
			gm.printWinner();
	}

}

