package sudoku;

import java.util.HashMap;
import java.util.HashSet;

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
		// we make a new board so that we can make changes to it
		int[][] newBoard = board;

		HashSet<Integer> domainOfSingleCell = this.createDomainForSingleCell();
		HashMap<int[], HashSet<Integer>> domainOfAllCells = this.createDomainForEntireBoard(newBoard, domainOfSingleCell);


		// this will be changed to while(!this.isSolved(board)
		// we may want to limit the number of times this loops to prevent an infinite loop in case the solution is too hard to find
		// for example, we could have:
		// int limit = 10000;
		// while (limit > 0 || !this.isSolved)
		// this.applyArcConsistency
		// this.applyDomainSplitting
		// limit--;
		if (!this.isSolved(newBoard)) {
			this.applyArcConsistency(newBoard, domainOfAllCells);
			// if (check if domain is > 1)
			// this.applyDomainSplitting
			System.out.println("This board is not solved");
		} else {
			System.out.println("Solved!!");
		}
		return newBoard;
	}

	/**
	 * creates a set containing the full domain of each cell on the board
	 * i.e. [1, 2, 3, 4, 5, 6, 7, 8, 9]
	 * @return HashSet
	 */
	private HashSet<Integer> createDomainForSingleCell() {
		HashSet<Integer> domainOfSingleCell = new HashSet<>();
		for (int i = 0; i < 9; i++ ){
			domainOfSingleCell.add(i+1);
		}
		return domainOfSingleCell;
	}

	/**
	 * creates a map of all the cells on the board as keys and maps them to their individual domains
	 * i.e. cell(3,5) has the domain [1,2,3,4,5,6,7,8,9]
	 * @param board
	 * @param domainOfSingleCell
	 * @return HashMap
	 */
	private HashMap<int[], HashSet<Integer>> createDomainForEntireBoard(int[][] board, HashSet<Integer> domainOfSingleCell) {
		HashMap<int[], HashSet<Integer>> domainOfAllCells = new HashMap<>();
		// cell is int[2] to represent the row,column
		int[] cell = new int[2];
		for(int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				cell[0] = i;
				cell[1] = j;
				domainOfAllCells.put(cell,domainOfSingleCell);
			}
		}
		return domainOfAllCells;
	}

	/**
	 * checks to see if the current board is the solution
	 * @param board
	 * @return boolean
	 */
	private boolean isSolved(int[][] board) {
		return this.checkRows(board) && this.checkColumns(board) && this.checkSquares(board);
	}

	/**
	 * goes through the entire board row by row to check for 0's and duplicates
	 * @param board
	 * @return boolean
	 */
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

	/**
	 * goes through the entire board column by column to check for 0's and duplicates
	 * @param board
	 * @return boolean
	 */
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

	/**
	 * goes through the entire board and inspects individual squares at a time for 0's and duplicates
	 * @param board
	 * @return boolean
	 */
	private boolean checkSquares(int[][] board) {
		// HashSets don't accept duplicates
		HashSet<Integer> squareValues = new HashSet<>();
		for (int i = 0; i < board.length; i=i+3) {
			for (int j = 0; j < board.length; j=j+3) {
				for (int k = i; k < i+3; k++) {
					for (int l = j; l < j+3; l++) {
						if (board[k][l] == 0) {
							return false;
						}
						squareValues.add(board[k][l]);
					}
				}
				// this means that there was at least one duplicate
				if (squareValues.size() < 9) {
					return false;
				}
				// reset to check a different square
				squareValues.clear();
			}
		}
		return true;
	}

	// TODO
	private int[][] applyArcConsistency(int[][] board, HashMap<int[], HashSet<Integer>> domainOfAllCells) {
		// code in here is not correct but I left it so you can get the general idea of what I'm doing
		// Need to pass in all the domains and slowly pick it apart
		HashSet<Integer> domain = new HashSet<>();
		// initialize domain of each cell
		for (int i = 0; i < 9; i++ ){
			domain.add(i+1);
		}
		// go through row to remove values from domain
		for (int j = 0; j < board.length; j++) {
			domain.remove(board[0][j]);
		}
		return board;
	}

	// TODO
	private int[][] applyDomainSplitting(int[][] board) {
		return board;
	}
}
