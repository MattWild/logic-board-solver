package rules;
import exceptions.LogicException;
import exceptions.SetupException;
import objects.LogicPuzzle;
import objects.Option;
import objects.Relation;

/**
 * This class defines a relation rule, one that relates the values
 * from a numerical class for two other options by an equation or 
 * inequality
 * 
 * @author matthiaswilder
 *
 */

public class RelationRule implements Rule {
	
	private LogicPuzzle lp;
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

	public RelationRule(LogicPuzzle lp, String string) throws SetupException {
		String[] x = string.split(" ");
		
		mainCategory = lp.getCategoryFromName(x[0]);
		category1 = lp.getCategoryFromName(x[1]);
		category2 = lp.getCategoryFromName(x[2]);
		option1 = lp.getOptionFromName(category1, x[3]);
		option2 = lp.getOptionFromName(category2, x[4]);
		difference = Integer.parseInt(x[5]);
	}

	public RelationRule(LogicPuzzle lp) {
		this.lp = lp;
	}

	public void setMainCategory(String mainCategory) throws SetupException {
		this.mainCategory = lp.getCategoryFromName(mainCategory);
	}



	public void setCategory1(String category1) throws SetupException {
		this.category1 = lp.getCategoryFromName(category1);
	}



	public void setCategory2(String category2) throws SetupException {
		this.category2 = lp.getCategoryFromName(category2);
	}



	public void setOption1(String option1) throws SetupException {
		this.option1 = lp.getOptionFromName(category1, option1);
	}



	public void setOption2(String option2) throws SetupException {
		this.option2 = lp.getOptionFromName(category2, option2);
	}



	public void setDifference(int difference) {
		this.difference = difference;
	}



	@Override
	public void apply() throws LogicException, SetupException {
		Option opt1 = lp.getOption(category1, option1);
		Option opt2 = lp.getOption(category2, option2);
		
		Relation r;
		if (difference == 0) {
			r = new Relation(mainCategory, category1, option1, category2, option2, -1, true);
		} else {
			int[] param = lp.getCategoryParams(mainCategory);	
			System.out.println(difference / param[1]);
			r = new Relation(mainCategory, category1, option1, category2, option2, difference / param[1], false);
		}
		
		opt1.declareMiss(category2, option2);
		opt2.declareMiss(category1, option1);
		
		opt1.addRelation(r, false);
		opt2.addRelation(r, true);
	}

}
