package rules;
import exceptions.LogicException;
import exceptions.SetupException;
import objects.PuzzleLogic;
import presentation.MainApp;

public class DoubleRestrictionRule implements Rule {
	private int[] categories1;
	private int[] options1;
	private int[] categories2;
	private int[] options2;
	private Rule[] subrules;
	
	public DoubleRestrictionRule() {
		categories1 = new int[2];
		options1 = new int[2];
		categories2 = new int[2];
		options2 = new int[2];
	}

	/*public DoubleRestrictionRule(PuzzleLogic lp, String string) throws SetupException {
		this(lp);
		
		String[] x = string.split(" ");
		
		categories1[0] = x[0];
		categories1[1] = x[1];
		options1[0] = x[2];
		options1[1] = x[3];
		categories2[0] = x[4];
		categories2[1] = x[5];
		options2[0] = x[6];
		options2[1] = x[7];
		
		process();
	}*/
	
	public void process() throws SetupException {
		RestrictionRule r1 = new RestrictionRule();
		r1.setCategoryToRestrict(categories1[0]);
		r1.setOptionToRestrict(options1[0]);
		r1.addTargetOption(categories2[0], options2[0]);
		r1.addTargetOption(categories2[1], options2[1]);
		
		RestrictionRule r2 = new RestrictionRule();
		r2.setCategoryToRestrict(categories1[1]);
		r2.setOptionToRestrict(options1[1]);
		r2.addTargetOption(categories2[0], options2[0]);
		r2.addTargetOption(categories2[1], options2[1]);
		
		RestrictionRule r3 = new RestrictionRule();
		r3.setCategoryToRestrict(categories2[0]);
		r3.setOptionToRestrict(options2[0]);
		r3.addTargetOption(categories1[0], options1[0]);
		r3.addTargetOption(categories1[1], options1[1]);
		
		RestrictionRule r4 = new RestrictionRule();
		r4.setCategoryToRestrict(categories2[1]);
		r4.setOptionToRestrict(options2[1]);
		r4.addTargetOption(categories1[0], options1[0]);
		r4.addTargetOption(categories1[1], options1[1]);
		
		subrules = new Rule[]{r1,r2,r3,r4};
	}

	public void setFirstCategoryInFirstPair(int category) {
		categories1[0] = category;
	}
	
	public void setSecondCategoryInFirstPair(int category) {
		categories1[1] = category;
	}
	
	public void setFirstCategoryInSecondPair(int category) {
		categories2[0] = category;
	}
	
	public void setSecondCategoryInSecondPair(int category) {
		categories2[1] = category;
	}
	
	public void setFirstOptionInFirstPair(int option) {
		options1[0] = option;
	}
	
	public void setSecondOptionInFirstPair(int option) {
		options1[1] = option;
	}
	
	public void setFirstOptionInSecondPair(int option) {
		options2[0] = option;
	}
	
	public void setSecondOptionInSecondPair(int option) {
		options2[1] = option;
	}

	@Override
	public void apply(PuzzleLogic logic) throws LogicException, SetupException {
		process();
		for(Rule r : subrules) 
			r.apply(logic);
	}

	@Override
	public String buildRuleString(MainApp mainApp) {
		String str = "";
		
		str += "Of ";
		str += mainApp.getOptionName(categories1[0], options1[0]) + " from " + mainApp.getCategoryName(categories1[0]);
		str += " and ";
		str += mainApp.getOptionName(categories1[1], options1[1]) + " from " + mainApp.getCategoryName(categories1[1]);
		str += ", one is ";
		str += mainApp.getOptionName(categories2[0], options2[0]) + " from " + mainApp.getCategoryName(categories2[0]);
		str += " and one is ";
		str += mainApp.getOptionName(categories2[1], options2[1]) + " from " + mainApp.getCategoryName(categories2[1]);
		
		
		return str;
	}

}
