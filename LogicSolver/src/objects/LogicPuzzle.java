package objects;

import java.util.HashMap;
import java.util.Map;

import exceptions.SetupException;

public class LogicPuzzle {
	
	Option[][] options;
	String[][] optionNames;
	String[] categoryNames;
	Map<String, int[]> categoryParams;
	private int optionNum;
	
	public LogicPuzzle(String[] categoryNames, String[][] optionNames) {
		this.categoryNames = categoryNames;
		this.optionNames = optionNames;
		this.options = new Option[optionNames.length][optionNames[0].length];
		this.optionNum = this.options[0].length;
		this.categoryParams = new HashMap<String, int[]>();
		
		for (int i = 0; i < this.options.length; i++) {
			for (int j = 0; j < this.options[0].length; j++) {
				options[i][j] = new Option(this, options.length, options[0].length, i, j);
			}
		}
		
		try {
			validateCategories();
			validateOptions();
		} catch (SetupException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	private void validateOptions() throws SetupException {
		for(int i = 0; i < categoryNames.length; i++) {
			if (options[i].length != optionNum)
				throw new SetupException("option number mismatch for category: " + categoryNames[i] + 
						", " + optionNum + " options expected, " + options[i].length + " found");
		}
	}

	private void validateCategories() throws SetupException {
		for(int i = 0; i < categoryNames.length; i++) {
			String[] split = categoryNames[i].split(" ");
			
			if(split.length > 1) {
				try {
					categoryNames[i] = split[0];
					categoryParams.put(split[0], new int[]{Integer.parseInt(split[1]), Integer.parseInt(split[2])});
				} catch (NumberFormatException e) {
					throw new SetupException("parameters for category not parseable as integers: " + split[0]);
				} catch (IndexOutOfBoundsException e) {
					throw new SetupException("not enough parameters for category: " + split[0]);
				}
			}
		}
	}

	public Option getOption(int categoryIndex, int optionIndex) throws SetupException {
		try {
			return options[categoryIndex][optionIndex];
		} catch (IndexOutOfBoundsException e) {
			throw new SetupException("option number not found: " + optionIndex + " in category: " + categoryNames[categoryIndex]);
		}
	}

	public void setOption(int categoryIndex, int optionIndex, Option option) throws SetupException {
		try {
			options[categoryIndex][optionIndex] = option;
		} catch (IndexOutOfBoundsException e) {
			throw new SetupException("option number not found: " + optionIndex + " in category: " + categoryNames[categoryIndex]);

		}
	}

	public String getOptionName(int categoryIndex, int optionIndex) throws SetupException {
		try {
			return optionNames[categoryIndex][optionIndex];
		} catch (IndexOutOfBoundsException e) {
			throw new SetupException("option number not found: " + optionIndex + " in category: " + categoryNames[categoryIndex]);
		}
	}
	
	public String getCategoryName(int categoryIndex) {
		return categoryNames[categoryIndex];
	}
	
	public int getOptionFromName(int categoryIndex, String optName) throws SetupException {
		for(int i = 0; i < optionNames[categoryIndex].length; i++) {
			String name = optionNames[categoryIndex][i]; 
			if (name.compareTo(optName) == 0) 
				return i;
		}
		
		throw new SetupException("option not found: " + optName + " in category: " + categoryNames[categoryIndex]);
	}
	
	public int getCategoryFromName(String categoryName) throws SetupException {
		for(int i = 0; i < categoryNames.length; i++) {
			String name = categoryNames[i]; 
			if (name.compareTo(categoryName) == 0) 
				return i;
		}
		
		throw new SetupException("category not found: " + categoryName);
	}

	public int getOptionNum() {
		return optionNum;
	}
	
	public int[] getCategoryParams(String catName) throws SetupException {
		int[] params = categoryParams.get(catName);
		if (params != null)
			return params;
		else 
			throw new SetupException("category not found or does not have parameters: " + catName);
	}
	
	public void printBoard() {
		
	}
}
