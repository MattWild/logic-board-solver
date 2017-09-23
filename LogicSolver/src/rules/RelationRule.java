package rules;
import exceptions.SetupException;
import exceptions.SolvingException;
import objects.Category;
import objects.NumericCategory;
import objects.CategoryOptionPair;
import objects.LogicBoard;
import objects.Relation;
import objects.SubBoard;

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
	public void applyTo(LogicBoard lb) throws SolvingException, SetupException {
		NumericCategory main = (NumericCategory) lb.getCategory(mainCategory);
		Category cat1 = lb.getCategory(category1);
		Category cat2 = lb.getCategory(category2);
		int opt1 = cat1.getOptionIndex(option1);
		//System.out.println(category2);
		int opt2 = cat2.getOptionIndex(option2);
	
		Relation r = new Relation(new CategoryOptionPair(cat1, opt1), 
				new CategoryOptionPair(cat2, opt2),difference);
		
		main.addRelation(r);
		
		int offset = difference;
		if (offset == 0) offset = 1;
		
		boolean flipAxes1 = false;
		SubBoard subBoard1 = main.getBoardWithCategory(cat1);
		if (subBoard1 == null) {
			subBoard1 = cat1.getBoardWithCategory(main);
			flipAxes1 = true;
		}
		
		boolean flipAxes2 = false;
		SubBoard subBoard2 = main.getBoardWithCategory(cat2);
		if (subBoard2 == null) {
			subBoard2 = cat1.getBoardWithCategory(main);
			flipAxes2 = true;
		}
		
		for (int i = 0; i < lb.getElementNumber(); i++) {
			int val = (Integer) main.getOption(i);
			if (val < difference) {
				subBoard1.update(lb.getElementNumber() - i - 1, opt1, -1, flipAxes1);
				subBoard2.update(i, opt2, -1, flipAxes2);
			} else {
				break;
			}
		}
	}

}
