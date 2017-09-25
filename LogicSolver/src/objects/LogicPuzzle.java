package objects;

import java.util.HashMap;
import java.util.Map;

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
		
		validateCategories();
	}
	
	private void validateCategories() {
		for(int i = 0; i < categoryNames.length; i++) {
			String[] split = categoryNames[i].split(" ");
			
			if(split.length > 1) {
				categoryNames[i] = split[0];
				categoryParams.put(split[0], new int[]{Integer.parseInt(split[1]), Integer.parseInt(split[2])});
			}
		}
	}

	public Option getOption(int categoryIndex, int optionIndex) {
		return options[categoryIndex][optionIndex];
	}

	public void setOption(int categoryIndex, int optionIndex, Option option) {
		options[categoryIndex][optionIndex] = option;
		
	}

	public String getOptionName(int categoryIndex, int optionIndex) {
		return optionNames[categoryIndex][optionIndex];
	}
	
	public String getCategoryName(int categoryIndex) {
		return categoryNames[categoryIndex];
	}
	
	public int getOptionFromName(int categoryIndex, String optName) {
		for(int i = 0; i < optionNames[categoryIndex].length; i++) {
			String name = optionNames[categoryIndex][i]; 
			if (name.compareTo(optName) == 0) 
				return i;
		}
		
		return -1;
	}
	
	public int getCategoryFromName(String categoryName) {
		for(int i = 0; i < categoryNames.length; i++) {
			String name = categoryNames[i]; 
			if (name.compareTo(categoryName) == 0) 
				return i;
		}
		
		return -1;
	}

	public int getOptionNum() {
		return optionNum;
	}
	
	public int[] getCategoryParams(String catName) {
		return categoryParams.get(catName);
	}
}
