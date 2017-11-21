package rules;
import exceptions.SetupException;
import exceptions.LogicException;
import objects.LogicPuzzle;

/**
 * This class defines a declaration rule, one that defines the 
 * relationship between two options as either an 'X' or 'O'
 * 
 * @author matthiaswilder
 *
 */

public class DeclarationRule implements Rule {
	
	private LogicPuzzle lp;
	private int category1;
	private int category2;
	private int option1;
	private int option2;
	private int value;
	
	public DeclarationRule(LogicPuzzle lp) {
		this.lp = lp;
	}
	
	/**
	 * 
	 * @param lp 
	 * @param string
	 * @throws SetupException 
	 */
	
	public DeclarationRule(LogicPuzzle lp, String string) throws SetupException {
		this(lp);
		String[] x = string.split(" ");
		
		category1 = this.lp.getCategoryFromName(x[0]);
		category2 = this.lp.getCategoryFromName(x[1]);
		option1 = this.lp.getOptionFromName(category1, x[2]);
		option2 = this.lp.getOptionFromName(category2, x[3]);
		value = Integer.parseInt(x[4]);
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

	public void setValue(int value) {
		this.value = value;
	}


	@Override
	public void apply() throws LogicException, SetupException {
		try {
			if (value == -1) {
				lp.getOption(category1, option1).declareMiss(category2, option2);
				lp.getOption(category2, option2).declareMiss(category1, option1);
			} else {
				lp.getOption(category1, option1).declareLink(category2, option2);
			}
		} catch (LogicException e) {
			e.addOption(category1, option1);
			throw e;
		}
	}

	@Override
	public String buildRuleString() {
		String str = "";
		
		try {
			str += lp.getOptionName(category1, option1) + " from " + lp.getCategoryName(category1);
			if (value == -1) 
				str += " is not ";
			else 
				str += " is ";
			str += lp.getOptionName(category2, option2) + " from " + lp.getCategoryName(category2);
		} catch (SetupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(str);
		
		return str;
	}

}
