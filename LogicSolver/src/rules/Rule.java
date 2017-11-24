package rules;
import exceptions.LogicException;
import exceptions.SetupException;
import objects.PuzzleLogic;
import presentation.MainApp;

/**
 * This interface defines a simple framework rules (hints) that
 * define the initial setup of the logic puzzle
 * 
 * @author matthiaswilder
 *
 */

public interface Rule {
	
	/**
	 * applies rule to a logic board for setup of logic puzzle
	 * 
	 * @param lb
	 * @throws LogicException
	 * @throws SetupException
	 */
	public void apply(PuzzleLogic logic) throws LogicException, SetupException;
	
	public String buildRuleString(MainApp mainApp);
	
}
