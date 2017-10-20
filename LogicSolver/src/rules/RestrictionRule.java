package rules;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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
	
	String categoryToRestrict;
	ArrayList<String> targetCategories;
	String optionToRestrict;
	ArrayList<String> targetOptions;

	/**
	 * 
	 * @param string
	 */
	
	public RestrictionRule(String string) {
		targetCategories = new ArrayList<String>();
		targetOptions = new ArrayList<String>();
		
		String[] x = string.split(" ");
		
		categoryToRestrict = x[0];
		optionToRestrict = x[1];
		for(int i = 2; i + 1 < x.length; i += 2) {
			targetCategories.add(x[i]);
			targetOptions.add(x[i+1]);
		}
		
	}
	

	@Override
	public void applyTo(LogicPuzzle lp) throws LogicException, SetupException {
		int catRes = lp.getCategoryFromName(categoryToRestrict);
		int optRes = lp.getOptionFromName(catRes, optionToRestrict);
		
		Set<int[]> restrictedOptions = new HashSet<int[]>();
		
		for (int i = 0; i < targetCategories.size(); i++) {
			int tarCat = lp.getCategoryFromName(targetCategories.get(i));
			int tarOpt = lp.getOptionFromName(tarCat, targetOptions.get(i));
			
			restrictedOptions.add(new int[]{tarCat, tarOpt});
		}
		
		List<int[]> resOptList = new ArrayList<int[]>(restrictedOptions);
		
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
		
		Restriction r = new Restriction(restrictedOptions);
		lp.getOption(catRes, optRes).addRestriction(r);
	}

}
