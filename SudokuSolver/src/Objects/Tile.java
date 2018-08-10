package Objects;

import java.util.HashSet;
import java.util.Set;

public class Tile {	
	private int value;
	private int rowIndex;
	private int columnIndex;
	private Set<Integer> possibilities;
	
	public Tile(int rowIndex, int columnIndex) {
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
	}
	
	public Tile(int rowIndex, int columnIndex, Set<Integer> possibilities) {
		this(rowIndex, columnIndex);
		this.value = -1;
		this.possibilities = possibilities;
	}
	
	public Tile(int rowIndex, int columnIndex, int value) {
		this(rowIndex, columnIndex);
		this.value = value;
		if (value == 0) {
			this.possibilities = new HashSet<Integer>();
			for (int i = 1; i < 10; i++) this.possibilities.add(i);
		}
	}
	
	public Tile(Tile tile) {
		this.value = tile.getValue();
		this.columnIndex = tile.getColumnIndex();
		this.rowIndex = tile.getRowIndex();
		this.possibilities = (tile.getPossibilities() == null)? null : new HashSet<Integer>(tile.getPossibilities());
	}

	public void removePossibility(int n) {
		if (possibilities != null) possibilities.remove(n);
	}
	
	public void removePossibilities(Set<Integer> set) {
		possibilities.removeAll(set);
	}
	
	public Set<Integer> getPossibilities() {
		return possibilities;
	}
	
	public void assertValue(int value) {
		this.value = value;
		this.possibilities = null;
	}
	
	public int getValue() {
		return value;
	}
	
	public int getRowIndex() {
		return rowIndex;
	}
	
	public int getColumnIndex() {
		return columnIndex;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Tile)) return false;
		Tile tile = (Tile) obj;
		
		boolean result = (value == tile.getValue());
		result = result && 
				((possibilities == null && tile.getPossibilities() == null) || 
						(possibilities != null 
						&& tile.getPossibilities() != null 
						&& possibilities.containsAll(tile.getPossibilities()) 
						&& tile.getPossibilities().containsAll(possibilities)));
		return result;
	}

	public boolean isValid() {
		return (value != 0) || ((possibilities !=  null) && (possibilities.size() != 0));
	}
}
