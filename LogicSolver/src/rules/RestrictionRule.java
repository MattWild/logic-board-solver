package rules;
import java.util.HashSet;
import java.util.Set;

import exceptions.LogicException;
import exceptions.SetupException;
import objects.PuzzleLogic;
import presentation.MainApp;

/**
 * This class defines a restriction rule, one that limits the 
 * possible links for a given option to an either or situation
 * (requires two rules)
 * 
 * @author matthiaswilder
 *
 */

public class RestrictionRule implements Rule {
	private int categoryToRestrict;
	private int optionToRestrict;
	Set<int[]> restrictedCategoryOptions;
	
	public RestrictionRule() {
		restrictedCategoryOptions = new HashSet<int[]>();
	}

	public void setCategoryToRestrict(int categoryToRestrict) {
		this.categoryToRestrict = categoryToRestrict;
	}

	public void setOptionToRestrict(int optionToRestrict) {
		this.optionToRestrict = optionToRestrict;
	}
	
	public void addTargetOption(int categoryIndex, int targetOption) {
		restrictedCategoryOptions.add(new int[]{categoryIndex, targetOption});
	}


	@Override
	public void apply(PuzzleLogic logic) throws LogicException, SetupException {
		logic.setupRestriction(categoryToRestrict, optionToRestrict, restrictedCategoryOptions);
	}

	@Override
	public String buildRuleString(MainApp mainApp) {
		String str = "";
		str +=  mainApp.getOptionName(categoryToRestrict, optionToRestrict)+ " from " + mainApp.getCategoryName(categoryToRestrict);
		str += " is either";
		for (int[] pair : restrictedCategoryOptions) {
			str += " " + mainApp.getOptionName(pair[0], pair[1]) + " from " + mainApp.getCategoryName(pair[0]);
		}
		
		return str;
	}

}
