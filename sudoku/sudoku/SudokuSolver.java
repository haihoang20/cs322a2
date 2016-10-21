package sudoku;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

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
		HashMap<Integer, HashSet<Integer>> domainOfAllCells = this.createDomainForEntireBoard(newBoard, domainOfSingleCell);


		// this will be changed to while(!this.isSolved(board)
		// we may want to limit the number of times this loops to prevent an infinite loop in case the solution is too hard to find
		// for example, we could have:
		// int limit = 10000;
		// while (limit > 0 || !this.isSolved)
		// this.applyArcConsistency
		// this.applyDomainSplitting
		// limit--;
		
		//System.out.println("domain" + domainOfAllCells.size());

		int limit = 10;
		while (!this.isSolved(newBoard) && limit != 0) {
			domainOfAllCells = this.applyArcConsistency(newBoard, domainOfAllCells);
			limit--;
			
			for (int i = 0; i < domainOfAllCells.size(); ++i)
			{
				HashSet<Integer> currentCell = domainOfAllCells.get(i);
				if (currentCell.size() == 1)
				{
					for (Integer value : currentCell)
					{
						int modulus = i % 9;
						int column = modulus;
						int row = Math.floorDiv(i, 9);
						newBoard[row][column] = value;
					}
				}
			}
			// if (check if domain is > 1)
			// this.applyDomainSplitting

			// we can give the newBoard to applyDomainSplitting so that it can create the newBoard with the new values
			// or we can make a helper function with that
			// we'll need to check that the domain for each cell is exactly 1 value before doing this
			System.out.println("This board is not solved");
		}
		
		System.out.println("Solved or Timeout");
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
	private HashMap<Integer, HashSet<Integer>> createDomainForEntireBoard(int[][] board, HashSet<Integer> domainOfSingleCell) {
		HashMap<Integer, HashSet<Integer>> domainOfAllCells = new HashMap<>();
		for(int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (board[i][j] == 0) {
					domainOfAllCells.put((i*9) + j,domainOfSingleCell);
				} else {
					HashSet<Integer> singleValueDomain = new HashSet<>();
					singleValueDomain.add(board[i][j]);
					domainOfAllCells.put((i*9) + j, singleValueDomain);
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
		for (int j = 0; j < board[row].length; ++j) {
			cellToCompare = board[row][j];
			// iterate through the row's column and see if value is already present in the row
			if (j != column && cellToCompare == value ){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * goes through a specific column to check if column is consistent for a specific value of a cell
	 * @param board
	 * @return boolean
	 */
	private boolean checkSpecificColumn(int row, int column, int value, int[][] board) {
		int cellToCompare;
		for (int i = 0; i < board.length; ++i) {
			cellToCompare = board[i][column];
			// iterate through the row's column and see if value is already present in the row
			if (i != row && cellToCompare == value){
				return false;
			}
		}
		return true;
	}

	/**
	 * goes through a specific block to check if block is consistent for a specific value of a cell
	 * @param board
	 * @return boolean
	 */
	private boolean checkSpecificBlock(int row, int column, int value, int[][] board) {
		int startRow = row - (row % 3);
		int startColumn = column - (column %3);
		
		for (int i = startRow; i < startRow + 3; ++i) {
			for (int j = startColumn; j < startColumn + 3; ++j){				
				if (board[i][j] == value){
					return false;
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

	// TODO: need to check if this is correct
	private HashMap<Integer, HashSet<Integer>> applyArcConsistency(int[][] board, HashMap<Integer, HashSet<Integer>> domainOfAllCells) {
		boolean recheck = true;
		int[][] newBoard = board;
		while (recheck){
			recheck = false;
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					HashSet<Integer> domainOfSingleCell = new HashSet<Integer>();
					domainOfSingleCell = domainOfAllCells.get((i*9) + j);
					ArrayList<Integer> toRemove = new ArrayList<Integer>();
					if (domainOfSingleCell.size() > 1)
					{
						for (Integer dom : domainOfSingleCell){	
							
							if (!this.checkSpecificColumn(i, j, dom, newBoard)){
								//domain for that variable is not consistent
								toRemove.add(dom);
								recheck = true;
							}
							else if (!this.checkSpecificBlock(i,j, dom, newBoard)){
								toRemove.add(dom);
								recheck = true;
							}
							else if (!this.checkSpecificRow(i,j, dom, newBoard)){
								// TODO: not sure why this is false
								//toRemove.add(dom);
								recheck = true;
							}	
						}
					}
					if (toRemove.size() > 0)
					{
						for (int k = 0; k < toRemove.size(); ++k)
						{
							domainOfSingleCell.remove(toRemove.get(k));
						}
					}

					if (recheck){
						domainOfAllCells.put((i*9) + j, domainOfSingleCell);
						
						if (domainOfSingleCell.size() == 1)
						{
							for (Integer value : domainOfSingleCell)
							{
								newBoard[i][j] = value;
							}
						}
					}
				}
			}
		}	
		
		/*
		int[] key = new int[2];
		HashSet<Integer> domainOfSingleCell;
		// goes through rows
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				valueInCellToCompare = board[i][j];
				for (int k = 0; k < board.length; k++) {
					key[0] = i;
					key[1] = k;
					domainOfSingleCell = domainOfAllCells.get((i*9) + k);
					domainOfSingleCell.remove(valueInCellToCompare);
					domainOfAllCells.put((i*9) + k, domainOfSingleCell);
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
					domainOfSingleCell = domainOfAllCells.get((p*9) + l);
					domainOfSingleCell.remove(valueInCellToCompare);
					domainOfAllCells.put((p*9) + l, domainOfSingleCell);
				}
			}
		}
		*/
		// TODO: do we also need one for squares
		
		return domainOfAllCells;
	}

	// TODO
	private int[][] applyDomainSplitting(int[][] board) {
		return board;
	}
}
