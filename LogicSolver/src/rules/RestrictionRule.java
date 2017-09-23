package rules;
import exceptions.SetupException;
import exceptions.SolvingException;
import objects.Category;
import objects.LogicBoard;

public class RestrictionRule implements Rule {
	
	String categoryToRestrict;
	String targetCategory;
	String optionToRestrict;
	String targetOption;

	public RestrictionRule(String string) {
		String[] x = string.split(" ");
		
		categoryToRestrict = x[0];
		targetCategory = x[1];
		optionToRestrict = x[2];
		targetOption = x[3];
	}

	@Override
	public void applyTo(LogicBoard lb) throws SolvingException, SetupException {
		Category catRes = lb.getCategory(categoryToRestrict);
		Category catTar = lb.getCategory(targetCategory);

		try {
			int optResIndex = catRes.getOptionIndex(optionToRestrict);
			int optTarIndex = catTar.getOptionIndex(targetOption);
		
			catRes.addRestriction(optResIndex, catTar, optTarIndex);
		} catch (SetupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
}

}
