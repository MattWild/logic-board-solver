package objects;
import java.util.ArrayList;

import exceptions.SolvingException;

/**
 * This class defines and implements a subboard of a larger logic board, 
 * a subboard is defined by two categories and represents the grid of hits/links
 * 'O's and misses 'X's implied by the logic puzzle.
 * 
 * @author matthiaswilder
 *
 */

public class SubBoard {
	
	private Category cat1;
	private Category cat2;
	private int[][] board;
	private ArrayList<Integer[]> hitQueue = new ArrayList<Integer[]>();
	private ArrayList<Integer[]> hitList = new ArrayList<Integer[]>();
	
	/**
	 * 
	 * @param cat1
	 * @param cat2
	 * @param elementNumber
	 */
	
	public SubBoard(Category cat1, Category cat2, int elementNumber) {
		this.cat1 = cat1;
		this.cat2 = cat2;
		this.board = new int[elementNumber][elementNumber];
		for (int i = 0; i < elementNumber; i++) {
			for(int j = 0; j < elementNumber; j++) {
				this.board[i][j] = 0;
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	
	public Category getCategory1() {
		return cat1;
	}
	
	/**
	 * 
	 * @return
	 */
	
	public Category getCategory2() {
		return cat2;
	}
	
	/**
	 * 
	 * @param index1
	 * @param index2
	 * @param flipAxes
	 * @return
	 */
	
	public int getIndex(int index1, int index2, boolean flipAxes) {
		int x,y;
		if (flipAxes) {
			x = index2;
			y = index1;
		} else {
			x = index1;
			y = index2;
		}
		
		return board[x][y];
	}
	
	/**
	 * 
	 * @return
	 */
	
	public ArrayList<Integer[]> getHitList() {
		return hitList;
	}

	/**
	 * updates value at specific index
	 * 
	 * @param index1
	 * @param index2
	 * @param status
	 * @param flipAxes
	 * @throws SolvingException
	 */
	public void update(int index1, int index2, int status, boolean flipAxes) throws SolvingException {
		int x,y;
		if (flipAxes) {
			x = index2;
			y = index1;
		} else {
			x = index1;
			y = index2;
		}
		
		if (status != 1 && status != -1)
			throw(new SolvingException("status must be either 1 or -1"));
		if (status == 1) {
			Integer[] pair = new Integer[2];
			pair[0] = x;
			pair[1] = y;
			hitQueue.add(pair);
			
			if (board[x][y] == -1)
				throw(new SolvingException("Logical contradiction: cannot assign O to X"));
		} else {
			if (board[x][y] == 1)
				throw(new SolvingException("Logical contradiction: cannot assign O to X"));
		}
		board[x][y] = status;
	}
	
	/**
	 * takes the most recent hits not yet expanded and turns everything else in 
	 * their rows and columns into misses, removes hits from hitQueue
	 * 
	 * @return
	 * @throws SolvingException
	 */

	public boolean expandHits() throws SolvingException {
		if (hitQueue.isEmpty())
			return false;
		else {
			Integer[] pair;
			while(!hitQueue.isEmpty()) {
				pair = hitQueue.get(0);
				hitQueue.remove(pair);
				
				for (int x = 0; x < board.length; x++) {
					if (x != pair[0]) update(x, pair[1], -1, false);
				}
				for (int y = 0; y < board.length; y++) {
					if (y != pair[1]) update(pair[0], y, -1, false);
				}
				
				hitList.add(pair);
			}
			
			return true;
		}
	}
	
	/**
	 * parses through board and fills in holes (single non-decided spaces) in rows
	 * and columns with hits, adds the hits to the hit queue
	 * 
	 * @return
	 * @throws SolvingException
	 */

	public boolean inferHits() throws SolvingException {
		boolean result = false;
		
		int temp;
		for (int x = 0; x < board.length; x++) {
			temp = -1;
			for (int y = 0; y < board.length; y++) {
				if (board[x][y] == 0) {
					if (temp == -1) {
						temp = y;
					}
					else {
						temp = -1;
						break;
					}
				} else if (board[x][y] == 1) {
					temp = -2;
					continue;
				}
			}
			
			if (temp >= 0) {
				update(x, temp, 1, false);
				result = true;
			}
		}
		
		for (int y = 0; y < board.length; y++) {
			temp = -1;
			for (int x = 0; x < board.length; x++) {
				if (board[x][y] == 0) {
					if (temp == -1) {
						temp = x;
					}
					else {
						temp = -1;
						break;
					}
				} else if (board[x][y] == 1) {
					temp = -2;
					continue;
				}
			}
			
			if (temp >= 0) {
				update(temp, y, 1, false);
				result = true;
			}
		}
		
		return result;
	}
}
