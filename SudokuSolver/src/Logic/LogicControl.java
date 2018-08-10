package Logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import Objects.*;

public class LogicControl {
	public static int divcount = 0;
	public static int maxlevel = 0;
	
	public static void main (String[] args) {
		/*String input =   
				  "700"+"201"+"008"
				+ "040"+"080"+"050"
				+ "005"+"000"+"700"
				+ "100"+"309"+"005"
				+ "050"+"010"+"070"
				+ "200"+"507"+"001"
				+ "009"+"000"+"800"
				+ "030"+"070"+"090"
				+ "500"+"908"+"006";*/
		
		String input =   
				  "800"+"000"+"000"
				+ "003"+"600"+"000"
				+ "070"+"090"+"200"
				+ "050"+"007"+"000"
				+ "000"+"045"+"700"
				+ "000"+"100"+"030"
				+ "001"+"000"+"068"
				+ "008"+"500"+"010"
				+ "090"+"000"+"400";
		
		Board board = new Board(input);
		long startTime = System.nanoTime();
		recurSortAndSolve(board, 0);
		long endTime = System.nanoTime();
		
		System.out.println(board);
		System.out.println("\n");
		System.out.println("Board is Valid: " + board.isValid());
		System.out.println("Board is Filled: " + board.isFilled());
		System.out.println("Board is Complete: " + board.isComplete());
		System.out.println("Board contains original input : " + board.contains(new Board(input)));
		System.out.println("Divcount: " + divcount);
		System.out.println("Solve Time = " + ((endTime - startTime))/1000000);
		System.out.println("Max Levle : " + maxlevel);
	} 

	public static void condensePossibilities(Board board) {
		for (TileCollection collection : board.getAllCollections()) {
			collection.reducePossibilities();
			collection.fillHoles();
		}
	}
	
	public static boolean recurSortAndSolve(Board board, int level) {
		if (level > maxlevel) maxlevel = level;
		condensePossibilities(board);
		Tile branchTile = null;
		
		for(Tile tile : board.getTiles()) {
			if (tile.getValue() == 0) {
				branchTile = tile;
				break;
			}
		}
		depthFirstSearch(board, branchTile, level);
		return board.isValid() && board.isComplete();
	}

	private static void depthFirstSearch(Board board, Tile branchTile, int level) {

		if (branchTile == null || branchTile.getPossibilities() == null) return;
		else {
			for (int i : branchTile.getPossibilities()) {
				divcount++;
				Board testBoard = new Board(board);
				Tile changeTile = testBoard.getTile(branchTile.getRowIndex(), branchTile.getColumnIndex());
				changeTile.assertValue(i);
				
				boolean result = recurSortAndSolve(testBoard, level + 1);
				
				if (result) {
					board.copy(testBoard);
					return;
				}
			}
		}
	}
	

	private static Tile bubbleSearch(ArrayList<Tile> list) {
		Iterator<Tile> iterator = list.iterator();
		Tile a = null;
		while (iterator.hasNext())  {
			a = iterator.next();
			if(a.getPossibilities() == null) {
				iterator.remove();
			} else {
				break;
			}
		}
		
		if (a == null) return null;
		
		if (a.getPossibilities().size() == 1) {
			iterator.remove();
			return a;
		}
		
		while (iterator.hasNext()) {
			Tile b = iterator.next();
			if (b.getPossibilities() == null) {
				iterator.remove();
				continue;
			}
			
			if (b.getPossibilities().size() == 1) {
				iterator.remove();
				return b;
			}
			
			if (b.getPossibilities().size() > a.getPossibilities().size()) {
				a = b;
			}
		}
		
		return null;
	}
	
	private static void employCollectionLogic(Board board, TileCollection collection) {
		for (int i = 1; i < 10; i++) {
			List<Tile> tiles = collection.getTileWithValuePossible(i);
			if (tiles.size() <= 1 || tiles.size() > 3) continue;
			
			TileCollection candidate = null;
			boolean candidateValid = false;
			
			for (TileCollectionType type : TileCollectionType.values()) {
				if (type == collection.getType()) continue;
				candidateValid = true;
				
				candidate = board.getCollectionfromTile(tiles.get(0), type);
				for (Tile tile : tiles) {
					candidateValid = candidateValid && (candidate == board.getCollectionfromTile(tile, type));
					if (!candidateValid) break;
				}
				if (candidateValid) break;
			}
			
			if (candidateValid) {
				
				Tile[] reduceTiles = candidate.getTiles();
				
				for (Tile tile : reduceTiles) {
					if (!tiles.contains(tile)) tile.removePossibility(i);
				}
			}
		}
	}
}

/*

Board oldBoard = new Board(board);
while (!oldBoard.equals(board)) {
	oldBoard = new Board(board);
	Tile result = bubbleSearch(list);

	while (result != null) {
		result.assertValue((int) result.getPossibilities().toArray()[0]);
		for (TileCollectionType type : TileCollectionType.values())
			board.getCollectionfromTile(result, type).reducePossibilities();
		
		result = bubbleSearch(list);
	}
	
	condensePossibilities(board);
	for (int i = 0; i < 9; i++) {
		employCollectionLogic(board, board.getSquare(i));
		employCollectionLogic(board, board.getRow(i));
		employCollectionLogic(board, board.getColumn(i));
	}
}*/