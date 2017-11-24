package rules;
import java.util.ArrayList;
import exceptions.LogicException;
import exceptions.SetupException;
import javafx.collections.ObservableList;
import objects.PuzzleLogic;

/**
 * This class parses a list of rules as strings and stores them
 * until asked to apply them to a logic board
 * 
 * @author matthiaswilder
 *
 */

public class RuleManager {
	
	private ArrayList<Rule> rules;
	private PuzzleLogic lp;
	
	public RuleManager(PuzzleLogic lp) {
		this.lp = lp;
		rules = new ArrayList<Rule>();
	}
	
	public RuleManager(String[] ruleList, PuzzleLogic lp) throws SetupException {
		this(lp);
		//for (String rule : ruleList) 
			 //addRule(rule);
	}

	/**
	 * constructs a new rule of specific type from a formatted string
	 * 
	 * @param ruleString
	 * @throws SetupException 
	 */
	
	/*public void addRule(String ruleString) throws SetupException {
		int type = Integer.parseInt(ruleString.substring(0,1));
		Rule rule = null;
		
		switch(type) {
		case 0:
			rule = new DeclarationRule(lp, ruleString.substring(2));
			break;
			
		case 1:
			rule = new RestrictionRule(lp, ruleString.substring(2));
			break;
			
		case 2:
			rule = new RelationRule(lp, ruleString.substring(2));
			break;
			
		case 3:
			rule = new DoubleRestrictionRule(lp, ruleString.substring(2));
			break;
		}
		
		rules.add(rule);
	}*/
	
	public void addRule(Rule rule) {
		rules.add(rule);
	}

	/**
	 * 
	 * @param lb
	 * @throws LogicException
	 * @throws SetupException
	 */

	public void applyRulesTo(PuzzleLogic lp) throws LogicException, SetupException {
		for (Rule r : rules) {
			if (r == null) continue;
			//r.apply();
			lp.printBoard();
		}
	}

	public void clear() {
		rules.clear();
	}

	public ArrayList<Rule> getRules() {
		return rules;
	}
}
