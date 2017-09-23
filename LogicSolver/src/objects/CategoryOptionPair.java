package objects;
import exceptions.SetupException;

/**
 * this is a helper class designed to categories and options
 * 
 * @author matthiaswilder
 *
 */

public class CategoryOptionPair {
	private Category cat;
	private int optionIndex;
	
	/**
	 * 
	 * @param cat
	 * @param optionIndex
	 * @throws SetupException
	 */
	
	public CategoryOptionPair(Category cat, int optionIndex) throws SetupException {
		this.cat = cat;
		this.optionIndex = optionIndex;
	}
	
	/**
	 * 
	 * @return
	 */
	
	public int getOptionIndex() {
		return optionIndex;
	}
	
	/**
	 * 
	 * @return
	 */
	
	public Category getCategory() {
		return cat;
	}
}
