package rules;
import java.util.ArrayList;
import exceptions.LogicException;
import exceptions.SetupException;
import objects.LogicPuzzle;

/**
 * This class parses a list of rules as strings and stores them
 * until asked to apply them to a logic board
 * 
 * @author matthiaswilder
 *
 */

public class RuleManager {
	
	ArrayList<Rule> rules;
	
	public RuleManager(String[] ruleList) {
		 rules = new ArrayList<Rule>();
		 for (String rule : ruleList) 
			 addRule(rule);
	}
	
	/**
	 * constructs a new rule of specific type from a formatted string
	 * 
	 * @param ruleString
	 */
	
	public void addRule(String ruleString) {
		int type = Integer.parseInt(ruleString.substring(0,1));
		Rule rule = null;
		
		switch(type) {
		case 0:
			rule = new DeclarationRule(ruleString.substring(2));
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
	
	/**
	 * 
	 * @param lb
	 * @throws LogicException
	 * @throws SetupException
	 */

	public void applyRulesTo(LogicPuzzle lp) throws LogicException, SetupException {
		for (Rule r : rules)
			r.applyTo(lp);
	}
}
