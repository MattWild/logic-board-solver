package objects;
import java.util.ArrayList;

import exceptions.SetupException;

/**
 * This class defines the concept of a string category, string categories have
 * strings for options
 * 
 * @author matthiaswilder
 *
 */

public class StringCategory extends Category{	
	
	/**
	 * 
	 * @param name
	 * @param elementNumber
	 */
	
	public StringCategory(String name, int elementNumber) {
		super(name, elementNumber);
	}
	
	@Override
	public  int getOptionIndex(Object option) throws SetupException {
		String targetStr = (String) option;
		for (int i = 0; i < options.length; i++) {
			String optionStr = (String) options[i];
			if (optionStr.compareTo(targetStr) == 0) {
				return i;
			}
		}
		throw(new SetupException("option: " + option + " not found in category: " + name));
	}

	@Override
	public void addOption(Object name) throws SetupException {
		int i = 0;
		while (options[i] != null) {
			i++;
			
			if(i == options.length)
				throw(new SetupException("Options list for category: " + name + " already full"));
		}
		options[i] = name;
	}
}
