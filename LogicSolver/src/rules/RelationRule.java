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
	
	String mainCategory;
	String category1;
	String category2;
	String option1;
	String option2;
	int difference;
	
	/**
	 * 
	 * @param string
	 */

	public RelationRule(String string) {
		String[] x = string.split(" ");
		
		mainCategory = x[0];
		category1 = x[1];
		category2 = x[2];
		option1 = x[3];
		option2 = x[4];
		difference = Integer.parseInt(x[5]);
	}

	@Override
	public void applyTo(LogicPuzzle lp) throws LogicException, SetupException {
		int main = lp.getCategoryFromName(mainCategory);
		int cat1 = lp.getCategoryFromName(category1);
		int cat2 = lp.getCategoryFromName(category2);
		int opt1 = lp.getOptionFromName(cat1, option1);
		int opt2 = lp.getOptionFromName(cat2, option2);
		
		Option option1 = lp.getOption(cat1, opt1);
		Option option2 = lp.getOption(cat2, opt2);
		
		Relation r;
		if (difference == 0) {
			r = new Relation(main, cat1, opt1, cat2, opt2, -1, true);
		} else {
			int[] param = lp.getCategoryParams(mainCategory);	
			System.out.println(difference / param[1]);
			r = new Relation(main, cat1, opt1, cat2, opt2, difference / param[1], false);
		}
		
		option1.declareMiss(cat2, opt2);
		option2.declareMiss(cat1, opt1);
		
		option1.addRelation(r, false);
		option2.addRelation(r, true);
	}

}
