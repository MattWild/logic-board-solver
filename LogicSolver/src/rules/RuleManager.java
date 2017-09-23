package rules;
import java.util.ArrayList;

import exceptions.SetupException;
import exceptions.SolvingException;
import objects.LogicBoard;

public class RuleManager {
	
	ArrayList<Rule> rules;
	
	public RuleManager(String[] ruleList) {
		 rules = new ArrayList<Rule>();
		 for (String rule : ruleList) 
			 addRule(rule);
	}
	
	public void addRule(String ruleString) {
		int type = Integer.parseInt(ruleString.substring(0,1));
		Rule rule = null;
		
		switch(type) {
		case 0:
			rule = new DelarationRule(ruleString.substring(2));
			break;
			
		case 1:
			rule = new RestrictionRule(ruleString.substring(2));
			break;
			
		case 2:
			rule = new RelationRule(ruleString.substring(2));
			break;
		}
		
		rules.add(rule);
	}

	public void applyRulesTo(LogicBoard lb) throws SolvingException, SetupException {
		for (Rule r : rules)
			r.applyTo(lb);
	}
}
