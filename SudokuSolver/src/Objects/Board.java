package Objects;

public class Board {
	
	private Tile[] tiles;
	private TileCollection[] rows;
	private TileCollection[] columns;
	private TileCollection[] squares;
	
	public Board() {
		tiles = new Tile[81];
	}
	
	public Board(String s) {
		this();
		parseBoard(s);
		setupCollections();
	}
	
	public Board(Board board) {
		this();
		for (int i = 0; i < 81; i++) {
			tiles[i] = new Tile(board.getTile(i/9, i%9));
		}
		setupCollections();
	}

	public Tile getTile(int i, int j) {
		return tiles[i*9 + j];
	}
	
	public Tile getTile(int i) {
		return tiles[i];
	}
	
	public Tile[] getTiles() {
		return tiles;
	}
	
	public TileCollection[] getAllCollections() {
		TileCollection[] result = new TileCollection[27];
		for (int i = 0; i < 9; i++) {
			result[i] = getRow(i);
			result[9 + i] = getColumn(i);
			result[18 + i] = getSquare(i);
		}
		
		return result;
	}
	
	public TileCollection getSquare(int i) {
		return squares[i];
	}
	
	public TileCollection getRow(int i) {
		return rows[i];
	}
	
	public TileCollection getColumn(int i) {
		return columns[i];
	}
	
	public TileCollection getCollectionfromTile(Tile tile, TileCollectionType type) {
		switch (type) {
		case SQUARE:
			return getSquare((tile.getRowIndex() / 3) * 3 + (tile.getColumnIndex() / 3));
			
		case ROW:
			return getRow(tile.getRowIndex());
			
		case COLUMN:
			return getColumn(tile.getColumnIndex());
			
		default:
			return null;
		}
	}

	private void parseBoard(String s) {
		if (s.length() == 81) {
			for (int i = 0; i < 81; i++) {
				tiles[i] = new Tile(i / 9, i % 9, Integer.parseInt(s.substring(i, i+1)));
			}
		}
	}
	
	private void setupCollections() {
		rows = new TileCollection[9];
		columns = new TileCollection[9];
		squares = new TileCollection[9];
		
		for (int i = 0; i < 9; i++) {
			Tile[] rowTiles = new Tile[9];
			for (int j = 0; j < 9; j++) rowTiles[j] = getTile(i,j);
			TileCollection row = new TileCollection(rowTiles, TileCollectionType.ROW);
			
			Tile[] columnTiles = new Tile[9];
			for (int j = 0; j < 9; j++) columnTiles[j] = getTile(j,i);
			TileCollection column = new TileCollection(columnTiles, TileCollectionType.COLUMN);
			
			Tile[] squareTiles = new Tile[9];
			int rowStart = i / 3 * 3;
			int columnStart = i % 3 * 3;
			for(int j = 0; j < 3; j++) 
				for(int k = 0; k < 3; k++) 
					squareTiles[3 * j + k] = getTile(rowStart + j,columnStart + k);
			TileCollection square = new TileCollection(squareTiles, TileCollectionType.SQUARE);
			
			rows[i] = row;
			columns[i] = column;
			squares[i] = square;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 81; i++) {
			sb.append(tiles[i].getValue());
			if ((i + 1) % 9 == 0) sb.append("\n");
		}
		
		return sb.toString();
	}
	
	public String toCopyString() {
		return toString().replaceAll("\n", "");
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (!(obj instanceof Board)) return false;	
		Board board = (Board) obj;
		
		for (int i = 0; i < 81; i++) {
			if (!tiles[i].equals(board.getTile(i))) return false;
		}
		
		return true;
	}
	
	public boolean contains(Board board) {
		for (int i = 0; i < 81; i++) {
			if (tiles[i].getValue() != board.getTile(i).getValue()) {
				if (board.getTile(i / 9, i % 9).getValue() != 0) 
					return false;
			}
		}
		
		return true;
	}
	
	public boolean isValid() {
		for (TileCollection collection : getAllCollections()) {
			if (!collection.isValid()) return false;
		}
		
		
		for (Tile tile : tiles) {
			if (!tile.isValid()) return false;
		}
		
		return true;
	}
	
	public boolean isFilled() {
		for (Tile tile : getTiles()) {
			if (tile.getValue() == 0) return false;
		}
		
		return true;
	}
	
	public boolean isComplete() {
		return isFilled() && isValid();
	}

	public void copy(Board board) {
		for (int i = 0; i < 81; i++) {
			tiles[i] = new Tile(board.getTile(i/9, i%9));
		}
		setupCollections();
	}
}
