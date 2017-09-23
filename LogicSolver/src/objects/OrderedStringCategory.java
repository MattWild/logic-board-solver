package objects;
import exceptions.SetupException;

public class OrderedStringCategory extends NumericCategory {
	private String[] optNames;

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
