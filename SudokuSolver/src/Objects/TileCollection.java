package Objects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TileCollection {
	private Tile[] tiles;
	private TileCollectionType type;
	
	public TileCollection(Tile[] tiles, TileCollectionType type) {
		this.tiles = tiles;
		this.type = type;
	}
	
	public void reducePossibilities() {
		HashSet<Integer> exclusions = new HashSet<Integer>();
		ArrayList<Tile> toBeReduced = new ArrayList<Tile>();
		for (Tile tile : tiles) {
			if (tile.getValue() == 0)
				toBeReduced.add(tile);
			else
				exclusions.add(tile.getValue());
		}
		
		for (Tile tile : toBeReduced) {
			tile.removePossibilities(exclusions);
		}
	}
	
	public Tile[] getTiles() {
		return tiles;
	}
	
	public TileCollectionType getType() {
		return type;
	}

	public void fillHoles() {
		for (int value = 1; value < 10; value++) {
			boolean breakCondition = false;
			Tile onlyOption = null;
			
			for (Tile tile : tiles) {
				if (tile.getValue() == value) {
					breakCondition = true;
					break;
				} else if (tile.getValue() != 0){
					continue;
				} else {
					if (tile.getPossibilities().contains(value)) {
						if (onlyOption == null) {
							onlyOption = tile;
						} else {
							breakCondition = true;
							break;
						}
					}
				}
			}
			
			if (breakCondition) continue;
			else {
				if (onlyOption != null) {
					onlyOption.assertValue(value);
				}
			}
		}
	}

	public boolean isValid() {
		Set<Integer> values = new HashSet<Integer>();
		for (int i = 0; i < 9; i++) {
			int value = tiles[i].getValue();
			if (value == 0) continue;
			if (values.contains(value)) {
				return false;
			}
			else values.add(value);
		}
		
		return true;
	}

	public List<Tile> getTileWithValuePossible(int value) {
		ArrayList<Tile> result = new ArrayList<Tile>();
		for (Tile tile : tiles) {
			if (tile.getValue() == value) {
				result.add(tile);
				break;
			} else if (tile.getPossibilities() != null && tile.getPossibilities().contains(value)) {
				result.add(tile);
			}
		}
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(); 
		
		sb.append(type.toString() + " \n");
		
		switch (type) {
		case ROW:
			for (int i = 0; i < 9; i++) sb.append(tiles[i].getValue());
			break;
			
		case COLUMN:
			for (int i = 0; i < 9; i++) sb.append(tiles[i].getValue() + "\n");
			break;
			
		case SQUARE:
			for (int i = 0; i < 3; i++) sb.append(tiles[i].getValue());
			sb.append('\n');
			for (int i = 3; i < 6; i++) sb.append(tiles[i].getValue());
			sb.append('\n');
			for (int i = 6; i < 9; i++) sb.append(tiles[i].getValue());
			break;
		}
		
		return sb.toString();
	}
}
