package rules;
import exceptions.SetupException;
import exceptions.LogicException;
import objects.LogicPuzzle;
import objects.Option;

/**
 * This class defines a declaration rule, one that defines the 
 * relationship between two options as either an 'X' or 'O'
 * 
 * @author matthiaswilder
 *
 */

public class DeclarationRule implements Rule {
	
	String category1;
	String category2;
	String option1;
	String option2;
	int value;
	
	/**
	 * 
	 * @param string
	 */
	
	public DeclarationRule(String string) {
		String[] x = string.split(" ");
		
		category1 = x[0];
		category2 = x[1];
		option1 = x[2];
		option2 = x[3];
		value = Integer.parseInt(x[4]);
	}

	@Override
	public void applyTo(LogicPuzzle lp) throws LogicException, SetupException {
		int cat1 = lp.getCategoryFromName(category1);
		int cat2 = lp.getCategoryFromName(category2);
		int opt1 = lp.getOptionFromName(cat1, option1);
		int opt2 = lp.getOptionFromName(cat2, option2);

		try {
			if (value == -1) {
				lp.getOption(cat1, opt1).declareMiss(cat2, opt2);
				lp.getOption(cat2, opt2).declareMiss(cat1, opt1);
			} else {
				lp.getOption(cat1, opt1).declareLink(cat2, opt2);
			}
		} catch (LogicException e) {
			e.addOption(cat1, opt1);
			throw e;
		}
	}

}
