package objects;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
		String str = "";
		
		str += "+";
		for(int i = 0; i < categoryNames.length - 1; i++) {
			for (int j = 0; j < optionNum; j++) {
				str += "-";
			}
			str += "+";
		}
		str += "\n";
		
		for (int cat1 = 0; cat1 < categoryNames.length - 1; cat1++) {
			for (int opt1 = 0; opt1 < optionNum; opt1++) {				
				Option option1 = options[cat1][opt1];
				for(int cat2 = categoryNames.length - 1; cat2 > cat1; cat2--) {
					str += "|";
					if (option1.getLink(cat2) != -1) {
						for (int opt2 = 0; opt2 < optionNum; opt2++) {
							if (option1.getLink(cat2) == opt2) 
								str += "O";
							else
								str += "X";
						}
					} else {
						Set<Integer> possibilities = option1.getPossibilities(cat2);
						
						for (int opt2 = 0; opt2 < optionNum; opt2++) {
							if (possibilities.contains(opt2)) 
								str += " ";
							else
								str += "X";
						}
					}
				}
				
				
				str += "|\n";
			}
			
			str += "+";
			for(int i = 0; i < categoryNames.length - cat1 - 1; i++) {
				for (int j = 0; j < optionNum; j++) {
					str += "-";
				}
				str += "+";
			}
			str += "\n";
		}
		
		System.out.println(str);
		
		
		/*str = "Reversed\n";
		
		str += "+";
		for(int i = 0; i < categoryNames.length - 1; i++) {
			for (int j = 0; j < optionNum; j++) {
				str += "-";
			}
			str += "+";
		}
		str += "\n";
		
		for (int cat1 = categoryNames.length - 1; cat1 > 0; cat1--) {
			for (int opt1 = 0; opt1 < optionNum; opt1++) {				
				Option option1 = options[cat1][opt1];
				for(int cat2 = 0; cat2 < cat1; cat2++) {
					str += "|";
					if(cat1 == 0 && opt1 == 2 && cat2 == 1) System.out.println(option1);
					if (option1.getLink(cat2) != -1) {
						for (int opt2 = 0; opt2 < optionNum; opt2++) {
							if (option1.getLink(cat2) == opt2) 
								str += "O";
							else
								str += "X";
						}
					} else {
						Set<Integer> possibilities = option1.getPossibilities(cat2);
						
						for (int opt2 = 0; opt2 < optionNum; opt2++) {
							if (possibilities.contains(opt2)) 
								str += " ";
							else
								str += "X";
						}
					}
				}
				
				
				str += "|\n";
			}
			
			str += "+";
			for(int i = 0; i < cat1; i++) {
				for (int j = 0; j < optionNum; j++) {
					str += "-";
				}
				str += "+";
			}
			str += "\n";
		}
			
		System.out.println(str);
		
		/*try {
			validate();
		} catch (SetupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	public int getCategoryNum() {
		return categoryNames.length;
	}
	
	public void validate() throws SetupException {
		for(Option[] cat1 : options) {
			for(Option opt1 : cat1) {
				for (int i = 0; i < options.length; i++) {
					if (opt1.getLink(i) == -1) {
						Set<Integer> possibilities = opt1.getPossibilities(i);
						
						for (int j = 0; j < cat1.length; j++) {
							if (possibilities.contains(j))
								for (int cat2 = 0; cat2 < options.length; cat2++) {
									if (opt1.getLink(cat2) != -1)
										if (!getOption(i,j).getPossibilities(cat2).contains(opt1.getLink(cat2))) {
											throw new SetupException("Possibility mismatch " + cat2 + " " + opt1.getLink(cat2) + " " + i + " " + j);
										}
								}
							else 
								for (int cat2 = 0; cat2 < options.length; cat2++) {
									if (opt1.getLink(cat2) != -1)
										if (getOption(i,j).getPossibilities(cat2) != null && getOption(i,j).getPossibilities(cat2).contains(opt1.getLink(cat2))) {
											throw new SetupException("Possibility mismatch " + cat2 + " " + opt1.getLink(cat2) + " " + i + " " + j);
										}
								}
						}
					} else {
						if (getOption(i, opt1.getLink(i)) != opt1) {
							throw new SetupException("Link not established " + i + " " + opt1.getLink(i));
						}
					}
				}
			}
		}
	}
}
