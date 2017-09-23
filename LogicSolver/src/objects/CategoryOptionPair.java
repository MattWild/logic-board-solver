package objects;
import exceptions.SetupException;

public class CategoryOptionPair {
	private Category cat;
	private int optionIndex;
	
	public CategoryOptionPair(Category cat, int optionIndex) throws SetupException {
		this.cat = cat;
		this.optionIndex = optionIndex;
	}
	
	public int getOptionIndex() {
		return optionIndex;
	}
	
	public Category getCategory() {
		return cat;
	}
}
