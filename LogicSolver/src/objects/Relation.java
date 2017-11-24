package objects;

/**
 * This class defines the components for the idea of a numerical relation between
 * option links for a specific numerical category
 * 
 * @author matthiaswilder
 *
 */

public class Relation {
	
	private int mainCategoryIndex;
	private int[] categoryIndices;
	private int[] optionIndices;
	private int difference;
	private boolean[] active;

	
	public Relation(int mainCategoryIndex, int category1Index, int option1Index, int category2Index, int option2Index, int difference) {
		this.categoryIndices = new int[2];
		this.optionIndices = new int[2];
		this.active = new boolean[2];
		
		this.mainCategoryIndex = mainCategoryIndex;
		this.categoryIndices[0] = category1Index;
		this.optionIndices[0] = option1Index;
		this.categoryIndices[1] = category2Index;
		this.optionIndices[1] = option2Index;
		this.difference = difference;
		this.active[0] = true;
		this.active[1] = true;
	}
	
	public int getMainCategoryIndex() {
		return mainCategoryIndex;
	}
	
	public int getCategoryIndex(boolean isGreater) {
		if (isGreater)
			return categoryIndices[1];
		else 
			return categoryIndices[0];
	}
	
	public int getOptionIndex(boolean isGreater) {
		if (isGreater)
			return optionIndices[1];
		else 
			return optionIndices[0];
	}
	
	public boolean isGreaterLessThan() {
		return difference == 0;
	}
	
	public int getDifference() {
		return difference;
	}
	
	public boolean isActive(int x) {
		return active[x];
	}
	
	public void markInactive(int x) {
		active[x] = false;
	}
}
