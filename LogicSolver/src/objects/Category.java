package objects;
import java.util.ArrayList;
import exceptions.SetupException;

public abstract class Category {
	
	protected Object[] options;
	protected String name;
	private ArrayList<SubBoard> subBoards;
	private boolean validated = false;
	private ArrayList<ArrayList<CategoryOptionPair>> restrictions;
	
	public Category(String name, int elementNumber) {
		this.name = name;
		this.options = new Object[elementNumber];
		this.subBoards = new ArrayList<SubBoard>();
		this.restrictions = new ArrayList<ArrayList<CategoryOptionPair>>();
		for (int i = 0; i < elementNumber; i++) {
			restrictions.add(null);
		}
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public abstract int getOptionIndex(Object option) throws SetupException;
	
	public Object getOption(int index) throws SetupException {
		if (index < options.length && index >= 0 ) {
			if (options[index] != null) {
				return options[index];
			} else {
				throw(new SetupException("option index: " + index + " not setup in category: " + name));
			}
		} else {
			throw(new SetupException("option index: " + index + " out of bounds in category: " + name));
		}
	}
	
	public abstract void addOption(Object name) throws SetupException;
	
	public void addRestriction(int index, Category targetCategory, int targetOptionIndex) throws SetupException {
		if(restrictions.get(index) == null) 
			restrictions.set(index, new ArrayList<CategoryOptionPair>());
		restrictions.get(index).add(new CategoryOptionPair(targetCategory, targetOptionIndex));
	}
	
	public ArrayList<CategoryOptionPair> getRestrictions(int index) {
		return restrictions.get(index);
	}
	
	public ArrayList<SubBoard> getBoards() {
		return subBoards;
	}
	
	public SubBoard getBoardWithCategory(Category cat) {
		SubBoard board = null;
		for (SubBoard b : subBoards) {
			if (b.getCategory2() == cat) {
				board = b;
				break;
			}
		}
		return board;
	}
	
	public void addBoard(SubBoard board) {
		subBoards.add(board);
	}

	public void validate() throws SetupException {
		if (!validated) {
			for (int i = 0; i < options.length; i++) {
				getOption(i);
			}
		}
	}

	public void eliminateRestrictions(int i) {
		restrictions.set(i, null);
	}
}
