package objects;

public class NumericOption extends Option {
	
	private int value;

	public NumericOption(LogicPuzzle lp, int categoryNum, int optionNum, int categoryInd, int optionInd, int value) {
		super(lp, categoryNum, optionNum, categoryInd, optionInd);
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}

}
