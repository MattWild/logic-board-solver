package rules;
import exceptions.SetupException;
import exceptions.SolvingException;
import objects.Category;
import objects.LogicBoard;
import objects.SubBoard;

public class DelarationRule implements Rule {
	
	String category1;
	String category2;
	String option1;
	String option2;
	int value;
	
	public DelarationRule(String string) {
		String[] x = string.split(" ");
		
		category1 = x[0];
		category2 = x[1];
		option1 = x[2];
		option2 = x[3];
		value = Integer.parseInt(x[4]);
	}

	@Override
	public void applyTo(LogicBoard lb) throws SolvingException, SetupException {
		Category cat1 = lb.getCategory(category1);
		Category cat2 = lb.getCategory(category2);
		boolean flipAxes = false;
		
		SubBoard subBoard = cat1.getBoardWithCategory(cat2);
		if (subBoard == null) {
			subBoard = cat2.getBoardWithCategory(cat1);
			flipAxes = true;
		}
		
		subBoard.update(cat1.getOptionIndex(option1), cat2.getOptionIndex(option2), value, flipAxes);
	}

}
