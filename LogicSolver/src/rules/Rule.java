package rules;
import exceptions.SetupException;
import exceptions.SolvingException;
import objects.LogicPuzzle;

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
	 * @throws SolvingException
	 * @throws SetupException
	 */
	public void applyTo(LogicPuzzle lp) throws SolvingException, SetupException;
	
}
