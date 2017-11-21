package rules;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import exceptions.LogicException;
import exceptions.SetupException;
import objects.LogicPuzzle;
import objects.Option;
import objects.Restriction;

/**
 * This class defines a restriction rule, one that limits the 
 * possible links for a given option to an either or situation
 * (requires two rules)
 * 
 * @author matthiaswilder
 *
 */

public class RestrictionRule implements Rule {
	
	LogicPuzzle lp;
	private int categoryToRestrict;
	private int optionToRestrict;
	Set<int[]> restrictedCategoryOptions;
	

	public RestrictionRule(LogicPuzzle lp) {
		this.lp = lp;
		restrictedCategoryOptions = new HashSet<int[]>();
	}

	/**
	 * 
	 * @param lp 
	 * @param string
	 * @throws SetupException 
	 */
	
	public RestrictionRule(LogicPuzzle lp, String string) throws SetupException {
		this(lp);
		
		String[] x = string.split(" ");
		
		categoryToRestrict = this.lp.getCategoryFromName(x[0]);
		optionToRestrict = this.lp.getOptionFromName(categoryToRestrict, x[1]);
		for(int i = 2; i + 1 < x.length; i += 2) {
			int tarCat = this.lp.getCategoryFromName(x[i]);
			int tarOpt = this.lp.getOptionFromName(tarCat, x[i+1]);
			
			restrictedCategoryOptions.add(new int[]{tarCat, tarOpt});
		}
	}

	public void setCategoryToRestrict(String categoryToRestrict) throws SetupException {
		this.categoryToRestrict = lp.getCategoryFromName(categoryToRestrict);
	}

	public void setOptionToRestrict(String optionToRestrict) throws SetupException {
		this.optionToRestrict = lp.getOptionFromName(categoryToRestrict, optionToRestrict);
	}
	
	public void addTargetOption(int categoryIndex, String targetOption) throws SetupException {
		restrictedCategoryOptions.add(new int[]{categoryIndex, lp.getOptionFromName(categoryIndex, targetOption)});
	}


	@Override
	public void apply() throws LogicException, SetupException {
		List<int[]> resOptList = new ArrayList<int[]>(restrictedCategoryOptions);
		
		for (int i = 0; i < resOptList.size(); i++) {
			int[] catOptIndex = resOptList.get(i);
			Option opt = lp.getOption(catOptIndex[0], catOptIndex[1]);
			
			for (int j = 0; j < resOptList.size(); j++) {
				if (i != j) {
					catOptIndex = resOptList.get(j);
					opt.declareMiss(catOptIndex[0], catOptIndex[1]);
				}
			}
		}
		
		Restriction r = new Restriction(restrictedCategoryOptions);
		lp.getOption(categoryToRestrict, optionToRestrict).addRestriction(r);
	}

	@Override
	public String buildRuleString() {
		String str = "";
		try {
			str +=  lp.getOptionName(categoryToRestrict, optionToRestrict)+ " from " + lp.getCategoryName(categoryToRestrict);
			str += " is either";
			for (int[] pair : restrictedCategoryOptions) {
				str += " " + lp.getOptionName(pair[0], pair[1]) + " from " + lp.getCategoryName(pair[0]);
			}
		} catch (SetupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return str;
	}

}
