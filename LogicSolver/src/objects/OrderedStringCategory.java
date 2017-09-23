package objects;
import exceptions.SetupException;

/**
 * This class defines the concept of a ordered category, ordered categories are 
 * numeric categories whose values are their place in the order even though each
 * option has a name
 * 
 * @author matthiaswilder
 *
 */

public class OrderedStringCategory extends NumericCategory {
	private String[] optNames;

	/**
	 * 
	 * @param name
	 * @param elementNumber
	 */
	
	public OrderedStringCategory(String name, int elementNumber) {
		super(name, elementNumber);
		optNames = new String[elementNumber];
	}
	
	@Override
	public int getOptionIndex(Object option) throws SetupException {
		return (Integer) option;
	}
	
	@Override
	public void addOption(Object name) throws SetupException {
		int i = 0;
		while (options[i] != null) {
			i++;
			
			if(i == options.length)
				throw(new SetupException("Options list for category: " + name + " already full"));
		}
		options[i] = i;
		optNames[i] = (String) name;
	} 
}
