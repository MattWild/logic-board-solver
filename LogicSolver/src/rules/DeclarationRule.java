package rules;
import exceptions.SetupException;
import exceptions.LogicException;
import objects.PuzzleLogic;
import presentation.MainApp;

/**
 * This class defines a declaration rule, one that defines the 
 * relationship between two options as either an 'X' or 'O'
 * 
 * @author matthiaswilder
 *
 */

public class DeclarationRule implements Rule {
	
	private int category1;
	private int category2;
	private int option1;
	private int option2;
	private int value;
	
	public DeclarationRule() {
		
	}
	
	/**
	 * 
	 * @param lp 
	 * @param string
	 * @throws SetupException 
	 */
	
	/*public DeclarationRule(PuzzleLogic lp, String string) throws SetupException {
		this(lp);
		String[] x = string.split(" ");
		
		category1 = this.lp.getCategoryIndex(x[0]);
		category2 = this.lp.getCategoryIndex(x[1]);
		option1 = this.lp.getOptionFromName(category1, x[2]);
		option2 = this.lp.getOptionFromName(category2, x[3]);
		value = Integer.parseInt(x[4]);
	}*/
	

	public void setCategory1(int category1) {
		this.category1 = category1;
	}

	public void setCategory2(int category2) {
		this.category2 = category2;
	}

	public void setOption1(int option1) {
		this.option1 = option1;
	}

	public void setOption2(int option2) {
		this.option2 = option2;
	}

	public void setValue(int value) {
		this.value = value;
	}


	@Override
	public void apply(PuzzleLogic logic) throws LogicException, SetupException {
		if (value == -1) {
			logic.declareMiss(category1, option1, category2, option2);
		} else {
			logic.declareLink(category1, option1, category2, option2);
		}
	}

	@Override
	public String buildRuleString(MainApp mainApp) {
		String str = "";

		str += mainApp.getOptionName(category1, option1) + " from " + mainApp.getCategoryName(category1);
		if (value == -1) 
			str += " is not ";
		else 
			str += " is ";
		str += mainApp.getOptionName(category2, option2) + " from " + mainApp.getCategoryName(category2);

		System.out.println(str);
		
		return str;
	}

}
