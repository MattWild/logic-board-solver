package objects;

public class Relation {
	
	CategoryOptionPair lesserCatOptPair;
	CategoryOptionPair greaterCatOptPair;
	int difference;
	
	public Relation(CategoryOptionPair catOptPair1, CategoryOptionPair catOptPair2, int difference) {
		this.lesserCatOptPair = catOptPair1;
		this.greaterCatOptPair = catOptPair2;
		this.difference = difference;
	}
	
	public CategoryOptionPair getCatOptPair1() {
		return lesserCatOptPair;
	}

	public CategoryOptionPair getCatOptPair2() {
		return greaterCatOptPair;
	}
	
	public int getDifference() {
		return difference;
	}
}
