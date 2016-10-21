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
			//domainOfAllCells = this.applyArcConsistency(newBoard, domainOfAllCells);
			// if (check if domain is > 1)
			// this.applyDomainSplitting

			// we can give the newBoard to applyDomainSplitting so that it can create the newBoard with the new values
			// or we can make a helper function with that
			// we'll need to check that the domain for each cell is exactly 1 value before doing this
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
		for (int i = 1; i < 10; i++){
			domainOfSingleCell.add(i);
		}
		return domainOfSingleCell;
	}

	/**
	 * creates a map of all the cells on the board as keys and maps them to their individual domains
	 * i.e. cell(3,5) has the domain [1,2,3,4,5,6,7,8,9]
	 * or cell(5,5) has the domain [3]
	 * @param board
	 * @param domainOfSingleCell
	 * @return HashMap
	 */
	private HashMap<int[], HashSet<Integer>> createDomainForEntireBoard(int[][] board, HashSet<Integer> domainOfSingleCell) {
		HashMap<int[], HashSet<Integer>> domainOfAllCells = new HashMap<>();
		// cell is int[2] to represent the row,column
		int[] cell = new int[2];
		HashSet<Integer> singleValueDomain = new HashSet<>();
		
		for(int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				cell[0] = i;
				cell[1] = j;
				
				if (board[i][j] == 0) {
					domainOfAllCells.put(cell,domainOfSingleCell);
				} else {
					singleValueDomain.add(board[i][j]);
					domainOfAllCells.put(cell, singleValueDomain);
					singleValueDomain.clear();
				}
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
	 * goes through a specific row to check if row is consistent for a specific value of a cell
	 * @param board
	 * @return boolean
	 */
	private boolean checkSpecificRow(int row, int column, int value, int[][] board) {
		int cellToCompare;
		for (int i = 0; i < board.length; i++) {
			cellToCompare = board[row][i];
			// iterate through the row's column and see if value is already present in the row
			if (column != i && cellToCompare == value ){
				return false;
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

	// TODO: need to check if this is correct
	private HashMap<int[], HashSet<Integer>> applyArcConsistency(int[][] board, HashMap<int[], HashSet<Integer>> domainOfAllCells) {
		int valueInCellToCompare;
		int[] key = new int[2];
		HashSet<Integer> domainOfSingleCell;
		// goes through rows
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				valueInCellToCompare = board[i][j];
				for (int k = 0; k < board.length; k++) {
					key[0] = i;
					key[1] = k;
					domainOfSingleCell = domainOfAllCells.get(key);
					domainOfSingleCell.remove(valueInCellToCompare);
					domainOfAllCells.put(key, domainOfSingleCell);
				}
			}
		}
		// goes through columns
		for (int l = 0; l < board.length; l++) {
			for (int m = 0; m < board.length; m++) {
				valueInCellToCompare = board[m][l];
				for (int p = 0; p < board.length; p++) {
					key[0] = p;
					key[1] = l;
					domainOfSingleCell = domainOfAllCells.get(key);
					domainOfSingleCell.remove(valueInCellToCompare);
					domainOfAllCells.put(key, domainOfSingleCell);
				}
			}
		}
		// TODO: do we also need one for squares?
		return domainOfAllCells;
	}

	// TODO
	private int[][] applyDomainSplitting(int[][] board) {
		return board;
	}
}
