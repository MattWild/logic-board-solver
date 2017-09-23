package objects;
import java.util.ArrayList;
import exceptions.SetupException;

/**
 * This class defines an abstract category for the use of a logic board
 * 
 * @author matthiaswilder
 *
 */

public abstract class Category {
	
	protected Object[] options;
	protected String name;
	private ArrayList<SubBoard> subBoards;
	private boolean validated = false;
	private ArrayList<ArrayList<CategoryOptionPair>> restrictions;
	
	/**
	 * 
	 * @param name
	 * @param elementNumber
	 */
	
	public Category(String name, int elementNumber) {
		this.name = name;
		this.options = new Object[elementNumber];
		this.subBoards = new ArrayList<SubBoard>();
		this.restrictions = new ArrayList<ArrayList<CategoryOptionPair>>();
		for (int i = 0; i < elementNumber; i++) {
			restrictions.add(null);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @param option
	 * @return
	 * @throws SetupException
	 */
	
	public abstract int getOptionIndex(Object option) throws SetupException;
	
	/**
	 * 
	 * @param index
	 * @return
	 * @throws SetupException
	 */
	
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
	
	/**
	 * 
	 * @param name
	 * @throws SetupException
	 */
	
	public abstract void addOption(Object name) throws SetupException;
	
	/**
	 * 
	 * @param index
	 * @param targetCategory
	 * @param targetOptionIndex
	 * @throws SetupException
	 */
	
	public void addRestriction(int index, Category targetCategory, int targetOptionIndex) throws SetupException {
		if(restrictions.get(index) == null) 
			restrictions.set(index, new ArrayList<CategoryOptionPair>());
		restrictions.get(index).add(new CategoryOptionPair(targetCategory, targetOptionIndex));
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	
	public ArrayList<CategoryOptionPair> getRestrictions(int index) {
		return restrictions.get(index);
	}
	
	/**
	 * 
	 * @return
	 */
	
	public ArrayList<SubBoard> getBoards() {
		return subBoards;
	}
	
	/**
	 * 
	 * @param cat
	 * @return
	 */
	
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
	
	/**
	 * 
	 * @param board
	 */
	
	public void addBoard(SubBoard board) {
		subBoards.add(board);
	}

	/**
	 * interrogates the options to assure the logic puzzle has been set up correctly
	 * 
	 * @throws SetupException	an option hasn't been initialized
	 */
	
	public void validate() throws SetupException {
		if (!validated) {
			for (int i = 0; i < options.length; i++) {
				getOption(i);
			}
			validated = true;
		}
	}
	
	/**
	 * removes the restrictions for a particular option
	 * 
	 * @param i
	 */

	public void eliminateRestrictions(int i) {
		restrictions.set(i, null);
	}
}
