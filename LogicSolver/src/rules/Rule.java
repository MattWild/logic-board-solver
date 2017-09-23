package rules;
import exceptions.SetupException;
import exceptions.SolvingException;
import objects.LogicBoard;

public interface Rule {
	
	public void applyTo(LogicBoard lb) throws SolvingException, SetupException;
	
}
