package objects;

/**
 * This class defines the components for the idea of a numerical relation between
 * option links for a specific numerical category
 * 
 * @author matthiaswilder
 *
 */

public class Relation {
	
	CategoryOptionPair lesserCatOptPair;
	CategoryOptionPair greaterCatOptPair;
	int difference;
	
	/**
	 * 
	 * @param catOptPair1
	 * @param catOptPair2
	 * @param difference
	 */
	
	public Relation(CategoryOptionPair catOptPair1, CategoryOptionPair catOptPair2, int difference) {
		this.lesserCatOptPair = catOptPair1;
		this.greaterCatOptPair = catOptPair2;
		this.difference = difference;
	}
	
	/**
	 * 
	 * @return
	 */
	
	public CategoryOptionPair getCatOptPair1() {
		return lesserCatOptPair;
	}
	
	/**
	 * 
	 * @return
	 */

	public CategoryOptionPair getCatOptPair2() {
		return greaterCatOptPair;
	}
	
	/**
	 * 
	 * @return
	 */
	
	public int getDifference() {
		return difference;
	}
}
