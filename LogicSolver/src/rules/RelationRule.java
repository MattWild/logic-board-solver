package rules;
import exceptions.LogicException;
import exceptions.SetupException;
import objects.PuzzleLogic;
import objects.Option;
import objects.Relation;
import presentation.MainApp;

/**
 * This class defines a relation rule, one that relates the values
 * from a numerical class for two other options by an equation or 
 * inequality
 * 
 * @author matthiaswilder
 *
 */

public class RelationRule implements Rule {
	private int mainCategory;
	private int category1;
	private int category2;
	private int option1;
	private int option2;
	private int difference;
	
	/**
	 * 
	 * @param lp 
	 * @param string
	 * @throws SetupException 
	 */

	/*public RelationRule(PuzzleLogic lp, String string) throws SetupException {
		String[] x = string.split(" ");
		
		mainCategory = lp.getCategoryIndex(x[0]);
		category1 = lp.getCategoryIndex(x[1]);
		category2 = lp.getCategoryIndex(x[2]);
		option1 = lp.getOptionFromName(category1, x[3]);
		option2 = lp.getOptionFromName(category2, x[4]);
		difference = Integer.parseInt(x[5]);
	}*/

	
	public RelationRule() {
		
	}

	public void setMainCategory(int mainCategory) {
		this.mainCategory = mainCategory;
	}



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



	public void setDifference(int difference) {
		this.difference = difference;
	}



	@Override
	public void apply(PuzzleLogic logic) throws LogicException, SetupException {
		logic.setupRelation(mainCategory, category1, option1, category2, option2, difference);
	}

	@Override
	public String buildRuleString(MainApp mainApp) {
		String str = "";
		
		str += "In " + mainApp.getCategoryName(mainCategory) + ", ";
		str += mainApp.getOptionName(category2,option2) + " from " + mainApp.getCategoryName(category2);
		str += " is greater than ";
		str += mainApp.getOptionName(category1,option1) + " from " + mainApp.getCategoryName(category1);
		if (difference != 0) {
			str += " by " + difference + " units";
		}
		
		return str;
	}

}
