package sudoku;

/**
 * Place for your code.
 */
public class SudokuSolver {

	/**
	 * @return names of the authors and their student IDs (1 per line).
	 */
	public String authors() {
		return "sup m9";
	}

	/**
	 * Performs constraint satisfaction on the given Sudoku board using Arc Consistency and Domain Splitting.
	 * 
	 * @param board the 2d int array representing the Sudoku board. Zeros indicate unfilled cells.
	 * @return the solved Sudoku board
	 */
	public int[][] solve(int[][] board) {
		// this will be changed to while(!this.isSolved(board)
		if (!this.isSolved(board)) {
			// this will be changed to this.applyArcConsistency
			// if (check if domain is > 1)
			// this.applyDomainSplitting
			System.out.println("This board is not solved");
		} else {
			System.out.println("Solved!!");
		}
		return board;
	}

	private boolean isSolved(int[][] board) {
		return this.checkRows(board) && this.checkColumns(board) && this.checkSquares(board);
	}

	private boolean checkRows(int[][] board) {
		int cellToCompare;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				cellToCompare = board[i][j];
				if (cellToCompare == 0){
					return false;
				}
				for (int k = j; k < board.length-1; k++) {
					if (board[i][k] == 0 || cellToCompare == board[i][k+1]) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private boolean checkColumns(int[][] board) {
		int cellToCompare;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				cellToCompare = board[j][i];
				if (cellToCompare == 0){
					return false;
				}
				for (int k = j; k < board.length-1; k++) {
					if (board[k][i] == 0 || cellToCompare == board[k+1][i]) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private boolean checkSquares(int[][] board) {
		return true;
	}

	private int[][] applyArcConsistency(int[][] board) {
		return board;
	}

	private int[][] applyDomainSplitting(int[][] board) {
		return board;
	}
}
