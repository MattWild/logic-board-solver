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

public class DoubleRestrictionRule implements Rule {
	
	private LogicPuzzle lp;
	private String[] categories1;
	private String[] options1;
	private String[] categories2;
	private String[] options2;
	private Rule[] subrules;
	

	public DoubleRestrictionRule(LogicPuzzle lp) {
		this.lp = lp;
		categories1 = new String[2];
		options1 = new String[2];
		categories2 = new String[2];
		options2 = new String[2];
	}

	public DoubleRestrictionRule(LogicPuzzle lp, String string) throws SetupException {
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
	}
	
	public void process() throws SetupException {
		RestrictionRule r1 = new RestrictionRule(lp,
				categories1[0] + " " + 
				options1[0] + " " + 
				categories2[0] +  " " + 
				options2[0] + " " + 
				categories2[1] + " " + 
				options2[1]);
		
		RestrictionRule r2 = new RestrictionRule(lp,
				categories1[1] + " " + 
				options1[1] + " " + 
				categories2[0] +  " " + 
				options2[0] + " " + 
				categories2[1] + " " + 
				options2[1]);
		
		RestrictionRule r3 = new RestrictionRule(lp,
				categories2[0] + " " + 
				options2[0] + " " + 
				categories1[0] +  " " + 
				options1[0] + " " + 
				categories1[1] + " " + 
				options1[1]);
		
		RestrictionRule r4 = new RestrictionRule(lp,
				categories2[1] + " " + 
				options2[1] + " " + 
				categories1[0] +  " " + 
				options1[0] + " " + 
				categories1[1] + " " + 
				options1[1]);
		
		subrules = new Rule[]{r1,r2,r3,r4};
	}

	public void setFirstCategoryInFirstPair(String category) {
		categories1[0] = category;
	}
	
	public void setSecondCategoryInFirstPair(String category) {
		categories1[1] = category;
	}
	
	public void setFirstCategoryInSecondPair(String category) {
		categories2[0] = category;
	}
	
	public void setSecondCategoryInSecondPair(String category) {
		categories2[1] = category;
	}
	
	public void setFirstOptionInFirstPair(String option) {
		options1[0] = option;
	}
	
	public void setSecondOptionInFirstPair(String option) {
		options1[1] = option;
	}
	
	public void setFirstOptionInSecondPair(String option) {
		options2[0] = option;
	}
	
	public void setSecondOptionInSecondPair(String option) {
		options2[1] = option;
	}

	@Override
	public void apply() throws LogicException, SetupException {
		for(Rule r : subrules) 
			r.apply();
	}

}
